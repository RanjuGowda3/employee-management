package com.training.microservices.employeemanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.training.microservices.employeemanagement.common.EmployeeRepository;
import com.training.microservices.employeemanagement.model.Employee;

@Service
public class EmployeeManagementService {
		
    @Autowired
    EmployeeRepository repository;
	
    @Cacheable("employee.all")
	public List<Employee> findAllEmployees() {
		List<Employee> employeeList = new ArrayList<>();
		employeeList = repository.findAll();
		return employeeList;

	}

    @Cacheable("employee.byId")
	public Long addEmployeeData(Employee empData) {	
		empData = repository.save(empData);
		long empId = empData.getEmpId();
		return empId;
	}

    @CacheEvict("employee.byId")
	public boolean deleteEmployeeData(long empId) {
		boolean result = false;
		Employee employeeData = repository.findEmployeeByempId(empId);
		if (employeeData == null) {
			return result;
		} else {
			repository.deleteById(empId);
			result = true;
		}
		return result;
	}

	public Employee getEmployeeDetails(long empId) {
		Employee employeeData = repository.findEmployeeByempId(empId);
		return employeeData;	
	}


	public boolean checkEmployeeLogin(String username, String password) {
		boolean result = false;
		Employee empDetails = repository.findEmployeeByUsername(username);
		if(empDetails.getUsername().equals(username) && empDetails.getPassword().equals(password)){
			result = true;
		}
		return result;
	}

	

}
