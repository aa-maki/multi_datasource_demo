package com.example.multi_datasource_demo;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {

  private final EmployeeRepository employeeRepository;

  @GetMapping("")
  public ResponseEntity<List<Employee>> getEmployees() {
    return ResponseEntity.ok(employeeRepository.findAll());
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public void registerEmployee(@RequestBody Employee employee) throws Exception {
    employeeRepository.save(employee);
    employeeRepository.flush();
  }
}
