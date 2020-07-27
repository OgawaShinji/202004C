package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
public class StoreController {

	@RequestMapping("/detail")
	public String showDetails() {
		return "store/details";
	}
}