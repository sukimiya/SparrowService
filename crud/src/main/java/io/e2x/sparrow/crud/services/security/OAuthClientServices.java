package io.e2x.sparrow.crud.services.security;

import io.e2x.sparrow.crud.mongod.repo.security.OAuthClientRepository;
import io.e2x.sparrow.crud.mongod.vo.security.OAuthClientDetail;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import reactor.core.publisher.Mono;

public class OAuthClientServices implements ClientDetailsService {

    private OAuthClientRepository clientRepository;

    public OAuthClientServices(OAuthClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        Mono<OAuthClientDetail> clientDetails = clientRepository.findByClientId(s);
        OAuthClientDetail clientDetail = clientDetails.block();
        clientDetail.of(System.currentTimeMillis());
        return clientDetail;
    }
} 

