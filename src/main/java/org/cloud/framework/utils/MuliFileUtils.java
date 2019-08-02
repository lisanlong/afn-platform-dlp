package org.cloud.framework.utils;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 本例使用的是基于JDK HttpURLConnection的同步下载，即按顺序下载 如果同时下载多个任务，可以使用多线程
 *
 */
public class MuliFileUtils {

	@Value("${data.upload.file}")
	private static String uploadFilePath;

	// 文件打包下载
	public static void downLoadFiles(List<File> files, List<String> nameList, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			/**
			 * 创建一个临时压缩文件， 我们会把文件流全部注入到这个文件中 这里的文件你可以自定义是.rar还是.zip
			 */
			String name = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
			String tempf = "";
			if(uploadFilePath.endsWith("/") || uploadFilePath.endsWith("\\")){
				tempf = uploadFilePath + name + ".rar";
			}else{
				tempf = uploadFilePath + "/" + name + ".rar";
			}
			File file = new File(tempf);
			if (!file.exists()) {
				file.createNewFile();
			}
			response.reset();
			// response.getWriter()
			// 创建文件输出流
			FileOutputStream fous = new FileOutputStream(file);
			/**
			 * 打包的方法我们会用到ZipOutputStream这样一个输出流, 所以这里我们把输出流转换一下
			 */
			ZipOutputStream zipOut = new ZipOutputStream(fous);
			/**
			 * 这个方法接受的就是一个所要打包文件的集合， 还有一个ZipOutputStream
			 */
			zipFile(files, nameList, zipOut);
			zipOut.close();
			fous.close();
			downloadZip(file, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 直到文件的打包已经成功了， 文件的打包过程被我封装在FileUtil.zipFile这个静态方法中，
		 * 稍后会呈现出来，接下来的就是往客户端写数据了
		 */
	}

	/**
	 * 压缩zip
	 * @param files
	 * @param nameList
	 * @param ouputStream
	 */


	public static void zipFile(List<File> files,List<String> nameList, ZipOutputStream ouputStream) {
		// 要生成的压缩文件地址和文件名称
		ZipOutputStream zipStream = null;
		FileInputStream zipSource = null;
		BufferedInputStream bufferStream = null;
		try {
			// 构造最终压缩包的输出流
			zipStream = ouputStream;
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				// 将需要压缩的文件格式化为输入流
				zipSource = new FileInputStream(file);
				// 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样
				ZipEntry zipEntry = null;
				if(nameList!=null){
					zipEntry = new ZipEntry(nameList.get(i));
				}else{
					zipEntry = new ZipEntry(file.getName());
				}
				// 定位该压缩条目位置，开始写入文件到压缩包中
				zipStream.putNextEntry(zipEntry);
				// 输入缓冲流
				bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
				int read = 0;
				// 创建读写缓冲区
				byte[] buf = new byte[1024 * 10];
				while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
					zipStream.write(buf, 0, read);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (null != bufferStream)
					bufferStream.close();
				if (null != zipStream)
					zipStream.close();
				if (null != zipSource)
					zipSource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 下载压缩文件
	 * @param file
	 * @param response
	 */
	public static void downloadZip(File file, HttpServletResponse response) {
		try {
			String fname = file.getName();
			fname = URLEncoder.encode(fname, "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + fname);

			BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
			OutputStream out = response.getOutputStream();
			byte[] bs = new byte[1024];
			int len = 0;
			while ((len = br.read(bs)) > 0) {
				out.write(bs, 0, len);
			}
			out.flush();
			out.close();
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				File f = new File(file.getPath());
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 下载单个文件
	 * @param file
	 * @param fileChangeName
	 * @param response
	 */
	public static void downloadFile(File file,String fileChangeName, HttpServletResponse response) {
		try {
			String fname = file.getName();
			if(fileChangeName!=null && !fileChangeName.equals("")){
				fname = fileChangeName;
			}
			fname = URLEncoder.encode(fname, "UTF-8");
			fname = fname.replaceAll("\\+", "%20");
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + fname);
			
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
			OutputStream out = response.getOutputStream();
			byte[] bs = new byte[1024];
			int len = 0;
			while ((len = br.read(bs)) > 0) {
				out.write(bs, 0, len);
			}
			out.flush();
			out.close();
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 压缩zip
	 * @param files
	 * @param nameList
	 * @param outPath
	 */

	public static void toZip(List<File> files,List<String> nameList, String outPath) {
		try {
			File file = new File(outPath);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream fous = new FileOutputStream(outPath);
			ZipOutputStream zipOut = new ZipOutputStream(fous);
			zipFile(files, nameList, zipOut);
			zipOut.close();
			fous.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 解压zip格式的压缩文件到指定位置
	 * @param zipFileName
	 *            压缩文件
	 * @param extPlace
	 *            解压目录
	 * @throws Exception
	 */
	public static boolean unZip(String zipFileName, String extPlace)
			throws Exception {
		System.setProperty("sun.zip.encoding",
				System.getProperty("sun.jnu.encoding"));
		try {
			(new File(extPlace)).mkdirs();
			File f = new File(zipFileName);
			ZipFile zipFile = new ZipFile(zipFileName, "GBK"); // 处理中文文件名乱码的问题
			if ((!f.exists()) && (f.length() <= 0)) {
				throw new Exception("要解压的文件不存在!");
			}
			String strPath, gbkPath, strtemp;
			File tempFile = new File(extPlace);
			strPath = tempFile.getAbsolutePath();
			Enumeration<?> e = zipFile.getEntries();
			while (e.hasMoreElements()) {
				ZipEntry zipEnt = (ZipEntry) e.nextElement();
				gbkPath = zipEnt.getName();
				if (zipEnt.isDirectory()) {
					strtemp = strPath + File.separator + gbkPath;
					File dir = new File(strtemp);
					dir.mkdirs();
					continue;
				} else { // 读写文件
					InputStream is = zipFile.getInputStream(zipEnt);
					BufferedInputStream bis = new BufferedInputStream(is);
					gbkPath = zipEnt.getName();
					strtemp = strPath + File.separator + gbkPath;// 建目录
					String strsubdir = gbkPath;
					for (int i = 0; i < strsubdir.length(); i++) {
						if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
							String temp = strPath + File.separator
									+ strsubdir.substring(0, i);
							File subdir = new File(temp);
							if (!subdir.exists())
								subdir.mkdir();
						}
					}
					FileOutputStream fos = new FileOutputStream(strtemp);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					int c;
					while ((c = bis.read()) != -1) {
						bos.write((byte) c);
					}
					bos.close();
					fos.close();
					bis.close();
					is.close();
				}
			}
			zipFile.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据原始rar路径，解压到指定文件夹下.
	 * @param srcRarPath
	 *            原始rar路径
	 * @param dstDirectoryPath
	 *            解压到的文件夹
	 */
	public static void unRar(String srcRarPath, String dstDirectoryPath) {
		if (!srcRarPath.toLowerCase().endsWith(".rar")) {
			System.out.println("非rar文件！");
			return;
		}
		File dstDiretory = new File(dstDirectoryPath);
		if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
			dstDiretory.mkdirs();
		}
		Archive a = null;
		try {
			FileInputStream fis = new FileInputStream(new File(srcRarPath));
			a = new Archive(fis);
			if (a != null) {
				// a.getMainHeader().print(); // 打印文件信息.
				FileHeader fh = a.nextFileHeader();
				while (fh != null) {
					// 防止文件名中文乱码问题的处理
					String fileName = fh.getFileNameW().isEmpty() ? fh
							.getFileNameString() : fh.getFileNameW();
					if (fh.isDirectory()) { // 文件夹
						File fol = new File(dstDirectoryPath + File.separator
								+ fileName);
						fol.mkdirs();
					} else { // 文件
						File out = new File(dstDirectoryPath + File.separator
								+ fileName.trim());
						try {
							if (!out.exists()) {
								if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
									out.getParentFile().mkdirs();
								}
								out.createNewFile();
							}
							FileOutputStream os = new FileOutputStream(out);
							a.extractFile(fh, os);
							os.close();
							fis.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					fh = a.nextFileHeader();
				}
				a.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压 7z 格式的压缩文件 (apache的common-compress)
	 * @param orgPath 源压缩文件地址
	 * @param tarPath 解压后存放的目录地址
	 */
	public static void un7Z(String orgPath, String tarPath) {
		try {
			SevenZFile sevenZFile = new SevenZFile(new File(orgPath));
			SevenZArchiveEntry entry = sevenZFile.getNextEntry();
			while (entry != null) {
				if (entry.isDirectory()) {
					new File(tarPath + File.separator + entry.getName()).mkdirs();
					entry = sevenZFile.getNextEntry();
					continue;
				}
				FileOutputStream out = new FileOutputStream(tarPath
						+ File.separator + entry.getName());
				byte[] content = new byte[(int) entry.getSize()];
				sevenZFile.read(content, 0, content.length);
				out.write(content);
				out.close();
				entry = sevenZFile.getNextEntry();
			}
			sevenZFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String originDir = "D:/root/123/";
		String zipPath = originDir + "123.rar";
		try {
			unRar(zipPath, "D:/root/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}