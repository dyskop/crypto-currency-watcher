package by.skopinau.cryptocurrencywatcher.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyMessage {
    CURRENCY_NOT_FOUND("Could not find any currency"),
    CURRENCY_NOT_FOUND_BY_ID("Could not find currency %d"),
    CURRENCY_NOT_FOUND_BY_SYMBOL("Could not find currency %s");

    private final String message;
}
