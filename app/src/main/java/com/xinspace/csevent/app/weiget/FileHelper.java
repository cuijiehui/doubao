package com.xinspace.csevent.app.weiget;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

import com.xinspace.csevent.app.CoresunApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件帮助类
 *
 */
public class FileHelper {

    public static boolean judgeFilePath() {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }

    }

    public static File getOrignialStorageFile() {
        File file;
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Shide" + "/" + ".data" + "/");
        return file;
    }

    public static File getStorageFile() {
        File file;
        file = CoresunApp.context.getFilesDir();
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    // 创建新文件夹
    public static File createNewFile(String fileName) {
        File file = null;
        File storageFile = getStorageFile();
        if (storageFile != null) {
            file = new File(storageFile + File.separator + fileName + File.separator);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getCacheFile() {
        File file;
        file = CoresunApp.context.getCacheDir();
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getDataCacheFile() {
        File file;
        File storageFile = getStorageFile();
        if (storageFile != null) {
            file = new File(storageFile + "/" + "Shide" + "/" + ".data" + "/" + "datacache" + "/");
        } else {
            file = CoresunApp.context.getCacheDir();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getShowOFile() {
        File file;

        File storageFile = getStorageFile();

        if (storageFile != null) {
            file = new File(storageFile + "/" + "Shide" + "/" + ".data" + "/");
        } else {
            file = new File(CoresunApp.context.getFilesDir() + "/" + "ShowO" + "/" + ".data" + "/");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getDataFile() {
        File file;
        File storageFile = getStorageFile();
        if (storageFile != null) {
            file = new File(storageFile + "/" + "Shide" + "/" + ".data" + "/");
        } else {
            file = new File(CoresunApp.context.getFilesDir() + "/" + "ShowO" + "/" + ".data" + "/");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getImgTypeFile(String type) {
        File dataFile = getDataFile();
        File file = new File(dataFile + "/" + "img" + "/" + type + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getModelFile() {
        File dataFile = getDataFile();
        File file = new File(dataFile + "/" + "modle" + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getSaveFile() {
        File file;
        file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "Shide" + "/" + "你真漂亮" + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            }
        }
    }

    public static void deleteFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(to);
            to.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File innerFile : files) {
                    deleteFile(innerFile);
                }
            }
            final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(to);
            to.delete();
        }
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 99, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 图片裁剪的 处理
    public static void writeImage(Bitmap bitmap, String destPath, int quality) {
        try {
            FileHelper.deleteFile(destPath);
            if (FileHelper.createFile(destPath)) {
                FileOutputStream out = new FileOutputStream(destPath);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                    out.flush();
                    out.close();
                    out = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个文件，创建成功返回true
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static byte[] File2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    ///////////////////////复制文件//////////////////////////////

    /**
     * 移动一个文件
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws IOException
     */

    public static boolean moveFileTo(File srcFile, File destFile) throws IOException {

        boolean iscopy = copyFileTo(srcFile, destFile);

        if (!iscopy)

            return false;

        delFile(srcFile);

        return true;

    }

    /**
     * 拷贝一个文件,srcFile源文件，destFile目标文件
     *
     * @throws IOException
     */

    public static boolean copyFileTo(File srcFile, File destFile) throws IOException {

        if (srcFile.isDirectory() || destFile.isDirectory())

            return false;// 判断是否是文件

        FileInputStream fis = new FileInputStream(srcFile);

        FileOutputStream fos = new FileOutputStream(destFile);

        int readLen = 0;

        byte[] buf = new byte[1024];

        while ((readLen = fis.read(buf)) != -1) {

            fos.write(buf, 0, readLen);

        }

        fos.flush();

        fos.close();

        fis.close();

        return true;

    }

    /**
     * 拷贝目录下的所有文件到指定目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */

    public static boolean copyFilesTo(File srcDir, File destDir) throws IOException {

        if (!srcDir.isDirectory() || !destDir.isDirectory())

            return false;// 判断是否是目录

        if (!destDir.exists())

            return false;// 判断目标目录是否存在

        File[] srcFiles = srcDir.listFiles();

        for (int i = 0; i < srcFiles.length; i++) {

            if (srcFiles[i].isFile()) {

                // 获得目标文件

                File destFile = new File(destDir.getPath() + "/"

                        + srcFiles[i].getName());

                copyFileTo(srcFiles[i], destFile);

            } else if (srcFiles[i].isDirectory()) {

                File theDestDir = new File(destDir.getPath() + "/"

                        + srcFiles[i].getName());

                copyFilesTo(srcFiles[i], theDestDir);

            }

        }

        return true;

    }

    /**
     * 移动目录下的所有文件到指定目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */

    public static boolean moveFilesTo(File srcDir, File destDir) throws IOException {
        if (!srcDir.isDirectory() || !destDir.isDirectory()) {
            return false;
        }

        File[] srcDirFiles = srcDir.listFiles();

        for (int i = 0; i < srcDirFiles.length; i++) {

            if (srcDirFiles[i].isDirectory()) {

                File oneDestFile = new File(destDir.getPath() + "/"
                        + srcDirFiles[i].getName());
                if (!oneDestFile.exists()) oneDestFile.mkdirs();
//                LogManger.i("hyn", srcDirFiles[i].getAbsolutePath() + "       isDirectory  move to       " + oneDestFile.getAbsolutePath() + "     " + oneDestFile.isDirectory());
                moveFilesTo(srcDirFiles[i], oneDestFile);

                delDir(srcDirFiles[i]);

            } else {

                File oneDestFile = new File(destDir.getPath() + "/"
                        + srcDirFiles[i].getName());
//                LogManger.i("hyn", srcDirFiles[i].getAbsolutePath() + "     move to    " + oneDestFile.getAbsolutePath() + "     " + oneDestFile.isDirectory());
                moveFileTo(srcDirFiles[i], oneDestFile);

                delFile(srcDirFiles[i]);

            }
        }
        return true;
    }

    /**
     * 删除一个文件
     *
     * @param file
     * @return
     */

    public static boolean delFile(File file) {

        if (file.isDirectory())

            return false;

        return file.delete();

    }

    /**
     * 删除一个目录（可以是非空目录）
     *
     * @param dir
     */

    public static boolean delDir(File dir) {

        if (dir == null || !dir.exists() || dir.isFile()) {

            return false;

        }

        for (File file : dir.listFiles()) {

            if (file.isFile()) {

                file.delete();

            } else if (file.isDirectory()) {

                delDir(file);// 递归

            }

        }

        dir.delete();

        return true;

    }

}
