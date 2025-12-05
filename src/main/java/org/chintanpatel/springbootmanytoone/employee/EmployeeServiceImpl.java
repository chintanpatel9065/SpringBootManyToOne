package org.chintanpatel.springbootmanytoone.employee;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployeeList() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> searchEmployeeByEmployeeName(String employeeName) {
        return employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(employeeName,employeeName);
    }

    @Override
    public List<Employee> searchEmployeeByDepartmentName(String departmentName) {
        return employeeRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
    }

    @Override
    public boolean isEmailExist(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public boolean isUserNameExist(String userName) {
        return employeeRepository.existsByUserName(userName);
    }
}
