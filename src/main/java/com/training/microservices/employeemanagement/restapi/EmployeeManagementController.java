package com.training.microservices.employeemanagement.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.training.microservices.employeemanagement.common.ResourceNotFoundException;
import com.training.microservices.employeemanagement.model.Employee;
import com.training.microservices.employeemanagement.service.EmployeeManagementService;

@RestController
@RequestMapping("/EmpMgt")
public class EmployeeManagementController {

	@Autowired
	EmployeeManagementService employeeService;

	// To get all employee details
	@RequestMapping(method = RequestMethod.GET, value = "/getAllEmpDetails", produces = "application/json")
	public ResponseEntity<List<Employee>> listAllEmployees() throws ResourceNotFoundException {
		List<Employee> employeeList = employeeService.findAllEmployees();
		if (employeeList.isEmpty()) {
			throw new ResourceNotFoundException("Employee details not found");
		}
		return new ResponseEntity<List<Employee>>(employeeList, HttpStatus.OK);
	}

	// To add employee to database record
	@RequestMapping(method = RequestMethod.PUT, value = "/addEmp", produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> addEmployee(@RequestBody Employee empData) throws Exception {	
			       Long empid =  employeeService.addEmployeeData(empData);
			       System.out.println(empid);
			       if(empid < 0 || empid == 0){
					    throw new Exception("Internal Server error. Try after sometime");
			       } 
			       return new ResponseEntity<Object>("Employee data inserted successfully", HttpStatus.OK);			  
		
	}

	// To delete employee based on employeeId
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteEmp/{empId}", produces = "application/json")
	public ResponseEntity<Object> deleteEmployee(@PathVariable long empId) throws ResourceNotFoundException {
		boolean result = employeeService.deleteEmployeeData(empId);
		if(result == false){
			throw new ResourceNotFoundException("Employee not found");
		}else{
			return new ResponseEntity<Object>("Employee data deleted successfully", HttpStatus.OK);
		}

	}

	// To get the employee details based on employeeId
	@RequestMapping(method = RequestMethod.GET, value = "/getByEmpId/{empId}", produces = "application/json")
	public ResponseEntity<Employee> getEmployeeDetails(@PathVariable long empId)
			throws NullPointerException, ResourceNotFoundException {
		Employee employeeData = employeeService.getEmployeeDetails(empId);
		if (null == employeeData) {
			throw new ResourceNotFoundException("Employee not found");
		}
		return new ResponseEntity<Employee>(employeeData, HttpStatus.OK);
	}

	// To check the employee authentication
	@RequestMapping(method = RequestMethod.POST, value = "/checkLogin", produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> checkEmployeeLogin(@RequestBody Employee empData) {
		boolean result = employeeService.checkEmployeeLogin(empData.getUsername(),empData.getPassword());
		String checkLoginMessage;
		if (false == result) {
			checkLoginMessage = "Invalid Password";
			
		}else{
		checkLoginMessage = "Employee has authenticated successfully";
		}
		return new ResponseEntity<Object>(checkLoginMessage, HttpStatus.OK);
	}
}
