package com.github.harboat.core.games;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@Document
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
@Builder
public class Game {
    @Id private String id;
    private String gameId;
    private Collection<String> players;
    private String playerTurn;
    private Boolean ended;
}
