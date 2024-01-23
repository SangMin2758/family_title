package side.family_title.service.user;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public List<FamilyProfile> familyMemberList (String memberId) {
        return userMapper.familyMemberList(memberId);
    }

    //추가한 가족 구성원 정보 수정
    public void modifyFamilyMember (FamilyProfile familyProfile) {userMapper.modifyFamilyMember(familyProfile);}

    //추가한 가족 구성원 삭제
    public void deleteFamilyMember (String profileCode){userMapper.deleteFamilyMember(profileCode);}
}

