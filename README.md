# Automação de Testes de API advantage

### Requisitos:
* Java 11
* Gradle 7+



### Executar os Testes Localmente
* Rodar os testes - `./gradlew test`
* Relatório do - `app/build/reports/tests/test/index.html`


# Anotações
- Encontrei um problema na documentação swagger:
- na criação de usuários, tive que olhar no portal como estava sendo passado o contrato, verifiquei que o atributo "country" na verdade é "countryId" 
- atributo "aobUser" não é obrigatório na API
- contrato response do logout com sucesso está diferente do swagger
- logout segundo a documentação com username ou token incorreto deveria ser 403, inicialmente estava funcionando, mas o comportamento mudou passado ser 500
- Teste de logout com userId incorreto o Header é X-Content-Type-Options, tive que remover validação do Content-Type


- Breve explicação do projeto: criamos um builder padrão chamado user, utilizamos o mesmo para criar usuário padrão e usamos hooks para deleção ao final do teste.
- Implementamos o conceito de designer client api para criar as chamadas das Apis e posteriormente executar os testes de forma mais limpa.
- Podemos criar uma estratégia de seed(semente), para inputar dados automaticamente e executar nossos testes mais controlados
