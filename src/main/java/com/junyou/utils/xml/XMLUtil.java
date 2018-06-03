package com.junyou.utils.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.junyou.utils.exception.GameCustomException;


/**
 * @author DaoZheng Yuan
 * 2014年11月15日 上午10:52:03
 */
public class XMLUtil {
	
	public static Document loadFromFile(String sFilePathName) throws GameCustomException {
		InputStream is = null;
		try {
			is = new FileInputStream(sFilePathName);
		} catch (FileNotFoundException e) {
			throw new GameCustomException("load file '" + sFilePathName + "' failed " + e.getMessage());
		}
		return loadDocument(is);
	}

	public static Document loadDocument(InputStream is) throws GameCustomException {
		SAXReader rd = new SAXReader();
		Document document = null;
		try {
			document = rd.read(is);
		} catch (Exception e) {
			throw new GameCustomException("parsing document failed:" + e.getMessage());
		}
		return document;
	}

	public static int attributeValueInt(Element element, String attr) throws GameCustomException {
		if (element == null) {
			throw new GameCustomException("element == null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			throw new GameCustomException(String.format("缺少属性%s: %s", attr, element.asXML()));
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new GameCustomException(String.format("属性%s不是数值类型: %s", attr, element.asXML()));
		}
	}

	public static float attributeValueFloat(Element element, String attr) throws GameCustomException {
		if (element == null) {
			throw new GameCustomException("element == null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			throw new GameCustomException(String.format("缺少属性%s: %s", attr, element.asXML()));
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			throw new GameCustomException(String.format("属性%s不是数值类型: %s", attr, element.asXML()));
		}
	}

	public static int attributeValueInt(Element element, String attr, int defaultValue) throws GameCustomException {
		if (element == null) {
			throw new GameCustomException("element == null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new GameCustomException(String.format("属性%s不是数值类型: %s", attr, element.asXML()));
		}
	}

	public static String attributeValueString(Element element, String attr) throws GameCustomException {
		if (element == null) {
			throw new GameCustomException("element == null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			throw new GameCustomException(String.format("缺少属性%s: %s", attr, element.asXML()));
		}
		return value;
	}
	
	public static String attributeValueStringOrNull(Element element, String attr) throws GameCustomException {
		if (element == null) {
			throw new GameCustomException("element == null");
		}
		return element.attributeValue(attr);
	}

	public static String attributeValueString(Element element, String attr, String defaultValue) throws GameCustomException {
		if (element == null) {
			throw new GameCustomException("element == null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static boolean attributeValueBoolean(Element element, String attr, boolean defaultValue) throws GameCustomException {
		if (element == null) {
			throw new GameCustomException("element == null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(value);
		} catch (NumberFormatException e) {
			throw new GameCustomException(String.format("属性%s不是布尔类型: %s", attr, element.asXML()));
		}
	}

	public static boolean attributeValueBoolean(Element element, String attr) throws GameCustomException {
		if (element == null) {
			throw new GameCustomException("element == null");
		}
		String value = element.attributeValue(attr);
		if (value == null) {
			throw new GameCustomException(String.format("缺少属性%s: %s", attr, element.asXML()));
		}
		try {
			return Boolean.parseBoolean(value);
		} catch (NumberFormatException e) {
			throw new GameCustomException(String.format("属性%s不是布尔类型: %s", attr, element.asXML()));
		}
	}

	public static Element subElement(Element parent, String name) throws GameCustomException {
		if (parent == null) {
			throw new GameCustomException("parent == null");
		}
		Element result = parent.element(name);
		if (result == null) {
			throw new GameCustomException(String.format("找不到%s节点的子节点%s", parent.getName(), name));
		}
		return result;
	}
}
