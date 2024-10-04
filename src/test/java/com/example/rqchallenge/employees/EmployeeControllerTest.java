package com.example.rqchallenge.employees;
import com.example.rqchallenge.Service.DummyEmployeeService;
import com.example.rqchallenge.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeControllerTest {
    private EmployeeController controller;
    private DummyEmployeeService dummyService;
    @BeforeEach
    void setup() {
        dummyService = new DummyEmployeeService(20, 3, Integer.MAX_VALUE);
        controller = new EmployeeController(dummyService);
    }

    @Test
    void returnCorrectDataSize_when_callingGetAll() {
        List<Employee> employees = controller.getAllEmployees().getBody();
        assertEquals(dummyService.getDataSize(), employees.size());
    }

    @Test
    void returnHighestSalaryEmployee_when_callingGetAll() {
        int highest = controller.getHighestSalaryOfEmployees().getBody();
        assertEquals(dummyService.getHighestPay().getSalary(), highest);
    }

    @Test
    void returnCorrectEmployee_when_callingGetById() {
        Employee employee = controller.getEmployeeById("2").getBody();
        assertEquals("User2", employee.getName());
    }

    @Test
    void returnCorrectEmployees_when_callingSearchByName() {
        List<Employee> list = controller.getEmployeesByNameSearch("deleted").getBody();
        assertEquals(3, list.size());
    }

    @Test
    void returnCorrectEmployees_when_callingGetTop10() {
        List<String> list = controller.getTopTenHighestEarningEmployeeNames().getBody();
        assertEquals(10, list.size());
        assertTrue(list.contains("User_highest"));
    }

    @Test
    void returnCorrectNewEmployee_when_callingCreateEmployee() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "test1");
        map.put("salary", 123);
        map.put("age", 90);

        Employee employee = controller.createEmployee(map).getBody();
        assertEquals("test1", employee.getName());
        assertTrue(employee.getId() != 0);
    }

    @Test
    void deleteSuccessfully_when_callingDelete() {
        String name = controller.deleteEmployeeById("2").getBody();

        List<Employee> employees = controller.getAllEmployees().getBody();
        boolean employeeFound = false;
        for (Employee employee : employees) {
            if (employee.getId() == 2) {employeeFound = true;}
        }
        assertFalse(employeeFound);
        assertEquals("User2", name);
    }
}
