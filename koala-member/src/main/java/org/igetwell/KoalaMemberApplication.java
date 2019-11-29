package org.igetwell;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.annotation.EnableKoalaFeign;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableKoalaFeign
@SpringCloudApplication
@MapperScan("org.igetwell.**.mapper")
@ComponentScan(basePackages = {"org.igetwell.**"})
public class KoalaMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoalaMemberApplication.class, args);
    }

}
