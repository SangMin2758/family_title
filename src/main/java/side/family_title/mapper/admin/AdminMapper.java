package side.family_title.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import side.family_title.dto.FamilyTitle;

import java.util.List;

@Mapper
public interface AdminMapper {

    //가족호칭 중복여부 조회
    public int isExist (String familyRelation);

    //가족호칭 추가
    public void addFamilyTitle (FamilyTitle familyTitle);

    //가족호칭 조회
    public List<FamilyTitle> familyTitleList();

    //가족호칭 삭제
    public void delFamilyTitle (String familyCode);

    //가족호칭 수정
    public void modifyTitle (String familyCode, String appellation, String title);

    //가족관계 요약
    public String shortRelation (String beforeRelation);

}
