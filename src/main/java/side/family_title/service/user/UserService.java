package side.family_title.service.user;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.family_title.dto.FamilyGroup;
import side.family_title.dto.FamilyProfile;
import side.family_title.dto.FamilyTitle;
import side.family_title.mapper.user.UserMapper;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    UserMapper userMapper;

    //2촌 이하 가족 조회
    public List<FamilyTitle> firstFamily (String gender){
        return userMapper.firstFamily(gender);
    }

    //가족 호칭 검색
    public FamilyTitle searchFamily (String relation){
        return userMapper.searchFamily(relation);
    }

    //다음 가족 호칭 조회
    public List<FamilyTitle> nextFamily (String relation) {
        return userMapper.nextFamily(relation);
    }


    //가족 구성원 추가하기
    public void addFamilyMember (String familyCode, String memberId) {
        userMapper.addFamilyMember(familyCode, memberId);
    }

    //추가한 가족 구성원 전체 조회
    public List<FamilyProfile> allFamilyList (String memberId) {
        return userMapper.allFamilyList(memberId);
    }

    //추가한 가족 구성원 그룹 별 조회
    public List<FamilyGroup> familyMemberListByGroup (String memberId) {
        return userMapper.familyMemberListByGroup(memberId);
    }

    //추가한 가족 구성원 정보 수정
    public void modifyFamilyMember (FamilyProfile familyProfile) {userMapper.modifyFamilyMember(familyProfile);}

    //가족 구성원 정보 수정 시, 등록된 프로필 사진 있는지 조회. 있다면 기존사진 삭제 후 새로운 사진 추가하기 위함.
    public String originFileRoot (String profileCode) {
        return userMapper.originFileRoot(profileCode);
    }

    //추가한 가족 구성원 그룹관계 삭제 후 프로필 삭제
    public void deleteFamilyMember (String profileCode){
        userMapper.deleteProfileAssociation(profileCode);
        userMapper.deleteFamilyMember(profileCode);
    }

    //가족 구성원 그릅 추가
    public void addFamilyGroup (FamilyGroup familyGroup) {

        userMapper.addFamilyGroup(familyGroup);

        String groupCode = familyGroup.getGroupCode();

        List<FamilyProfile> familyProfileList = familyGroup.getFamilyProfileList();

        for(FamilyProfile familyProfile : familyProfileList) {
            String profileCode = familyProfile.getProfileCode();
            userMapper.groupAssociation(groupCode, profileCode);
        }
    }

    //그룹과 그룹관계 삭제 후 그룹 삭제
    public void deleteGroup (List<String> groupCodeList){

        for(String groupCode : groupCodeList) {
            userMapper.deleteGroupAssociation(groupCode);
            userMapper.deleteGroup(groupCode);
        }
    }

    //가족 구성원 그룹에서 삭제
    public void deleteFamilyByGroup (String profileCode, String groupCode){
        userMapper.deleteFamilyByGroup(profileCode, groupCode);
    }

    //회원가입 진행
    public void goAccount (String loginId) {
        //회원가입 된 회원인지 조회
        int isAcoount = userMapper.isAccount(loginId);
        //가입 됐으면 안된 회원이면 회원가입 진행.
        if(isAcoount == 0) {
            userMapper.goAccount(loginId);
        }

    }
}

