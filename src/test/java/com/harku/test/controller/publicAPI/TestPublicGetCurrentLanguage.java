package com.harku.test.controller.publicAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Locale;

import javax.servlet.http.Cookie;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.LocaleResolver;

import com.harku.config.ConstantConfig;
import com.harku.config.WebConfig;
import com.harku.controller.publicAPI.PublicRestController;

@RunWith(MockitoJUnitRunner.class)
public class TestPublicGetCurrentLanguage {
	private MockMvc mockMvc;
	
	@Spy
	private LocaleResolver localeResolver = getLocaleResolver();
	
	@InjectMocks
	private PublicRestController PRController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(PRController)
				.build();
	}
	
	@Test
	public void noLocaleCookie() throws Exception {
		MvcResult result = mockMvc.perform(get("/public/get_current_language"))
				.andExpect(status().isOk())
				.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		//expect default locale
		assertEquals(ConstantConfig.DEFAULT_LOCALE.toString(), res.get("language"));
	}
	
	@Test
	public void notDefaultLocale() throws Exception {
		Locale locale = Locale.TAIWAN;
		Cookie cookie = new Cookie("LOCALE", locale.toString());
		
		MvcResult result = mockMvc.perform(get("/public/get_current_language")
											.cookie(cookie))
				.andExpect(status().isOk())
				.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		
		//the tested locale is a supported locale
		assertTrue(Arrays.asList(ConstantConfig.SUPPORTED_LOCALES).contains(locale));
		assertEquals(locale.toString(), res.get("language"));
	}
	
	@Test
	public void notSupportedLocale() throws Exception {
		Locale notSupportedLocale = new Locale("not", "supported", "locale");
		Cookie cookie = new Cookie("LOCALE", notSupportedLocale.toString());
		
		MvcResult result = mockMvc.perform(get("/public/get_current_language")
											.cookie(cookie))
				.andExpect(status().isOk())
				.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		
		//the tested locale isn't a supported locale
		assertTrue(!Arrays.asList(ConstantConfig.SUPPORTED_LOCALES).contains(notSupportedLocale));
		//expect default locale
		assertEquals(ConstantConfig.DEFAULT_LOCALE.toString(), res.get("language"));
	}
	
	private LocaleResolver getLocaleResolver() {
		WebConfig webConfig = new WebConfig();
		return webConfig.localeResolver();
	}
}
