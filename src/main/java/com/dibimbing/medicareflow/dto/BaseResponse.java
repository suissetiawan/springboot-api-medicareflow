package com.dibimbing.medicareflow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "responseStatus", "message", "data", "metadata" })
public class BaseResponse<T> {
    private int responseStatus;
    private String message;
    private T data;
    private PaginationMeta metadata;
}
