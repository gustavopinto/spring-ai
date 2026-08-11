# Spring AI: criando agentes com Java

Agente de atendimento para uma API de pedidos — consulta informações, toma
decisões simples e executa ações controladas usando tools Java.

**Esta branch está vazia de propósito. O código está nas branches de cada aula.**

| Branch | Aula |
|---|---|
| `base` | Projeto base — a API de pedidos, sem nenhuma IA |
| `aula-1` | Criando o primeiro agente com Spring AI |
| `aula-2` | Tool Calling com métodos Java |
| `aula-3` | Orquestrando decisões do agente |
| `aula-4` | Executando ações com segurança |
| `aula-5` | Preparando o agente para produção |

As branches são encadeadas: `base` → `aula-1` → `aula-2` → … → `aula-5`.
Cada uma contém tudo o que veio antes, mais o conteúdo da sua aula.

```bash
git checkout aula-2                 # ir para o projeto da Aula 2
git diff aula-1..aula-2             # ver exatamente o que a Aula 2 acrescenta
```

Os métodos vêm com a assinatura, a documentação da lógica esperada e um `TODO` —
o corpo é escrito durante a aula.

```bash
grep -rn "TODO" src/                # o que implementar na branch atual
```

## Pré-requisitos

JDK 21, Maven 3.9+ e uma chave da OpenAI (`OPENAI_API_KEY`) a partir da `aula-1`.
