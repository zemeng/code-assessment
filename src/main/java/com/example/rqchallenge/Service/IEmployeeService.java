package com.example.rqchallenge.Service;

import com.example.rqchallenge.model.Employee;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

/* Interface for employee service */
public interface IEmployeeService {

    List<Employee> getAll();

    Optional<Employee> getById(int id);

    Employee create(@NonNull String name, @NonNull Integer age, @NonNull Integer salary);

    /* The service contract says the status "success" is returned if the call succeeds
    *  Assume a returned status should be good enough.
    * */
    boolean delete(int employeeId);
}
