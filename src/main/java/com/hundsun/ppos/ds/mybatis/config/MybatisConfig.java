package com.hundsun.ppos.ds.mybatis.config;

import com.alibaba.druid.util.JdbcConstants;
import com.hundsun.ppos.ds.mybatis.MybatisDaoSupport;
import com.hundsun.ppos.ds.mybatis.assist.MySqlInterceptor;
import com.hundsun.ppos.ds.mybatis.assist.OracleInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNullApi;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@ComponentScan(basePackages = "com.hundsun.ppos.ds.mybatis")
@PropertySource("classpath:/sysdb.properties")
public class MybatisConfig implements EnvironmentAware {

	final static Logger logger = LoggerFactory.getLogger(MybatisConfig.class);

	private Environment environment;
	
	@Bean(value="dataSource",initMethod="init",destroyMethod="close")
	public DruidDataSource dataSource() throws Exception {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(environment.getProperty("app.db.driverClassName"));
		druidDataSource.setUrl(environment.getProperty("app.db.url"));
		druidDataSource.setUsername(environment.getProperty("app.db.username"));
		druidDataSource.setPassword(environment.getProperty("app.db.password"));
		if(StringUtils.isNotBlank(environment.getProperty("app.db.filters"))){
			druidDataSource.setFilters(environment.getProperty("app.db.filters"));
		}
		if(StringUtils.isNotBlank(environment.getProperty("app.db.connectionProperties"))){
			druidDataSource.setConnectionProperties(environment.getProperty("app.db.connectionProperties"));
		}
		druidDataSource.setInitialSize(initialSize);
		druidDataSource.setMinIdle(minIdle);
		druidDataSource.setMaxActive(maxActive);
		druidDataSource.setMaxWait(maxWait);
		druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		druidDataSource.setValidationQuery(validationQuery);
		druidDataSource.setTestWhileIdle(testWhileIdle);
		druidDataSource.setTestOnBorrow(testOnBorrow);
		druidDataSource.setTestOnReturn(testOnReturn);
		druidDataSource.setRemoveAbandoned(removeAbandoned);
		druidDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
		druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		return druidDataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() throws Exception {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean(value="sqlSessionFactoryBean")
	public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean ssFactoryBean = new SqlSessionFactoryBean();
		//设置数据源
		ssFactoryBean.setDataSource(dataSource());
		org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
		//开启二级缓存
		mybatisConfig.setCacheEnabled(true);
		//则所有相关联的都会被初始化加载。
		mybatisConfig.setLazyLoadingEnabled(false);
		//每个属性都按需加载。
		mybatisConfig.setAggressiveLazyLoading(false);
		//默认查询超时间(根据ppos业务场景综合考虑放松到30秒)
		mybatisConfig.setDefaultStatementTimeout(30);
		mybatisConfig.setMapUnderscoreToCamelCase(true);
		mybatisConfig.setJdbcTypeForNull(JdbcType.NULL);
		mybatisConfig.setLogImpl(org.apache.ibatis.logging.log4j2.Log4j2Impl.class);
		ssFactoryBean.setConfiguration(mybatisConfig);
		//加载插件
		deduceMybatisInterceptors(ssFactoryBean);
		//加载配置文件
		assembleMapperLocations(ssFactoryBean);
		return ssFactoryBean;
	}

	private void assembleMapperLocations(SqlSessionFactoryBean ssFactoryBean) throws IOException {
		String[] strs = environment.getProperty("app.db.mapperLocations").split(",");
		Map<String, Resource> resMap = new HashMap<>();
		PathMatchingResourcePatternResolver resolver;
		for(String resStr : strs){
			resolver = new PathMatchingResourcePatternResolver();
			Resource[] resArry = resolver.getResources(resStr);
			for(Resource res : resArry){
				resMap.put(res.getFilename(),res);
			}
		}
		Resource[] resa = new Resource[resMap.size()];
		int resaCount = 0 ;
		for(String key : resMap.keySet()){
			resa[resaCount] = resMap.get(key);
			resaCount ++ ;
		}
		ssFactoryBean.setMapperLocations(resa);
	}

	@Bean(value="mybatisDaoSupport",initMethod="init")
	public MybatisDaoSupport mybatisDaoSupport() throws Exception {
		MybatisDaoSupport daoSupport = new MybatisDaoSupport();
		daoSupport.setSqlSessionFactory(sqlSessionFactoryBean().getObject());
		return daoSupport;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	private void deduceMybatisInterceptors(SqlSessionFactoryBean ssFactoryBean){
		Interceptor[] interceptors = new Interceptor[1];
		String driverName = environment.getProperty("app.db.driverClassName");
		if(StringUtils.isBlank(driverName)){
			throw new IllegalArgumentException("mybatis 数据源构建失败");
		}
		if(driverName.contains(JdbcConstants.ORACLE)){
			interceptors[0] = new OracleInterceptor();
		}else if(driverName.contains(JdbcConstants.MYSQL)){
			interceptors[0] = new MySqlInterceptor();
		}else{
			throw new IllegalStateException("分页插件构建失败");
		}
		ssFactoryBean.setPlugins(interceptors);
	}

	@Value("${sys.db.initialSize}")
	private int initialSize;

	@Value("${sys.db.minIdle}")
	private int minIdle;

	@Value("${sys.db.maxActive}")
	private int maxActive;

	@Value("${sys.db.maxWait}")
	private long maxWait;

	@Value("${sys.db.removeAbandoned}")
	private boolean removeAbandoned;

	@Value("${sys.db.removeAbandonedTimeout}")
	private int removeAbandonedTimeout;

	@Value("${sys.db.timeBetweenEvictionRunsMillis}")
	private long timeBetweenEvictionRunsMillis;

	@Value("${sys.db.minEvictableIdleTimeMillis}")
	private long minEvictableIdleTimeMillis;

	@Value("${sys.db.validationQuery}")
	private String validationQuery;

	@Value("${sys.db.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${sys.db.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${sys.db.testOnReturn}")
	private boolean testOnReturn;

	@Value("${sys.db.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	@Value("${sys.db.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;

}
