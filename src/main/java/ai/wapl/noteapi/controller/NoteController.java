package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.domain.response.Result;
import ai.wapl.noteapi.dto.note.NoteCreateDto;
import ai.wapl.noteapi.dto.note.NoteShowDto;
import ai.wapl.noteapi.service.NoteBookService;
import ai.wapl.noteapi.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestControllerAdvice
@Transactional
@Slf4j
@RequestMapping("/api/note")
public class NoteController {
    private final NoteService noteService;
    private final NoteBookService noteBookService;

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e) {
        System.out.println("Error in NoteBookController = " + e);// log로찍기.
    }

    @GetMapping("/{noteId}")
    public Result testNoteBook(@PathVariable String noteId){
        NoteShowDto noteShowDto = noteService.Note_Show_With_Id(noteId);
        if(noteShowDto==null)
            return new Result("There is no Note");
        return new Result(noteShowDto);
    }

    @PostMapping("/create/{notebookId}")
    public Result createNote(@PathVariable String notebookId) {
        //create 후 update임.
        return new Result(noteService.CreateBlankPage(notebookId));
    }

    @PutMapping("/update")
    public Result updateNote(@RequestBody NoteCreateDto noteCreateDto) {
        noteService.updateNote(noteCreateDto);
        return new Result("Note Updated");
    }

    @PostMapping("/delete/{noteId}")
    public Result deleteNote(@PathVariable String noteId) {
        //TODO: note와 note랑 tag 매핑된거는 삭제되는데, tag 테이블도 삭제를 해야하나? 해야겠지?
        noteService.deleteNote(noteId);
        return new Result("Note Deleted");
    }

    @PostMapping("/move/recycle_bin/{noteId}")
    public Result gotorecyclebin(@PathVariable String noteId) {
        //TODO: note와 note랑 tag 매핑된거는 삭제되는데, tag 테이블도 삭제를 해야하나? 해야겠지?
        noteService.moveRecycle(noteId);
        return new Result("Note Moved!");
    }


    //노트(페이지)공유시
    @PostMapping("/share")
    public Result createSharedNote(@RequestBody NoteCreateDto noteCreateDto) { //어디로? ( 보낼 CID가 있어야함)
        //TODO:: 컨트롤러단에서 뭐하는짓임 ㅋㅋ 서비스로 바꾸기
        //TODO:: 첫번째 공유일때 , 그 CID에 shared 노트북이 존재하지 않으면
        String destCID=noteCreateDto.getShared_room_name();

        if(! noteBookService.IsThereType(destCID,"shared_page")) {
            log.info("there is not  Shared_page Notebook!");
            String createdNotebookId = noteBookService.CreateDefaultSharedPage(noteCreateDto);//만들어진 노트북 id
            String cloneNoteId = noteService.CreateDefaultPage(createdNotebookId); //만들어진 디폴트 페이지 id
            noteCreateDto.setNoteid(cloneNoteId);
            noteCreateDto.setNotebookid(createdNotebookId);
        }
        else {
            log.info("there is Shared_page Notebook!");
            String sharednotebookid = noteBookService.FindShareNotebookId(destCID);
            System.out.println("sharednotebookid = " + sharednotebookid);
            String cloneNoteId = noteService.CreateDefaultPage(sharednotebookid); //만들어진 디폴트 페이지 id
            System.out.println("cloneNoteId = " + cloneNoteId);
            noteCreateDto.setNoteid(cloneNoteId);
            noteCreateDto.setNotebookid(sharednotebookid);

        }
        String updatedId = noteService.updateNote(noteCreateDto); //그 id로 업데이트함
        return new Result(updatedId);
    }


}
