package mp3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class Util {

	public static final Locale TR_LOCALE = new Locale("tr", "TR");

	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	public static String convertToDateString(Date date, String pattern) {
		String tarih = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, TR_LOCALE);
			tarih = sdf.format(date);
		} catch (Exception e) {
			tarih = "";
		}
		return tarih;
	}

	/**
	 * @param bytes
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static File byteArrayToFile(byte[] bytes, String fileName) throws Exception {

		File file = new File(fileName);

		OutputStream os = new FileOutputStream(file);
		os.write(bytes);

		os.close();

		return file;
	}

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] getFileByteArray(File file) throws Exception {
		byte[] dosyaIcerik = new byte[(int) file.length()];
		InputStream ios = null;
		try {
			ios = new FileInputStream(file);
			if (ios.read(dosyaIcerik) == -1) {
				throw new IOException("EOF reached while trying to read the whole file");
			}
		} finally {
			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
			}
		}
		return dosyaIcerik;

	}

	/**
	 * @param str
	 * @param findStr
	 * @param replace
	 * @return
	 */
	public static String replaceAllManuel(String str, String findStr, String replace) {
		if ((str != null) && (findStr != null) && (findStr.length() > 0) && (replace != null)) {
			int l = findStr.length();
			while (str.indexOf(findStr) >= 0) {
				StringBuffer lSb = new StringBuffer();
				int i = 0;
				int j = str.indexOf(findStr, i);
				int m = str.length();
				if (j > -1) {
					while (j > -1) {
						if (i != j)
							lSb.append(str.substring(i, j));
						lSb.append(replace);
						i = j + l;
						j = (i > m) ? -1 : str.indexOf(findStr, i);
					}
					if (i < m)
						lSb.append(str.substring(i));

				} else
					lSb.append(str);

				str = lSb.toString();
				if (replace.contains(findStr))
					break;
				// if (replace.indexOf(findStr) >= 0)
				// break;
			}
		}

		return str;
	}

	/**
	 * @param str
	 * @param findStr
	 * @param replace
	 * @return
	 */
	public static String replaceAll(String str, String findStr, String replace) {
		if (str != null && findStr != null && replace != null && str.contains(findStr)) {
			if (replace.contains(findStr))
				str = str.replaceAll(findStr, replace);
			else
				str = replaceAllManuel(str, findStr, replace);
		}
		return str;
	}

	/**
	 * @param xml
	 * @param map
	 * @return
	 */
	public static String getStringReplaceMap(String xml, LinkedHashMap<String, String> map) {
		String data = xml;
		if (data != null && map != null) {
			for (String pattern : map.keySet()) {
				String replace = map.get(pattern);
				if (data.indexOf(replace) >= 0) {
					data = replaceAll(data, replace, pattern);
				}
			}
		}
		return data;
	}

	/**
	 * @param str
	 * @return
	 */
	public static String getUTF8String(String str) {
		String data = str;
		if (data != null) {
			LinkedHashMap<String, String> map1 = getUnicodeMap();
			// LinkedHashMap<String, String> map1 = getUTF8Map();
			// LinkedHashMap<String, String> map2 = getUnicodeMap();
			// LinkedHashMap<String, String> map3 = getUnicodeHexMap();
			// map1.putAll(map2);
			// map1.putAll(map3);
			data = getStringReplaceMap(data, map1);
			map1 = null;
			// map2 = null;
			// map3 = null;
		}
		return data;

	}

	/**
	 * @return
	 */
	public static LinkedHashMap<String, String> getUTF8Map() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("&#304;", "İ");
		map.put("&#305;", "ı");
		map.put("&#214;", "Ö");
		map.put("&#246;", "ö");
		map.put("&#220;", "Ü");
		map.put("&#252;", "ü");
		map.put("&#199;", "Ç");
		map.put("&#231;", "ç");
		map.put("&#286;", "Ğ");
		map.put("&#287;", "ğ");
		map.put("&#350;", "Ş");
		map.put("&#351;", "ş");
		return map;
	}

	/**
	 * @return
	 */
	public static LinkedHashMap<String, String> getUnicodeMap() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("\u015E", "Ş");
		map.put("\u015F", "ş");
		map.put("\u011E", "Ğ");
		map.put("\u011F", "ğ");
		map.put("\u00D6", "Ö");
		map.put("\u00F6", "ö");
		map.put("\u00DC", "Ü");
		map.put("\u00FC", "ü");
		map.put("\u00C7", "Ç");
		map.put("\u00E7", "ç");
		map.put("\u0130", "İ");
		map.put("\u0131", "ı");
		return map;
	}

	/**
	 * @return
	 */
	public static LinkedHashMap<String, String> getUnicodeHexMap() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("&#x11E;", "Ğ");
		map.put("&#x11F;", "ğ");
		map.put("&#x130;", "İ");
		map.put("&#x131;", "ı");
		map.put("&#x15E;", "Ş");
		map.put("&#x15F;", "ş");
		map.put("&#xDC;", "Ü");
		map.put("&#xFC;", "ü");
		map.put("&#xD6;", "Ö");
		map.put("&#xF6;", "ö");
		return map;
	}

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static List<String> getStringListFromFile(File file) throws Exception {
		List<String> list = null;
		if (file != null && file.exists()) {
			list = new ArrayList<String>();
			// BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			InputStream in = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.indexOf("\uFEFF") >= 0)
					System.out.println("");
				list.add(line);
			}

			// String line = reader.readLine();
			// while (line != null) {
			// list.add(line);
			// line = reader.readLine();
			// }
		}
		return list;
	}

	public static void fileWrite(String content, String fileName) throws Exception {
		fileWrite(content, fileName, Boolean.FALSE);
	}

	public static void fileWriteEkle(String content, String fileName) throws Exception {
		fileWrite(content, fileName, Boolean.TRUE);
	}

	/**
	 * @param content
	 * @param fileName
	 * @param ekle
	 * @throws Exception
	 */
	private static void fileWrite(String content, String fileName, Boolean ekle) throws Exception {
		String path = "/tmp/pdks";
		if (fileName.indexOf("\\") > 0)
			fileName = fileName.replaceAll("\\\\", "/");
		File tmp = new File(path);
		boolean devam = tmp.exists();
		if (!devam) {
			try {
				devam = tmp.mkdirs();
			} catch (Exception e) {

				e.printStackTrace();

			}
		}
		if (devam) {
			Writer printWriter = null;
			FileOutputStream fos = null;
			String dosyaAdi = (fileName.indexOf("/") < 0 ? path + "/" : "") + fileName + (fileName.indexOf(".") < 0 ? ".xml" : "");
			File file = new File(dosyaAdi);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {

				}
			}

			try {
				fos = new FileOutputStream(dosyaAdi, ekle);
				printWriter = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
				printWriter.write(content + "\n");
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				if (printWriter != null) {
					printWriter.flush();
					printWriter.close();
					fos.flush();
					fos.close();
				}
			}

		}

	}

}
