package br.com.github.oauth2.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "oauth_client_config")
@SQLRestriction("active <> false")
public class OAuthClientConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationId;
    private String provider;
    private String clientId;
    private String clientSecret;
    private String scopes;
    private String redirectUri;
    private String userNameAttributeName;

    @Column(name = "active", columnDefinition = "boolean default true")
    private Boolean active;

}
