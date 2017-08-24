package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.NewUser;
import com.qiwkreport.qiwk.etl.domain.OldUser;

public class UserProcessor implements ItemProcessor<OldUser, NewUser> {

	@Override
	public NewUser process(OldUser user) throws Exception {

		return new NewUser(
				user.getId(), 
				user.getUsername().toUpperCase(), 
				user.getPassword().toUpperCase(),
				user.getAge());

	}

}
