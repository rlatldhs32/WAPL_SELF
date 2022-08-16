package ai.wapl.noteapi.service;

import ai.wapl.noteapi.domain.*;
import ai.wapl.noteapi.dto.note.NoteCreateDto;
import ai.wapl.noteapi.dto.note.NoteShowDto;
import ai.wapl.noteapi.exception.Exceptions.*;
import ai.wapl.noteapi.repository.NoteBookRepository;
import ai.wapl.noteapi.repository.NoteRepository;
import ai.wapl.noteapi.repository.NoteTagMapRepository;
import ai.wapl.noteapi.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteBookRepository noteBookRepository;
    private final TagRepository tagRepository;

    private final NoteTagMapRepository noteTagMapRepository;

    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public String CreateDefaultPage(String NoteBookID) {
        return Create_Page_With_NoteBookId_And_Title(NoteBookID, "새 페이지");
    }

    public String CreateBlankPage(String NoteBookID) {
        return Create_Page_With_NoteBookId_And_Title(NoteBookID, "(제목 없음)");

    }

    private String Create_Page_With_NoteBookId_And_Title(String NoteBookID, String NoteTitle) {
        String newId = getRandid();
        String CreatedDate = NowTime();
        String ModifiedDate = NowTime();
        String NoteContent = "<p><br></p>";
        String TextContent = NoteContent.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        Note note = Note.builder()
                .mynotebook(noteBookRepository.findById(NoteBookID).orElseThrow(CNoteBookNotFoundException::new)) // 예외처리 방식에 대한 이해 필요. 되는지 확인하기.
                .noteId(newId)
                .NoteTitle(NoteTitle)
                .CreatedDate(CreatedDate)
                .ModifiedDate(ModifiedDate)
                .IsFavorite("none")
                .NoteContent(NoteContent)
                .TextContent(TextContent)
                .build();
        noteRepository.save(note);
        return newId;
    }

    @Transactional
    public void moveRecycle(String noteId) {
        //노트는 그대로 두고 parent notebook id를 옮기면됨.
        Note note = noteRepository.findByNoteId(noteId).orElseThrow(CNoteNotFoundException::new);
        String noteChannelId = note.getMynotebook().getNoteChannelId();
        if (noteChannelId == null) throw new CChannelNotFoundException();
        //1.noteid를 통해 parent_notebook를 찾고
        //2. 노트북을 통해 CID를 찾고
        NoteBook recycle_binNotebook = noteBookRepository.findByNoteChannelIdAndType(noteChannelId, "recycle_bin").orElseThrow(CNoteBookNotFoundException::new);

        //3. 그것들 중에서 휴지통의 notebook id 를 찾고 ( 없으면 에러)
        //부모를 휴지통으로 바꾸어주면됨.
        //4. 기존 note의 parent_notebook을 바꾸면 됨.
        note.setMynotebook(recycle_binNotebook);
        return;
    }


    @Transactional
    public void deleteNote(String noteId) {
        System.out.println(" delete 1");
        Note note = noteRepository.findByNoteId(noteId).orElseThrow(CNoteNotFoundException::new);
        System.out.println(" delete 2");

        List<NoteTagMap> allByNotemap = noteTagMapRepository.findAllByNotemap(note);
        System.out.println(" delete 3");


        noteRepository.deleteByNoteId(noteId);
        /**
         * 밑에 코드 넣으면 알아서 지워지지않더라.
         */
//        for (NoteTagMap NoteTagMap : allByNotemap) {
//            Tag tagmap = NoteTagMap.getTagmap();
//            System.out.println("tagmap = " + tagmap);
//            System.out.println("tagmap.getText() = " + tagmap.getText());
//            System.out.println("tagmap.getTagId() = " + tagmap.getTagId());
//            tagRepository.deleteByTagId(tagmap.getTagId()); //이게 안되나봐.
//        }
    }

//    @Transactional
//    public void updateNote(String noteId){
//        Note note = noteRepository.findByNoteId(noteId).orElseThrow(CNoteNotFoundException::new);
//
//        noteRepository.deleteByNoteId(noteId);
//    }


    @Transactional
    public String updateNote(NoteCreateDto noteCreatedto) {
        log.info("노트 업데이트!1");
        //노트북 찾기
        String notebookid = noteCreatedto.getNotebookid();
        NoteBook noteBook = noteBookRepository.findById(notebookid).orElseThrow(CNoteBookNotFoundException::new);
        log.info("기존 notebook정보 : ");
//        System.out.println("noteBook.getTEXT() = " + noteBook.getTEXT());
//        System.out.println("noteBook.getId() = " + noteBook.getId());
//        System.out.println("noteBook.getNoteChannelId() = " + noteBook.getNoteChannelId());
//        System.out.println("noteBook.toString() = " + noteBook.toString());
        log.info("노트 업데이트!2");
        //노트찾기
        Note beforenote = noteRepository.findByNoteId(noteCreatedto.getNoteid()).orElseThrow(CNoteNotFoundException::new);
        log.info("노트 업데이트3!");
        Note note = noteCreatedto.toEntity(noteBook, NowTime(), beforenote.getCreatedDate()); //여기에 부모가없네. 이것만 하면 되는듯?
//        log.info("여기 봐봐 시온아!");
//
//        System.out.println("note.getMynotebook() = " + note.getMynotebook());
        log.info("노트 업데이트4!");

        noteRepository.save(note);
        log.info("노트 업데이트!5");

        //노트에 태그들 저장하기
        List<String> tags = noteCreatedto.getTags();
        log.info("노트 업데이트!6");

        if (tags != null)
            updateTags(note, tags); //태그가 널이면 안되는듯?

        log.info("노트 업데이트!7");

        return note.getNoteId();
    }

    public NoteShowDto Note_Show_With_Id(String Id) {
        log.info("show note with id");
        Note note = noteRepository.findByNoteId(Id).orElseThrow(CNoteNotFoundException::new);
        if (note == null) return null;
        NoteShowDto noteShowDto = note.toShowDto();

        List<NoteTagMap> allByNotemap = noteTagMapRepository.findAllByNotemap(note);
        List<String> exists = new ArrayList<>();

        for (NoteTagMap NoteTagMap : allByNotemap) {
            exists.add(NoteTagMap.getTagmap().getText());
        }

        noteShowDto.setTagTexts(exists);

        return noteShowDto;
    }


    //태그 더하기
    @Transactional
    public void addTag(Note note, String text) {
        //text가 같은 게 있으면 연결됨.
        Tag tag = tagRepository.findByText(text)
                .orElse(tagRepository.save(Tag.builder().text(text).tagId(getRandid()).
                        build()));
        Note byId = noteRepository.findById(note.getNoteId())
                .orElseThrow(() -> new CNoteNotFoundException());

        noteTagMapRepository.save(NoteTagMap.builder()
                .notemap(byId).tagmap(tag).build());
        //연결 됨.
    }


    /**
     * TODO : 이미 있는 Tag 중복으로 여러명 할 때 tag 테이블은 만들어지는데 mapping은 한 노트로만 되고있음 하하
     *
     * @param note
     * @param texts
     */
    @Transactional
    public void addTags(Note note, List<String> texts) {
        //text가 같은 게 있으면 연결됨.
        //현재  tag_id가 중복으로 들어가벌임.
        for (String text : texts) {

////            Tag save = tagRepository.save(Tag.builder().text(text).tagId(getRandid()).build());
//            Tag sion =tagRepository.findByText(text).orElseThrow(CTagNotFoundException::new);
//            System.out.println("sion.getText() = " + sion.getText());
////            Tag save = tagRepository.findByText(text).orElse(tagRepository.save(Tag.builder().text(text).tagId(getRandid()).build()));
            List<Tag> tag= tagRepository.findAllByText(text);
            Tag sion = new Tag();
            if(tag.isEmpty()) sion = tagRepository.save(Tag.builder().text(text).tagId(getRandid()).build());
            else sion = tag.get(0);
            System.out.println("sion = " + sion);
//            //또 비교.
//            //이 때 tag가, 맨 처음 발견한 tag를 발견함. 이게 문제임.
//            for (Tag tagbytext : tags) { //text타고 올라간 tag id 와 매핑된 note id와 // 기존 note id를 비교
//                //같아야 tag 연결해줌.
//                System.out.println("tagbytext = " + tagbytext);
//                String noteIdFromText = noteTagMapRepository.findByTagmap(tagbytext).orElseThrow(CTagMappingNotFountException::new).getNotemap().getNoteId();
//                System.out.println(" text 타고 올라온 noteid : " + noteIdFromText);
//                System.out.println(" 바꿔야할 note id = " + note.getNoteId());
//                if (noteIdFromText.equals(note.getNoteId())) {
//                    System.out.println(" 바꿀 떄 :! ");
//                    NoteTagMap mapping = noteTagMapRepository.findByNotemapAndTagmap(note, tagbytext).orElseThrow(CTagMappingNotFountException::new);
//                    noteTagMapRepository.delete(mapping);
//                }

//

                //

                Note byId = noteRepository.findById(note.getNoteId())
                        .orElseThrow(() -> new CNoteNotFoundException());

                noteTagMapRepository.save(NoteTagMap.builder()
                        .notemap(byId).tagmap(sion).build());
            }
        }



        @Transactional
        public void updateTags (Note note, List < String > texts){
            //TODO:: set으로 바꿔보기
            List<NoteTagMap> allByNotemap = noteTagMapRepository.findAllByNotemap(note);
            List<String> exists = new ArrayList<>();

            for (NoteTagMap NoteTagMap : allByNotemap) {
                exists.add(NoteTagMap.getTagmap().getText());
            }
            //note -> notebook -> cid를 찾아야함.

            //A에는 있는데 B에는 없다 -> 제거
            ArrayList<String> Existed = difference(exists, texts);
            ArrayList<String> To_add = difference(texts, exists);
            System.out.println("To_add = " + To_add);
            System.out.println("Existed = " + Existed);
            if (!Existed.isEmpty())
                removeTags(note, Existed);
            System.out.println(" ONE ! ");
            if (!To_add.isEmpty())
                addTags(note, To_add);
            System.out.println(" ONE 2! ");
            log.info("after Update Tags");
        }

        /**
         * TODO: CID를 받아와서 TAG를 지워야함.
         * TODO:
         */
        @Transactional
//    public void removeTags(Note note, ArrayList<String> existed,String CID) {
        public void removeTags (Note note, ArrayList < String > existed){  //현재 : 옮기는건되는데 삭제가 안됨.

            //현재 지우려는 노트, 지우려는 글자들 --> tag 테이블에서 findbytext로 찾을때 문제 -> 두개이상이니까, tagid를 다 뒤져보고, 그 tag_id의 매핑 테이블에서 note와 같은걸 삭제함.

            //노트 객체 , 있는 글자들임.
//        String NCID=FindNoteCID(note);
            Note note1 = noteRepository.findByNoteId(note.getNoteId()).orElseThrow(CNoteBookNotFoundException::new);
//        System.out.println("note1.getNoteId() = " + note1.getNoteId());
//        System.out.println("note1.getNoteTitle() = " + note1.getNoteTitle());
//        System.out.println("note1.getMynotebook() = " + note1.getMynotebook()); ::TODO: 이걸 넣으면 에러인건가?
//        System.out.println("note1 = " + note1.toString());
            for (String text : existed) {
                System.out.println("text = " + text);
//            List<Tag> tags = tagRepository.findListByText(text).orElseThrow(CTagNotFoundException::new);
//            for (Tag tag : tags) {
//                String tagId = tag.getTagId();
//            }
                //TODO: 같은 text의 태그가 2개일때 생각해야함.
                //TODO: 같은 text의 태그가 1개일때도 안되고있음

                List<Tag> allByText = tagRepository.findAllByText(text);
                if(allByText.isEmpty()) throw new CTagNotFoundException();
                for (Tag tagbytext : allByText) { //text타고 올라간 tag id 와 매핑된 note id와 // 기존 note id를 비교
                    System.out.println("tagbytext = " + tagbytext);
                    String noteIdFromText = noteTagMapRepository.findByTagmap(tagbytext).orElseThrow(CTagMappingNotFountException::new).getNotemap().getNoteId();
                    System.out.println(" text 타고 올라온 noteid : " + noteIdFromText);
                    System.out.println(" 바꿔야할 note id = " + note1.getNoteId());
                    if (noteIdFromText.equals(note1.getNoteId())) {
                        System.out.println(" 바꿀 떄 :! ");
                        NoteTagMap mapping = noteTagMapRepository.findByNotemapAndTagmap(note1, tagbytext).orElseThrow(CTagMappingNotFountException::new);
                        noteTagMapRepository.delete(mapping);
                        tagRepository.delete(tagbytext);
                        //순서 바꿔보기..?
                    }
                }

//            Tag tag = tagRepository.findByText(text).orElseThrow(CTagNotFoundException::new); //<<여기서, 같은 text에 여러 TAGID가 나올 수 있음.
                //같은 notebook에선 당연히 되고, cid가 다른 노
                //지우려는


//            System.out.println("tag.getText() = " + tag.getText());
                //한 CID에서 같은 text는 하나고, CID별 text는 하나임.
                //따라서 text를 받아서 id를 읽는데, tag_id를 타고 가서 cid가 다를 때를 봐야함.
                //findAllbytext로 list만든다음에, 하나하나 비교해서 맞으면 바로 ㄱㄱ? --> 너무 성능이 안좋을 것 같은데...
//            System.out.println("tag = " + tag);
//            System.out.println("tag.getText() = " + tag.getText());
                //아니면 여기서 tag가 여러개 생겨서 그런듯?
//            NoteTagMapId NoteTagMapId = new NoteTagMapId(note.getNoteId(),tag.getTagId()); //TODO: 기존코드임!
//            NoteTagMapId NoteTagMapId = new NoteTagMapId(note1,tag);
//            System.out.println("note1.getNoteId() = " + note1.getNoteId());


//            NoteTagMap mapping = noteTagMapRepository.findByNotemapAndTagmap(note1, tag).orElseThrow(CTagMappingNotFountException::new);

//            System.out.println("NoteTagMapId.getTagmap() = " + NoteTagMapId.getTagmap());
//            System.out.println("NoteTagMapId = " + NoteTagMapId);
//            System.out.println("NoteTagMapId.getNotemap() = " + NoteTagMapId.getNotemap());
                //혹시 같은 노트번호/태그번호 있는 값이 있는건가?
//            NoteTagMap mapping = NoteTagMapRepository.findNoteTagMap(NoteTagMapId).orElseThrow(CTagMappingNotFountException::new); //여기가 에러
//            System.out.println("mapping.toString() = " + mapping.toString());
//            System.out.println("mapping = " + mapping);

                //노트와 태그 연결 삭제해야함.
//            noteTagMapRepository.delete(mapping);
            }
        }

        private String FindNoteCID (Note note){
            NoteBook mynotebook = note.getMynotebook();
            NoteBook noteBook = noteBookRepository.findById(mynotebook.getId()).orElseThrow(CNoteBookNotFoundException::new);
            return noteBook.getNoteChannelId();
        }
//
//    private String FindTagCID(Tag tag){
//        String tagId = tag.getTagId();
//        NoteTagMapRepository.findby
//        NoteBook mynotebook = note.getMynotebook();
//        NoteBook noteBook = noteBookRepository.findById(mynotebook.getId()).orElseThrow(CNoteBookNotFoundException::new);
//        return noteBook.getNoteChannelId();
//    }


        private static String getRandid () {
            //가정 : uuid 중복은 체크 안해도 됨?
            return UUID.randomUUID().toString();
        }
        private String NowTime () {
            Date time = new Date();
            String localTime = format.format(time);
            return localTime;
        }
        private static String getRandcolor () {
            return "#3A7973";
        }


        private static <T > ArrayList < T > intersection(ArrayList < T > list1, ArrayList < T > list2) // 교집합
        {
            ArrayList<T> result = new ArrayList<>(15);
            result.addAll(list1);
            result.retainAll(list2);
            return result;
        }

        private static <T > ArrayList < T > difference(List < String > list1, List < String > list2) // 차집합
        {
            ArrayList<T> result = new ArrayList<>(15);
            result.addAll((Collection<? extends T>) list1);
            result.removeAll(list2);
            return result;
        }
    }
