package com.zjtc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjtc.base.util.FileUtil;
import com.zjtc.mapper.waterBiz.WaterBalanceTestMapper;
import com.zjtc.model.File;
import com.zjtc.model.User;
import com.zjtc.model.WaterBalanceTest;
import com.zjtc.model.WaterBalanceTestProduct;
import com.zjtc.model.vo.FileVO;
import com.zjtc.model.vo.WaterBalanceTestProductVO;
import com.zjtc.model.vo.WaterBalanceTestVO;
import com.zjtc.service.CommonService;
import com.zjtc.service.FileService;
import com.zjtc.service.SystemLogService;
import com.zjtc.service.UseWaterUnitService;
import com.zjtc.service.WaterBalanceTestProductService;
import com.zjtc.service.WaterBalanceTestService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lianghao
 * @date 2021/02/25
 */
@Service
public class WaterBalanceTestServiceImpl extends
    ServiceImpl<WaterBalanceTestMapper, WaterBalanceTest> implements
    WaterBalanceTestService {

  @Value("${file.fileUploadRootPath}")
  private String fileUploadRootPath;

  @Value("${file.preViewRealPath}")
  private String preViewRealPath;


  @Value("${file.fileUploadPath}")
  private String fileUploadPath;

  @Autowired
  private FileUtil fileUtil;

  @Autowired
  private WaterBalanceTestProductService waterBalanceTestProductService;

  @Autowired
  private FileService fileService;

  @Autowired
  private CommonService commonService;

  @Autowired
  private UseWaterUnitService useWaterUnitService;

  @Autowired
  private SystemLogService systemLogService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean updateModel(User user, JSONObject jsonObject) {
    boolean result = false;
    WaterBalanceTest balanceTest = jsonObject.toJavaObject(WaterBalanceTest.class);
    WaterBalanceTestVO balanceTestVO = jsonObject.toJavaObject(WaterBalanceTestVO.class);
    List<WaterBalanceTestProduct> products = balanceTestVO.getProducts();
    List<FileVO> files = balanceTestVO.getFileList();

    /**?????????????????????*/
    this.updateById(balanceTest);
    /**????????????????????????????????????????????????*/
    this.updateOrInsertProducts(products, balanceTest.getId(), user.getNodeCode());
    /**??????????????????*/
    this.handleFiles(files, balanceTest.getId());
    /**??????*/
    systemLogService.logInsert(user,"???????????????","??????",null);
    result = true;
    return result;
  }



  @Override
  public boolean deleteModel(List<String> ids) {
    boolean result = this.baseMapper.updateDeleted(ids);
    return result;
  }

  @Override
  public Map<String, Object> queryPage(User user, JSONObject jsonObject) {
    Map<String, Object> result = new HashMap<>();
    Integer size = jsonObject.getInteger("size");
    Integer current = jsonObject.getInteger("current");
    //String nodeCode = user.getNodeCode();
    //	String userId = user.getId();
    String unitName = jsonObject.getString("unitName");
    String unitCode = jsonObject.getString("unitCode");

    Map<String, Object> map = new HashMap<>();
    map.put("size", size);
    map.put("current", current);
    //    map.put("nodeCode", nodeCode);
    if (StringUtils.isNotBlank(jsonObject.getString("nodeCode"))) {
      map.put("nodeCode", jsonObject.getString("nodeCode"));
    }else{
      map.put("nodeCode", user.getNodeCode());
    }
    map.put("preViewRealPath", preViewRealPath  + "/");
    //	map.put("userId", userId);
    if (StringUtils.isNotBlank(unitName)) {
      map.put("unitName", unitName);
    }
    if (StringUtils.isNotBlank(unitCode)) {
      map.put("unitCode", unitCode);
    }

    /**????????????????????????????????????*/
    int num = this.baseMapper.queryNum(map);
    result.put("total", num);//????????????????????????
    result.put("size", size);//????????????
    result.put("pages", (int) Math.ceil((double) num / size));//??????????????????
    result.put("current", current);//?????????

    List<Map<String, Object>> records = this.baseMapper.queryPage(map);
    result.put("records", records);
    return result;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void importData(User user,MultipartFile file) throws Exception {
    String nodeCode = user.getNodeCode();
    Map beans = new HashMap<String, List>();
    WaterBalanceTestVO info = new WaterBalanceTestVO();
    List<WaterBalanceTestProductVO> products = new ArrayList<>();
    beans.put("info", info);
    beans.put("products", products);
    String xmlConfig = "template/xml/WaterBalanceTestManage.xml";
    Map result = new HashMap();
//    String fileRealPath =  fileUploadRootPath +"/"+ filePath;
//    //String fileRealPath =  "C:\\Users\\LH\\Desktop\\????????????12.xlsx";
    /**excel??????????????????bean*/
    result = commonService.importExcel(beans,file, xmlConfig,true);
    /**lombok???????????????getter???setter????????????mybatisPlus?????????????????? ??????????????????????????????????????????????????????????????????VO*/
    info = (WaterBalanceTestVO) result.get("info");
    products = (List<WaterBalanceTestProductVO>) result.get("products");
    /**?????????????????????????????????*/
    WaterBalanceTest balanceTest = new WaterBalanceTest();
    balanceTest.setId(UUID.randomUUID().toString().replace("-", ""));
    balanceTest.setUnitName(info.getUnitName());
    balanceTest.setUnitCode(info.getUnitCode());
    balanceTest.setIndustryName(info.getIndustryName());
    balanceTest.setIndustryCode(info.getIndustryCode());
    balanceTest.setCertificateNo(info.getCertificateNo());
    balanceTest.setLeakageRate(info.getLeakageRate());
    balanceTest.setCoverSpace(info.getCoverSpace());
    balanceTest.setFloorSpace(info.getFloorSpace());
    balanceTest.setUsePeopleNum(info.getUsePeopleNum());
    balanceTest.setYearAmount(info.getYearAmount());
    balanceTest.setLastTestTime(info.getLastTestTime());
    balanceTest.setCreatePersonId(user.getId());
    balanceTest.setCreateTime(new Date());
    balanceTest.setDeleted("0");
    balanceTest.setNodeCode(user.getNodeCode());
    /**????????????????????????????????????*/
    Map<String,Object>  useWaterUnit= useWaterUnitService.selectByUnitCode(balanceTest.getUnitCode(),user);
    if(null != useWaterUnit) {
      String unitAddress = useWaterUnit.get("unitAddress").toString();
      if (StringUtils.isNotBlank(unitAddress)) {
        balanceTest.setUnitAddress(unitAddress);
      }
    }
    this.save(balanceTest);
    //?????????????????????????????? ???????????????????????????
    if(StringUtils.isNotBlank(balanceTest.getUnitCode())){
      useWaterUnitService.refreshWaterBalance(balanceTest.getUnitCode(),balanceTest.getLastTestTime()) ;
    }
    /**???????????????????????????????????????*/
    waterBalanceTestProductService.add(products,user,balanceTest.getId());
    /**??????*/
    systemLogService.logInsert(user,"???????????????","??????",null);
  }

  @Override
  public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {

    final String fileName = "???????????????????????????????????????.xls";
    String saveFilePath =
        fileUploadRootPath + java.io.File.separator + fileUploadPath + java.io.File.separator + fileName;
//    ??????????????????
    String templatePath = "template/???????????????????????????????????????.xls";
//    ?????????
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream(templatePath);
//    ??????????????????
    java.io.File file = new java.io.File(saveFilePath);
    try {
//      ????????????
      FileUtils.copyInputStreamToFile(inputStream, file);
    }catch (Exception e) {
      e.printStackTrace();
    }
    fileUtil.downloadTemplate(file, templatePath, fileName, request, response);
    file.delete();

  }

  private void handleFiles(List<FileVO> files, String balanceTestId) {
    List<File> fileList = new ArrayList<>();
    for (FileVO fileVO : files) {
      File file = new File();
      file.setId(fileVO.getId());
      file.setDeleted(fileVO.getDeleted());
      fileList.add(file);
    }
    /**??????????????????*/
    fileService.updateBusinessId(balanceTestId, fileList);
  }

  private void updateOrInsertProducts(List<WaterBalanceTestProduct> products, String balanceTestId,
      String nodeCode) {
    if (products != null && products.size() > 0) {
      for (WaterBalanceTestProduct product : products) {
        if (null != product.getId()) {
          if ("1".equals(product.getDeleted())) {
            product.setDeleteTime(new Date());
          }
          waterBalanceTestProductService.updateNotNull(product);
        } else {
          product.setBalanceTestId(balanceTestId);
          product.setNodeCode(nodeCode);
          product.setCreateTime(new Date());
          product.setDeleted("0");
//          product.setProductCode()
          waterBalanceTestProductService.save(product);
        }
      }
    }
  }
}