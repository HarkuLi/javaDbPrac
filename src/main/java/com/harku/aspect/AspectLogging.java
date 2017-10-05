package com.harku.aspect;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
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
	
	@AfterReturning(pointcut = "execution(* com.harku.controller.sign.SignRestServlet.NewUser(..))", returning = "retVal")
	private void NewUserAdvice(Object retVal) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> retMap = (Map<String, String>) retVal;
		String account = retMap.get("account");
		String errMsg = retMap.get("errMsg");
		
		if(errMsg != null)
			log.info("Failed to create a new account [" + account + "] becuase of: " + errMsg);
		else
			log.info("Create a new account: " + account);
	}
	
	@AfterReturning(pointcut = "execution(* com.harku.controller.user.UserRestServlet.UpdateUser(..))", returning = "retVal")
	private void UpdateUserAdvice(Object retVal) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> retMap = (Map<String, String>) retVal;
		String id = retMap.get("id");
		String errMsg = retMap.get("errMsg");
		
		if(errMsg != null)
			log.info("Failed to update a account with id [" + id + "] becuase of: " + errMsg);
		else
			log.info("Update a account whose id is: " + id);
	}
	
	@AfterReturning(pointcut = "execution(* com.harku.controller.user.UserRestServlet.DeleteUser(..))", returning = "retVal")
	private void DeleteUserAdvice(Object retVal) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> retMap = (Map<String, String>) retVal;
		String id = retMap.get("id");
		
		log.info("Delete a account whose id is: " + id);
	}
	
	@AfterThrowing(pointcut = "selectAll()", throwing = "e")
	public void AllThrowingAdvice(JoinPoint joinPoint, Exception e) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		String wholeName = className + "." + methodName;
		
		log.error("Exception in [" + wholeName + "]: " + e.toString());
	}
}
