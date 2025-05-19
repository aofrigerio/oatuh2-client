package br.com.github.oauth2.config;

import br.com.github.oauth2.model.repository.DatabaseClientRegistrationRepository;
import br.com.github.oauth2.service.ClientRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2ClientSecurityConfig {

    @Autowired
    private  CustomSuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/oauth2/clients").permitAll()
                            .requestMatchers("/auth/callback").permitAll()
                            .requestMatchers("/noauth").permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2Login(
                        oauthLogin -> {
                            oauthLogin.successHandler(oAuth2SuccessHandler)
                                    .loginPage("/noauth")
                            ;
                        }
                );
        ;
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
