# Aula 2 · Tool Calling com métodos Java

O agente da Aula 1 inventava respostas sobre pedidos. Nesta aula ele passa a
consultar a aplicação de verdade.

## O que implementar

```bash
grep -rn "TODO (Aula 2)" src/
```

| Arquivo | O que fazer |
|---|---|
| `agent/PedidoConsultaTools.java` | os quatro métodos `@Tool` |
| `agent/AgenteDeAtendimento.java` | registrar as tools no `ChatClient` (uma linha) |

## Rodando

```bash
export OPENAI_API_KEY="sk-..."
mvn spring-boot:run
```

```bash
curl -X POST http://localhost:8080/api/chat \
  -H 'Content-Type: application/json' \
  -d '{"mensagem":"qual o status do PED-1003?","emailCliente":"ana@exemplo.com"}'
```

A mesma pergunta da Aula 1 — agora com a resposta certa. Acompanhe o log: as
linhas `[TOOL]` mostram o modelo pedindo para executar seus métodos.

## Coisas para observar em aula

**O modelo não executa nada.** Ele devolve "quero chamar `consultarStatus` com
`PED-1003`". Quem executa é o Spring AI, na sua JVM, com suas credenciais.

**A descrição da tool é o contrato.** Troque a descrição de `consultarStatus`
por algo vago como "retorna informação" e repita a pergunta: o modelo passa a
chamar a ferramenta errada, ou nenhuma. O texto da anotação é código.

**Pergunte algo que exija duas ferramentas** — "o PED-1003 vai atrasar?" — e
repare que o modelo encadeia as chamadas sozinho. É esse encadeamento que a
Aula 3 vai colocar sob regras.

Anterior: `aula-1` · Próxima: `aula-3`
