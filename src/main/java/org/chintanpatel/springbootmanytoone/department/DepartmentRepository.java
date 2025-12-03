package org.chintanpatel.springbootmanytoone.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("departmentRepository")
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentName(String departmentName);
}