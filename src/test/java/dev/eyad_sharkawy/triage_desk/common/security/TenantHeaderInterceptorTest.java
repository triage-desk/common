package dev.eyad_sharkawy.triage_desk.common.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.eyad_sharkawy.triage_desk.common.context.TenantContext;
import java.io.IOException;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

class TenantHeaderInterceptorTest {

    private final TenantHeaderInterceptor interceptor = new TenantHeaderInterceptor();
    private HttpRequest request;
    private ClientHttpRequestExecution execution;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpRequest.class);
        execution = mock(ClientHttpRequestExecution.class);
        headers = new HttpHeaders();
        when(request.getHeaders()).thenReturn(headers);
        when(execution.execute(request, new byte[0])).thenReturn(mock(ClientHttpResponse.class));
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldAddTenantHeaderWhenContextIsSet() throws IOException {
        UUID tenantId = UUID.randomUUID();
        TenantContext.setTenantId(tenantId);

        interceptor.intercept(request, new byte[0], execution);

        assertThat(headers.getFirst(TenantHeaderInterceptor.TENANT_HEADER))
                .isEqualTo(tenantId.toString());
        verify(execution).execute(request, new byte[0]);
    }

    @Test
    void shouldNotAddTenantHeaderWhenContextIsEmpty() throws IOException {
        interceptor.intercept(request, new byte[0], execution);

        assertThat(headers.asSingleValueMap())
                .doesNotContainKey(TenantHeaderInterceptor.TENANT_HEADER);
        verify(execution).execute(request, new byte[0]);
    }
}
