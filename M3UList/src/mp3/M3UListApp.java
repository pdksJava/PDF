package mp3;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class M3UListApp {

	public static void main(String[] args) {
		File[] roots = File.listRoots();
		String rootStr = "";
		for (int i = 0; i < roots.length; i++) {
			File root = new File(roots[i] + "Data\\");
			if (root.exists())
				rootStr = root.getPath();

		}

		Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
		final JFrame myFrame = new JFrame();
		myFrame.setLayout(null);
		myFrame.setTitle("Mp3 Liste Kontrol");
		myFrame.setSize(850, 650);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLocationRelativeTo(null);
		final JFileChooser chooser = new JFileChooser(rootStr);
		final JButton btn = new JButton("Çalma Listesi seç");
		final JFileChooser chooser1 = new JFileChooser(rootStr);
		final JButton btn1 = new JButton("Mp3 Klasör kontrol");
		final JCheckBox dosyaKontrol = new JCheckBox();
		final JLabel baslik = new JLabel("Dosya Kontrol");
		final JTextArea textArea = new JTextArea();
		final JScrollPane scrollPane = new JScrollPane(textArea);
		baslik.setBounds(100, 50, 150, 40);
		dosyaKontrol.setBounds(300, 50, 40, 40);
		scrollPane.setBounds(100, 100, 650, 400);
		btn.setBounds(100, 20, 200, 40);
		btn1.setBounds(350, 20, 200, 40);
		textArea.setBounds(100, 150, 400, 400);
		chooser1.setBounds(100, 150, 400, 400);
		chooser.setBounds(100, 150, 400, 400);
		scrollPane.setVisible(true);
		myFrame.setTitle("Mp3 Liste Kontrol");
		myFrame.setSize(800, 600);
		chooser.setDialogTitle("Dosya Seç");
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileNameExtensionFilter("MP3 List Dosya", "m3u8"));
		chooser.setLocale(Util.TR_LOCALE);
		chooser.setApproveButtonText("Seç");
		chooser1.setDialogTitle("Klasör Seç");
		chooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser1.setApproveButtonText("Klasör Seç");

		Container cp = myFrame.getContentPane();
		cp.add(baslik);
		cp.add(dosyaKontrol);
		cp.add(btn);
		cp.add(btn1);
		cp.add(chooser);
		cp.add(chooser1);
		cp.add(scrollPane);

		textArea.setEditable(false);
		chooser1.setVisible(false);
		chooser.setVisible(false);
		myFrame.setVisible(true);
		textArea.setVisible(false);
		textArea.setEditable(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setVisible(false);
				textArea.setText("");
				btn.setVisible(false);
				chooser.setSelectedFile(null);
				chooser.setSelectedFiles(null);
				chooser.setVisible(true);
			}
		});

		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btn1.setVisible(false);
				chooser1.setSelectedFile(null);
				textArea.setVisible(false);
				textArea.setText("");
				chooser1.setVisible(true);
			}
		});
		chooser1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				StringBuffer sb = new StringBuffer();
				chooser1.setVisible(false);
				if (actionCommand.equals(JFileChooser.APPROVE_SELECTION)) {
					File file = chooser1.getSelectedFile();
					mp3KontrolEt(file, sb);
					if (sb.length() > 0) {
						textArea.setText(sb.toString());
						textArea.setVisible(true);
					}
				}
				sb = null;

				btn1.setVisible(true);
			}
		});
		chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				chooser.setVisible(false);

				if (actionCommand.equals(JFileChooser.APPROVE_SELECTION)) {
					StringBuffer sb = new StringBuffer();
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
					int index = listDosya.size();
					String klasor = file.getParentFile().getPath();
					boolean yazildi = false, duzgunBasladi = false;
					String baslik = null;
					for (Iterator iterator = listDosya.iterator(); iterator.hasNext();) {
						String satir = (String) iterator.next();
						if (list.contains(satir))
							continue;
						String line = Util.getUTF8String(satir);
						if (ilkSatir != null) {
							File mp3File = null;
							if (line.startsWith("#EXTINF:")) {
								duzgunBasladi = true;
								yazildi = false;
								baslik = line;

							} else if (yazildi == false && baslik != null) {
								if (line.indexOf("xxxxssd") > 0)
									klasor = file.getParentFile().getPath();
								String gercekStr = line;
								if (line.startsWith(".")) {
									gercekStr = klasor + line.substring(1);
									mp3File = new File(gercekStr);
								} else
									mp3File = new File(line);
								if (mp3File.exists()) {

									if (dosyaKontrol.isSelected() == false) {
										list.add(baslik);
										listeIlk = new Liste(baslik, null);
										listOrj.add(listeIlk);
										listeIlk.setValue(line);
										yazildi = true;
									} else {
										Tag tag = null;
										String mp3Adi = mp3File.getName();
										mp3Adi = mp3Adi.substring(0, mp3Adi.lastIndexOf("."));
										String dosyaAdi = null;
										try {
											AudioFile audioFile = AudioFileIO.read(mp3File);
											tag = audioFile.getTag();
											if (tag != null) {
												String sarkiAdi = tag.getFirst(FieldKey.TITLE);
												String sanatci = tag.getFirst(FieldKey.ARTIST);
												// String album = tag.getFirst(FieldKey.ALBUM);
												// String yil = tag.getFirst(FieldKey.YEAR);
												// String janr = tag.getFirst(FieldKey.GENRE);
												dosyaAdi = sanatci + " - " + sarkiAdi;
											}
										} catch (Exception e1) {
											e1.printStackTrace();
										}

										if (tag == null) {
											int sonKarakter1 = line.lastIndexOf("/"), sonKarakter2 = line.lastIndexOf("\\");
											if (sonKarakter1 > 0 || sonKarakter2 > 0) {
												int dosyaIndex = sonKarakter2 > sonKarakter1 ? sonKarakter2 : sonKarakter1;
												dosyaAdi = line.substring(dosyaIndex + 1);
												dosyaAdi = dosyaAdi.substring(0, dosyaAdi.indexOf(".")).trim();
											}
										}
										if (dosyaAdi != null) {
											if (mp3Adi.trim().equals(dosyaAdi.trim())) {
												Liste liste = new Liste("#EXTINF:" + (++index) + "," + dosyaAdi.trim(), line);
												listOrj.add(liste);
												yazildi = true;
											} else {
												sb.append(dosyaAdi + " -> " + mp3File.getAbsolutePath());

											}

										}
									}
								} else
									sb.append(gercekStr + " dosya yok!\n");
							}

						} else
							ilkSatir = line;

					}
					if (sb.length() > 0) {
						textArea.setText(sb.toString());
						textArea.setVisible(true);
					}
					sb = null;
					list = null;
					int orjFak = 0;
					LinkedHashMap<String, List<Integer>> map = new LinkedHashMap<String, List<Integer>>();
					int maxAdet = 0;
					if (listOrj != null) {
						TreeMap<Integer, String> solistMap = new TreeMap<Integer, String>();
						boolean devam = true;
						int islemAdet = 0;
						while (devam) {
							++islemAdet;
							boolean degisti = false, farkVar = false;
							try {
								map.clear();
								solistMap.clear();
								int sira = 0;
								maxAdet = 0;

								System.out.println("işlem Adet : " + islemAdet + " " + new Date() + "\n");

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

								int solistKok2 = (int) Math.sqrt((double) map.size()), parcaKok2 = (int) Math.sqrt((double) listOrj.size());
								if (orjFak <= 0) {
									if (orjFak < solistKok2)
										orjFak = solistKok2;
									if (orjFak < parcaKok2)
										orjFak = parcaKok2;

								}

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

													} else {
														farkVar = true;
													}

												}
												if (yenidenSirala) {
													degisti = true;
													for (int j = 0; j < solistSarkiSirasi.size() - 1; j++) {
														for (int k = j + 1; k < solistSarkiSirasi.size(); k++) {
															int deger1 = solistSarkiSirasi.get(j), deger2 = solistSarkiSirasi.get(k);
															if (deger1 > deger2) {
																solistSarkiSirasi.set(j, deger2);
																solistSarkiSirasi.set(k, deger1);
															}
														}
													}

												}
												oncekiYarimFark = (solistSarkiSirasi.get(i) - solistSarkiSirasi.get(i - 1)) / 2;
											} else
												oncekiYarimFark = (simdikiSira - oncekiSira) / 2;
											// if (islemAdet < fark * 2)
											oncekiYarimFark += (islemAdet % 2 == 0 ? 1 : 2);
										}
										if (guncellendi)
											System.out.println(islemAdet + ". " + key + " " + solistSarkiSirasi.toString() + " : " + siraFark);

									}
								}
								if (degisti == false)
									devam = false;
							} catch (Exception e2) {
								e2.printStackTrace();
								System.err.println("işlem Adet : " + islemAdet + " " + new Date() + "\n" + e2);
							}
							if (degisti == false || farkVar || islemAdet > 50) {
								sb = new StringBuffer();
								if (ilkSatir == null || ilkSatir.contains("EXTM3U") == false)
									sb.append("#EXTM3U\n");
								List<String> strList = new ArrayList<String>();
								if (duzgunBasladi)
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
										sb.append(str + "\n");
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
										// Util.fileWrite(content, fileName);
										BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), StandardCharsets.UTF_8));
										writer.write(content);
										writer.flush();
										writer.close();
									} catch (Exception e1) {
										e1.printStackTrace();
									}

									devam = !txt.exists();
									if (devam == false) {
										System.out.println(fileName + " oluştu.");
										JOptionPane.showMessageDialog(null, fileName);
									}
								}
								listOrj = null;
								sb = null;
							} else if (farkVar)
								devam = false;

						}

					}

					sb = null;
				}

				btn.setVisible(true);

			}
		});
	}

	/**
	 * @param klasor
	 * @param label
	 * @param sb
	 */
	private static void mp3KontrolEt(File klasor, StringBuffer sb) {
		File[] dosyalar = klasor != null ? klasor.listFiles() : null;
		if (dosyalar != null) {

			Tag tag = null;
			for (int i = 0; i < dosyalar.length; i++) {
				File mp3File = dosyalar[i];
				if (mp3File.isDirectory()) {
					mp3KontrolEt(mp3File, sb);
				} else if (mp3File.isFile() && mp3File.getName().toLowerCase().endsWith(".mp3")) {
					String line = mp3File.getAbsolutePath();
					String oldStr = line;
					if (line.indexOf(" .") > 0 || line.indexOf("  ") > 0 || (line.indexOf("-") > 0 && line.indexOf("-") == line.lastIndexOf("-"))) {
						if (line.indexOf(" .") > 0)
							System.err.println(line + " boşluk at");
						if (line.indexOf("-") > 0 && line.indexOf("-") == line.lastIndexOf("-"))
							line = line.replaceAll("-", " - ");
						while (line.indexOf("  ") > 0)
							line = line.replaceAll("  ", " ");
						if (line.equals(oldStr) == false) {
							System.err.println(mp3File.getAbsolutePath() + " boşluk " + line + " düzenlendi!");
							mp3File.renameTo(new File(line));
							mp3File = new File(line);
						}
					}
					try {
						AudioFile audioFile = AudioFileIO.read(mp3File);
						tag = audioFile.getTag();
						String dosyaAdi = "";
						String mp3Adi = mp3File.getName();
						mp3Adi = mp3Adi.substring(0, mp3Adi.lastIndexOf("."));
						if (tag != null) {
							String sarkiAdi = tag.getFirst(FieldKey.TITLE);
							String sanatci = tag.getFirst(FieldKey.ARTIST);
							dosyaAdi = sanatci.trim() + " - " + sarkiAdi.trim();
							if (dosyaAdi.equals(mp3Adi) == false) {
								String str = oldStr + " --> " + dosyaAdi;
								sb.append(str + "\n");
								// System.err.println(str);

							}
						}
					} catch (Exception e2) {

					}
				}
			}
		}
	}
}
