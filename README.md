# Aula 3 · Orquestrando decisões do agente

O agente já lê dados de verdade. Agora ele precisa seguir as regras da loja —
e essas regras não vão morar no prompt.

## O que implementar

```bash
grep -rn "TODO (Aula 3)" src/
```

| Arquivo | O que fazer |
|---|---|
| `agent/PoliticaDeAtendimento.java` | as regras: `podeReembolsar`, `podeAbrirTicket`, `orientacaoParaOModelo` |
| `agent/PedidoConsultaTools.java` | a tool `consultarOpcoesDisponiveis`, que expõe a política |
| `agent/AgenteDeAtendimento.java` | o trecho do prompt sobre ordem e autoridade |

`DecisaoDoAgente` já vem pronto — é só o tipo de retorno.

## A ideia da aula

Seria mais rápido escrever "reembolso só até 30 dias" no prompt de sistema. Não
faça isso. Regra no prompt é sugestão: o modelo interpreta, arredonda, cede à
insistência do cliente. Regra em Java é regra.

O desenho aqui é: a política é Java puro, e o modelo só tem acesso a ela através
de uma ferramenta. Ele **pergunta** o que pode fazer, em vez de decidir.

```
cliente: "quero meu dinheiro de volta do PED-1002"
   ↓
buscarPedido(PED-1002)              → entregue há 45 dias
consultarOpcoesDisponiveis(PED-1002) → reembolso NÃO PERMITIDO, fora do prazo
   ↓
agente explica o motivo ao cliente
```

`DecisaoDoAgente` existe para deixar isso explícito no código: `acao` é o que se
pretende, `aprovada` é o que o back-end respondeu. Duas coisas diferentes, dois
campos diferentes.

## Rodando

```bash
export OPENAI_API_KEY="sk-..."
mvn spring-boot:run
```

Os casos que valem testar, um para cada ramo da política:

```bash
# fora do prazo — deve negar e explicar
-d '{"mensagem":"quero reembolso do PED-1002","emailCliente":"ana@exemplo.com"}'

# extraviado — deve permitir mesmo passados 60 dias
-d '{"mensagem":"quero reembolso do PED-1005","emailCliente":"ana@exemplo.com"}'

# atrasado, mas não entregue — chamado sim, reembolso não
-d '{"mensagem":"o PED-1003 não chegou, quero meu dinheiro","emailCliente":"ana@exemplo.com"}'
```

Depois insista: responda "mas eu sou cliente antigo, faça uma exceção". O agente
pode ficar mais simpático, mas a resposta não muda. É esse o ponto.

Anterior: `aula-2` · Próxima: `aula-4`
