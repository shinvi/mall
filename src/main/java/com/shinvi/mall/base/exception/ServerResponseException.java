package com.shinvi.mall.base.exception;

import com.shinvi.mall.common.ResponseCode;

public class ServerResponseException extends RuntimeException {
    private final ResponseCode responseCode;
    private ResponseCode extrasCode;

    public ServerResponseException(ResponseCode responseCode) {
        super(responseCode.getDesc());
        this.responseCode = responseCode;
    }

    public ServerResponseException(String message) {
        super(message);
        this.responseCode = ResponseCode.ERROR.setDesc(message);
    }

    public ResponseCode getExtrasCode() {
        return extrasCode;
    }

    public ServerResponseException setExtrasCode(ResponseCode extrasCode) {
        this.extrasCode = extrasCode;
        return this;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
