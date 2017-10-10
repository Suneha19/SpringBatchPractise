package com.qiwkreport.qiwk.etl.common;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import wt.dataservice.DSPropertiesServer;
import wt.dataservice.DataServiceFactory;
import wt.dataservice.Datastore;
import wt.dataservice.Oracle;
import wt.util.WTProperties;

@Configuration
public class FlexDBConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public static String DATABASE;
	
   // private Connection conn;
 //   private boolean DEBUG = true;
    private String LCS_DB_PROPERTIES_FILE = "db.properties";

/*    public FlexDBConnectionConfiguration(String URL, String login, String password) {//not used

        //Loading drivers
        try { // register driver
					//System.out.println("DBConnection constructor---");

            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            conn = DriverManager.getConnection(URL, login, password);
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }*/

    @Bean
	public DataSource flexDataSource() {

		Properties properties = new Properties();
		
		Datastore datastore = DataServiceFactory.getDefault().getDatastore();
		if (datastore instanceof Oracle) {
			// System.out.println("DATASTORE:oracle");
			DATABASE = "oracle";
		} else {
			// System.out.println("DATASTORE:sqlserver");
			DATABASE = "sqlserver";
		}
	
		try {
			String WT_HOME = WTProperties.getLocalProperties().getProperty("wt.home");

			String propFileName = WT_HOME + File.separator + "db" + File.separator + LCS_DB_PROPERTIES_FILE;
			properties.load(new FileInputStream(propFileName));
			// get DB properties
			String db_login = properties.getProperty("wt.pom.dbUser");
			String db_pwd = properties.getProperty("wt.pom.dbPassword");
			// Decrypting the Password if encrypted
			if (db_pwd != null && (db_pwd.indexOf("encrypted.") == 0)) {
				db_pwd = DSPropertiesServer.getKeyStoreValue(db_pwd);
			}
			String db_url = "";
			String host = properties.getProperty("wt.pom.jdbc.host");

			// register driver
			db_url = "jdbc:oracle:thin:@" + properties.getProperty("wt.pom.jdbc.host") + ":"
					+ properties.getProperty("wt.pom.jdbc.port") + "/" + properties.getProperty("wt.pom.jdbc.service");
			if (host != null && (host.startsWith("(DESCRIPTION") || host.startsWith("(description"))) {
				db_url = "jdbc:oracle:thin:@" + host;
			}

		/*	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			conn = DriverManager.getConnection(db_url, db_login, db_pwd);*/
			
			dataSource.setUsername(db_login);
			dataSource.setPassword(db_pwd);
			dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
			dataSource.setUrl(db_url);
			
		} catch (IOException e) {
			
		}
		return dataSource;

	}

    @Bean
    public EntityManagerFactory getEntityManagerFactory(){
    	LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    	  factoryBean.setDataSource(this.flexDataSource());
    	 // factoryBean.setPersistenceUnitName("persistenceUnitName");
    	  factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	  factoryBean.afterPropertiesSet();

    	  EntityManagerFactory factory = factoryBean.getNativeEntityManagerFactory();
    	  return factory;
    }
    
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		
	}
    
/*    public FlexDBConnectionConfiguration() {
    	

        //Loading drivers
        try {
            // Read properties file.
            Properties properties = new Properties();
            Datastore datastore = DataServiceFactory.getDefault().getDatastore();
            if (datastore instanceof Oracle)
            {
            	//System.out.println("DATASTORE:oracle");
            	DATABASE="oracle";
            }else{
            	//System.out.println("DATASTORE:sqlserver");
            	DATABASE="sqlserver";
            }
            
       			String WT_HOME=WTProperties.getLocalProperties().getProperty("wt.home");
				
				String propFileName = WT_HOME+ File.separator +
				                         "db" + File.separator + LCS_DB_PROPERTIES_FILE;
                properties.load(new FileInputStream(propFileName));
            
            //get properties
            String db_login = properties.getProperty("wt.pom.dbUser");
            String db_pwd = properties.getProperty("wt.pom.dbPassword");
            //Decrypting the Password if encrypted
            if (db_pwd != null && (db_pwd.indexOf("encrypted.") == 0)) {
            	db_pwd = DSPropertiesServer.getKeyStoreValue(db_pwd);
            }
            String db_url ="";
            String host = properties.getProperty("wt.pom.jdbc.host");
            
            //register driver
			if (DATABASE.equals("oracle")) {
				db_url = "jdbc:oracle:thin:@" + properties.getProperty("wt.pom.jdbc.host") + ":"
						+ properties.getProperty("wt.pom.jdbc.port") + "/"
						+ properties.getProperty("wt.pom.jdbc.service");
				if (host != null && (host.startsWith("(DESCRIPTION") || host.startsWith("(description"))) {
					db_url = "jdbc:oracle:thin:@" + host;
				}
				
				DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
				conn = DriverManager.getConnection(db_url, db_login, db_pwd);
			}
            else if (DATABASE.equals("sqlserver")){
            	try{
            	db_url = "jdbc:sqlserver://"+properties.getProperty("wt.pom.jdbc.host");
            	if(null != service && !service.equals("")){
            		db_url=db_url+"\\"+properties.getProperty("wt.pom.jdbc.service");
            	}
            	if(null != port && !port.equals("") && (service==null || service.equals(""))){
            		db_url=db_url+":" + properties.getProperty("wt.pom.jdbc.port");
            	}
            	if(null != dataBase && !dataBase.equals("")){
            		db_url=db_url+";databaseName="+dataBase;
            	}
            	//System.out.println("DB URL="+db_url);
            	DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                conn = DriverManager.getConnection(db_url, db_login, db_pwd);
            	}
            	catch(Exception e){
            		//in case there is exception in connection try other way
            		db_url = "jdbc:sqlserver:"+serviceName;
            		//System.out.println("DB URL 2="+db_url);
                	DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                    conn = DriverManager.getConnection(db_url, db_login, db_pwd);

            	}
            }
          //  System.out.println("db URL="+db_url);
          //  System.out.println("DATABASE="+DATABASE);
            
            }
        catch (Exception e){
            e.printStackTrace();
        }
    }*/

/*    public Connection getConnection() {
        return conn;   //return connection
    }

    public ResultSet executeQuery(String queryString, boolean setAsUpdate) {

        Statement stmt;
        ResultSet result = null;

        try {
            // get updatable resultset if true
            if(setAsUpdate){
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            }
            else {
            	stmt = conn.createStatement();
            }

            result = stmt.executeQuery(queryString); //execute query
        }
        catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }

        return result;

    }

    public int executeUpdate(String queryString) {

        try {

            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(queryString);  //update database
        }
        catch(SQLException sqlex) {
            sqlex.printStackTrace();
        }
        return 0;
    }

    public void closeConnection() {
        try {
            conn.close();  //close connection
        }
        catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }*/

}