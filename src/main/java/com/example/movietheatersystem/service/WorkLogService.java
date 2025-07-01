package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.User;
import com.example.movietheatersystem.entity.WorkLog;
import com.example.movietheatersystem.repository.WorkLogRepository;
import com.example.movietheatersystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkLogService {
    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private UserRepository userRepository;

    public WorkLog saveWorkLog(Long userId, String content) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        WorkLog log = new WorkLog();
        log.setContent(content);
        log.setUser(user);
        log.setCreateTime(LocalDateTime.now());
        return workLogRepository.save(log);
    }

    public List<WorkLog> getAllLogs() {
        return workLogRepository.findAll();
    }
} 