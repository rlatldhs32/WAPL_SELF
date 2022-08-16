package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Note;
import ai.wapl.noteapi.domain.NoteBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, String> {
    public Optional<Note> findByNoteId(String noteId);

//    @Transactional
    void deleteByNoteId(String noteId);
}
