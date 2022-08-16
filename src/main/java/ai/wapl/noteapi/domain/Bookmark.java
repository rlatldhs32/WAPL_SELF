package ai.wapl.noteapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@RequiredArgsConstructor
//@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {

    /**
     * 각 칼럼의 길이는 추후 @Column(length = 1024) 같은 것을 통해 조정해보기
     */
    @Id
    private String id;



}
