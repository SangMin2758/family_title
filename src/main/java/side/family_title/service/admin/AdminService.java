package side.family_title.service.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.family_title.dto.FamilyTitle;
import side.family_title.mapper.admin.AdminMapper;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AdminService {

    AdminMapper adminMapper;

    //가족호칭 중복여부 조회
    public int isExist (String familyRelation) {
        return adminMapper.isExist(familyRelation);
    }

    //가족호칭 추가
    public void addFamilyTitle (FamilyTitle familyTitle) {adminMapper.addFamilyTitle(familyTitle);}

    //가족호칭 조회
    public List<FamilyTitle> familyTitleList() {
        List<FamilyTitle> familyTitleList = adminMapper.familyTitleList();


        for (FamilyTitle familyTitle : familyTitleList) {
            String relation = familyTitle.getFamilyRelation();

            if(relation.contains("의")) {
                int idx = relation.lastIndexOf("의");
                //가장 최근 가족관계
                String beforRelation = relation.substring(0,idx);
                //마지막 가족관계
                String lastRelation = relation.substring(idx+1);

                String beforeRelationStr = adminMapper.shortRelation(beforRelation);
                familyTitle.setShortRelation(beforeRelationStr + "의 " + lastRelation);

            } else {
                familyTitle.setShortRelation(relation);
            }

        }

        return familyTitleList;
    }

    //가족호칭 삭제
    public void delFamilyTitle (String familyCode) {
        adminMapper.delFamilyTitle(familyCode);
    }

    //가족호칭 수정
    public void modifyTitle (String familyCode, String appellation, String title){
        adminMapper.modifyTitle(familyCode, appellation, title);
    }

}
