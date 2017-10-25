package com.qiwkreport.qiwk.etl.processor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lcs.wc.flextype.AttributeValueList;
import com.lcs.wc.flextype.FlexType;
import com.lcs.wc.flextype.FlexTypeAttribute;
import com.lcs.wc.flextype.FlexTypeCache;
import com.lcs.wc.util.FormatHelper;
import com.lcs.wc.util.MOAHelper;
import com.qiwkreport.qiwk.etl.common.QiwkRMIService;
import com.qiwkreport.qiwk.etl.common.QiwkUtil;
import com.qiwkreport.qiwk.etl.controller.JobLaunchingController;
import com.qiwkreport.qiwk.etl.domain.QiwkColor;
import com.qiwkreport.qiwk.etl.flex.domain.LCSColor;

import wt.util.WTException;

@Component
public class ColorProcessor implements ItemProcessor<LCSColor, QiwkColor> {

	@Autowired
	private QiwkRMIService rmiService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ColorProcessor.class);

	@Override
	public QiwkColor process(LCSColor lcsColor) throws Exception {
		String scope = null;
		String level = null;
		HashMap hashMap = getMapOfColorFields(lcsColor);
		LOGGER.info("1---->"+hashMap);
		//hashMap = rmiService.getTransformedResullts(hashMap, scope, level);
		LOGGER.info("2---->"+hashMap);
		return convertMapToQiwkColorObject(hashMap, lcsColor);
	}

	private QiwkColor convertMapToQiwkColorObject(HashMap hashMap, LCSColor lcsColor) throws Exception {
		QiwkColor qiwkColor=new QiwkColor();
		
		qiwkColor.setBRANCHID((Integer)hashMap.get("BRANCHID"));
		qiwkColor.setLOADERCREATESTAMP(QiwkUtil.getTimestampValue());
		qiwkColor.setLOADERUPDATESTAMP(QiwkUtil.getTimestampValue());		
		qiwkColor.setPTC_STR_11TYPEINFOLCSCOLOR((String)hashMap.get("PTC_STR_11TYPEINFOLCSCOLOR"));
		
		qiwkColor.setCREATESTAMPA2(QiwkUtil.getDateTagValue((String)hashMap.get("CREATESTAMPA2")));
		qiwkColor.setUPDATESTAMPA2(QiwkUtil.getDateTagValue((String)hashMap.get("UPDATESTAMPA2")));
		qiwkColor.setMARKEDFORDELETEA2((Integer)hashMap.get("MARKFORDELETEA2"));
		qiwkColor.setUPDATECOUNTA2((Integer)hashMap.get("UPDATECOUNTA2"));
		qiwkColor.setFLEXTYPEID((Integer)hashMap.get("FLEXTYPEID"));
		qiwkColor.setIDA2TYPEDEFINITIONREFERENCE((Integer)hashMap.get("FLEXTYPEID"));
		qiwkColor.setFLEXTYPEIDPATH((String)hashMap.get("FLEXTYPEIDPATH"));
		qiwkColor.setCOLORHEXIDECIMALVALUE((String)hashMap.get("COLORHEXIDECIMALVALUE"));
		qiwkColor.setCOLORNAME((String)hashMap.get("COLORNAME"));
		qiwkColor.setTHUMBNAIL((String)hashMap.get("THUMBNAIL"));
		LOGGER.info("qiwkColor---->"+qiwkColor);
		return qiwkColor;
	
	}

	private HashMap getMapOfColorFields(LCSColor lcsColor) {
		HashMap map = new HashMap<>();

		map.put("BRANCHID", lcsColor.getIDA2A2());
		map.put("FLEXTYPEID", lcsColor.getBRANCHIDA2TYPEDEFINITIONREFE());
		map.put("COLORHEXIDECIMALVALUE", "#" + lcsColor.getCOLORHEXIDECIMALVALUE());
		map.put("COLORNAME", lcsColor.getCOLORNAME());
		map.put("FLEXTYPEIDPATH", lcsColor.getFLEXTYPEIDPATH());
		map.put("IDA2TYPEDEFINITIONREFERENCE", lcsColor.getIDA2TYPEDEFINITIONREFERENCE());
		map.put("CREATESTAMPA2", lcsColor.getCREATESTAMPA2().toString());
		map.put("FLEXTYPEIDPATH", lcsColor.getFLEXTYPEIDPATH());
		map.put("IDA3A2FOLDERINGINFO", lcsColor.getIDA3A2FOLDERINGINFO());
		map.put("IDA3A2OWNERSHIP", lcsColor.getIDA3A2OWNERSHIP());
		map.put("IDA3A2STATE", lcsColor.getIDA3A2STATE());
		map.put("IDA3A7", lcsColor.getIDA3A7());
		map.put("IDA3B2FOLDERINGINFO", lcsColor.getIDA3B2FOLDERINGINFO());
		map.put("IDA3B8", lcsColor.getIDA3B8());
		map.put("IDA3C8", lcsColor.getIDA3C8());
		map.put("IDA3CONTAINERREFERENCE", lcsColor.getIDA3CONTAINERREFERENCE());
		map.put("IDA3DOMAINREF", lcsColor.getIDA3DOMAINREF());
		map.put("IDA3TEAMID", lcsColor.getIDA3TEAMID());
		map.put("IDA3TEAMTEMPLATEID", lcsColor.getIDA3TEAMTEMPLATEID());
		map.put("MARKFORDELETEA2", lcsColor.getMARKFORDELETEA2());
		map.put("PTC_STR_1TYPEINFOLCSCOLOR", lcsColor.getPTC_STR_1TYPEINFOLCSCOLOR());
		map.put("SECURITYLABELS", lcsColor.getSECURITYLABELS());
		map.put("THUMBNAIL", lcsColor.getTHUMBNAIL());
		map.put("UPDATECOUNTA2", lcsColor.getUPDATECOUNTA2());
		map.put("UPDATESTAMPA2", lcsColor.getUPDATESTAMPA2().toString());

		return map;
	}

	public HashMap getTransformCollections(HashMap child, String scope, String level) throws WTException, Exception {
		FlexTypeAttribute attribute = null;
		Integer flexTypeoid = null;

		if (child.get("FLEXTYPEID") instanceof String) {
			flexTypeoid = new Integer((String) child.get("FLEXTYPEID"));
		} else if (child.get("FLEXTYPEID") instanceof Integer) {
			flexTypeoid = (Integer) child.get("FLEXTYPEID");
		} else if (child.get("FLEXTYPEID") == null && child.get("FLEXTYPEIDPATH") != null) {
			String flexTypeIdPath = (String) child.get("FLEXTYPEIDPATH");
			flexTypeIdPath = flexTypeIdPath.substring(flexTypeIdPath.lastIndexOf("\\") + 1, flexTypeIdPath.length());
			flexTypeoid = Integer.valueOf(flexTypeIdPath);
		}
		String imageURL = (String) child.get("PARTPRIMARYIMAGEURL");
		if (imageURL != null && imageURL.indexOf("/") > -1) {
			imageURL = imageURL.substring(imageURL.lastIndexOf("/") + 1, imageURL.length());
			child.put("PARTPRIMARYIMAGEURL", imageURL);
		}

		imageURL = (String) child.get("THUMBNAIL");
		if (imageURL != null && imageURL.indexOf("/") > -1) {
			imageURL = imageURL.substring(imageURL.lastIndexOf("/") + 1, imageURL.length());
			child.put("THUMBNAIL", imageURL);
		}

		if ("COST_SHEET".equals(scope) && "product".equalsIgnoreCase(level)) {
			String colorName = (String) child.get("APPLICABLECOLORNAMES");
			colorName = MOAHelper.parseOutDelims(colorName, ",");
			String sizeCategoryName = (String) child.get("APPLICABLESIZECATEGORYNAMES");
			sizeCategoryName = MOAHelper.parseOutDelims(sizeCategoryName, ",");
			String applicableDestinationNames = (String) child.get("APPLICABLEDESTINATIONNAMES");
			applicableDestinationNames = MOAHelper.parseOutDelims(applicableDestinationNames, ",");
			String applicableSize = (String) child.get("APPLICABLESIZES");
			applicableSize = MOAHelper.parseOutDelims(applicableSize, ",");
			String applicableSizes2 = (String) child.get("APPLICABLESIZES2");
			applicableSizes2 = MOAHelper.parseOutDelims(applicableSizes2, ",");

			child.put("APPLICABLECOLORNAMES", colorName);
			child.put("APPLICABLESIZECATEGORIESNAMES", sizeCategoryName);
			child.put("APPLICABLEDESTINATIONNAMES", applicableDestinationNames);
			child.put("APPLICABLESIZES", applicableSize);
			child.put("APPLICABLESIZES2", applicableSizes2);
		}

		FlexType flexType = null;

		if (flexTypeoid != null) {
			flexType = FlexTypeCache.getFlexType("OR:com.lcs.wc.flextype.FlexType:" + flexTypeoid.toString());
		} else {
			System.out.println("flexTypeoid is null for the child : " + child);
		}

		Collection atts = flexType.getAllAttributes(scope, level);
		Set attColumns = child.keySet();
		Iterator it = atts.iterator();
		String moaAttvalue = null;
		AttributeValueList attValList = null;
		attValList = new AttributeValueList();

		while (it.hasNext()) {
			attribute = (FlexTypeAttribute) it.next();
			if (attribute != null && attribute.getColumnName() != null) {
				String tableColName = attribute.getColumnName().toUpperCase();
				if (tableColName != null && (tableColName.startsWith("PTC_STR_") || tableColName.startsWith("PTC_BLN_"))
						&& attColumns.contains(tableColName)) {
					String rowColKey = (String) child.get(attribute.getColumnName().toUpperCase());
					String variableType = attribute.getAttVariableType();
					if (!FormatHelper.hasContentAllowZero(rowColKey))
						continue;

					if (variableType.equalsIgnoreCase("composite")) {

						attValList = attribute.getAttValueList();
						moaAttvalue = MOAHelper.parseCompositeString(rowColKey, attValList, Locale.getDefault(),
								"|~*~|");

						child.put(tableColName, moaAttvalue);
					} else if (variableType.equalsIgnoreCase("boolean")) {
						attValList = attribute.getAttValueList();
						moaAttvalue = FormatHelper.formatBoolean(rowColKey);
						child.put(tableColName, moaAttvalue);
					} else if (variableType.equalsIgnoreCase("choice") || variableType.equalsIgnoreCase("driven")
							|| variableType.equalsIgnoreCase("moaEntry") || variableType.equalsIgnoreCase("moaList")
							|| variableType.equalsIgnoreCase("derivedString")
							|| variableType.equalsIgnoreCase("colorSelect")) {

						attValList = attribute.getAttValueList();
						moaAttvalue = MOAHelper.parseOutDelimsLocalized(rowColKey, ",", attValList,
								Locale.getDefault());

						child.put(tableColName, moaAttvalue);
					}
				}
			}
		}
		return child;
	}

}
