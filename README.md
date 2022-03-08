# Automação de Testes de API advantage

### Requisitos:
* Java 11
* Gradle 7+



### Executar os Testes Localmente
* Rodar os testes - `./gradlew test`
* Relatório do - `app/build/reports/tests/test/index.html`


# Anotações
- Deixei alguns logs para visualização dos testes, não é uma boa prática manter, mas como é apenas algo didático, achei interessante manter
- Encontrei um problema na documentação swagger:
- na criação de usuários, tive que olhar no portal como estava sendo passado o contrato, verifiquei que o atributo "country" na verdade é "countryId" 
- atributo "aobUser" não é obrigatório na API
- contrato response do logout com sucesso está diferente do swagger
- logout segundo a documentação com username ou token incorreto deveria ser 403, inicialmente estava funcionando, mas o comportamento mudou passado ser 500
- Para executar logout não é necessário estar logado, apenas userId e token correto
- Teste de logout com userId incorreto o Header é X-Content-Type-Options, tive que remover validação do Content-Type


- Breve explicação do projeto: criamos um builder padrão chamado user, utilizamos o mesmo para criar usuário padrão e usamos hooks para deleção ao final do teste.
- Implementamos o conceito de designer client api para criar as chamadas das Apis e posteriormente executar os testes de forma mais limpa.
- Podemos criar uma estratégia de seed(semente), para inputar dados automaticamente e executar nossos testes mais controlados


#### Cenários de testes
Funcionalidade: Gerenciamento de criação de usuários

Cenário: Cliente não cria usuário com login existente
Dado que eu possua dados de login existente
Quando faço solicitação de cadastro
Então não é possível criar usuário

Cenário: Cliente não cria usuário com dados de senha incorreto
Dado que eu possua dados de login com senha incorreta
Quando faço solicitação de cadastro
Então não é possível criar usuário

Cenário: Cliente não cria usuário com dados de usuário incorreto
Dado que eu possua dados de login com senha incorreta
Quando faço solicitação de cadastro
Então não é possível criar usuário

Cenário: Cliente cria usuário
Dado que eu possua dados de login
Quando faço solicitação de cadastro
Então é possível criar novo usuário


Funcionalidade: Gerenciamento de Login

Cenário: Cliente não realiza login com usuário incorreto
Dado que eu não tenha dado correto do usuário
Quando faço solicitação de login
Então não é possível realizar login

Cenário: Cliente não realiza login com password de usuário incorreto
Dado que eu não tenha dado de password correto do usuário
Quando faço solicitação de login
Então não é possível realizar login

Cenário: Cliente realiza login
Dado que eu tenha dado correto do usuário
Quando faço solicitação de login
Então é possível realizar login


Funcionalidade: Gerenciamento de Logout

Cenário: Cliente não realiza logout com usuário incorreto
Dado que eu não tenha dado correto do usuário
Quando faço solicitação de logout
Então não é possível realizar logout

Cenário: Cliente não realiza logout com token de usuário incorreto
Dado que eu não tenha dado de token correto do usuário
Quando faço solicitação de logout
Então não é possível realizar logout

Cenário: Cliente realiza logout
Dado que eu tenha dado correto do usuário
Quando faço solicitação de logout
Então é possível realizar logout


Funcionalidade: Gerenciamento de Pedido(carrinho)

Cenário: Novo cliente adiciona produto ao carrinho
Dado que eu crio e estou logado com usuário valido
Quando adiciono produto ao carrinho de compras
Então é possível remover produto e efetuar logout



####