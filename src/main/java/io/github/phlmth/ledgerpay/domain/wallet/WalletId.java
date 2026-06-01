package io.github.phlmth.ledgerpay.domain.wallet;

import java.util.Objects;
import java.util.UUID;

public record WalletId(String id) {

  public WalletId {
    Objects.requireNonNull(id);

    if (id.isBlank()) {
      throw new IllegalArgumentException();
    }
  }

  public WalletId() {
    this(UUID.randomUUID().toString());
  }

  public static WalletId of(String id) {
    return new WalletId(id);
  }
}
