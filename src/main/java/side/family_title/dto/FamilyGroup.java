package side.family_title.dto;


import lombok.Data;

import java.util.List;

@Data
public class FamilyGroup {

    private String groupCode;
    private String groupName;
    private String memberId;
    private List<FamilyProfile> familyProfileList;

}
