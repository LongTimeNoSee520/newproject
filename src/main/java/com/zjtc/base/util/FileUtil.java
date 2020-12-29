package com.zjtc.base.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuchen
 * @date 2020/8/19
 */
@Log4j
@Component
public class FileUtil {

  public void downloadTemplate(File file,String templatePath, String fileName,
      HttpServletRequest request,HttpServletResponse response) {
    BufferedInputStream bis;
    try {
      Map<String, Long> result =setResponseHeader(fileName, file.length(), request, response);
      long rangeSwitch = result.get("rangeSwitch");
      long from = result.get("from");
      long contentLength = result.get("contentLength");

      InputStream ins = getClass().getClassLoader()
          .getResourceAsStream(templatePath);
      bis = new BufferedInputStream(ins);

      // 输出流
      bis.skip(from);
      OutputStream out = response.getOutputStream();
      int read;
      long readLength = 0;
      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];
      if (rangeSwitch == 2) {
        // 写区间
        while (readLength <= contentLength - bufferSize) {
          read = bis.read(buffer);
          readLength += read;
          out.write(buffer, 0, read);
        }
        if (readLength < contentLength) {
          read = bis.read(buffer, 0, (int) (contentLength - readLength));
          out.write(buffer, 0, read);
        }
      } else {
        // 写（余下）全部
        while ((read = bis.read(buffer)) != -1) {
          out.write(buffer, 0, read);
        }
      }
      out.flush();
      out.close();
      bis.close();
    } catch (IOException e) {
      log.warn("下载文件发生错误" + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void saveAsFileWriter(String content,String filePath) {
    FileWriter fwriter = null;
    try {
      // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
      fwriter = new FileWriter(filePath, true);
      fwriter.write(content);
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      try {
        fwriter.flush();
        fwriter.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
  public static boolean writeBytes(File file, String fileName, HttpServletRequest request,
      HttpServletResponse response) {
    BufferedInputStream bis;
    boolean downloadSuccess = false;
    try {
      Map<String, Long> result = setResponseHeader(fileName, file.length(), request, response);
      long rangeSwitch = result.get("rangeSwitch");
      long from = result.get("from");
      long contentLength = result.get("contentLength");

      InputStream ins = new FileInputStream(file);
      bis = new BufferedInputStream(ins);

      // 输出流
      bis.skip(from);
      OutputStream out = response.getOutputStream();
      int read;
      long readLength = 0;
      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];
      if (rangeSwitch == 2) {
        // 写区间
        while (readLength <= contentLength - bufferSize) {
          read = bis.read(buffer);
          readLength += read;
          out.write(buffer, 0, read);
        }
        if (readLength < contentLength) {
          read = bis.read(buffer, 0, (int) (contentLength - readLength));
          out.write(buffer, 0, read);
        }
      } else {
        // 写（余下）全部
        while ((read = bis.read(buffer)) != -1) {
          out.write(buffer, 0, read);
        }
      }
      out.flush();
      out.close();
      bis.close();
      downloadSuccess = true;
    }catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      downloadSuccess = false;
      log.warn("下载文件发生错误" + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      downloadSuccess = false;
    }
    return downloadSuccess;
  }


  private static Map<String, Long> setResponseHeader(String fileName, long totalSize,
      HttpServletRequest request, HttpServletResponse response) {
    Map<String, Long> result = new HashMap<String, Long>();
    long rangeSwitch = 0;
    long contentLength;
    long from = 0;
    long to = totalSize - 1;

    String range = request.getHeader("Range");
    if (StringUtils.isNotBlank(range)) {
      response.setStatus(206);
      String rangBytes = range.split("=")[1];
      int splitIndex = rangBytes.indexOf("-");
      if (rangBytes.endsWith("-")) { // bytes=from-, 单线程断点续传
        rangeSwitch = 1;
        from = Long.valueOf(rangBytes.substring(0, splitIndex));
      } else { // bytes=from-to, 多线程下载
        rangeSwitch = 2;
        from = Long.valueOf(rangBytes.substring(0, splitIndex));
        to = Long.valueOf(rangBytes.substring(splitIndex + 1));
      }
    }

    contentLength = to - from + 1;

    response.setHeader("Accept-Ranges", "bytes");
    response.setHeader("Content-Length", String.valueOf(contentLength));
    // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
    response.setHeader("Content-Range",
        rangeSwitch == 2 ? (range.replace("=", " ") + "/" + totalSize) : ("bytes "
            + from + "-" + to + "/" + totalSize));
    response.setContentType("application/octet-stream");
    try {
      response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder
          .encode(fileName, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    result.put("rangeSwitch", rangeSwitch);
    result.put("contentLength", contentLength);
    result.put("from", from);
    result.put("to", to);

    return result;
  }


  /**
   * 创建文件夹
   */
  public static void createDir(String path) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
  }


  /**
   * 上传
   *
   * @param multipartFile 文件
   * @param saveFilePath 存储路径
   */
  public static boolean upload(MultipartFile multipartFile, String saveFilePath) {
    boolean result = false;
    if (null == multipartFile || StringUtils.isBlank(saveFilePath)) {
      return result;
    }
    File file = new File(saveFilePath);
    try {
      multipartFile.transferTo(file);
      result = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 合并指定目录下的所有 文件
   */
  public static void combineFiles(List<File> files, String dirPath, String saveFileName)
      throws IOException {

    if (files == null || files.size() < 1) {
      throw new RuntimeException("待合并的文件不存在...");
    }

    File outputFile = new File(dirPath + File.separator + saveFileName);

    if (!outputFile.exists()) {
      outputFile.createNewFile();
    }
    FileChannel outChannel = new FileOutputStream(outputFile).getChannel();
    FileChannel inChannel;
    for (File file : files) {

      inChannel = new FileInputStream(file).getChannel();
      inChannel.transferTo(0, inChannel.size(), outChannel);

      inChannel.close();
    }

    outChannel.close();

  }

  /**
   * 删除文件夹及文件夹下的所有文件
   */
  public static void deleteDir(String dirPath) {
    File file = new File(dirPath);
    if (file.isFile()) {
      file.delete();
    } else {
      File[] files = file.listFiles();
      if (files == null) {
        file.delete();
      } else {
        for (int i = 0; i < files.length; i++) {
          deleteDir(files[i].getAbsolutePath());
        }
        file.delete();
      }
    }
  }

  public static void main(String args[]) {
    String dir = "/Users/json/Downloads/upload/111";

  }

  /**
   * 流的方式下载
   */
  public static void writesToFlow(File file, String fileName, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    setResponseHeader(fileName, file.length(), request, response);
    //读取路径下面的文件
    //读取指定路径下面的文件
    InputStream in = new FileInputStream(file);
    OutputStream outputStream = response.getOutputStream();
    //创建存放文件内容的数组
    byte[] buff = new byte[1024];
    //所读取的内容使用n来接收
    int n;
    //当没有读取完时,继续读取,循环
    while ((n = in.read(buff)) != -1) {
      //将字节数组的数据全部写入到输出流中
      outputStream.write(buff, 0, n);
    }
    //强制将缓存区的数据进行输出
    outputStream.flush();
    //关流
    outputStream.close();
    in.close();
  }



  /**
   * 设置响应参数
   */
  private static OutputStream setDownloadOutputStream(HttpServletResponse response, String fileName,
      String fileType) throws IOException {

    fileName = new String(fileName.getBytes(), "ISO-8859-1");
    response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "." + fileType);
    response.setContentType("multipart/form-data");
    return response.getOutputStream();

  }

  /**
   * 将byte[]类型的数据，写入到输出流中
   *
   * @param out 输出流
   * @param data 希望写入的数据
   * @param cacheSize 写入数据是循环读取写入的，此为每次读取的大小，单位字节，建议为4096，即4k
   */
  private static void writeBytesToOut(OutputStream out, byte[] data, int cacheSize)
      throws IOException {
    int surplus = data.length % cacheSize;
    int count = surplus == 0 ? data.length / cacheSize : data.length / cacheSize + 1;

    for (int i = 0; i < count; i++) {
      if (i == count - 1 && surplus != 0) {
        out.write(data, i * cacheSize, surplus);
        continue;
      }
      out.write(data, i * cacheSize, cacheSize);
    }
  }

  /**
   * 获得指定文件的byte数组
   */
  private static byte[] getBytes(String filePath) {
    byte[] buffer = null;
    try {
      File file = new File(filePath);
      FileInputStream fis = new FileInputStream(file);
      ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
      byte[] b = new byte[1000];
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


  /**
   * 获取文件类型
   * @param fileName
   * @return
   */
  public static String getFileType(String fileName) {
    if (StringUtils.isNotBlank(fileName) && fileName.indexOf(".") > 0) {
      return fileName.substring(fileName.lastIndexOf("."));
    }
    return null;
  }

}