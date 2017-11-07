package com.harku.test.controller.publicAPI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.harku.config.WebConfig;
import com.harku.controller.publicAPI.PublicRestController;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestPublicSetLanguage {
	private MockMvc mockMvc;
	private String language;
	private String country;
	
	@Spy
	private LocaleResolver localeResolver = getLocaleResolver();
	
	@InjectMocks
	private PublicRestController PRController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(PRController)
				.build();
		
		setTestData();
	}
	
	@Test
	public void basic() throws Exception {
		String locale = language + "_" + country;
		String localeCookieName = ((CookieLocaleResolver)localeResolver).getCookieName();
		
		mockMvc.perform(post("/public/set_language")
                       .param("language", locale))
	           .andExpect(cookie().value(localeCookieName, locale));
		
	}
	
	@Test
	public void wrongFromatLanguage() throws Exception {
		String locale = "wrong_format_language";
		
		mockMvc.perform(post("/public/set_language")
						.param("language", locale))
				.andExpect(status().isBadRequest());
	}
	
	private void setTestData() {
		language = RandomData.genStr(2, 2).toLowerCase();
		country = RandomData.genStr(2, 2).toUpperCase();
	}
	
	private LocaleResolver getLocaleResolver() {
		WebConfig webConfig = new WebConfig();
		return webConfig.localeResolver();
	}
}
