package by.skopinau.cryptocurrencywatcher.service.client;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dto.CurrencyRequest;
import by.skopinau.cryptocurrencywatcher.exception.CoinLoreResponseException;
import by.skopinau.cryptocurrencywatcher.exception.message.CoinLoreResponseMessage;
import by.skopinau.cryptocurrencywatcher.mapper.CurrencyMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CurrencyClient {
    private final WebClient webClient;

    public CurrencyClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Currency getActualCurrency(long id) throws CoinLoreResponseException {
        CurrencyRequest[] currencyRequests = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/ticker/")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .bodyToMono(CurrencyRequest[].class)
                .onErrorResume(throwable -> Mono.error(new CoinLoreResponseException(CoinLoreResponseMessage.SERVER_IS_NOT_RESPONDING.getMessage())))
                .block();

        assert currencyRequests != null;
        return CurrencyMapper.INSTANCE.currencyRequestToCurrencyEntity(currencyRequests[0]);
    }
}
