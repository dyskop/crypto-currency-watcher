package by.skopinau.cryptocurrencywatcher.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "price_usd")
    private Double priceUsd;

    public Currency() {
    }

    public Currency(Long id, String symbol, Double priceUsd) {
        this.id = id;
        this.symbol = symbol;
        this.priceUsd = priceUsd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(Double priceUsd) {
        this.priceUsd = priceUsd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;

        Currency currency = (Currency) o;

        if (!id.equals(currency.id)) return false;
        if (!symbol.equals(currency.symbol)) return false;
        return Objects.equals(priceUsd, currency.priceUsd);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + symbol.hashCode();
        result = 31 * result + (priceUsd != null ? priceUsd.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", priceUsd=" + priceUsd +
                '}';
    }
}
