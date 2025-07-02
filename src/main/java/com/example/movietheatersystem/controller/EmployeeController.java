package com.example.movietheatersystem.controller;

import com.example.movietheatersystem.entity.Employee;
import com.example.movietheatersystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 用户可查看
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<Employee>> getEmployeesByCinema(@PathVariable Long cinemaId) {
        return ResponseEntity.ok(employeeService.getEmployeesByCinemaId(cinemaId));
    }

    // 管理员专属操作
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee,
                                                   @RequestParam Long cinemaId) {
        return ResponseEntity.ok(employeeService.addEmployee(employee, cinemaId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
                                                   @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
