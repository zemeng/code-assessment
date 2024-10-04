package com.example.rqchallenge.model;

import lombok.Builder;
import lombok.Data;

/* Data model to represent Employee */
@Data
@Builder
public class Employee {

    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "employee_name";
    public static final String SALARY_KEY = "employee_salary";
    public static final String AGE_KEY = "employee_age";
    public static final String IMAGE_KEY = "profile_image";

    private int id;

    private String name;

    private int salary;

    private int age;

    private String image;
}