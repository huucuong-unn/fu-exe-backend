package com.exe01.backend.dto.request;

import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.company.BaseCompanyRequest;
import com.exe01.backend.dto.request.student.CreateStudentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpWithCompanyRequest {
    private CreateAccountRequest createAccountRequest;
    private BaseCompanyRequest createCompanyRequest;
}
