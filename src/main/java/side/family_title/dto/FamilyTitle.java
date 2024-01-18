package side.family_title.dto;

import lombok.Data;

@Data
public class FamilyTitle {
    private String familyCode;
    private String familyAppellation;
    private String familyTitle;
    private String familyRelation;
    private String nextRelation;
    private String shortRelation;
    private int familyGender;
    private String regDate;

}
