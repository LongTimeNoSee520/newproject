package com.zjtc.base.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
//import net.sourceforge.pinyin4j.PinyinHelper;
//import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
//import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class CommonUtil {

    public static String getLoginUserId() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession();
        Object sessionObject = session.getAttribute("user");
        if (sessionObject == null) {
            return "lichao81";
        } else {
            return sessionObject.toString();
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

            // ?????????
            bis.skip(from);
            OutputStream out = response.getOutputStream();
            int read;
            long readLength = 0;
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            if (rangeSwitch == 2) {
                // ?????????
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
                // ?????????????????????
                while ((read = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }
            out.flush();
            out.close();
            bis.close();
            downloadSuccess = true;
        } catch (IOException e) {
            downloadSuccess = false;
            log.warn("????????????????????????" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            downloadSuccess = false;
        }
        return downloadSuccess;
    }

    public static Map<String, Long> setResponseHeader(String fileName, long totalSize,
                                                      HttpServletRequest request, HttpServletResponse response) {
        Map<String, Long> result = new HashMap<String, Long>();
        long rangeSwitch = 0;
        long contentLength;
        long from = 0;
        long to = totalSize - 1;

        String range = request.getHeader("Range");
        if (StringUtils.isNotBlank(range)) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String rangBytes = range.split("=")[1];
            int splitIndex = rangBytes.indexOf("-");
            if (rangBytes.endsWith("-")) { // bytes=from-, ?????????????????????
                rangeSwitch = 1;
                from = Long.valueOf(rangBytes.substring(0, splitIndex));
            } else { // bytes=from-to, ???????????????
                rangeSwitch = 2;
                from = Long.valueOf(rangBytes.substring(0, splitIndex));
                to = Long.valueOf(rangBytes.substring(splitIndex + 1));
            }
        }

        contentLength = to - from + 1;

        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Length", String.valueOf(contentLength));
        // Content-Range: bytes [????????????????????????]-[?????????????????? - 1]/[??????????????????]
        response.setHeader("Content-Range",
                rangeSwitch == 2 ? (range.replace("=", " ") + "/" + totalSize) : ("bytes "
                        + from + "-" + to + "/" + totalSize));
        response.setContentType("application/octet-stream");

        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder
                    .encode(fileName, "UTF-8"));
//            response.addHeader("Content-Disposition", URLEncoder
//                    .encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Expose-Headers", "Authorization,Content-Disposition");
            
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
     * ???map?????????javabean??????
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }


    public static void writes(File file, String fileName, HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        setResponseHeader(fileName, file.length(), request, response);

        //???????????????????????????
        //?????????????????????????????????
        InputStream in = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        //?????????????????????????????????
        byte[] buff = new byte[1024];
        //????????????????????????n?????????
        int n;
        //?????????????????????,????????????,??????
        while ((n = in.read(buff)) != -1) {
            //???????????????????????????????????????????????????
            outputStream.write(buff, 0, n);
        }
        //???????????????????????????????????????
        outputStream.flush();
        //??????
        outputStream.close();
        in.close();
    }

//    public static String toPinYin(String chineseLan) {
//        String ret = "";
//        // ??????????????????????????????
//        char[] charChineseLan = chineseLan.toCharArray();
//        // ??????????????????
//        HanyuPinyinOutputFormat hpFormat = new HanyuPinyinOutputFormat();
//        // ??????????????????
//        hpFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        // ?????????????????????
//        hpFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        try {
//            for (int i = 0; i < charChineseLan.length; i++) {
//                if (String.valueOf(charChineseLan[i]).matches("[\u4e00-\u9fa5]+")) {
//                    // ?????????????????????,??????????????????????????????????????????????????????????????????????????????
//                    ret += PinyinHelper.toHanyuPinyinStringArray(charChineseLan[i], hpFormat)[0].substring(0, 1);
//                } else {
//                    // ????????????????????????,????????????
//                    ret += charChineseLan[i];
//                }
//            }
//        } catch (BadHanyuPinyinOutputFormatCombination e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }

    public static Map<String, Object> convertBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // ??????class??????
                if (!key.equals("class")) {
                    // ??????property?????????getter??????
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null == value) {
                        map.put(key, "");
                    } else {
                        map.put(key, value);
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
