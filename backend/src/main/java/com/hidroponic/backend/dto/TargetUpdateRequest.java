package com.hidroponic.backend.dto;

import jakarta.validation.constraints.NotNull;

public class TargetUpdateRequest {
    @NotNull
    private Double targetMin;
    @NotNull
    private Double targetMax;

    public Double getTargetMin() {
        return targetMin;
    }

    public void setTargetMin(Double targetMin) {
        this.targetMin = targetMin;
    }

    public Double getTargetMax() {
        return targetMax;
    }

    public void setTargetMax(Double targetMax) {
        this.targetMax = targetMax;
    }
}
