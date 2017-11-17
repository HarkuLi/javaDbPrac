package com.harku.aspect;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice(basePackages = {"com.harku.controller"})
public class GlobalControllerAdvice {
	private static Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Object rootExceptionHandler(HttpServletRequest req, HttpServletResponse res, Exception e) {
		String className = e.getStackTrace()[0].getClassName();
		String methodName = e.getStackTrace()[0].getMethodName();
		String sourceName = className + "." + methodName;
		log.error("[{}] An error occurs: {}", sourceName, e.toString());
		
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("errMsg", "there are some errors in the server.");
		return msg;
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = NoHandlerFoundException.class)
	public String notFoundPageHandler(HttpServletRequest req, HttpServletResponse res, Exception e) {
		return "views/notFound";
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	@ResponseBody
	public Object missingParameterHandler(HttpServletRequest req, HttpServletResponse res, Exception e) {
		String className = e.getStackTrace()[0].getClassName();
		String methodName = e.getStackTrace()[0].getMethodName();
		String sourceName = className + "." + methodName;
		String errMsg = e.toString().split(": ")[1];
		
		log.info("[{}] Call API without required parameters: {}", sourceName, errMsg);
		
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("errMsg", errMsg);
		return msg;
	}
}
