package org.cloud.framework.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileEncode {

	public static void main(String[] args) {


		//加密
//		encodeFile("/Users/anhuifeng/Downloads/test_jiami/1.mkv", "/Users/anhuifeng/Downloads/test_jiami/1_jiami.mkv", 1, 1);
//		//解密
//		decodeFile("/Users/anhuifeng/Downloads/test_jiami/1_jiami.mkv", "/Users/anhuifeng/Downloads/test_jiami/1_jiemi.mkv", 1, 1);

		// 加密完成135728
		// 解密完成38120

		//加密
		encodeFile("/Users/anhuifeng/Downloads/test_jiami/1.pdf", "/Users/anhuifeng/Downloads/test_jiami/1_jiami.pdf", 1, 1);
		//解密
		decodeFile("/Users/anhuifeng/Downloads/test_jiami/1_jiami.pdf", "/Users/anhuifeng/Downloads/test_jiami/1_jiemi.pdf", 1, 1);

        //加密完成1144
		//解密完成1531

	}

	/**
	 * 文件加密传输的方法
	 *
	 * @param from 原文件（带绝对路径）
	 * @param to 复制后文件（带绝对路径）
	 */
	public static void encodeFile(String from, String to, int day, int height) {
		try {
			long startTime = System.currentTimeMillis();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(to));
			int len = 0;
			// 循环写入
			while ((len = bis.read()) != -1) {// 加密算法，可行定义，与解密算法相逆
				if (day % height * len != 1) {
					bos.write(len + height);
				} else {
					bos.write(len);
				}
			}
			bis.close();
			bos.close();
			long endTime = System.currentTimeMillis();

			System.out.println("加密完成"+(endTime-startTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件解密接收的方法
	 *
	 * @param from 原文件（带绝对路径）
	 * @param to 复制后文件（带绝对路径）
	 */
	public static void decodeFile(String from, String to, int day, int height) {
		try {
			long startTime = System.currentTimeMillis();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(to));
			int len = 0;
			// 循环写入
			while ((len = bis.read()) != -1) {// 解密算法，可行定义，与解密算法相逆
				if (day % height * len != 1) {
					bos.write(len - height);
				} else {
					bos.write(len);
				}
			}
			// 关闭输入输出流
			bis.close();
			bos.close();
			long endTime = System.currentTimeMillis();
			System.out.println("解密完成"+(endTime-startTime));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
