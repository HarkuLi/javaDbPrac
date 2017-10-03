package com.harku.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogging {
	private static Logger log = LoggerFactory.getLogger(AspectLogging.class);
	
	@Pointcut("execution(* com.harku..*.*(..))")
	private void selectAll() {}
	
	@Before("selectAll()")
	public void beforeAdvice(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		String wholeName = className + "." + methodName;
		log.info("call function: " + wholeName);
	}
}
