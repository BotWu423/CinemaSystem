package com.example.movietheatersystem.service;

import com.example.movietheatersystem.entity.Cinema;
import com.example.movietheatersystem.entity.Employee;
import com.example.movietheatersystem.repository.CinemaRepository;
import com.example.movietheatersystem.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CinemaRepository cinemaRepository;

    public EmployeeService(EmployeeRepository employeeRepository, CinemaRepository cinemaRepository) {
        this.employeeRepository = employeeRepository;
        this.cinemaRepository = cinemaRepository;
    }

    public List<Employee> getEmployeesByCinemaId(Long cinemaId) {
        return employeeRepository.findByCinemaId(cinemaId);
    }

    public Employee addEmployee(Employee employee, Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("影院不存在"));
        employee.setCinema(cinema);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("员工不存在"));

        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setPhone(updatedEmployee.getPhone());

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
