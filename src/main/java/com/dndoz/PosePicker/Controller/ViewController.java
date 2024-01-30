package com.dndoz.PosePicker.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@RequestMapping("/admin")
public class ViewController {
	@GetMapping("/forms")
	public String formsPage() {
		return "forms";
	}

	@GetMapping("/tables")
	public String tablesPage() {
		return "tables";
	}

	@GetMapping("/details")
	public String detailsPage() {
		return "details";
	}

	@GetMapping("/talks")
	public String talksPage() {
		return "talks";
	}
}
