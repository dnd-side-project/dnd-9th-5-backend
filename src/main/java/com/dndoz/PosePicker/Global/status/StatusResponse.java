package com.dndoz.PosePicker.Global.status;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(value = "모델 태그 정보")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외하고 보내기
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StatusResponse {
    @ApiModelProperty(value = "이미지 url",name = "entity", required = true)
    private String entity;
    @ApiModelProperty(value = "상태 메시지",name = "message", example = "OK", required = true)
    private String message;
    @ApiModelProperty(value = "상태 코드",name = "status", example = "200", required = true)
    private int status;
    @ApiModelProperty(value = "리다이렉트 url",name = "redirect", example = "null", required = true)
    private URI redirect;


    public StatusResponse(final StatusCode code, final String res) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.entity = res;
    }

    public StatusResponse(final StatusCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
    }

    public StatusResponse(final StatusCode code, final URI redirect) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.redirect = redirect;
    }

}
