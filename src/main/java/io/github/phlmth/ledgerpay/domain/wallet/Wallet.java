package io.github.phlmth.ledgerpay.domain.wallet;

import io.github.phlmth.ledgerpay.domain.money.Money;
import java.util.Objects;

public class Wallet {
  private Money balance;

  public Wallet(Money balance) {
    Objects.requireNonNull(balance);

    if (balance.isLessThan(Money.of("0.00"))) {
      throw new IllegalArgumentException();
    }

    this.balance = balance;
  }

  public Wallet() {
    this(Money.of("0.00"));
  }

  public Money balance() {
    return this.balance;
  }

  public void credit(Money amount) {
    if (!amount.isPositive()) {
      throw new IllegalArgumentException();
    }

    balance = balance.add(amount);
  }

  public void debit(Money amount) {
    if (!amount.isPositive()) {
      throw new IllegalArgumentException();
    }

    if (balance.isLessThan(amount)) {
      throw new IllegalStateException();
    }

    balance = balance.subtract(amount);
  }
}
