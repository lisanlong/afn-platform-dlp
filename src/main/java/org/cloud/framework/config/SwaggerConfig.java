package org.cloud.framework.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo initApiInfo() {
        ApiInfo apiInfo = new ApiInfo("服务接口（REST）", // 大标题
                initContextInfo(), // 简单的描述
                "1.0.0", // 版本
                "服务条款", "精锐部队", // 作者
                "Copyright@2017-2018", // 链接显示文字
                "#"// 网站链接
        );
        return apiInfo;
    }

    private String initContextInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("提供知识检索服务接口，可支持系统集成和移动端开发需求，采用标准的rest规范体系提供接口服务。")
                .append("<br/>")
                .append("——右下角错误忽略，不影响系统使用。<br/>")
                .append("时间是一切财富中最宝贵的财富。 —— 德奥弗拉斯多");
        return sb.toString();
    }

    @Bean
    public Docket restfulApi() {
        System.out.println("http://localhost:58001/swagger-ui.html");
        return new Docket(DocumentationType.SWAGGER_2).groupName("RestfulApi")
                // .genericModelSubstitutes(DeferredResult.class)
                .genericModelSubstitutes(ResponseEntity.class).useDefaultResponseMessages(true).forCodeGeneration(false)
                .pathMapping("/") // base，最终调用接口后会和paths拼接在一起
                .select()
                .paths(doFilteringRules())
                .build().apiInfo(initApiInfo());
        // .select().paths(Predicates.not(PathSelectors.regex("/error.*"))).build().apiInfo(initApiInfo());
    }

    /**
     * 设置过滤规则 这里的过滤规则支持正则匹配
     //若有静态方法在此之前加载就会报集合相关的错误.
     *
     * @return
     */
    private Predicate<String> doFilteringRules() {
//		return Predicates.not(PathSelectors.regex("/error.*"));
//		return or(regex("/hello.*"), regex("/rest/adxSspFinanceManagement.*"));//success
        return or(regex("/user-api.*"), regex("/search-api.*"));
    }


}
