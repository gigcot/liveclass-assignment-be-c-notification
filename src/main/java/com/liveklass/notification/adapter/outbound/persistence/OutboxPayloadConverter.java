package com.liveklass.notification.adapter.outbound.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liveklass.notification.domain.exception.OutboxSerializationException;
import com.liveklass.notification.domain.model.OutboxPayload;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OutboxPayloadConverter implements AttributeConverter<OutboxPayload, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(OutboxPayload attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw OutboxSerializationException.onSerialize(e);
        }
    }

    @Override
    public OutboxPayload convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, OutboxPayload.class);
        } catch (Exception e) {
            throw OutboxSerializationException.onDeserialize(e);
        }
    }
}
