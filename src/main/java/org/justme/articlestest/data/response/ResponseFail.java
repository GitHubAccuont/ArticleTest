package org.justme.articlestest.data.response;

import lombok.Getter;

@Getter
public class ResponseFail {

    private final String error;
    private final boolean success;

    public ResponseFail(String error) {
        this.success = false;
        this.error = error;
    }
}
