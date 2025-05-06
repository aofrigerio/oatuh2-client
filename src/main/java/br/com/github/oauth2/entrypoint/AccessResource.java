package br.com.github.oauth2.entrypoint;

import br.com.github.oauth2.service.ClientRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class AccessResource {

    @Autowired
    private ClientRegistrationService service;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/all")
    public ResponseEntity<?> all(@AuthenticationPrincipal OAuth2User principal) {
        return ResponseEntity.ok(principal.getAuthorities());
    }

    @GetMapping("/redirect/{registrationId}")
    public ModelAndView redirect(OAuth2AuthenticationToken authenticationToken) {
        authenticationToken.getAuthorizedClientRegistrationId().equals("google-a");
        return new ModelAndView("redirect:https://www.youtube.com.br");
    }

    @GetMapping("/oauth2/clients")
    public List<ClientRegistration> list() {
        return service.findAll();
    }

    @GetMapping("/custom-login")
    public ModelAndView redirected(@RequestParam("provider") String registrationId){
        var registration = service.loadByRegistrationId(registrationId);
        if(registration == null){
            throw new IllegalArgumentException("Argumento não preenchido");
        }
        return new ModelAndView("redirect:"+registration.getRedirectUri());
    }
}
