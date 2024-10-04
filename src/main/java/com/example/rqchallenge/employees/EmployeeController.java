package com.example.rqchallenge.employees;

import com.example.rqchallenge.Service.IEmployeeService;
import com.example.rqchallenge.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class EmployeeController implements IEmployeeController {

    private IEmployeeService employeeService;

    /* Dependency should be auto wired (injected), depending on the choice of DI frameworks
       For simplicity, let's pass it in as an argument
    * */
    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Get all employees");
        try {
            return ResponseEntity.ok(employeeService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        log.info("Get all employees by name {}", searchString);
        try {
            return ResponseEntity.ok(
                    employeeService.getAll().stream()
                            .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                            .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        log.info("Get employee by id {}", id);
        try {
            // in readme, turns out id is a number, not a string
            Optional<Employee> target = employeeService.getById(Integer.parseInt(id));
            if (target.isPresent()) {
                return ResponseEntity.ok(target.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        List<Employee> employees = employeeService.getAll();
        int highest = 0;
        for (Employee employee : employees) {
            highest = Math.max(highest, employee.getSalary());
        }
        log.info("Highest salary of employees: {}", highest);
        return ResponseEntity.ok(highest);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        PriorityQueue<Employee> top = new PriorityQueue<>((e1, e2) -> e1.getSalary() - e2.getSalary());
        List<Employee> employees = employeeService.getAll();
        for (Employee employee : employees) {
            top.add(employee);
            if (top.size() > 10) {
                top.poll();
            }
        }
        List<String> names = new ArrayList<>();
        while (!top.isEmpty()) {
            names.add(top.poll().getName());
        }
        return ResponseEntity.ok(names);
    }

    /* Since the interface specifies a map, I'd assume we just use map */
    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        String name = null;
        Integer salary = null;
        Integer age = null;
        for (String key : employeeInput.keySet()) {
            String value = employeeInput.get(key).toString();
            if (key.equals("name")) {
                name = value;
            }
            if (key.equals("salary")) {
                salary = Integer.parseInt(value);
            }
            if (key.equals("age")) {
                age = Integer.parseInt(value);
            }
        }
        try {
            return ResponseEntity.ok(employeeService.create(name, age, salary));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        log.info("Delete employee by id {}", id);
        try {
            Employee employeeToDelete = getEmployeeById(id).getBody();
            String name = employeeToDelete == null ? "" : employeeToDelete.getName();
            boolean result = employeeService.delete(Integer.parseInt(id));
            if (result) {
                return ResponseEntity.ok(name);
            } else {
                // returned value should get reflected accordingly based on the return of the downstream
                // I think we need more info about the downstream service, maybe add it in README.
                // since we're using a mocked dummy service, let's use 500x for now.
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
