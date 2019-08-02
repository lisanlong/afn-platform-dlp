package org.cloud.framework.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.util.*;

public class ReadXml extends DefaultHandler {
	private StringReader sr = null;  
	private InputSource is = null;
	private Document doc = null;
	private Element root = null;
	private String xpath="";
	private boolean field;
	private int startIndex;//从第几块儿开始读取
	private int pageSize;
	private int index = 1;//记录快数
	private int ind = 1;//记录有效快数
	SAXReader reader=null;
	public List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	private Stack<String> sk = new Stack<String>();//存放标签
	private StringBuffer sb=new StringBuffer();

	public ReadXml(String xpath, int startIndex, int pageSize, boolean field) {
		super();
		this.xpath = xpath;
		this.field = field;
		this.startIndex=startIndex;
		this.pageSize=pageSize;
	}
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		System.out.println("SAX解析开始");
	}
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		System.out.println("SAX解析结束");
	}
	/**
	 * 用来遍历xml文件的开始标签
	 * 解析xml元素
	 */
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if((field && list.size()>0)){
		}else{
			if(sk.size()==1 && sb.length() == 0){
				sb.append("<"+sk.firstElement()+">");
			}
			sb.append("<"+qName+">");
			sk.push(qName);
		}
	}
	/**
	 * 用来遍历xml文件的结束标签
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if((field && list.size()>0)){
		}else{
			if(sk.size()>1){
				String s = sk.get(1);
				String pop = sk.pop();
				if(pop.equals(s)){
					try {
						if(ind <= pageSize && index > startIndex){
							sb.append("</"+pop+">");
							sb.append("</"+sk.get(0)+">");
							sr = new StringReader(sb.toString());  
							is = new InputSource(sr);
							reader = new SAXReader();
							doc = reader.read(is);
							root = doc.getRootElement();
							getListMap(root);
							ind = ind + 1;
						}
						index = index + 1;
						
					} catch (DocumentException e) {
					}
					sb.setLength(0);
				}else{
					sb.append("</"+qName+">");
				}
			}
		}
		
	}
	/**
	* 获取文本
	* 重写charaters()方法时，
	* String(byte[] bytes,int offset,int length)的构造方法进行数组的传递
	* 去除解析时多余空格
	*/
	@Override
	public void characters(char[] ch, int start, int length)throws SAXException {
		super.characters(ch, start, length);
		if((field && list.size()>0)){
		}else{
			String value = new String(ch, start, length);
			 if(!value.trim().equals("")){//如果value去掉空格后不是空字符串
				String s = "";
				char[] charArray = value.toCharArray();
				for (char c : charArray) {
					if (!(c < 0x9 || c > 0x9 && c < 0xA || c > 0xA && c < 0xD || c > 0xD && c  
							< 0x20 || c > 0xD7FF && c < 0xE000 || c > 0xFFFD)){//下面了允许的字符范围
						s += c;
					}
				}
				sb.append(s);
			 }
		}
	}
	@SuppressWarnings("unchecked")
	public void getListMap(Element root){
		if(index>=startIndex){
			Map<String,Object> m = new HashMap<String, Object>();
			List<Element> selectNodes = root.selectNodes(xpath);
			List<Element> elements = selectNodes.get(0).elements();
			for (Element e : elements) {
				m.put(e.getName(), e.getText());
			}
			list.add(m);
		}else{
			list.clear();
		}
	}
}
