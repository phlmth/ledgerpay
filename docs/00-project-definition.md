# LedgerPay — Definição Inicial do Projeto

## Propósito

O **LedgerPay** é um backend Java sandbox para carteiras digitais e transferências, concebido para simular movimentações financeiras entre carteiras em um ambiente controlado. O projeto será desenvolvido incrementalmente, com foco técnico em evoluir um domínio financeiro de forma consistente, rastreável e testável. Trata-se de uma aplicação de demonstração: não opera dinheiro real nem possui integração com instituições bancárias.

## Cenário Sandbox

- O cenário considera usuários comuns que poderão participar de movimentações financeiras simuladas.
- Cada usuário terá uma única carteira denominada em **BRL**, criada inicialmente com saldo zero.
- O sistema possuirá uma carteira interna denominada `SYSTEM_TREASURY`, responsável por representar a tesouraria do ambiente sandbox.
- A `SYSTEM_TREASURY` iniciará com **BRL 1.000.000,00** fictícios e será a origem controlada dos valores distribuídos no ambiente.
- O sistema deverá permitir funding sandbox da tesouraria para carteiras comuns e transferências entre carteiras de usuários.
- Enquanto mecanismos adequados de segurança não forem introduzidos, as operações serão destinadas exclusivamente a execução local e demonstração controlada.

## Escopo do MVP

O MVP deverá entregar as seguintes capacidades:

- Cadastro mínimo de usuários com nome e email.
- Normalização e unicidade do email cadastrado.
- Criação automática de uma carteira BRL com saldo zero para cada usuário.
- Inicialização da carteira interna `SYSTEM_TREASURY`.
- Funding sandbox da tesouraria para carteiras de usuários.
- Consulta de saldo de uma carteira.
- Transferência entre carteiras de usuários.
- Histórico operacional de movimentações concluídas.
- Validação de valor positivo, saldo suficiente e transferência para carteira diferente da origem.
- Persistência transacional quando a infraestrutura de banco de dados for introduzida.

## Decisões Atuais

| Decisão | Justificativa |
|---|---|
| Java 21 como versão principal | Permite desenvolver o projeto sobre uma versão moderna da linguagem e consolidar práticas atuais do ecossistema Java. |
| BRL como única moeda no MVP | Mantém o domínio inicial concentrado em movimentações, sem introduzir regras de câmbio ou conversão monetária. |
| `Money` sem campo `currency` inicialmente | Como o MVP opera somente em BRL, armazenar a moeda no objeto de valor adicionaria complexidade sem necessidade imediata. |
| Uma carteira por usuário | Reduz ambiguidades do domínio inicial e simplifica cadastro, consulta de saldo e transferências. |
| `SYSTEM_TREASURY` como origem de fundos | Permite distribuir valores fictícios de maneira explícita e controlada, evitando criação de saldo sem origem modelada. |
| Saldo persistido na carteira na primeira implementação | Mantém o primeiro ciclo pequeno e observável, permitindo compreender e testar o fluxo básico antes de adotar um modelo contábil mais sofisticado. |
| Desenvolvimento incremental orientado por testes | Permite proteger comportamentos já definidos e refatorar o domínio com maior segurança à medida que novas necessidades surgirem. |
| Ausência de autenticação no primeiro incremento local | O primeiro objetivo é consolidar as regras do domínio em ambiente controlado, antes de expor operações protegidas por identidade e autorização. |

## Limitações Aceitas

- O MVP inicial adotará o **saldo persistido na carteira** como fonte da verdade. Essa escolha reduz a complexidade do primeiro ciclo de desenvolvimento e poderá ser revisada quando auditoria detalhada ou reconstrução de saldo se tornarem requisitos relevantes.
- O histórico inicial será um **histórico operacional de movimentações**, destinado a registrar operações concluídas. Ele não representará, nesta fase, um ledger contábil formal.
- A primeira implementação não adotará **double-entry**. A modelagem de débitos e créditos balanceados será avaliada em uma evolução posterior do domínio financeiro.
- O primeiro incremento local não terá autenticação ou autorização. Consequentemente, sua execução será restrita a ambiente de desenvolvimento e demonstração, sem pretensão de exposição pública segura.
- O funding originado pela `SYSTEM_TREASURY` será exclusivamente um mecanismo sandbox para disponibilizar valores fictícios às carteiras de usuários.
- O MVP não suportará múltiplas moedas, integração bancária, pagamentos reais ou qualquer movimentação de dinheiro real.

## Evoluções Planejadas

Conforme o domínio amadurecer e novos requisitos forem justificados, o LedgerPay poderá evoluir para:

- Substituição do saldo persistido por um modelo de **ledger append-only** com **double-entry**, caso requisitos de auditoria e consistência financeira o demandem.
- Introdução de idempotência para impedir processamento duplicado de transferências.
- Testes de concorrência e definição de uma estratégia de locking quando operações simultâneas passarem a ser exercitadas.
- Autenticação e autorização para proteger operações sobre carteiras antes de qualquer exposição além do ambiente local.
- Inclusão futura de documento de usuário por migration, caso a evolução das regras de identificação torne esse dado necessário.
- Implementação de outbox e notificações assíncronas após a consolidação do motor principal de transferências.
