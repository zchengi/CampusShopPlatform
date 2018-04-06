package com.cheng.o2o.util;

import com.cheng.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /**
     * 处理缩略图，并返回新生成图片的相对路径
     *
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
        // 获取不重复的随机名
        String realFileName = FileUtil.getRandomFileName();
        // 获取文件的扩展名，如:png，jpg等
        String extension = getFileExtension(thumbnail.getImageName());
        // 如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        // 获取文件要保存的目标路径
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is : ", relativeAddr);
        // 获取文件要保存到的目录路径
        File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is : ", FileUtil.getImgBasePath() + realFileName);
        logger.debug("basePath is : ", basePath);

        // 调用 Thumbnails 生成带有水印的图片
        try {
            Thumbnails.of(thumbnail.getImage())
                    .size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "img/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f)
                    .toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败，" + e.toString());
        }

        // 返回图片相对路径地址
        return relativeAddr;
    }

    /**
     * 处理详情图，并返回新生成图片的相对路径
     *
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
        // 获取不重复的随机名
        String realFileName = FileUtil.getRandomFileName();
        // 获取文件的扩展名，如:png，jpg等
        String extension = getFileExtension(thumbnail.getImageName());
        // 如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        // 获取文件要保存的目标路径
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is : ", relativeAddr);
        // 获取文件要保存到的目录路径
        File dest = new File(FileUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is : ", FileUtil.getImgBasePath() + realFileName);
        logger.debug("basePath is : ", basePath);

        // 调用 Thumbnails 生成带有水印的图片
        try {
            Thumbnails.of(thumbnail.getImage())
                    .size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "img/watermark.jpg")), 0.25f)
                    .outputQuality(0.f)
                    .toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败，" + e.toString());
        }

        // 返回图片相对路径地址
        return relativeAddr;
    }


    /**
     * 获取输入文件流的扩展名
     */
    private static String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
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

    /**
     * 删除 storePath
     * 如果是文件:删除该文件
     * 如果是目录:删除该目录下所有文件且删除目录
     *
     * @param storePath 文件的目录或路径
     */
    public static void deleteFileOrPath(String storePath) {

        File fileOrPath = new File(FileUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()) {
            if (fileOrPath.isDirectory()) {
                File[] files = fileOrPath.listFiles();
                for (File file : files) {
                    file.delete();
                }
            }
            fileOrPath.delete();
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
