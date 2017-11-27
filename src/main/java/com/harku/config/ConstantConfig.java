package com.harku.config;

import java.util.Locale;

public class ConstantConfig {
	
	//locale settings
	
	public static final Locale DEFAULT_LOCALE = Locale.US;
	
	public static final Locale[] SUPPORTED_LOCALES = {Locale.US, Locale.TAIWAN, Locale.JAPAN};
	
	//
	
	//routes
	
	public static final String ROOT_ROUTE = "/javaDbPrac";
	
	public static final String SIGN_IN_ROUTE = ROOT_ROUTE + "/sign_in/page";
	
	//
	
	//data format
	
	public static final int MAX_NAME_LENGTH = 40;
	
	public static final int MAX_ACCOUNT_LENGTH = 32;
	
	public static final int MAX_PASSWORD_LENGTH = 60;
	
	public static final int MAX_UPLOAD_SIZE = 10485760; //10MB
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	
	//
	
	//bcrypt parameters
	
	public static final int BCRYPT_WORKLOAD = 12;
	
	//
	
	//pagination settings
	
	public static final int ENTRY_PER_PAGE = 10;
	
	//
	
	//cookie settings
	
	public static final int EXPIRE_TIME_SEC = 604800;	//one week, 60*60*24*7
	
	public static final String LOGIN_TOKEN_COOKIE_NAME = "LOGIN_INFO";
	
	public static final String LOCALE_COOKIE_NAME = "LOCALE";
	
	//
	
	//database settings
	
	public static final String DB_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	
	public static final String DB_URL = "jdbc:mysql://127.0.0.1/db1?useSSL=false";
	
	public static final String DB_USERNAME = "root";
	
	public static final String DB_PASSWORD = "1234,Qwer";
	
	//
}
