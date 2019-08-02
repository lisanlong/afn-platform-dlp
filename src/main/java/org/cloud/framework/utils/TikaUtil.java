package org.cloud.framework.utils;

import org.apache.commons.io.FileUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Tika工具类
 *
 * @author hexin
 * @date 2018/11/12
 */
public class TikaUtil {
	private TikaUtil() {
	}

	/**
	 * 用Tika获取文件的媒体类型
	 */
	public static String getMimeType(File file) {
		if (file.isDirectory()) {
			return "file/directory";
		}

		AutoDetectParser parser = new AutoDetectParser();
		parser.setParsers(new HashMap<>());

		Metadata metadata = new Metadata();
		metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());

		InputStream stream;
		try {
			stream = new FileInputStream(file);
			parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return metadata.get(HttpHeaders.CONTENT_TYPE);
	}

	/**
	 * 获取文件的文本内容并写入到指定文件
	 *
	 * @param file     源文件，需要抽取内容的文件
	 * @param textFile 目标文件，抽到的文本后写入的文件
	 */
	public static void parseTextToFile(File file, File textFile) throws IOException, TikaException {
		// Metadata metadata = new Metadata();
		// try (
		// InputStream stream = TikaInputStream.get(file.toPath(), metadata);
		// FileOutputStream fos = new FileOutputStream(textFile);
		// BufferedOutputStream bos = new BufferedOutputStream(fos);
		// OutputStreamWriter writer = new OutputStreamWriter(bos,
		// Charset.forName("UTF-8"))
		// ) {
		// WriteOutContentHandler handler = new WriteOutContentHandler(writer);
		// Parser parser = new AutoDetectParser(TikaConfig.getDefaultConfig());
		// ParseContext context = new ParseContext();
		// context.set(Parser.class, parser);
		// parser.parse(stream, new BodyContentHandler(handler), metadata, context);
		// writer.flush();
		// } catch (SAXException e) {
		// throw new TikaException("Unexpected SAX processing failure", e);
		// }
		try (FileOutputStream fos = new FileOutputStream(textFile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				OutputStreamWriter writer = new OutputStreamWriter(bos, Charset.forName("UTF-8"))) {
			parseText(file, writer);
		}
	}

	public static void parseText(File file, Writer writer) throws IOException, TikaException {
		Metadata metadata = new Metadata();
		try (InputStream stream = TikaInputStream.get(file.toPath(), metadata)
		// FileOutputStream fos = new FileOutputStream(textFile);
		// BufferedOutputStream bos = new BufferedOutputStream(fos);
		// OutputStreamWriter writer = new OutputStreamWriter(bos,
		// Charset.forName("UTF-8"))
		) {
			WriteOutContentHandler handler = new WriteOutContentHandler(writer);
			Parser parser = new AutoDetectParser(TikaConfig.getDefaultConfig());
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			parser.parse(stream, new BodyContentHandler(handler), metadata, context);
			writer.flush();
		} catch (SAXException e) {
			throw new TikaException("Unexpected SAX processing failure", e);
		}
	}

	public static String getParseText(File file) throws IOException, TikaException {
		if(file.getName().toLowerCase().endsWith(".txt")) {
			return FileUtils.readFileToString(file);
		}
		InputStream input = new FileInputStream(file);// 可以写文件路径，pdf，word，html等
		BodyContentHandler textHandler = new BodyContentHandler();
		Metadata matadata = new Metadata();// Metadata对象保存了作者，标题等元数据
		Parser parser = new AutoDetectParser();// 当调用parser，AutoDetectParser会自动估计文档MIME类型，此处输入pdf文件，因此可以使用PDFParser
		ParseContext context = new ParseContext();
		try {
			parser.parse(input, textHandler, matadata, context);// 执行解析过程
		} catch (SAXException e) {
			e.printStackTrace();
		}
		input.close();
		return textHandler.toString();
	}
	public static void main(String[] args) {
		try {
//			String s  = getParseText(new File("D:\\corpus\\test\\20190719120451050.txt"));
			
			parseTextToFile(new File("D:/corpus/test/20190203024249456.xls"), new File("D:/corpus/test/20190203024249456.txt"));
//			System.out.println("s: "+s.replaceAll("[\n]+[ \t\n]*", "\n"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}
	}
}
