package org.igetwell;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.gateway.route.annotation.EnableKoalaDynamicRoute;

@EnableKoalaDynamicRoute
@SpringCloudApplication
public class KoalaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KoalaGatewayApplication.class, args);
    }

}
