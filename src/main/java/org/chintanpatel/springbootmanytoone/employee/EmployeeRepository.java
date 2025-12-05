package org.chintanpatel.springbootmanytoone.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("employeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee>findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    @Query("select e from Employee e where lower(e.department.departmentName)like lower(concat('%',:departmentName,'%'))")
    List<Employee>findByDepartmentNameContainingIgnoreCase(String departmentName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);


}