package dev.eyad_sharkawy.triage_desk.common.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class TenantContextTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldSetAndGetTenantId() {
        UUID tenantId = UUID.randomUUID();
        TenantContext.setTenantId(tenantId);

        assertThat(TenantContext.getTenantId()).isEqualTo(tenantId);
        assertThat(TenantContext.requireTenantId()).isEqualTo(tenantId);
    }

    @Test
    void shouldThrowExceptionWhenRequireTenantIdWithoutContext() {
        assertThatThrownBy(TenantContext::requireTenantId)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Tenant context is not set");
    }

    @Test
    void shouldIsolateTenantAcrossThreads() throws ExecutionException, InterruptedException {
        UUID mainTenantId = UUID.randomUUID();
        TenantContext.setTenantId(mainTenantId);

        try (var executor = Executors.newSingleThreadExecutor()) {
            Future<UUID> asyncTenant = executor.submit(TenantContext::getTenantId);

            assertThat(asyncTenant.get())
                    .as("Separate thread must not see main thread's tenant")
                    .isNull();
        }

        assertThat(TenantContext.getTenantId()).isEqualTo(mainTenantId);
    }

    @Test
    void shouldClearTenantIdWhenPassingNull() {
        TenantContext.setTenantId(UUID.randomUUID());
        TenantContext.setTenantId(null);

        assertThat(TenantContext.getTenantId()).isNull();
    }

    @Test
    void shouldInvokePrivateConstructorForCoverage() throws Exception {
        var constructor = TenantContext.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }
}
