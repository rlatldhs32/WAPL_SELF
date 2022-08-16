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
public class NoteCreateDto {
    //제목
    //수정 시간
    //내용
    private String notebookid;
    private String noteid;
    private String title;
    private String content;
    private List<String> tags;
    private String shared_room_name; //쏘는곳

    //content를 저장하기.
    //

    public Note toEntity(NoteBook noteBook,String nowtime,String BeforeTime){
        String HtmlToText= getContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        return Note.builder()
                .noteId(getNoteid())
                .mynotebook(noteBook)
                .NoteTitle(getTitle())
                .CreatedDate(nowtime)
                .ModifiedDate(nowtime)
                .IsFavorite("none")
                .NoteContent(getContent())
                .TextContent(HtmlToText)
                .build();
    }
}


