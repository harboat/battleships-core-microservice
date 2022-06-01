package com.github.harboat.core.shot;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/games/{gameId}")
@AllArgsConstructor
public class ShotController {

    private final ShotService service;

    @PostMapping("shoot")
    public ResponseEntity<?> shot(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String gameId,
            @Valid @RequestBody ShotDto shotDto
    ) {
        service.takeAShoot(gameId, userDetails.getUsername(), shotDto.cellId());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("nuke")
    public ResponseEntity<?> shotNuke(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String gameId,
            @Valid @RequestBody ShotDto shotDto
    ) {
        service.takeANukeShoot(gameId, userDetails.getUsername(), shotDto.cellId());
        return ResponseEntity.accepted().build();
    }

}
