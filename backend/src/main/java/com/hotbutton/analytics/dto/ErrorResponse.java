package com.hotbutton.analytics.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    private String code;

    private String requestId;

    private Long timestamp;
}