package by.skopinau.cryptocurrencywatcher.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoinLoreResponseMessage {
    SERVER_IS_NOT_RESPONDING("Coin lore is not responding to get the current exchange rate");

    private final String message;
}
