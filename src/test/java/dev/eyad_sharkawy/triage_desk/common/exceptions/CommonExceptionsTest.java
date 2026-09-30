package dev.eyad_sharkawy.triage_desk.common.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CommonExceptionsTest {

    @Test
    void shouldCreateExceptionsWithMessages() {
        var ex1 = new TenantNotFoundException("Tenant not found");
        var ex2 = new TenantAccessDeniedException("Access denied");
        var ex3 = new ResourceNotFoundException("Resource not found");

        assertThat(ex1.getMessage()).isEqualTo("Tenant not found");
        assertThat(ex2.getMessage()).isEqualTo("Access denied");
        assertThat(ex3.getMessage()).isEqualTo("Resource not found");
    }
}
