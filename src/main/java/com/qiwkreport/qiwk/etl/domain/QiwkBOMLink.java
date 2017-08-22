package com.qiwkreport.qiwk.etl.domain;

import java.sql.Timestamp;
import java.util.Date;

public class QiwkBOMLink implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int primaryKey;
	private int BRANCHIDITERATIONINFO;

	private int MATERIALCOLORID;
	private int COLORID;
	private int MATERIALID;
	private int BOMID;
	private int SUPPLIERID;
	private int SOURCEID;
	private int SKUID;
	private int DESTINATIONID;
	private int SEASONREVID;

	private String DIMENSIONNAME;
	private String DIMENSIONID;

	private int DROPPED;

	private Date CREATESTAMPA2;
	private Date UPDATESTAMPA2;
	private String FLEXTYPEIDPATH;
	private int UPDATECOUNTA2;

	private int MARKEDFORDELETEA2;
	private Timestamp LOADERCREATESTAMP;
	private Timestamp LOADERUPDATESTAMP;
	private int BRANCHID;
	private int FLEXTYPEID;

	private String ATT1;
	private String ATT2;
	private String ATT3;
	private String ATT4;

	private String ATT5;
	private String ATT6;
	private String ATT7;
	private String ATT8;
	private String ATT9;
	private String ATT10;
	private String ATT11;
	private String ATT12;
	private String ATT13;
	private String ATT14;
	private String ATT15;
	private String ATT16;
	private String ATT17;
	private String ATT18;
	private String ATT19;
	private String ATT20;
	private String ATT21;
	private String ATT22;
	private String ATT23;
	private String ATT24;
	private String ATT25;
	private String ATT26;
	private String ATT27;
	private String ATT28;
	private String ATT29;
	private String ATT30;

	private Date DATE1;
	private Date DATE2;
	private Date DATE3;
	private Date DATE4;
	private Date DATE5;
	private Date DATE6;
	private Date DATE7;
	private Date DATE8;
	private Date DATE9;
	private Date DATE10;

	private double NUM1;
	private double NUM2;
	private double NUM3;
	private double NUM4;
	private double NUM5;
	private double NUM6;
	private double NUM7;
	private double NUM8;
	private double NUM9;
	private double NUM10;
	private double NUM11;
	private double NUM12;
	private double NUM13;
	private double NUM14;
	private double NUM15;
	private double NUM16;
	private double NUM17;
	private double NUM18;
	private double NUM19;
	private double NUM20;

	public int getDROPPED() {
		return DROPPED;
	}

	public void setDROPPED(int dROPPED) {
		DROPPED = dROPPED;
	}

	public int getMATERIALCOLORID() {
		return MATERIALCOLORID;
	}

	public void setMATERIALCOLORID(int mATERIALCOLORID) {
		MATERIALCOLORID = mATERIALCOLORID;
	}

	public int getCOLORID() {
		return COLORID;
	}

	public void setCOLORID(int cOLORID) {
		COLORID = cOLORID;
	}

	public int getMATERIALID() {
		return MATERIALID;
	}

	public void setMATERIALID(int mATERIALID) {
		MATERIALID = mATERIALID;
	}

	public int getBOMID() {
		return BOMID;
	}

	public void setBOMID(int bOMID) {
		BOMID = bOMID;
	}

	public int getSUPPLIERID() {
		return SUPPLIERID;
	}

	public void setSUPPLIERID(int sUPPLIERID) {
		SUPPLIERID = sUPPLIERID;
	}

	public int getSKUID() {
		return SKUID;
	}

	public void setSKUID(int sKUID) {
		SKUID = sKUID;
	}

	public int getSOURCEID() {
		return SOURCEID;
	}

	public void setSOURCEID(int sOURCEID) {
		SOURCEID = sOURCEID;
	}

	public int getDESTINATIONID() {
		return DESTINATIONID;
	}

	public void setDESTINATIONID(int dESTINATIONID) {
		DESTINATIONID = dESTINATIONID;
	}

	public int getBRANCHID() {
		return BRANCHID;
	}

	public void setBRANCHID(int bRANCHID) {
		BRANCHID = bRANCHID;
	}

	public int getFLEXTYPEID() {
		return FLEXTYPEID;
	}

	public void setFLEXTYPEID(int fLEXTYPEID) {
		FLEXTYPEID = fLEXTYPEID;
	}

	public Timestamp getLOADERCREATESTAMP() {
		return LOADERCREATESTAMP;
	}

	public void setLOADERCREATESTAMP(Timestamp lOADERCREATESTAMP) {
		LOADERCREATESTAMP = lOADERCREATESTAMP;
	}

	public Timestamp getLOADERUPDATESTAMP() {
		return LOADERUPDATESTAMP;
	}

	public void setLOADERUPDATESTAMP(Timestamp lOADERUPDATESTAMP) {
		LOADERUPDATESTAMP = lOADERUPDATESTAMP;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getBRANCHIDITERATIONINFO() {
		return BRANCHIDITERATIONINFO;
	}

	public void setBRANCHIDITERATIONINFO(int bRANCHIDITERATIONINFO) {
		BRANCHIDITERATIONINFO = bRANCHIDITERATIONINFO;
	}

	public Date getCREATESTAMPA2() {
		return CREATESTAMPA2;
	}

	public void setCREATESTAMPA2(Date cREATESTAMPA2) {
		CREATESTAMPA2 = cREATESTAMPA2;
	}

	public Date getUPDATESTAMPA2() {
		return UPDATESTAMPA2;
	}

	public void setUPDATESTAMPA2(Date uPDATESTAMPA2) {
		UPDATESTAMPA2 = uPDATESTAMPA2;
	}

	public String getFLEXTYPEIDPATH() {
		return FLEXTYPEIDPATH;
	}

	public void setFLEXTYPEIDPATH(String fLEXTYPEIDPATH) {
		FLEXTYPEIDPATH = fLEXTYPEIDPATH;
	}

	public int getUPDATECOUNTA2() {
		return UPDATECOUNTA2;
	}

	public void setUPDATECOUNTA2(int uPDATECOUNTA2) {
		UPDATECOUNTA2 = uPDATECOUNTA2;
	}

	public int getMARKEDFORDELETEA2() {
		return MARKEDFORDELETEA2;
	}

	public void setMARKEDFORDELETEA2(int mARKEDFORDELETEA2) {
		MARKEDFORDELETEA2 = mARKEDFORDELETEA2;
	}

	public String getDIMENSIONNAME() {
		return DIMENSIONNAME;
	}

	public void setDIMENSIONNAME(String dIMENSIONNAME) {
		DIMENSIONNAME = dIMENSIONNAME;
	}

	public String getDIMENSIONID() {
		return DIMENSIONID;
	}

	public void setDIMENSIONID(String dIMENSIONID) {
		DIMENSIONID = dIMENSIONID;
	}

	public int getSEASONREVID() {
		return SEASONREVID;
	}

	public void setSEASONREVID(int sEASONREVID) {
		SEASONREVID = sEASONREVID;
	}

	public String getATT1() {
		return ATT1;
	}

	public void setATT1(String aTT1) {
		ATT1 = aTT1;
	}

	public String getATT2() {
		return ATT2;
	}

	public void setATT2(String aTT2) {
		ATT2 = aTT2;
	}

	public String getATT3() {
		return ATT3;
	}

	public void setATT3(String aTT3) {
		ATT3 = aTT3;
	}

	public String getATT4() {
		return ATT4;
	}

	public void setATT4(String aTT4) {
		ATT4 = aTT4;
	}

	public String getATT5() {
		return ATT5;
	}

	public void setATT5(String aTT5) {
		ATT5 = aTT5;
	}

	public String getATT6() {
		return ATT6;
	}

	public void setATT6(String aTT6) {
		ATT6 = aTT6;
	}

	public String getATT7() {
		return ATT7;
	}

	public void setATT7(String aTT7) {
		ATT7 = aTT7;
	}

	public String getATT8() {
		return ATT8;
	}

	public void setATT8(String aTT8) {
		ATT8 = aTT8;
	}

	public String getATT9() {
		return ATT9;
	}

	public void setATT9(String aTT9) {
		ATT9 = aTT9;
	}

	public String getATT10() {
		return ATT10;
	}

	public void setATT10(String aTT10) {
		ATT10 = aTT10;
	}

	public String getATT11() {
		return ATT11;
	}

	public void setATT11(String aTT11) {
		ATT11 = aTT11;
	}

	public String getATT12() {
		return ATT12;
	}

	public void setATT12(String aTT12) {
		ATT12 = aTT12;
	}

	public String getATT13() {
		return ATT13;
	}

	public void setATT13(String aTT13) {
		ATT13 = aTT13;
	}

	public String getATT14() {
		return ATT14;
	}

	public void setATT14(String aTT14) {
		ATT14 = aTT14;
	}

	public String getATT15() {
		return ATT15;
	}

	public void setATT15(String aTT15) {
		ATT15 = aTT15;
	}

	public String getATT16() {
		return ATT16;
	}

	public void setATT16(String aTT16) {
		ATT16 = aTT16;
	}

	public String getATT17() {
		return ATT17;
	}

	public void setATT17(String aTT17) {
		ATT17 = aTT17;
	}

	public String getATT18() {
		return ATT18;
	}

	public void setATT18(String aTT18) {
		ATT18 = aTT18;
	}

	public String getATT19() {
		return ATT19;
	}

	public void setATT19(String aTT19) {
		ATT19 = aTT19;
	}

	public String getATT20() {
		return ATT20;
	}

	public void setATT20(String aTT20) {
		ATT20 = aTT20;
	}

	public String getATT21() {
		return ATT21;
	}

	public void setATT21(String aTT21) {
		ATT21 = aTT21;
	}

	public String getATT22() {
		return ATT22;
	}

	public void setATT22(String aTT22) {
		ATT22 = aTT22;
	}

	public String getATT23() {
		return ATT23;
	}

	public void setATT23(String aTT23) {
		ATT23 = aTT23;
	}

	public String getATT24() {
		return ATT24;
	}

	public void setATT24(String aTT24) {
		ATT24 = aTT24;
	}

	public String getATT25() {
		return ATT25;
	}

	public void setATT25(String aTT25) {
		ATT25 = aTT25;
	}

	public String getATT26() {
		return ATT26;
	}

	public void setATT26(String aTT26) {
		ATT26 = aTT26;
	}

	public String getATT27() {
		return ATT27;
	}

	public void setATT27(String aTT27) {
		ATT27 = aTT27;
	}

	public String getATT28() {
		return ATT28;
	}

	public void setATT28(String aTT28) {
		ATT28 = aTT28;
	}

	public String getATT29() {
		return ATT29;
	}

	public void setATT29(String aTT29) {
		ATT29 = aTT29;
	}

	public String getATT30() {
		return ATT30;
	}

	public void setATT30(String aTT30) {
		ATT30 = aTT30;
	}

	public Date getDATE1() {
		return DATE1;
	}

	public void setDATE1(Date dATE1) {
		DATE1 = dATE1;
	}

	public Date getDATE2() {
		return DATE2;
	}

	public void setDATE2(Date dATE2) {
		DATE2 = dATE2;
	}

	public Date getDATE3() {
		return DATE3;
	}

	public void setDATE3(Date dATE3) {
		DATE3 = dATE3;
	}

	public Date getDATE4() {
		return DATE4;
	}

	public void setDATE4(Date dATE4) {
		DATE4 = dATE4;
	}

	public Date getDATE5() {
		return DATE5;
	}

	public void setDATE5(Date dATE5) {
		DATE5 = dATE5;
	}

	public Date getDATE6() {
		return DATE6;
	}

	public void setDATE6(Date dATE6) {
		DATE6 = dATE6;
	}

	public Date getDATE7() {
		return DATE7;
	}

	public void setDATE7(Date dATE7) {
		DATE7 = dATE7;
	}

	public Date getDATE8() {
		return DATE8;
	}

	public void setDATE8(Date dATE8) {
		DATE8 = dATE8;
	}

	public Date getDATE9() {
		return DATE9;
	}

	public void setDATE9(Date dATE9) {
		DATE9 = dATE9;
	}

	public Date getDATE10() {
		return DATE10;
	}

	public void setDATE10(Date dATE10) {
		DATE10 = dATE10;
	}

	public double getNUM1() {
		return NUM1;
	}

	public void setNUM1(double nUM1) {
		NUM1 = nUM1;
	}

	public double getNUM2() {
		return NUM2;
	}

	public void setNUM2(double nUM2) {
		NUM2 = nUM2;
	}

	public double getNUM3() {
		return NUM3;
	}

	public void setNUM3(double nUM3) {
		NUM3 = nUM3;
	}

	public double getNUM4() {
		return NUM4;
	}

	public void setNUM4(double nUM4) {
		NUM4 = nUM4;
	}

	public double getNUM5() {
		return NUM5;
	}

	public void setNUM5(double nUM5) {
		NUM5 = nUM5;
	}

	public double getNUM6() {
		return NUM6;
	}

	public void setNUM6(double nUM6) {
		NUM6 = nUM6;
	}

	public double getNUM7() {
		return NUM7;
	}

	public void setNUM7(double nUM7) {
		NUM7 = nUM7;
	}

	public double getNUM8() {
		return NUM8;
	}

	public void setNUM8(double nUM8) {
		NUM8 = nUM8;
	}

	public double getNUM9() {
		return NUM9;
	}

	public void setNUM9(double nUM9) {
		NUM9 = nUM9;
	}

	public double getNUM10() {
		return NUM10;
	}

	public void setNUM10(double nUM10) {
		NUM10 = nUM10;
	}

	public double getNUM11() {
		return NUM11;
	}

	public void setNUM11(double nUM11) {
		NUM11 = nUM11;
	}

	public double getNUM12() {
		return NUM12;
	}

	public void setNUM12(double nUM12) {
		NUM12 = nUM12;
	}

	public double getNUM13() {
		return NUM13;
	}

	public void setNUM13(double nUM13) {
		NUM13 = nUM13;
	}

	public double getNUM14() {
		return NUM14;
	}

	public void setNUM14(double nUM14) {
		NUM14 = nUM14;
	}

	public double getNUM15() {
		return NUM15;
	}

	public void setNUM15(double nUM15) {
		NUM15 = nUM15;
	}

	public double getNUM16() {
		return NUM16;
	}

	public void setNUM16(double nUM16) {
		NUM16 = nUM16;
	}

	public double getNUM17() {
		return NUM17;
	}

	public void setNUM17(double nUM17) {
		NUM17 = nUM17;
	}

	public double getNUM18() {
		return NUM18;
	}

	public void setNUM18(double nUM18) {
		NUM18 = nUM18;
	}

	public double getNUM19() {
		return NUM19;
	}

	public void setNUM19(double nUM19) {
		NUM19 = nUM19;
	}

	public double getNUM20() {
		return NUM20;
	}

	public void setNUM20(double nUM20) {
		NUM20 = nUM20;
	}

	@Override
	public String toString() {
		return "QiwkBOMLink [primaryKey=" + primaryKey + ", BRANCHIDITERATIONINFO=" + BRANCHIDITERATIONINFO
				+ ", MATERIALCOLORID=" + MATERIALCOLORID + ", COLORID=" + COLORID + ", MATERIALID=" + MATERIALID
				+ ", BOMID=" + BOMID + ", SUPPLIERID=" + SUPPLIERID + ", SOURCEID=" + SOURCEID + ", SKUID=" + SKUID
				+ ", DESTINATIONID=" + DESTINATIONID + ", SEASONREVID=" + SEASONREVID + ", DIMENSIONNAME="
				+ DIMENSIONNAME + ", DIMENSIONID=" + DIMENSIONID + ", DROPPED=" + DROPPED + ", CREATESTAMPA2="
				+ CREATESTAMPA2 + ", UPDATESTAMPA2=" + UPDATESTAMPA2 + ", FLEXTYPEIDPATH=" + FLEXTYPEIDPATH
				+ ", UPDATECOUNTA2=" + UPDATECOUNTA2 + ", MARKEDFORDELETEA2=" + MARKEDFORDELETEA2
				+ ", LOADERCREATESTAMP=" + LOADERCREATESTAMP + ", LOADERUPDATESTAMP=" + LOADERUPDATESTAMP
				+ ", BRANCHID=" + BRANCHID + ", FLEXTYPEID=" + FLEXTYPEID + ", ATT1=" + ATT1 + ", ATT2=" + ATT2
				+ ", ATT3=" + ATT3 + ", ATT4=" + ATT4 + ", ATT5=" + ATT5 + ", ATT6=" + ATT6 + ", ATT7=" + ATT7
				+ ", ATT8=" + ATT8 + ", ATT9=" + ATT9 + ", ATT10=" + ATT10 + ", ATT11=" + ATT11 + ", ATT12=" + ATT12
				+ ", ATT13=" + ATT13 + ", ATT14=" + ATT14 + ", ATT15=" + ATT15 + ", ATT16=" + ATT16 + ", ATT17=" + ATT17
				+ ", ATT18=" + ATT18 + ", ATT19=" + ATT19 + ", ATT20=" + ATT20 + ", ATT21=" + ATT21 + ", ATT22=" + ATT22
				+ ", ATT23=" + ATT23 + ", ATT24=" + ATT24 + ", ATT25=" + ATT25 + ", ATT26=" + ATT26 + ", ATT27=" + ATT27
				+ ", ATT28=" + ATT28 + ", ATT29=" + ATT29 + ", ATT30=" + ATT30 + ", DATE1=" + DATE1 + ", DATE2=" + DATE2
				+ ", DATE3=" + DATE3 + ", DATE4=" + DATE4 + ", DATE5=" + DATE5 + ", DATE6=" + DATE6 + ", DATE7=" + DATE7
				+ ", DATE8=" + DATE8 + ", DATE9=" + DATE9 + ", DATE10=" + DATE10 + ", NUM1=" + NUM1 + ", NUM2=" + NUM2
				+ ", NUM3=" + NUM3 + ", NUM4=" + NUM4 + ", NUM5=" + NUM5 + ", NUM6=" + NUM6 + ", NUM7=" + NUM7
				+ ", NUM8=" + NUM8 + ", NUM9=" + NUM9 + ", NUM10=" + NUM10 + ", NUM11=" + NUM11 + ", NUM12=" + NUM12
				+ ", NUM13=" + NUM13 + ", NUM14=" + NUM14 + ", NUM15=" + NUM15 + ", NUM16=" + NUM16 + ", NUM17=" + NUM17
				+ ", NUM18=" + NUM18 + ", NUM19=" + NUM19 + ", NUM20=" + NUM20 + "]";
	}

}
