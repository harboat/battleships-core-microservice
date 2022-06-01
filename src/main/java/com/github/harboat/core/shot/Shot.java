package com.github.harboat.core.shot;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
@Builder
class Shot {
    @Id private String id;
    private String gameId;
    private String playerId;
    private Integer cellId;
    private Boolean hit;
}
