package side.family_title.controller.user;


import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import side.family_title.dto.FamilyProfile;
import side.family_title.dto.FamilyTitle;
import side.family_title.service.admin.AdminService;
import side.family_title.service.user.UserService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    UserService userService;
    AdminService adminService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    //가족 호칭 검색
    @GetMapping("/searchFamily")
    @ResponseBody
    public Map<String,Object> searchFamily (@RequestParam(name="relation")String relation) {

        Map<String,Object> map = new HashMap<>();
        FamilyTitle selectedFamily = userService.searchFamily(relation);
        List<FamilyTitle> nextFamilyList = userService.nextFamily(relation);
        map.put("selectedFamily",selectedFamily);
        map.put("nextFamilyList",nextFamilyList);

        log.info("선택된 가족 : {}", selectedFamily);
        log.info("다음 가족 : {}", nextFamilyList);

        return map;
    }

    //성별 별 호칭 조회
    @GetMapping("/familyListByGender")
    @ResponseBody
    public List<FamilyTitle> familyListByGender (@RequestParam(name="gender")String gender) {

        List<FamilyTitle> familyList = userService.firstFamily(gender);

        log.info("성별별 다음 가족 : {}", familyList);

        return familyList;
    }

    //호칭 목록 페이지
    @GetMapping("/titleList")
    public String titleListPage (Model model) {
        List<FamilyTitle> familyTitleList = adminService.familyTitleList();

        model.addAttribute("familyTitleList",familyTitleList);
        return"/user/titleList";
    }

    //가족 구성원 추가
    @GetMapping("/addFamilyMember")
    @ResponseBody
    public void addFamilyMember (@RequestParam(name="familyCode") String familyCode) {

        userService.addFamilyMember(familyCode, "id001");

    }


    //구성원 관리 페이지
    @GetMapping("/familyGroup")
    public String familyGroupPage (Model model) {

        List<FamilyProfile> familyProfileList = userService.familyMemberList("id001");

        model.addAttribute("familyProfileList", familyProfileList);

        log.info("추가된 가족 구성원 : {}", familyProfileList);

        return"/user/familyGroup";
    }


    //프로필 사진 업로드
    @PostMapping(value="/uploadImage", produces = "application/json")
    @ResponseBody
    public JsonObject uploadImage (@RequestParam("file") MultipartFile uploadFile,
                                   HttpServletRequest request) {


        JsonObject result = new JsonObject();

        String fileRoot = getOsFileRootPath();

        String orginalName = uploadFile.getOriginalFilename();
        assert orginalName != null;
        String fileName = orginalName.substring(orginalName.lastIndexOf(File.separator) + 1);
        String uuid = UUID.randomUUID().toString();
        String saveName = fileRoot + File.separator + uuid + "_" + fileName;
        System.out.println("saveName: " + saveName);
        System.out.println("fileRoot: " + fileRoot);

        Path savePath = Paths.get(saveName);

        try {
            uploadFile.transferTo(savePath);
            result.addProperty("fileName", saveName);
            result.addProperty("responseCode", "success");

        } catch (IOException e) {
            result.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        System.out.println(result);

        return result;
    }

    //저장 경로 설정
    public String getOsFileRootPath(){
        String os = System.getProperty("os.name").toLowerCase();
        String rootPath = "/home/springboot/resources/fmaily_image/";
        if (os.contains("win")) {
            rootPath = "C:\\fmaily_image\\";
        } else if (os.contains("mac")) {
            rootPath = "/Users/Shared/family_image/";

        }

        return rootPath;
    }

    //가족 구성원 정보 수정
    @PostMapping("/modifyFamilyMember")
    @ResponseBody
    public void modifyFamilyMember (@RequestBody FamilyProfile modifyObj) {

        System.out.println(modifyObj);
    }

}
