package br.com.github.oauth2.service;

import br.com.github.oauth2.model.entity.OAuthClientConfig;
import br.com.github.oauth2.model.entity.UriOauthConfig;
import br.com.github.oauth2.model.repository.OAuthClientConfigRepository;
import br.com.github.oauth2.model.repository.UriOauthConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientRegistrationService {

    @Autowired
    private OAuthClientConfigRepository repository;

    @Autowired
    private UriOauthConfigRepository uriOauthConfigRepository;

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

        ClientRegistration clientRegistration = null;
        Optional<UriOauthConfig> uriOauthConfigOptional = uriOauthConfigRepository.findByProvider(config.getProvider());

        if(uriOauthConfigOptional.isPresent()){
            UriOauthConfig uriOauthConfig = uriOauthConfigOptional.get();

            clientRegistration = ClientRegistration.withRegistrationId(config.getRegistrationId())
                    .clientId(config.getClientId())
                    .clientSecret(config.getClientSecret())
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri(config.getRedirectUri())
                    .scope(config.getScopes().split(","))
                    .authorizationUri(uriOauthConfig.getAuthUri())
                    .tokenUri(uriOauthConfig.getTokenUri())
                    .userInfoUri(uriOauthConfig.getUserInfoUri())
                    .userNameAttributeName(config.getUserNameAttributeName())
                    .clientName(config.getProvider().toUpperCase() + " " + config.getRegistrationId())
                    .jwkSetUri(uriOauthConfig.getJwkSetUri())
                    .build();
        }

        //TODO - PRECISA MELHORAR AQUI, POIS SE VIER NULL, ELE NÃO SOBE A APLICAÇÃO
        return clientRegistration;
    }
}
