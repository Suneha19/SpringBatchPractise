/**
 * 
 */
package com.qiwkreport.qiwk.etl.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Abhilash
 * 
 * This class is developed to disable default security configuration in spring boot applications
 *
 */

@Configuration
public class QiwkETLSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity.authorizeRequests().antMatchers("/").permitAll();
	}
}
