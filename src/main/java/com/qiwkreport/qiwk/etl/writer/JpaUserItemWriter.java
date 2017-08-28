package com.qiwkreport.qiwk.etl.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.qiwkreport.qiwk.etl.domain.NewUser;

public class JpaUserItemWriter implements ItemWriter<NewUser>{

	@Override
	public void write(List<? extends NewUser> items) throws Exception {
		new JpaUserItemWriter().write(items);
		
	}

}
