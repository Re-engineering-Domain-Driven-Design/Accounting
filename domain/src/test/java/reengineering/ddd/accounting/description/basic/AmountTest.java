package reengineering.ddd.accounting.description.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AmountTest {

    @Test
    public void should_sum_amount() {
        assertEquals(Amount.cny("600.00"), Amount.sum(Amount.cny("100.00"), Amount.cny("200.00"), Amount.cny("300.00")));
    }

    @Test
    public void should_throw_exception_if_different_currencies_given() {
        assertThrows(IllegalArgumentException.class, () ->
                Amount.sum(Amount.cny("100.00"), Amount.usd("100.00")));
    }
}
