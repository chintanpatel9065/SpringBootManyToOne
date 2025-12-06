package org.chintanpatel.springbootmanytoone.department;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department dep(long id, String name) {
        Department d = new Department();
        d.setId(id);
        d.setDepartmentName(name);
        return d;
    }

    @Test
    void addDepartment_delegatesToRepository() {
        Department d = dep(0, "IT");
        departmentService.addDepartment(d);
        verify(departmentRepository).save(d);
    }

    @Test
    void getAllDepartmentList_returnsData() {
        when(departmentRepository.findAll()).thenReturn(List.of(dep(1, "IT")));
        assertThat(departmentService.getAllDepartmentList()).hasSize(1);
    }

    @Test
    void getDepartmentById_found_and_notFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dep(1, "IT")));
        when(departmentRepository.findById(2L)).thenReturn(Optional.empty());
        assertThat(departmentService.getDepartmentById(1L)).isNotNull();
        assertThat(departmentService.getDepartmentById(2L)).isNull();
    }

    @Test
    void updateDepartment_delegatesToRepository() {
        Department d = dep(1, "IT");
        departmentService.updateDepartment(d);
        verify(departmentRepository).save(d);
    }

    @Test
    void deleteDepartmentById_delegatesToRepository() {
        departmentService.deleteDepartmentById(3L);
        verify(departmentRepository).deleteById(3L);
    }

    @Test
    void isDepartmentExist_delegatesToRepository() {
        when(departmentRepository.existsByDepartmentName("IT")).thenReturn(true);
        assertThat(departmentService.isDepartmentExist("IT")).isTrue();
    }
}
