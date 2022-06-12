package reengineering.ddd.accounting.description;

import reengineering.ddd.accounting.description.basic.Amount;

import java.time.LocalDateTime;

public record TransactionDescription(Amount amount, LocalDateTime createdAt) {
}
