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
				user.getAge(),
				user.getEmailId().toUpperCase(),
				user.getFirstName().toUpperCase(),
				user.getLastName().toUpperCase(),
				user.getMobileNumber().toUpperCase(),
				user.getSalary(),
				user.isAbove18());

	}

}
