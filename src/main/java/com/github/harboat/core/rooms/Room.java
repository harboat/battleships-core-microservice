package com.github.harboat.core.rooms;

import com.github.harboat.clients.game.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;


@Document
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
@Builder
public class Room {
    @Id
    private String id;
    private String roomId;
    private Collection<String> players;
    private Size size;
    private Boolean visible;
}
