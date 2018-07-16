package dk.sds.sts.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {
	
	@Value("${dataSource.driverClassName}")
	private String driverClassName;
	
	@Value("${dataSource.password}")
	private String password;
	
	@Value("${dataSource.username}")
	private String username;
	
	@Value("${dataSource.url}")
	private String url;
	
	@Value("${dataSource.maxActive}")
	private int maxActive;
	
	@Value("${dataSource.maxIdle}")
	private int maxIdle;
	
	@Value("${dataSource.minIdle}")
	private int minIdle;

	@Value("${dataSource.initialSize}")
	private int initialSize;
	
	@Value("${dataSource.testWhileIdle}")
	private boolean testWhileIdle;
	
	@Value("${dataSource.validationQuery}")
	private String validationQuery;
	
	@Value("${dataSource.validationInterval}")
	private int validationInterval;
	
	@Value("${dataSource.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${dataSource.maxAge}")
	private int maxAge;
	
	@Bean
	public DataSource dataSource() {
		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();		
		
		dataSource.setDriverClassName(driverClassName);
		dataSource.setPassword(password);
		dataSource.setUsername(username);
		dataSource.setUrl(url);
		dataSource.setMaxActive(maxActive);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxIdle(maxIdle);
		dataSource.setInitialSize(initialSize);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setValidationInterval(validationInterval);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setMaxAge(maxAge);

		return dataSource;
	}
}
