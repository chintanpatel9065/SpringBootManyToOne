package org.chintanpatel.springbootmanytoone.department;

import java.util.List;

public interface DepartmentService {

    void addDepartment(Department department);

    List<Department>getAllDepartmentList();

    Department getDepartmentById(Long id);

    void updateDepartment(Department department);

    void deleteDepartmentById(Long id);

    boolean isDepartmentExist(String departmentName);
}
