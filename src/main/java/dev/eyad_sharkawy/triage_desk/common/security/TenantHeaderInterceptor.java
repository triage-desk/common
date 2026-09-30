package dev.eyad_sharkawy.triage_desk.common.security;

import dev.eyad_sharkawy.triage_desk.common.context.TenantContext;
import java.io.IOException;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class TenantHeaderInterceptor implements ClientHttpRequestInterceptor {
    public static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    @NullMarked
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        UUID tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            request.getHeaders().set(TENANT_HEADER, tenantId.toString());
        }

        return execution.execute(request, body);
    }
}
