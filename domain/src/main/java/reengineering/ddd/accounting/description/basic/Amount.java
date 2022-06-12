package reengineering.ddd.accounting.description.basic;

import java.math.BigDecimal;

public record Amount(BigDecimal value, Currency currency) {
}
