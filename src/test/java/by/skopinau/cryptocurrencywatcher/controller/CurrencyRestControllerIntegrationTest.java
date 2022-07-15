package by.skopinau.cryptocurrencywatcher.controller;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dal.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CurrencyRepository currencyRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        currencyRepository.deleteAll();
    }

    @Test
    public void givenListOfCurrencies_whenGetAvailableCurrencies_thenReturnCurrenciesList()
            throws Exception {
        // given
        List<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(90L, "BTC", 123.12));
        currencies.add(new Currency(80L, "ETH", 10.10));
        currencies.add(new Currency(4L, "SOL", 987654.98));
        currencyRepository.saveAll(currencies);

        // when
        ResultActions response = mockMvc.perform(get("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(currencies.size())))
                .andExpect(jsonPath("$[0].id", is(90)))
                .andExpect(jsonPath("$[1].id", is(80)))
                .andExpect(jsonPath("$[2].id", is(4)))
                .andExpect(jsonPath("$[0].symbol", is("BTC")))
                .andExpect(jsonPath("$[1].symbol", is("ETH")))
                .andExpect(jsonPath("$[2].symbol", is("SOL")));
    }

    @Test
    public void givenCurrencySymbol_whenGetActualCurrencyPrice_thenReturnCurrencyPriceObject()
            throws Exception {
        // given
        Currency currency = currencyRepository.save(new Currency(90L, "BTC", 123.12));

        // when
        ResultActions response = mockMvc.perform(get("/api/currencies/").param("symbol", currency.getSymbol())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(currency.getPriceUsd())));
    }

    @Test
    public void givenCurrencySymbol_whenGetActualCurrencyPrice_thenReturnCurrencyPriceEmpty()
            throws Exception {
        // given
        String symbol = "ETH";
        currencyRepository.save(new Currency(90L, "BTC", 123.12));

        // when
        ResultActions response = mockMvc.perform(get("/api/currencies/").param("symbol", symbol)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
    }
}