package org.igetwell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class KoalaWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoalaWechatApplication.class, args);
    }

}
