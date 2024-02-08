package side.family_title.controller.user;


import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import side.family_title.dto.FamilyGroup;
import side.family_title.dto.FamilyProfile;
import side.family_title.dto.FamilyTitle;
import side.family_title.service.admin.AdminService;
import side.family_title.service.user.UserService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
        return"user/titleList";
    }

    //가족 구성원 추가
    @GetMapping("/addFamilyMember")
    @ResponseBody
    public void addFamilyMember (@RequestParam(name="familyCode") String familyCode,
                                 @RequestParam(name="loginId") String loginId) {

        userService.addFamilyMember(familyCode, loginId);

    }


    //구성원 관리 페이지
    @GetMapping("/myFamily")
    public String familyGroupPage (Model model, HttpSession httpSession) {

        String loginId = (String) httpSession.getAttribute("SID");
        //전체 가족 구성원 조회
        List<FamilyProfile> allFamilyList = userService.allFamilyList(loginId);
        //그룹 별 가족 구성원 조회
        List<FamilyGroup> familyGroupList = userService.familyMemberListByGroup(loginId);

        model.addAttribute("allFamilyList", allFamilyList);
        model.addAttribute("familyGroupList", familyGroupList);

        log.info("나의 가족 구성원 : {}", familyGroupList);

        return"user/myFamily";
    }


    //프로필 사진 업로드
    @PostMapping(value="/uploadImage", produces = "application/json")
    @ResponseBody
    public Map<String,String> uploadImage (@RequestParam("file") MultipartFile multipartFile,
                                   HttpServletRequest request) {

        Map<String,String> result = new HashMap<>();

        //기존에 저장된 파일 있는지 조회. 있다면 삭제 진행 후 새 사진 업로드, 아니면 그냥 바로 업로드

        String fileRoot = getOsFileRootPath();
        //OS 별 저장될 외부 파일 경로 지정
        String orginalName = multipartFile.getOriginalFilename();
        //기존 파일명
        String fileName = orginalName.substring(orginalName.lastIndexOf(File.separator) + 1);
        //기존 파일명이 전체 경로로 들어올 경우 마지막 파일 명만 남기기.
        String uuid = UUID.randomUUID().toString();
        // 중복 방지를 위해 랜덤 파일명 생성
        String saveName = uuid + "_" + fileName;
        // 저장될 파일 이름 : [랜덤이름] + [_] + [기존 파일명]
        String saveFullName = fileRoot + saveName;
        // 저장될 경로 + 저장될 파일 이름
        Path savePath = Paths.get(saveFullName);

        try {
            multipartFile.transferTo(savePath);
            result.put("saveName",saveName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result);

        return result;
    }


    //기존에 파일 있는지 조회, 있다면 기존 파일 삭제.
    @PostMapping(value="/originFileRoot")
    @ResponseBody
    public String originFileRoot(@RequestBody String profileCode) {

        String originFileName = userService.originFileRoot(profileCode);

        if(originFileName != null) {
            deleteFile(originFileName);
        }
        return originFileName;
    }

    // 기존 파일 삭제
    private void deleteFile(String originFileName) {
        // 폴더 위치
        String filePath = getOsFileRootPath();
        Path path = Paths.get(filePath,originFileName);

        try {
            Files.delete(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //OS 별 파일 저장 경로 설정
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
    public FamilyProfile modifyFamilyMember (@RequestBody FamilyProfile modifyObj) {

        log.info("수정할 정보 : {}" , modifyObj);

        userService.modifyFamilyMember(modifyObj);

        return  modifyObj;
    }

    //가족 구성원 삭제
    @GetMapping("/deleteFamilyMember")
    public String deleteFamilyMember (@RequestParam(name="profileCode") String profileCode) {

        //가족 구성원 삭제 시, 프로필 사진 있다면 삭제.
        String originFileName = userService.originFileRoot(profileCode);

        if(originFileName != null) {
            deleteFile(originFileName);
        }
        userService.deleteFamilyMember(profileCode);

        return "redirect:/user/myFamily";
    }

    //가족 구성원 그룹 추가
    @PostMapping(value="/addFamilyGroup")
    @ResponseBody
    public String addFamilyGroup (@RequestBody FamilyGroup familyGroup, HttpSession httpSession) {

        String loginId = (String) httpSession.getAttribute("SID");
        familyGroup.setMemberId(loginId);

        log.info("그룹 추가 회원 목록 : {}", familyGroup);
        userService.addFamilyGroup(familyGroup);

        return "/user/myFamily";
    }

    // 그룹 삭제
    @PostMapping("/deleteFamilyGroup")
    @ResponseBody
    public String deleteFamilyGroup (@RequestParam(name="groupCodeList[]") List<String> groupCodeList){

        log.info("삭제할 그룹 그룹코드 목록 : {}", groupCodeList);

        userService.deleteGroup(groupCodeList);

        return "/user/myFamily";
    }

    //그룹에서 가족 구성원 삭제
    @GetMapping("/deleteFamilyByGroup")
    public String deleteFamilyByGroup (@RequestParam(name="profileCode") String profileCode,
                                       @RequestParam(name="groupCode") String groupCode,
                                       @RequestParam(name="groupIdx") int groupIdx,
                                       RedirectAttributes reAttr) {
        userService.deleteFamilyByGroup(profileCode, groupCode);

        reAttr.addFlashAttribute("groupIdx", groupIdx);
        return "redirect:/user/myFamily";
    }

}
