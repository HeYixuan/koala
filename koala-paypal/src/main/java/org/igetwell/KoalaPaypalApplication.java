package org.igetwell;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.annotation.EnableKoalaFeign;
import org.springframework.cloud.client.SpringCloudApplication;

@EnableKoalaFeign
@SpringCloudApplication
public class KoalaPaypalApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoalaPaypalApplication.class, args);
    }

}
