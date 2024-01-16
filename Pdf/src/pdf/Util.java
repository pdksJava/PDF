package pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class Util {

	public static final Locale TR_LOCALE = new Locale("tr", "TR");

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
		// Create document and pdfReader objects.
		Document document = new Document();

		// Create writer for the outputStream
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);

		// Open document.
		document.open();

		// Contain the pdf data.
		PdfContentByte pageContentByte = writer.getDirectContent();

		for (InputStream inputStream : inputPdfList) {
			PdfReader reader = new PdfReader(inputStream);
			int numberOfPages = reader.getNumberOfPages();
			for (int i = 1; i <= numberOfPages; i++) {
				// import the page from source pdf
				PdfImportedPage page = writer.getImportedPage(reader, i);
				if (page.getWidth() > page.getHeight())
					document.setPageSize(PageSize.A4.rotate());
				else
					document.setPageSize(PageSize.A4);

				document.newPage();
				// add the page to the destination pdf
				pageContentByte.addTemplate(page, 0, 0);
			}

			reader.close();
			// Create page and add content.

		}

		// Close document and outputStream.
		outputStream.flush();
		document.close();
		outputStream.close();
	}
}
