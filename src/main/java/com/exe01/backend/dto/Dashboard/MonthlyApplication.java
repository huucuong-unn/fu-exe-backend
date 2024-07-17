package com.exe01.backend.dto.Dashboard;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyApplication {
    Integer month;
    Long applicationCount;
}
