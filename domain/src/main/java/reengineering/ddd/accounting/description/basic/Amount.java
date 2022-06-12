package reengineering.ddd.accounting.description.basic;

import java.math.BigDecimal;

public record Amount(BigDecimal value, Currency currency) {
    public static Amount cny(String value) {
        return new Amount(new BigDecimal(value), Currency.CNY);
    }
}
