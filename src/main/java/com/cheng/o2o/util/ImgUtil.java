package com.cheng.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理
 *
 * @author cheng
 *         2018/3/29 11:50
 */
public class ImgUtil {

    private static Logger logger = LoggerFactory.getLogger(ImgUtil.class);

    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    public static String generateThumbnail(File thumbnail, String targetAddr) {

        logger.debug("basePath is : {}", basePath);

        String realFileName = FileUtil.getRandomFileName();
        String extension = getFileExtension(thumbnail);
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is : {}", relativeAddr);

        File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is : {}", dest);

        try {
            Thumbnails.of(thumbnail)
                    .size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "img/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f)
                    .toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败：" + e.toString());
        }

        return relativeAddr;
    }

    /**
     * 获取输入文件流的扩展名
     */
    private static String getFileExtension(File cFile) {
        String originalFileName = cFile.getName();
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 创建目标路径所涉及到的目录,即 /home/work/cheng/xxx.jpg,
     * 则 home work cheng 三个文件夹都得自动创建
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = FileUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    public static void main(String[] args) throws IOException {

        // 这里或得的路径是当前线程运行时的根目录(路径中不能有空格等非法字符，下同)
        System.out.println(basePath);
        Thumbnails.of(new File(basePath + "img/002.png"))
                // 压缩后图片大小
                .size(318, 450)
                // 水印图片
                .watermark(Positions.BOTTOM_RIGHT,
                        ImageIO.read(new File(basePath + "img/watermark.jpg")), 0.25f)
                // 图片压缩比
                .outputQuality(0.8f)
                // 压缩后图片位置
                .toFile("src/main/resources/img/002New.png");
    }
}
