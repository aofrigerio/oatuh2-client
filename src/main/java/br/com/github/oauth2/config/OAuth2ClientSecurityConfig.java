package br.com.github.oauth2.config;

import br.com.github.oauth2.model.DatabaseClientRegistrationRepository;
import br.com.github.oauth2.service.ClientRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2ClientSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/", "/oauth2/clients", "/login/**").permitAll()
                            .requestMatchers("/custom-login").permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2Login(
                        oauthLogin -> {
                            oauthLogin.loginPage("/custom-login?provider={registrationId}");
                        }
                );
        return httpSecurity.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(ClientRegistrationService service){
        return new DatabaseClientRegistrationRepository(service);
    }

    @Bean
    public OAuth2AuthorizedClientService auth2AuthorizedClientService(ClientRegistrationRepository registrationRepository){
        return new InMemoryOAuth2AuthorizedClientService(registrationRepository);
    }
}
