package org.chintanpatel.springbootmanytoone.department;

import jakarta.validation.Valid;
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
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Logic to display all departments

    @GetMapping("/departments")
    public String listDepartments(Model model){
        List<Department> departmentList = departmentService.getAllDepartmentList();
        model.addAttribute("departmentList", departmentList);
        return "department/department-list";
    }

    // Logic to display creates a department form

    @GetMapping("/departments/create")
    public String createDepartmentForm(Model model){
        Department department = new Department();
        model.addAttribute("department", department);
        return "department/department-form";
    }

    // Logic to insert or update a department

    @PostMapping("/departments/insertOrUpdateDepartment")
    public String insertOrUpdateDepartment(@Valid @ModelAttribute("department") Department department, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        if(bindingResult.hasErrors()){
            return "department/department-form";
        }
        if (department.getId() != null) {
            department.setId(department.getId());
            departmentService.updateDepartment(department);
            redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully");
        } else {
            if (departmentService.isDepartmentExist(department.getDepartmentName())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Department already exists");
                return "redirect:/departments/create";
            }
            departmentService.addDepartment(department);
            redirectAttributes.addFlashAttribute("successMessage", "Department added successfully");
        }
        return "redirect:/departments";
    }

    // Logic to get department by id and display the department form

    @GetMapping("/departments/manageDepartment/{id}")
    public String manageDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model){
        Department department = departmentService.getDepartmentById(id);
        if (department != null) {
            model.addAttribute("department", department);
            return "department/department-form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Department not found");
            return "redirect:/departments";
        }
    }

    // Logic to delete a department by id

    @GetMapping("/departments/deleteDepartment/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Department department = departmentService.getDepartmentById(id);
        if (department != null) {
            departmentService.deleteDepartmentById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Department not found");
        }
        return "redirect:/departments";
    }
}
