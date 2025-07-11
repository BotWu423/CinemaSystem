package com.example.movietheatersystem.repository;

import com.example.movietheatersystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByCinemaId(Long cinemaId);
}
