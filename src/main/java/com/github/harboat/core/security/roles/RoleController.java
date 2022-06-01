package com.github.harboat.core.security.roles;

import com.github.harboat.core.GenericCRUDController;
import com.github.harboat.core.GenericResponseDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends GenericCRUDController<RoleDTO, RoleDTO, RoleDTO, GenericResponseDto, RoleService> {
    protected RoleController(RoleService service) {
        super(service);
    }
}
