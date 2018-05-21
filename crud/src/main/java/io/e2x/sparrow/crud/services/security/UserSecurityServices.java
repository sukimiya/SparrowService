package io.e2x.sparrow.crud.services.security;

import io.e2x.sparrow.crud.mongod.repo.security.OUserDetailRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public class UserSecurityServices implements ReactiveUserDetailsService{
    private OUserDetailRepository oUserDetailRepository;

    public UserSecurityServices(OUserDetailRepository oUserDetailRepository) {
        this.oUserDetailRepository = oUserDetailRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        return oUserDetailRepository.findByUsername(s).cast(UserDetails.class);
    }
}
