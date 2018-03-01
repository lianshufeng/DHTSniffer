package com.fast.dht.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {

	private final static String CallBackName = "callback";

	private final static JsonEncoding encoding = JsonEncoding.UTF8;

	private final static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 设置文件的response头
	 * 
	 * @param response
	 * @param contentType
	 * @param fileName
	 * @param contentLenth
	 * @throws Exception
	 */
	public static void setFileHeader(HttpServletResponse response, String contentType, String fileName,
			long contentLenth) throws Exception {
		response.setContentType(contentType);
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (null != fileName) {
			response.addHeader("Content-Disposition",
					"attachment;fileName*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
		}
		response.addHeader("Content-Length", String.valueOf(contentLenth));
	}

	/**
	 * 将输入流的内容写到response的outputstream里
	 * 
	 * @param response
	 * @param inputStream
	 */
	public static void wirteStream(HttpServletResponse response, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			StreamUtils.copy(inputStream, outputStream);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
				inputStream.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 写出json对象,支持JSONP
	 * 
	 * @param request
	 * @param response
	 * @param content
	 * @throws Exception
	 */
	public static void write(final HttpServletRequest request, final HttpServletResponse response, final Object content)
			throws Exception {
		// 取出jsonp的方法名
		String jsonpFunction = request.getParameter(CallBackName);
		// 写出响应头
		writeHeader(jsonpFunction, response);
		// 写出内容
		writeContent(jsonpFunction, response, content);
	}

	/**
	 * 写出json对象,支持JSONP
	 * 
	 * @param jsonpFunction
	 * @param response
	 * @param content
	 * @throws Exception
	 */
	public static void write(final String jsonpFunction, final HttpServletResponse response, final Object content)
			throws Exception {
		// 写出响应头
		writeHeader(jsonpFunction, response);
		// 写出内容
		writeContent(jsonpFunction, response, content);
	}

	/**
	 * 写出字节集
	 * 
	 * @param request
	 * @param response
	 * @param head
	 * @param bin
	 * @throws Exception
	 */
	public static void write(final HttpServletRequest request, final HttpServletResponse response,
			final Map<String, String> head, final byte[] bin) throws Exception {
		if (head != null) {
			for (Entry<String, String> entry : head.entrySet()) {
				response.setHeader(entry.getKey(), entry.getValue());
			}
		}
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(bin);
		outputStream.close();
	}

	/**
	 * 写出JSON响应头
	 * 
	 * @param request
	 * @param response
	 */
	private static void writeHeader(final String jsonpFunction, final HttpServletResponse response) {
		String contentType = jsonpFunction == null ? "application/json" : "text/javascript";
		response.setHeader("Content-Type", contentType + ";charset=" + encoding.getJavaName());
		response.setCharacterEncoding(encoding.getJavaName());
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.addDateHeader("Expires", 1L);
	}

	/**
	 * 像管道里写出数据
	 * 
	 * @param stream
	 * @param object
	 * @throws IOException
	 */
	private static void writeContent(final String jsonpFunction, final HttpServletResponse response,
			final Object object) throws IOException {
		JsonGenerator generator = objectMapper.getFactory().createGenerator(response.getOutputStream(), encoding);
		writePrefix(generator, jsonpFunction);
		objectMapper.writeValue(generator, object);
		writeSuffix(generator, jsonpFunction);
		generator.flush();
	}

	// json前缀，考虑jsonp的情况
	private static void writePrefix(final JsonGenerator generator, final String jsonpFunction) throws IOException {
		if (jsonpFunction != null) {
			generator.writeRaw(jsonpFunction + "(");
		}
	}

	// json后缀，考虑json情况
	private static void writeSuffix(final JsonGenerator generator, final String jsonpFunction) throws IOException {
		if (jsonpFunction != null) {
			generator.writeRaw(");");
		}
	}

}
