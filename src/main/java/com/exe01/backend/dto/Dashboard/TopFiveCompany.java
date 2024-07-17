package com.exe01.backend.dto.Dashboard;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TopFiveCompany {
    String companyName;
    Long applicationCount;
}
