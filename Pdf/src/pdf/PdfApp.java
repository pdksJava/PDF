package pdf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

public class PdfApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JFrame myFrame = new JFrame();
		myFrame.setTitle("Pdf Birleþtirme");
		myFrame.setSize(800, 600);
		UIManager.put("FileChooser.cancelButtonText", "Ýptal");
		UIManager.put("FileChooser.saveButtonText", "Kaydet");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JButton btnPDF = new JButton("PDF birleþtir");
		final JButton btn = new JButton("Dosya seç");
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Dosya Seç");

		chooser.setLocale(Util.TR_LOCALE);
		chooser.setApproveButtonText("Dosya Ekle");
		final JList<File> jList = new JList<File>();
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 0, 800, 80);
		btn.setBounds(50, 20, 100, 40);
		btnPDF.setBounds(200, 20, 100, 40);
		chooser.setBounds(50, 50, 500, 500);
		jList.setBounds(50, 100, 500, 400);
		jPanel.add(btn);
		jPanel.add(btnPDF);
		myFrame.add(chooser);
		myFrame.add(jList);
		myFrame.add(jPanel);
		jList.setVisible(false);
		chooser.setVisible(false);
		btnPDF.setVisible(false);
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
		final JLabel label = new JLabel();
		jList.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				//

			}

			@Override
			public void keyReleased(KeyEvent e) {
				//

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					DefaultListModel<File> model = (DefaultListModel<File>) jList.getModel();
					int selectedIndex = jList.getSelectedIndex();
					while (selectedIndex != -1) {
						model.remove(selectedIndex);
						selectedIndex = jList.getSelectedIndex();
					}
					File[] selectedFiles = null;
					List<File> items = (List<File>) Collections.list(model.elements());
					if (items != null && !items.isEmpty()) {
						selectedFiles = new File[items.size()];
						selectedFiles = items.toArray(selectedFiles);
					}
					chooser.setSelectedFiles(selectedFiles);
					btnPDF.setEnabled(false);
					jList.setVisible(items.size() > 1);
				}
			}
		});

		jList.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// System.out.println("jList mouseReleased " + new Date());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// System.out.println("jList mousePressed " + new Date());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// System.out.println("jList mouseExited " + new Date());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// System.out.println("jList mouseEntered " + new Date());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				List<File> files = jList.getSelectedValuesList();
				int adet = 0;
				if (files != null) {
					for (File file : files) {
						String extension = FilenameUtils.getExtension(file.getName());
						if (extension.equalsIgnoreCase("pdf"))
							++adet;
 					}
				}

				btnPDF.setEnabled(adet > 1);
			}
		});

		chooser.addActionListener(new ActionListener() {
 			@Override
			public void actionPerformed(ActionEvent e) {
				File[] files = null;
				DefaultListModel<File> listModel = null;
				String actionCommand = e.getActionCommand();
				if (actionCommand.equals(JFileChooser.CANCEL_SELECTION)) {
					btn.setVisible(true);
					chooser.setVisible(false);
				} else {
					if (actionCommand.equals(JFileChooser.APPROVE_SELECTION)) {
						jList.setVisible(false);
						btnPDF.setEnabled(false);
						btnPDF.setVisible(false);
						files = chooser.getSelectedFiles();
						if (files != null) {
 							if (listModel == null)
								listModel = new DefaultListModel<File>();
							List<File> items = null;
							if (jList.getModel() != null) {
								try {
									Object object = jList.getModel();
									if (object instanceof DefaultListModel) {
										@SuppressWarnings("unchecked")
										DefaultListModel<File> model = (DefaultListModel<File>) object;
										items = (List<File>) Collections.list(model.elements());
										for (File file : items) {
											listModel.addElement(file);
										}
									}
 								} catch (Exception e2) {
									System.err.println(e2);
								}
 							}
							int adet = listModel.size();
							if (items == null)
								items = new ArrayList<File>();
							for (int i = 0; i < files.length; i++) {
								File file = files[i];
								String extension = FilenameUtils.getExtension(file.getName());
								if (extension.equalsIgnoreCase("pdf")) {
									if (!listModel.contains(file)) {
										++adet;
										listModel.addElement(file);
									}
								}
							}
							if (adet == 1)
								JOptionPane.showMessageDialog(null, " En az 2 adet pdf dosyasý seçiniz!");
							jList.setVisible(adet > 1);
							if (jList.isVisible()) {
								jList.setModel(listModel);
								btnPDF.setVisible(true);
								btn.setVisible(true);
								chooser.setVisible(false);
							}
						}
					}
				}

			}
		});
		
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btn.setVisible(false);
				btnPDF.setVisible(false);
				chooser.setSelectedFile(null);
				chooser.setSelectedFiles(null);
				chooser.setVisible(true);
			}
		});

		btnPDF.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pdfOnClick(jList);

			}

			private void pdfOnClick(final JList<File> jList) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				List<InputStream> inputPdfList = new ArrayList<InputStream>();
				List<File> files = jList.getSelectedValuesList();
				String path = null;
				for (File file : files) {
					try {
						if (path == null) {
							path = file.getParent();
							if (path.indexOf(":") > 0) {
								path = path.substring(0, path.indexOf(":") + 1) + "/tmp/pdf/";
							} else
								path = "/tmp/pdf/";
							File tmp = new File(path);
							if (!tmp.exists())
								tmp.mkdirs();
						}
						byte[] source = Util.getFileByteArray(file);
						if (source != null)
							inputPdfList.add(new ByteArrayInputStream(source));
					} catch (Exception e1) {
						if (e1.getMessage() != null)
							JOptionPane.showMessageDialog(null, e1.getMessage());
					}

				}
				try {
					if (!inputPdfList.isEmpty()) {
						Util.mergePdfFiles(inputPdfList, outputStream);
						String dosyaAdi = chooser.getCurrentDirectory().getAbsolutePath() + File.separator + "merge" + inputPdfList.size() + "_" + Util.convertToDateString(new Date(), "yyyyMMdd") + ".pdf";
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setDialogTitle("Dosya Kaydet");
						fileChooser.setLocale(Util.TR_LOCALE);
						fileChooser.setSelectedFile(new File(dosyaAdi));
						fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
						int option = fileChooser.showSaveDialog(myFrame);
						if (option == JFileChooser.APPROVE_OPTION) {
							File file = fileChooser.getSelectedFile();
							dosyaAdi = file.getAbsolutePath();
							if (dosyaAdi.toLowerCase().indexOf(".pdf") < 0)
								dosyaAdi += ".pdf";
							label.setText("File Saved as: " + dosyaAdi);
							File mergeFile = new File(dosyaAdi);
							if (mergeFile.exists())
								mergeFile.deleteOnExit();
							mergeFile = Util.byteArrayToFile(outputStream.toByteArray(), dosyaAdi);
							if (mergeFile != null && mergeFile.exists())
								JOptionPane.showMessageDialog(null, mergeFile.getName() + " dosyasý oluþtu!");
						}

					}

				} catch (Exception e1) {
					if (e1.getMessage() != null)
						JOptionPane.showMessageDialog(null, e1.getMessage());

				}
			}
		});

		myFrame.setVisible(true);
	}

}
