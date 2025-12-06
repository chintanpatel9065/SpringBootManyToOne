package org.chintanpatel.springbootmanytoone.employee;

import org.chintanpatel.springbootmanytoone.department.Department;
import org.chintanpatel.springbootmanytoone.department.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private DepartmentService departmentService;

    @BeforeEach
    void setup() {
        EmployeeController controller = new EmployeeController(employeeService, departmentService);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(validator)
                .build();
    }

    private Department dep(long id, String name) {
        Department d = new Department();
        d.setId(id);
        d.setDepartmentName(name);
        return d;
    }

    private Employee employee(long id) {
        Employee e = new Employee();
        e.setId(id);
        e.setFirstName("John");
        e.setMiddleName("Q");
        e.setLastName("Doe");
        e.setAddress("123 Street");
        e.setEmail("john@acme.com");
        e.setMobileNumber("9999999999");
        e.setHiringDate(LocalDate.of(2024, 1, 1));
        e.setSalary(new BigDecimal("1000.00"));
        e.setUserName("johndoe");
        e.setPassword("password8");
        e.setDepartment(dep(1L, "IT"));
        return e;
    }

    @Test
    @DisplayName("GET /employees shows list")
    void listEmployees() throws Exception {
        given(employeeService.getAllEmployeeList()).willReturn(List.of(employee(1)));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employeeList"));
    }

    @Test
    @DisplayName("GET /employees/create shows form with department list")
    void createEmployeeForm() throws Exception {
        given(departmentService.getAllDepartmentList()).willReturn(List.of(dep(1, "IT")));

        mockMvc.perform(get("/employees/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeExists("departmentList"));
    }

    @Test
    @DisplayName("POST insertOrUpdate - validation errors return form with departments")
    void insertOrUpdateEmployee_validationErrors() throws Exception {
        given(departmentService.getAllDepartmentList()).willReturn(List.of(dep(1, "IT")));

        mockMvc.perform(post("/employees/insertOrUpdateEmployee")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        // intentionally omit required fields to trigger @Valid errors
                        .param("firstName", "")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("departmentList"));

        Mockito.verifyNoInteractions(employeeService);
    }

    @Test
    @DisplayName("POST insert - duplicate email redirects back with error")
    void insertEmployee_duplicateEmail() throws Exception {
        given(employeeService.isEmailExist("john@acme.com")).willReturn(true);

        mockMvc.perform(post("/employees/insertOrUpdateEmployee")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("middleName", "Q")
                        .param("lastName", "Doe")
                        .param("address", "123 Street")
                        .param("email", "john@acme.com")
                        .param("mobileNumber", "9999999999")
                        .param("hiringDate", "2024-01-01")
                        .param("salary", "1000.00")
                        .param("userName", "johndoe")
                        .param("password", "password8")
                        .param("department", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees/create"))
                .andExpect(flash().attribute("errorMessage", containsString("Email already exists")));
    }

    @Test
    @DisplayName("POST insert - duplicate username redirects back with error")
    void insertEmployee_duplicateUserName() throws Exception {
        given(employeeService.isEmailExist("john@acme.com")).willReturn(false);
        given(employeeService.isUserNameExist("johndoe")).willReturn(true);

        mockMvc.perform(post("/employees/insertOrUpdateEmployee")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("middleName", "Q")
                        .param("lastName", "Doe")
                        .param("address", "123 Street")
                        .param("email", "john@acme.com")
                        .param("mobileNumber", "9999999999")
                        .param("hiringDate", "2024-01-01")
                        .param("salary", "1000.00")
                        .param("userName", "johndoe")
                        .param("password", "password8")
                        .param("department", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees/create"))
                .andExpect(flash().attribute("errorMessage", containsString("User Name already exists")));
    }

    @Test
    @DisplayName("POST insert - success redirects to list")
    void insertEmployee_success() throws Exception {
        given(employeeService.isEmailExist(anyString())).willReturn(false);
        given(employeeService.isUserNameExist(anyString())).willReturn(false);

        mockMvc.perform(post("/employees/insertOrUpdateEmployee")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("middleName", "Q")
                        .param("lastName", "Doe")
                        .param("address", "123 Street")
                        .param("email", "john@acme.com")
                        .param("mobileNumber", "9999999999")
                        .param("hiringDate", "2024-01-01")
                        .param("salary", "1000.00")
                        .param("userName", "johndoe")
                        .param("password", "password8")
                        .param("department", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attribute("successMessage", containsString("Employee added successfully")));

        verify(employeeService).addEmployee(org.mockito.ArgumentMatchers.any(Employee.class));
    }

    @Test
    @DisplayName("POST update - success redirects to list")
    void updateEmployee_success() throws Exception {
        mockMvc.perform(post("/employees/insertOrUpdateEmployee")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "5")
                        .param("firstName", "John")
                        .param("middleName", "Q")
                        .param("lastName", "Doe")
                        .param("address", "123 Street")
                        .param("email", "john@acme.com")
                        .param("mobileNumber", "9999999999")
                        .param("hiringDate", "2024-01-01")
                        .param("salary", "1000.00")
                        .param("userName", "johndoe")
                        .param("password", "password8")
                        .param("department", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attribute("successMessage", containsString("Employee updated successfully")));

        verify(employeeService).updateEmployee(org.mockito.ArgumentMatchers.any(Employee.class));
    }

    @Test
    @DisplayName("GET manageEmployee/{id} - found shows form, not found redirects")
    void manageEmployee() throws Exception {
        given(employeeService.getEmployeeById(1L)).willReturn(employee(1));
        given(departmentService.getAllDepartmentList()).willReturn(List.of(dep(1, "IT")));

        mockMvc.perform(get("/employees/manageEmployee/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employee", "departmentList"));

        given(employeeService.getEmployeeById(2L)).willReturn(null);
        mockMvc.perform(get("/employees/manageEmployee/{id}", 2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attribute("errorMessage", containsString("Employee not found")));
    }

    @Test
    @DisplayName("GET deleteEmployee/{id} - success and not found paths")
    void deleteEmployee() throws Exception {
        given(employeeService.getEmployeeById(1L)).willReturn(employee(1));

        mockMvc.perform(get("/employees/deleteEmployee/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attribute("successMessage", containsString("deleted successfully")));

        given(employeeService.getEmployeeById(2L)).willReturn(null);
        mockMvc.perform(get("/employees/deleteEmployee/{id}", 2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attribute("errorMessage", containsString("Employee not found")));
    }

    @Test
    @DisplayName("GET search endpoints populate model and return list view")
    void searchEndpoints() throws Exception {
        given(employeeService.searchEmployeeByEmployeeName("jo")).willReturn(List.of(employee(1)));
        mockMvc.perform(get("/employees/search/employeeName").param("employeeName", "jo"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("searchType", "employeeName"))
                .andExpect(model().attributeExists("employeeList"));

        given(employeeService.searchEmployeeByDepartmentName("it")).willReturn(List.of(employee(1)));
        mockMvc.perform(get("/employees/search/departmentName").param("departmentName", "it"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("searchType", "departmentName"))
                .andExpect(model().attributeExists("employeeList"));
    }
}
