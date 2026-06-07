# LedgerPay — Project Definition

## Project Summary

LedgerPay is a Java backend sandbox for modeling digital wallets, money movement, and financial domain rules.

## Main Objective

The main objective is to build a backend system that demonstrates:

- monetary correctness;
- explicit domain modeling;
- automated testing;
- safe financial operations;
- readiness for future persistence, APIs, idempotency, and ledger-based accounting.

## Current Stage

The project has completed its technical foundation and currently implements two in-memory financial flows:

```text
SystemTreasury → TreasuryFunding → Wallet
Wallet         → PeerTransfer    → Wallet
```

Currently implemented:

- `Money` as a monetary Value Object;
- `WalletId` as a wallet identity Value Object;
- `Wallet` as a mutable in-memory wallet with stable identity;
- `SystemTreasury` as an explicit institutional funding source;
- `TreasuryFunding` as a domain operation for funding wallets;
- `PeerTransfer` as a domain operation for wallet-to-wallet transfers;
- domain-specific exceptions for meaningful business rule failures.

## Current Domain Model

### `Money`

`Money` represents monetary amounts.

Current responsibilities:

- wrap `BigDecimal`;
- normalize values to two decimal places;
- reject real fractions of cents;
- reject `null`;
- allow zero and negative monetary values as representable amounts;
- support addition and subtraction;
- expose positivity and comparison operations needed by domain flows.

`Money` does not decide whether a financial operation is valid. Operational rules belong to the domain behavior that performs the operation.

### `WalletId`

`WalletId` represents the stable identity of a wallet.

Current responsibilities:

- model wallet identity as a Value Object;
- reject `null`;
- reject blank or empty identifiers;
- support explicit identity through `WalletId.of(...)`;
- support generated identity through `WalletId.newId()`;
- compare identity by value.

The current implementation uses a `String` internally and generates new identifiers using UUID-backed string values. This is a minimal identity model and does not yet imply a final persistence format.

### `Wallet`

`Wallet` represents a common wallet with stable identity and mutable in-memory balance.

Current responsibilities:

- start with zero balance by default;
- generate a wallet identity for new wallets;
- preserve explicit wallet identity when provided;
- reject `null` identity in the explicit constructor;
- reject `null` initial balance;
- reject negative initial balance;
- allow positive credits;
- reject zero or negative credits;
- allow positive debits when balance is sufficient;
- reject zero or negative debits;
- reject debits above available balance;
- allow debiting the exact available balance.

`Wallet` currently stores direct balance state. This is intentional for the current stage and may evolve when ledger-based accounting is introduced.

### `SystemTreasury`

`SystemTreasury` represents the system's institutional funding source.

Current responsibilities:

- start with a fixed fictitious balance of `1000000.00`;
- expose its current balance;
- allow protected debit through its internal funds;
- act as the only valid source type for treasury funding.

`SystemTreasury` is implemented using composition rather than inheritance from `Wallet`.

### `TreasuryFunding`

`TreasuryFunding` represents the operation of funding a common wallet from the system treasury.

Current responsibilities:

- require `SystemTreasury` as the funding source;
- require a destination `Wallet`;
- require a positive `Money` amount;
- reject zero and negative funding amounts;
- reject funding above treasury funds;
- debit the treasury and credit the destination wallet;
- preserve balances when funding is rejected.

### `PeerTransfer`

`PeerTransfer` represents the operation of transferring money between two wallets.

Current responsibilities:

- require a source `Wallet`;
- require a destination `Wallet`;
- reject transfers between wallets with the same identity;
- debit the source wallet;
- credit the destination wallet;
- reject zero and negative transfer amounts;
- reject transfers above the source wallet balance;
- preserve balances when a transfer is rejected.

The same-wallet rule is based on `WalletId`, not on in-memory reference equality.

## Current Exception Model

The domain distinguishes business rule violations from structural contract failures.

Business rule violations use domain-specific unchecked exceptions:

| Exception | Meaning |
| --- | --- |
| `InvalidWalletBalanceException` | A wallet balance violates a wallet invariant. |
| `InvalidMoneyMovementAmountException` | A money movement uses an amount that is not valid for the operation. |
| `InsufficientBalanceException` | A debit-like operation exceeds available balance. |
| `SameWalletTransferException` | A peer transfer uses the same wallet identity as source and destination. |

Structural contract failures still use standard Java exceptions for now:

- `NullPointerException` for `null` arguments;
- `IllegalArgumentException` for structurally invalid Value Object input, such as blank identifiers or invalid monetary scale.

No HTTP mapping, API error format, `ControllerAdvice`, error codes, or Problem Details model exists yet.

## Current Technical Foundation

The project currently uses:

- Java 21;
- Maven;
- Maven Wrapper;
- JUnit 5;
- AssertJ;
- Spotless;
- `google-java-format`;
- GitHub Codespaces with Java 21;
- optional Makefile shortcuts for local development.

## Validation Strategy

The project uses automated tests to drive and protect domain behavior.

Current validation includes:

- monetary invariants in `Money`;
- wallet identity rules in `WalletId`;
- wallet balance and mutation rules in `Wallet`;
- treasury initialization in `SystemTreasury`;
- valid and invalid funding flows in `TreasuryFunding`;
- valid and invalid peer transfer flows in `PeerTransfer`;
- domain-specific exception types for business rule failures;
- formatting validation through Spotless;
- full local validation through `make verify`.

## Current Scope

The current scope includes:

- in-memory domain modeling;
- monetary correctness for basic operations;
- wallet identity;
- wallet balance protection;
- treasury-based funding;
- peer-to-peer wallet transfer;
- domain-specific exceptions for existing business rules;
- automated tests for implemented behavior.

## Out of Scope for the Current Stage

The following concerns are intentionally out of scope for now:

- REST API;
- Spring Boot application layer;
- PostgreSQL persistence;
- repositories;
- authentication;
- authorization;
- idempotency;
- distributed transactions;
- formal ledger;
- double-entry accounting;
- users and wallet ownership;
- production-grade treasury lifecycle management;
- HTTP error mapping;
- global exception handling;
- structured error codes;
- internationalized error messages.

## Known Limitations

The current implementation has the following known limitations:

- `Wallet.credit(...)` and `Wallet.debit(...)` are still publicly callable.
- `SystemTreasury.debit(...)` is still publicly callable.
- Multiple `SystemTreasury` instances can be created in memory.
- `Wallet(Money balance)` remains public while creation versus rehydration has not yet been formally modeled.
- Treasury funding still performs explicit amount validation before delegating to wallet debit/credit behavior.
- Balances are stored directly instead of being derived from ledger entries.
- Domain exception messages are intentionally simple and are not treated as public API contracts.
- The project has no application service boundary yet.
- There is no persistence, transaction boundary, API, or ledger.

These limitations are intentional at this stage and should be revisited only when additional flows, application services, persistence, or ledger modeling create real design pressure.

## Roadmap Candidates

### Application Service Boundary

Possible concerns:

- coordinate domain operations from an application-level use case;
- control direct mutation access;
- prepare the project for API and persistence;
- define request and response boundaries without introducing Spring prematurely.

### Ledger Modeling

Possible concerns:

- replace direct balance as the only source of financial truth;
- introduce append-only entries;
- derive balances from movement history;
- support auditability and reversals;
- evaluate double-entry accounting.

### Persistence Preparation

Possible concerns:

- distinguish wallet creation from rehydration;
- define repository boundaries;
- decide persistence identity representation;
- preserve domain invariants when loading existing state.

### API Layer

Possible concerns:

- expose domain use cases through HTTP;
- map domain exceptions to HTTP responses;
- introduce error response contracts;
- evaluate Spring Boot integration.

## Future Stages

Later stages may include:

- users and wallet ownership;
- application services;
- persistence with PostgreSQL;
- REST API;
- idempotent operations;
- ledger entries;
- double-entry accounting;
- custom API error model;
- observability and structured logs.
