package br.com.github.oauth2.config;

import br.com.github.oauth2.service.security.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String jwt = tokenService.gerarToken(oAuth2AuthenticationToken);

        HttpSession session = request.getSession(false);
        if(session != null) session.invalidate();

        //TODO - INTERCEPTA O JWT E DEMONSTRA NA ROTA. PRECISA DA ROTA
        response.sendRedirect("/auth/callback?token=" + jwt);

    }
}
