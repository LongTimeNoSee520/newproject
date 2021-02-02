package com.zjtc.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.transform.poi.WritableCellValue;
import org.jxls.util.JxlsHelper;
public class JxlsUtils {

  static {
    //添加自定义指令（可覆盖jxls原指令）
    //合并单元格(模板已经做过合并单元格操作的单元格无法再次合并)
    XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
  }

  //用于jxls的foreach循环计数
  private Map<String, Integer> countMap = new HashMap<>();

  public static void exportExcel(InputStream is, OutputStream os, Map<String, Object>
      model) throws IOException {
    Context context = PoiTransformer.createInitialContext();
    if (model != null) {
      for (Map.Entry<String, Object> entry : model.entrySet()) {
        context.putVar(entry.getKey(), entry.getValue());
      }
    }
    JxlsHelper jxlsHelper = JxlsHelper.getInstance();
    Transformer transformer = jxlsHelper.createTransformer(is, os);
    //Transformer transformer = TransformerFactory.createTransformer(is, os);
    //获得配置
    JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer
        .getTransformationConfig().getExpressionEvaluator();
    /**较低版本自定义方法的添加    //设置静默模式，不报警告
     evaluator.getJexlEngine().setSilent(true);
     //函数强制，自定义功能
     Map<String, Object> funcs = new HashMap<String, Object>();
     funcs.put("jx", new JxlsUtils());    //添加自定义功能
     evaluator.getJexlEngine().setFunctions(funcs);
     */
    //函数强制，自定义功能
    Map<String, Object> funcs = new HashMap<String, Object>();
    funcs.put("utils", new JxlsUtils()); //添加自定义功能
    JexlBuilder jb = new JexlBuilder();
    jb.namespaces(funcs);
    //jb.silent(true); //设置静默模式，不报警告
    JexlEngine je = jb.create();
    evaluator.setJexlEngine(je);
    //必须要这个，否者表格函数统计会错乱
    jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
  }

  public static void exportExcel(File xls, File out, Map<String, Object> model)
      throws FileNotFoundException, IOException {
    exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
  }

  public static void exportExcel(File templatePath, OutputStream os, Map<String, Object> model)
      throws Exception {
    File template = templatePath;//getTemplate(templatePath);
    if (template != null) {
      exportExcel(new FileInputStream(template), os, model);
    } else {
      throw new Exception("Excel 模板未找到。");
    }
  }

  //获取jxls模版文件
  public static File getTemplate(String path) {
    File template = new File(path);
    if (template.exists()) {
      return template;
    }
    return null;
  }

  // 日期格式化
  public String dateFmt(Date date, String fmt) {
    if (date == null) {
      return "";
    }
    try {
      SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
      return dateFmt.format(date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  // if判断
  public Object ifelse(boolean b, Object o1, Object o2) {
    return b ? o1 : o2;
  }

  //自定义计数函数
  public Integer count(String var) {
    if (var == null) {
      return null;
    }
    if (countMap.containsKey(var)) {
      Integer t = countMap.get(var);
      t += 1;
      countMap.replace(var, t);
      return t;
    } else {
      countMap.put(var, 1);
    }
    return 1;
  }

  //自定义背景色
  public static WritableCellValue showColor(String value) {
    return new ColorCellValue(value);
  }
}

