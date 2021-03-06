package com.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultFinallyHandler implements FinallyHandler {
	@Override
	public void handle() {
		log.info("DefaultFinallyHandler");
	}

}
