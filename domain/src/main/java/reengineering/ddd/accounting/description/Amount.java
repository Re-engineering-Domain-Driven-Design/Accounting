package reengineering.ddd.accounting.description;

import java.math.BigDecimal;

public record Amount(BigDecimal value, Currency currency) {
}
