package org.cloud.framework.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.cloud.framework.model.DlpCheckRecord;
import org.cloud.framework.model.DlpCheckTask;
import org.cloud.framework.model.SystemUser;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author wgsh
 * @Date wgshb on 2019/7/19 15:22
 */
public class POIUtils {

	public static void createFixationSheet(OutputStream os,
										   List<DlpCheckRecord> records, DlpCheckTask task, SystemUser user) throws Exception {
		// 创建工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 在工作薄上建一张工作表
		HSSFSheet sheet = wb.createSheet();
		HSSFRow row = sheet.createRow((short) 0);

		// 表头标题样式
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 22);// 字体大小
		HSSFCellStyle headstyle = wb.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
		headstyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
		headstyle.setWrapText(true);
		headstyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		headstyle.setBorderBottom(BorderStyle.THIN); //下边框
		headstyle.setBorderLeft(BorderStyle.THIN);//左边框
		headstyle.setBorderTop(BorderStyle.THIN);//上边框
		headstyle.setBorderRight(BorderStyle.THIN);//右边框

		headstyle.setLocked(true);

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
		row.setHeight((short) 0x200);
		Cell cell = row.createCell(0);
		// 设置表头的内容
		cell.setCellValue(getTableHeadVal(task, user));
		cell.setCellStyle(headstyle);

		HSSFCellStyle topstyle = wb.createCellStyle();
		HSSFFont topfont = wb.createFont();
		topfont.setFontName("宋体");
		topfont.setFontHeightInPoints((short) 15);// 字体大小
		topstyle.setFont(topfont);
		topstyle.setAlignment(HorizontalAlignment.CENTER);
		topstyle.setLocked(true);

		topstyle.setBorderBottom(BorderStyle.THIN); //下边框
		topstyle.setBorderLeft(BorderStyle.THIN);//左边框
		topstyle.setBorderTop(BorderStyle.THIN);//上边框
		topstyle.setBorderRight(BorderStyle.THIN);//右边框



//		sheet.createFreezePane(0, 2,1,0);

		HSSFRow topColumn = sheet.createRow((short) 1);
		cteateCell(wb, topColumn, (short) 0, "序号", topstyle);
		cteateCell(wb, topColumn, (short) 1, "文件名称", topstyle);
		cteateCell(wb, topColumn, (short) 2, "原始密级", topstyle);
		cteateCell(wb, topColumn, (short) 3, "预测密级", topstyle);
		cteateCell(wb, topColumn, (short) 4, "确认密级", topstyle);
		cteateCell(wb, topColumn, (short) 5, "预警状态", topstyle);
		cteateCell(wb, topColumn, (short) 6, "创建时间", topstyle);
		int i = 1;

		HSSFCellStyle cellstyle = wb.createCellStyle();
		cellstyle.setAlignment(HorizontalAlignment.CENTER);

		cellstyle.setBorderBottom(BorderStyle.THIN); //下边框
		cellstyle.setBorderLeft(BorderStyle.THIN);//左边框
		cellstyle.setBorderTop(BorderStyle.THIN);//上边框
		cellstyle.setBorderRight(BorderStyle.THIN);//右边框


		for(int j = 0; j < records.size(); j++) {
			HSSFRow rowi = sheet.createRow(++i);
			DlpCheckRecord record = records.get(j);
			cteateCell(wb, rowi, (short) 0, "" + (j + 1), cellstyle);
			cteateCell(wb, rowi, (short) 1, record.getFileName(), cellstyle);
			cteateCell(wb, rowi, (short) 2, record.getOriginLabel(), cellstyle);
			cteateCell(wb, rowi, (short) 3, record.getCheckLabel(), cellstyle);
			cteateCell(wb, rowi, (short) 4, record.getReviewLabel(), cellstyle);
			cteateCell(wb, rowi, (short) 5, getCheckStatus(record.getCheckStatus()), cellstyle);
			cteateCell(wb, rowi, (short) 6, record.getCreateTime(), cellstyle);
		}

		sheet.autoSizeColumn((short)0); //调整第一列宽度
		sheet.autoSizeColumn((short)1); //调整第二列宽度
		sheet.autoSizeColumn((short)2); //调整第三列宽度
		sheet.autoSizeColumn((short)3); //调整第四列宽度
		sheet.autoSizeColumn((short)4); //调整第四列宽度
		sheet.autoSizeColumn((short)5); //调整第四列宽度
		sheet.autoSizeColumn((short)6); //调整第四列宽度

		wb.write(os);
		os.flush();
		os.close();
		System.out.println("文件生成");

	}

	private static String getTableHeadVal(DlpCheckTask task, SystemUser user) throws ParseException {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
		sb.append(task.getName())
				.append("_辅助检查分析报告(")
//				.append(user.getUsername())
//				.append(")/")
				.append(shortSdf.format(fullSdf.parse(task.getCreateTime())) + "/")
				.append(user.getUsername() + ")");

		return sb.toString();
	}

	private static String getCheckStatus(Integer checkStatus) {
		String result = "";
		switch (checkStatus) {
			case 0:
				result = "尚未检查";
				break;
			case 1:
				result = "正常";
				break;
			case 2:
				result = "预警";
				break;
				default:
					result = "未知";
		}
		return result;
	}

	private static void cteateCell(HSSFWorkbook wb, HSSFRow row, short col,
	                        String val, HSSFCellStyle cellStyle) {
		HSSFCell cell = row.createCell(col);
		cell.setCellValue(val);
		cell.setCellStyle(cellStyle);
	}
}
