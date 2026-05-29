package io.github.phlmth.ledgerpay.domain.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.money.Money;
import org.junit.jupiter.api.Test;

class WalletTest {

  @Test
  void shouldRejectDebitWhenBalanceIsInsufficient() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.debit(Money.of("150.00")))
        .isInstanceOf(IllegalStateException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }
}
