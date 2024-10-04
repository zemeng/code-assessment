package com.example.rqchallenge.Service;

import com.example.rqchallenge.model.Employee;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/* A dummy service to simulate downstream providing actual employee data */
@Slf4j
public class DummyEmployeeService implements IEmployeeService {

    private int nextId = 1;

    /* assume there is a database to store employees
     * For the sake of simplicity, just store them in place here */
    List<Employee> employees = new ArrayList<>();

    private Employee employeeHighestPay;

    public DummyEmployeeService(int validUserCount, int deletedUserCount, int highestPay) {

        /* There should be some data serialization/deserialization
        * For example,
        *   {"id": "1",
            "employee_name": "Tiger Nixon",
            "employee_salary": "320800",
            "employee_age": "61",
            "profile_image": ""}

         * should convert to: id=1, name="Tiger Nixon", salary=320800, age=61, image=something.
        * */

        // For testing purpose, just create employee objects in place
        for(int i = 1; i <= validUserCount; i++) {
            employees.add(Employee.builder()
                    .id(generateNewEmployeeId())
                    .name("User"+i)
                    .age(5 * i)
                    .salary(100 * i)
                    .image(null)
                    .build());
        }
        this.employeeHighestPay = Employee.builder()
                .id(generateNewEmployeeId())
                .name("User_highest")
                .age(20)
                .salary(highestPay)
                .image(null)
                .build();
        employees.add(employeeHighestPay);

        for(int i = 1; i <= deletedUserCount; i++) {
            employees.add(Employee.builder()
                    .id(generateNewEmployeeId())
                    .name("DeletedPerson"+i)
                    .age(5 * i)
                    .salary(100 * i)
                    .image(null)
                    .build());
        }
    }

    @Override
    public List<Employee> getAll() {
        return employees;
    }

    @Override
    public Optional<Employee> getById(int id) {
        return employees.stream().filter(employee -> employee.getId() == id).findFirst();
    }

    @Override
    public Employee create(@NonNull String name, @NonNull Integer age, @NonNull Integer salary) {
        Employee newEmployee = Employee.builder()
                .id(generateNewEmployeeId())
                .name(name)
                .age(age)
                .salary(salary)
                .build();

        employees.add(newEmployee);
        return newEmployee;
    }

    @Override
    public boolean delete(int employeeId) {
        Optional<Employee> employeeToDelete = getById(employeeId);
        if (employeeToDelete.isPresent()) {
            return employees.remove(employeeToDelete.get());
        } else {
            return false;
        }
    }

    /* Helper method to facilitate tests only */
    public int getDataSize() {
        return employees.size();
    }

    /* Helper method to facilitate tests only */
    public Employee getHighestPay() {
        return employeeHighestPay;
    }

    private int generateNewEmployeeId() {
        return nextId++;
    }
}