package dev.eyad_sharkawy.triage_desk.common.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import dev.eyad_sharkawy.triage_desk.common.context.TenantContext;
import dev.eyad_sharkawy.triage_desk.common.dto.ValidationErrorDetail;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

class ProblemDetailFactoryTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldCreateProblemDetailWithTimestampAndNoTenant() {
        ProblemDetail problem =
                ProblemDetailFactory.create(HttpStatus.NOT_FOUND, "Resource not found");
        assertThat(problem.getStatus()).isEqualTo(404);
        assertThat(problem.getDetail()).isEqualTo("Resource not found");
        assertThat(problem.getProperties()).containsKey("timestamp");
        assertThat(problem.getProperties()).doesNotContainKey("tenantId");
    }

    @Test
    void shouldIncludeTenantIdWhenContextIsSet() {
        UUID tenantId = UUID.randomUUID();
        TenantContext.setTenantId(tenantId);
        ProblemDetail problem =
                ProblemDetailFactory.create(HttpStatus.BAD_REQUEST, "Invalid request");
        assertThat(problem.getProperties()).containsEntry("tenantId", tenantId);
    }

    @Test
    void shouldIncludeValidationErrors() {
        List<ValidationErrorDetail> errors =
                List.of(new ValidationErrorDetail("subject", "must not be blank", null));
        ProblemDetail problem =
                ProblemDetailFactory.create(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        assertThat(problem.getProperties()).containsEntry("errors", errors);
    }
}
