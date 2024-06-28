package com.exe01.backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    List<TopFiveCompany> topFiveCompany;



    public class TopFiveCompany{
        String companyName;
        Integer applicationCount;
    }

}
