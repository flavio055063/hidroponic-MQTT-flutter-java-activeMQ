package com.hidroponic.backend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Equipment equipment;

    private double ph;
    private double temperature;
    private double luminosity;
    private double tds;
    private double targetPhMin;
    private double targetPhMax;

    private Instant recordedAt = Instant.now();

    public SensorReading() {
    }

    public SensorReading(Equipment equipment, double ph, double temperature, double luminosity, double tds, double targetPhMin, double targetPhMax) {
        this.equipment = equipment;
        this.ph = ph;
        this.temperature = temperature;
        this.luminosity = luminosity;
        this.tds = tds;
        this.targetPhMin = targetPhMin;
        this.targetPhMax = targetPhMax;
    }

    public Long getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(double luminosity) {
        this.luminosity = luminosity;
    }

    public double getTds() {
        return tds;
    }

    public void setTds(double tds) {
        this.tds = tds;
    }

    public double getTargetPhMin() {
        return targetPhMin;
    }

    public void setTargetPhMin(double targetPhMin) {
        this.targetPhMin = targetPhMin;
    }

    public double getTargetPhMax() {
        return targetPhMax;
    }

    public void setTargetPhMax(double targetPhMax) {
        this.targetPhMax = targetPhMax;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
    }
}
