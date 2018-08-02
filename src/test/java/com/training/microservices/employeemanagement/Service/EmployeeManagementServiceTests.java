package com.training.microservices.employeemanagement.Service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.microservices.employeemanagement.common.ResourceNotFoundException;
import com.training.microservices.employeemanagement.model.Employee;
import com.training.microservices.employeemanagement.common.EmployeeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeManagementServiceTests {

	@MockBean
	private EmployeeRepository repository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void verifyAllEmployeeList() throws Exception {
		List<Employee> empDataArray = new ArrayList<>();
		Employee empData = new Employee("ranju", "ranju123", "ranjitha", "ranju", "24/07/2018", "female", "test",
				"security");
		empDataArray.add(empData);
		Mockito.doReturn(empDataArray).when(repository).findAll();
		mockMvc.perform(get("/EmpMgt/getAllEmpDetails").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(
						"[{\"username\":\"ranju\",\"password\":\"ranju123\",\"fullName\":\"ranjitha\",\"emailID\":\"ranju\",\"dateOfBirth\":\"24/07/2018\",\"gender\":\"female\",\"securityQuestion\":\"test\",\"securityAnswer\":\"security\"}]"));
	}

	@Test
	public void verifyEmptyEmployeeList() throws Exception{
		//List<Employee> empDataArray = new ArrayList<>();
		Mockito.doReturn(null).when(repository).findAll();
		//Mockito.when(repository.findAll()).thenThrow(ResourceNotFoundException);

		mockMvc.perform(get("/EmpMgt/getAllEmpDetails"))
				.andExpect(status().isOk());

	}

	@Test
	public void addEmployee() throws Exception {
		Employee empData = new Employee();
		empData.setEmpId((long) 1);
		empData.setUsername("ranju");
		empData.setPassword("ranju123");
		empData.setFullName("ranjitha");
		empData.setEmailID("ranju@gmail.com");
		empData.setDateOfBirth("24/07/2018");
		empData.setGender("female");
		empData.setSecurityQuestion("test");
		empData.setSecurityAnswer("security");

		Mockito.doReturn(empData).when(repository).save(Matchers.any(Employee.class));

		mockMvc.perform(put("/EmpMgt/addEmp").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(
						"{\"empId\":\"1\",\"username\":\"ranju\",\"password\":\"ranju123\",\"fullName\":\"ranjitha\",\"emailID\":\"ranju\",\"dateOfBirth\":\"24-07-2018\",\"gender\":\"female\",\"securityQuestion\":\"test\",\"securityAnswer\":\"security\"}"))
				.andExpect(status().isOk());
	}

	@Test
	public void verifyAddEmployeeInvalid() throws Exception {
		Employee empData = new Employee();
		//String empDetails = null;
		String employeeDTOJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(empData);
		System.out.println("Employee Data ***********" +employeeDTOJson);

		Mockito.doReturn(Long.valueOf(0)).when(repository).save(empData);

		mockMvc.perform(put("/EmpMgt/addEmp").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(employeeDTOJson))
				.andExpect(status().isInternalServerError());
	}
	
	
	@Test
	public void verifyGetEmployeebyEmpId() throws ResourceNotFoundException, Exception {
		Employee empData = new Employee();
		empData.setEmpId((long) 1);
		empData.setUsername("ranju");
		empData.setPassword("ranju123");
		empData.setFullName("ranjitha");
		empData.setEmailID("ranju@gmail.com");
		empData.setDateOfBirth("24/07/2018");
		empData.setGender("female");
		empData.setSecurityQuestion("test");
		empData.setSecurityAnswer("security");

		Mockito.when(repository.findEmployeeByempId(empData.getEmpId())).thenReturn(empData);

		mockMvc.perform(get("/EmpMgt/getByEmpId/1").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(
						"{\"username\":\"ranju\",\"password\":\"ranju123\",\"fullName\":\"ranjitha\",\"emailID\":\"ranju@gmail.com\",\"dateOfBirth\":\"24/07/2018\",\"gender\":\"female\",\"securityQuestion\":\"test\",\"securityAnswer\":\"security\"}"));
	}

	@Test
	public void verifyGetEmployeebyEmpIdForNull() throws Exception {
		
		Mockito.when(repository.findEmployeeByempId(1)).thenReturn(null);

		mockMvc.perform(get("/EmpMgt/getByEmpId/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void verifyDeleteEmployeebyEmpId() throws ResourceNotFoundException, Exception {
		Employee empData = new Employee();
		empData.setEmpId((long) 1);
		empData.setUsername("ranju");
		empData.setPassword("ranju123");
		empData.setFullName("ranjitha");
		empData.setEmailID("ranju@gmail.com");
		empData.setDateOfBirth("24/07/2018");
		empData.setGender("female");
		empData.setSecurityQuestion("test");
		empData.setSecurityAnswer("security");

		//Mockito.doNothing().when(repository).deleteById(empData.getEmpId());
		  Mockito.doNothing().when(repository).deleteById(empData.getEmpId());

		mockMvc.perform(MockMvcRequestBuilders.delete("/EmpMgt/deleteEmp/1").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

	}

	@Test
	public void checkEmployeeLogin() throws ResourceNotFoundException, Exception {
		Employee empData = new Employee();
		empData.setEmpId((long) 1);
		empData.setUsername("ranju");
		empData.setPassword("ranju123");
		
		Mockito.when(repository.findEmployeeByUsername(empData.getUsername())).thenReturn(empData);

		mockMvc.perform(post("/EmpMgt/checkLogin").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content("{\"username\":\"ranju\",\"password\":\"ranju123\"}"))
				.andExpect(status().isOk()).andExpect(content().string("Employee has authenticated successfully"));
	}

	@Test
	public void checkEmployeeLoginFailure() throws ResourceNotFoundException, Exception {
		Employee empData = new Employee();
		empData.setEmpId((long) 1);
		empData.setUsername("ranju");
		empData.setPassword("ranju123");

		Mockito.when(repository.findEmployeeByUsername(empData.getUsername())).thenReturn(empData).equals(false);

		mockMvc.perform(post("/EmpMgt/checkLogin").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content("{\"username\":\"ranju\",\"password\":\"test\"}"))
				.andExpect(status().isOk()).andExpect(content().string("Invalid Password"));

	}

}
