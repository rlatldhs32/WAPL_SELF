package ai.wapl.noteapi.dto;//package ai.wapl.noteapi.dto.notebook;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@RequiredArgsConstructor
@ToString
public class ShowTagDto {
    Map<Character,List<Tagdto>> ENG = new ConcurrentHashMap<>();
    Map<Character,List<Tagdto>> KOR = new ConcurrentHashMap<>();
    Map<Character,List<Tagdto>> NUM = new ConcurrentHashMap<>();
    Map<Character,List<Tagdto>> ETC = new ConcurrentHashMap<>();
}


