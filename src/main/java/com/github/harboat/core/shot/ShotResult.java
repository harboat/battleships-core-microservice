package com.github.harboat.core.shot;

import com.github.harboat.clients.game.Cell;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Collection;

@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public record ShotResult(String playerId, Collection<Cell> cells) {
}
