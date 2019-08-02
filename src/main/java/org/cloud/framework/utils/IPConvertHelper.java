package org.cloud.framework.utils;

public class IPConvertHelper {

	/// <summary>
    /// 将IPv4字符串转换成数字
    /// </summary>
    /// <param name="ip">IPv4字符串</param>
    /// <returns>对应的无符号整数</returns>
    public static long IPToNumber(String ip){
        long result = 0;
        String[] ipSection = ip.split("\\.");
        result += (long)(Integer.parseInt(ipSection[0]) * 256 * 256) * 256;
        result += Integer.parseInt(ipSection[1]) * 256 * 256;
        result += Integer.parseInt(ipSection[2]) * 256;
        result += Integer.parseInt(ipSection[3]);

        return result;
    }

    /// <summary>
    /// 将数字装换成IP
    /// </summary>
    /// <param name="ipNumber">IP对应的无符号整数</param>
    /// <returns></returns>
    public static String NumberToIP(long ipNumber) {
        // 4段
        String result = (ipNumber % 256)+"";
        ipNumber = ipNumber / 256;

        // 3段
        result = (ipNumber % 256) + "." + result;
        ipNumber = ipNumber / 256;

        // 2段
        result = (ipNumber % 256) + "." + result;
        ipNumber = ipNumber / 256;

        // 1段
        result = ipNumber + "." + result;

        return result;
    }
    /*
    public static long getIp2long(String ip) {
        ip = ip.trim();
        String[] ips = ip.split("\\.");
       long ip2long = 0L;
       for (int i = 0; i < 4; ++i) {
            ip2long = ip2long << 8 | Integer.parseInt(ips[i]);
       }
       return ip2long;
    }
    public static long getIp2long2(String ip) {
       ip = ip.trim();
       String[] ips = ip.split("\\.");
       long ip1 = Integer.parseInt(ips[0]);
       long ip2 = Integer.parseInt(ips[1]);
       long ip3 = Integer.parseInt(ips[2]);
       long ip4 = Integer.parseInt(ips[3]);
       long ip2long =1L* ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256 + ip4;
       return ip2long;
    }
    public static boolean ipExistsInRange(String ip,String ipSection) {
       ipSection = ipSection.trim();
       ip = ip.trim();
       int idx = ipSection.indexOf('-');
       String beginIP = ipSection.substring(0, idx);
       String endIP = ipSection.substring(idx + 1);
       return getIp2long(beginIP)<=getIp2long(ip) && getIp2long(ip)<=getIp2long(endIP);
    }
    */
    public static void main(String[] args) {
//    	System.out.println(IPToNumber("10.215.4.181"));
    	System.out.println(IPToNumber("10.215.4.140"));
    	System.out.println(IPToNumber("10.215.4.150"));
//    	System.out.println(getIp2long2("10.215.4.130"));
//    	System.out.println(ipExistsInRange("192.168.1.1", "192.168.1.1-192.168.1.255"));
    	System.out.println(NumberToIP(2829083146L));
	}
}
