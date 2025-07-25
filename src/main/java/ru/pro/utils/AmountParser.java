package ru.pro.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmountParser {
    public BigDecimal parse(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректная сумма: " + amount);
        }
    }
}
