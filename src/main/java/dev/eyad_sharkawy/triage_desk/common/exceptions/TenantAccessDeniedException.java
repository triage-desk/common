package dev.eyad_sharkawy.triage_desk.common.exceptions;

public class TenantAccessDeniedException extends RuntimeException {
    public TenantAccessDeniedException(String message) {
        super(message);
    }
}
