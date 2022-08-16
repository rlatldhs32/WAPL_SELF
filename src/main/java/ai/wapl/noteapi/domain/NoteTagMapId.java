package ai.wapl.noteapi.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NoteTagMapId implements Serializable {
//    private String notemap;
//    private String tagmap;
    private Note notemap;
    private Tag tagmap;

}
