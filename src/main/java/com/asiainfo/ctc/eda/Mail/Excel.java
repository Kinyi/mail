package com.asiainfo.ctc.eda.Mail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Excel {

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

		try {
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(new File("E:/数据晨检.xls"));
			// 生成名为"第一页"的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet(yesterday + "日数据", 0);
			WritableSheet sheet2 = book.createSheet("三日内数据", 1);
			
//			WritableSheet.setRowView(int i,int height); 
			/**作用是指定第i+1行的高度，比如：*/ 
			//将第一行的高度设为200 
			sheet.setRowView(0,200); 
//			WritableSheet.setColumnView(int i,int width); 
			/**作用是指定第i+1列的宽度，比如：*/ 
			//将第一列的宽度设为30 
			sheet.setColumnView(1,30);
			sheet.setColumnView(2,15);
			sheet.setColumnView(3,12);
			sheet.setColumnView(4,15);

			
			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0),以及单元格内容为test
			Label field1 = new Label(0, 0, "op_time");
			Label field2 = new Label(1, 0, "type_name");
			Label field3 = new Label(2, 0, "data_num");
			Label field4 = new Label(3, 0, "partition_num");
			Label field5 = new Label(4, 0, "size_num");
			// 将定义好的单元格添加到工作表中
			sheet.addCell(field1);
			sheet.addCell(field2);
			sheet.addCell(field3);
			sheet.addCell(field4);
			sheet.addCell(field5);
			// 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
			// Number number = new Number(1, 0, 789.123);
			// sheet.addCell(number);
			// 写入数据并关闭文件
			book.write();
			book.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}