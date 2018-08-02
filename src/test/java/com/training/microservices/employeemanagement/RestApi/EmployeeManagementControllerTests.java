package com.training.microservices.employeemanagement.RestApi;

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
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.microservices.employeemanagement.common.ResourceNotFoundException;
import com.training.microservices.employeemanagement.model.Employee;
import com.training.microservices.employeemanagement.service.EmployeeManagementService;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc

public class EmployeeManagementControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	EmployeeManagementService employeeService;

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
		Mockito.doReturn(empDataArray).when(employeeService).findAllEmployees();

		mockMvc.perform(get("/EmpMgt/getAllEmpDetails").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(
						"[{\"username\":\"ranju\",\"password\":\"ranju123\",\"fullName\":\"ranjitha\",\"emailID\":\"ranju\",\"dateOfBirth\":\"24/07/2018\",\"gender\":\"female\",\"securityQuestion\":\"test\",\"securityAnswer\":\"security\"}]"));
	}

	@Test
	public void verifyEmptyEmployeeList() throws ResourceNotFoundException, Exception {
		List<Employee> empDataArray = new ArrayList<>();
		Mockito.doReturn(empDataArray).when(employeeService).findAllEmployees();
		mockMvc.perform(get("/EmpMgt/getAllEmpDetails").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().json("{\"message\":\"Employee details not found\"}"));

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

		Mockito.doReturn(empData.getEmpId()).when(employeeService).addEmployeeData(Matchers.any(Employee.class));

		mockMvc.perform(put("/EmpMgt/addEmp").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(
						"{\"username\":\"ranju\",\"password\":\"ranju123\",\"fullName\":\"ranjitha\",\"emailID\":\"ranju\",\"dateOfBirth\":\"24-07-2018\",\"gender\":\"female\",\"securityQuestion\":\"test\",\"securityAnswer\":\"security\"}"))
				.andExpect(status().isOk());
	}

	
	@Test
	public void verifyAddEmployeeInvalid() throws Exception {
		Employee empData = new Employee();

		String employeeDTOJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(empData);

		Mockito.doReturn(Long.valueOf(0)).when(employeeService).addEmployeeData(empData);

		mockMvc.perform(put("/EmpMgt/addEmp").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(employeeDTOJson)).andExpect(status().isInternalServerError());;
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
		
		Mockito.when(employeeService.getEmployeeDetails(empData.getEmpId())).thenReturn(empData);

		mockMvc.perform(get("/EmpMgt/getByEmpId/1").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(
						"{\"username\":\"ranju\",\"password\":\"ranju123\",\"fullName\":\"ranjitha\",\"emailID\":\"ranju@gmail.com\",\"dateOfBirth\":\"24/07/2018\",\"gender\":\"female\",\"securityQuestion\":\"test\",\"securityAnswer\":\"security\"}"));

	}

	@Test
	public void verifyGetEmployeebyEmpIdForNull() throws Exception {
		Mockito.when(employeeService.getEmployeeDetails(1)).thenReturn(null);

		mockMvc.perform(get("/EmpMgt/getByEmpId/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().json("{\"message\":\"Employee not found\"}"));

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

//		Mockito.doNothing().when(employeeService).deleteEmployeeData(empData.getEmpId());
		Mockito.doReturn(true).when(employeeService).deleteEmployeeData(empData.getEmpId());

		mockMvc.perform(MockMvcRequestBuilders.delete("/EmpMgt/deleteEmp/1").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	public void checkEmployeeLogin() throws ResourceNotFoundException, Exception {
		Employee empData = new Employee();
		empData.setEmpId((long) 1);
		empData.setUsername("ranju");
		empData.setPassword("ranju123");
		Mockito.when(employeeService.checkEmployeeLogin(empData.getUsername(), empData.getPassword())).thenReturn(true);

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

		Mockito.when(employeeService.checkEmployeeLogin(empData.getUsername(), empData.getPassword())).thenReturn(false);
		
		mockMvc.perform(post("/EmpMgt/checkLogin").contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content("{\"username\":\"ranju\",\"password\":\"ranru123\"}"))
				.andExpect(status().isOk()).andExpect(content().string("Invalid Password"));

	}

}
