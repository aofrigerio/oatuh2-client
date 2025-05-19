package br.com.github.oauth2.entrypoint;

import br.com.github.oauth2.service.ClientRegistrationService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OauthController {

    @Autowired
    private ClientRegistrationService service;

    /* Pagina de redirect configurada no loginPage só para não demonstrar o que vem padrão do spring-client */
    @GetMapping("/noauth")
    public ResponseEntity<?> noAUth(){

        Map<String, Object> map = new HashMap<>();
        map.put("message","Unathorized");

        JSONObject body = new JSONObject(map);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @GetMapping("/test")
    public String test(){
        return "Se você estiver vendo isso depois de logado, ele ainda guarda sessão";
    }

    /* Para fins didáticos, demonstra quais os clients no banco */
    @GetMapping("/oauth2/clients")
    public List<ClientRegistration> list() {
        return service.findAll();
    }

    /* Utiliza o callback para demonstar o token */
    /* Aqui você pode colocar uma regra mais elaborada, como enviar para um auth e gerar um token, e não nesse microsserviço */
    @GetMapping("/auth/callback")
    public ResponseEntity<?> getToken(@RequestParam("token") String token){
        return ResponseEntity.ok("Bearer " + token);
    }
}
