package com.exe01.backend.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateRoleRequest {
    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    @Size(max = 30, message = "Name must be less than or equal to 30 characters")
    private String name;

    @NotBlank(message = "Must not be blank")
    @NotNull(message = "Must not be null")
    @Size(max = 30, message = "Description must be less than or equal to 100 characters")
    private String description;
}
