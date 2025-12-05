package org.chintanpatel.springbootmanytoone.employee;


import jakarta.validation.Valid;
import org.chintanpatel.springbootmanytoone.department.Department;
import org.chintanpatel.springbootmanytoone.department.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    // Logic to display all employees

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        List<Employee> employeeList = employeeService.getAllEmployeeList();
        model.addAttribute("employeeList", employeeList);
        return "employee/employee-list";
    }

    // Logic to display creates the employee form

    @GetMapping("/employees/create")
    public String createEmployeeForm(Model model) {
        Employee employee = new Employee();
        List<Department> departmentList = departmentService.getAllDepartmentList();
        model.addAttribute("employee", employee);
        model.addAttribute("departmentList", departmentList);
        return "employee/employee-form";
    }

    // Logic to insert or update an employee

    @PostMapping("/employees/insertOrUpdateEmployee")
    public String insertOrUpdateEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            List<Department> departmentList = departmentService.getAllDepartmentList();
            model.addAttribute("departmentList", departmentList);
            return "employee/employee-form";
        }
        if (employee.getId() != null) {
            employee.setId(employee.getId());
            employeeService.updateEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully");
        } else {
            if (employeeService.isEmailExist(employee.getEmail())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Email already exists");
                return "redirect:/employees/create";
            }
            if (employeeService.isUserNameExist(employee.getUserName())) {
                redirectAttributes.addFlashAttribute("errorMessage", "User Name already exists");
                return "redirect:/employees/create";
            }
            employeeService.addEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully");
        }
        return "redirect:/employees";
    }

    // logic to get employee by id and display the employee form

    @GetMapping("/employees/manageEmployee/{id}")
    public String manageEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            List<Department> departmentList = departmentService.getAllDepartmentList();
            model.addAttribute("departmentList", departmentList);
            model.addAttribute("employee", employee);
            return "employee/employee-form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
        }
        return "redirect:/employees";
    }

    // Logic to delete an employee by id

    @GetMapping("/employees/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            employeeService.deleteEmployeeById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
        }
        return "redirect:/employees";
    }

    // Logic to search employees by name

    @GetMapping("/employees/search/employeeName")
    public String searchEmployeeByName(String employeeName, Model model){
        List<Employee> employeeList = employeeService.searchEmployeeByEmployeeName(employeeName);
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("employeeName", employeeName);
        model.addAttribute("searchType", "employeeName");
        return "employee/employee-list";
    }

    // Logic to search employees by department

    @GetMapping("/employees/search/departmentName")
    public String searchEmployeeByDepartment(String departmentName, Model model){
        List<Employee> employeeList = employeeService.searchEmployeeByDepartmentName(departmentName);
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("departmentName", departmentName);
        model.addAttribute("searchType", "departmentName");
        return "employee/employee-list";
    }
}
