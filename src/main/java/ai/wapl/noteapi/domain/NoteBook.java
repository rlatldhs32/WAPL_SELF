package ai.wapl.noteapi.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@Entity
@RequiredArgsConstructor
//@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_NOTEAPP_NOTEBOOK_MST")
@ToString
public class NoteBook implements Cloneable{
    @Id
    @Column(length = 1024,name ="ID")
    private String Id;

    /**
     * 노트와 노트북 사이
     */
    @OneToMany(mappedBy = "mynotebook")
//    @JsonManagedReference
    @JsonBackReference
    private List<Note> NoteList = new ArrayList<>();

    @Column(name = "NOTE_CHANNEL_ID",length = 1024)
    private String noteChannelId;

    @Column(name = "TEXT",length = 1024)
    private String TEXT;

    @Column(name = "MODIFIED_DATE",length = 128)
    private String ModifiedDate;

    @Column(name = "TYPE",length = 128)
    private String type;

    @Column(name = "COLOR",length = 7)
    private String COLOR;

    /*
    아래 세 개는 type이 shared 일때만 사용함.
     */

    @Column(name = "SHARED_DATE",length = 64)
    private String SharedDate;

    @Column(name = "SHARED_USER_ID",length = 128)
    private String SharedUserId;

    @Column(name = "SHARED_ROOM_NAME",length = 256)
    private String SharedRoomName;

    //언제 사용하는지 모르겠음.
    @Column(name = "CREATED_USER_ID",length = 128)
    private String CreatedUserId;



}
