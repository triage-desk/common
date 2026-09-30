package dev.eyad_sharkawy.triage_desk.common.dto;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record ValidationErrorDetail(String field, String message, @Nullable Object rejectedValue) {}
