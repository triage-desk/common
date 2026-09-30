package dev.eyad_sharkawy.triage_desk.common.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record TicketResolvedEvent(
        @NotNull UUID ticketId,
        @NotNull UUID tenantId,
        @NotNull UUID resolvedBy,
        @NotBlank String resolutionSummary,
        @NotNull Instant resolvedAt) {}
