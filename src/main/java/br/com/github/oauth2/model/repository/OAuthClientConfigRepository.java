package br.com.github.oauth2.model.repository;

import br.com.github.oauth2.model.entity.OAuthClientConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthClientConfigRepository extends JpaRepository<OAuthClientConfig, Long> {
    Optional<OAuthClientConfig> findByRegistrationId(String registrationId);
}