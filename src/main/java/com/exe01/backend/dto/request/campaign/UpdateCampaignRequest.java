package com.exe01.backend.dto.request.campaign;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCampaignRequest extends BaseCampaignRequest {
    private String status;
}
