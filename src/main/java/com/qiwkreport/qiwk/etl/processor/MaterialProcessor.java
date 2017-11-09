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
import com.qiwkreport.qiwk.etl.domain.QiwkColor;
import com.qiwkreport.qiwk.etl.domain.QiwkMaterial;
import com.qiwkreport.qiwk.etl.flex.domain.LCSColor;
import com.qiwkreport.qiwk.etl.flex.domain.LCSMaterial;

import wt.util.WTException;

@Component
public class MaterialProcessor implements ItemProcessor<LCSMaterial, QiwkMaterial> {

	@Autowired
	private QiwkRMIService rmiService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialProcessor.class);

	@Override
	public QiwkMaterial process(LCSMaterial lcsMaterial) throws Exception {
		String scope = null;
		String level = null;
		HashMap hashMap = getMapOfColorFields(lcsMaterial);
		hashMap = rmiService.getTransformCollections(hashMap, scope, level);
		return convertMapToQiwkColorObject(hashMap, lcsMaterial);
	}

	private QiwkMaterial convertMapToQiwkColorObject(HashMap hashMap, LCSMaterial lcsMaterial) throws Exception {
		QiwkMaterial qiwkMaterial=new QiwkMaterial();
		
		qiwkMaterial.setBRANCHID((Integer)hashMap.get("BRANCHID"));
		qiwkMaterial.setLOADERCREATESTAMP(QiwkUtil.getTimestampValue());
		qiwkMaterial.setLOADERUPDATESTAMP(QiwkUtil.getTimestampValue());		
		qiwkMaterial.setPTC_STR_11TYPEINFOLCSCOLOR((String)hashMap.get("PTC_STR_11TYPEINFOLCSCOLOR"));
		
		qiwkMaterial.setCREATESTAMPA2(QiwkUtil.getDateTagValue((String)hashMap.get("CREATESTAMPA2")));
		qiwkMaterial.setUPDATESTAMPA2(QiwkUtil.getDateTagValue((String)hashMap.get("UPDATESTAMPA2")));
		qiwkMaterial.setMARKEDFORDELETEA2((Integer)hashMap.get("MARKFORDELETEA2"));
		qiwkMaterial.setUPDATECOUNTA2((Integer)hashMap.get("UPDATECOUNTA2"));
		qiwkMaterial.setFLEXTYPEID((Integer)hashMap.get("FLEXTYPEID"));
		qiwkMaterial.setIDA2TYPEDEFINITIONREFERENCE((Integer)hashMap.get("FLEXTYPEID"));
		qiwkMaterial.setFLEXTYPEIDPATH((String)hashMap.get("FLEXTYPEIDPATH"));
		qiwkMaterial.setCOLORHEXIDECIMALVALUE((String)hashMap.get("COLORHEXIDECIMALVALUE"));
		qiwkMaterial.setCOLORNAME((String)hashMap.get("COLORNAME"));
		qiwkMaterial.setTHUMBNAIL((String)hashMap.get("THUMBNAIL"));
		return qiwkMaterial;
	
	}

	private HashMap getMapOfColorFields(LCSMaterial lcsMaterial) {
		HashMap map = new HashMap<>();

		map.put("BRANCHID", lcsMaterial.getIDA2A2());
		map.put("FLEXTYPEID", lcsMaterial.getBRANCHIDA2TYPEDEFINITIONREFE());
		map.put("COLORHEXIDECIMALVALUE", "#" + lcsMaterial.getCOLORHEXIDECIMALVALUE());
		map.put("COLORNAME", lcsMaterial.getCOLORNAME());
		map.put("FLEXTYPEIDPATH", lcsMaterial.getFLEXTYPEIDPATH());
		map.put("IDA2TYPEDEFINITIONREFERENCE", lcsMaterial.getIDA2TYPEDEFINITIONREFERENCE());
		map.put("CREATESTAMPA2", lcsMaterial.getCREATESTAMPA2().toString());
		map.put("FLEXTYPEIDPATH", lcsMaterial.getFLEXTYPEIDPATH());
		map.put("IDA3A2FOLDERINGINFO", lcsMaterial.getIDA3A2FOLDERINGINFO());
		map.put("IDA3A2OWNERSHIP", lcsMaterial.getIDA3A2OWNERSHIP());
		map.put("IDA3A2STATE", lcsMaterial.getIDA3A2STATE());
		map.put("IDA3A7", lcsMaterial.getIDA3A7());
		map.put("IDA3B2FOLDERINGINFO", lcsMaterial.getIDA3B2FOLDERINGINFO());
		map.put("IDA3B8", lcsMaterial.getIDA3B8());
		map.put("IDA3C8", lcsMaterial.getIDA3C8());
		map.put("IDA3CONTAINERREFERENCE", lcsMaterial.getIDA3CONTAINERREFERENCE());
		map.put("IDA3DOMAINREF", lcsMaterial.getIDA3DOMAINREF());
		map.put("IDA3TEAMID", lcsMaterial.getIDA3TEAMID());
		map.put("IDA3TEAMTEMPLATEID", lcsMaterial.getIDA3TEAMTEMPLATEID());
		map.put("MARKFORDELETEA2", lcsMaterial.getMARKFORDELETEA2());
		map.put("PTC_STR_1TYPEINFOLCSCOLOR", lcsMaterial.getPTC_STR_1TYPEINFOLCSCOLOR());
		map.put("SECURITYLABELS", lcsMaterial.getSECURITYLABELS());
		map.put("THUMBNAIL", lcsMaterial.getTHUMBNAIL());
		map.put("UPDATECOUNTA2", lcsMaterial.getUPDATECOUNTA2());
		map.put("UPDATESTAMPA2", lcsMaterial.getUPDATESTAMPA2().toString());

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
			LOGGER.error("flexTypeoid is null for the child : " + child);
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
