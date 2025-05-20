package br.com.github.oauth2.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String jwtToken = ((DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal()).getIdToken().getTokenValue();

        HttpSession session = request.getSession(false);
        if(session != null) session.invalidate();

        //TODO - INTERCEPTA O JWT E DEMONSTRA NA ROTA. PRECISA DA ROTA
        //RETORNA O JWT TOKEN DO PROVIDER DEFINIDO
        //PODE ENVIAR AQUI NO AUTH SERVICE PARA PODER VALIDAR, PEGAR O ID SSO E VALIDAR COM A BASE, E APÓS ISSO, GERAR UM TOKEN INTERNO
        response.sendRedirect("/auth/callback?token=" + jwtToken);
    }
}
