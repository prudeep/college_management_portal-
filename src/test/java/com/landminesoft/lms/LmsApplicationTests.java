package com.landminesoft.lms;

import com.landminesoft.lms.config.JwtUtils;
import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.entity.Student;
import com.landminesoft.lms.exception.*;
import com.landminesoft.lms.repository.*;
import com.landminesoft.lms.service.AuthService;
import com.landminesoft.lms.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LmsApplicationTests {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @InjectMocks
    private ProfileService profileService;

    private StudentRegisterDTO studentDTO;
    private LoginDTO loginDTO;
    private Student mockStudent;

    @BeforeEach
    void setUp() {
        studentDTO = new StudentRegisterDTO(
                "Rahul Kumar",
                "rahul@college.edu",
                "9876543210",
                "SecurePass@123",
                "CSE",
                2024
        );

        loginDTO = new LoginDTO("rahul@college.edu", "SecurePass@123");

        mockStudent = Student.builder()
                .id(1L)
                .name("Rahul Kumar")
                .email("rahul@college.edu")
                .phone("9876543210")
                .passwordHash("$2a$10$hashedpassword")
                .branch("CSE")
                .semester(1)
                .enrollmentYear(2024)
                .build();
    }

    // ── AuthService Tests ──

    @Test
    void registerStudent_Success() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(studentRepository.save(any(Student.class))).thenReturn(mockStudent);

        RegisterResponseDTO response = authService.registerStudent(studentDTO);

        assertNotNull(response);
        assertEquals("Rahul Kumar", response.getName());
        assertEquals("rahul@college.edu", response.getEmail());
        assertEquals("STUDENT", response.getRole());
        assertEquals("Registration successful. Please login.", response.getMessage());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void registerStudent_EmailAlreadyExists_ThrowsException() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () ->
                authService.registerStudent(studentDTO));

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void loginStudent_Success() {
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(mockStudent));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(anyLong(), anyString(), anyString()))
                .thenReturn("mock.jwt.token");

        JwtResponseDTO response = authService.loginStudent(loginDTO);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        assertEquals("STUDENT", response.getRole());
        assertEquals("rahul@college.edu", response.getEmail());
    }

    @Test
    void loginStudent_InvalidPassword_ThrowsException() {
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(mockStudent));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () ->
                authService.loginStudent(loginDTO));
    }

    @Test
    void loginStudent_EmailNotFound_ThrowsException() {
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () ->
                authService.loginStudent(loginDTO));
    }

    // ── JwtUtils Tests ──

    @Test
    void jwtUtils_GenerateAndValidateToken() {
        JwtUtils jwt = new JwtUtils();
        setPrivateField(jwt, "jwtSecret",
                "LandmineSoftLMSSecretKey2024SuperSecureKeyForJWTTokenGeneration");
        setPrivateField(jwt, "jwtExpiration", 86400000L);

        String token = jwt.generateToken(1L, "rahul@college.edu", "STUDENT");

        assertNotNull(token);
        assertTrue(jwt.validateToken(token));
        assertEquals("rahul@college.edu", jwt.getEmailFromToken(token));
        assertEquals("STUDENT", jwt.getRoleFromToken(token));
        assertEquals(1L, jwt.getUserIdFromToken(token));
    }

    @Test
    void jwtUtils_InvalidToken_ReturnsFalse() {
        JwtUtils jwt = new JwtUtils();
        setPrivateField(jwt, "jwtSecret",
                "LandmineSoftLMSSecretKey2024SuperSecureKeyForJWTTokenGeneration");
        setPrivateField(jwt, "jwtExpiration", 86400000L);

        assertFalse(jwt.validateToken("invalid.token.here"));
    }

    // ── ProfileService Tests ──

    @Test
    void getStudentProfile_Success() {
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(mockStudent));

        Student result = profileService.getStudentProfile("rahul@college.edu");

        assertNotNull(result);
        assertEquals("Rahul Kumar", result.getName());
        assertEquals("rahul@college.edu", result.getEmail());
    }

    @Test
    void getStudentProfile_NotFound_ThrowsException() {
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                profileService.getStudentProfile("notfound@college.edu"));
    }

    @Test
    void changePassword_Success() {
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(mockStudent));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");
        when(studentRepository.save(any(Student.class))).thenReturn(mockStudent);

        ChangePasswordDTO dto = new ChangePasswordDTO("OldPass@123", "NewPass@456");
        String result = profileService.changePassword("rahul@college.edu", dto);

        assertEquals("Password changed successfully", result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void changePassword_WrongOldPassword_ThrowsException() {
        when(studentRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(mockStudent));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        ChangePasswordDTO dto = new ChangePasswordDTO("WrongPass@123", "NewPass@456");

        assertThrows(RuntimeException.class, () ->
                profileService.changePassword("rahul@college.edu", dto));
    }

    // Helper method to set private fields for testing
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}