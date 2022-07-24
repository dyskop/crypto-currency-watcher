package by.skopinau.cryptocurrencywatcher.service.client;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dto.CurrencyRequest;
import by.skopinau.cryptocurrencywatcher.exception.CoinLoreResponseException;
import by.skopinau.cryptocurrencywatcher.mapper.CurrencyMapper;
import by.skopinau.cryptocurrencywatcher.service.client.impl.CurrencyClientImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

class CurrencyClientTests {
    public static MockWebServer mockBackEnd;

    private CurrencyClient currencyClient;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void init() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        WebClient webClient = WebClient.create(baseUrl);
        currencyClient = new CurrencyClientImpl(webClient);
    }

    @Test
    void testGetActualCurrency_whenApiGivesCorrectResponse_thenReturnCurrency() throws JsonProcessingException, CoinLoreResponseException, InterruptedException {
        // given
        Currency expected = new Currency(90L, "BTC", 6465.26);
        CurrencyRequest currencyRequest = CurrencyMapper.INSTANCE.currencyEntityToCurrencyRequest(expected);
        CurrencyRequest[] currencyRequests = new CurrencyRequest[] {currencyRequest};
        mockBackEnd.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(currencyRequests))
                .addHeader("Content-Type", "application/json"));

        // when
        Currency actual = currencyClient.getActualCurrency(90L);

        // then
        Assertions.assertEquals(expected, actual);

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/api/ticker/?id=90", recordedRequest.getPath());
    }
    // TODO: write test for CoinLoreResponseException throwing
}
