package com.zjtc.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Log4j
@Component
public class PoiUtil {

  /**
   * @param file 导入的xlsx文件
   * @param startrow 开始行
   */
  public  List<String[]> readExcel(MultipartFile file, int startrow) throws IOException {
    InputStream inputXLS = file.getInputStream();
    Workbook workbook = WorkbookFactory.create(inputXLS);
    List<String[]> list = new ArrayList<String[]>();
    //第一个sheet页
    Sheet sheet = workbook.getSheetAt(0);
    // 获得当前sheet的结束行
    int lastRowNum = sheet.getLastRowNum();
    for (int rowNum = startrow; rowNum <= lastRowNum; rowNum++) {
      // 获得当前行
      Row row = sheet.getRow(rowNum);
      if (row == null) {
        sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), -1);
        continue;
      }
      // 获得当前行的列数
      int lastCellNum = row.getLastCellNum();
      String[] cells = new String[lastCellNum];
      // 循环当前行
      for (Cell c : row) {//获取每列数据
        if (c == null) {
          continue;
        }
        int regions = sheet.getNumMergedRegions();//是否含有合并的单元格
        if (regions > 0) {
          boolean isMerge = isMergedRegion(sheet, rowNum, c.getColumnIndex());
          // 判断是否具有合并单元格
          if (isMerge) {
            String value = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
            cells[c.getColumnIndex()] = value;
          } else {
            cells[c.getColumnIndex()] = getCellValue(c);
          }
        } else {
          cells[c.getColumnIndex()] = getCellValue(c);
        }
      }
      workbook.close();
      inputXLS.close();
      list.add(cells);
    }
    return list;
  }

  /**
   * 判断指定的单元格是否是合并单元格
   *
   * @param row 行下标
   * @param column 列下标
   */
  public  boolean isMergedRegion(Sheet sheet, int row, int column) {
    int sheetMergeCount = sheet.getNumMergedRegions();
    for (int i = 0; i < sheetMergeCount; i++) {
      CellRangeAddress range = sheet.getMergedRegion(i);
      int firstColumn = range.getFirstColumn();
      int lastColumn = range.getLastColumn();
      int firstRow = range.getFirstRow();
      int lastRow = range.getLastRow();
      if (row >= firstRow && row <= lastRow) {
        if (column >= firstColumn && column <= lastColumn) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 获取合并单元格的值
   * @param sheet
   * @param row
   * @param column
   * @return
   */
  public  String getMergedRegionValue(Sheet sheet, int row, int column) {
    int sheetMergeCount = sheet.getNumMergedRegions();
    for (int i = 0; i < sheetMergeCount; i++) {
      CellRangeAddress ca = sheet.getMergedRegion(i);
      int firstColumn = ca.getFirstColumn();
      int lastColumn = ca.getLastColumn();
      int firstRow = ca.getFirstRow();
      int lastRow = ca.getLastRow();
      if (row >= firstRow && row <= lastRow) {
        if (column >= firstColumn && column <= lastColumn) {
          Row fRow = sheet.getRow(firstRow);
          Cell fCell = fRow.getCell(firstColumn);
          return getCellValue(fCell);
        }
      }
    }
    return null;
  }

  /**
   * 读取每个单元格的数据
   * @param cell
   * @return
   */
  public  String getCellValue(Cell cell) {
    String cellValue = "";
    if (cell == null) {
      return cellValue;
    }
    // 判断数据的类型
    switch (cell.getCellType()) {
      case NUMERIC: // 数字 又分为纯数字和时间
        cellValue = String.valueOf(cell.getNumericCellValue());
        break;
      case STRING: // 字符串
        cellValue = String.valueOf(cell.getStringCellValue());
        break;
      case BOOLEAN: // Boolean
        cellValue = String.valueOf(cell.getBooleanCellValue());
        break;
      case FORMULA: // 公式
        cellValue = String.valueOf(cell.getCellFormula());
        break;
      case BLANK: // 空值
        cellValue = "";
        break;
      case ERROR: // 故障
        cellValue = "非法字符";
        break;
      default:
        cellValue = "无";
        break;
    }
    return cellValue;
  }

}