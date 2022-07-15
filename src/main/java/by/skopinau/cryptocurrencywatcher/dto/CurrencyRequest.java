package by.skopinau.cryptocurrencywatcher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRequest {
    @JsonProperty
    private long id;

    @JsonProperty
    private String symbol;

    @JsonProperty("price_usd")
    private double priceUsd;
}
