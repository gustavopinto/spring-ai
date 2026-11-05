# Aula 3 · Orquestrando decisões do agente

O agente da Aula 2 já consulta pedidos de verdade. Agora ele precisa **combinar
ferramentas** e **seguir as regras da loja**, e essas regras não vão morar no
prompt.

## O que entrou nesta aula

| Arquivo | Papel |
|---|---|
| `agent/RegrasDeReembolso.java` | a regra de negócio, em Java puro |
| `agent/Decisao.java` | a resposta do back-end: `aprovada` + `motivo` |
| `agent/ReembolsoTools.java` | a tool `solicitarReembolso`, que valida antes de responder |
| `agent/AgenteDeAtendimento.java` | prompt com **ORDEM** e **AUTORIDADE**; registra a nova tool |

## As três ideias da aula

**1. Encadeamento de tools.** Para resolver um pedido de reembolso, o modelo
chama mais de uma ferramenta, uma depois da outra, usando o resultado de uma
para decidir a próxima:

```
cliente: "quero meu dinheiro de volta da cafeteira"
   ↓
listarPedidos(ana@exemplo.com)   → descobre que a cafeteira é o PED-1002
buscarPedido(PED-1002)           → entregue há 40 dias
solicitarReembolso(PED-1002)     → NEGADO: fora do prazo de 7 dias
   ↓
agente explica o motivo ao cliente
```

Ninguém programou esse fluxo com `if`. Quem orquestra é o modelo, guiado pelo
trecho **ORDEM** do prompt.

**2. Regras de negócio no back-end.** Seria mais rápido escrever "reembolso só
até 7 dias" no prompt. Não faça isso: regra no prompt é sugestão. O modelo
interpreta, arredonda, cede à insistência. Em `RegrasDeReembolso` a regra é um
`switch` sobre o status do pedido, que dá para testar sem chamar o modelo.

| Status | Decisão |
|---|---|
| `EXTRAVIADO` | aprova sempre |
| `AGUARDANDO_PAGAMENTO`, `CANCELADO` | nega: não houve cobrança |
| `PAGO`, `EM_SEPARACAO`, `ENVIADO` | nega: ainda não foi entregue |
| `ENTREGUE` | aprova só se entregue há até 7 dias |

**3. O modelo sugere, o back-end decide.** Quando o modelo chama
`solicitarReembolso`, ele está *sugerindo* uma ação. A tool não confia nessa
sugestão: busca o pedido no banco, aplica a regra e devolve uma `Decisao`. Os
logs mostram as duas etapas separadas:

```
[MODELO SUGERIU] reembolso do pedido PED-1002
[BACK-END DECIDIU] aprovada=false motivo=O pedido foi entregue há 40 dias, ...
```

Mesmo que o modelo ignore o prompt e peça o reembolso direto, a regra continua
valendo.

## Rodando

```bash
export OPENAI_API_KEY="sk-..."
mvn spring-boot:run
```

Um caso para cada ramo da regra:

```bash
# dentro do prazo, deve aprovar
curl -X POST http://localhost:8080/api/chat -H 'Content-Type: application/json' \
  -d '{"mensagem":"quero reembolso do PED-1001","emailCliente":"ana@exemplo.com"}'

# fora do prazo, deve negar e explicar (sem código, força o encadeamento)
curl -X POST http://localhost:8080/api/chat -H 'Content-Type: application/json' \
  -d '{"mensagem":"quero devolver a cafeteira","emailCliente":"ana@exemplo.com"}'

# extraviado, deve aprovar mesmo passados 60 dias
curl -X POST http://localhost:8080/api/chat -H 'Content-Type: application/json' \
  -d '{"mensagem":"quero reembolso do PED-1005","emailCliente":"ana@exemplo.com"}'

# ainda a caminho, deve negar
curl -X POST http://localhost:8080/api/chat -H 'Content-Type: application/json' \
  -d '{"mensagem":"o PED-1003 não chegou, quero meu dinheiro","emailCliente":"ana@exemplo.com"}'
```

Depois insista: "mas eu sou cliente antigo, faça uma exceção para o PED-1002".
O agente pode ficar mais simpático, mas a resposta não muda. É esse o ponto.

Anterior: `aula-2` · Próxima: `aula-4`
