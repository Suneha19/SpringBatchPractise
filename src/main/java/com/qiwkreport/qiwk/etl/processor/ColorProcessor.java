package com.qiwkreport.qiwk.etl.processor;

import org.springframework.batch.item.ItemProcessor;

import com.lcs.wc.color.LCSColor;
import com.qiwkreport.qiwk.etl.domain.QiwkColor;

public class ColorProcessor implements ItemProcessor<LCSColor, QiwkColor> {
	

	@Override
	public QiwkColor process(LCSColor lcsColor) {
		QiwkColor color=new QiwkColor();
		//lcsColor.
		
		return color;
	}

}
