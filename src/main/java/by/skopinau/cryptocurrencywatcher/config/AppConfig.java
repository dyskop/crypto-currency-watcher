package by.skopinau.cryptocurrencywatcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    public static final long TIME_INTERVAL = 60000;
    public static final String URL = "https://api.coinlore.net/api/ticker/?id=%d";

    // TODO: change to WebClient
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
