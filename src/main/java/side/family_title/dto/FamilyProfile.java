package side.family_title.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FamilyProfile {

    private String profileCode;
    private String familyCode;
    private String name;
    private String birthday;
    private String image;
    private String detailInfo;
    private String memberId;

    private FamilyTitle familyTitle;
    private GroupAssociation groupAssociation;
}
