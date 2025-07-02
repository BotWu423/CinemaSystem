package com.example.movietheatersystem.service;

import com.example.movietheatersystem.dto.ActivityDTO;
import com.example.movietheatersystem.entity.Activity;
import com.example.movietheatersystem.entity.User;
import com.example.movietheatersystem.repository.ActivityRepository;
import com.example.movietheatersystem.repository.UserRepository;
import com.example.movietheatersystem.repository.CinemaRepository;
import com.example.movietheatersystem.entity.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CinemaRepository cinemaRepository;

    public List<ActivityDTO> getAllActivities() {
        return activityRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void createActivity(ActivityDTO dto) {
        Activity activity = new Activity();
        activity.setName(dto.getName());
        activity.setDescription(dto.getDescription());
        activity.setQuota(dto.getQuota());
        activity.setCinemaId(dto.getCinemaId());
        activityRepository.save(activity);
    }

    @Transactional
    public void joinActivity(Long activityId, String username) {
        Activity activity = activityRepository.findById(activityId).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow();
        if (activity.getParticipants().contains(user)) {
            throw new RuntimeException("已报名该活动");
        }
        if (activity.getParticipants().size() >= activity.getQuota()) {
            throw new RuntimeException("名额已满");
        }
        activity.getParticipants().add(user);
        activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long activityId) {
        activityRepository.deleteById(activityId);
    }

    private ActivityDTO toDTO(Activity activity) {
        ActivityDTO dto = new ActivityDTO();
        dto.setId(activity.getId());
        dto.setName(activity.getName());
        dto.setDescription(activity.getDescription());
        dto.setQuota(activity.getQuota());
        dto.setParticipants(activity.getParticipants().stream().map(User::getId).collect(Collectors.toList()));
        dto.setCinemaId(activity.getCinemaId());
        if (activity.getCinemaId() != null) {
            dto.setCinemaName(
                cinemaRepository.findById(activity.getCinemaId())
                    .map(cinema -> cinema.getName())
                    .orElse("未知影院")
            );
        } else {
            dto.setCinemaName("未知影院");
        }
        return dto;
    }
    public List<ActivityDTO> getUserActivities(Long userId)
    {
        return activityRepository.findByParticipantsId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }
    public void leaveActivity(Long activityId, String username) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!activity.getParticipants().contains(user)) {
            throw new RuntimeException("你未参与该活动");
        }

        activity.getParticipants().remove(user);
        activityRepository.save(activity);
    }

}