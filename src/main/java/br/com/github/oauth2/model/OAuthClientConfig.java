package br.com.github.oauth2.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "oauth_client_config")
public class OAuthClientConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationId;
    private String provider; // ex: google, github, azure
    private String clientId;
    private String clientSecret;
    private String scopes; // separados por vírgula
    private String redirectUri;

}
