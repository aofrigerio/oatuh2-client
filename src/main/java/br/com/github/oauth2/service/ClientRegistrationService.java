package br.com.github.oauth2.service;

import br.com.github.oauth2.model.OAuthClientConfig;
import br.com.github.oauth2.model.OAuthClientConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientRegistrationService {

    @Autowired
    private OAuthClientConfigRepository repository;

    public ClientRegistration loadByRegistrationId(String registrationId) {
        return repository.findByRegistrationId(registrationId)
                .map(this::toClientRegistration)
                .orElse(null);
    }

    public List<ClientRegistration> findAll() {
        return repository.findAll().stream()
                .map(this::toClientRegistration)
                .toList();
    }

    private ClientRegistration toClientRegistration(OAuthClientConfig config) {
        return ClientRegistration.withRegistrationId(config.getRegistrationId())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(config.getRedirectUri())
                .scope(config.getScopes().split(","))
                .authorizationUri(getAuthUri(config.getProvider()))
                .tokenUri(getTokenUri(config.getProvider()))
                .userInfoUri(getUserInfoUri(config.getProvider()))
                .userNameAttributeName("sub")
                .clientName(config.getProvider().toUpperCase() + " " + config.getRegistrationId())
                .jwkSetUri(getJwkSetUri(config.getProvider()))
                .build();
    }

    private String getJwkSetUri(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> "https://www.googleapis.com/oauth2/v3/certs";
            case "azure" -> "https://login.microsoftonline.com/common/discovery/v2.0/keys";
            // GitHub não fornece JWT, então não precisa
            default -> null;
        };
    }

    // Métodos que retornam URIs com base no provedor

    private String getAuthUri(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> "https://accounts.google.com/o/oauth2/v2/auth";
            case "github" -> "https://github.com/login/oauth/authorize";
            case "azure" -> "https://login.microsoftonline.com/common/oauth2/v2.0/authorize";
            default -> throw new IllegalArgumentException("Provedor não suportado: " + provider);
        };
    }

    private String getTokenUri(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> "https://oauth2.googleapis.com/token";
            case "github" -> "https://github.com/login/oauth/access_token";
            case "azure" -> "https://login.microsoftonline.com/common/oauth2/v2.0/token";
            default -> throw new IllegalArgumentException("Provedor não suportado: " + provider);
        };
    }

    private String getUserInfoUri(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> "https://www.googleapis.com/oauth2/v3/userinfo";
            case "github" -> "https://api.github.com/user";
            case "azure" -> "https://graph.microsoft.com/oidc/userinfo";
            default -> throw new IllegalArgumentException("Provedor não suportado: " + provider);
        };
    }
}
