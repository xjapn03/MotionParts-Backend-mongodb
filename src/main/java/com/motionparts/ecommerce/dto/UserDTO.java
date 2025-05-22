package com.motionparts.ecommerce.dto;

import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {
    
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    private String username;

    @Email(message = "Debe ser un correo electrónico válido.")
    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    private String password;

    @NotNull(message = "Los roles son obligatorios.")
    private List<RoleDTO> roles; // Ahora los roles son obligatorios

    // Getters y Setters
    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }

    public List<RoleDTO> getRoles() { 
        return roles; 
    }
    public void setRoles(List<RoleDTO> roles) { 
        this.roles = roles; 
    }
}