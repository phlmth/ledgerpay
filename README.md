# LedgerPay

> Java backend sandbox for modeling digital wallets, money movement, and financial domain rules.

LedgerPay is a small backend project focused on monetary correctness, explicit domain modeling, and automated validation for financial operations.

The project currently models wallet funding and peer-to-peer transfers entirely in memory. It is intentionally framework-free while the core financial domain is being shaped and tested.

## Current Scope

LedgerPay currently supports:

- monetary values through a `Money` Value Object;
- stable wallet identities through `WalletId`;
- wallets with protected balance invariants;
- an explicit `SystemTreasury` funding source;
- wallet funding through `TreasuryFunding`;
- peer-to-peer wallet transfers through `PeerTransfer`;
- domain-specific exceptions for business rule violations;
- automated tests for implemented behavior.

The current financial flows are:

```text
SystemTreasury → TreasuryFunding → Wallet
Wallet         → PeerTransfer    → Wallet
```

## Documentation

- [Project definition](docs/00-project-definition.md)

## Tech Stack

- Java 21
- Maven
- Maven Wrapper
- JUnit 5
- AssertJ
- Spotless
- `google-java-format`
- GitHub Codespaces dev container
- Optional Makefile shortcuts

## Running Tests

Linux/macOS:

```bash
./mvnw test
```

Windows:

```powershell
.\mvnw.cmd test
```

## Local Validation

The Maven Wrapper is the canonical way to run the project.

For environments with `make` available, the repository also provides shortcuts:

| Command | Description |
| --- | --- |
| `make help` | Lists available shortcuts. |
| `make test` | Runs the test suite. |
| `make verify` | Runs tests and formatting checks. |
| `make clean` | Removes Maven build artifacts. |
| `make format` | Applies Java formatting. |
| `make format-check` | Checks Java formatting. |

Before review or merge, run:

```bash
make verify
```

## Domain Rules

LedgerPay currently enforces the following rules:

- monetary values use two decimal places;
- real fractions of cents are rejected;
- wallets start with zero balance by default;
- wallets cannot be created with negative balance;
- credits and debits require positive amounts;
- debits cannot exceed available balance;
- treasury funding requires a positive amount;
- treasury funding cannot exceed treasury funds;
- peer transfers require different wallet identities;
- peer transfers cannot exceed the source wallet balance;
- failed operations preserve existing balances.

## Exception Model

Business rule violations use domain-specific unchecked exceptions:

- `InvalidWalletBalanceException`
- `InvalidMoneyMovementAmountException`
- `InsufficientBalanceException`
- `SameWalletTransferException`

Structural contract failures, such as `null` arguments or invalid Value Object input, still use standard Java exceptions.

## Not Yet Included

LedgerPay does not yet include:

- REST API;
- Spring Boot application layer;
- persistence;
- repositories;
- authentication or authorization;
- idempotency;
- transaction boundaries;
- formal ledger;
- double-entry accounting;
- HTTP error mapping.

These omissions are intentional. The project is first establishing a small, well-tested financial domain before adding infrastructure concerns.

## Roadmap Candidates

Future stages may include:

- application service boundaries;
- persistence with PostgreSQL;
- REST API exposure;
- idempotent money movement operations;
- ledger entries;
- balance derivation from ledger history;
- double-entry accounting;
- structured API error responses.
