# Spring Boot OAuth2 Client com Múltiplos Provedores Dinâmicos

Este projeto é uma aplicação Spring Boot configurada como **OAuth2 Client** para autenticação de usuários via múltiplos provedores (Google, GitHub, Azure, etc), suportando múltiplos `clientId/clientSecret` por provedor, com configuração dinâmica via banco de dados.

---

## Funcionalidades

- Autenticação OAuth2 baseada em provedores dinâmicos armazenados no banco de dados
- Suporte a múltiplos `clientId`s para o mesmo provedor (ex: vários apps Google)
- CustomSuccessHandler intercepta para gerar um token interno. Pode haver customização

---

## Como funciona

O sistema carrega os `ClientRegistration` diretamente do banco de dados por meio de uma implementação customizada do `ClientRegistrationRepository`.

Cada provedor é identificado por um `registrationId` único (ex: `google-a`, `github-dev`, `azure-prod`).

---

## Configuração

### 1. Tabela no banco de dados (exemplo PostgreSQL)

Lembrando que o projeto já está com:
```yaml
    hibernate:
      ddl-auto: none
````

Criação das tabelas:

```sql
CREATE TABLE IF NOT EXISTS oauth_client_config (
    registration_id VARCHAR PRIMARY KEY,
    provider VARCHAR NOT NULL,
    client_id VARCHAR NOT NULL,
    client_secret VARCHAR NOT NULL,
    scopes VARCHAR,
    redirect_uri VARCHAR NOT NULL,
    active BOOLEAN DEFAULT 1,
    PRIMARY KEY (`id`)
);
````

```sql
CREATE TABLE IF NOT EXISTS `uri_oauth_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `auth_uri` varchar(255) DEFAULT NULL,
  `jwk_set_uri` varchar(255) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `token_uri` varchar(255) DEFAULT NULL,
  `user_info_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
````

### 1.A Configurações padrões do Google

````sql
INSERT INTO oauth2.uri_oauth_config (auth_uri,jwk_set_uri,provider,token_uri,user_info_uri) VALUES
('https://accounts.google.com/o/oauth2/v2/auth','https://www.googleapis.com/oauth2/v3/certs','google','https://oauth2.googleapis.com/token','https://www.googleapis.com/oauth2/v3/userinfo');
````

### 1.B Exemplo de um oauth2 provider config

```sql
INSERT INTO oauth2.oauth_client_config (client_id,client_secret,provider,redirect_uri,registration_id,scopes,user_name_attribute_name,active) VALUES
('<seu_client_id>','<seu_secret_id>','google','http://localhost:8080/login/oauth2/code/google-a','google-a','openid,email,profile','sub',1)
````

