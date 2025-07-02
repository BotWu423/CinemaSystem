package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.CinemaComment;
import com.example.movietheatersystem.entity.User;
import com.example.movietheatersystem.repository.CinemaCommentRepository;
import com.example.movietheatersystem.repository.UserRepository;
import com.example.movietheatersystem.repository.CinemaRepository;
import com.example.movietheatersystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cinema-comments")
@CrossOrigin(origins = "*")
public class CinemaCommentController {

    @Autowired
    private CinemaCommentRepository cinemaCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 获取某个影院的所有评论
    @GetMapping("/cinema/{cinemaId}")
    public List<CinemaComment> getCommentsByCinemaId(@PathVariable Long cinemaId) {
        return cinemaCommentRepository.findByCinemaId(cinemaId);
    }

    // 提交影院评论
    @PostMapping
    public ResponseEntity<CinemaComment> addComment(
            @RequestBody Map<String, Object> payload,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtTokenProvider.getUserIdFromJWT(token);
        Long cinemaId = Long.valueOf(payload.get("cinemaId").toString());
        String content = payload.get("content").toString();

        CinemaComment comment = new CinemaComment();
        comment.setCinema(cinemaRepository.findById(cinemaId).orElseThrow());
        comment.setUser(userRepository.findById(userId).orElseThrow());
        comment.setContent(content);
        comment.setCreateTime(LocalDateTime.now());

        return ResponseEntity.ok(cinemaCommentRepository.save(comment));
    }

    // 删除影院评论
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtTokenProvider.getUserIdFromJWT(token);

        CinemaComment comment = cinemaCommentRepository.findById(id).orElseThrow();
        boolean isAdmin = userRepository.findById(userId).orElseThrow().getRole() == User.Role.ADMIN;
        boolean isOwner = comment.getUser().getId().equals(userId);

        if (isAdmin || isOwner) {
            cinemaCommentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).body("无权限删除");
        }
    }
    @RestController
    @RequestMapping("/api/users")
    public class UserController {

        @Autowired
        private UserRepository userRepository;

        @GetMapping("/me")
        public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtTokenProvider.getUserIdFromJWT(token);
            User user = userRepository.findById(userId).orElseThrow();
            return ResponseEntity.ok(user);
        }
    }

}
