package org.igetwell;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.annotation.EnableKoalaFeign;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;


@EnableKoalaFeign
@SpringCloudApplication
@ComponentScan(basePackages = {"org.igetwell.**"})
public class KoalaMerchantApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoalaMerchantApplication.class, args);
    }

}
