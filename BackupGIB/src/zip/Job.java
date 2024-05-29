package zip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Job {
	
	
	public static void main(String[] args) {
		File cfg = Sakla.bilgiDosya;
		if (cfg.exists()) {
			try {
				String jsonStr = new String(Util.getFileByteArray(cfg));
				JSONObject map = new JSONObject(jsonStr);
				if (map.has("yil") && map.has("anaKlasor") && map.has("yedekKlasor")) {
					int yil = Integer.parseInt((String) map.get("yil"));
					String klasorAdi = (String) map.get("anaKlasor");
					File anaDosya = new File(klasorAdi);
					klasorAdi = (String) map.get("anaKlasor");
					File yedekDosya = new File(klasorAdi);
					if (anaDosya.exists() && yedekDosya.exists()) {
						List<File> klasorler = new ArrayList<>(), dosyalar = null;
						Util.dosyaBul(anaDosya, klasorler, "." + yil);
 						if (!klasorler.isEmpty()) {
							int adet = 0;
							dosyalar = new ArrayList<>();
							for (File klasor : klasorler)
								Util.dosyaEkle(klasor, dosyalar);
							if (!dosyalar.isEmpty())
								adet = Util.dosyaKopyala(anaDosya.getPath(), yedekDosya.getPath(), dosyalar);
							if (adet > 0)
								System.out.println(yil + " yýlýna ait " + adet + " kopyalandý.");
							else
								System.out.println(yil + " yýlýna ait kopyalanacak yeni dosya bulunamadý.");

						} else
							System.err.println((String) map.get("anaKlasor") + " dosyalar bulunamadý!");
					} else {
						if (!anaDosya.exists())
							System.err.println((String) map.get("anaKlasor") + " bulunamadý!");
						if (!yedekDosya.exists())
							System.err.println((String) map.get("yedekKlasor") + " bulunamadý!");

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
