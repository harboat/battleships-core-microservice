package com.github.harboat.core.configuration;

import com.github.harboat.clients.game.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/v1/rooms/{roomId}")
@AllArgsConstructor
public class ConfigurationController {

    private ConfigurationService configurationService;

    @PostMapping("/size")
    public ResponseEntity<?> setSize(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String roomId,
            @Valid @RequestBody Size size
    ) {
        configurationService.setSize(roomId, userDetails.getUsername(), size);
        return ResponseEntity.ok().build();
    }


}
