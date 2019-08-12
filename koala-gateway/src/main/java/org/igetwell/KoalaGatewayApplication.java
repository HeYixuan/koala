package org.igetwell;

import org.igetwell.gateway.annotation.EnableKoalaDynamicRoute;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@EnableKoalaDynamicRoute
@SpringCloudApplication
public class KoalaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoalaGatewayApplication.class, args);
    }

}
