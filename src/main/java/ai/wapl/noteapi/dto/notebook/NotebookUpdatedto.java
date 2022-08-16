package ai.wapl.noteapi.dto.notebook;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NotebookUpdatedto {
    //제목
    //수정 시간
    //내용
    private String id;
    private String title;

}