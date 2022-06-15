package reengineering.ddd.accounting.description.basic;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record Amount(BigDecimal value, Currency currency) {
    public static Amount cny(String value) {
        return new Amount(new BigDecimal(value), Currency.CNY);
    }
    public static Amount usd(String value) {
        return new Amount(new BigDecimal(value), Currency.USD);
    }

    public static Amount sum(Amount... amounts) {
        Currency[] currencies = Arrays.stream(amounts).map(Amount::currency).collect(Collectors.toSet())
                .toArray(Currency[]::new);
        if (currencies.length > 1) throw new IllegalArgumentException();
        BigDecimal sum = Arrays.stream(amounts).map(Amount::value).reduce(new BigDecimal(0), BigDecimal::add);
        return new Amount(sum, currencies[0]);
    }
}
