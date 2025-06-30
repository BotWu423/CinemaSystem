package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.dto.ActivityDTO;
import com.example.movietheatersystem.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<ActivityDTO> getAllActivities() {
        return activityService.getAllActivities();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void createActivity(@RequestBody ActivityDTO activityDTO) {
        activityService.createActivity(activityDTO);
    }

    @PostMapping("/{id}/join")
    @PreAuthorize("isAuthenticated()")
    public void joinActivity(@PathVariable Long id, Authentication authentication) {
        activityService.joinActivity(id, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
    }
}