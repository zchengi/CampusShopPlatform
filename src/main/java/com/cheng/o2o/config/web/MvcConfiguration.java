package com.cheng.o2o.config.web;

import com.cheng.o2o.interceptor.controller.ShopLoginInterceptor;
import com.cheng.o2o.interceptor.controller.ShopPermissionInterceptor;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 开启 mvc，自动注入；配置视图解析器
 *
 * @author cheng
 *         2018/4/19 21:12
 */
@Configuration
@EnableWebMvc // 等价于 <mvc:annotation-driven/>
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    @Value("${kaptcha.border}")
    private String border;
    @Value("${kaptcha.textproducer.char.string}")
    private String str;
    @Value("${kaptcha.textproducer.char.length}")
    private String length;
    @Value("${kaptcha.textproducer.font.names}")
    private String names;
    @Value("${kaptcha.textproducer.font.color}")
    private String fontColor;
    @Value("${kaptcha.textproducer.font.size}")
    private String size;
    @Value("${kaptcha.noise.color}")
    private String noiseColor;
    @Value("${kaptcha.image.width}")
    private String width;
    @Value("${kaptcha.image.height}")
    private String height;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取 spring 容器
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 静态资源配置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");

        // 添加解析本地文件 windows / linux
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:D:/IntelliJProject/image/upload/");
    }

    /**
     * 定义默认的请求处理器
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * 创建 viewResolver
     *
     * @return
     */
    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        // 设置 spring 容器
        viewResolver.setApplicationContext(this.applicationContext);
        // 取消缓存
        viewResolver.setCache(false);
        // 设置解析的前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        // 设置解析的后缀
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    /**
     * 文件上传解析器
     *
     * @return
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        // 1024 * 1024 * 20 = 20M
        multipartResolver.setMaxUploadSize(20971525);
        multipartResolver.setMaxInMemorySize(20971520);

        return multipartResolver;
    }

    /**
     * 由于 web.xml 不生效了，需要在这里配置 Kaptcha 验证码 servlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/Kaptcha");
        servlet.addInitParameter("kaptcha.border", border);
        servlet.addInitParameter("kaptcha.textproducer.char.string", str);
        servlet.addInitParameter("kaptcha.textproducer.char.length", length);
        servlet.addInitParameter("kaptcha.textproducer.font.names", names);
        servlet.addInitParameter("kaptcha.textproducer.font.color", fontColor);
        servlet.addInitParameter("kaptcha.textproducer.font.size", size);
        servlet.addInitParameter("kaptcha.noise.color", noiseColor);
        servlet.addInitParameter("kaptcha.image.width", width);
        servlet.addInitParameter("kaptcha.image.height", height);

        return servlet;
    }

    /**
     * 添加拦截器配置
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String LoginInterceptPath = "/shopadmin/**";
        // 注册拦截器  校验是否已登录了店家管理系统
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        // 配置拦截的路径
        loginIR.addPathPatterns(LoginInterceptPath);

        String doInterceptPath = "/shop/**";
        // 注册第二个拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        // 配置拦截的路径  校验是否对该店铺有操作权限的拦截器
        permissionIR.addPathPatterns(doInterceptPath);

        // 配置不拦截的路径

        // shoplist page
        permissionIR.excludePathPatterns("/shop/shoplist");
        permissionIR.excludePathPatterns("/shop/getshoplist");
        // shopregister page
        permissionIR.excludePathPatterns("/shop/getshopinitinfo");
        permissionIR.excludePathPatterns("/shop/registershop");
        permissionIR.excludePathPatterns("/shop/shopoperation");
        // shopmanage page
        permissionIR.excludePathPatterns("/shop/shopmanagement");
        permissionIR.excludePathPatterns("/shop/getshopmanagementinfo");
    }
}
