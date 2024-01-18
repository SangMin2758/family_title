package side.family_title.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import side.family_title.dto.FamilyTitle;
import side.family_title.service.MainService;
import side.family_title.service.user.UserService;


import java.util.List;

@Controller("mainController")
@AllArgsConstructor
public class MainCotroller {

    UserService userService;

    @GetMapping(value = {"", "/"})
    public String mainPage(Model model){

        List<FamilyTitle> familyList = userService.firstFamily("1");
        model.addAttribute("familyList",familyList);

        return "user/index";
    }
}
