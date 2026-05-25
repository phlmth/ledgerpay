package io.github.phlmth.ledgerpay.domain.money;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MoneyTest {
    @Test
    void shouldCreateMoneyWithTwoDecimalPlaces() {
        var expectedAmount = new BigDecimal("9.50");

        Money money = Money.of("9.50");

        assertThat(money.amount()).isEqualTo(expectedAmount);
    }

    @Test
    void shouldNormalizeValueWithoutDecimalPlaces() {
        var expectedAmount = new BigDecimal("9.00");

        Money money = Money.of("9");

        assertThat(money.amount()).isEqualTo(expectedAmount);
    }

    @Test
    void shouldNormalizeAmountCreatedThroughCanonicalConstructor() {
        var expectedAmount = new BigDecimal("9.00");

        Money money = new Money(new BigDecimal("9"));

        assertThat(money.amount()).isEqualTo(expectedAmount);
    }

    @Test
    void shouldNormalizeValueWithOneDecimalPlace() {
        var expectedAmount = new BigDecimal("9.50");

        Money money = Money.of("9.5");

        assertThat(money.amount()).isEqualTo(expectedAmount);
    }

    @Test
    void shouldNormalizeAmountWithTrailingFractionalZeros() {
        var expectedAmount = new BigDecimal("10.00");

        Money money = Money.of("10.000");

        assertThat(money.amount()).isEqualTo(expectedAmount);
    }


}
