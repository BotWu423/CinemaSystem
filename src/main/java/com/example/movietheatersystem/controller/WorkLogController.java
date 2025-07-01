package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.WorkLog;
import com.example.movietheatersystem.service.WorkLogService;
import com.example.movietheatersystem.entity.User;
import com.example.movietheatersystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/worklogs")
@CrossOrigin(origins = "*")
public class WorkLogController {
    @Autowired
    private WorkLogService workLogService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAllLogs() {
        List<WorkLog> logs = workLogService.getAllLogs();
        // 只返回需要的字段
        List<Map<String, Object>> result = logs.stream().map(log -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", log.getId());
            map.put("content", log.getContent());
            map.put("username", log.getUser().getUsername());
            map.put("createTime", log.getCreateTime());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> addLog(@RequestBody Map<String, String> body, Principal principal) {
        String content = body.get("content");
        // 通过principal获取当前用户名，再查userId
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("用户不存在"));
        WorkLog log = workLogService.saveWorkLog(user.getId(), content);
        Map<String, Object> result = new HashMap<>();
        result.put("id", log.getId());
        result.put("content", log.getContent());
        result.put("username", log.getUser().getUsername());
        result.put("createTime", log.getCreateTime());
        return ResponseEntity.ok(result);
    }
} 