package com.assignment2.queuingmechanism.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class CustomGenericException extends RuntimeException {
    String errorCode;
    String message;
}
