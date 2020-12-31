package com.zjtc.service.impl;



import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zjtc.base.response.ApiResponse;
import com.zjtc.base.util.FileUtil;
import com.zjtc.base.util.RedisUtil;
import com.zjtc.base.util.TimeUtil;
import com.zjtc.mapper.WaterQuantityManageMapper;
import com.zjtc.model.ImportLog;
import com.zjtc.model.User;
import com.zjtc.model.WaterUseData;
import com.zjtc.model.vo.WaterUseDataVO;
import com.zjtc.service.FileService;
import com.zjtc.service.ImportLogService;
import com.zjtc.service.UseWaterUnitMeterService;
import com.zjtc.service.WaterQuantityManageService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lianghao
 * @date 2020/12/25
 */

@Service
@Slf4j
public class WaterQuantityManageServiceImpl extends ServiceImpl<WaterQuantityManageMapper, WaterUseData> implements
		WaterQuantityManageService {

  /**
   * 附件上传盘符
   */
  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;

  /**
   * 附件上传目录
   */
  @Value("${file.fileUploadPath}")
  private String fileUploadPath;

  @Value("${file.importErrorFilePath}")
  private String importErrorFilePath;

  @Autowired
  private FileUtil fileUtil;

  @Autowired
  private RedisUtil redisUtil;


  @Autowired
  private ImportLogService importLogService;

  @Autowired
  private UseWaterUnitMeterService useWaterUnitMeterService;

  @Autowired
  private FileService fileService;

  /**
   * 错误文件信息
   */
  private static StringBuffer errorMsgs = null;


	@Override
	public Map<String, Object> queryPage(User user, JSONObject jsonObject) {
    int current = jsonObject.getInteger("current");//当前页
    int size = jsonObject.getInteger("size");//每页条数
    String nodeCode = user.getNodeCode();//节点编码
    String unitName = jsonObject.getString("unitName");//单位名称
    String waterMeterCode = jsonObject.getString("waterMeterCode");//水表档案号
    Float waterNumberStart =jsonObject.getFloat("waterNumberStart");//水量区间起始数
    Float waterNumberEnd =jsonObject.getFloat("waterNumberEnd");//水量区间结束数
    Float priceStart =jsonObject.getFloat("priceStart");//价格区间起始数
    Float priceEnd =jsonObject.getFloat("priceEnd");//价格区间结束数
    Integer caliberStart = jsonObject.getInteger("caliberStart");//口径区间起始数
    Integer caliberEnd = jsonObject.getInteger("caliberEnd");//口径区间结束数
    Integer sectorStart = jsonObject.getInteger("sectorStart");//区段区间开始数
    Integer sectorEnd = jsonObject.getInteger("sectorEnd");//区段区间结束数
    Integer useYear = jsonObject.getInteger("useYear");//年份

    Map<String, Object> map = new HashMap();
    map.put("current", current);
    map.put("size", size);
    map.put("nodeCode", nodeCode);
    if (StringUtils.isNotBlank(unitName)){
    map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(waterMeterCode)){
      map.put("waterMeterCode", waterMeterCode);
    }
    if (null != waterNumberStart){
      map.put("waterNumberStart", waterNumberStart);
    }
    if (null != waterNumberEnd){
      map.put("waterNumberEnd", waterNumberEnd);
    }
    if (null != priceStart){
      map.put("priceStart", priceStart);
    }
    if (null != priceEnd){
      map.put("priceEnd", priceEnd);
    }
    if (null != caliberStart){
      map.put("caliberStart", caliberStart);
    }
    if (null != caliberEnd){
      map.put("caliberEnd", caliberEnd);
    }
    if (null != sectorStart){
      map.put("sectorStart", sectorStart);
    }
    if (null != sectorStart){
      map.put("sectorEnd", sectorEnd);
    }
    if (null != useYear){
      map.put("useYear", useYear);
    }


    /**查出满足条件的共有多少条*/
    int num = this.baseMapper.queryNum(map);
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("total", num);//满足条件的总条数
    result.put("size", size);//每页条数
    result.put("pages", (int) Math.ceil((double) num / size));//一共有多少页
    result.put("current", current);//当前页

    /**查出满足条件的数据*/
    List<Map<String,Object>> records = this.baseMapper.queryPage(map);
    result.put("records", records);
    return result;
	}

  @Override
  public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {

    final String fileName = "自来水公司原始资料导入模板.xlsx";
    String saveFilePath =
        fileUploadRootPath + File.separator + fileUploadPath + File.separator + fileName;
//    模板下载路径
    String templatePath = "template/WaterQuantityManageTemplate.xlsx";
//    输入流
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream(templatePath);
//    创建文件对象
    File file = new File(saveFilePath);
    try {
//      文件拷贝
      FileUtils.copyInputStreamToFile(inputStream, file);
    }catch (Exception e) {
      e.printStackTrace();
    }
    fileUtil.downloadTemplate(file, templatePath, fileName, request, response);
    file.delete();

  }

  @Override
  public boolean zoneUpload(MultipartFile file,String fileProcessId,int chunk) {
    //临时文件夹
    String tempFolder =
        fileUploadRootPath + fileUploadPath + java.io.File.separator + fileProcessId;
    FileUtil.createDir(tempFolder);
    String saveFilePath =
        tempFolder + java.io.File.separator + fileProcessId + "_" + chunk + ".tmp";
    //上传文件
    boolean result = FileUtil.upload(file, saveFilePath);
    return result;
  }

  @Override
  public boolean merge(String fileProcessId) {
    boolean result = false;
    java.io.File dir = new java.io.File(
        fileUploadRootPath + fileUploadPath + java.io.File.separator
            + fileProcessId);
    if (!dir.exists()) {
      return result;
    }
    List<java.io.File> files = new ArrayList<>();
    for (java.io.File file : dir.listFiles()) {
      if (file.getName().startsWith(fileProcessId + "_")) {
        files.add(file);
      }
    }
    try {
      //合并文件
      FileUtil.combineFiles(files, fileUploadRootPath + fileUploadPath,
          fileProcessId + "." + "xlsx");
      //删除临时文件夹
      FileUtil.deleteDir(dir.getPath());
      result = true;
    } catch (IOException e) {
      log.error("合并文件失败,errMsg======{}", e.getMessage());
    }
    return result;
  }

  @Override
  public ApiResponse checkAndInsertData(User user,String fileProcessId,String fileName) throws Exception {
	  ApiResponse response =new ApiResponse();
    Map beans = new HashMap<String, List>();
    List<WaterUseDataVO> infos = new ArrayList<>();
    beans.put("infos", infos);

    String xmlConfig = "template/xml/WaterQuantityManage.xml";
    Map result = new HashMap();
    String fileRealPath = "C:\\Users\\LH\\Desktop\\WaterQuantityManageTemplate.xlsx";
        //fileUploadRootPath + fileUploadPath +java.io.File.separator+ fileProcessId + ".xlsx";
    //"C:\\Users\\LH\\Desktop\\导入测试.xlsx";
    //WaterQuantityManageTemplate.xlsx;
    errorMsgs = new StringBuffer();//错误信息
    String nodeCode = user.getNodeCode();
    /**日志*/
    ImportLog importLog =new ImportLog();
    importLog.setId(UUID.randomUUID().toString().replace("-", ""));
    importLog.setImportFileName(fileName);
    importLog.setImportTime(new Date());
    importLog.setNodeCode(nodeCode);
    /**记录附件信息*/
    com.zjtc.model.File file1 = new com.zjtc.model.File();//上传的excel信息
    file1.setDeleted("0");
    file1.setBusinessId(importLog.getId());
    file1.setCreaterId(user.getId());
    file1.setCreateTime(new Date());
    file1.setFileName(fileProcessId + ".xlsx");
    file1.setNodeCode(user.getNodeCode());
    file1.setFilePath(fileUploadPath +java.io.File.separator+ fileProcessId + ".xlsx");
    List<com.zjtc.model.File> files = new ArrayList<>();
    files.add(file1);
    /**excel数据解析写入bean*/
    result = this.importExcel(beans, xmlConfig, fileRealPath, fileName, nodeCode, true);
    infos = (List<WaterUseDataVO>) result.get("infos");
    /**计算发送webSocket消息的时机(按比例一共推10次给前端)*/
    int size = (int) Math.ceil((double) infos.size() / 10);
    List<Integer> sendSocketIndex = new LinkedList<>();
    /**将要发送的下标有序存入list*/
    for (int i =1; i<10; i++){
      sendSocketIndex.add(i*size);
    }
    /**解析完后,逐条检查数据，检查是否存在数据格式问题，和相同水表档案号在相同月份是否有多条数据*/
      /**查询所有水表号对应单位编号*/
    Map<String, String> meterMap = useWaterUnitMeterService.getMeterMap(nodeCode);
    List<WaterUseData> waterUseDataList = new ArrayList<>();
    /**existMap用于判断相同年份+月份是否已经有该水表档案号的信息,
     * key:水表档案号+年份+月份
     * value：保存重复数据出现在excel中的第几行，有几条相同的就把他们出现的位置存入list*/
    Map<String,List<Integer>> existMap = new HashMap<>();
    for (int i = 1; i <= infos.size(); i++) {
      if (sendSocketIndex.contains(i)){
        double percent = (double)(sendSocketIndex.indexOf(i)+1)/10;
        /**TODO 将完成百分比通过webSocket推送给前端(最后一次在接口代码最后发送)*/
      }

      WaterUseDataVO waterUseDataVO = infos.get(i - 1);
      WaterUseData waterUseData = new WaterUseData();
      waterUseData.setId(UUID.randomUUID().toString().replace("-", ""));
      waterUseData.setNodeCode(nodeCode);
      waterUseData.setUseWaterUnitId(meterMap.get(waterUseDataVO.getWaterMeterCode()));
      waterUseData.setUnitCode(waterUseDataVO.getWaterMeterCode());//水表档案号作原始数据的单位编号
      waterUseData.setUnitNames(waterUseDataVO.getUserName());
      waterUseData.setUnitAddress(waterUseDataVO.getAddress());
      waterUseData.setWaterMeterCode(waterUseDataVO.getWaterMeterCode());
      waterUseData.setWaterUseKinds(waterUseDataVO.getWaterUseKinds());
      waterUseData.setSector(waterUseDataVO.getSector());
      /**调用formatVoToBean方法,每个格式转换的地方都调用该方法一次，
       * 这样不会在第一个异常时就停止执行检查数据。
       * 可以把导入的文件所有不满足格式的数据都找到，
       * 并记录到errorMsgs，写入文件*/
      formatVoToBean(waterUseDataVO,waterUseData,"date",i);
      formatVoToBean(waterUseDataVO,waterUseData,"caliberInt",i);
      formatVoToBean(waterUseDataVO,waterUseData,"waterBeginFloat",i);
      formatVoToBean(waterUseDataVO,waterUseData,"waterEndFloat",i);
      formatVoToBean(waterUseDataVO,waterUseData,"waterNumberFloat",i);
      formatVoToBean(waterUseDataVO,waterUseData,"priceFloat",i);
      /**判断相同年份+月份是否已经有该水表档案号的信息*/
      String meterCodeAndYearMonth =
          waterUseData.getWaterMeterCode() + waterUseData.getUseYear() + waterUseData.getUseMonth();
       if(null != existMap.get(meterCodeAndYearMonth)){
         List<Integer> dataIndex =  existMap.get(meterCodeAndYearMonth);
         StringJoiner stringJoiner = new StringJoiner(",", "水表档案号"+waterUseData.getWaterMeterCode()+"在",
             "行是相同月份的数据，同一个水表号在相同的月份不能有多条数据");
         for (Integer integer :dataIndex){
             stringJoiner.add(integer+"");//将excel前面出现位置拼接
         }
         stringJoiner.add(i+"");//将本条数据的位置也拼接
         errorMsgs.append("\n"+stringJoiner.toString());
       }
      if (null != waterUseData){
        /**记录existMap*/
        List<Integer> existIndex = new ArrayList<>();
        if (null != existMap.get(meterCodeAndYearMonth)){
         existIndex = existMap.get(meterCodeAndYearMonth);
         existIndex.add(Integer.valueOf(i));
        }else {
         existIndex.add(Integer.valueOf(i));
        }
        existMap.put(meterCodeAndYearMonth,existIndex);
        waterUseDataList.add(waterUseData);
      }
    }
    if (errorMsgs.length()>0){
      /**将所有错误写到txt文件*/
      String filePath =fileUploadRootPath + importErrorFilePath + java.io.File.separator + TimeUtil
          .formatTimeyyyyMMddHHmmss(new Date()) + ".txt";
      fileUtil.saveAsFileWriter(errorMsgs.toString(), filePath);
      /**记录日志*/
      importLog.setImportStatus("0");
//      importLog.setImportDetail(filePath);
      com.zjtc.model.File file2 = new com.zjtc.model.File();//错误信息txt文件
      file2.setDeleted("0");
      file2.setBusinessId(importLog.getId());
      file2.setCreaterId(user.getId());
      file2.setCreateTime(new Date());
      file2.setFileName(filePath.substring(15,filePath.length()));
      file2.setNodeCode(user.getNodeCode());
      file2.setFilePath(filePath.substring(3,filePath.length()));
      files.add(file2);
      fileService.insertBatch(files);
      response.recordError("本次导入数据存在错误，请查看错误日志文件");
    }else {
      /**数据插入数据库,有则更新，无则新增*/
      for (WaterUseData waterUseData:waterUseDataList) {
        this.baseMapper.insertOrUpdate(waterUseData);
      }
      /**日志*/
      importLog.setImportStatus("1");
      /**附件信息*/
      fileService.insertBatch(files);
    }
    /**日志写入数据库*/
    importLogService.add(importLog);
    /**TODO 将完成百分比(100%)通过webSocket推送给前端(最后一次)*/
    return response;
  }

  @Override
  public void importEnd() {
	 Integer year =Integer.parseInt(TimeUtil.formatTimeStr(new Date()).substring(0,4)) ;
    this.baseMapper.insertOrUpdateToMonthData(year);
  }

  /**解析excel数据到bean*/
  public  Map<String, List> importExcel(Map<String, List> beans, String xmlConfig,
      String fileRealPath, String uploadFileName,String nodeCode,boolean isThrowException) throws Exception {
    File file = new File(fileRealPath);
    String fileName = file.getName();
    InputStream inputXLS = null;
    InputStream inputXML = null;
    try {
      // 文件流
      inputXLS = new FileInputStream(file);
      // xml配置文件流
      Resource resource = new ClassPathResource(xmlConfig);
      inputXML = getClass().getClassLoader()
          .getResourceAsStream(xmlConfig);//非静态方法可以用此方法获取xml配置文件流
      // 执行解析
      XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
      //按照xml中的配置将数据从文件中读入beens中对应key的value中
      XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
      if (readStatus.isStatusOK()) {
        log.debug("读取excel文件成功: 【{}】", fileName);

      }
    } catch (Exception e) {
      handleException(e, isThrowException,uploadFileName,nodeCode);
    } finally {
      try {
        if (inputXLS != null) {
          inputXLS.close();
        }
        if (inputXML != null) {
          inputXML.close();
        }
      } catch (IOException e) {
        log.error("parse excel error : 【{}】", e.getMessage());
      }
    }
    return beans;
  }

  /**
   * 处理异常
   *
   * @param e: 异常
   * @param isThrowException: 是否抛出异常
   * @param uploadFileName: 文件上传时的名字
   */
  private  void handleException(Exception e, boolean isThrowException, String uploadFileName,String nodeCode)
      throws Exception {
    // ① 记录错误位置
    String errorCell = e.getMessage().split(" ")[3];
    // ② 记录错误原因
    String errorMsg = e.getCause().toString();
    String[] causeMsgArray = errorMsg.split(":");
    errorMsg = errorMsg.substring(causeMsgArray[0].length() + 2).split(":")[0];
    switch (errorMsg) {
      case "For input string":
        errorMsg = "格式不正确(时间)";
        break;
      case "Error converting from 'String' to 'Integer' For input string":
        errorMsg = "请填写数字类型";
        break;
      default:
        break;
    }
    errorMsg = "读取" + uploadFileName + "文件异常: " + errorCell + errorMsg;
    if (isThrowException) {
      throw new Exception(errorMsg);
    } else {
      log.error(errorMsg);
    }
  }

  private WaterUseData formatVoToBean(WaterUseDataVO waterUseDataVO,WaterUseData importWaterUseData,String formatType,int i){
    switch (formatType) {
      case "date":
        try {
          Integer year = Integer.parseInt(
              TimeUtil.formatTimeStr(TimeUtil.excelformatDate(waterUseDataVO.getDate()))
                  .substring(0, 4));
          importWaterUseData.setUseYear(year);
          Integer month = Integer.parseInt(
              TimeUtil.formatTimeStr(TimeUtil.excelformatDate(waterUseDataVO.getDate()))
                  .substring(5, 7));
          importWaterUseData.setUseMonth(month);
        } catch (Exception e) {
          errorMsgs.append("\n第" + i + "行,日期填写错误");
          importWaterUseData = null;
        }
        break;
      case "caliberInt":
        try {
          Integer caliber = Integer.parseInt(waterUseDataVO.getCaliber());
          importWaterUseData.setCaliber(caliber);
        } catch (Exception e) {
          errorMsgs.append("\n第"+ i+ "行口径格式填写错误");
          importWaterUseData = null;
        }
        break;
      case "waterBeginFloat":
        try {
          Float waterBegin = Float.parseFloat(waterUseDataVO.getWaterBegin());
          importWaterUseData.setWaterBegin(waterBegin);
        } catch (Exception e) {
          errorMsgs.append("\n第"+ i+ "行,起度格式填写错误");
          importWaterUseData = null;
        }
        break;
      case "waterEndFloat":
        try {
          Float waterEnd = Float.parseFloat(waterUseDataVO.getWaterEnd());
          importWaterUseData.setWaterEnd(waterEnd);
        } catch (Exception e) {
          errorMsgs.append("\n第"+ i+ ",止度格式填写错误");
          importWaterUseData = null;
        }
        break;
      case "waterNumberFloat":
        try {
          Float waterNumber = Float.parseFloat(waterUseDataVO.getWaterNumber());
          importWaterUseData.setWaterNumber(waterNumber);
        } catch (Exception e) {
          errorMsgs.append("\n第"+ i+ ",水量格式填写错误");
          importWaterUseData = null;
        }
        break;
      case "priceFloat":
        try {
          Float price = Float.parseFloat(waterUseDataVO.getPrice());
          importWaterUseData.setPrice(price);
        } catch (Exception e) {
          errorMsgs.append("\n第"+ i+ ",单价格式填写错误");
          importWaterUseData = null;
        }
        break;
    }
    return importWaterUseData;
  }
}