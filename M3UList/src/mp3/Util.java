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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
			list = new ArrayList<String>();
			while (line != null) {
				list.add(line);
				line = reader.readLine();
			}
		}
		return list;
	}

	public static void fileWrite(String content, String fileName) throws Exception {
		fileWrite(content, fileName, Boolean.FALSE);
	}

	public static void fileWriteEkle(String content, String fileName) throws Exception {
		fileWrite(content, fileName, Boolean.TRUE);
	}

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
					// fos.flush();
					// fos.close();
				}
			}

		}

	}

}
