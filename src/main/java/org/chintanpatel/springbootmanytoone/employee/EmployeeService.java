package org.chintanpatel.springbootmanytoone.employee;

import java.util.List;

public interface EmployeeService {

    void addEmployee(Employee employee);

    List<Employee>getAllEmployeeList();

    Employee getEmployeeById(Long id);

    void updateEmployee(Employee employee);

    void deleteEmployeeById(Long id);

    List<Employee>searchEmployeeByEmployeeName(String employeeName);

    List<Employee>searchEmployeeByDepartmentName(String departmentName);

    boolean isEmailExist(String email);

    boolean isUserNameExist(String userName);
}
