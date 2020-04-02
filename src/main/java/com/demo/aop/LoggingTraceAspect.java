package com.demo.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LoggingTraceAspect {

	@Around("execution(* com.demo.service..*(..)) || "
			+ "execution(* com.demo.controller..*(..))")
	public Object controller(ProceedingJoinPoint joinPoint) throws Throwable {
			Signature signature = joinPoint.getSignature();

			loggingIn(signature, joinPoint.getArgs());
			try {
				Object o = joinPoint.proceed();

				loggingReturn(signature, o);

				return o;
			} finally {
				loggingOut(signature);
			}
	}

	private void loggingIn(Signature signature, Object o) {
		if ( Objects.nonNull(o) ) {
			try {
				log.info("TRACE_LOGGING: IN {}.{} ==> {}", signature.getDeclaringType().getSimpleName(), signature.getName(), new ObjectMapper().writeValueAsString(o));
			} catch (Exception e) {
				log.error("loggingIn(), exception-message=", e.getMessage());
			}
		}
	}

	private void loggingOut(Signature signature) {
		log.info("TRACE_LOGGING: OUT {}.{}", signature.getDeclaringType().getSimpleName(), signature.getName());
	}

	private void loggingReturn(Signature signature, Object o) {
		if ( Objects.nonNull(o) ) {
			try {
				log.info("TRACE_LOGGING: RETURN {}.{} ==> {}", signature.getDeclaringType().getSimpleName(), signature.getName(), new ObjectMapper().writeValueAsString(o));
			} catch (Exception e) {
				log.error("loggingReturn(), exception-message=", e.getMessage());
			}
		}
	}

}
