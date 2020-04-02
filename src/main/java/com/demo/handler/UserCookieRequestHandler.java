package com.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCookieRequestHandler implements RequestHandler {

	@Override
	public void handle(Object[] requestArgs) {
		log.info("NEW COOKIE");
	}
}
