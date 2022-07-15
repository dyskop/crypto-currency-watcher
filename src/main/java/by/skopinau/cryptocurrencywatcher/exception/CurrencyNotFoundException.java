package by.skopinau.cryptocurrencywatcher.exception;

public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException() {
        super("Could not find any currency");
    }

    public CurrencyNotFoundException(long id) {
        super("Could not find currency " + id);
    }

    public CurrencyNotFoundException(String symbol) {
        super("Could not find currency " + symbol);
    }
}