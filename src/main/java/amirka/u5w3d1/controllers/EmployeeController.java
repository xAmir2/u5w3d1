package amirka.u5w3d1.controllers;

import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.exceptions.ValidationEx;
import amirka.u5w3d1.payloads.EmployeeDTO;
import amirka.u5w3d1.payloads.EmployeeResponseDTO;
import amirka.u5w3d1.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
    public Page<Employee> getEmployees(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "name") String orderBy) {
        return employeeService.getAll(page, size, orderBy);
    }

    @GetMapping("/{employeeId}")
    public Employee getById(@PathVariable UUID employeeId) {
        return employeeService.findById(employeeId);
    }

    @PutMapping("/{employeeId}")
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable UUID employeeId) {

        employeeService.findByIdAndDelete(employeeId);
    }
}
