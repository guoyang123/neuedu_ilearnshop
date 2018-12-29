package com.neuedu.log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 分析引擎sdk java服务器端数据收集
 * 
 * @author root
 * @version 1.0
 *
 */
public class NeueduAnalyticsEngineSDK {
	// 日志打印对象
	private static final Logger log = Logger.getGlobal();
	// 请求url的主体部分  -->订单确认页
	public static final String domain="http://www.business.top:8089";
	public static final String orderURI = "/portal/order/create.do";
	public static final String cancelOrderURI = "/portal/order/cancel.do";
	private static final String platformName = "java_server";
	private static final String sdkName = "jdk";
	private static final String version = "1";

	/**
	 * 触发订单支付成功事件，发送事件数据到服务器
	 *
	 * @param ip
	 *        客户端ip
	 * @param orderId
	 *            订单支付id
	 * @param memberId
	 *            订单支付会员id
	 * @param createOrderTime
	 *            下单时间
	 * @param paidOrderTime
	 *             支付时间
	 * @return 将返回的字符串写入日志文件即可
	 *
	 *
	 */
	public static String recordPaidOrderLog(String ip,String orderId, String memberId,String createOrderTime,
										 String paidOrderTime) {
		try {
			if (isEmpty(orderId) || isEmpty(memberId)||isEmpty(ip)) {
				// 订单id或者memberid为空
				log.log(Level.WARNING, "订单id和会员id不能为空");
				return null;
			}

			StringBuffer sBufffer=new StringBuffer(ip);
			sBufffer.append("^A").append(dateToStr(createOrderTime))
			.append("^A").append(domain)
					.append("^A").append(orderURI)
			.append("?")
			.append("c_time=").append(paidOrderTime)
			.append("&").append("oid=").append(orderId)
			.append("&").append("u_mid").append(memberId)
			.append("&").append("pl=").append(platformName)
			.append("&").append("en=e_cs")
			.append("&").append("sdk=").append(sdkName)
			.append("&ver=").append(version)
			;

			return  sBufffer.toString();
		} catch (Throwable e) {
			log.log(Level.WARNING, "发送数据异常", e);
		}
		return null;
	}

	private static String dateToStr(String datestr){
		StringBuffer stringBuffer=new StringBuffer(datestr);
		stringBuffer.insert(10,".");
		return stringBuffer.toString();
	}

	/**
	 * 触发订单退款事件，发送退款数据到服务器
	 * 
	 * @param orderId
	 *            退款订单id
	 * @param memberId
	 *            退款会员id
	 * @return 如果发送数据成功，返回true。否则返回false。
	 *
	 * 10.1.238.52(ip)^A1543993771.551(下单时间)^Ahadoop01（域名）
	 * ^A/log.gif?c_time=1543993770298(取消订单时间)&oid=orderid456（订单编号）
	 * &u_mid=lisi（用户id）&pl=java_server&en=e_cr&sdk=jdk&ver=1
	 */
	public static String recordCancelOrderLog(String  ip,String orderId, String memberId
			,String createOrderTime, String cancelOrderTime) {
		try {
			if (isEmpty(orderId) || isEmpty(memberId)||isEmpty(ip)) {
				// 订单id或者memberid为空
				log.log(Level.WARNING, "订单id和会员id不能为空");
				return null;
			}

			StringBuffer sBufffer=new StringBuffer(ip);
			sBufffer.append("^A").append(dateToStr(createOrderTime))
					.append("^A").append(domain)
					.append("^A").append(cancelOrderURI)
					.append("?")
					.append("c_time=").append(cancelOrderTime)
					.append("&").append("oid=").append(orderId)
					.append("&").append("u_mid").append(memberId)
					.append("&").append("pl=").append(platformName)
					.append("&").append("en=e_cr")
					.append("&").append("sdk=").append(sdkName)
					.append("&ver=").append(version)
			;

			return  sBufffer.toString();

		} catch (Throwable e) {
			log.log(Level.WARNING, "发送数据异常", e);
		}
		return null;
	}

	/**
	 * 根据传入的参数构建url
	 *
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String buildUrl(Map<String, String> data)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append(domain+orderURI).append("?");
		for (Map.Entry<String, String> entry : data.entrySet()) {
			if (isNotEmpty(entry.getKey()) && isNotEmpty(entry.getValue())) {
				sb.append(entry.getKey().trim())
						.append("=")
						.append(URLEncoder.encode(entry.getValue().trim(), "utf-8"))
						.append("&");
			}
		}
		return sb.substring(0, sb.length() - 1);// 去掉最后&
	}

	/**
	 * 判断字符串是否为空，如果为空，返回true。否则返回false。
	 * 
	 * @param value
	 * @return
	 */
	private static boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	/**
	 * 判断字符串是否非空，如果不是空，返回true。如果是空，返回false。
	 * 
	 * @param value
	 * @return
	 */
	private static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
}
