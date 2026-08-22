package dev.jason.project.spring.vc_server;

import dev.jason.project.spring.vc_server.core.exception.VcExceptionHandler;
import dev.jason.project.spring.vc_server.device_microservice.service.DeviceService;
import dev.jason.project.spring.vc_server.microservice.messaging.service.MessagingService;
import dev.jason.project.spring.vc_server.social_microservice.service.SocialService;
import dev.jason.project.spring.vc_server.user_microservice.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
@Import({
    VcExceptionHandler.class,
    UserService.class,
    DeviceService.class,
    MessagingService.class,
    SocialService.class
})
public class VcServerApplication {

    static void main(String[] args) {
        SpringApplication.run(VcServerApplication.class, args);
    }
}
