package com.hidroponic.backend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double targetPhMin = 5.5;
    private double targetPhMax = 6.5;
    private double alertTolerance = 0.8;

    @OneToOne(mappedBy = "equipment")
    private User owner;

    private Instant lastHeartbeat;

    public Equipment() {
    }

    public Equipment(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getAlertTolerance() {
        return alertTolerance;
    }

    public void setAlertTolerance(double alertTolerance) {
        this.alertTolerance = alertTolerance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Instant getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Instant lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
}
