package com.github.harboat.core.security.authorities;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Optional;

public interface AuthorityRepository extends MongoRepository<Authority, String> {
    Optional<Authority> findByName(String name);
    Collection<Authority> findAllByNameIn(Collection<String> name);
}
