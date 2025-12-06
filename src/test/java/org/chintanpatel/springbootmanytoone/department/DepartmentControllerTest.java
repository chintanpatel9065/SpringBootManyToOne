package org.chintanpatel.springbootmanytoone.department;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @BeforeEach
    void setup() {
        DepartmentController controller = new DepartmentController(departmentService);
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

    @Test
    @DisplayName("GET /departments shows list")
    void listDepartments() throws Exception {
        given(departmentService.getAllDepartmentList()).willReturn(List.of(dep(1, "IT")));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("departmentList"));
    }

    @Test
    @DisplayName("GET /departments/create shows form")
    void createForm() throws Exception {
        mockMvc.perform(get("/departments/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("department"));
    }

    @Test
    @DisplayName("POST insert - validation errors return form")
    void insert_validationErrors() throws Exception {
        mockMvc.perform(post("/departments/insertOrUpdateDepartment")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("departmentName", "")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("department/department-form"));
    }

    @Test
    @DisplayName("POST insert - duplicate name redirects back with error")
    void insert_duplicateName() throws Exception {
        given(departmentService.isDepartmentExist("IT")).willReturn(true);

        mockMvc.perform(post("/departments/insertOrUpdateDepartment")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("departmentName", "IT")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments/create"))
                .andExpect(flash().attribute("errorMessage", containsString("Department already exists")));
    }

    @Test
    @DisplayName("POST insert - success redirects to list")
    void insert_success() throws Exception {
        given(departmentService.isDepartmentExist("IT")).willReturn(false);

        mockMvc.perform(post("/departments/insertOrUpdateDepartment")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("departmentName", "IT")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"))
                .andExpect(flash().attribute("successMessage", containsString("Department added successfully")));

        verify(departmentService).addDepartment(org.mockito.ArgumentMatchers.any(Department.class));
    }

    @Test
    @DisplayName("POST update - success redirects to list")
    void update_success() throws Exception {
        mockMvc.perform(post("/departments/insertOrUpdateDepartment")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "5")
                        .param("departmentName", "HR")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"))
                .andExpect(flash().attribute("successMessage", containsString("Department updated successfully")));

        verify(departmentService).updateDepartment(org.mockito.ArgumentMatchers.any(Department.class));
    }

    @Test
    @DisplayName("GET manageDepartment/{id} - found loads form; not found redirects")
    void manageDepartment() throws Exception {
        given(departmentService.getDepartmentById(1L)).willReturn(dep(1, "IT"));
        mockMvc.perform(get("/departments/manageDepartment/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("department"));

        given(departmentService.getDepartmentById(2L)).willReturn(null);
        mockMvc.perform(get("/departments/manageDepartment/{id}", 2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"))
                .andExpect(flash().attribute("errorMessage", containsString("Department not found")));
    }

    @Test
    @DisplayName("GET deleteDepartment/{id} - success and not found paths")
    void deleteDepartment() throws Exception {
        given(departmentService.getDepartmentById(1L)).willReturn(dep(1, "IT"));
        mockMvc.perform(get("/departments/deleteDepartment/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"))
                .andExpect(flash().attribute("successMessage", containsString("deleted successfully")));

        given(departmentService.getDepartmentById(2L)).willReturn(null);
        mockMvc.perform(get("/departments/deleteDepartment/{id}", 2))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"))
                .andExpect(flash().attribute("errorMessage", containsString("Department not found")));
    }
}
