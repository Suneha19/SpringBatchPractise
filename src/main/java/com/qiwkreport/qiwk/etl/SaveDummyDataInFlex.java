package com.qiwkreport.qiwk.etl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qiwkreport.qiwk.etl.flex.domain.LCSColorFlex;
import com.qiwkreport.qiwk.etl.repository.LCSColorFlexRepository;

@Component
public class SaveDummyDataInFlex {
	
	@Autowired
	private LCSColorFlexRepository repository;
	

	public void saveLCSColorData() throws Exception {
		long count = 0L;
		List<LCSColorFlex> listofFlexItems=new ArrayList<>();
		//int id=0;
		for (int i = 980006; i <= 1000007; i++) {
			
			String stringDate="25-Oct-17";
			Date date=new SimpleDateFormat("dd-MMM-yy").parse(stringDate);
			LCSColorFlex lcsColorFlex = new LCSColorFlex();
			//this line errored out in second run...need to check later..
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
			//changing
			lcsColorFlex.setCOLORHEXIDECIMALVALUE("640000");
			//changing
			lcsColorFlex.setCOLORNAME("RED");
			lcsColorFlex.setCREATESTAMPA2(date);
			lcsColorFlex.setENTRYSETADHOCACL(
					"rO0ABXNyABV3dC5hY2Nlc3MuQWNsRW50cnlTZXQAAAAAAAAAAQQAAHhwlpARNxt6t6t1cgACW0Ks8xf4BghU4AIAAHhwAAAAPgACAAAAAQAOd3Qub3JnLldUR3JvdXAAAAABAAAAAAAABWgAAAAAAAAMAwAAAAAAAAAAAAAAAAAAAAAAAAAA");
			lcsColorFlex.setEVENTSET(null);
			lcsColorFlex.setFLEXTYPEIDPATH("\66162");
			lcsColorFlex.setIDA2A2(i);
			lcsColorFlex.setIDA2TYPEDEFINITIONREFERENCE(86402);
			lcsColorFlex.setIDA3A2FOLDERINGINFO(78142);
			lcsColorFlex.setIDA3A2OWNERSHIP(0);
			lcsColorFlex.setIDA3A2STATE(16542);
			lcsColorFlex.setIDA3A7(11);
			lcsColorFlex.setIDA3B2FOLDERINGINFO(80565);
			lcsColorFlex.setIDA3B8(0);
			lcsColorFlex.setIDA3C8(11);
			lcsColorFlex.setIDA3CONTAINERREFERENCE(78109);
			lcsColorFlex.setIDA3DOMAINREF(80509);
			//changing
			lcsColorFlex.setIDA3TEAMID(104384);
			//lcsColorFlex.setIDA3TEAMTEMPLATEID();
			lcsColorFlex.setINDEXERSINDEXERSET(null);
			lcsColorFlex.setINHERITEDDOMAIN(1);
			lcsColorFlex.setMARKFORDELETEA2(0);
			lcsColorFlex.setMODIFYSTAMPA2(date);
			//changing
			lcsColorFlex.setPTC_STR_1TYPEINFOLCSCOLOR("RED");
			lcsColorFlex.setSECURITYLABELS(null);
			lcsColorFlex.setSTATESTATE("INWORK");
			lcsColorFlex.setTEAMIDISNULL(0);
			//lcsColorFlex.setTEAMTEMPLATEIDISNULL();
			lcsColorFlex.setTHUMBNAIL(null);
			lcsColorFlex.setTYPEADMINISTRATIVELOCK(null);
			lcsColorFlex.setTYPEDISPLAY(null);
			lcsColorFlex.setUPDATECOUNTA2(1);
			lcsColorFlex.setUPDATESTAMPA2(date);
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
