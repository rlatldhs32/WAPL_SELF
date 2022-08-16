package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.NoteBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteBookRepository extends JpaRepository<NoteBook, String> {
    public Optional<NoteBook> findById(String id);

    boolean existsByNoteChannelIdAndType(String cid, String type);

    void deleteAllByNoteChannelId(String cid);
    Optional<NoteBook> findByNoteChannelIdAndType(String cid, String type);

//    Optional<NoteBook> findByNoteChannelId(String cid);

    Optional<List<NoteBook>> findAllByNoteChannelId(String cid);
}
