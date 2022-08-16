package ai.wapl.noteapi.dto.note;

import ai.wapl.noteapi.domain.Note;
import ai.wapl.noteapi.domain.NoteBook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NoteDtoByTags {
    //제목
    //수정 시간
    //내용
    private String notebookid;
    private String notebooktype;
    private String noteid;
    private String notetitle;

    //content를 저장하기.
    //

}


