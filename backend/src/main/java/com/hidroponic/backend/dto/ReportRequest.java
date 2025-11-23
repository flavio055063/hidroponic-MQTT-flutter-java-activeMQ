package com.hidroponic.backend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class ReportRequest {
    @NotNull
    private Instant start;
    @NotNull
    private Instant end;

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }
}
