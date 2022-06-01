package com.github.harboat.core.users;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data @Builder
public class UserGetDTO {
    @NonNull private String email;
    @NonNull private String name;
}
