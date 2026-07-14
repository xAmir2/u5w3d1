package amirka.u5w3d1.controllers;

import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.exceptions.ValidationEx;
import amirka.u5w3d1.payloads.EmployeeChangePasswordDTO;
import amirka.u5w3d1.payloads.EmployeeDTO;
import amirka.u5w3d1.payloads.EmployeeResponseDTO;
import amirka.u5w3d1.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDTO saveEmployee(@RequestBody @Validated EmployeeDTO body, BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationEx(errorsList);
        }
        Employee saved = this.employeeService.save(body);
        return new EmployeeResponseDTO(saved.getId());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Employee> getEmployees(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "name") String orderBy) {
        return employeeService.getAll(page, size, orderBy);
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee getById(@PathVariable UUID employeeId) {
        return employeeService.findById(employeeId);
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee updateEmployee(@PathVariable UUID employeeId, @RequestBody @Validated EmployeeDTO body, BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationEx(errorsList);
        }
        return employeeService.findByIdAndUpdate(employeeId, body);
    }

    @PatchMapping("/{employeeId}/avatar")
    public Employee updateAvatar(
            @PathVariable UUID employeeId,
            @RequestParam("avatar") MultipartFile file
    ) {

        return employeeService.updateAvatar(employeeId, file);
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable UUID employeeId) {

        employeeService.findByIdAndDelete(employeeId);
    }

    @GetMapping("/me")
    public Employee getMyProfile(@AuthenticationPrincipal Employee authenticatedEmployee) {
        return authenticatedEmployee;
    }

    @PutMapping("/me")
    public Employee updateMyProfile(@AuthenticationPrincipal Employee authenticatedEmployee,
                                    @RequestBody @Validated EmployeeDTO payload) {
        return this.employeeService.findByIdAndUpdate(authenticatedEmployee.getId(), payload);
    }


    @DeleteMapping("/me")
    public void deleteMyProfile(@AuthenticationPrincipal Employee authenticatedEmployee) {
        this.employeeService.findByIdAndDelete(authenticatedEmployee.getId());
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@AuthenticationPrincipal Employee authenticatedUser, @RequestBody EmployeeChangePasswordDTO body) {
        this.employeeService.updatePassword(authenticatedUser.getId(), body);
    }
}
