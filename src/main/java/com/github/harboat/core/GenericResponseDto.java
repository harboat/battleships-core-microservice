package com.github.harboat.core;

import lombok.Data;
import lombok.NonNull;

@Data
public class GenericResponseDto {
    @NonNull private String message;
}
