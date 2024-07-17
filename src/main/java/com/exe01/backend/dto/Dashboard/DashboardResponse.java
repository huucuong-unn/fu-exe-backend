package com.exe01.backend.dto.Dashboard;


import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

   private List<TopFiveCompany> topFiveCompany;

    private List<MonthlyApplication> applicationByMonth;
    private List<MonthlyRevenue> monthlyRevenue;

}
