package com.exe01.backend.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest extends BaseAccountRequest{
    @NotBlank(message = "This field must not be blank")
    @NotNull(message = "This field must not be null")
    private String status;
}
