package com.qiwkreport.qiwk.etl.domain;

import java.security.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "QIWKMATERIAL")
public class QiwkMaterial implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PRIMARYKEY")
	private int primaryKey;
	@Column(name = "BRANCHID")
	private int BRANCHID;
	@Column(name = "IDA3MASTERREFERENCE")
	private int IDA3MASTERREFERENCE;
	@Column(name = "IDA2A2")
	private int IDA2A2;
	@Column(name = "CREATESTAMPA2")
	private Date CREATESTAMPA2;
	@Column(name = "UPDATESTAMPA2")
	private Date UPDATESTAMPA2;
	@Column(name = "DATELOCK")
	private Date DATELOCK;
	@Column(name = "FLEXTYPEIDPATH")
	private String FLEXTYPEIDPATH;
	@Column(name = "LIFECYCLE")
	private String LIFECYCLE;
	@Column(name = "FLEXTYPEID")
	private int FLEXTYPEID;
	@Column(name = "UPDATECOUNTA2")
	private int UPDATECOUNTA2;
	@Column(name = "MARKEDFORDELETEA2")
	private int MARKEDFORDELETEA2;
	@Column(name = "LOADERCREATESTAMP")
	private Timestamp LOADERCREATESTAMP;
	@Column(name = "LOADERUPDATESTAMP")
	private Timestamp LOADERUPDATESTAMP;

	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_STR_1TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_STR_2TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_STR_3TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_STR_4TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_STR_5TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_STR_6TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_STR_7TYPEINFOLCSMATERIAL;
   
	//double
	@Column(name = "LOADERUPDATESTAMP")
	private double PTC_DBL_1TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private double PTC_DBL_2TYPEINFOLCSMATERIAL;
	
	// long
	@Column(name = "LOADERUPDATESTAMP")
	private long PTC_LNG_1TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private long PTC_LNG_2TYPEINFOLCSMATERIAL;
	
	// boolean
	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_BLN_1TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private String PTC_BLN_2TYPEINFOLCSMATERIAL;
	
	// tms
	@Column(name = "LOADERUPDATESTAMP")
	private Date PTC_TMS_1TYPEINFOLCSMATERIAL;
	@Column(name = "LOADERUPDATESTAMP")
	private Date PTC_TMS_2TYPEINFOLCSMATERIAL;
	
	public QiwkMaterial() {
		super();
	}
	
	public QiwkMaterial(int primaryKey, int bRANCHID, int iDA3MASTERREFERENCE, int iDA2A2, Date cREATESTAMPA2,
			Date uPDATESTAMPA2, Date dATELOCK, String fLEXTYPEIDPATH, String lIFECYCLE, int fLEXTYPEID,
			int uPDATECOUNTA2, int mARKEDFORDELETEA2, Timestamp lOADERCREATESTAMP, Timestamp lOADERUPDATESTAMP,
			String pTC_STR_1TYPEINFOLCSMATERIAL, String pTC_STR_2TYPEINFOLCSMATERIAL,
			String pTC_STR_3TYPEINFOLCSMATERIAL, String pTC_STR_4TYPEINFOLCSMATERIAL,
			String pTC_STR_5TYPEINFOLCSMATERIAL, String pTC_STR_6TYPEINFOLCSMATERIAL,
			String pTC_STR_7TYPEINFOLCSMATERIAL, double pTC_DBL_1TYPEINFOLCSMATERIAL,
			double pTC_DBL_2TYPEINFOLCSMATERIAL, long pTC_LNG_1TYPEINFOLCSMATERIAL, long pTC_LNG_2TYPEINFOLCSMATERIAL,
			String pTC_BLN_1TYPEINFOLCSMATERIAL, String pTC_BLN_2TYPEINFOLCSMATERIAL, Date pTC_TMS_1TYPEINFOLCSMATERIAL,
			Date pTC_TMS_2TYPEINFOLCSMATERIAL) {
		super();
		this.primaryKey = primaryKey;
		BRANCHID = bRANCHID;
		IDA3MASTERREFERENCE = iDA3MASTERREFERENCE;
		IDA2A2 = iDA2A2;
		CREATESTAMPA2 = cREATESTAMPA2;
		UPDATESTAMPA2 = uPDATESTAMPA2;
		DATELOCK = dATELOCK;
		FLEXTYPEIDPATH = fLEXTYPEIDPATH;
		LIFECYCLE = lIFECYCLE;
		FLEXTYPEID = fLEXTYPEID;
		UPDATECOUNTA2 = uPDATECOUNTA2;
		MARKEDFORDELETEA2 = mARKEDFORDELETEA2;
		LOADERCREATESTAMP = lOADERCREATESTAMP;
		LOADERUPDATESTAMP = lOADERUPDATESTAMP;
		PTC_STR_1TYPEINFOLCSMATERIAL = pTC_STR_1TYPEINFOLCSMATERIAL;
		PTC_STR_2TYPEINFOLCSMATERIAL = pTC_STR_2TYPEINFOLCSMATERIAL;
		PTC_STR_3TYPEINFOLCSMATERIAL = pTC_STR_3TYPEINFOLCSMATERIAL;
		PTC_STR_4TYPEINFOLCSMATERIAL = pTC_STR_4TYPEINFOLCSMATERIAL;
		PTC_STR_5TYPEINFOLCSMATERIAL = pTC_STR_5TYPEINFOLCSMATERIAL;
		PTC_STR_6TYPEINFOLCSMATERIAL = pTC_STR_6TYPEINFOLCSMATERIAL;
		PTC_STR_7TYPEINFOLCSMATERIAL = pTC_STR_7TYPEINFOLCSMATERIAL;
		PTC_DBL_1TYPEINFOLCSMATERIAL = pTC_DBL_1TYPEINFOLCSMATERIAL;
		PTC_DBL_2TYPEINFOLCSMATERIAL = pTC_DBL_2TYPEINFOLCSMATERIAL;
		PTC_LNG_1TYPEINFOLCSMATERIAL = pTC_LNG_1TYPEINFOLCSMATERIAL;
		PTC_LNG_2TYPEINFOLCSMATERIAL = pTC_LNG_2TYPEINFOLCSMATERIAL;
		PTC_BLN_1TYPEINFOLCSMATERIAL = pTC_BLN_1TYPEINFOLCSMATERIAL;
		PTC_BLN_2TYPEINFOLCSMATERIAL = pTC_BLN_2TYPEINFOLCSMATERIAL;
		PTC_TMS_1TYPEINFOLCSMATERIAL = pTC_TMS_1TYPEINFOLCSMATERIAL;
		PTC_TMS_2TYPEINFOLCSMATERIAL = pTC_TMS_2TYPEINFOLCSMATERIAL;
	}
	public int getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}
	public int getBRANCHID() {
		return BRANCHID;
	}
	public void setBRANCHID(int bRANCHID) {
		BRANCHID = bRANCHID;
	}
	public int getIDA3MASTERREFERENCE() {
		return IDA3MASTERREFERENCE;
	}
	public void setIDA3MASTERREFERENCE(int iDA3MASTERREFERENCE) {
		IDA3MASTERREFERENCE = iDA3MASTERREFERENCE;
	}
	public int getIDA2A2() {
		return IDA2A2;
	}
	public void setIDA2A2(int iDA2A2) {
		IDA2A2 = iDA2A2;
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
	public Date getDATELOCK() {
		return DATELOCK;
	}
	public void setDATELOCK(Date dATELOCK) {
		DATELOCK = dATELOCK;
	}
	public String getFLEXTYPEIDPATH() {
		return FLEXTYPEIDPATH;
	}
	public void setFLEXTYPEIDPATH(String fLEXTYPEIDPATH) {
		FLEXTYPEIDPATH = fLEXTYPEIDPATH;
	}
	public String getLIFECYCLE() {
		return LIFECYCLE;
	}
	public void setLIFECYCLE(String lIFECYCLE) {
		LIFECYCLE = lIFECYCLE;
	}
	public int getFLEXTYPEID() {
		return FLEXTYPEID;
	}
	public void setFLEXTYPEID(int fLEXTYPEID) {
		FLEXTYPEID = fLEXTYPEID;
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
	public String getPTC_STR_1TYPEINFOLCSMATERIAL() {
		return PTC_STR_1TYPEINFOLCSMATERIAL;
	}
	public void setPTC_STR_1TYPEINFOLCSMATERIAL(String pTC_STR_1TYPEINFOLCSMATERIAL) {
		PTC_STR_1TYPEINFOLCSMATERIAL = pTC_STR_1TYPEINFOLCSMATERIAL;
	}
	public String getPTC_STR_2TYPEINFOLCSMATERIAL() {
		return PTC_STR_2TYPEINFOLCSMATERIAL;
	}
	public void setPTC_STR_2TYPEINFOLCSMATERIAL(String pTC_STR_2TYPEINFOLCSMATERIAL) {
		PTC_STR_2TYPEINFOLCSMATERIAL = pTC_STR_2TYPEINFOLCSMATERIAL;
	}
	public String getPTC_STR_3TYPEINFOLCSMATERIAL() {
		return PTC_STR_3TYPEINFOLCSMATERIAL;
	}
	public void setPTC_STR_3TYPEINFOLCSMATERIAL(String pTC_STR_3TYPEINFOLCSMATERIAL) {
		PTC_STR_3TYPEINFOLCSMATERIAL = pTC_STR_3TYPEINFOLCSMATERIAL;
	}
	public String getPTC_STR_4TYPEINFOLCSMATERIAL() {
		return PTC_STR_4TYPEINFOLCSMATERIAL;
	}
	public void setPTC_STR_4TYPEINFOLCSMATERIAL(String pTC_STR_4TYPEINFOLCSMATERIAL) {
		PTC_STR_4TYPEINFOLCSMATERIAL = pTC_STR_4TYPEINFOLCSMATERIAL;
	}
	public String getPTC_STR_5TYPEINFOLCSMATERIAL() {
		return PTC_STR_5TYPEINFOLCSMATERIAL;
	}
	public void setPTC_STR_5TYPEINFOLCSMATERIAL(String pTC_STR_5TYPEINFOLCSMATERIAL) {
		PTC_STR_5TYPEINFOLCSMATERIAL = pTC_STR_5TYPEINFOLCSMATERIAL;
	}
	public String getPTC_STR_6TYPEINFOLCSMATERIAL() {
		return PTC_STR_6TYPEINFOLCSMATERIAL;
	}
	public void setPTC_STR_6TYPEINFOLCSMATERIAL(String pTC_STR_6TYPEINFOLCSMATERIAL) {
		PTC_STR_6TYPEINFOLCSMATERIAL = pTC_STR_6TYPEINFOLCSMATERIAL;
	}
	public String getPTC_STR_7TYPEINFOLCSMATERIAL() {
		return PTC_STR_7TYPEINFOLCSMATERIAL;
	}
	public void setPTC_STR_7TYPEINFOLCSMATERIAL(String pTC_STR_7TYPEINFOLCSMATERIAL) {
		PTC_STR_7TYPEINFOLCSMATERIAL = pTC_STR_7TYPEINFOLCSMATERIAL;
	}
	public double getPTC_DBL_1TYPEINFOLCSMATERIAL() {
		return PTC_DBL_1TYPEINFOLCSMATERIAL;
	}
	public void setPTC_DBL_1TYPEINFOLCSMATERIAL(double pTC_DBL_1TYPEINFOLCSMATERIAL) {
		PTC_DBL_1TYPEINFOLCSMATERIAL = pTC_DBL_1TYPEINFOLCSMATERIAL;
	}
	public double getPTC_DBL_2TYPEINFOLCSMATERIAL() {
		return PTC_DBL_2TYPEINFOLCSMATERIAL;
	}
	public void setPTC_DBL_2TYPEINFOLCSMATERIAL(double pTC_DBL_2TYPEINFOLCSMATERIAL) {
		PTC_DBL_2TYPEINFOLCSMATERIAL = pTC_DBL_2TYPEINFOLCSMATERIAL;
	}
	public long getPTC_LNG_1TYPEINFOLCSMATERIAL() {
		return PTC_LNG_1TYPEINFOLCSMATERIAL;
	}
	public void setPTC_LNG_1TYPEINFOLCSMATERIAL(long pTC_LNG_1TYPEINFOLCSMATERIAL) {
		PTC_LNG_1TYPEINFOLCSMATERIAL = pTC_LNG_1TYPEINFOLCSMATERIAL;
	}
	public long getPTC_LNG_2TYPEINFOLCSMATERIAL() {
		return PTC_LNG_2TYPEINFOLCSMATERIAL;
	}
	public void setPTC_LNG_2TYPEINFOLCSMATERIAL(long pTC_LNG_2TYPEINFOLCSMATERIAL) {
		PTC_LNG_2TYPEINFOLCSMATERIAL = pTC_LNG_2TYPEINFOLCSMATERIAL;
	}
	public String getPTC_BLN_1TYPEINFOLCSMATERIAL() {
		return PTC_BLN_1TYPEINFOLCSMATERIAL;
	}
	public void setPTC_BLN_1TYPEINFOLCSMATERIAL(String pTC_BLN_1TYPEINFOLCSMATERIAL) {
		PTC_BLN_1TYPEINFOLCSMATERIAL = pTC_BLN_1TYPEINFOLCSMATERIAL;
	}
	public String getPTC_BLN_2TYPEINFOLCSMATERIAL() {
		return PTC_BLN_2TYPEINFOLCSMATERIAL;
	}
	public void setPTC_BLN_2TYPEINFOLCSMATERIAL(String pTC_BLN_2TYPEINFOLCSMATERIAL) {
		PTC_BLN_2TYPEINFOLCSMATERIAL = pTC_BLN_2TYPEINFOLCSMATERIAL;
	}
	public Date getPTC_TMS_1TYPEINFOLCSMATERIAL() {
		return PTC_TMS_1TYPEINFOLCSMATERIAL;
	}
	public void setPTC_TMS_1TYPEINFOLCSMATERIAL(Date pTC_TMS_1TYPEINFOLCSMATERIAL) {
		PTC_TMS_1TYPEINFOLCSMATERIAL = pTC_TMS_1TYPEINFOLCSMATERIAL;
	}
	public Date getPTC_TMS_2TYPEINFOLCSMATERIAL() {
		return PTC_TMS_2TYPEINFOLCSMATERIAL;
	}
	public void setPTC_TMS_2TYPEINFOLCSMATERIAL(Date pTC_TMS_2TYPEINFOLCSMATERIAL) {
		PTC_TMS_2TYPEINFOLCSMATERIAL = pTC_TMS_2TYPEINFOLCSMATERIAL;
	}
	
	@Override
	public String toString() {
		return "QiwkMaterial [primaryKey=" + primaryKey + ", BRANCHID=" + BRANCHID + ", IDA3MASTERREFERENCE="
				+ IDA3MASTERREFERENCE + ", IDA2A2=" + IDA2A2 + ", CREATESTAMPA2=" + CREATESTAMPA2 + ", UPDATESTAMPA2="
				+ UPDATESTAMPA2 + ", DATELOCK=" + DATELOCK + ", FLEXTYPEIDPATH=" + FLEXTYPEIDPATH + ", LIFECYCLE="
				+ LIFECYCLE + ", FLEXTYPEID=" + FLEXTYPEID + ", UPDATECOUNTA2=" + UPDATECOUNTA2 + ", MARKEDFORDELETEA2="
				+ MARKEDFORDELETEA2 + ", LOADERCREATESTAMP=" + LOADERCREATESTAMP + ", LOADERUPDATESTAMP="
				+ LOADERUPDATESTAMP + ", PTC_STR_1TYPEINFOLCSMATERIAL=" + PTC_STR_1TYPEINFOLCSMATERIAL
				+ ", PTC_STR_2TYPEINFOLCSMATERIAL=" + PTC_STR_2TYPEINFOLCSMATERIAL + ", PTC_STR_3TYPEINFOLCSMATERIAL="
				+ PTC_STR_3TYPEINFOLCSMATERIAL + ", PTC_STR_4TYPEINFOLCSMATERIAL=" + PTC_STR_4TYPEINFOLCSMATERIAL
				+ ", PTC_STR_5TYPEINFOLCSMATERIAL=" + PTC_STR_5TYPEINFOLCSMATERIAL + ", PTC_STR_6TYPEINFOLCSMATERIAL="
				+ PTC_STR_6TYPEINFOLCSMATERIAL + ", PTC_STR_7TYPEINFOLCSMATERIAL=" + PTC_STR_7TYPEINFOLCSMATERIAL
				+ ", PTC_DBL_1TYPEINFOLCSMATERIAL=" + PTC_DBL_1TYPEINFOLCSMATERIAL + ", PTC_DBL_2TYPEINFOLCSMATERIAL="
				+ PTC_DBL_2TYPEINFOLCSMATERIAL + ", PTC_LNG_1TYPEINFOLCSMATERIAL=" + PTC_LNG_1TYPEINFOLCSMATERIAL
				+ ", PTC_LNG_2TYPEINFOLCSMATERIAL=" + PTC_LNG_2TYPEINFOLCSMATERIAL + ", PTC_BLN_1TYPEINFOLCSMATERIAL="
				+ PTC_BLN_1TYPEINFOLCSMATERIAL + ", PTC_BLN_2TYPEINFOLCSMATERIAL=" + PTC_BLN_2TYPEINFOLCSMATERIAL
				+ ", PTC_TMS_1TYPEINFOLCSMATERIAL=" + PTC_TMS_1TYPEINFOLCSMATERIAL + ", PTC_TMS_2TYPEINFOLCSMATERIAL="
				+ PTC_TMS_2TYPEINFOLCSMATERIAL + "]";
	}
	
	
}
