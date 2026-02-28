package com.dibimbing.medicareflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationMeta {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
