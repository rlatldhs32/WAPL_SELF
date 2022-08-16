package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.domain.NoteBook;
import ai.wapl.noteapi.domain.response.Result;
import ai.wapl.noteapi.dto.note.NoteCreateDto;
import ai.wapl.noteapi.dto.notebook.NotebookDto;
import ai.wapl.noteapi.dto.notebook.NotebookUpdatedto;
import ai.wapl.noteapi.service.NoteBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestControllerAdvice
@Transactional
@Slf4j
@RequestMapping("/api/notebook")
public class NoteBookController {

    private final NoteBookService noteBookService;

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e) {
        System.out.println("Error in NoteBookController = " + e);// log로찍기.
    }


    @GetMapping("/{notebookid}")
    public Result ShowChannel(@PathVariable String notebookid){
        return new Result(noteBookService.findnoteBook(notebookid));
    }

    @GetMapping("/new")
    public Result createdefaultNoteBook(){
        return new Result(noteBookService.CreateDefaultNoteBooks());
    }


    @PostMapping("/delete/{id}")
    public Result deleteNoteBook(@PathVariable String id){
        noteBookService.deleteNotebook(id);
        return new Result("Deleted Notebook : "+id);
    }

    @PostMapping("/delete/channel/{id}")
    public Result deleteChannel(@PathVariable String id){
        noteBookService.deleteChapter(id);
        return new Result("Deleted Notebook : "+id);
    }

    //TODO:: 노트북 공유 고치기!
    @PostMapping("/share")
    public Result createSharedNoteBook(@RequestBody NotebookDto notebookDto) { //어디로? ( 보낼 CID가 있어야함)
        //노트북 ID
        log.info("Notebook Share start");

        return new Result(noteBookService.notebookshare(notebookDto));
    }


    @PutMapping("/update")
    public Result updateNote(@RequestBody NotebookUpdatedto notebookUpdatedto) {
        return new Result(noteBookService.updatenotebook(notebookUpdatedto));

    }


}
