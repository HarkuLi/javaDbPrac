package com.harku.aspect;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogging {
	private static Logger log = LoggerFactory.getLogger(AspectLogging.class);
	
	@Pointcut("execution(* com.harku..*.*(..))")
	private void selectAll() {}
	
	@AfterReturning(pointcut = "execution(* com.harku.controller.sign.SignRestController.newUser(..))", returning = "retVal")
	private void newUserAdvice(JoinPoint joinPoint, Object retVal) {
		
		@SuppressWarnings("unchecked")
		ResponseEntity<Map<String, String>> retEntity = (ResponseEntity<Map<String, String>>) retVal;
		Map<String, String> retMap = retEntity.getBody();
		String account = retMap.get("account");
		String errMsg = retMap.get("errMsg");
		String sourceName = getSourceName(joinPoint);
		
		if(errMsg != null)
			log.info(String.format("[%s] Failed to create a new account [%s] becuase of: %s",
									sourceName, account, errMsg));
		else
			log.info(String.format("[%s] Create a new account: " + account, sourceName));
	}
	
	@AfterReturning(pointcut = "execution(* com.harku.controller.user.UserRestController.updateUser(..))", returning = "retVal")
	private void updateUserAdvice(JoinPoint joinPoint, Object retVal) {
		
		@SuppressWarnings("unchecked")
		ResponseEntity<Map<String, String>> retEntity = (ResponseEntity<Map<String, String>>) retVal;
		Map<String, String> retMap = retEntity.getBody();
		String id = retMap.get("id");
		String errMsg = retMap.get("errMsg");
		String sourceName = getSourceName(joinPoint);
		
		if(errMsg != null)
			log.info(String.format("[%s] Failed to update a account with id [%s] becuase of: %s",
									sourceName, id, errMsg));
		else
			log.info(String.format("[%s] Update a account whose id is: %s", sourceName, id));
	}
	
	@AfterReturning(pointcut = "execution(* com.harku.controller.user.UserRestController.deleteUser(..))", returning = "retVal")
	private void deleteUserAdvice(JoinPoint joinPoint, Object retVal) {
		
		@SuppressWarnings("unchecked")
		ResponseEntity<Map<String, String>> retEntity = (ResponseEntity<Map<String, String>>) retVal;
		Map<String, String> retMap = retEntity.getBody();
		String id = retMap.get("id");
		String errMsg = retMap.get("errMsg");
		String sourceName = getSourceName(joinPoint);
		
		if(errMsg != null)
			log.info(String.format("[%s] Failed to delete a account with id [%s] becuase of: %s",
									sourceName, id, errMsg));
		else
			log.info(String.format("[%s] Delete a account whose id is: %s", sourceName, id));
	}
	
	private String getSourceName(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		String wholeName = className + "." + methodName;
		
		return wholeName;
	}
}
