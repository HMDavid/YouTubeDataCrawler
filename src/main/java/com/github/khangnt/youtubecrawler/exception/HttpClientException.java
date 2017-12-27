package com.github.khangnt.youtubecrawler.exception;

import java.io.IOException;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class HttpClientException extends IOException {
    private int code;
    private String message;
    private String errorBody;

    public HttpClientException(int code, String message, String body) {
        super(code + " - " + message + "\n" + body);
        this.code = code;
        this.message = message;
        this.errorBody = body;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getErrorBody() {
        return errorBody;
    }
}
