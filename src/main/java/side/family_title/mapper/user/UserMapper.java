package side.family_title.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import side.family_title.dto.FamilyProfile;
import side.family_title.dto.FamilyTitle;

import java.util.List;


@Mapper
public interface UserMapper {

    //2촌 이하 가족 조회
    public List<FamilyTitle> firstFamily (String gender);

    //선택된 가족 호칭 검색
    public FamilyTitle searchFamily (String relation);

    //다음 선택할 가족 호칭 조회
    public List<FamilyTitle> nextFamily (String relation);

    //가족 구성원 추가하기
    public void addFamilyMember (String familyCode, String memberId);

    //추가한 가족 구성원 전체 조회
    public List<FamilyProfile> familyMemberList (String memberId);

}
