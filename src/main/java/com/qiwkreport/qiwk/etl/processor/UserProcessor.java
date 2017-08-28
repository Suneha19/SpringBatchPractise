package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.NewUser;
import com.qiwkreport.qiwk.etl.domain.Olduser;

public class UserProcessor implements ItemProcessor<Olduser, NewUser> {

	@Override
	public NewUser process(Olduser user) throws Exception {

		return new NewUser(
				user.getId(), 
				user.getUsername().toUpperCase(), 
				user.getPassword().toUpperCase(),
				user.getAge());

	}

}
