package com.dndoz.PosePicker.Global.status;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusCode {

        OK(200, "OK"),
        NO_CONTENT(204, "No Content");
//        SEE_OTHER(303, "See Other"); // redirect


    private final String message;
        private int status;

        StatusCode(final int status, final String message) {
            this.status = status;
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

        public int getStatus() {
            return status;
        }


}
