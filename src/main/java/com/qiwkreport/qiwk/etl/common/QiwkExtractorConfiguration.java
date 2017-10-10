package com.qiwkreport.qiwk.etl.common;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiwkreport.qiwk.etl.controller.JobLaunchingController;

import wt.method.MethodContext;
import wt.method.RemoteMethodServer;
import wt.util.WTProperties;


@Component
public class QiwkExtractorConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QiwkExtractorConfiguration.class);
	
	@Value("${flexplm.admin.username}")
	private String flexUsername;
	
	@Value("${flexplm.admin.password}")
	private String flexPassword;

	public boolean runExtractorLoader(String objectName) {

		/*
		 * The Main method for running the extraction. This method will be
		 * calling the Java Classes specific to Objects to extract the Flex data
		 */
		RemoteMethodServer remoteMethodServer = null;// RemoteMethodServer.getDefault();
		try {
			WTProperties localWTProperties = WTProperties.getLocalProperties();
			String backgroundServer = localWTProperties.getProperty("java.rmi.server.hostname");
			URL url = new URL("http://" + backgroundServer + "/Windchill/rfa");
			remoteMethodServer = RemoteMethodServer.getInstance(url, "BackgroundMethodServer");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Boolean flag = true;
		
		LOGGER.info("Object --->" + objectName);
		
		remoteMethodServer.setUserName(flexUsername);
		remoteMethodServer.setPassword(flexPassword);
		
		
		MethodContext methodContext=new MethodContext("default", remoteMethodServer);
		
		//wt.method.MethodContext mc = new wt.method.MethodContext("default", remoteMethodServer);

		ExtractorThread.THREADCOUNT = Integer.parseInt(rb.getString("com.qiwkreports.extraction.threadCount"));
		System.out.println("----------THREADCOUNT------" + ExtractorThread.THREADCOUNT);
		String baseQuery = null;
		if ("FR".equalsIgnoreCase(mode)) {
			baseQuery = QiwkDBOperation.prepareQuery("createStampA2", startDate, endDate);
		} else {
			baseQuery = QiwkDBOperation.prepareQuery("modifyStampA2", startDate, endDate);
		}

		try {
			if ("SEASON".equals(objectName)) {
				QiwkSeason.extractSeasons(baseQuery, mode, batchCount, logger);
			}

			else if ("PRODUCT".equals(objectName)) {

				QiwkProduct.extractProducts(baseQuery, mode, batchCount, logger);
			} else if ("SEASONGROUP".equals(objectName)) {
				QiwkSeasonGroup.extractSeasonGroups(baseQuery, mode, batchCount, logger);
			} else if ("SEASONGROUPTOSKULINK".equals(objectName)) {
				QiwkSeasonGroupToSKULink.extractSeasonGroupToSKULinks(baseQuery, mode, batchCount, logger);

			}
			// This is for ProductCostSheet
			else if ("COSTSHEET".equals(objectName)) {
				QiwkCostSheet.extractCostSheet(baseQuery, mode, batchCount, logger);
			} else if ("SKUCOSTSHEET".equalsIgnoreCase(objectName)) {
				QiwkSKUCostSheet.extractCostSheet(baseQuery, mode, batchCount, logger);
			} else if ("SAMPLEREQUEST".equals(objectName)) {
				QiwkSampleRequest.extractSampleRequest(baseQuery, mode, batchCount, logger);
			} else if ("PRODUCTSEASONLINK".equals(objectName)) {
				QiwkProductSeasonLink.extractProductLinks(baseQuery, mode, batchCount, logger);
			} else if ("SKU".equals(objectName)) {
				QiwkSKU.extractColorways(baseQuery, mode, batchCount, logger);
			} else if ("SKUSEASONLINK".equals(objectName)) {
				QiwkSKUSeasonLink.extractSKULinks(baseQuery, mode, batchCount, logger);
			} else if ("SOURCINGCONFIG".equals(objectName)) {
				QiwkSourcingConfig.extractSourcingConfig(baseQuery, mode, batchCount, logger);
			} else if ("SKUSOURCINGLINK".equals(objectName)) {
				QiwkSKUSourcingLink.extractSourceSeasonLink(baseQuery, mode, batchCount, logger);
			} else if ("SAMPLE".equals(objectName)) {
				QiwkSample.extractSample(baseQuery, mode, batchCount, logger);
			} else if ("COUNTRY".equals(objectName)) {
				QiwkCountry.extractCountry(baseQuery, mode, batchCount, logger);
			} else if ("SOURCESEASONLINK".equals(objectName)) {
				QiwkSourceSeasonLink.extractSourceSeasonLink(baseQuery, mode, batchCount, logger);
			} else if ("FIT".equals(objectName)) {
				QiwkFit.extractFit(baseQuery, mode, batchCount, logger);
			} else if ("SPECIFICATION".equals(objectName)) {
				QiwkSpecification.extractSpecifications(baseQuery, mode, batchCount, logger);
			} else if ("SPECCOMPONENTLINK".equals(objectName)) {
				QiwkSpecComponentLink.extractSpecComponentLink(baseQuery, mode, batchCount, logger);
			} else if ("SUPPLIER".equals(objectName)) {
				QiwkSupplier.extractSupplier(baseQuery, mode, batchCount, logger);
			} else if ("MATERIAL".equals(objectName)) {
				QiwkMaterial.extractMaterial(baseQuery, mode, batchCount, logger);
			} else if ("MEASUREMENT".equals(objectName)) {
				QiwkMeasurement.extractMeasurements(baseQuery, mode, batchCount, logger);
			} else if ("COLOR".equals(objectName)) {
				QiwkColor.extractColor(baseQuery, mode, batchCount, logger);
			} else if ("BUSINESSOBJECT".equals(objectName)) {
				QiwkBusinessObject.extractBusinessObject(baseQuery, mode, batchCount, logger);
			} else if ("MATERIALCOLOR".equals(objectName)) {
				QiwkMaterialColor.extractMaterialColor(baseQuery, mode, batchCount, logger);
			} else if ("MATERIALSUPPLIER".equals(objectName)) {
				QiwkMaterialSupplier.extractMaterialSupplier(baseQuery, mode, batchCount, logger);
			} else if ("POINTSOFMEASURE".equals(objectName)) {
				QiwkPointsOfMeasure.extractPointsOfMeasure(baseQuery, mode, batchCount, logger);
			} else if ("MATERIALPRICINGENTRY".equals(objectName)) {
				QiwkMaterialPricingEntry.extractMaterialPricingEntry(baseQuery, mode, batchCount, logger);
			} else if ("BOM".equals(objectName)) {
				QiwkBOM.extractBOM(baseQuery, mode, batchCount, logger);
			} else if ("BOMLINK".equals(objectName)) {
				QiwkBOMLinkExport.extract(mode, baseQuery, null, batchCount, logger);
				flag = QiwkBOMUtils.noErrorInBOM;
			} else if ("PALETTE".equals(objectName)) {
				QiwkPalette.extractPallet(baseQuery, mode, batchCount, logger);
			} else if ("PALETTETOCOLORLINK".equals(objectName)) {
				QiwkPaletteToColorLink.extractPaletteToColorLink(baseQuery, mode, batchCount, logger);
			} else if ("PALETTEMATERIALLINK".equals(objectName)) {
				QiwkPaletteMaterialLink.extractPaletteMaterialLink(baseQuery, mode, batchCount, logger);
			} else if ("PALETTEMATERIALCOLORLINK".equals(objectName)) {
				QiwkPaletteMaterialColorLink.extractPaletteMaterialColorLink(baseQuery, mode, batchCount, logger);
			} else if ("REVISABLEENTITY".equals(objectName)) {
				QiwkRevisableEntity.extractRevisableEntities(baseQuery, mode, batchCount, logger);
			} else if ("PRODUCTSIZECATEGORY".equals(objectName)) {
				QiwkProductSizeCategory.extractProductSizeCategory(baseQuery, mode, batchCount, logger);
			} else if ("PRODUCTSIZECATEGORYTOSEASON".equals(objectName)) {
				QiwkProdSizeCategoryToSeason.extractProdSizeCategoryToSeason(baseQuery, mode, batchCount, logger);
			} else if ("USERLIST".equals(objectName)) {
				QiwkUser.extractUsers(baseQuery, mode, batchCount, logger);
			} else if ("DOCUMENT".equals(objectName)) {
				QiwkDocument.extractDocument(baseQuery, mode, batchCount, logger);
			} else if ("CHANGEAUDITEVENT".equals(objectName)) {
				QiwkChangeAuditEvent.extractChangeAuditEvent(baseQuery, mode, batchCount, logger);
			} else if ("CHANGEAUDITEVENTDETAIL".equals(objectName)) {
				QiwkChangeAuditEventDetail.extractChangeAuditEventDetail(baseQuery, mode, batchCount, logger);
			} else if ("MOAOBJECT".equals(objectName)) {
				QiwkMOAObject.extractMOAObject(baseQuery, mode, batchCount, logger);
			} else if ("DOCUMENTCOLLECTION".equals(objectName)) {
				QiwkDocumentCollection.extractDocumentCollection(baseQuery, mode, batchCount, logger);
			} else if ("DOCUMENTIMAGECONTENT".equals(objectName)) {
				QiwkDocumentImageContent.extractDocumentImageContent(baseQuery, mode, batchCount, logger);
			} else if ("FLEXTYPE".equals(objectName)) {
				QiwkFlexType.extractFlexType(baseQuery, mode, batchCount, logger);
			} else if ("FLEXTYPEATTRIBUTE".equals(objectName)) {
				QiwkFlexTypeAttribute.extractFlexTypeAttribute(baseQuery, mode, batchCount, logger);
			} else {

			}

			// All the sessions are closed directly with in each session.
			// no need to call this method separately.......
			// if(mode.equalsIgnoreCase("NC")){
			// QiwkService.commitSessionForNC();
			// }

		} catch (Exception wte) {
			/*
			 * mc.clear(); mc.freeConnection(); mc.clearConnection(); mc = null;
			 * server = null;
			 */
			wte.printStackTrace();
			logger.info("Exception in " + objectName + " Extraction: " + wte.toString());
			// System.out.println("mbean"+mc.mbean.getExceptionStackTrace().toString());
			flag = false;
		}
		mc.clear();

		mc.freeConnection();
		mc.clearConnection();

		mc = null;
		remoteMethodServer = null;
		return flag;

	}
}
