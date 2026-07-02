package dev.jason.project.spring.vc_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class VcServerApplication {

    static void main(String[] args) {
        SpringApplication.run(VcServerApplication.class, args);
    }
}
