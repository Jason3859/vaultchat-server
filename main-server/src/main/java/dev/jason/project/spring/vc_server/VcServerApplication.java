package dev.jason.project.spring.vc_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
public class VcServerApplication {

    static void main(String[] args) {
        SpringApplication.run(VcServerApplication.class, args);
    }
}
