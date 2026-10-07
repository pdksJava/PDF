package mp3;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Quart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String saat = "1,4:20-30";
		if (zamaniGeldimi(13, 15, saat))
			System.out.println(saat);

	}

	public static boolean zamaniGeldimi(int saat, int dakika, String str) {
		boolean durum = Boolean.FALSE;
		str = str.trim();
		if (str.length() > 0) {
			String delim = null;
			int saatDeger = -1, dakikaDeger = -1;
			if (str.indexOf(":") > 0)
				delim = ":";
			else if (str.indexOf(".") > 0)
				delim = ".";

			if (delim == null) {
				delim = ":";
				str = str + delim + "0";
			}
			StringTokenizer st = new StringTokenizer(str, delim);
			String saatStr = st.nextToken();
			String dakikaStr = st.nextToken();
			List<String> saatList = new ArrayList<String>(), dakikaList = new ArrayList<String>();
			getQuartList(saatStr, saatList, 24);
			getQuartList(dakikaStr, dakikaList, 60);
			durum = false;
			for (String saatData : saatList) {
				try {
					saatDeger = Integer.parseInt(saatData);
				} catch (Exception e) {
					saatDeger = -1;
				}
				if (saatDeger < 0 || saatDeger > 23)
					continue;
				for (String dakikaData : dakikaList) {
					try {
						dakikaDeger = Integer.parseInt(dakikaData);
					} catch (Exception e) {

						dakikaDeger = -1;
					}
					if (dakikaDeger < 0 || dakikaDeger > 59)
						continue;
					st = null;

					durum = saat == saatDeger && dakika == dakikaDeger;
					if (durum)
						break;
				}
				if (durum)
					break;
			}

		}
		return durum;
	}

	/**
	 * @param str
	 * @param delim
	 * @return
	 */
	private static List<String> getListStringTokenizer(String str, String delim) {
		List<String> list = new ArrayList();
		if (delim == null) {
			delim = "";
			if (str.indexOf(",") > 0)
				delim = ",";
			else if (str.indexOf(";") > 0)
				delim = ";";
			else if (str.indexOf("|") > 0)
				delim = "|";
			else if (str.indexOf(" ") > 0)
				delim = " ";
		}
		StringTokenizer st = new StringTokenizer(str, delim);
		while (st.hasMoreTokens()) {
			String part = st.nextToken();
			list.add(part);
		}
		return list;
	}

	/**
	 * @param str
	 * @param list
	 * @param lastIndex
	 */
	private static void getQuartList(String str, List<String> list, int lastIndex) {
		if (str.equals("*") || str.equals("-") || str.equals(",")) {
			for (int i = 0; i < lastIndex; i++)
				list.add(String.valueOf(i));
		} else if (str.indexOf(",") > 0) {
			list.addAll(getListStringTokenizer(str, ","));
		} else if (str.indexOf("-") >= 0) {
			int bas = 0, bit = lastIndex;
			if (str.startsWith("-")) {
				if (str.length() > 1)
					bit = Integer.parseInt(str.substring(1));
			} else if (str.endsWith("-")) {
				bas = Integer.parseInt(str.substring(0, str.length() - 1));
			} else {
				StringTokenizer st2 = new StringTokenizer(str, "-");
				String basStr = st2.nextToken();
				String bitStr = st2.nextToken();
				bas = Integer.parseInt(basStr);
				if (bas < 0)
					bas = 0;
				bit = Integer.parseInt(bitStr) + 1;
				if (bit > lastIndex)
					bit = lastIndex;
			}

			for (int i = bas; i < bit; i++)
				list.add(String.valueOf(i));

		} else
			list.add(str);
	}

}
