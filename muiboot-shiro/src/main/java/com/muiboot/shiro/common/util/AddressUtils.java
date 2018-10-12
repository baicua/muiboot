package com.muiboot.shiro.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* <p>Description: ip解析工具</p>
* @version 1.0 2018/10/12
* @author jin
*/
public class AddressUtils {

	private static String getAddresses(String content, String encodingString) throws UnsupportedEncodingException {
		String urlStr = "http://whois.pconline.com.cn/ipJson.jsp";
		String returnStr = getResult(urlStr, content, encodingString);
		if (returnStr != null) {
			returnStr = decodeUnicode(returnStr);
			String[] temp = returnStr.split(",");
			if (temp.length < 3) {
				return "0";
			}
			return returnStr;
		}
		return null;
	}

	private static String getResult(String urlStr, String content, String encoding) {
		URL url;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(2000);
			connection.setReadTimeout(2000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(content);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	private static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuilder outBuffer = new StringBuilder(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	public static String getRealAddressByIP(String ip, ObjectMapper mapper) {
		String address = "";
		try {
			address = getAddresses("json=true&ip=" + ip, "gbk");
			JsonNode node = mapper.readTree(address);
			//JsonNode data = node.get("data");
			String region = node.get("pro").asText();
			String city = node.get("city").asText();
			address = region + "" + city;
		} catch (Exception ignored) {

		}
		return address;
	}

}