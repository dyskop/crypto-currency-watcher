package by.skopinau.cryptocurrencywatcher.service.util;

import by.skopinau.cryptocurrencywatcher.config.AppConfig;
import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dto.CurrencyRequest;
import by.skopinau.cryptocurrencywatcher.mapper.CurrencyMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CurrencyClient {
    private final RestTemplate restTemplate;

    public CurrencyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Currency getActualCurrency(long id) {
        CurrencyRequest[] template = null;
        while (template == null) {
            template = restTemplate.getForObject(String.format(AppConfig.URL, id), CurrencyRequest[].class);
        }
        return CurrencyMapper.INSTANCE.currencyRequestToCurrencyEntity(template[0]);
    }
}
