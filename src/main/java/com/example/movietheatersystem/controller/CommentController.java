package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.Comment;
import com.example.movietheatersystem.entity.User;
import com.example.movietheatersystem.repository.CommentRepository;
import com.example.movietheatersystem.repository.MovieRepository;
import com.example.movietheatersystem.repository.UserRepository;
import com.example.movietheatersystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/movie/{movieId}")
    public List<Comment> getComments(@PathVariable Long movieId) {
        return commentRepository.findByMovieId(movieId);
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Map<String, Object> payload, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtTokenProvider.getUserIdFromJWT(token);
        Long movieId = Long.valueOf(payload.get("movieId").toString());
        String content = payload.get("content").toString();
        Comment comment = new Comment();
        comment.setUser(userRepository.findById(userId).orElseThrow());
        comment.setMovie(movieRepository.findById(movieId).orElseThrow());
        comment.setContent(content);
        comment.setCreateTime(LocalDateTime.now());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtTokenProvider.getUserIdFromJWT(token);
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(id).orElseThrow();
        boolean isAdmin = user.getRole() == User.Role.ADMIN;
        boolean isOwner = comment.getUser().getId().equals(userId);
        if (isAdmin || isOwner) {
            commentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).body("无权限删除");
        }
    }
} 