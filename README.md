# Aula 4 · Executando ações com segurança

Até aqui o agente só lia. Agora ele passa a alterar o mundo — e é por isso que
esta é a aula com mais trava.

## O que implementar

```bash
grep -rn "TODO (Aula 4)" src/
```

| Arquivo | O que fazer |
|---|---|
| `agent/PedidoAcaoTools.java` | as três tools de ação: validar, registrar pendente, pedir confirmação |
| `agent/ConfirmacaoService.java` | guardar e consumir a ação pendente |
| `service/AtendimentoService.java` | executar de fato: abrir chamado, reembolsar |
| `controller/ChatController.java` | o endpoint `/api/chat/confirmar` |
| `agent/AgenteDeAtendimento.java` | o trecho do prompt sobre ações |
| `src/test/…` | os testes marcados com `@Disabled` |

## Consulta e ação são coisas diferentes

| tool de consulta | tool de ação |
|---|---|
| idempotente | muda o mundo |
| chamar duas vezes não custa nada | chamar duas vezes reembolsa duas vezes |
| erro = resposta incompleta | erro = prejuízo |
| o modelo chama à vontade | passa por política e confirmação |

Por isso estão em classes separadas, e por isso nenhuma tool de ação executa
nada: ela valida, guarda um pendente e devolve uma pergunta.

```
cliente: "quero reembolso do PED-1001"
   ↓
solicitarReembolso  → política aprova → registra pendente → AGUARDANDO_CONFIRMACAO
   ↓
agente: "confirma o reembolso de R$ 289,90 do PED-1001?"
   ↓
cliente: POST /api/chat/confirmar
   ↓
AtendimentoService revalida a política e executa
```

Repare que a política é consultada **duas vezes**: na tool e de novo no service,
na hora de executar. Redundância proposital — o estado pode ter mudado entre uma
e outra, e o service não tem como saber quem o chamou.

## Rodando

```bash
export OPENAI_API_KEY="sk-..."
mvn spring-boot:run
```

```bash
# 1. pedir o reembolso — nada é executado, guarde o conversationId da resposta
curl -X POST http://localhost:8080/api/chat \
  -H 'Content-Type: application/json' \
  -d '{"mensagem":"quero reembolso do PED-1001","emailCliente":"ana@exemplo.com"}'

# 2. confirmar
curl -X POST http://localhost:8080/api/chat/confirmar \
  -H 'Content-Type: application/json' \
  -d '{"mensagem":"sim","emailCliente":"ana@exemplo.com","conversationId":"COLE_AQUI"}'
```

Entre um passo e outro, confira no H2 que `reembolsado` continua `false`. Essa
verificação é a aula inteira em uma linha: o agente disse que ia fazer, e o
banco prova que ele não fez até você confirmar.

Anterior: `aula-3` · Próxima: `aula-5`
