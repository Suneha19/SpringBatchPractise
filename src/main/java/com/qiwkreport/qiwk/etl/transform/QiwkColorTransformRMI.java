package com.qiwkreport.qiwk.etl.transform;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.lcs.wc.flextype.AttributeValueList;
import com.lcs.wc.flextype.FlexType;
import com.lcs.wc.flextype.FlexTypeAttribute;
import com.lcs.wc.flextype.FlexTypeCache;
import com.lcs.wc.util.FormatHelper;
import com.lcs.wc.util.MOAHelper;

import wt.method.RemoteAccess;
import wt.util.WTException;

public class QiwkColorTransformRMI implements RemoteAccess {

	public static HashMap getTransformCollections(HashMap child, String scope, String level)
			throws WTException, Exception {
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
		}

		Collection<FlexTypeAttribute> atts = flexType.getAllAttributes(scope, level);
		Set attColumns = child.keySet();
		Iterator<FlexTypeAttribute> it = atts.iterator();
		String moaAttvalue = null;
		AttributeValueList attValList = new AttributeValueList();

		while (it.hasNext()) {
			attribute = it.next();
			if (attribute != null && attribute.getColumnName() != null) {
				String tableColName = attribute.getColumnName().toUpperCase();
				if (tableColName != null && (tableColName.startsWith("PTC_STR_") || tableColName.startsWith("PTC_BLN_"))
						&& attColumns.contains(tableColName)) {
					String rowColKey = (String) child.get(tableColName);
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
