package com.github.harboat.core.security.authorities;

import com.github.harboat.core.GenericCRUDController;
import com.github.harboat.core.GenericResponseDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/authorities")
public class AuthorityController extends
        GenericCRUDController<AuthorityDTO, AuthorityDTO, AuthorityDTO, GenericResponseDto, AuthorityService> {

    public AuthorityController(AuthorityService service) {
        super(service);
    }

}
