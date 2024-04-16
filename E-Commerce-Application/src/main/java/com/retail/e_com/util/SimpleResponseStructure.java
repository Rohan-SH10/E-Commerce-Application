package com.retail.e_com.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SimpleResponseStructure {
    private String message;
    private int statusCode;

    public SimpleResponseStructure setMessage(String message) {
        this.message = message;
        return this;
    }

    public SimpleResponseStructure setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
