package com.qiwkreport.qiwk.etl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.qiwkreport.qiwk.etl.flex.domain.LCSColorFlex;
import com.qiwkreport.qiwk.etl.repository.LCSColorFlexRepository;

@Component
public class SaveDummyDataInFlex implements CommandLineRunner{
	
	@Autowired
	private LCSColorFlexRepository repository;
	
/*	public void saveLCSColorData(){
		
		List<LCSColorFlex> listofFlexItems=new ArrayList<>();
		
	//	for(long i=0; i<1;i++){
		LCSColorFlex lcsColorFlex=new LCSColorFlex();
		lcsColorFlex.setCOLORNAME("wsdsadadsa");
		listofFlexItems.add(lcsColorFlex);
	//	}
		repository.save(listofFlexItems);
	}*/

	@Override
	public void run(String... arg0) throws Exception {
		long count = 0L;
		List<LCSColorFlex> listofFlexItems=new ArrayList<>();
		
		for (long i = 0; i < 1; i++) {
			
			LCSColorFlex lcsColorFlex = new LCSColorFlex();
			lcsColorFlex.setADMINISTRATIVELOCKISNULL(0);
			lcsColorFlex.setATGATESTATE(0);
			lcsColorFlex.setBLOB$ENTRYSETADHOCACL(null);
			lcsColorFlex.setBRANCHIDA2TYPEDEFINITIONREFE(66162);
			lcsColorFlex.setCLASSNAMEA2A2("com.lcs.wc.color.LCSColor");
			lcsColorFlex.setCLASSNAMEKEYA2FOLDERINGINFO("wt.folder.Cabinet");
			lcsColorFlex.setCLASSNAMEKEYA2OWNERSHIP(null);
			lcsColorFlex.setCLASSNAMEKEYA2STATE("wt.lifecycle.LifeCycleTemplate");
			lcsColorFlex.setCLASSNAMEKEYA7("wt.org.WTUser");
			lcsColorFlex.setCLASSNAMEKEYB2FOLDERINGINFO("wt.folder.SubFolder");
			lcsColorFlex.setCLASSNAMEKEYB8(null);
			lcsColorFlex.setCLASSNAMEKEYC8("wt.org.WTUser");
			lcsColorFlex.setCLASSNAMEKEYCONTAINERREFEREN("wt.inf.library.WTLibrary");
			lcsColorFlex.setCLASSNAMEKEYDOMAINREF("wt.admin.AdministrativeDomain");
			lcsColorFlex.setCLASSNAMEKEYTEAMID("wt.team.Team");
			lcsColorFlex.setCLASSNAMEKEYTEAMTEMPLATEID(null);
			lcsColorFlex.setCOLORHEXIDECIMALVALUE("640000");
			lcsColorFlex.setCOLORNAME("RED");
			lcsColorFlex.setCREATESTAMPA2();

			listofFlexItems.add(lcsColorFlex);
			count++;
			if (count % 20000 == 0) {
				count = 0;
				repository.save(listofFlexItems);
				listofFlexItems = new ArrayList<>();
			}
		}
	}

}
