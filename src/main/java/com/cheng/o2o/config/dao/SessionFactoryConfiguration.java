package com.cheng.o2o.config.dao;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author cheng
 *         2018/4/19 16:53
 */
@Configuration
public class SessionFactoryConfiguration {

    /**
     * mybatis-config.xml 配置文件的路径
     * static的参数 必须使用 set方法 赋值，且set方法取消 static
     */
    private static String mybatisConfigFile;
    /**
     * mybatis mapper 文件所在路径
     */
    private static String mapperPath;
    /**
     * 实体类所在的 package
     */
    @Value("${type_alias_package}")
    private String typeAliasPackage;

    @Autowired
    private DataSource dataSource;


    /**
     * 创建 sqlSessionFactoryBean 实例并且设置 configuration，设置 mapper 映射路径
     * 设置 datasource 数据源
     *
     * @return
     * @throws IOException
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置 mybatis configuration 扫描路径
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
        // 添加 mapper 扫描路径
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
        // 设置 datasource
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 设置 typeAlias 包扫描路径
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
        // 解决 别名扫描不到的问题
        sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
        return sqlSessionFactoryBean;
    }

    @Value("${mybatis_config_file}")
    public void setMybatisConfigFile(String mybatisConfigFile) {
        SessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
    }

    @Value("${mapper_path}")
    public void setMapperPath(String mapperPath) {
        SessionFactoryConfiguration.mapperPath = mapperPath;
    }
}
