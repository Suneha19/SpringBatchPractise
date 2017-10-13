package com.qiwkreport.qiwk.etl.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class QiwkUtil {

	/**
	 * @param sTag
	 * @param eElement
	 * @return
	 */
	public static String getTagValue(String sTag, Element eElement) {
		String nodeValue = null;
		NodeList nodelist = eElement.getElementsByTagName(sTag);
		Element element1 = (Element) nodelist.item(0);
		if (element1.hasChildNodes()) {
			NodeList nlList = element1.getChildNodes();
			nodeValue = nlList.item(0).getNodeValue();
			return nodeValue;
		} else {
			return nodeValue;
		}

	}

	/**
	 * @param nodeName
	 * @param element
	 * @return
	 */
	public static double getNUMTagValue(String value, String eElement) {
		double dd = 0.0;
		if (value != null)
			dd = Double.valueOf(value);
		return dd;
	}

	public static long getLongTagValue(String value, String eElement) {
		long ll = 0;
		if (value != null)
			ll = Long.valueOf(value);
		return ll;
	}

	/**
	 * @param sTag
	 * @param eElement
	 * @return
	 * @throws Exception
	 */
	
	public static Date getDateTagValue(String sTag) throws Exception {
		Date dt = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String TimeStr = getTagValue(sTag, null);
		if (TimeStr != null && (!"null".equalsIgnoreCase(TimeStr))) {
			dt = dateFormat.parse(TimeStr);
		}
		return dt;
	}
	
	public static Date getDateTagValue(String sTag, Element eElement) throws Exception {
		Date dt = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String TimeStr = getTagValue(sTag, eElement);
		if (TimeStr != null && (!"null".equalsIgnoreCase(TimeStr))) {
			dt = dateFormat.parse(TimeStr);
		}
		return dt;
	}

	public static Date getDateTagValue(String sTag, String eElement) throws Exception {

		Date dt = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String TimeStr = sTag;
		if (TimeStr != null && (!"null".equalsIgnoreCase(TimeStr))) {
			dt = dateFormat.parse(TimeStr);
		}
		return dt;
	}

	/**
	 * @param sTag
	 * @param eElement
	 * @return
	 */
	public static int getIntTagValue(String sTag, Element eElement) {
		int a = 0;
		String s = getTagValue(sTag, eElement);
		if (s != null && !s.isEmpty()) {
			a = Integer.parseInt(s);
		}
		return a;
	}
	
	public static int getIntTagValue(String sTag) {
		int a = 0;
		String s = getTagValue(sTag,null);
		if (s != null && !s.isEmpty()) {
			a = Integer.parseInt(s);
		}
		return a;
	}

	public static int getIntTagValue(String sTag, String eElement) {
		int a = 0;
		String s = sTag;
		if (s != null && !s.isEmpty()) {
			a = Integer.parseInt(s);
		}
		return a;
	}

	public static Timestamp getTimestampValue() throws Exception {
		SimpleDateFormat gmtDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z");
		gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Timestamp gmtStamp = new Timestamp(gmtDateFormat.parse(gmtDateFormat.format(new Date())).getTime());
		return gmtStamp;
	}

}
