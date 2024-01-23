package side.family_title.controller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    //SpringBoot에서 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정.
    //로 요청하면 localhost:8088/summernoteImage/1234.jpg
    //Window -> C:/family_image/1234.jpg 파일을 불러온다.
    //Mag -> Users/Shared/family_image/1234.jpg 파일을 불러온다.
    //Ubuntu -> home/springboot/resources/family_image/1234.jpg 파일을 불러온다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rootPath = getOsFileRootPath();
        registry.addResourceHandler("/familyImage/**")
                .addResourceLocations(rootPath);
    }


    //OS 별 파일 경로 설정
    public String getOsFileRootPath(){
        String os = System.getProperty("os.name").toLowerCase();
        String rootPath = "file:////home/springboot/resources/family_image/";

        if (os.contains("win")) {
            rootPath = "file:///C:/family_image/";
        } else if (os.contains("mac")) {
            rootPath = "file:////Users/Shared/family_image/";
        }

        return rootPath;
    }

}
