package com.liveclass.notification.infrastructure.config;

import com.liveclass.notification.application.port.outbound.NotificationRepository;
import com.liveclass.notification.application.service.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public NotificationService notificationService(NotificationRepository notificationRepository) {
        return new NotificationService(notificationRepository);
    }
}
