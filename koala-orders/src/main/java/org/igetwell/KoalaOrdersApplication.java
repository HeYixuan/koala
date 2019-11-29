package org.igetwell;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@MapperScan("org.igetwell.**.mapper")
@ComponentScan(basePackages = {"org.igetwell.**"})
public class KoalaOrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoalaOrdersApplication.class, args);
    }

}
