package com.github.harboat.core.security;


import com.github.harboat.core.security.authorities.Authority;
import com.github.harboat.core.security.authorities.AuthorityRepository;
import com.github.harboat.core.security.roles.Role;
import com.github.harboat.core.security.roles.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
@AllArgsConstructor
class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        Collection<String> authoritiesNames = Set.of(
                "BASIC_USER"
        );
        initAuthorities(new HashSet<>(authoritiesNames));
        Collection<Authority> authorities = authorityRepository.findAllByNameIn(authoritiesNames);
        createRoleWithAuthoritiesIfNotExist("user", 1, authorities);
    }

    private void initAuthorities(Collection<String> names) {
        findAuthoritiesToCreate(names);
        Collection<Authority> authorities = createAuthorities(names);
        authorityRepository.saveAll(authorities);
    }

    private void findAuthoritiesToCreate(Collection<String> authorityNames) {
        Collection<Authority> authorities = authorityRepository.findAllByNameIn(authorityNames);
        authorities.forEach(a -> authorityNames.remove(a.getName()));
    }

    private Collection<Authority> createAuthorities(Collection<String> authorityNames) {
        Set<Authority> authorities = new HashSet<>();
        for (String authorityName : authorityNames) {
            Authority authority = new Authority(authorityName.toUpperCase());
            authorities.add(authority);
        }
        return authorities;
    }

    private void createRoleWithAuthoritiesIfNotExist(String name, int grade, Collection<Authority> authorities) {
        if (roleRepository.findByAuthoritiesIsContaining(authorities).isEmpty()) {
            Role role = new Role(name.toUpperCase(Locale.ROOT), grade);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
    }
}


