package com.hidroponic.backend.service;

import com.hidroponic.backend.model.ActionLog;
import com.hidroponic.backend.repository.ActionLogRepository;
import org.springframework.stereotype.Service;

@Service
public class ActionLogService {
    private final ActionLogRepository repository;

    public ActionLogService(ActionLogRepository repository) {
        this.repository = repository;
    }

    public ActionLog log(String actor, String action, String details) {
        return repository.save(new ActionLog(actor, action, details));
    }
}
