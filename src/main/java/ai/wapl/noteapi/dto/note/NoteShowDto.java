package ai.wapl.noteapi.dto.note;

import ai.wapl.noteapi.domain.NoteBook;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class NoteShowDto {

    private String NoteId;
    private String ParentNoteBookId;
    private String NoteTitle;
    private String NoteContent;
    private String CreatedDate;
    private String ModifiedDate;
    private String TextContent;

    private List<String> tagTexts;

}
