package com.exe01.backend.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagingModel<T> {

    private int page;
    private int totalPage;
    private int totalCount;
    private int limit;
    private List<T> listResult = new ArrayList<>();


}
