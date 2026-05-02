package com.liveklass.notification.adapter.inbound.api;

import com.liveklass.notification.adapter.inbound.api.dto.CreateTemplateRequest;
import com.liveklass.notification.adapter.inbound.api.dto.TemplateResponse;
import com.liveklass.notification.application.port.inbound.TemplateUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final TemplateUseCase templateUseCase;

    public TemplateController(TemplateUseCase templateUseCase) {
        this.templateUseCase = templateUseCase;
    }

    @PostMapping
    public ResponseEntity<TemplateResponse> create(@Valid @RequestBody CreateTemplateRequest request) {
        TemplateResponse response = TemplateResponse.from(
                templateUseCase.create(request.type(), request.channel(), request.title(), request.body())
        );
        return ResponseEntity.created(URI.create("/api/templates/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        templateUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TemplateResponse>> findAll() {
        List<TemplateResponse> responses = templateUseCase.findAll().stream()
                .map(TemplateResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
