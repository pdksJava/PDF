package zip;

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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Util {

	public static final Locale TR_LOCALE = new Locale("tr", "TR");
	
	/**
	 * @param klasor
	 * @param dosyalar
	 */
	public static void dosyaEkle(File klasor, List<File> dosyalar) {
		if (klasor != null && klasor.isDirectory()) {
			File[] files = klasor.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					dosyaEkle(file, dosyalar);
				} else {
					String fileName = file.getName();
					int lastIndex = fileName.lastIndexOf(".");
					if (lastIndex > 0) {
						String extName = file.getName().substring(lastIndex + 1);
						if (extName.equalsIgnoreCase("zip") && fileName.indexOf("-DR-") < 0) {
							dosyalar.add(file);
						}
					}

				}
			}
		}
	}
	/**
	 * @param dosyaPath
	 * @param backDosyaPath
	 * @param klasorler
	 * @return
	 */
	public static int dosyaKopyala(String dosyaPath,String backDosyaPath ,List<File> klasorler) {
		int adet=0;

		if (!dosyaPath.equalsIgnoreCase(backDosyaPath)) {

			File file = new File(backDosyaPath);
			if (file.exists() == false)
				file.mkdirs();
			for (File zipFile : klasorler) {
				String dosyaAdi = Util.replaceAllManuel(zipFile.getPath(), dosyaPath, backDosyaPath);
				String yeniPath = Util.replaceAllManuel(zipFile.getParent(), dosyaPath, backDosyaPath);
				File fileYedek = new File(yeniPath);
				if (!fileYedek.exists())
					file.mkdirs();

				try {
					File fileNew = new File(dosyaAdi);
					if (fileNew.exists() == false || zipFile.lastModified() != fileNew.lastModified()) {
						Files.copy(zipFile.toPath(), fileNew.toPath(), StandardCopyOption.REPLACE_EXISTING);
						++adet;
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
		return adet;
	}

	/**
	 * @param inputFile
	 * @param list
	 * @param yil
	 */
	public static void dosyaBul(File inputFile, List<File> list, String yil) {
		if (inputFile != null && inputFile.isDirectory()) {
			File[] files = inputFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					if (file.getName().endsWith(yil)) {
						list.add(file);

					} else
						dosyaBul(file, list, yil);
				}
			}
		}

	}

	/**
	 * @param jsonString
	 * @return
	 */
	public static String toPrettyFormat(String jsonString) {
		String prettyJson = null;
		try {
			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(jsonString).getAsJsonObject();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			prettyJson = gson.toJson(json);
			if (prettyJson.lastIndexOf("\\u0026") > 0)
				prettyJson = replaceAllManuel(prettyJson, "\\u0026", "&");
			if (prettyJson.lastIndexOf("\\u003d") > 0)
				prettyJson = replaceAllManuel(prettyJson, "\\u003d", "=");
		} catch (Exception e) {
			prettyJson = jsonString;
		}
		return prettyJson;
	}

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static List<String> getStringListFromFile(File file) throws Exception {
		List<String> list = null;
		if (file != null && file.exists()) {
			BufferedReader reader;

			InputStream in = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = reader.readLine();
			LinkedHashMap<String, List<Integer>> map = new LinkedHashMap<String, List<Integer>>();
			int sira = 0;
			list = new ArrayList<String>();
			while (line != null) {
				if (line.startsWith("#EXTINF:")) {
					String str = line.substring(line.indexOf(",") + 1);
					int endIndex = str.indexOf("-");
					if (endIndex > 0) {
						list.add(line);
						++sira;
						String key = str.substring(0, endIndex);
						List<Integer> siralar = map.containsKey(key) ? map.get(key) : new ArrayList<Integer>();
						if (siralar.isEmpty())
							map.put(key, siralar);
						siralar.add(sira);
					}

				}
				// read next line
				line = reader.readLine();
			}

		}
		return list;
	}

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

	public static void fileWrite(String content, String fileName) throws Exception {
 		Writer printWriter = null;
		FileOutputStream fos = null;
		String dosyaAdi = fileName;
		File file = new File(dosyaAdi);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {

			}
		}

		try {
			fos = new FileOutputStream(dosyaAdi, false);
			printWriter = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
			printWriter.write(content + "\n");
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
				// fos.flush();
				// fos.close();
			}
		}

	}

	/**
	 * @param str
	 * @param pattern
	 * @param replace
	 * @return
	 */
	public static String replaceAllManuel(String str, String pattern, String replace) {
		StringBuffer lSb = new StringBuffer();
		if ((str != null) && (pattern != null) && (pattern.length() > 0) && (replace != null)) {
			int i = 0;
			int j = str.indexOf(pattern, i);
			int l = pattern.length();
			int m = str.length();
			if (j > -1) {
				while (j > -1) {
					if (i != j) {
						lSb.append(str.substring(i, j));
					}
					lSb.append(replace);
					i = j + l;
					j = (i > m) ? -1 : str.indexOf(pattern, i);
				}
				if (i < m) {
					lSb.append(str.substring(i));
				}
			} else {
				lSb.append(str);
			}
			str = lSb.toString();

		}

		return str;
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
	 * @param inputPdfList
	 * @param outputStream
	 * @throws Exception
	 */
	public static void mergePdfFiles(List<InputStream> inputPdfList, OutputStream outputStream) throws Exception {
	}
}
