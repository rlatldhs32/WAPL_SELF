package ai.wapl.noteapi.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "TB_NOTEAPP_TAG_MST")
@ToString
@IdClass (NoteTagMapId.class)
public class NoteTagMap {
    @Id
    @ManyToOne
    @JoinColumn(name = "NOTE_ID")
    private Note notemap;

    @Id
    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    private Tag tagmap;
}
