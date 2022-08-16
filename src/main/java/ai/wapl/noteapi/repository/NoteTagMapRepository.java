package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteTagMapRepository extends JpaRepository<NoteTagMap, NoteTagMapId> {
    //한 노트에 있는 태그id들(TAG_ID) 찾기
    //note 객체로 나머지 id 찾기가능

    List<NoteTagMap> findAllByNotemap(Note note); //신기한게 NoteTaMap이 String일때도 되고 Note일때도됨.. ??
    Optional<List<NoteTagMap>> findAllByTagmap(Tag tag); //신기한게 NoteTaMap이 String일때도 되고 Note일때도됨.. ??


//    Optional<NoteTagMap> findByIdNoteTagMapId(NoteTagMapId NoteTagMapId);

    Optional<NoteTagMap> findByNotemapAndTagmap(Note note,Tag tag);

    Optional<NoteTagMap> findByTagmap(Tag tag); //

}
