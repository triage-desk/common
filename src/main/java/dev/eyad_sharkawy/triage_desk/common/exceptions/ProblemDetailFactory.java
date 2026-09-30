package dev.eyad_sharkawy.triage_desk.common.exceptions;

import dev.eyad_sharkawy.triage_desk.common.context.TenantContext;
import dev.eyad_sharkawy.triage_desk.common.dto.ValidationErrorDetail;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@NullMarked
public final class ProblemDetailFactory {
    private ProblemDetailFactory() {}

    public static ProblemDetail create(HttpStatus status, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setProperty("timestamp", Instant.now());

        UUID tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            problemDetail.setProperty("tenantId", tenantId);
        }

        return problemDetail;
    }

    public static ProblemDetail create(
            HttpStatus status, String detail, List<ValidationErrorDetail> validationErrors) {
        ProblemDetail problemDetail = create(status, detail);
        problemDetail.setProperty("errors", validationErrors);

        return problemDetail;
    }
}
