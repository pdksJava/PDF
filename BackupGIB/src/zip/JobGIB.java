package zip;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class JobGIB {

	public static void main(String[] args) {
		File cfg = BackUpZip.bilgiDosya;
		if (cfg.exists()) {
			try {
				String jsonStr = new String(Util.getFileByteArray(cfg));
				JSONObject map = new JSONObject(jsonStr);
				Integer yil = null;
				if (args != null && args.length == 1)
					try {
						int deger = Integer.parseInt(args[0]);
						yil = deger;
					} catch (Exception e) {
					}
				if ((yil != null || map.has("yil")) && map.has("anaKlasor") && map.has("yedekKlasor")) {
					if (yil == null)
						yil = Integer.parseInt((String) map.get("yil"));
					String klasorAdi = (String) map.get("anaKlasor");
					File anaDosya = new File(klasorAdi);
					String yedekKlasorAdi = (String) map.get("yedekKlasor");
					File yedekDosya = new File(yedekKlasorAdi);
					String mesaj = zipGIBDosyaYedekle(yil, anaDosya, yedekDosya);
					if (mesaj != null && mesaj.length() > 0)
						System.out.println(mesaj + " " + Util.convertToDateString(new Date(), "yyyy-MM-dd HH:ss"));
				}
				map = null;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * @param klasor
	 * @param dosyalar
	 */
	private static void dosyaEkle(File klasor, List<File> dosyalar) {
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
	 * @param inputFile
	 * @param list
	 * @param yil
	 */
	private static void dosyaBul(File inputFile, List<File> list, String yil) {
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
	 * @param yil
	 * @param anaDosya
	 * @param yedekDosya
	 * @return
	 */
	public static String zipGIBDosyaYedekle(int yil, File anaDosya, File yedekDosya) {
		StringBuffer sb = new StringBuffer();
		if (yil >= BackUpZip.baslangicYil) {
			if (anaDosya.exists() && yedekDosya.exists()) {
				List<File> klasorler = new ArrayList<>(), dosyalar = null;
				dosyaBul(anaDosya, klasorler, "." + yil);
				if (!klasorler.isEmpty()) {
					dosyalar = new ArrayList<>();
					for (File klasor : klasorler)
						dosyaEkle(klasor, dosyalar);
					if (!dosyalar.isEmpty()) {
						String anaDosyaPath = anaDosya.getPath(), yedekDosyaPath = yedekDosya.getPath();
						if (yedekDosya.exists()) {
							int adet = Util.dosyaKopyala(anaDosyaPath, yedekDosyaPath, dosyalar);
							if (adet > 0)
								sb.append(yil + " yýlýna ait " + adet + " kopyalandý.");
							else if (anaDosyaPath.equals(yedekDosyaPath))
								sb.append(yedekDosyaPath + " yedekleme klasörünü farklý seçin!");
							else
								sb.append(yil + " yýlýna ait kopyalanacak yeni dosya bulunamadý!");
						} else
							sb.append(yedekDosyaPath + " yedekleme klasörünü bulunamadý!");

					} else
						sb.append(yil + " yýlýna ait kopyalanacak dosya bulunamadý!");
					dosyalar = null;
				} else
					sb.append(anaDosya.getPath() + " " + yil + " yýlýna dönem dosyalarý bulunamadý!");
				klasorler = null;
			} else {
				if (!anaDosya.exists())
					sb.append(anaDosya.getPath() + " bulunamadý!");
				if (!yedekDosya.exists())
					sb.append(yedekDosya.getPath() + " bulunamadý!");
			}
		} else
			sb.append(BackUpZip.baslangicYil + " yýlýndan küçük olamaz!");

		String mesaj = sb.toString();
		sb = null;
		return mesaj;
	}

}
