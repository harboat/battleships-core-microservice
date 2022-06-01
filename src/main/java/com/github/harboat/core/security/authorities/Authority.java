package com.github.harboat.core.security.authorities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor @NoArgsConstructor @RequiredArgsConstructor
@Getter @Setter @ToString
@Builder
public class Authority {

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull private String name;

    AuthorityDTO toDto() {
        return new AuthorityDTO(name);
    }

}
