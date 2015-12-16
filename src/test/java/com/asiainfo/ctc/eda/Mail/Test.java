package com.asiainfo.ctc.eda.Mail;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Test {

	@org.junit.Test
	public void formatTest() throws Exception {
//		System.out.println("hellfdsafdsafdaso    world");
//		System.out.println("you\tworld");
		
		String op_time = "20151204";
		String type_name = "t_source_aaa_3g_wifi 3G_WIFI";
		String data_num = "5467";
		String partition_num = "1";
		String size_num = "1413072";
		
		int firstSpace = (50-op_time.length());
		int secondSpace = (50-type_name.length());
		int thirdSpace = (20-data_num.length());
		int fourthSpace = (20-partition_num.length());
		
		System.out.printf("%s%"+firstSpace+"s%"+secondSpace+"s%"+thirdSpace+"s%"+fourthSpace+"s\n",op_time,type_name,data_num,partition_num,size_num);
	}
	
	@org.junit.Test
	public void dateTest() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		
		cal.add(Calendar.DATE, -1);
		String twodaysago = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		
		cal.add(Calendar.DATE, -1);
		String threedaysago = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		
		System.out.println(yesterday);
		System.out.println(twodaysago);
		System.out.println(threedaysago);
	}
	
	@org.junit.Test
	public void numberFormat() throws Exception {
		long n =10000000;
		DecimalFormat df = new DecimalFormat("#,###");
		String m = df.format(n);
		System.out.print(m);
	}
	
	@org.junit.Test
	public void test() throws Exception {
		String str = "t_source_dpi_3g 3G_DPI";
		System.out.println(str.length());
		System.out.println(40-str.length());
		System.out.printf("%23s",str);
	}
}
