package com.asiainfo.ctc.eda.Mail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DAO_ini {

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

		//用于封装发邮件的内容
		StringBuilder sb = new StringBuilder();

		//昨天,前天,大前天
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

		cal.add(Calendar.DATE, -1);
		String twodaysago = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

		cal.add(Calendar.DATE, -1);
		String threedaysago = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

		sb.append("大家好：\n    大数据平台" + yesterday + "日数据如下\n");

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM all_sum_data a WHERE a.`op_time`=20151130;");

			//将输出内容格式化之后再输出
			//%ns -- 中的n表示整个字符串的长度,若长度不够,左边补空格
			String header = String.format("%s%36s%22s%17s%17s\n", "op_time", "type_name", "data_num", "partition_num",
					"size_num");
			sb.append(header);

			while (rs.next()) {
				String op_time = rs.getString(1); // 此方法比较高效
				String type_name = rs.getString(2);
				String data_num = rs.getString(3);
				String partition_num = rs.getString(4);
				String size_num = rs.getString(5);

				String content = String.format("%s%35s%22s%12s%22s\n", op_time, type_name, data_num, partition_num,
						size_num);
				sb.append(content);
			}

			sb.append("\n三日内数据量统计：\n");

			//聚合三天的数据大小
			rs = stmt.executeQuery(
					"SELECT a.`type_name`,a.`size_num`,b.size_num,c.size_num FROM check_data.`all_sum_data` a LEFT OUTER JOIN (SELECT type_name,size_num FROM check_data.`all_sum_data` sec WHERE sec.`op_time`="
							+ twodaysago
							+ ")b ON a.`type_name`=b.type_name LEFT OUTER JOIN (SELECT type_name,size_num FROM check_data.`all_sum_data` third WHERE third.`op_time`="
							+ threedaysago + ")c ON b.type_name=c.type_name WHERE a.`op_time`=" + yesterday);

			String tableheader = String.format("%28s%20s%20s%20s\n", "type_name", yesterday, twodaysago, threedaysago);
			sb.append(tableheader);

			while (rs.next()) {
				String type = rs.getString(1);
				String one = rs.getString(2);
				String two = rs.getString(3);
				String three = rs.getString(4);

				String tablecontent = String.format("%28s%20s%20s%20s\n", type, numberFormat(one), numberFormat(two),
						numberFormat(three));
				sb.append(tablecontent);
			}

			//把拼装好的内容打印
			System.out.print(sb);
		} catch (SQLException e) {
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