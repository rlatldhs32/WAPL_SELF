package ai.wapl.noteapi.repository;

import ai.wapl.noteapi.domain.Note;
import ai.wapl.noteapi.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {

    void deleteByTagId(String s);
    Optional<Tag> findByTagId(String id);

    /**
     * 태그 text가 주어지면 그에 맞는 노트들을 보여줘야함.
     * 1. text -> tagid
     * @return
     */

    Optional<Tag> findByText(String text);

    List<Tag> findAllByText(String text);

    Optional<List<Tag>> findListByText(String text);

    /**
     * 노트별 모든 태그 볼 수 있게해야함.
     */



}
