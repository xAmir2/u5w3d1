package amirka.u5w3d1.services;

import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.exceptions.BadRequestEx;
import amirka.u5w3d1.exceptions.FileUploadEx;
import amirka.u5w3d1.exceptions.NotFoundEx;
import amirka.u5w3d1.payloads.EmployeeChangePasswordDTO;
import amirka.u5w3d1.payloads.EmployeeDTO;
import amirka.u5w3d1.repositories.BookingRepository;
import amirka.u5w3d1.repositories.EmployeeRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;
    private final Cloudinary fileUploader;
    private PasswordEncoder bCrypt;

    public EmployeeService(EmployeeRepository employeeRepository, BookingRepository bookingRepository, Cloudinary fileUploader, PasswordEncoder bCrypt) {
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
        this.fileUploader = fileUploader;
        this.bCrypt = bCrypt;
    }

    public Employee save(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.email())) {
            throw new BadRequestEx("The following e-mail address " + employeeDTO.email() + " is already in use.");
        }

        Employee newEmployee = new Employee(employeeDTO.username(), employeeDTO.name(), employeeDTO.surname(),
                employeeDTO.email(), bCrypt.encode(employeeDTO.password()));
        return employeeRepository.save(newEmployee);
    }

    public Employee findById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundEx(id));
    }

    public Page<Employee> getAll(int page, int size, String orderBy) {
        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));

        return employeeRepository.findAll(pageable);
    }

    public Employee findByIdAndUpdate(UUID id, EmployeeDTO employeeDTO) {
        Employee found = findById(id);

        if (!found.getEmail()
                .equals(employeeDTO.email()) && employeeRepository.existsByEmail(employeeDTO.email())) {
            throw new BadRequestEx("Email address " + employeeDTO.email() + " is already in use.");
        }

        found.setUsername(employeeDTO.username());
        found.setName(employeeDTO.name());
        found.setSurname(employeeDTO.surname());
        found.setEmail(employeeDTO.email());

        return employeeRepository.save(found);
    }

    @Transactional
    public void findByIdAndDelete(UUID id) {
        Employee found = this.findById(id);

        bookingRepository.deleteAllByEmployee_Id(id);
        this.employeeRepository.delete(found);
    }

    public Employee updateAvatar(UUID authorId, MultipartFile file) {

        if (file.isEmpty()) {
            throw new FileUploadEx("The uploaded file cannot be empty");
        }

        if (!file.getContentType()
                .equals("image/gif")
                && !file.getContentType()
                .equals("image/jpeg")
                && !file.getContentType()
                .equals("image/png")) {

            throw new FileUploadEx(
                    "Only GIF, JPEG and PNG images are allowed"
            );
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new FileUploadEx(
                    "The image cannot be bigger than 5MB"
            );
        }

        Employee employee = employeeRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundEx(authorId));

        try {

            Map result = fileUploader.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            String url = (String) result.get("secure_url");

            employee.setAvatar(url);

            return employeeRepository.save(employee);

        } catch (IOException e) {

            throw new FileUploadEx(
                    "Error while uploading image"
            );
        }
    }

    public Employee findByEmail(String email) {
        return this.employeeRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEx("Employee with the email: " + email + " was not found!"));
    }

    public void updatePassword(UUID userId, EmployeeChangePasswordDTO payload) {
        Employee found = this.findById(userId);

        if (!bCrypt.matches(payload.oldPassword(), found.getPassword())) {
            throw new BadRequestEx("Password do not match!");
        }

        found.setPassword(bCrypt.encode(payload.newPassword()));

        this.employeeRepository.save(found);
    }
}
