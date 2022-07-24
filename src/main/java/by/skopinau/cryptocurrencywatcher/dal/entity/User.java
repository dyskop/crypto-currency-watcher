package by.skopinau.cryptocurrencywatcher.dal.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "price")
    private Double price;

    public User() {
    }

    public User(String username, String symbol, Double price) {
        this.username = username;
        this.symbol = symbol;
        this.price = price;
    }

    public User(UUID id, String username, String symbol, Double price) {
        this.id = id;
        this.username = username;
        this.symbol = symbol;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!username.equals(user.username)) return false;
        if (!symbol.equals(user.symbol)) return false;
        return price.equals(user.price);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + symbol.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                '}';
    }
}
