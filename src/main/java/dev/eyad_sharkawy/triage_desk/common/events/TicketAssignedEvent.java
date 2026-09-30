package dev.eyad_sharkawy.triage_desk.common.events;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record TicketAssignedEvent(
        @NotNull UUID ticketId,
        @NotNull UUID tenantId,
        @NotNull UUID agentId,
        @NotNull Instant assignedAt) {}
