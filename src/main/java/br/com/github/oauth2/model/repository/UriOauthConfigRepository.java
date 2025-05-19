package br.com.github.oauth2.model.repository;

import br.com.github.oauth2.model.entity.UriOauthConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UriOauthConfigRepository extends JpaRepository<UriOauthConfig, Long> {
    Optional<UriOauthConfig> findByProvider(String provider);
}
