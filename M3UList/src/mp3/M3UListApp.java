package mp3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
					List<String> listOrj = null;
					try {
						listOrj = Util.getStringListFromFile(file);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					int orjFak = 0;
					LinkedHashMap<String, List<Integer>> map = new LinkedHashMap<String, List<Integer>>();
					int maxAdet = 0;
					if (listOrj != null) {
						TreeMap<Integer, String> solistMap = new TreeMap<Integer, String>();
						boolean devam = true;
						int islemAdet = 0;
						while (devam) {
							List<String> list = new ArrayList<String>(listOrj);
							map.clear();
							solistMap.clear();
							int sira = 0;
							maxAdet = 0;
							++islemAdet;
							for (String line : list) {
								++sira;
								if (line.startsWith("#EXTINF:")) {
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

								}

							}
							boolean degisti = false, farkVar = false;
							int solistKok2 = (int) Math.sqrt((double) map.size()), parcaKok2 = (int) Math.sqrt((double) listOrj.size() / 2);
							if (orjFak <= 0) {
								if (orjFak < solistKok2)
									orjFak = solistKok2;
								if (orjFak < parcaKok2)
									orjFak = parcaKok2;

							}
							try {
								for (String key : map.keySet()) {
									List<Integer> siralar = map.get(key);

									if (siralar.size() > 1) {
										int oncekiYarimFark = -1;
										int siraFark = orjFak;
										if (siralar.size() > 1) {
											Double bolum = (double) listOrj.size() / (2 * siralar.size());
											if (bolum > orjFak) {
												siraFark = (int) Math.sqrt(bolum);
											}
											if (islemAdet < 2)
												System.out.println(key + " " + siralar.size() + " : " + siraFark);

										}
										for (int i = 1; i < siralar.size(); i++) {
											int simdikiSira = siralar.get(i), oncekiSira = siralar.get(i - 1);
											int fark = (simdikiSira - oncekiSira) / 2;
											boolean yenidenSirala = false;
											if (fark < siraFark) {
												if (islemAdet > 3) {
													if (key.startsWith("Hirai") || key.startsWith("Mustafa Ö"))
														System.out.println(islemAdet + " " + key + " " + siralar.size());
												}
												int indis = i;
												if (siraFark < oncekiYarimFark) {
													simdikiSira = oncekiSira;
													fark = oncekiYarimFark;
													indis = i - 1;
												}
												int startIndex = simdikiSira + ((siraFark - fark) * 2);
												if (solistMap.containsKey(startIndex)) {
													String solistSonraki = solistMap.get(startIndex), solist = solistMap.get(simdikiSira);
													if (solistSonraki.equals(solist)) {
														int sonrakiIndis = siralar.get(siralar.size() - 1) + (siraFark / 2);
														if (solistMap.containsKey(sonrakiIndis)) {
															startIndex = sonrakiIndis;
															solistSonraki = solistMap.get(startIndex);
															yenidenSirala = true;
														}
													}
													if (!solistSonraki.equals(solist)) {
														String b1 = listOrj.get(startIndex - 1), b2 = listOrj.get(startIndex), a1 = listOrj.get(simdikiSira - 1), a2 = listOrj.get(simdikiSira);
														listOrj.set(startIndex - 1, a1);
														listOrj.set(startIndex, a2);
														listOrj.set(simdikiSira - 1, b1);
														listOrj.set(simdikiSira, b2);
														solistMap.put(simdikiSira, solistSonraki);
														solistMap.put(startIndex, solist);
														degisti = true;
														siralar.set(indis, startIndex);
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
													startIndex = oncekiSira - ((siraFark - fark) * 2);
													if (solistMap.containsKey(startIndex)) {
														String solistOnceki = solistMap.get(startIndex), solist = solistMap.get(oncekiSira);
														int solistSira = i - 1;
														if (solistOnceki.equals(solist)) {
															int ilkIndis = siralar.get(0) - (siraFark / 2);
															if (solistMap.containsKey(ilkIndis)) {
																startIndex = ilkIndis;
																solistOnceki = solistMap.get(startIndex);
																oncekiSira = simdikiSira;
																solistSira = i;
																yenidenSirala = true;
															}
														}
														if (!solistOnceki.equals(solist)) {
															String b1 = listOrj.get(startIndex - 1), b2 = listOrj.get(startIndex), a1 = listOrj.get(oncekiSira - 1), a2 = listOrj.get(oncekiSira);
															siralar.set(solistSira, startIndex);
															listOrj.set(startIndex - 1, a1);
															listOrj.set(startIndex, a2);
															listOrj.set(oncekiSira - 1, b1);
															listOrj.set(oncekiSira, b2);
															solistMap.put(oncekiSira, solistOnceki);
															solistMap.put(startIndex, solist);
															List<Integer> siralarDiger = map.get(solistOnceki);
															yenidenSirala = true;
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

													for (int j = 0; j < siralar.size() - 1; j++) {
														for (int k = j + 1; k < siralar.size(); k++) {
															int deger1 = siralar.get(j), deger2 = siralar.get(k);
															if (deger1 > deger2) {
																siralar.set(j, deger2);
																siralar.set(k, deger1);
															}
														}
													}
													degisti = true;
													if (islemAdet > 3)
														System.out.println(key + " " + siralar.size());
												}

												oncekiYarimFark = (siralar.get(i) - siralar.get(i - 1)) / 2;
											} else
												oncekiYarimFark = (simdikiSira - oncekiSira) / 2;

										}
									}

								}
							} catch (Exception e2) {
								e2.printStackTrace();
								System.out.println("Ýþlem Adet : " + islemAdet + " " + new Date());
							}

							System.out.println("Ýþlem Adet : " + islemAdet + " " + new Date());
							if (degisti == false) {
								StringBuffer sb = new StringBuffer();
								list.clear();
								sb.append(listOrj.get(0) + "\n");
								int i = 2;
								while (i < listOrj.size()) {
									String str = listOrj.get(i).trim();
									if (!list.contains(str)) {
										list.add(str);
										if (str.trim().length() > 0) {
											sb.append(listOrj.get(i - 1) + "\n");
											sb.append(str + "\n");
										}
									} else
										System.out.println(str);
									i = i + 2;

								}
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
