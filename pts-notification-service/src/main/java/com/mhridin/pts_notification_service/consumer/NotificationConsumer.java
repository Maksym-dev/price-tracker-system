package com.mhridin.pts_notification_service.consumer;

import com.mhridin.pts_common.kafka.NotificationEvent;
import com.mhridin.pts_notification_service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "${app.kafka.topics.notifications}", groupId = "notification-group")
    public void consumeNotification(NotificationEvent event) {
        log.info("Received notification event for user: {}", event.getEmail());

        if (event.getEmail() != null && !event.getEmail().isBlank()) {
            emailService.sendPriceAlert(
                    event.getEmail(),
                    event.getProductTitle(),
                    event.getNewPrice(),
                    event.getUrl()
            );
        }

    }
}
