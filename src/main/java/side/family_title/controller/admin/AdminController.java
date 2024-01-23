package side.family_title.controller.admin;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import side.family_title.dto.FamilyTitle;
import side.family_title.service.admin.AdminService;
import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    AdminService adminService;

    //가족호칭 추가 페이지
    @GetMapping("/addFamilyTitle")
    public String addFamilyTitle (Model model) {
        List<FamilyTitle> familyTitleList = adminService.familyTitleList();

        model.addAttribute("familyTitleList",familyTitleList);

        return "admin/addFamilyTitle";
    }


    //가족 호칭 중복여부 조사.
    @GetMapping("/isExist")
    @ResponseBody
    public Boolean isExist (@RequestParam(name="familyRelation", required = true) String familyRelation) {

        int isExist = adminService.isExist(familyRelation);

        if(isExist == 0 ) {
            return true;
        } else {
            return false;
        }
    }

    //가족 호칭 추가
    @PostMapping("/addFamilyTitle")
    @ResponseBody
    public String addFamilyTitle (@RequestBody FamilyTitle familyTitle) {
        adminService.addFamilyTitle(familyTitle);

        return "admin/addFamilyTitle";
    }

    //가족 호칭 삭제
    @GetMapping("/delFamilyTitle")
    public String delFamilyTitle (@RequestParam(name="familyCode") String familyCode) {
        adminService.delFamilyTitle(familyCode);

        return "redirect:/admin/addFamilyTitle";
    }

    //가족 호칭 수정
    @GetMapping("/modifyTitle")
    public String modifyTitle (@RequestParam(name="familyCode") String familyCode
                                ,@RequestParam(name="appellation") String appellation
                                ,@RequestParam(name="title") String title) {

        adminService.modifyTitle(familyCode, appellation, title);

        return  "redirect:/admin/addFamilyTitle";
    }


}
