package com.github.harboat.core.shot;

import org.hibernate.validator.constraints.Range;

public record ShotDto(@Range(min = 1L, max = 400) Integer cellId) {
}
