package ai.wapl.noteapi.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_NOTEAPP_TAG")
@ToString
public class Tag {

    @Id
    @Column(length = 1024,name ="TAG_ID")
    @NotNull
    private String tagId;

    @Column(length = 1024,name ="TEXT")
    private String text;


//    @OneToMany(mappedBy = "tagmap")
//    private List<NoteTagMap> tags = new ArrayList<>();
}
