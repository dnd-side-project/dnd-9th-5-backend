package com.dndoz.PosePicker.Global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    BAD_REQUEST(400, "C007", " Bad Request"),
    UNSUPPORTED_MEDIA_TYPE(415, "C008", "UNSUPPORTED_MEDIA_TYPE"),
    FILE_SIZE_EXCEED(416, "C009", "REQUESTED_RANGE_NOT_SATISFIABLE"),
    EXPECTATION_FAILED(417, "C010", "EXPECTATION_FAILED"),
	//JWT 토큰 ErrorCode
	UNSUPPORTED_JWT_TOKEN(401,"C011","UnsupportedJwtException 지원되지 않는 토큰"),
	MALFORMED_JWT_TOKEN(401,"C012","MalformedJwtException 잘못된 jwt 구조"),
	EXPIRED_JWT_TOKEN(401,"C013","ExpiredJwtException 토큰 만료"),
	UNAUTHORIZED_JWT_TOKEN(401,"C014","Unauthorized"),

	BOOKMARK_BAD_REQUEST(400,"C015","북마크 API 잘못된 요청")
	;


    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
