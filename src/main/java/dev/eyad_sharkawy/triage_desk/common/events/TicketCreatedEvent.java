package dev.eyad_sharkawy.triage_desk.common.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record TicketCreatedEvent(
        @NotNull UUID ticketId,
        @NotNull UUID tenantId,
        @NotNull UUID customerId,
        @NotBlank String customerEmail,
        @NotBlank String subject,
        @NotBlank String priority,
        @NotNull Instant createdAt,
        @NotNull Instant slaDueAt
) {
}
