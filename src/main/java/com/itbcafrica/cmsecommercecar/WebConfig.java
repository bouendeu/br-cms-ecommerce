package com.itbcafrica.cmsecommercecar;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	/*
	 * @Override public void addViewControllers(ViewControllerRegistry registry) {
	 * 
	 * registry.addViewController("/").setViewName("home");
	 * 
	 * }
	 */

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/media/**").addResourceLocations(
				"files:/C:/Users/OEM/Desktop/cmsecomerce/cmsecommercecar/src/main/resources/static/media/",
				"classpath:/static/media/");
	}

}