package com.training.microservices.employeemanagement.common;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.training.microservices.employeemanagement.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{
	
    List<Employee> findAll();
	Employee findEmployeeByUsername(String username);
	Employee findEmployeeByempId(long empId);
} 
