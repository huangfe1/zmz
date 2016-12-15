
package com.dreamer.util;

import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Excel文件工具
 * 类名: ExcelFile</br>
 * 包名：com.diyvc.common.data.excel </br>
 */
public class ExcelFile {




    public static void main(String[] args) {
        List<String> headers = new ArrayList<>();
        headers.add("业务单号");
        headers.add("寄件单位");
        headers.add("寄件人姓名");
        headers.add("寄件人电话");
        headers.add("寄件人手机");
        headers.add("寄件人省");
        headers.add("寄件人市");
        headers.add("寄件区/县");
        headers.add("寄件人地址");
        headers.add("寄件人邮编");
        headers.add("收件人姓名");
        headers.add("收件人电话");
        headers.add("收件人手机");
        headers.add("收件省");
        headers.add("收件市");
        headers.add("收件区/县");
        headers.add("收件人地址");
        headers.add("收件邮政编码");
        headers.add("运费");
        headers.add("订单金额");
        headers.add("商品名称");
        headers.add("商品编码");
        headers.add("销售属性");
        headers.add("商品金额");
        headers.add("数量");
        headers.add("备注");
        List<Map> datas = new ArrayList<>();
        Map m = null;
        for (int i = 0; i < 10; i++) {
            m = new HashedMap();
            for (int j = 0; j < headers.size(); j++) {
                m.put(j,"第"+i+"行数据"+j);
            }
            datas.add(m);
        }
//        ExpExs(path,"","统计报表",headers,datas);
    }






    //    * 通用的Excel文件创建方法
//    *   title:首行标题: 2015年度统计报表
//    *  sheets:sheet的tab标签页说明: 15年度报表
//    * headers:表头：List存放表头  编号、姓名、备注
//    *   datas:数据行：list存放实体数据，map存放具体每一行数据，和headers对应。
//            *      rs:HttpServletResponse响应作用域，如果不为null，会直接将文件流输出到客户端，下载文件
    public static void ExpExs(String title, List<String> sheetses,List< List>headerses,List< List<Map>> datases, HttpServletResponse rs) {
        try {


            HSSFWorkbook workbook = new HSSFWorkbook();
            for(int sindex=0;sindex<sheetses.size();sindex++){
                String sheets=sheetses.get(sindex);
                List<Map> datas=datases.get(sindex);
                List headers=headerses.get(sindex);
                if (sheets == null || "".equals(sheets)) {
                    sheets = "sheet";
                }
                HSSFSheet sheet = workbook.createSheet(sheets); //+workbook.getNumberOfSheets()
                HSSFRow row;
                HSSFCell cell;

                // 设置这些样式
                HSSFFont font = workbook.createFont();
                font.setFontName(HSSFFont.FONT_ARIAL);//字体
                font.setFontHeightInPoints((short) 16);//字号
//            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
                //font.setColor(HSSFColor.BLUE.index);//颜色

                HSSFCellStyle cellStyle = workbook.createCellStyle(); //设置单元格样式
                cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                cellStyle.setFont(font);

                //产生表格标题行
                row = sheet.createRow(0);
                row.setHeightInPoints(20);
                for (int i = 0; i < headers.size(); i++) {
                    HSSFRichTextString text = new HSSFRichTextString(headers.get(i).toString());
                    cell = row.createCell(i);
                    cell.setCellValue(text);
                    cell.setCellStyle(cellStyle);
                }


                cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                cellStyle.setDataFormat((short) 0x31);//设置显示格式，避免点击后变成科学计数法了
                //cellStyle.setWrapText(true);//设置自动换行
                Map map;
                //遍历集合数据，产生数据行
                for (int i = 0; i < datas.size(); i++) {
                    row = sheet.createRow((i + 1));
                    row.setHeightInPoints(20);
                    map = datas.get(i);

                    for (int j = 0; j < map.size(); j++) {
                        cell = row.createCell(j);
                        cell.setCellStyle(cellStyle);

                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        if (map.get(j) != null) {
                            cell.setCellValue(new HSSFRichTextString(map.get(j).toString()));
                        } else {
                            cell.setCellValue(new HSSFRichTextString(""));
                        }
                    }
                }

                for (int i = 0; i < headers.size(); i++) {
                    sheet.autoSizeColumn((short) i);
                }
            }

            rs.reset();
            rs.setContentType("multipart/form-data"); //自动识别
            rs.setHeader("Content-Disposition", "attachment;filename=data.xls");
            //文件流输出到rs里
            workbook.write(rs.getOutputStream());
            rs.getOutputStream().flush();
            rs.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //    * 通用的Excel文件创建方法
//    *   title:首行标题: 2015年度统计报表
//    *  sheets:sheet的tab标签页说明: 15年度报表
//    * headers:表头：List存放表头  编号、姓名、备注
//    *   datas:数据行：list存放实体数据，map存放具体每一行数据，和headers对应。
//            *      rs:HttpServletResponse响应作用域，如果不为null，会直接将文件流输出到客户端，下载文件
    public static void ExpExs(String title, String sheets, List headers, List<Map> datas, HttpServletResponse rs) {
        try {
            if (sheets == null || "".equals(sheets)) {
                sheets = "sheet";
            }

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(sheets); //+workbook.getNumberOfSheets()

            HSSFRow row;
            HSSFCell cell;

            // 设置这些样式
            HSSFFont font = workbook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);//字体
            font.setFontHeightInPoints((short) 16);//字号
//            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
            //font.setColor(HSSFColor.BLUE.index);//颜色

            HSSFCellStyle cellStyle = workbook.createCellStyle(); //设置单元格样式
            cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setFont(font);

            //产生表格标题行
            row = sheet.createRow(0);
            row.setHeightInPoints(20);
            for (int i = 0; i < headers.size(); i++) {
                HSSFRichTextString text = new HSSFRichTextString(headers.get(i).toString());
                cell = row.createCell(i);
                cell.setCellValue(text);
                cell.setCellStyle(cellStyle);
            }


            cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setDataFormat((short) 0x31);//设置显示格式，避免点击后变成科学计数法了
            //cellStyle.setWrapText(true);//设置自动换行
            Map map;
            //遍历集合数据，产生数据行
            for (int i = 0; i < datas.size(); i++) {
                row = sheet.createRow((i + 1));
                row.setHeightInPoints(20);
                map = datas.get(i);

                for (int j = 0; j < map.size(); j++) {
                    cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);

                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    if (map.get(j) != null) {
                        cell.setCellValue(new HSSFRichTextString(map.get(j).toString()));
                    } else {
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                }
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn((short) i);
            }

            rs.reset();
            rs.setContentType("multipart/form-data"); //自动识别
            rs.setHeader("Content-Disposition", "attachment;filename=data.xls");
            //文件流输出到rs里
            workbook.write(rs.getOutputStream());
            rs.getOutputStream().flush();
            rs.getOutputStream().close();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


//    /*
// * 通用的Excel文件创建方法
// *    path:保存路径: C:/xls/统计报表.xls
// *   title:首行标题: 2015年度统计报表
// *  sheets:sheet的tab标签页说明: 15年度报表
// * headers:表头：List存放表头  编号、姓名、备注
// *   datas:数据行：list存放实体数据，map存放具体每一行数据，和headers对应。
// */
//    public static void ExpExs(String path, String title, String sheets, List headers, List<Map> datas) {
//        try {
//            if (sheets == null || "".equals(sheets)) {
//                sheets = "sheet";
//            }
//
//            boolean isExist = new File(path).exists();
//            if (!isExist) {
//                HSSFWorkbook workbook = new HSSFWorkbook();
//                HSSFSheet sheet = workbook.createSheet(sheets);
//
//                FileOutputStream out = new FileOutputStream(new File(path));
//                workbook.write(out);
//                out.flush();
//                out.close();
//            }
//            FileInputStream file = new FileInputStream(new File(path));
//            HSSFWorkbook workbook = new HSSFWorkbook(file);
//
//            HSSFSheet sheet = null;
//            if (!isExist) {
//                sheet = workbook.getSheetAt(0);
//            } else {
//                if (workbook.getSheet(sheets) == null) {
//                    sheet = workbook.createSheet(sheets); //+workbook.getNumberOfSheets()
//                } else {
//                    System.out.println("文件：[" + path + "] [" + sheets + "] 已经存在...");
//                    System.out.println("");
//                    return;
//                }
//            }
//            HSSFRow row;
//            HSSFCell cell;
//
//            // 设置这些样式
//            HSSFFont font = workbook.createFont();
//            font.setFontName(HSSFFont.FONT_ARIAL);//字体
//            font.setFontHeightInPoints((short) 16);//字号
//            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
//            //font.setColor(HSSFColor.BLUE.index);//颜色
//
//            HSSFCellStyle cellStyle = workbook.createCellStyle(); //设置单元格样式
//            cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
//            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
////            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
////            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
////            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
////            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setFont(font);
//
//            //产生表格标题行
//            row = sheet.createRow(0);
//            row.setHeightInPoints(20);
//            for (int i = 0; i < headers.size(); i++) {
//                HSSFRichTextString text = new HSSFRichTextString(headers.get(i).toString());
//                cell = row.createCell(i);
//                cell.setCellValue(text);
//                cell.setCellStyle(cellStyle);
//            }
//
//
//            cellStyle = workbook.createCellStyle();
//            cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
////            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
////            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
////            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
////            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setDataFormat((short) 0x31);//设置显示格式，避免点击后变成科学计数法了
//            //cellStyle.setWrapText(true);//设置自动换行
//            Map map;
//            //遍历集合数据，产生数据行
//            for (int i = 0; i < datas.size(); i++) {
//                row = sheet.createRow((i + 1));
//                row.setHeightInPoints(20);
//                map = datas.get(i);
//
//                for (int j = 0; j < map.size(); j++) {
//                    cell = row.createCell(j);
//                    cell.setCellStyle(cellStyle);
//
//                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//                    if (map.get(j) != null) {
//                        cell.setCellValue(new HSSFRichTextString(map.get(j).toString()));
//                    } else {
//                        cell.setCellValue(new HSSFRichTextString(""));
//                    }
//                }
//            }
//
//            for (int i = 0; i < headers.size(); i++) {
//                sheet.autoSizeColumn((short) i);
//            }
//
//            FileOutputStream out = new FileOutputStream(new File(path));
//            workbook.write(out);
//            out.flush();
//            out.close();
//
//            /*
//            HSSFRow row = sheet.createRow(sheets);
//            HSSFCell cell = null;
//            cell=row.createCell(sheets);
//            cell.setCellValue(new HSSFRichTextString("-["+sheets+"]-"));
//            sheets=sheets+2;//中间空一行
//            row=sheet.createRow(sheets);
//            */
//
//        } catch (Exception e) {
//            System.out.println("#Error [" + e.getMessage() + "] ");
//        }
//        System.out.println("文件：[" + path + "] [" + sheets + "] 创建成功...");
//        System.out.println("");
//    }



    /**
     * 读取Excel文件
     *
     * @param filename   Excel文件名
     * @param sheetIndex Sheet编号(起始为0)
     * @param titleRow   标题列的高度(如果为0，则表明没有标题行)
     * @param columns    列名
     * @return 读取后的Excel数据
     */
    public  List<Map<String, Object>> read(String filename, int sheetIndex, int titleRow, String[] columns) {
        InputStream fileStream = null;
        try {
            fileStream = new FileInputStream(filename);
            if (filename.endsWith("xlsx")) {
                return read(fileStream, sheetIndex, titleRow, columns);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileStream);
        }
        return null;
    }

    /**
     * 读取Excel文件
     *
     * @param fileStream Excel文件流
     * @param sheetIndex Sheet编号(起始为0)
     * @param titleRow   标题列的高度(如果为0，则表明没有标题行)
     * @param columns    列名
     * @return 读取后的Excel数据
     */
    public   List<Map<String, Object>> read(InputStream fileStream, int sheetIndex, int titleRow, String[] columns) {
        List<Map<String, Object>> table = new ArrayList<Map<String, Object>>();
        try {
            Workbook wb =null;
            try {
                wb=new XSSFWorkbook(fileStream);
            }catch (Exception e){
                wb=new HSSFWorkbook(fileStream);
            }

            Sheet sheet = wb.getSheetAt(sheetIndex);

            int rowCount = 0;

            Iterator<Row> rows = sheet.iterator();
            while (rows.hasNext()) {
                Row row = rows.next();
                rowCount++;
                //过滤标题行
                if (titleRow >= rowCount) {
                    continue;
                }
                Map<String, Object> item = new HashMap<String, Object>();
                for (int i = 0; i < columns.length; i++) {
                    item.put(columns[i], getCellValue(row, i));
                }
                table.add(item);
            }
            IOUtils.closeQuietly(fileStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileStream);
        }
        return table;
    }

    private Object getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        Object value = null;
        if (cell instanceof XSSFCell) {
            XSSFCell xssfCell = (XSSFCell) cell;
            if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(xssfCell)) {
                    value = xssfCell.getDateCellValue();
                } else {
                    value = (int) xssfCell.getNumericCellValue();
                }
            } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                value = xssfCell.getStringCellValue();
            }
        }

        return value;
    }

}

