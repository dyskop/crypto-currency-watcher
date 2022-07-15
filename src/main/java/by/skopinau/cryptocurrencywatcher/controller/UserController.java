package by.skopinau.cryptocurrencywatcher.controller;

import by.skopinau.cryptocurrencywatcher.dal.entity.User;
import by.skopinau.cryptocurrencywatcher.exception.CurrencyNotFoundException;
import by.skopinau.cryptocurrencywatcher.service.CurrencyService;
import by.skopinau.cryptocurrencywatcher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final CurrencyService currencyService;
    private final UserService userService;

    @Autowired
    public UserController(CurrencyService currencyService, UserService userService) {
        this.currencyService = currencyService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> notify(@RequestParam String username,
                                       @RequestParam String symbol) {
        ResponseEntity<User> response = ResponseEntity.badRequest().build();

        try {
            long id = currencyService.findBySymbol(symbol).getId();
            double price = currencyService.getPrice(id);
            User user = new User(username, symbol, price);

            return ResponseEntity.ok(userService.save(user));
        } catch (CurrencyNotFoundException e) {
            e.printStackTrace();
        }

        return response;
    }
}
