# Aula 1 · Criando o primeiro agente com Spring AI

O que entra nesta aula: o Spring AI, o `ChatClient` e um endpoint conversacional
com prompt de sistema definindo o papel do agente.

## O que implementar

```bash
grep -rn "TODO (Aula 1)" src/
```

| Arquivo | O que fazer |
|---|---|
| `agent/AgenteDeAtendimento.java` | escrever o `PROMPT_DE_SISTEMA` e o método `atender` |
| `controller/ChatController.java` | ligar o endpoint ao agente |

O resto do projeto (`base`) já está pronto e funcionando.

## Rodando

```bash
export OPENAI_API_KEY="sk-..."
mvn spring-boot:run
```

A chave é obrigatória a partir desta aula — sem ela a aplicação não sobe.

```bash
curl -X POST http://localhost:8080/api/chat \
  -H 'Content-Type: application/json' \
  -d '{"mensagem":"bom dia, como funciona a troca?","emailCliente":"ana@exemplo.com"}'
```

## O experimento que abre a Aula 2

Depois de implementar, pergunte ao agente sobre um pedido de verdade:

```bash
curl -X POST http://localhost:8080/api/chat \
  -H 'Content-Type: application/json' \
  -d '{"mensagem":"qual o status do PED-1001?","emailCliente":"ana@exemplo.com"}'
```

Ele vai responder com confiança — e inventar tudo. O pedido existe no banco
(`curl http://localhost:8080/api/pedidos/PED-1001`), mas o agente não tem como
enxergá-lo. Um modelo sozinho não é um agente; falta dar a ele acesso à
aplicação. É exatamente esse buraco que o tool calling da Aula 2 preenche.

Anterior: `base` · Próxima: `aula-2`
