package br.com.github.oauth2.model;

import br.com.github.oauth2.service.ClientRegistrationService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.Iterator;

public class DatabaseClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {

    private final ClientRegistrationService service;

    public DatabaseClientRegistrationRepository(ClientRegistrationService service) {
        this.service = service;
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return service.loadByRegistrationId(registrationId);
    }

    @Override
    public Iterator<ClientRegistration> iterator() {
        return service.findAll().iterator();
    }
}
