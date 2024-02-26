package mp3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class M3UListApp {

	public static void main(String[] args) {
		final JFrame myFrame = new JFrame();
		final JFileChooser chooser = new JFileChooser("I:\\Data");
		final JButton btn = new JButton("Dosya seç");
		btn.setBounds(50, 20, 100, 40);
		chooser.setBounds(50, 50, 500, 500);
		myFrame.setTitle("Mp3 Liste Kontrol");
		myFrame.setSize(800, 600);
		chooser.setDialogTitle("Dosya Seç");
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileNameExtensionFilter("MP3 List Dosya", "m3u8"));
		chooser.setLocale(Util.TR_LOCALE);
		chooser.setApproveButtonText("Seç");
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 800, 80);
		jPanel.add(btn);
		myFrame.add(chooser);
		myFrame.add(jPanel);
		chooser.setVisible(false);
		myFrame.setVisible(true);

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btn.setVisible(false);
				chooser.setSelectedFile(null);
				chooser.setSelectedFiles(null);
				chooser.setVisible(true);
			}
		});
		chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				chooser.setVisible(false);

				if (actionCommand.equals(JFileChooser.APPROVE_SELECTION)) {
					File file = chooser.getSelectedFile();
					List<String> listDosya = null;
					try {
						listDosya = Util.getStringListFromFile(file);
					} catch (Exception e2) {
						e2.printStackTrace();
					}

					String ilkSatir = null;
					List<Liste> listOrj = new ArrayList<Liste>();
					Liste listeIlk = null;
					List<String> list = new ArrayList<>();
					for (Iterator iterator = listDosya.iterator(); iterator.hasNext();) {
						String line = (String) iterator.next();
						if (ilkSatir != null) {
							if (line.startsWith("#EXTINF:")) {
								listeIlk = null;
								if (list.contains(line))
									continue;
								list.add(line);
								listeIlk = new Liste(line, null);
								listOrj.add(listeIlk);
							} else if (listeIlk != null)
								listeIlk.setValue(line);
						} else
							ilkSatir = line;

					}
					list = null;
					int orjFak = 0;
					LinkedHashMap<String, List<Integer>> map = new LinkedHashMap<String, List<Integer>>();
					int maxAdet = 0;
					if (listOrj != null) {
						TreeMap<Integer, String> solistMap = new TreeMap<Integer, String>();
						boolean devam = true;
						int islemAdet = 0;
						while (devam) {
							map.clear();
							solistMap.clear();
							int sira = 0;
							maxAdet = 0;
							++islemAdet;
							System.out.println("Ýþlem Adet : " + islemAdet + " " + new Date() + "\n");

							for (Liste liste : listOrj) {
								liste.setNumValue(sira);
								String line = (String) liste.getId();
								String str = line.substring(line.indexOf(",") + 1);
								int endIndex = str.indexOf("-");
								if (endIndex > 0) {
									String solist = str.substring(0, endIndex).trim();
									solistMap.put(sira, solist);
									List<Integer> siralar = map.containsKey(solist) ? map.get(solist) : new ArrayList<Integer>();
									if (siralar.isEmpty())
										map.put(solist, siralar);
									siralar.add(sira);

									if (siralar.size() > maxAdet)
										maxAdet = siralar.size();
								}

								++sira;
							}
							boolean degisti = false, farkVar = false;
							int solistKok2 = (int) Math.sqrt((double) map.size()), parcaKok2 = (int) Math.sqrt((double) listOrj.size());
							if (orjFak <= 0) {
								if (orjFak < solistKok2)
									orjFak = solistKok2;
								if (orjFak < parcaKok2)
									orjFak = parcaKok2;

							}

							try {
								int toplamSarkiAdet = listOrj.size();
								for (String key : map.keySet()) {
									List<Integer> solistSarkiSirasi = map.get(key);
									int solistSarkiAdet = solistSarkiSirasi.size();
									if (solistSarkiAdet > 1) {
										boolean guncellendi = false;
										int oncekiYarimFark = -1;
										int siraFark = orjFak;
										Double bolum = (double) toplamSarkiAdet / (solistSarkiAdet);
										if (bolum > orjFak) {
											siraFark = (int) Math.sqrt(bolum * 2);
											// siraFark = (int) (bolum * 0.5d);
										}

										if (islemAdet < 2)
											System.out.println(islemAdet + ". " + key + " " + solistSarkiSirasi.toString() + " : " + siraFark);

										for (int i = 1; i < solistSarkiAdet; i++) {
											int simdikiSira = solistSarkiSirasi.get(i), oncekiSira = solistSarkiSirasi.get(i - 1);
											int fark = (simdikiSira - oncekiSira);
											boolean yenidenSirala = false;
											if (fark < siraFark) {

												int indis = i;
												if (siraFark < oncekiYarimFark) {
													simdikiSira = oncekiSira;
													fark = oncekiYarimFark;
													indis = i - 1;
												}
												Liste listeSimdi = listOrj.get(simdikiSira);
												int startIndex = simdikiSira + ((siraFark - fark));
												if (solistMap.containsKey(startIndex)) {
													String solistSonraki = solistMap.get(startIndex), solist = solistMap.get(simdikiSira);
													if (solistSonraki.equals(solist)) {
														int sonrakiIndis = solistSarkiSirasi.get(solistSarkiAdet - 1) + siraFark;
														if (solistMap.containsKey(sonrakiIndis)) {
															startIndex = sonrakiIndis;
															solistSonraki = solistMap.get(startIndex);
														}
													}
													if (!solistSonraki.equals(solist)) {
														guncellendi = true;
														Liste listeStart = listOrj.get(startIndex);
														listOrj.set(startIndex, listeSimdi);
														listOrj.set(simdikiSira, listeStart);
														solistMap.put(startIndex, solist);
														solistMap.put(simdikiSira, solistSonraki);
														degisti = true;
														solistSarkiSirasi.set(indis, startIndex);
														List<Integer> siralarDiger = map.get(solistSonraki);
														yenidenSirala = true;
														for (int j = 0; j < siralarDiger.size(); j++) {
															if (siralarDiger.get(j) == startIndex) {
																siralarDiger.set(j, simdikiSira);
															}
														}
													}
												}
												if (yenidenSirala == false) {
													startIndex = oncekiSira - ((siraFark - fark));
													if (solistMap.containsKey(startIndex)) {
														String solistOnceki = solistMap.get(startIndex), solist = solistMap.get(oncekiSira);
														if (solistOnceki.equals(solist)) {
															int ilkIndis = solistSarkiSirasi.get(0) - (siraFark);
															if (solistMap.containsKey(ilkIndis)) {
																startIndex = ilkIndis;
																solistOnceki = solistMap.get(startIndex);
																oncekiSira = simdikiSira;
																yenidenSirala = true;
															}
														}
														if (!solistOnceki.equals(solist)) {
															guncellendi = true;
															Liste listeStart = listOrj.get(startIndex), listeOnceki = listOrj.get(oncekiSira);
															// System.out.println(listeStart.getId() + " " + listeOnceki.getId());
															listOrj.set(startIndex, listeOnceki);
															listOrj.set(oncekiSira, listeStart);
															solistMap.put(oncekiSira, solistOnceki);
															solistMap.put(startIndex, solist);
															solistSarkiSirasi.set(indis, startIndex);
															List<Integer> siralarDiger = map.get(solistOnceki);
															yenidenSirala = true;
															degisti = true;
															for (int j = 0; j < siralarDiger.size(); j++) {
																if (siralarDiger.get(j) == startIndex) {
																	siralarDiger.set(j, oncekiSira);

																}

															}

														}

													} else
														farkVar = true;
												}
												if (yenidenSirala) {
													degisti = true;
												}
												oncekiYarimFark = (solistSarkiSirasi.get(i) - solistSarkiSirasi.get(i - 1)) / 2;
											} else
												oncekiYarimFark = (simdikiSira - oncekiSira) / 2;
											oncekiYarimFark += islemAdet;
										}
										if (guncellendi)
											System.out.println(islemAdet + ". " + key + " " + solistSarkiSirasi.toString() + " : " + siraFark);

									} else {
										// Liste listeSimdi = listOrj.get(solistSarkiSirasi.get(0));
										// System.err.println(key + " " + listeSimdi.getNumValue() + "\n");
									}
								}
								if (degisti == false)
									devam = false;
							} catch (Exception e2) {
								e2.printStackTrace();
								System.out.println("Ýþlem Adet : " + islemAdet + " " + new Date() + "\n");
							}
							if (degisti == false) {
								StringBuffer sb = new StringBuffer();
								List<String> strList = new ArrayList<String>();
								sb.append(ilkSatir + "\n");
								for (Liste listeSon : listOrj) {
									String str = (String) listeSon.getId();
									// System.out.println(listeSon.getNumValue() + " -->" + str);
									if (!strList.contains(str)) {
										strList.add(str);
										if (str.trim().length() > 0) {
											sb.append(str + "\n");
											sb.append(listeSon.getValue() + "\n");
										}
									} else {
										System.err.println(str);
									}

								}
								strList = null;
								if (sb.length() > 0) {
									String fileName = file.getAbsolutePath().replaceAll("m3u8", "m3u");
									File txt = new File(fileName);
									if (txt.exists())
										txt.delete();
									String content = sb.toString();
									try {
										Util.fileWrite(content, fileName);
									} catch (Exception e1) {
										e1.printStackTrace();
									}

									devam = !txt.exists();
									if (devam == false) {
										System.out.println(fileName + " oluþtu.");
										JOptionPane.showMessageDialog(null, fileName);
									}
								}
								listOrj = null;
								sb = null;
							} else if (farkVar)
								devam = false;

						}

					}
				}

				btn.setVisible(true);

			}
		});
	}
}
