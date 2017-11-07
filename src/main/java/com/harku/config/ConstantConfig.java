package com.harku.config;

import java.util.Locale;

public class ConstantConfig {
	public static final Locale DEFAULT_LOCALE = Locale.US;
	
	public static final String LOCALE_COOKIE_NAME = "LOCALE";
	
	public static final Locale[] SUPPORTED_LOCALES = {Locale.US, Locale.TAIWAN, Locale.JAPAN};
}