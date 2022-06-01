package com.github.harboat.core.users;

import com.github.harboat.core.security.roles.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor @NoArgsConstructor @RequiredArgsConstructor
@Getter @Setter @ToString
@Builder
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull private String email;

    @NonNull private String name;
    private String password;

    private LocalDateTime createdDate;

    @DocumentReference
    private Role role;

    UserGetDTO toDto() {
        return UserGetDTO.builder()
                .email(this.email)
                .name(this.name)
                .build();
    }

}
