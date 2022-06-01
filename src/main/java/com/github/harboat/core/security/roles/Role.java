package com.github.harboat.core.security.roles;

import com.github.harboat.core.security.authorities.Authority;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Collection;

@Document
@AllArgsConstructor @NoArgsConstructor @RequiredArgsConstructor
@Getter @Setter @ToString
@Builder
public class Role {

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull private String name;

    @NonNull private Integer grade;

    @DocumentReference
    private Collection<Authority> authorities;

    RoleDTO toDto() {
        return RoleDTO.builder()
                .name(name)
                .grade(grade)
                .authorities(
                        authorities.stream()
                                .map(Authority::getName)
                                .toList()
                )
                .build();
    }

}
