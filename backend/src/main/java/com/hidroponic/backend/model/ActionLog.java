package com.hidroponic.backend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actor;
    private String action;

    @Column(length = 2048)
    private String details;

    private Instant createdAt = Instant.now();

    public ActionLog() {}

    public ActionLog(String actor, String action, String details) {
        this.actor = actor;
        this.action = action;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
