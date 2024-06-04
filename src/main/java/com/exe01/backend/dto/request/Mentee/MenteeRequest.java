package com.exe01.backend.dto.request.Mentee;


import com.exe01.backend.entity.MentorApply;
import com.exe01.backend.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenteeRequest {

    private UUID studentId;

}
