package com.hidroponic.backend.dto;

import java.time.Instant;

public class TelemetryResponse {
    private double ph;
    private double temperature;
    private double luminosity;
    private double tds;
    private double targetMin;
    private double targetMax;
    private Instant recordedAt;

    public TelemetryResponse(double ph, double temperature, double luminosity, double tds, double targetMin, double targetMax, Instant recordedAt) {
        this.ph = ph;
        this.temperature = temperature;
        this.luminosity = luminosity;
        this.tds = tds;
        this.targetMin = targetMin;
        this.targetMax = targetMax;
        this.recordedAt = recordedAt;
    }

    public double getPh() {
        return ph;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getLuminosity() {
        return luminosity;
    }

    public double getTds() {
        return tds;
    }

    public double getTargetMin() {
        return targetMin;
    }

    public double getTargetMax() {
        return targetMax;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}
