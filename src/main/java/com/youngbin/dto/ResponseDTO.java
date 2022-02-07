package com.youngbin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    private String resultCode;
    private Object response;
}
