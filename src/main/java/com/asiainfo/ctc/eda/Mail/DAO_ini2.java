package com.asiainfo.ctc.eda.Mail;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 数据检查：
 * 1.除了固网3A外，其他项不为0
 * 2.七天数据求方差
 * 
 * @author lvqian
 *
 */
public class DAO_ini2 {

	public static void DBConnect() {
		try {
			// 加载MySql的驱动类
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("找不到驱动程序类 ,加载驱动失败！");
			e.printStackTrace();
		}

		// 连接MySql数据库
		String url = "jdbc:mysql://10.62.242.131:3306/check_data";
		String username = "root";
		String password = "root123";

		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException se) {
			System.out.println("数据库连接失败！");
			se.printStackTrace();
		}

		Statement stmt = null;
		ResultSet rs = null; 

		// 用于封装发邮件的内容
//		StringBuilder sb = new StringBuilder();

		// 昨天,前天,大前天
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

		cal.add(Calendar.DATE, -1);
		String twodaysago = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

		cal.add(Calendar.DATE, -1);
		String threedaysago = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

//		sb.append("大家好：\n    大数据平台" + yesterday + "日数据如下\n");

		try {
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(new File("/work/kinyi/checkData/file/dataCheck.xls"));
			// 生成名为"第一页"的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet(yesterday + "日数据", 0);
			WritableSheet sheet2 = book.createSheet("三日内数据", 1);
			
			//调整列宽
			sheet.setColumnView(1,30);
			sheet.setColumnView(2,15);
			sheet.setColumnView(3,12);
			sheet.setColumnView(4,17);
			
			sheet2.setColumnView(0,30);
			sheet2.setColumnView(1,17);
			sheet2.setColumnView(2,17);
			sheet2.setColumnView(3,17);
			
			//设置字体格式
			WritableFont font = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD);
			WritableCellFormat format = new WritableCellFormat(font);
			
			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0),以及单元格内容为test
			Label field1 = new Label(0, 0, "op_time", format);
			Label field2 = new Label(1, 0, "type_name", format);
			Label field3 = new Label(2, 0, "data_num", format);
			Label field4 = new Label(3, 0, "partition_num", format);
			Label field5 = new Label(4, 0, "size_num", format);
			// 将定义好的单元格添加到工作表中
			sheet.addCell(field1);
			sheet.addCell(field2);
			sheet.addCell(field3);
			sheet.addCell(field4);
			sheet.addCell(field5);
			// 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
			// Number number = new Number(1, 0, 789.123);
			// sheet.addCell(number);

			// ---------------------------------------------------------------------

			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM all_sum_data a WHERE a.`op_time`=20151130;");

			// 将输出内容格式化之后再输出
			// %ns -- 中的n表示整个字符串的长度,若长度不够,左边补空格
//			String header = String.format("%s%36s%22s%17s%17s\n", "op_time", "type_name", "data_num", "partition_num", "size_num");
//			sb.append(header);
			
			int i = 0;

			while (rs.next()) {
				i++;
				String op_time = rs.getString(1); // 此方法比较高效
				String type_name = rs.getString(2);
				String data_num = rs.getString(3);
				String partition_num = rs.getString(4);
				String size_num = rs.getString(5);
				
				Label value1 = new Label(0, i, op_time);
				Label value2 = new Label(1, i, type_name);
				Label value3 = new Label(2, i, numberFormat(data_num));
				Label value4 = new Label(3, i, partition_num);
				Label value5 = new Label(4, i, numberFormat(size_num));
				
				sheet.addCell(value1);
				sheet.addCell(value2);
				sheet.addCell(value3);
				sheet.addCell(value4);
				sheet.addCell(value5);

//				String content = String.format("%s%35s%22s%12s%22s\n", op_time, type_name, data_num, partition_num, size_num);
//				sb.append(content);
			}

//			sb.append("\n三日内数据量统计：\n");

			// 聚合三天的数据大小
			rs = stmt.executeQuery(
					"SELECT a.`type_name`,a.`size_num`,b.size_num,c.size_num FROM check_data.`all_sum_data` a LEFT OUTER JOIN (SELECT type_name,size_num FROM check_data.`all_sum_data` sec WHERE sec.`op_time`="
							+ twodaysago
							+ ")b ON a.`type_name`=b.type_name LEFT OUTER JOIN (SELECT type_name,size_num FROM check_data.`all_sum_data` third WHERE third.`op_time`="
							+ threedaysago + ")c ON b.type_name=c.type_name WHERE a.`op_time`=" + yesterday);

//			String tableheader = String.format("%28s%20s%20s%20s\n", "type_name", yesterday, twodaysago, threedaysago);
//			sb.append(tableheader);
			
			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0),以及单元格内容为test
			Label field1_ = new Label(0, 0, "type_name", format);
			Label field2_ = new Label(1, 0, yesterday, format);
			Label field3_ = new Label(2, 0, twodaysago, format);
			Label field4_ = new Label(3, 0, threedaysago, format);
			// 将定义好的单元格添加到工作表中
			sheet2.addCell(field1_);
			sheet2.addCell(field2_);
			sheet2.addCell(field3_);
			sheet2.addCell(field4_);
			
			int j = 0;

			while (rs.next()) {
				j++;
				String type = rs.getString(1);
				String one = rs.getString(2);
				String two = rs.getString(3);
				String three = rs.getString(4);
				
				Label value1 = new Label(0, j, type);
				Label value2 = new Label(1, j, numberFormat(one));
				Label value3 = new Label(2, j, numberFormat(two));
				Label value4 = new Label(3, j, numberFormat(three));
				
				sheet2.addCell(value1);
				sheet2.addCell(value2);
				sheet2.addCell(value3);
				sheet2.addCell(value4);

//				String tablecontent = String.format("%28s%20s%20s%20s\n", type, numberFormat(one), numberFormat(two), numberFormat(three));
//				sb.append(tablecontent);
			}

			// 把拼装好的内容打印
//			System.out.print(sb);

			// 写入数据并关闭文件
			book.write();
			book.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (rs != null) { // 关闭记录集
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) { // 关闭声明
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) { // 关闭连接对象
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 将数字转化为千位分隔符的数字
	private static String numberFormat(String input) {
		long num = Long.parseLong(input);
		DecimalFormat df = new DecimalFormat("#,###");
		String transfer_num = df.format(num);

		return transfer_num;
	}

	public static void main(String[] args) {
		DBConnect();
	}
}