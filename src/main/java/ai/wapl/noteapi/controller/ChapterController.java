package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.domain.response.Result;
import ai.wapl.noteapi.dto.note.NoteShowDto;
import ai.wapl.noteapi.dto.notebook.NotebookDto;
import ai.wapl.noteapi.service.NoteBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestControllerAdvice
@Transactional
@Slf4j
@RequestMapping("/api/channel")
public class ChapterController {

    private final NoteBookService noteBookService;

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e) {
        System.out.println("Error in Chaper = " + e);// log로찍기.
    }


    @GetMapping("/{CID}")
    public Result ShowChannel(@PathVariable String CID){
        return new Result(noteBookService.findChapter(CID));
    }
}
