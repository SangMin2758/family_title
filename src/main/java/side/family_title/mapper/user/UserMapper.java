package side.family_title.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import side.family_title.dto.FamilyGroup;
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
    public List<FamilyProfile> allFamilyList (String memberId);

    //추가한 가족 구성원 그룹 별 조회
    public List<FamilyGroup> familyMemberListByGroup (String memberId);

    //추가한 가족 구성원 정보 수정
    public void modifyFamilyMember (FamilyProfile familyProfile);

    //가족 구성원과 그룹 관계 삭제
    public void deleteProfileAssociation (String profileCode);

    //추가한 가족 구성원 삭제
    public void deleteFamilyMember (String profileCode);

    //가족 구성원 그릅 추가
    public void addFamilyGroup (FamilyGroup familyGroup);

    //가족 구성원 그릅 관계 맺기
    public void groupAssociation (String groupCode, String profileCode);

    //구성원 그릅과 그룹 관계 삭제
    public void deleteGroupAssociation (String groupCode);

    //그룹 삭제
    public void deleteGroup (String groupCode);

    //가족 구성원 그룹에서 삭제
    public void deleteFamilyByGroup (String profileCode, String groupCode);


}
