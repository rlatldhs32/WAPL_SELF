package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Note;
import ai.wapl.noteapi.domain.NoteBook;
import ai.wapl.noteapi.dto.note.NoteCreateDto;
import ai.wapl.noteapi.dto.notebook.NotebookDto;
import ai.wapl.noteapi.dto.notebook.NotebookUpdatedto;
import ai.wapl.noteapi.exception.Exceptions.CChannelNotFoundException;
import ai.wapl.noteapi.exception.Exceptions.CNoteBookNotFoundException;
import ai.wapl.noteapi.repository.NoteBookRepository;
import ai.wapl.noteapi.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoteBookService {
    private final NoteBookRepository noteBookRepository;

    private final NoteService noteService;
    private final NoteRepository noteRepository;

    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    /**
     * 시작시 default 노트북 생성 함수
     * ## 추가 : 채널이 지금 의존적이라 따로 매개변수로 들어올때는 어떻게함?
     */
    @Transactional
    public String CreateDefaultNoteBooks () {
        log.info("create Default NoteBooks");
        String CID = getRandid();
        String NotebookId = CreateDefaultChapter(CID);
        String noteid = noteService.CreateDefaultPage(NotebookId);
        CreateDefaultRecycleBin(CID);
        return "notebookid :"+ NotebookId+" \nnote : "+noteid;
    }

    @Transactional
    public String updatenotebook(NotebookUpdatedto notebookUpdatedto){
        NoteBook noteBook = noteBookRepository.findById(notebookUpdatedto.getId()).orElseThrow(CNoteBookNotFoundException::new);
        noteBook.setTEXT(notebookUpdatedto.getTitle());
        return noteBook.getId();
    }

    public Boolean IsThereType(String CID, String type){ //CID랑 type
        return noteBookRepository.existsByNoteChannelIdAndType(CID, type);
    }
    public String FindShareNotebookId(String CID){ //CID랑 type
        return noteBookRepository.findByNoteChannelIdAndType(CID,"shared_page").orElseThrow(CChannelNotFoundException::new).getId();
    }

    public List<NoteBook> findChapter(String CID){
        List<NoteBook> noteBooks = noteBookRepository.findAllByNoteChannelId(CID).orElseThrow(CNoteBookNotFoundException::new);
        if(noteBooks.isEmpty()) throw new CNoteBookNotFoundException();
        return noteBooks;
    }

    public NoteBook findnoteBook(String NID){
        NoteBook noteBook = noteBookRepository.findById(NID).orElseThrow(CNoteBookNotFoundException::new);
        return noteBook;
    }

    public String CreateDefaultChapter(String CID){
        return CreateDefaultNotebook(CID,"새 챕터","notebook");
    }
    public String CreateDefaultRecycleBin(String CID){
        return CreateDefaultNotebook_Nocolor(CID,"휴지통","recycle_bin");
    }

    //리턴값 : 노트북 ID
    //노트에서 CID 받아서
    public String CreateDefaultSharedPage(NoteCreateDto noteCreateDto){
        String noteChannelId = GetCIDfromDto(noteCreateDto);//갈 곳의 CID를 뽑아서 그 채널에 만듬.
        return CreateDefaultNotebook_Nocolor(noteChannelId,"전달받은 페이지","shared_page");
    }

    private String GetCIDfromDto(NoteCreateDto noteCreateDto) {
        String To_Go_CId = noteCreateDto.getShared_room_name();
        if(To_Go_CId==null) throw new CChannelNotFoundException();
        return To_Go_CId;
    }

    public String CreateDefaultSharedChapter(String CID){
        return CreateDefaultNotebook(CID,"새 챕터","shared");
    }

    @Transactional
    public String notebookshare(NotebookDto notebookDto){
        //들어온 노트북 id 정보 그대로 복사해야함. ( 뒤에 노트북까지)
        String OriginNotebookId = notebookDto.getId();
        NoteBook originNotebook = noteBookRepository.findById(OriginNotebookId).orElseThrow(CNoteBookNotFoundException::new);
        System.out.println("originNotebook.getId() = " + originNotebook.getId());
//        System.out.println("originNotebook.getNoteList() = " + originNotebook.getNoteList());
        List<Note> noteList = originNotebook.getNoteList();
        for (Note note : noteList) {
            System.out.println("note.getNoteId() = " + note.getNoteId());
        }

        //새 notebook을 만든 후 CID는 dto에서 들어온 destCID로 설정함.
        String destCID = notebookDto.getShared_room_name();
        System.out.println("destCID = " + destCID);
        return CopyNoteBook(originNotebook,destCID);
    }

    @Transactional
    public String CopyNoteBook(NoteBook noteBook,String destCID){
        List<Note> noteList = noteBook.getNoteList(); //TODO:혹시 tag까지 넘겨올수 있는데, 그럴일 없어도 됨.
        for (Note note : noteList) {
            System.out.println("note.getNoteId() = " + note.getNoteId());
        }
        List<Note> cloneNotes= new ArrayList<>();

        String NewNotebookId = CreateDefaultSharedChapter(destCID);
        NoteBook noteBook1 = noteBookRepository.findById(NewNotebookId).orElseThrow(CNoteBookNotFoundException::new);
        noteBook1.setNoteList(new ArrayList<>());
        System.out.println("noteBook1.toString() = " + noteBook1.toString());

        for (Note note : noteList) {
            System.out.println("note.getNoteTitle() = " + note.getNoteTitle());
            Note cloneNote = Note.builder()
                    .NoteTitle(note.getNoteTitle())
                    .noteId(getRandid())
                    .NoteContent(note.getNoteContent())
                    .TextContent(note.getTextContent())
                    .CreatedDate(note.getCreatedDate())
                    .IsFavorite(note.getIsFavorite())
                    .mynotebook(noteBook1) //현재 설정 되어있긴함.
                    .ModifiedDate(note.getModifiedDate())

                    .build();
            System.out.println("cloneNote = " + cloneNote.toString()); //현재 클론노트에는
            System.out.println("noteBook1 = " + noteBook1.getId());
            noteBook1.getNoteList().add(cloneNote);
//            cloneNote.setNotebook(noteBook1);
            noteRepository.save(cloneNote);
            log.info("여기 안오지");
        }
//        noteBook1.setNoteList(cloneNotes);
//        System.out.println("noteBook1.getNotelist = " + noteBook1.getNoteList());

        return noteBook1.getId();
    }


    @Transactional
    public void deleteNotebook(String notebookid){
        NoteBook noteBook = noteBookRepository.findById(notebookid).orElseThrow(CNoteBookNotFoundException::new);
        noteBookRepository.delete(noteBook);
    }

    @Transactional
    //1. notbook레포에서 CID 찾아서 다 삭제
    public void deleteChapter(String CID){
        noteBookRepository.deleteAllByNoteChannelId(CID);
    }

    private String NowTime() {
        Date time = new Date();
        String localTime = format.format(time);
        return localTime;
    }

    private static String getRandid() {
        //가정 : uuid 중복은 체크 안해도 됨?
        return UUID.randomUUID().toString();
    }

    /**
     * 총 14개의 색을 랜덤으로 지정해주는듯. 우선 하나만 함. 추후 조정
     * #000000
     * #3A7973
     * #5C83DA
     * #679886
     * #77B69B
     * #77BED3
     * #8F91E7
     * #C84847
     * #CA6D6D
     * #DF97AA
     * #ED5683
     * #F29274
     * #F6C750
     * #FB7748
     */
    private static String getRandcolor() {
        return "#3A7973";
    }



    private String CreateDefaultNotebook(String CID,String text,String type) {
        String newId = getRandid();
        String ModifiedDate = NowTime();
        String COLOR = getRandcolor(); // 고치기
        NoteBook noteBook = NoteBook.builder()
                .Id(newId)
                .noteChannelId(CID)
                .TEXT(text)
                .ModifiedDate(ModifiedDate)
                .type(type)
                .COLOR(COLOR)
                .build();
        noteBookRepository.save(noteBook);
        return noteBook.getId();
    }

    private String CreateDefaultNotebook_Nocolor(String CID,String text,String type) {
        String newId = getRandid();
        String ModifiedDate = NowTime();
        NoteBook noteBook = NoteBook.builder()
                .Id(newId)
                .noteChannelId(CID)
                .TEXT(text)
                .ModifiedDate(ModifiedDate)
                .type(type)
                .build();
        noteBookRepository.save(noteBook);
        return noteBook.getId();
    }
}
