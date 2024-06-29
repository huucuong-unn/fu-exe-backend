package com.exe01.backend.dto.request;

import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.student.CreateStudentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpWithStudentRequest  {
    private CreateAccountRequest createAccountRequest;
    private CreateStudentRequest studentRequest;

}
