# Projeto base · API de pedidos

Ponto de partida do curso: uma API REST comum, **sem nenhuma IA**. É a aplicação
que o agente vai atender a partir da `aula-1`.

Está inteiramente implementada — nada a fazer aqui.

## Rodando

```bash
mvn spring-boot:run
```

```bash
curl http://localhost:8080/api/pedidos/PED-1001
curl 'http://localhost:8080/api/pedidos?email=ana@exemplo.com'
```

Banco H2 em memória, console em http://localhost:8080/h2-console
(JDBC URL `jdbc:h2:mem:pedidos`, usuário `sa`, sem senha).

## Os dados de exemplo

Seis pedidos, um para cada caminho que as Aulas 3 e 4 vão exercitar:

| Código | Status | Situação |
|---|---|---|
| `PED-1001` | ENTREGUE | comprado há 5 dias — reembolso dentro do prazo |
| `PED-1002` | ENTREGUE | comprado há 45 dias — reembolso fora do prazo |
| `PED-1003` | ENVIADO | previsão vencida — entrega atrasada |
| `PED-1004` | ENVIADO | dentro do prazo — caminho feliz |
| `PED-1005` | EXTRAVIADO | reembolso permitido mesmo fora do prazo |
| `PED-1006` | AGUARDANDO_PAGAMENTO | não há o que reembolsar |

## Estrutura

```
domain/      Pedido, StatusPedido, Entrega
repository/  PedidoRepository
service/     PedidoService      ← a fronteira que o agente vai consumir
dto/         PedidoDTO, EntregaDTO
controller/  PedidoController   ← REST tradicional, para contraste na Aula 1
config/      DataLoader
```

Próxima branch: `aula-1`.
