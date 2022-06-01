package com.github.harboat.core.security.roles;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Data @Builder
public class RoleDTO {

    @NotEmpty
    @NonNull private String name;

    @NotEmpty
    @NonNull private Integer grade;

    @NonNull private Collection<String> authorities;
}
