package com.neuedu.log;

import java.util.Date;
import java.util.Random;


public class Test {
	public static String day = "20160607";
	static Random r = new Random();

	public static void main(String[] args) {
	//	AnalyticsEngineSDK.onChargeSuccess("orderid123", "zhangsan");
	//	AnalyticsEngineSDK.onChargeRefund("orderid456", "lisi");



		System.out.println();
		String s=NeueduAnalyticsEngineSDK.recordPaidOrderLog("127.0.0.1","1111111","12",String.valueOf(System.currentTimeMillis()),System.currentTimeMillis()+"");
		System.out.println(s);
		String sc=NeueduAnalyticsEngineSDK.recordCancelOrderLog("127.0.0.1","1111111","12",String.valueOf(System.currentTimeMillis()),System.currentTimeMillis()+"");
		System.out.println(sc);
	}
}
