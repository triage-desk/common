package dev.eyad_sharkawy.triage_desk.common.events;

import static org.assertj.core.api.Assertions.assertThat;

import dev.eyad_sharkawy.triage_desk.common.domain.TicketPriority;
import dev.eyad_sharkawy.triage_desk.common.domain.TicketStatus;
import dev.eyad_sharkawy.triage_desk.common.dto.ValidationErrorDetail;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CommonEventsAndEnumsTest {

    @Test
    void shouldInstantiateEvents() {
        UUID ticketId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID agentId = UUID.randomUUID();
        Instant now = Instant.now();

        var created =
                new TicketCreatedEvent(
                        ticketId,
                        tenantId,
                        customerId,
                        "customer@example.com",
                        "Test",
                        "HIGH",
                        now,
                        now.plusSeconds(3600));
        var assigned = new TicketAssignedEvent(ticketId, tenantId, agentId, now);
        var resolved = new TicketResolvedEvent(ticketId, tenantId, agentId, "Fixed", now);

        assertThat(created.ticketId()).isEqualTo(ticketId);
        assertThat(created.tenantId()).isEqualTo(tenantId);
        assertThat(created.customerId()).isEqualTo(customerId);
        assertThat(created.customerEmail()).isEqualTo("customer@example.com");
        assertThat(created.subject()).isEqualTo("Test");
        assertThat(created.priority()).isEqualTo("HIGH");
        assertThat(created.createdAt()).isEqualTo(now);
        assertThat(created.slaDueAt()).isEqualTo(now.plusSeconds(3600));

        assertThat(assigned.ticketId()).isEqualTo(ticketId);
        assertThat(assigned.tenantId()).isEqualTo(tenantId);
        assertThat(assigned.agentId()).isEqualTo(agentId);
        assertThat(assigned.assignedAt()).isEqualTo(now);

        assertThat(resolved.ticketId()).isEqualTo(ticketId);
        assertThat(resolved.tenantId()).isEqualTo(tenantId);
        assertThat(resolved.resolvedBy()).isEqualTo(agentId);
        assertThat(resolved.resolutionSummary()).isEqualTo("Fixed");
        assertThat(resolved.resolvedAt()).isEqualTo(now);
    }

    @Test
    void shouldCoverEnums() {
        assertThat(TicketPriority.values())
                .containsExactly(
                        TicketPriority.NONE,
                        TicketPriority.LOW,
                        TicketPriority.MEDIUM,
                        TicketPriority.HIGH,
                        TicketPriority.URGENT);
        assertThat(TicketPriority.valueOf("HIGH")).isEqualTo(TicketPriority.HIGH);

        assertThat(TicketStatus.values())
                .containsExactly(
                        TicketStatus.OPEN,
                        TicketStatus.ASSIGNED,
                        TicketStatus.IN_PROGRESS,
                        TicketStatus.RESOLVED,
                        TicketStatus.CLOSED);
        assertThat(TicketStatus.valueOf("OPEN")).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    void shouldInstantiateValidationErrorDetail() {
        var detail = new ValidationErrorDetail("field", "message", "rejected");
        assertThat(detail.field()).isEqualTo("field");
        assertThat(detail.message()).isEqualTo("message");
        assertThat(detail.rejectedValue()).isEqualTo("rejected");
    }
}
