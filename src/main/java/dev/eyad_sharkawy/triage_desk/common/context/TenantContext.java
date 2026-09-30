package dev.eyad_sharkawy.triage_desk.common.context;

import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TenantContext {
    private static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {}

    public static void setTenantId(@Nullable UUID tenantId) {
        if (tenantId == null) {
            CURRENT_TENANT.remove();
        } else {
            CURRENT_TENANT.set(tenantId);
        }
    }

    public static @Nullable UUID getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static UUID requireTenantId() {
        UUID tenantId = CURRENT_TENANT.get();

        if (tenantId == null) {
            throw new IllegalStateException("Tenant context is not set for the current thread");
        }

        return tenantId;
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
