package by.skopinau.cryptocurrencywatcher.service.util;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dto.CurrencyRequest;
import by.skopinau.cryptocurrencywatcher.mapper.CurrencyMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CurrencyClient {
    private final WebClient webClient;

    public CurrencyClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Currency getActualCurrency(long id) {
        CurrencyRequest[] currencyRequests = null;

        while (currencyRequests == null) {
            currencyRequests = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/ticker/")
                            .queryParam("id", id)
                            .build())
                    .retrieve()
                    .bodyToMono(CurrencyRequest[].class)
                    .block();
        }

        return CurrencyMapper.INSTANCE.currencyRequestToCurrencyEntity(currencyRequests[0]);
    }
}
