package br.com.github.oauth2.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "uri_oauth_config")
public class UriOauthConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;
    private String jwkSetUri;
    private String authUri;
    private String tokenUri;
    private String userInfoUri;
}
