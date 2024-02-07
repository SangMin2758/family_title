package side.family_title.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import side.family_title.controller.config.ConfigUtils;
import side.family_title.dto.GoogleInfResponse;
import side.family_title.dto.GoogleRequest;
import side.family_title.dto.GoogleTokResponse;
import side.family_title.service.user.UserService;

import java.net.URI;
import java.net.URISyntaxException;


@Controller
@RequestMapping(value = "/login/oauth2")
@AllArgsConstructor
public class LoginController {

    ConfigUtils configUtils;
    UserService userService;

    @GetMapping("/code")
    public ResponseEntity<Object> goLoginUrl(){
        String url = configUtils.googleInitUrl();
        URI redirenctUri = null;
        try {
            redirenctUri = new URI(url);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirenctUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping ("/code/google")
    public String redirectGoogleLogin(@RequestParam(value="code") String authCode, HttpSession httpSession) {

        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest requestParams = GoogleRequest.builder()
                .clientId(configUtils.getGoogleClientId())
                .clientSecret(configUtils.getGoogleSecret())
                .code(authCode)
                .redirectUri(configUtils.getGoogleRedirectUri())
                .grantType("authorization_code")
                .build();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getGoogleAuthUrl() + "/token", httpRequestEntity, String.class);

            // ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            GoogleTokResponse googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleTokResponse>() {});

            // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(configUtils.getGoogleAuthUrl() + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            if(resultJson != null) {
                GoogleInfResponse userInfoDto = objectMapper.readValue(resultJson, new TypeReference<GoogleInfResponse>() {});

                //가입된 아이디라면 로그인 가입되지 않았다면 회원가입 진행
                String loginId = userInfoDto.getEmail();
                userService.goAccount(loginId);
                //로그인 (세션에 아이디 저장)
                httpSession.setAttribute("SID",userInfoDto.getEmail());
                return "redirect:/";
            }
            else {
                throw new Exception("Google OAuth failed!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout (HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

}
