package com.hidroponic.backend.dto;

import jakarta.validation.constraints.NotNull;

public class TelemetryRequest {
    @NotNull
    private Double ph;
    @NotNull
    private Double temperature;
    @NotNull
    private Double luminosity;
    @NotNull
    private Double tds;

    public Double getPh() {
        return ph;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(Double luminosity) {
        this.luminosity = luminosity;
    }

    public Double getTds() {
        return tds;
    }

    public void setTds(Double tds) {
        this.tds = tds;
    }
}
