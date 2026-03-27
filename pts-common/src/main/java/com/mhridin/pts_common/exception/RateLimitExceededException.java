package com.mhridin.pts_common.exception;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String s) {
        super(s);
    }
}
