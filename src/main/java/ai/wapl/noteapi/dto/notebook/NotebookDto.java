package ai.wapl.noteapi.dto.notebook;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NotebookDto {
    //제목
    //수정 시간
    //내용
    private String id;
    private String noteChannelId;
    private String TEXT;
    private String type;
    private String shared_room_name; //쏘는곳

}


