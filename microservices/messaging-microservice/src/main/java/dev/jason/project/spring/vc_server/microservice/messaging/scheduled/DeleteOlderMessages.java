package dev.jason.project.spring.vc_server.microservice.messaging.scheduled;

import dev.jason.project.spring.vc_server.microservice.messaging.repo.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class DeleteOlderMessages {

    private static final long DURATION_MONTH_IN_MILLIS = 2592000000L;

    private final MessageRepository messageRepository;

    @Scheduled(fixedRate = DURATION_MONTH_IN_MILLIS)
    void deleteOlderMessages() {
        log.info("Deleting older messages...");
        LocalDateTime now = LocalDateTime.now();

        messageRepository.findAll().forEach(messageEntity -> {
            if (now.getMonthValue() - messageEntity.timestamp().getMonthValue() >= 1) {
                messageRepository.delete(messageEntity);
            }
        });

        log.info("Successfully deleted messages older than a month.");
    }
}
