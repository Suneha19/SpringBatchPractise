/**
 * 
 */
package com.qiwkreport.qiwk.etl.common;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiwkreport.qiwk.etl.processor.ColorProcessor;

import wt.method.RemoteMethodServer;
import wt.util.WTProperties;

/**
 * @author abhilash
 *
 */
@Component
public class QiwkRMIService {
	
	@Value("${flexplm.admin.username}")
	private String flexUsername;
	
	@Value("${flexplm.admin.password}")
	private String flexPassword;

	private static final Logger LOGGER = LoggerFactory.getLogger(QiwkRMIService.class);
	
	public HashMap getTransformedResullts(HashMap hashMap, String scope, String level) {
		HashMap map = null;
		RemoteMethodServer server = null;

		try {
			//server =  RemoteMethodServer.getDefault();//Instance(new URL("http://acnovate14.bomdimensions.com/Windchill/")); 
			WTProperties localWTProperties = WTProperties.getLocalProperties();
			String backgroundServer = localWTProperties.getProperty("java.rmi.server.hostname");
			URL url = new URL("http://"+backgroundServer+"/Windchill/rfa");
			server = RemoteMethodServer.getInstance(url,"BackgroundMethodServer");

			server.setUserName(flexUsername);
			server.setPassword(flexPassword);
			Class[] argClass = {HashMap.class, String.class, String.class};
			Object[] argObject = {hashMap, scope, level};

			if("FLEXTYPE".equals(scope)){

				map = (HashMap)server.invoke("getfType", com.qiwkreport.qiwk.etl.transform.QiwkColorTransformRMI.class.getName(), null, argClass, argObject);

			}else {
			/*	map = (HashMap) server.invoke("getTransformCollections",
						com.qiwkreport.qiwk.etl.transform.QiwkColorTransformRMI.class.getName(), null, argClass, argObject);*/
				map = (HashMap) server.invoke("getTransformCollections",
						"C:/Users/ACNuseABHILASH/Downloads/Acnovate/Qiwk/QiwkEtl/target/classes/com/qiwkreport/qiwk/etl/transform/QiwkColorTransformRMI".getClass().getName(), null, argClass, argObject);
				
			}
		} catch (InvocationTargetException | IOException e) {
			e.printStackTrace();
		}finally{
			server=null;
		}

		return map;

	}

}
