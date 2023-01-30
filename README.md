## Como Rodar

Primeiro, entre na pasta raiz do projeto. Depois, verifique se a sua variável de ambiente JAVA_HOME está apontando para um JDK 17, que é a versão que o projeto usa e o Maven (gerenciador de dependências e pacotes que roda o projeto) utiliza essa variável pra saber qual a versão do java.

No Linux, utilize o comando export JAVA_HOME=<caminho_da_sua_jdk17>. No Windows, pode procurar alterar sua variável de ambiente pelas configurações do sistema, há vários tutoriais na internet. Se você usa Mac, o mesmo.

Após ter a sua variável JAVA_HOME configurada, você precisa buildar o projeto. Pode utilizar o seguinte comando, na raiz do projeto: `./mvnw clean package -DskipTests`.

Se tiver buildado corretamente, agora você precisa buildar a imagem docker do projeto. Primeiro, instale o docker e o docker-compose no seu computador. Após isso, utilize o comando: `docker build -t bantads_gerente .`.

Após buildar a imagem, é necessário executá-la, criando o container. Nesse sentido, rode o comando de build e depois o comando para erguer os containeres: `docker-compose build && docker-compose up -d`.

Agora os containeres estão rodando e você pode utilizar o sistema apontando para http://localhost:8480.

## Endpoints
Para acessar os recursos do sistema, você precisa saber os métodos HTTP e URLs disponíveis. Além disso, muito importante: para salvar um Pokémon, é preciso primeiro salvar a imagem dele! Utilize o POST descrito abaixo para salvar a imagem, que irá retornar o ID da imagem salva. Utilize o ID retornado para criar o Pokémon!

### Image

| Método HTTP | URL | Descrição |
| --- | --- | --- |
| POST | http://<endereço_ip>:8480/image | Salva uma imagem no banco de dados. É preciso enviar uma imagem no corpo HTTP. |
| GET  | http://<endereço_ip>:8480/image | Retorna todas as imagens salvas. Elas não retornam como imagem, mas sim como dados. |
| GET  | http://<endereço_ip>:8480/image/info/<id_da_imagem> | Retorna informações sobre a imagem com o ID pesquisado. |
| GET  | http://<endereço_ip>:8480/image/<id_da_imagem> | Retorna a imagem com o ID pesquisado. Este método retorna uma imagem real. |

### Pokemon
| Método HTTP | URL | Descrição |
| --- | --- | --- |
| POST | http://<endereço_ip>:8480/pokemon | Salva um Pokémon no banco de dados. É preciso enviar um JSON no corpo HTTP. |
| GET  | http://<endereço_ip>:8480/pokemon | Retorna todos os Pokémon salvos. |
| GET  | http://<endereço_ip>:8480/pokemon/<id_do_pokemon> | Retorna o Pokémon com o ID pesquisado. |
| GET  | http://<endereço_ip>:8480/pokemon/search/type?type=<tipo_para_pesquisar> | Retorna uma lista de nomes dos Pokémon daquele tipo. |
| GET  | http://<endereço_ip>:8480/pokemon/search/skill?skill=<habilidade_para_pesquisar> | Retorna uma lista de nomes dos Pokémon com aquela habilidade. |
| PUT  | http://<endereço_ip>:8480/pokemon/<id_do_pokemon> | Atualiza um Pokémon no banco de dados. É preciso enviar um JSON no corpo HTTP. |
| DELETE  | http://<endereço_ip>:8480/pokemon/<id_do_pokemon> | Deleta um Pokémon no banco de dados. |

#### Corpo da Requisição
```
{
    "name": "",
    "type": "",
    "skills": [
        "",
        "",
        ""
    ],
    "imageData": <id_da_imagem>,
    "username": ""
}
```

### Auth
| Método HTTP | URL | Descrição |
| --- | --- | --- |
| POST | http://<endereço_ip>:8480/auth | Salva um Usuário no banco de dados. É preciso enviar um JSON no corpo HTTP. |
| POST | http://<endereço_ip>:8480/auth/login | Verifica se o login e senha informados no corpo da requisição existem no banco de dados. É preciso enviar um JSON no corpo HTTP. |
| GET  | http://<endereço_ip>:8480/auth | Retorna todos os Usuários salvos. |
| GET  | http://<endereço_ip>:8480/auth/<id_da_auth> | Retorna o Usuário com o ID pesquisado. |
| PUT  | http://<endereço_ip>:8480/auth/<id_da_auth> | Atualiza um Usuário no banco de dados. É preciso enviar um JSON no corpo HTTP. |
| DELETE  | http://<endereço_ip>:8480/auth/<id_da_auth> | Deleta um Usuário no banco de dados. |

#### Corpo da Requisição
```
{
    "login": "",
    "password": ""
}
