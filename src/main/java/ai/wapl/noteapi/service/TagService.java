package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.Note;
import ai.wapl.noteapi.domain.NoteBook;
import ai.wapl.noteapi.domain.NoteTagMap;
import ai.wapl.noteapi.domain.Tag;
import ai.wapl.noteapi.dto.ShowTagDto;
import ai.wapl.noteapi.dto.Tagdto;
import ai.wapl.noteapi.dto.note.NoteCreateDto;
import ai.wapl.noteapi.dto.note.NoteDtoByTags;
import ai.wapl.noteapi.exception.Exceptions.CNoteBookNotFoundException;
import ai.wapl.noteapi.exception.Exceptions.CTagMappingNotFountException;
import ai.wapl.noteapi.exception.Exceptions.CTagNotFoundException;
import ai.wapl.noteapi.repository.NoteBookRepository;
import ai.wapl.noteapi.repository.NoteRepository;
import ai.wapl.noteapi.repository.NoteTagMapRepository;
import ai.wapl.noteapi.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;
    private final NoteRepository noteRepository;
    private final NoteTagMapRepository noteTagMapRepository;

    private final NoteBookRepository noteBookRepository;

    public ShowTagDto showTagList(String CID){
        ShowTagDto showTagDto = new ShowTagDto();
        ArrayList<String> tagIdByCID = findTagIdByCID(CID);
        for (String tagId : tagIdByCID) {
            Tag tag = tagRepository.findByTagId(tagId).orElseThrow(CTagNotFoundException::new);
            String text = tag.getText();
            if(text.length() > 0) {
                Tagdto tagdto= Tagdto.builder().name(text).id(tagId).tagCount(1L).
                        build();
                char chName = text.charAt(0);
                if(chName>='0'&& chName<='9') //숫자면
                {
                    if(!showTagDto.getNUM().containsKey(chName)) // 'ㄱ' 없을 때. 그리고 ㄱ 에서 강낭콩,뭐시기 다 있으니까 강낭콩일때도 확인
                    {
                        showTagDto.getNUM().put(chName,new ArrayList<>());
                        showTagDto.getNUM().get(chName).add(tagdto);
                    }
                    else{ // 'ㄱ' 이 있을때
                        List<Tagdto> tagdtos = showTagDto.getNUM().get(chName);
                        boolean check= false;
                        for (Tagdto tmptagdto : tagdtos) { //가나다 , 강낭콩
                            if(tmptagdto.getName().equals(text)) //text와 'ㄱ'에 있는 값들 비교. 같으면 count 증가
                            {
                                tmptagdto.setTagCount(tmptagdto.getTagCount() + 1); //count증가
                                check=true;
                                break;
                            }
                        }
                        if(!check)
                        showTagDto.getNUM().get(chName).add(tagdto);
                    }
                }
                else if (chName>='A' && chName <='z')
                {
                    if(!showTagDto.getENG().containsKey(chName)) // 'ㄱ' 없을 때. 그리고 ㄱ 에서 강낭콩,뭐시기 다 있으니까 강낭콩일때도 확인
                    {
                        showTagDto.getENG().put(chName,new ArrayList<>());
                        showTagDto.getENG().get(chName).add(tagdto);
                    }
                    else{ // 'ㄱ' 이 있을때
                        boolean check=false;
                        List<Tagdto> tagdtos = showTagDto.getENG().get(chName);
                        for (Tagdto tmptagdto : tagdtos) { //가나다 , 강낭콩
                            if(tmptagdto.getName().equals(text)) //text와 'ㄱ'에 있는 값들 비교. 같으면 count 증가 증가시키면 다음 text보면 되지ㅏㄶ아
                                {
                                    tmptagdto.setTagCount(tmptagdto.getTagCount() + 1); //count증가
                                    check=true;
                                    break;
                                }
                            }
                        if(!check)
                             showTagDto.getENG().get(chName).add(tagdto);
                        }
                    }

                else if(chName>=0xAC00 && chName<=0xD7AF) //한글이면
                {
                    Character initialHangle = getInitialHangle(chName);

                    if(!showTagDto.getKOR().containsKey(initialHangle)) // 'ㄱ' 없을 때. 그리고 ㄱ 에서 강낭콩,뭐시기 다 있으니까 강낭콩일때도 확인
                    {
                        showTagDto.getKOR().put(initialHangle,new ArrayList<>());
                        showTagDto.getKOR().get(initialHangle).add(tagdto);
                    }
                    else{ // 'ㄱ' 이 있을때
                        boolean check=false;
                        List<Tagdto> tagdtos = showTagDto.getKOR().get(initialHangle);
                        for (Tagdto tmptagdto : tagdtos) { //가나다 , 강낭콩
                            if(tmptagdto.getName().equals(text)) //text와 'ㄱ'에 있는 값들 비교. 같으면 count 증가 증가시키면 다음 text보면 되지ㅏㄶ아
                            {
                                tmptagdto.setTagCount(tmptagdto.getTagCount() + 1); //count증가
                                check=true;
                                break;
                            }
                        }
                        if(!check)
                            showTagDto.getKOR().get(initialHangle).add(tagdto);
                    }
                }

                else{ //ETC에 넣기.
                    if(!showTagDto.getETC().containsKey(chName)) // 'ㄱ' 없을 때. 그리고 ㄱ 에서 강낭콩,뭐시기 다 있으니까 강낭콩일때도 확인
                    {
                        showTagDto.getETC().put(chName,new ArrayList<>());
                        showTagDto.getETC().get(chName).add(tagdto);
                    }
                    else{ // 'ㄱ' 이 있을때
                        List<Tagdto> tagdtos = showTagDto.getETC().get(chName);
                        for (Tagdto tmptagdto : tagdtos) { //가나다 , 강낭콩
                            if(tmptagdto.getName().equals(text)) //text와 'ㄱ'에 있는 값들 비교. 같으면 count 증가
                                tmptagdto.setTagCount(tmptagdto.getTagCount()+1); //count증가
                            else{ // 강낭콩은 있고 가나다는 없을 때.
                                showTagDto.getETC().get(chName).add(tagdto);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return showTagDto;
    }
    /**
     *
     */
    public ArrayList<String> findTagIdByCID(String CID){
        List<String> tagIds = new ArrayList<>();
        List<NoteBook> noteBooks = noteBookRepository.findAllByNoteChannelId(CID).orElseThrow(CNoteBookNotFoundException::new);
        for (NoteBook noteBook : noteBooks) {
            List<Note> noteList = noteBook.getNoteList();
            for (Note note : noteList) {
                    List<NoteTagMap> notemap = note.getNotemap();//NoteTagMap들이 있음.
                    for (NoteTagMap noteTagMap : notemap) {
                        tagIds.add(noteTagMap.getTagmap().getTagId());
                    }
            }
        }
        return (ArrayList<String>) tagIds;
    }

    static Character getInitialHangle(Character text) {
        Character[] chs = {
                'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
                'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
                'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
                'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        };
            //chName: 첫글자.
        if(text >= 0xAC00 ) // 가 보다 크면
            {
                int uniVal = text - 0xAC00;
                int cho = ((uniVal - (uniVal % 28))/28)/21;
                return chs[cho];
            }
        return null;
    }

    public ShowTagDto findNotesByTagtext(String CID, String findtext) {
        //챕터별 태그 보여주는거에서 name 이 text에 포함되는 친구 있으면 그거 리턴하면 될듯.
        ShowTagDto showTagDto = new ShowTagDto();
        ArrayList<String> tagIdByCID = findTagIdByCID(CID);
        for (String tagId : tagIdByCID) {
            Tag tag = tagRepository.findByTagId(tagId).orElseThrow(CTagNotFoundException::new);
            String text = tag.getText();
            System.out.println("text = " + text);
            if(text.length() > 0) {
                if(!text.contains(findtext)) continue;
                System.out.println(" HERE!!!");
                Tagdto tagdto= Tagdto.builder().name(text).id(tagId).tagCount(1L).
                        build();
                char chName = text.charAt(0);
                if(chName>='0'&& chName<='9') //숫자면
                {
                    if(!showTagDto.getNUM().containsKey(chName)) // 'ㄱ' 없을 때. 그리고 ㄱ 에서 강낭콩,뭐시기 다 있으니까 강낭콩일때도 확인
                    {
                        showTagDto.getNUM().put(chName,new ArrayList<>());
                        showTagDto.getNUM().get(chName).add(tagdto);
                    }
                    else{ // 'ㄱ' 이 있을때
                        List<Tagdto> tagdtos = showTagDto.getNUM().get(chName);
                        boolean check= false;
                        for (Tagdto tmptagdto : tagdtos) { //가나다 , 강낭콩
                            if(tmptagdto.getName().equals(text)) //text와 'ㄱ'에 있는 값들 비교. 같으면 count 증가
                            {
                                tmptagdto.setTagCount(tmptagdto.getTagCount() + 1); //count증가
                                check=true;
                                break;
                            }
                        }
                        if(!check)
                            showTagDto.getNUM().get(chName).add(tagdto);
                    }
                }
                else if (chName>='A' && chName <='z')
                {
                    if(!showTagDto.getENG().containsKey(chName)) // 'ㄱ' 없을 때. 그리고 ㄱ 에서 강낭콩,뭐시기 다 있으니까 강낭콩일때도 확인
                    {
                        showTagDto.getENG().put(chName,new ArrayList<>());
                        showTagDto.getENG().get(chName).add(tagdto);
                    }
                    else{ // 'ㄱ' 이 있을때
                        boolean check=false;
                        List<Tagdto> tagdtos = showTagDto.getENG().get(chName);
                        for (Tagdto tmptagdto : tagdtos) { //가나다 , 강낭콩
                            if(tmptagdto.getName().equals(text)) //text와 'ㄱ'에 있는 값들 비교. 같으면 count 증가 증가시키면 다음 text보면 되지ㅏㄶ아
                            {
                                tmptagdto.setTagCount(tmptagdto.getTagCount() + 1); //count증가
                                check=true;
                                break;
                            }
                        }
                        if(!check)
                            showTagDto.getENG().get(chName).add(tagdto);
                    }
                }

                else if(chName>=0xAC00 && chName<=0xD7AF) //한글이면
                {
                    Character initialHangle = getInitialHangle(chName);

                    if(!showTagDto.getKOR().containsKey(initialHangle)) // 'ㄱ' 없을 때. 그리고 ㄱ 에서 강낭콩,뭐시기 다 있으니까 강낭콩일때도 확인
                    {
                        showTagDto.getKOR().put(initialHangle,new ArrayList<>());
                        showTagDto.getKOR().get(initialHangle).add(tagdto);
                    }
                    else{ // 'ㄱ' 이 있을때
                        boolean check=false;
                        List<Tagdto> tagdtos = showTagDto.getKOR().get(initialHangle);
                        for (Tagdto tmptagdto : tagdtos) { //가나다 , 강낭콩
                            if(tmptagdto.getName().equals(text)) //text와 'ㄱ'에 있는 값들 비교. 같으면 count 증가 증가시키면 다음 text보면 되지ㅏㄶ아
                            {
                                tmptagdto.setTagCount(tmptagdto.getTagCount() + 1); //count증가
                                check=true;
                                break;
                            }
                        }
                        if(!check)
                            showTagDto.getKOR().get(initialHangle).add(tagdto);
                    }
                }

                else{ //ETC에 넣기.
                    if(!showTagDto.getETC().containsKey(chName)) // 'ㄱ' 없을 때. 그리고 ㄱ 에서 강낭콩,뭐시기 다 있으니까 강낭콩일때도 확인
                    {
                        showTagDto.getETC().put(chName,new ArrayList<>());
                        showTagDto.getETC().get(chName).add(tagdto);
                    }
                    else{ // 'ㄱ' 이 있을때
                        List<Tagdto> tagdtos = showTagDto.getETC().get(chName);
                        for (Tagdto tmptagdto : tagdtos) { //가나다 , 강낭콩
                            if(tmptagdto.getName().equals(text)) //text와 'ㄱ'에 있는 값들 비교. 같으면 count 증가
                                tmptagdto.setTagCount(tmptagdto.getTagCount()+1); //count증가
                            else{ // 강낭콩은 있고 가나다는 없을 때.
                                showTagDto.getETC().get(chName).add(tagdto);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return showTagDto;


        //챕터별 list 불러들이고, 그중에
    }


    public List<NoteDtoByTags> showTagList(String CID, String tagid) {
        //1. text에 맞는게 아니라 tagid구나.
        //tagid에 맞는 note들에서 정보 뺴면됨.
        List<NoteDtoByTags> noteDtoByTags = new ArrayList<>();
        Tag tag = tagRepository.findByTagId(tagid).orElseThrow(CTagNotFoundException::new);
        List<NoteTagMap> allByTagmap = noteTagMapRepository.findAllByTagmap(tag).orElseThrow(CTagMappingNotFountException::new);
        if(allByTagmap.isEmpty()) throw new CTagMappingNotFountException();
        for (NoteTagMap noteTagMap : allByTagmap) {
            NoteDtoByTags tmp = new NoteDtoByTags();
            Note note = noteTagMap.getNotemap();
            NoteBook mynotebook = note.getMynotebook();
            tmp.setNotebooktype(mynotebook.getTEXT());
            tmp.setNotebookid(mynotebook.getId());
            tmp.setNoteid(note.getNoteId());
            tmp.setNotetitle(note.getNoteTitle());
            noteDtoByTags.add(tmp);
        }
        return noteDtoByTags;
    }
}
