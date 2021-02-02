package com.zjtc.base.util;

import static org.apache.poi.ss.usermodel.IndexedColors.BLACK;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.jxls.common.Context;
import org.jxls.transform.poi.WritableCellValue;

/**
 * 单位监控根据节超率设置导出数据单元格颜色的方法
 *
 * @author lianghao
 * @date 2021/01/29
 */
public class ColorCellValue implements WritableCellValue {

  private String value;

  public ColorCellValue(String value) {
    this.value = value;
  }

  @Override
  public Object writeToCell(Cell cell, Context context) {
    Workbook workbook = cell.getSheet().getWorkbook();
    CellStyle bgCell = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    short colorIndex = 0;

    if (Double.parseDouble(value) > 0d) {
      colorIndex = HSSFColorPredefined.RED.getIndex();
      font.setColor(HSSFColorPredefined.BLACK.getIndex());
    } else if (Double.parseDouble(value) <= 0d) {
      colorIndex = HSSFColorPredefined.GREEN.getIndex();
      font.setColor(HSSFColorPredefined.BLACK.getIndex());
    }
    bgCell.setFillForegroundColor(colorIndex);
    bgCell.setFont(font);
    /*填充：立体前景*/
    bgCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cell.setCellStyle(bgCell);
    cell.setCellValue(value);
    return cell;
  }

}
