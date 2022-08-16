package ai.wapl.noteapi.domain;

import ai.wapl.noteapi.dto.note.NoteCreateDto;
import ai.wapl.noteapi.dto.note.NoteShowDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_NOTEAPP_NOTE_MST")
@ToString
public class Note {
    @Id
    @Column(length = 1024,name ="NOTE_ID")
    @NotNull
    private String noteId;

    /**
     * 노트 다 : 노트북 1
     */
    @ManyToOne(fetch = FetchType.LAZY)

//    @JsonManagedReference
    @JsonIgnore
    @JoinColumn(name="PARENT_NOTEBOOK")
    private NoteBook mynotebook;

    @OneToMany(mappedBy = "notemap")
    @JsonIgnore
    private List<NoteTagMap> notemap = new ArrayList<>();

    @Column(name = "NOTE_TITLE",length = 1024)
    private String NoteTitle;

    @Column(name = "CREATED_DATE",length = 1024)
    private String CreatedDate;

    @Column(name = "MODIFIED_DATE",length = 1024)
    private String ModifiedDate;

    @Column(name = "USER_NAME",length = 1024)
    private String UserName;

    @Column(name = "IS_EDIT",length = 1024)
    private String IsEdit;

    @Column(name = "IS_FAVORITE",length = 1024)
//    @ColumnDefault("none")
    private String IsFavorite;

    @Lob
    @Column(name = "NOTE_CONTENT")
    private String NoteContent;

    @Column(name = "USER_ID",length = 1024)
    private String UserId;

    @Column(name = "TYPE",length = 10)
    private String TYPE;

    @Column(name = "SHARED_USER_ID",length = 128)
    private String SharedUserId;
    @Column(name = "SHARED_ROOM_NAME",length = 256)
    private String SharedRoomName;
    @Column(name = "CREATED_USER_ID",length = 128)
    private String CreatedUserId;
    @Column(name = "NOTE_DELETED_AT",length = 50)
    private String NoteDeletedAt;

    @Column(name = "RESTORECHAPTERID",length = 50)
    private String RestoreChapterId;

    @Lob
    @Column(name = "TEXT_CONTENT")
    private String TextContent;

    public NoteShowDto toShowDto(){
        return NoteShowDto.builder()
                .NoteId(getNoteId())
                .ParentNoteBookId(getMynotebook().getId())
                .NoteTitle(getNoteTitle())
                .NoteContent(getNoteContent())
                .CreatedDate(getCreatedDate())
                .ModifiedDate(getModifiedDate())//이건 고칠때
                .TextContent(getTextContent())
                .build();
        //textcontent질문
    }

    public void setNotebook(NoteBook notebook) {
        System.out.println("notebook = " + notebook);
        this.mynotebook = notebook;
//        System.out.println("notebook.getNoteList() = " + notebook.getNoteList()); //널이네
//        if(notebook.getNoteList()==null)
        System.out.println("this.toString() = " + this.toString());
        notebook.getNoteList().add(this);
        System.out.println("After set : notebook.getNoteList() = " + notebook.getNoteList());
    }



}
