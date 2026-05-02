package com.liveklass.notification.application.service;

import com.liveklass.notification.application.port.inbound.TemplateUseCase;
import com.liveklass.notification.application.port.outbound.TemplateRepository;
import com.liveklass.notification.domain.exception.TemplateNotFoundException;
import com.liveklass.notification.domain.model.Channel;
import com.liveklass.notification.domain.model.NotificationTemplate;
import com.liveklass.notification.domain.model.NotificationType;

import java.util.List;
import java.util.UUID;

public class TemplateService implements TemplateUseCase {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public NotificationTemplate create(NotificationType type, Channel channel, String title, String body) {
        NotificationTemplate template = NotificationTemplate.create(type, channel, title, body);
        return templateRepository.save(template);
    }

    @Override
    public void delete(UUID id) {
        if (templateRepository.findById(id).isEmpty()) {
            throw TemplateNotFoundException.byId(id);
        }
        templateRepository.deleteById(id);
    }

    @Override
    public List<NotificationTemplate> findAll() {
        return templateRepository.findAll();
    }
}
