package ai.wapl.noteapi.controller;

import ai.wapl.noteapi.domain.response.Result;
import ai.wapl.noteapi.dto.ShowTagDto;
import ai.wapl.noteapi.dto.Tagdto;
import ai.wapl.noteapi.dto.note.NoteCreateDto;
import ai.wapl.noteapi.dto.note.NoteDtoByTags;
import ai.wapl.noteapi.dto.note.NoteShowDto;
import ai.wapl.noteapi.service.NoteService;
import ai.wapl.noteapi.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@Transactional
@Slf4j
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e) {
        System.out.println("Error in NoteBookController = " + e);
    }

    @GetMapping("/{CID}")
    public Result ShowTagList(@PathVariable String CID){
        //CID에 있는 태그들 다 정리해서 보여주기.
        ShowTagDto showTagDto = tagService.showTagList(CID);
        return new Result(showTagDto);
    }

    @GetMapping("/{CID}/{tagid}") //id 눌렀을때 보여주기.
    public Result ShowNotesByTagIdAndCID(@PathVariable String CID,@PathVariable String tagid){
        //CID에 있는 태그들 다 정리해서 보여주기.
        List<NoteDtoByTags> noteDtoByTags = tagService.showTagList(CID, tagid);
        return new Result(noteDtoByTags);
    }

    @GetMapping("/{CID}/text/{text}")  //text기준 보여주기
    public Result FindNotesByTagtext(@PathVariable(value="CID") String CID,@PathVariable(value="text") String text){
        //CID에 있는 태그들 다 정리해서 보여주기.
        ShowTagDto notesByTagtext = tagService.findNotesByTagtext(CID, text);
        return new Result(notesByTagtext);
    }

}
