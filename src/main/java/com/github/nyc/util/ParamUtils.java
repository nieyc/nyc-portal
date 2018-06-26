package com.github.nyc.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import static org.apache.commons.lang.StringUtils.*;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParamUtils {

	private static final Logger LOG = Logger.getLogger(ParamUtils.class);
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat DATE_FORMAT_HMS = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat DATE_FORMAT_YMD_HMS = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_H_M_S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.00");
	
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	
	/**
	 * map转xml
	 * @param root
	 * @param encoding
	 * @param params
	 * @return
	 */
    public static String mapToXml(String root, String encoding, Map<String, Object> params) {
    	StringBuilder postDataXML = new StringBuilder();
        postDataXML.append("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
        postDataXML.append("<" + root + ">");
        if(params != null) {
        	for(Map.Entry<String, Object> param : params.entrySet()) {
        		postDataXML.append("<" + param.getKey() + "><![CDATA[" + param.getValue() + "]]></" + param.getKey() + ">");
        	}
        }
        postDataXML.append("</" + root + ">");
        return postDataXML.toString();
    }
    
    /**
     * xml转map
     * @param xmlStr
     * @return
     */
    public static Map<String, Object> xmlToMap(String xmlStr) {
    	Map<String, Object> map = new HashMap<String, Object>(0);
    	
    	Node root = null;
    	try {
    		root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(IOUtils.toInputStream(xmlStr, defaultIfEmpty(substringBetween(xmlStr, "encoding=\"", "\""), "UTF-8"))).getFirstChild();
		} catch (Exception e) {
			LOG.error(e);
		}
    	
    	if(root != null) {
	    	xmlToMap(map, root);
    	}
    	return map;
    }
    
    private static void xmlToMap(Map<String, Object> map, Node node) {
    	NodeList childNodes = node.getChildNodes();
    	if(childNodes.getLength() == 1 && childNodes.item(0).getNodeType() != Node.ELEMENT_NODE) {
    		map.put(node.getNodeName(), node.getTextContent());
    	} else {
	    	for(int i = childNodes.getLength() - 1; i >= 0; i--) {
	    		xmlToMap(map, childNodes.item(i));
	    	}
    	}
    } 

	/**
	 * map转参数串
	 * @param params
	 * @return
	 */
    public static String mapToLink(Map<String, ?> params) {
		StringBuilder paramsStr = new StringBuilder();
		if(params != null && params.size() > 0) {
			for(Entry<String, ?> param : new TreeMap<>(params).entrySet()) {
				if(param.getValue() != null) {
					if (param.getValue().getClass().isArray()) {
						Arrays.sort((Object[])param.getValue());
						for(Object value : (Object[])param.getValue()) {
							if (value != null && StringUtils.isNotBlank(value.toString())) {
								paramsStr.append(param.getKey() + "=" + value + "&");
							}
						}
					} else if (StringUtils.isNotBlank(param.getValue().toString())) {
						paramsStr.append(param.getKey() + "=" + param.getValue() + "&");
					}
				}
			}
			paramsStr.delete(paramsStr.length() - 1, paramsStr.length());
		}
		return paramsStr.toString();
	}
	
	/**
	 * 参数串转map
	 * @param paramsStr
	 * @return
	 */
	public static Map<String, String> linkToMap(String paramsStr) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		if(isNotBlank(paramsStr)) {
			for(String parms : paramsStr.split("&")) {
				params.put(substringBefore(parms, "="), substringAfter(parms, "="));
			}
		}
		return params;
	}
	
	/**
	 * map转换
	 * @param params
	 * @return
	 */
	public static Map<String, String> to(Map<?, ?> params) {
		Map<String, String> map = new HashMap<>();
		if (params != null && !params.isEmpty()) {
			Object key = null, value = null;
			for (Entry<?, ?> entry : params.entrySet()) {
				key = entry.getKey();
				key = key != null && key.getClass().isArray() && ((Object[])key).length > 0 ? ((Object[])key)[0] : key;
				
				value = entry.getValue();
				value = value != null && value.getClass().isArray() && ((Object[])value).length > 0 ? ((Object[])value)[0] : value;
				
				map.put(key != null ? key.toString() : null, value != null ? value.toString() : null);
			}
		}
		return map;
	}
}
