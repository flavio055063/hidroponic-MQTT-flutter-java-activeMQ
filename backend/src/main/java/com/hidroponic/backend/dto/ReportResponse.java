package com.hidroponic.backend.dto;

public class ReportResponse {
    private double averagePh;
    private double averageTemperature;
    private double averageLuminosity;
    private double averageTds;

    public ReportResponse(double averagePh, double averageTemperature, double averageLuminosity, double averageTds) {
        this.averagePh = averagePh;
        this.averageTemperature = averageTemperature;
        this.averageLuminosity = averageLuminosity;
        this.averageTds = averageTds;
    }

    public double getAveragePh() {
        return averagePh;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public double getAverageLuminosity() {
        return averageLuminosity;
    }

    public double getAverageTds() {
        return averageTds;
    }
}
