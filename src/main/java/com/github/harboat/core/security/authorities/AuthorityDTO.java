package com.github.harboat.core.security.authorities;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthorityDTO {

    @NotEmpty
    @NonNull private String name;

}
