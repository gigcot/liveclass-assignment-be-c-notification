package com.liveclass.notification.infrastructure.config;

import com.liveclass.notification.application.port.outbound.NotificationRegistrationPort;
import com.liveclass.notification.application.port.outbound.NotificationRepository;
import com.liveclass.notification.application.port.outbound.TemplateRepository;
import com.liveclass.notification.application.service.NotificationService;
import com.liveclass.notification.application.service.UpdateNotificationStatusService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public NotificationService notificationService(NotificationRepository notificationRepository,
                                                   NotificationRegistrationPort registrationPort,
                                                   TemplateRepository templateRepository) {
        return new NotificationService(notificationRepository, registrationPort, templateRepository);
    }

    @Bean
    public UpdateNotificationStatusService updateNotificationStatusService(
            NotificationRepository notificationRepository) {
        return new UpdateNotificationStatusService(notificationRepository);
    }
}
