package com.demo.aop;

import com.demo.handler.FinallyHandler;
import com.demo.handler.RequestHandler;
import com.demo.handler.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class ControllerHandlerAspect {
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) "
			+ "|| @annotation(org.springframework.web.bind.annotation.GetMapping) "
			+ "|| @annotation(org.springframework.web.bind.annotation.PostMapping) "
			)
	public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			Object[] requestArgs = joinPoint.getArgs();
			handleRequestArgs(requestArgs);
			Object responseObject = joinPoint.proceed(requestArgs);
			handleResponseObject(responseObject);
			return responseObject;
		} finally {
			handleFinally();
		}
	}

	private void handleRequestArgs(Object[] requestArgs) {
		if ( CollectionUtils.isEmpty(requestHandlers) ) {
			return;
		}
		requestHandlers.stream().forEach(handler -> handler.handle(requestArgs));
	}

	private void handleResponseObject(Object responseObject) {
		if ( CollectionUtils.isEmpty(responseHandlers) ) {
			return;
		}
		responseHandlers.stream().forEach(handler -> handler.handle(responseObject));
	}

	private void handleFinally() {
		if ( CollectionUtils.isEmpty(finallyHandlers) ) {
			return;
		}
		finallyHandlers.stream().forEach(handler -> {
			try {
				handler.handle();
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
			}
		});
	}

	private List<RequestHandler> requestHandlers;
	private List<ResponseHandler> responseHandlers;
	private List<FinallyHandler> finallyHandlers;

	@Autowired
    ApplicationContext applicationContext;
	@PostConstruct
	public void init() {
		log.info("Initializing ControllerAspect: {}", this);
		this.requestHandlers = initialize(RequestHandler.class);
		this.responseHandlers = initialize(ResponseHandler.class);
		this.finallyHandlers = initialize(FinallyHandler.class);
	}

	private <T> List<T> initialize(Class<T> cls) {
		Map<String, T> finHandlers = applicationContext.getBeansOfType(cls);
		if ( ! CollectionUtils.isEmpty(finHandlers) ) {
			List<T> list = new ArrayList<>();
			finHandlers.values().forEach(handler -> {
				list.add(handler);
				log.info("Initializing {}: {}", cls.getSimpleName(), handler);
			});
			return Collections.unmodifiableList(list);
		}
		return null;
	}
}
