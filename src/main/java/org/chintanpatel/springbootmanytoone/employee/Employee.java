package org.chintanpatel.springbootmanytoone.employee;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.chintanpatel.springbootmanytoone.department.Department;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotEmpty(message = "Please Provide First Name")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 255)
    @NotEmpty(message = "Please Provide Middle Name")
    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Size(max = 255)
    @NotEmpty(message = "Please Provide Last Name")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 255)
    @NotEmpty(message = "Please Provide Address")
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 255)
    @Email(message = "Please Provide Valid Email")
    @NotEmpty(message = "Please Provide Email")
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotEmpty(message = "Please Provide Mobile Number")
    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please Provide Hiring Date")
    @Column(name = "hiring_date", nullable = false)
    private LocalDate hiringDate;

    @NotNull(message = "Please Provide Salary")
    @DecimalMin(value = "0.01", message = "Salary must be greater than 0.01")
    @Column(name = "salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @Size(max = 255)
    @NotEmpty(message = "Please Provide User Name")
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
    @NotEmpty(message = "Please Provide Password")
    @Column(name = "password", nullable = false, length = 15)
    private String password;

    @NotNull(message = "Please Provide Department")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ColumnDefault("nextval('employee_department_id_seq')")
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

}