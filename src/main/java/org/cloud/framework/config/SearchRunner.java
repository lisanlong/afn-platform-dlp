package org.cloud.framework.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SearchRunner implements ApplicationRunner {

//    @Autowired
//    ISearchDataReadService searchDataReadService;
//    @Autowired
//    ISearchDataWriteService searchDataWriteService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(">>>... DLP文档安全检查工具数据初始化...");
    }

}
