package com.exe01.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class EmailDetailsEntity {
    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
    private String type;
    private String check;
}