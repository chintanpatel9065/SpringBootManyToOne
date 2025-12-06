package org.chintanpatel.springbootmanytoone.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee sample;

    @BeforeEach
    void setUp() {
        sample = new Employee();
        sample.setId(1L);
        sample.setFirstName("John");
        sample.setMiddleName("Q");
        sample.setLastName("Doe");
        sample.setAddress("123 Street");
        sample.setEmail("john.doe@example.com");
        sample.setMobileNumber("1234567890");
        sample.setHiringDate(LocalDate.of(2024, 1, 1));
        sample.setSalary(new BigDecimal("1000.00"));
        sample.setUserName("johndoe");
        sample.setPassword("password8");
    }

    @Test
    void addEmployee_delegatesToRepository() {
        employeeService.addEmployee(sample);
        verify(employeeRepository).save(sample);
    }

    @Test
    void getAllEmployeeList_returnsRepositoryData() {
        when(employeeRepository.findAll()).thenReturn(List.of(sample));
        assertThat(employeeService.getAllEmployeeList()).containsExactly(sample);
    }

    @Test
    void getEmployeeById_found_returnsEntity() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sample));
        assertThat(employeeService.getEmployeeById(1L)).isEqualTo(sample);
    }

    @Test
    void getEmployeeById_notFound_returnsNull() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());
        assertThat(employeeService.getEmployeeById(2L)).isNull();
    }

    @Test
    void updateEmployee_delegatesToRepository() {
        employeeService.updateEmployee(sample);
        verify(employeeRepository).save(sample);
    }

    @Test
    void deleteEmployeeById_delegatesToRepository() {
        employeeService.deleteEmployeeById(99L);
        verify(employeeRepository).deleteById(99L);
    }

    @Test
    void searchEmployeeByEmployeeName_callsRepositoryWithBothNames() {
        when(employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("jo", "jo"))
                .thenReturn(List.of(sample));
        List<Employee> list = employeeService.searchEmployeeByEmployeeName("jo");
        assertThat(list).containsExactly(sample);
    }

    @Test
    void searchEmployeeByDepartmentName_callsRepository() {
        when(employeeRepository.findByDepartmentNameContainingIgnoreCase("it")).thenReturn(List.of(sample));
        assertThat(employeeService.searchEmployeeByDepartmentName("it")).containsExactly(sample);
    }

    @Test
    void isEmailExist_delegatesToRepository() {
        when(employeeRepository.existsByEmail("a@b.com")).thenReturn(true);
        assertThat(employeeService.isEmailExist("a@b.com")).isTrue();
    }

    @Test
    void isUserNameExist_delegatesToRepository() {
        when(employeeRepository.existsByUserName("uname")).thenReturn(false);
        assertThat(employeeService.isUserNameExist("uname")).isFalse();
    }
}
