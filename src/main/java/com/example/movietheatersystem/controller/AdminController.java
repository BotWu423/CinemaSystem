package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.dto.AddScreeningRoomRequest;
import com.example.movietheatersystem.dto.ScreeningDTO;
import com.example.movietheatersystem.entity.*;
import com.example.movietheatersystem.repository.SeatRepository;
import com.example.movietheatersystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SeatService seatService;

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
        Cinema cinema = cinemaService.getCinemaById1(request.getCinemaId())
                .orElseThrow(() -> new RuntimeException("影院不存在"));

        ScreeningRoom room = new ScreeningRoom();
        room.setName(request.getName());
        room.setCinema(cinema);
        room.setLayout(request.getLayout());

        List<Seat> seats = new ArrayList<>();

        try {
            String layout = request.getLayout();
            if (layout == null || !layout.matches("\\d+x\\d+")) {
                throw new RuntimeException("座位布局格式错误，请使用类似 5x8 的格式");
            }

            String[] parts = layout.split("x");
            int rows = Integer.parseInt(parts[0]);
            int cols = Integer.parseInt(parts[1]);
            int totalSeats = rows * cols;

            room.setTotalSeats(totalSeats);

            // Step 1: 先保存放映室
            ScreeningRoom savedRoom = adminService.addScreeningRoom(room);

            // Step 2: 使用已保存的放映室 ID 创建座位
            for (int row = 1; row <= rows; row++) {
                for (int col = 1; col <= cols; col++) {
                    Seat seat = new Seat();
                    seat.setRowNumber(row);
                    seat.setSeatNumber(col);
                    seat.setSeatType(Seat.SeatType.NORMAL);
                    seat.setStatus(Seat.SeatStatus.AVAILABLE);
                    seat.setScreeningRoom(savedRoom); // 使用已保存的放映室
                    seats.add(seat);
                }
            }

            // Step 3: 保存座位
            System.out.println("开始保存 " + seats.size() + " 个座位");
            seatService.saveAllSeats(seats);
            System.out.println("座位保存完成");

            return ResponseEntity.ok(savedRoom);

        } catch (NumberFormatException e) {
            throw new RuntimeException("座位布局必须是数字x数字，如 5x8", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("座位布局处理失败：" + e.getMessage(), e);
        }
    }


}