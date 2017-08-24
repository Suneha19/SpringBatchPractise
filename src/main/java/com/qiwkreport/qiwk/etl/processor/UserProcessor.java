package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.qiwkreport.qiwk.etl.domain.NewUser;
import com.qiwkreport.qiwk.etl.domain.User;

public class UserProcessor implements ItemProcessor<User, NewUser> {

	@Override
	public NewUser process(User user) throws Exception {

		return new NewUser(user.getId(), user.getUsername().toUpperCase(), user.getPassword().toUpperCase(),
				user.getAge());

	}

}
