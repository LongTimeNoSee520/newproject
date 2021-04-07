package com.zjtc.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.transform.poi.WritableCellValue;
import org.jxls.util.JxlsHelper;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

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

  /**
   * CSV模式导入excel到pojo对象,大数据量模式,比如几十万、上百万
   * @param path
   * @param sheetName
   * @param keys
   * @param clz
   * @param skipTitle
   * @param <T>
   * @return
   * @throws IOException
   * @throws OpenXML4JException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public static <T> List<T> importExcel(String path, String sheetName, String[] keys, Class<T> clz, boolean skipTitle)
      throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
    return XLSXCovertCSVReader.readerExcel(path, sheetName, keys, clz, skipTitle);
  }
}

class XLSXCovertCSVReader {

  /**
   * The type of the data value is indicated by an attribute on the cell. The value is usually in a
   * "v" element within the cell.
   */
  enum xssfDataType {
    BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
  }

  /**
   * 使用xssf_sax_API处理Excel,请参考： http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
   * <p/>
   * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
   * http://www.ecma-international.org/publications/standards/Ecma-376.htm
   * <p/>
   * A web-friendly version is http://openiso.org/Ecma/376/Part4
   */
  class MyXSSFSheetHandler extends DefaultHandler {

    /**
     * Table with styles
     */
    private StylesTable stylesTable;

    /**
     * Table with unique strings
     */
    private ReadOnlySharedStringsTable sharedStringsTable;

    /**
     * Destination for data
     */
    private final PrintStream output;

    /**
     * Number of columns to read starting with leftmost
     */
    private final int minColumnCount;

    // Set when V start element is seen
    private boolean vIsOpen;

    // Set when cell start element is seen;
    // used when cell close element is seen.
    private xssfDataType nextDataType;

    // Used to format numeric cell values.
    private short formatIndex;
    private String formatString;
    private final DataFormatter formatter;

    private int thisColumn = -1;
    private int rowNum = 0;
    // The last column printed to the output stream
    private int lastColumnNumber = -1;

    // Gathers characters as they are seen.
    private StringBuffer value;
    private Map<String, String> record;
    private List<Map<String, String>> rows = new ArrayList<>();
    private boolean isCellNull = false;

    /**
     * Accepts objects needed while parsing.
     *
     * @param styles Table of styles
     * @param strings Table of shared strings
     * @param cols Minimum number of columns to show
     * @param target Sink for output
     */
    public MyXSSFSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings, int cols,
        PrintStream target) {
      this.stylesTable = styles;
      this.sharedStringsTable = strings;
      this.minColumnCount = cols;
      this.output = target;
      this.value = new StringBuffer();
      this.nextDataType = xssfDataType.NUMBER;
      this.formatter = new DataFormatter();
      record = new HashMap<>(this.minColumnCount);
      rows.clear();// 每次读取都清空行集合
    }

    public <T> List<T> getDataList(Class<T> clz) {
      List<T> list = new ArrayList<>();
      try {
        T obj = clz.newInstance();
        rows.forEach((map) -> {
          try {
            BeanUtils.populate(obj, map);
            list.add(obj);
          } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
          }
        });
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
      return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String name, Attributes attributes)
        throws SAXException {

      if ("inlineStr".equals(name) || "v".equals(name) || "t".equals(name)) {
        vIsOpen = true;
        // Clear contents cache
        value.setLength(0);
      }
      // c => cell
      else if ("c".equals(name)) {
        // Get the cell reference
        String r = attributes.getValue("r");
        int firstDigit = -1;
        for (int c = 0; c < r.length(); ++c) {
          if (Character.isDigit(r.charAt(c))) {
            firstDigit = c;
            break;
          }
        }
        thisColumn = nameToColumn(r.substring(0, firstDigit));

        // Set up defaults.
        this.nextDataType = xssfDataType.NUMBER;
        this.formatIndex = -1;
        this.formatString = null;
        String cellType = attributes.getValue("t");
        String cellStyleStr = attributes.getValue("s");
        if ("b".equals(cellType))
          nextDataType = xssfDataType.BOOL;
        else if ("e".equals(cellType))
          nextDataType = xssfDataType.ERROR;
        else if ("inlineStr".equals(cellType))
          nextDataType = xssfDataType.INLINESTR;
        else if ("s".equals(cellType))
          nextDataType = xssfDataType.SSTINDEX;
        else if ("str".equals(cellType))
          nextDataType = xssfDataType.FORMULA;
        else if (cellStyleStr != null) {
          // It's a number, but almost certainly one
          // with a special style or format
          int styleIndex = Integer.parseInt(cellStyleStr);
          XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
          this.formatIndex = style.getDataFormat();
          this.formatString = style.getDataFormatString();
          if (this.formatString == null)
            this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
        }
      }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String name) throws SAXException {

      String thisStr = null;

      // v => contents of a cell
      if ("v".equals(name) || "t".equals(name)) {
        // Process the value contents as required.
        // Do now, as characters() may be called more than once
        switch (nextDataType) {

          case BOOL:
            char first = value.charAt(0);
            thisStr = first == '0' ? "FALSE" : "TRUE";
            break;

          case ERROR:
            thisStr = "\"ERROR:" + value.toString() + '"';
            break;

          case FORMULA:
            // A formula could result in a string value,
            // so always add double-quote characters.
            // thisStr = '"' + value.toString() + '"';
            thisStr = value.toString();
            break;

          case INLINESTR:
            XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
            // thisStr = '"' + rtsi.toString() + '"';
            thisStr = rtsi.toString();
            break;

          case SSTINDEX:
            String sstIndex = value.toString();
            try {
              int idx = Integer.parseInt(sstIndex);
              XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
              // thisStr = '"' + rtss.toString() + '"';
              thisStr = rtss.toString();
            } catch (NumberFormatException ex) {
              output.println("Failed to parse SST index '" + sstIndex + "': " + ex.toString());
            }
            break;

          case NUMBER:
            String n = value.toString();
            // 判断是否是日期格式
            if (HSSFDateUtil.isADateFormat(this.formatIndex, n)) {
              Double d = Double.parseDouble(n);
              Date date = HSSFDateUtil.getJavaDate(d);
              thisStr = formateDateToString(date);
            } else if (this.formatString != null)
              thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex,
                  this.formatString);
            else {
              thisStr = StringUtils
                  .trimTrailingCharacter(StringUtils.trimTrailingCharacter(n, '0'), '.');
            }
            break;

          default:
            thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
            break;
        }

        // Output after we've seen the string contents
        // Emit commas for any fields that were missing on this row
        if (lastColumnNumber == -1) {
          lastColumnNumber = 0;
        }
        // 判断单元格的值是否为空
        if (thisStr == null || "".equals(thisStr)) {
          isCellNull = true;// 设置单元格是否为空值
        }
        record.put(keys[thisColumn], thisStr);
        // Update column
        if (thisColumn > -1)
          lastColumnNumber = thisColumn;

      } else if ("row".equals(name)) {

        // Print out any missing commas if needed
        if (minColumns > 0) {
          // Columns are 0 based
          if (lastColumnNumber == -1) {
            lastColumnNumber = 0;
          }
          // if (isCellNull == false && record[0] != null
          // && record[1] != null)// 判断是否空行
          if (isCellNull == false)// 判断是否空行
          {
            if (rowNum > 0 || !skipTitle) {
              Map<String, String> map = new HashMap<>();
              map.putAll(record);
              rows.add(map);
            }
            rowNum++;
            isCellNull = false;
            record.clear();
          }
        }
        lastColumnNumber = -1;
      }
    }

    public List<Map<String, String>> getRows() {
      return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
      this.rows = rows;
    }

    /**
     * Captures characters only if a suitable element is open. Originally was just "v"; extended for
     * inlineStr also.
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
      if (vIsOpen)
        value.append(ch, start, length);
    }

    /**
     * Converts an Excel column name like "C" to a zero-based index.
     *
     * @return Index corresponding to the specified name
     */
    private int nameToColumn(String name) {
      int column = -1;
      for (int i = 0; i < name.length(); ++i) {
        int c = name.charAt(i);
        column = (column + 1) * 26 + c - 'A';
      }
      return column;
    }

    private String formateDateToString(Date date) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期
      return sdf.format(date);

    }

  }

  // /

  private OPCPackage xlsxPackage;
  private int minColumns;
  private String[] keys;
  private PrintStream output;
  private String sheetName;
  boolean skipTitle;

  /**
   * Creates a new XLSX -> CSV converter
   *
   * @param pkg The XLSX package to process
   * @param output The PrintStream to output the CSV to
   */
  public XLSXCovertCSVReader(OPCPackage pkg, PrintStream output, String sheetName, String[] keys,
      boolean skipTitle) {
    this.xlsxPackage = pkg;
    this.output = output;
    this.skipTitle = skipTitle;
    this.keys = keys;
    this.minColumns = keys.length;
    this.sheetName = sheetName;
  }

  /**
   * Parses and shows the content of one sheet using the specified styles and shared-strings
   * tables.
   */
  public List<Map<String, String>> processSheet(StylesTable styles,
      ReadOnlySharedStringsTable strings,
      InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {

    InputSource sheetSource = new InputSource(sheetInputStream);
    SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    SAXParser saxParser = saxFactory.newSAXParser();
    XMLReader sheetParser = saxParser.getXMLReader();
    MyXSSFSheetHandler handler = new MyXSSFSheetHandler(styles, strings, this.minColumns,
        this.output);
    sheetParser.setContentHandler(handler);
    sheetParser.parse(sheetSource);
    return handler.getRows();
  }

  /**
   * 初始化这个处理程序 将
   */
  public List<Map<String, String>> process()
      throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {

    ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
    XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
    List<Map<String, String>> list = null;
    StylesTable styles = xssfReader.getStylesTable();
    XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
    while (iter.hasNext()) {
      InputStream stream = iter.next();
      String sheetNameTemp = iter.getSheetName();
      if (this.sheetName.equals(sheetNameTemp)) {
        list = processSheet(styles, strings, stream);
        stream.close();
      }
    }
    return list;
  }

  /**
   * 读取Excel
   *
   * @param path 文件路径
   * @param sheetName sheet名称
   * @param keys 字段列表
   */
  public static List<Map<String, String>> readerExcel(String path, String sheetName, String[] keys,
      boolean skipTitle)
      throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
    Assert.notEmpty(keys, "字段列表不能为空!");
    OPCPackage p = OPCPackage.open(path);
    XLSXCovertCSVReader xlsx2csv = new XLSXCovertCSVReader(p, System.out, sheetName, keys,
        skipTitle);
    List<Map<String, String>> list = xlsx2csv.process();
    p.close();
    return list;
  }

  public static <T> List<T> readerExcel(String path, String sheetName, String[] keys, Class<T> clz,
      boolean skipTitle)
      throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
    List<Map<String, String>> list = readerExcel(path, sheetName, keys, skipTitle);
    List<T> beans = new ArrayList<>();
    list.forEach((map) -> {
      try {
        T obj = clz.newInstance();
        BeanUtils.populate(obj, map);
        beans.add(obj);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
        e.printStackTrace();
      }
    });
    return beans;
  }
}

