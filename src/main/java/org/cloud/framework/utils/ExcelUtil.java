//package org.cloud.framework.utils;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//public class ExcelUtil {
//		/**
//		 * excel2003驱动
//		 * @param inPath
//		 * @return
//		 * @throws FileNotFoundException
//		 */
//		public static HSSFWorkbook getHSSFWork(String inPath) throws FileNotFoundException{
//			InputStream is = new FileInputStream(inPath);
//			HSSFWorkbook wb = null;
//			POIFSFileSystem fs = null;
//			try {
//				fs = new POIFSFileSystem(is);
//				wb = new HSSFWorkbook(fs);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return wb;
//		}
//		/**
//		 * excel2007驱动
//		 * @param inPath
//		 * @return
//		 * @throws FileNotFoundException
//		 */
//		public static XSSFWorkbook getXSSFWork(String inPath) throws FileNotFoundException{
//			InputStream is = new FileInputStream(inPath);
//			XSSFWorkbook wb = null;
//			try {
//				wb = new XSSFWorkbook(is);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return wb;
//		}
//		/**
//		 * 获取excel2003--sheetName
//		 * @param
//		 * @return
//		 * @throws Exception
//		 */
//		public static String[] getExcelSheetName(HSSFWorkbook wb) throws Exception{
//			int numberOfSheets = wb.getNumberOfSheets();
//			String []sheetsName=new String[numberOfSheets];
//			for (int i = 0; i < numberOfSheets; i++) {
//				HSSFSheet sheet = wb.getSheetAt(i);
//				sheetsName[i] = sheet.getSheetName();
//			}
//			return sheetsName;
//		}
//		/**
//		 * 获取excel2007--sheetName
//		 * @param
//		 * @return
//		 * @throws Exception
//		 */
//		public static String[] getExcelSheetName(XSSFWorkbook wb) throws Exception{
//			int numberOfSheets = wb.getNumberOfSheets();
//			String []sheetsName=new String[numberOfSheets];
//			for (int i = 0; i < numberOfSheets; i++) {
//				XSSFSheet sheet = wb.getSheetAt(i);
//				sheetsName[i] = sheet.getSheetName();
//			}
//			return sheetsName;
//		}
//		/**
//		 * 获取excel2003列名称
//		 * @param
//		 * @param sheetName
//		 * @return
//		 * @throws Exception
//		 */
//		public static String[] getColumnName(HSSFWorkbook wb,String sheetName) throws Exception{
//			HSSFSheet sheet = wb.getSheet(sheetName);
//			HSSFRow columnName = sheet.getRow(0);
//			if(columnName==null){
//				return new String[0];
//			}
//			int rowNum = columnName.getLastCellNum();
//			String []columnNames=new String[rowNum];
//			for (int i = 0; i < rowNum; i++) {
//				columnNames[i]=columnName.getCell(i).getStringCellValue();
//			}
//			return columnNames;
//		}
//		/**
//		 * 获取excel2007列名称
//		 * @param
//		 * @param sheetName
//		 * @return
//		 * @throws Exception
//		 */
//		public static String[] getColumnName(XSSFWorkbook wb,String sheetName) throws Exception{
//			XSSFSheet sheet = wb.getSheet(sheetName);
//			XSSFRow columnName = sheet.getRow(0);
//			if(columnName == null){
//				return new String[0];
//			}
//			int rowNum = columnName.getLastCellNum();
//			String []columnNames=new String[rowNum];
//			for (int i = 0; i < rowNum; i++) {
//				columnNames[i]=columnName.getCell(i).getStringCellValue();
//			}
//			return columnNames;
//		}
//		/**
//		 * Excel2003获取行数
//		 * @param wb
//		 * @param sheetName
//		 * @return
//		 */
//		public static int getRowNum(HSSFWorkbook wb,String sheetName){
//			HSSFSheet sheet = wb.getSheet(sheetName);
//			return sheet.getLastRowNum();
//		}
//		/**
//		 * Excel2007获取行数
//		 * @param wb
//		 * @param sheetName
//		 * @return
//		 */
//		public static int getRowNum(XSSFWorkbook wb,String sheetName){
//			XSSFSheet sheet = wb.getSheet(sheetName);
//			return sheet.getLastRowNum();
//		}
//		/**
//		 * Excel2003获取列数
//		 * @param wb
//		 * @param sheetName
//		 * @return
//		 */
//		public static int getColumnNum(HSSFWorkbook wb,String sheetName){
//			HSSFSheet sheet = wb.getSheet(sheetName);
//			HSSFRow row = sheet.getRow(0);
//			if(row==null){
//				return 0;
//			}
//			return row.getPhysicalNumberOfCells();
//		}
//		/**
//		 * Excel2007获取行数
//		 * @param wb
//		 * @param sheetName
//		 * @return
//		 */
//		public static int getColumnNum(XSSFWorkbook wb,String sheetName){
//			XSSFSheet sheet = wb.getSheet(sheetName);
//			XSSFRow row = sheet.getRow(0);
//			if(row  == null ){
//				return 0;
//			}
//			return row.getPhysicalNumberOfCells();
//		}
//		/**
//		 * Excel2003类型转换
//		 * @param cell
//		 * @return
//		 */
//		public static String getCellFormatValue(HSSFCell cell) {
//			String cellvalue = "";
//			if (cell != null) {
//				switch (cell.getCellType()) {// 判断当前Cell的Type
//				case HSSFCell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
//				case HSSFCell.CELL_TYPE_FORMULA: {// 判断当前的cell是否为Date
//					if (HSSFDateUtil.isCellDateFormatted(cell)) {
//						// 如果是Date类型则，转化为Data格式
//						// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
//						// cellvalue = cell.getDateCellValue().toLocaleString();
//						// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
//						Date date = cell.getDateCellValue();
//						SimpleDateFormat sdf = new SimpleDateFormat(
//								"yyyy-MM-dd");
//						cellvalue = sdf.format(date);
//					} else { // 如果是纯数字
//						cellvalue = String.valueOf(cell.getNumericCellValue()); // 取得当前Cell的数值
//						if(cellvalue.endsWith(".0")){
//							cellvalue=cellvalue.substring(0, cellvalue.length()-2);
//						}
//					}
//					break;
//				}
//				case XSSFCell.CELL_TYPE_STRING:// 如果当前Cell的Type为String
//					cellvalue = cell.getRichStringCellValue().getString();// 取得当前的Cell字符串
//					break;
//				default: // 默认的Cell值
//					cellvalue = " ";
//				}
//			} else {
//				cellvalue = "";
//			}
//			return cellvalue;
//		}
//		/**
//		 * Excel2007类型转换
//		 * @param cell
//		 * @return
//		 */
//		public static String getCellFormatValue(XSSFCell cell) {
//			String cellvalue = "";
//			if (cell != null) {
//				switch (cell.getCellType()) {// 判断当前Cell的Type
//				case XSSFCell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
//				case XSSFCell.CELL_TYPE_FORMULA: {// 判断当前的cell是否为Date
//					if (HSSFDateUtil.isCellDateFormatted(cell)) {
//						// 如果是Date类型则，转化为Data格式
//						// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
//						// cellvalue = cell.getDateCellValue().toLocaleString();
//						// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
//						Date date = cell.getDateCellValue();
//						SimpleDateFormat sdf = new SimpleDateFormat(
//								"yyyy-MM-dd");
//						cellvalue = sdf.format(date);
//					} else { // 如果是纯数字
//						cellvalue = String.valueOf(cell.getNumericCellValue()); // 取得当前Cell的数值
//						if(cellvalue.endsWith(".0")){
//							cellvalue=cellvalue.substring(0, cellvalue.length()-2);
//						}
//					}
//					break;
//				}
//				case XSSFCell.CELL_TYPE_STRING:// 如果当前Cell的Type为String
//					cellvalue = cell.getRichStringCellValue().getString();// 取得当前的Cell字符串
//					break;
//				default: // 默认的Cell值
//					cellvalue = " ";
//				}
//			} else {
//				cellvalue = "";
//			}
//			return cellvalue;
//		}
//		/**
//		 * 将Excel2003 组装到list->map
//		 * @param wb
//		 * @param sheetName
//		 * @return
//		 * @throws Exception
//		 */
//		public static List<Map<String, Object>> getReadListMap(HSSFWorkbook wb,String sheetName) throws Exception{
//			List<Map<String, Object>> listRow = new ArrayList<Map<String, Object>>();
//			Map<String, Object> mapRow = new HashMap<String, Object>();
//			
//			HSSFSheet sheet = wb.getSheet(sheetName);
//			String[] columnName = getColumnName(wb, sheetName);
//			int rowNum = getRowNum(wb, sheetName);
//			int columnNum = getColumnNum(wb, sheetName);
//			for (int i = 1; i < rowNum+1; i++) {
//				HSSFRow row = sheet.getRow(i);
//				mapRow = new HashMap<String, Object>();
//				for (int j = 0; j < columnNum; j++) {
//					mapRow.put(columnName[j], getCellFormatValue(row.getCell(j)));
//				}
//				listRow.add(mapRow);
//			}
//			return listRow;
//		}
//		/**
//		 * 将Excel2007 组装到list->map
//		 * @param wb
//		 * @param sheetName
//		 * @return
//		 * @throws Exception
//		 */
//		public static List<Map<String, Object>> getReadListMap(XSSFWorkbook wb,String sheetName) throws Exception{
//			List<Map<String, Object>> listRow = new ArrayList<Map<String, Object>>();
//			Map<String, Object> mapRow = new HashMap<String, Object>();
//			XSSFSheet sheet = wb.getSheet(sheetName);
//			String[] columnName = getColumnName(wb, sheetName);
//			int rowNum = getRowNum(wb, sheetName);
//			int columnNum = getColumnNum(wb, sheetName);
//			for (int i = 1; i < rowNum+1; i++) {
//				XSSFRow row = sheet.getRow(i);
//				mapRow = new HashMap<String, Object>();
//				for (int j = 0; j < columnNum; j++) {
//					mapRow.put(columnName[j], getCellFormatValue(row.getCell(j)));
//				}
//				listRow.add(mapRow);
//			}
//			return listRow;
//		}
//		/**
//		 * 读取Excel2003
//		 * @param inPath
//		 * @param sheetName
//		 * @return
//		 * @throws Exception
//		 */
//		public static List<Map<String, Object>> readExcel2003(String inPath,String sheetName) throws Exception {
//			List<Map<String, Object>> readListMap = new ArrayList<Map<String,Object>>();
//			HSSFWorkbook wb = getHSSFWork(inPath);
//			int numberOfSheets = wb.getNumberOfSheets();
//			if(sheetName == null || sheetName.equals("")){//没有指定sheetName，则全部倒进去
//				for (int i = 0; i < numberOfSheets; i++) {
//					getReadListMap(wb, wb.getSheetAt(i).getSheetName());
//				}
//			}else{//指定sheetName，则只导入sheet为sheetName页
//				readListMap = getReadListMap(wb, sheetName);
//			}
//			return readListMap;
//		}
//		/**
//		 * 读取Excel2007
//		 * @param inPath
//		 * @param sheetName
//		 * @return
//		 * @throws Exception
//		 */
//		public static List<Map<String, Object>> readExcel2007(String inPath,String sheetName) throws Exception {
//			List<Map<String, Object>> readListMap = new ArrayList<Map<String,Object>>();
//			XSSFWorkbook wb = getXSSFWork(inPath);
//			int numberOfSheets = wb.getNumberOfSheets();
//			if(sheetName == null || sheetName.equals("")){//没有指定sheetName，则全部倒进去
//				for (int i = 0; i < numberOfSheets; i++) {
//					getReadListMap(wb, wb.getSheetAt(i).getSheetName());
//				}
//			}else{//指定sheetName，则只导入sheet为sheetName页
//				readListMap = getReadListMap(wb, sheetName);
//			}
//			return readListMap;
//		}
//		/**
//		 * Excel2003和Excel2007 都可以
//		 * @param inPath
//		 * @param sheetName
//		 * @return
//		 * @throws Exception
//		 */
//		public static List<Map<String, Object>> readExcel(String inPath,String sheetName) throws Exception{
//			List<Map<String, Object>> readListMap = new ArrayList<Map<String,Object>>();
//			if(inPath.endsWith(".xls")){//excel2003
//				readListMap = readExcel2003(inPath, sheetName);
//			}else if(inPath.endsWith(".xlsx")){//excel2007
//				readListMap = readExcel2007(inPath, sheetName);
//			}
//			return readListMap;
//		}
//		public static  List<Map<String,String>>getExcelField(String inPath,String sheetName){
//			String[] columnName = null;
//			List<Map<String,String>>listMap = new ArrayList<Map<String,String>>();
//			Map<String,String> map = null;
//			try {
//				if(inPath.endsWith(".xls")){//excel2003
//					HSSFWorkbook wb = Execl.getHSSFWork(inPath);
//					columnName = Execl.getColumnName(wb, sheetName);
//				}else if(inPath.endsWith(".xlsx")){//excel2007
//					XSSFWorkbook wb = Execl.getXSSFWork(inPath);
//					columnName = Execl.getColumnName(wb, sheetName);
//				}
//			} catch (Exception e) {
//			}
//			if(columnName!=null){
//				for (int i = 0; i < columnName.length; i++) {
//					map=new HashMap<String,String>();
//					map.put("fieldName", columnName[i]);
//					listMap.add(map);
//				}
//			}
//			return listMap;
//		}
//		public static List<Map<String,String>> getExcelSheetName(String inPath) {
//			String[] sheetName = null;
//			List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
//			Map<String, String> map = null;
//			try {
//				if (inPath.endsWith(".xls")) {//excel2003
//					HSSFWorkbook wb = Execl.getHSSFWork(inPath);
//					sheetName = Execl.getExcelSheetName(wb);
//				} else if (inPath.endsWith(".xlsx")) {//excel2007
//					XSSFWorkbook wb = Execl.getXSSFWork(inPath);
//					sheetName = Execl.getExcelSheetName(wb);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			if (sheetName != null) {
//				for (int i = 0; i < sheetName.length; i++) {
//					map = new HashMap<String, String>();
//					map.put("sheetName", sheetName[i]);
//					listMap.add(map);
//				}
//			}
//			return listMap;
//		}
//
//	}
