package com.tdoer.auth.service;

import com.tdoer.springboot.error.ErrorCodeException;

public class UnknownClientCategoryException extends ErrorCodeException {
    public UnknownClientCategoryException(int errorCode) {
        super(errorCode);
    }

    public UnknownClientCategoryException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public UnknownClientCategoryException(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public UnknownClientCategoryException(int errorCode, Object... messageFormatArgs) {
        super(errorCode, messageFormatArgs);
    }

    public UnknownClientCategoryException(int errorCode, Throwable cause, Object... messageFormatArgs) {
        super(errorCode, cause, messageFormatArgs);
    }
}
