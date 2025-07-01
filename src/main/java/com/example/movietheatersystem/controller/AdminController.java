package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.dto.AddScreeningRoomRequest;
import com.example.movietheatersystem.dto.ScreeningDTO;
import com.example.movietheatersystem.entity.Cinema;
import com.example.movietheatersystem.entity.Movie;
import com.example.movietheatersystem.entity.Screening;
import com.example.movietheatersystem.entity.ScreeningRoom;
import com.example.movietheatersystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private ScreeningRoomService screeningRoomService;

    // 添加放映厅
    @PostMapping("movie-management/add")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.addMovie(movie);
        return ResponseEntity.ok(savedMovie);
    }
    @PostMapping("/cinemas-management/add")
    public ResponseEntity<Cinema> addCinema(@RequestBody Cinema cinema) {
        return ResponseEntity.ok(cinemaService.addCinema(cinema));
    }
    @PostMapping("/screenings/add")
    public ResponseEntity<Screening> addScreening(@RequestBody ScreeningDTO dto) {
        // 根据 movieId 获取 Movie 实体
        Movie movie = movieService.getMovieById(dto.getMovieId());

        // 根据 roomId 获取 ScreeningRoom 实体
        ScreeningRoom screeningRoom = screeningRoomService.getScreeningRoomById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("放映厅不存在"));

        // 构建 Screening 实体
        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setScreeningRoom(screeningRoom);
        screening.setStartTime(dto.getStartTime());
        screening.setPrice(dto.getPrice());
        screening.setStatus(Screening.ScreeningStatus.SCHEDULED); // 默认状态

        // 调用服务层保存
        Screening savedScreening = adminService.addScreening(screening);

        return ResponseEntity.ok(savedScreening);
    }
    // 获取所有放映室
    // 添加放映室
    @PostMapping("/rooms")
    public ResponseEntity<ScreeningRoom> addScreeningRoom(@RequestBody AddScreeningRoomRequest request) {
        // 获取影院信息，若不存在则抛出异常
        Cinema cinema = cinemaService.getCinemaById(request.getCinemaId())
                .orElseThrow(() -> new RuntimeException("影院不存在"));

        ScreeningRoom room = new ScreeningRoom();
        room.setName(request.getName());
        room.setCinema(cinema);
        room.setLayout(request.getLayout());

        // 解析 layout 计算总座位数
        try {
            String[] parts = request.getLayout().split("x");
            int rows = Integer.parseInt(parts[0]);
            int cols = Integer.parseInt(parts[1]);
            room.setTotalSeats(rows * cols);
        } catch (Exception e) {
            throw new RuntimeException("座位布局格式错误，请使用类似 5x8 的格式");
        }

        ScreeningRoom savedRoom = adminService.addScreeningRoom(room);
        return ResponseEntity.ok(savedRoom);
    }



}