package ai.wapl.noteapi.dto;//package ai.wapl.noteapi.dto.notebook;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Tagdto {
    String name;
    String id; //tagid
    Long tagCount; //그 tag에걸린 note 수.
}


