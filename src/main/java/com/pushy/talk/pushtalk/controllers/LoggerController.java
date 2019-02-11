package com.pushy.talk.pushtalk.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggerController {

	Logger logger = LoggerFactory.getLogger(LoggerController.class);

	@RequestMapping("/")
	public void index() {
		logger.info("INFO");
	}
}