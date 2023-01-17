
# Description du micro service
<p>
Pour ce test, j'ai créé une base de donnée d'exemple.<br/> Il faut noter que j'ai juste pris un cas très simple juste pour démontrer comment on peut créé un micro-service en utilisant le framework  <b>Spring Boot</b>.
</p>
<p>
Concernant la persistance des donnée j'ai utilisé <b>MySQL</b> comme SGBD.
</p>

<p>

Pour cet exemple, nous avons la table : 
- <b>users</b> : Qui va contenir les information de tout utilisateur de l'application, je considère que tout personne aura besoin d'un compte utilisateur.

- <b>agents</b> : Cette table contiendra les infos spécifiques pour les utilisateur qui auront le compte agent, juste pour nous aider à faire le retrait auprès d'un agent. Donc un agent est un utilisateur qui a un compte et un numero agent...

- <b>Roles</b> Comme tous les utilisateur nom pas le meme permission dans l'application, cette table nous permet de distinguer les utilisateurs. Vous trouverez pour certains API cette vérification...
```@PreAuthorize("hasRole('ADMIN')")```
- <b>Accounts</b> : Chaque utlisateur aura un compte banque, c'est ici qu'on va mettre ces info là.

- <b>Currency</b> : Comme nous devons avoir la possiblité de supporter plusieurs ;monnaies, chaque compte bancaire aura sa monnaie (USD, CFA ou autre) 

- <b>Transactions</b> : Pour enregistrer les opérations de retrait ou transfert d'argent, nous allons stocker ces données ici. Le montant, la date, le type de transaction, que utilisateur qui a realisé l'operation, le cout de la commission, le gain pour l'entreprise pour cette transaction.

- <b>Transfert</b> : Comme certaine opertations impliques plusieurs comptes compte, on poura avec cette table définir la provenance de l'argent..
</p>

<img src='./screens/schema.PNG'>
<b>
Vous remarquerez aussi que j'ai plus utiliser les identifiants auto-increment dans plusieurs tables, ce juste pour faciliter l'evaluateur à lire les données. Généralement j'utiliser les uuid... 

</b>

## Creation de la base de donnees
J'ai utilisé ```Mysql  8.0.21```


Dans le dossier ```sql``` du project, vous aller trouver les scripts nécessaires pour créer l'utilisateur qui va nous permettre d'avoir accès à  la base de données  (```db-user.sql```), le schema (```schema.sql```), bien que JPA peut aussi le faire et enregistrer les données de test (```data.sql```)

<br/>

Donc il faut exécuter respectivement : ```db-user.sql```, ```schema.sql``` et ```data.sql```
<br/>
<img src='./screens/sql.PNG' height='300'>

<br/>

### Information sur accès à la base de données dans ```Spring boot```
<img src='./screens/dbname.PNG'>
## Installation et démarrage de l'application

```$ mvn install```

```$ mvn spring-boot:run```

<br/>

## Test des API en utilisation swagger 3
http://localhost:8080/swagger-ui/index.html

<br/>

## Quelques API

<b> Authentification</b>

``` POST http://localhost:8080/api/auth/signin```

```json
{
    "username" : "mayele@gmail.com",
    "password":"tester"
}
```
Reponse:
```json
{
  "id": 2,
  "username": "mayele@gmail.com",
  "displayname": "Mayele",
  "phone": null,
  "email": "mayele@gmail.com",
  "roles": [
    "ROLE_USER"
  ],
  "accessToken": "un_long_token_sera_ici",
  "tokenType": "Bearer"
}

```
Dans le données d'exemple Mayele a un compte ```12002132``` en Dollars,
Il peut retirer l'argent au pres de l'agent ```JOHN DOE``` dont le compte est ```2300678```



Cet API nécessite un token, donc dans le header du request il faudra ajouter

```"Authorization": "Bearer un_long_token_sera_ici"```
Meme dans swagger aussi.
<br/>
<img src='./screens/swagger_token.PNG' height="200">
<br/>

```POST http://localhost:8080/api/transactions/withdrawal```

```json
{
    "accountNumber" : "12002132",
    "amount" : 25,
    "agentNumber": "2300678",
    "description" : "retrait chez un agent"
}
```
Réponse :

```json
{
  "transaction_date": "2023-01-15 09:16:30",
  "code": 200,
  "transaction_amount": 32.5,
  "transaction_reference": "RCKBDQFMDBSILLTYOCSW",
  "status": 1
}
```

Le user Mayele veut maintenant faire un transfert de son compte  ```12002132``` vers ```12002133```,

Cet API nécessite un token, donc dans le header du request il faudra ajouter

```"Authorization": "Bearer un_long_token_sera_ici"```

```POST http://localhost:8080/api/transactions/transfert```

```json
{
    "fromAccountNumber" : "12002132",
    "toAccountNumber" : "12002133",
    "amount" : 10,
    "description" : "Transfert argent"
}
```
Réponse
```json
{
  "transaction_date": "2023-01-15 09:21:16",
  "code": 200,
  "transaction_amount": 12,
  "transaction_reference": "CMKLLWJMSOFFDRRNWWWZ",
  "status": 1
}
```


## Autres
Il faut signaler que j'ai utilisé ce bout de code trouver sur le net, puis j'ai personnalisé.

https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication.git 