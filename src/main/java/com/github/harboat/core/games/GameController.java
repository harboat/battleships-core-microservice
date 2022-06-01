package com.github.harboat.core.games;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games")
@Validated
@AllArgsConstructor
public class GameController {

    private final GameService service;

    @PostMapping("{gameId}/forfeit")
    public ResponseEntity<?> forfeit(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String gameId
    ) {
        service.forfeit(userDetails.getUsername(), gameId);
        return ResponseEntity.ok().build();
    }
}
