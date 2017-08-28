package com.qiwkreport.qiwk.etl.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateItemWriter;

import com.qiwkreport.qiwk.etl.domain.NewUser;

public class HibernateUserItemWriter implements ItemWriter<NewUser> {

	@Override
	public void write(List<? extends NewUser> items) throws Exception {
		HibernateItemWriter<NewUser> hibernateItemWriter=new HibernateItemWriter<>();
		hibernateItemWriter.write(items);
	}

}
