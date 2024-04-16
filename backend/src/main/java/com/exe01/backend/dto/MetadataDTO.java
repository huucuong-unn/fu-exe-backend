package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetadataDTO {
    private boolean hasNextPage;
    private boolean hasPrevPage;
    private int limit;
    private int total;
    private int page;
}
