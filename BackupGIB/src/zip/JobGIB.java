package zip;

import java.io.File;
import java.util.ArrayList;
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
						if (deger >= BackUpZip.baslangicYil)
							yil = deger;
					} catch (Exception e) {
					}
				if ((yil != null || map.has("yil")) && map.has("anaKlasor") && map.has("yedekKlasor")) {
					if (yil == null)
						yil = Integer.parseInt((String) map.get("yil"));
					String klasorAdi = (String) map.get("anaKlasor");
					File anaDosya = new File(klasorAdi);
					klasorAdi = (String) map.get("anaKlasor");
					File yedekDosya = new File(klasorAdi);
					if (anaDosya.exists() && yedekDosya.exists()) {
						List<File> klasorler = new ArrayList<>(), dosyalar = null;
						Util.dosyaBul(anaDosya, klasorler, "." + yil);
						if (!klasorler.isEmpty()) {
							dosyalar = new ArrayList<>();
							for (File klasor : klasorler)
								Util.dosyaEkle(klasor, dosyalar);
							if (!dosyalar.isEmpty()) {
								int adet = Util.dosyaKopyala(anaDosya.getPath(), yedekDosya.getPath(), dosyalar);
								if (adet > 0)
									System.out.println(yil + " y�l�na ait " + adet + " kopyaland�.");
								else
									System.out.println(yil + " y�l�na ait kopyalanacak yeni dosya bulunamad�!");
							} else
								System.out.println(yil + " y�l�na ait kopyalanacak dosya bulunamad�!");
							dosyalar = null;
						} else
							System.err.println((String) map.get("anaKlasor") + " dosyalar bulunamad�!");
						klasorler = null;
					} else {
						if (!anaDosya.exists())
							System.err.println((String) map.get("anaKlasor") + " bulunamad�!");
						if (!yedekDosya.exists())
							System.err.println((String) map.get("yedekKlasor") + " bulunamad�!");
					}
				}
				map = null;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
