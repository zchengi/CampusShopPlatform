package com.cheng.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 根据运行环境的不同提供不同的路径
 *
 * @author cheng
 *         2018/3/29 12:37
 */
@Configuration
public class FileUtil {

    private static String separator = System.getProperty("file.separator");

    private static String winPath;

    private static String linuxPath;

    private static String shopPath;

    /**
     * 使用 DateTimeFormatter(java8) 代替 SimpleDateFormat (因为后者是线程不安全的)
     * private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random RANDOM = new Random();

    private static final String WIN = "win";

    /**
     * 返回图片根路径
     */
    public static String getImgBasePath() {

        String os = System.getProperty("os.name");
        String basePath;

        if (os.toLowerCase().startsWith(WIN)) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("/", separator);

        return basePath;
    }

    /**
     * 返回项目图片相对子路径
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = shopPath + shopId + separator;
        return imagePath.replace("/", separator);
    }

    /**
     * 生成随机文件名:当前年月日小时分钟秒钟 + 五位随机数
     * (为了在实际项目中防止文件同名而进行的处理)
     *
     * @return 文件名
     */
    public static String getRandomFileName() {
        // 获取随机的五位数
        int randNum = RANDOM.nextInt(89999) + 10000;
        String nowTimeStr = DATE_TIME_FORMATTER.format(LocalDateTime.now());
        return nowTimeStr + randNum;
    }

    @Value("${win.base.path}")
    public void setWinPath(String winPath) {
        FileUtil.winPath = winPath;
    }

    @Value("${linux.base.path}")
    public void setLinuxPath(String linuxPath) {
        FileUtil.linuxPath = linuxPath;
    }

    @Value("${shop.relevant.path}")
    public void setShopPath(String shopPath) {
        FileUtil.shopPath = shopPath;
    }
}
