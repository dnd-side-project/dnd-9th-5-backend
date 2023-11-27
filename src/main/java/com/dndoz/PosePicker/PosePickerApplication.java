package com.dndoz.PosePicker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PosePickerApplication {

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}
    public static void main(String[] args) {
        SpringApplication.run(PosePickerApplication.class, args);
    }
}
