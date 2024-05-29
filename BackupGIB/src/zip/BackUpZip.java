package zip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.json.JSONObject;

import com.google.gson.Gson;

public class BackUpZip {
	static int baslangicYil = 2010;
	static File anaDosya = null, yedekDosya = null;
	static File bilgiDosya = new File("/gib/saveCFG.json");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JFrame myFrame = new JFrame();
		myFrame.setResizable(false);
		JPanel yilPanel = new JPanel();
		final JSpinner spinYil = new JSpinner();
		SpinnerNumberModel model1 = new SpinnerNumberModel(baslangicYil, baslangicYil, 9999, 1);
		spinYil.setModel(model1);
		JComponent editor = new JSpinner.NumberEditor(spinYil, "#");
		spinYil.setEditor(editor);
		((JSpinner.DefaultEditor) editor).getTextField().setEditable(false);
		yilPanel.setBounds(0, 0, 340, 100);
		yilPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		JPanel anaPanel = new JPanel();
		anaPanel.setBackground(Color.RED);
		anaPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
		JPanel yedekPanel = new JPanel();
		yedekPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		yedekPanel.setBackground(Color.GREEN);
		anaPanel.setSize(340, 200);

		yedekPanel.setSize(340, 200);
		myFrame.setTitle("GIB Dosya Yedekleme");
		myFrame.setSize(360, 400);
		myFrame.setLocation(100, 200);

		myFrame.getContentPane().setLayout(new FlowLayout());
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel labelAna = new JLabel("Ana Klasör Ad");
		final JLabel labelYedek = new JLabel("Yedek Klasör Ad");
		final JLabel labelYil = new JLabel("Ýþlem Yýlý");
		final JTextField textAna = new JTextField();
		final JTextField textYedek = new JTextField();

		Calendar cal = Calendar.getInstance();
		spinYil.setValue(cal.get(Calendar.YEAR));
		// textYil.setText(String.valueOf(cal.get(Calendar.YEAR)));
		textAna.setEnabled(false);
		textYedek.setEnabled(false);
		final JButton btnAna = new JButton("Ana Klasör seç");
		final JButton btnYedek = new JButton("Yedek Klasör seç");
		final JButton saveBtn = new JButton("Sakla");
		final JFileChooser chooserAna = new JFileChooser();
		final JFileChooser chooserYedek = new JFileChooser();
		chooserAna.setDialogTitle("Ana Klasör");
		chooserAna.setApproveButtonText("Tamam");
		chooserAna.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooserAna.setLocale(new Locale("tr", "TR"));
		chooserYedek.setDialogTitle("Yedek Klasör");
		chooserYedek.setApproveButtonText("Tamam");
		chooserYedek.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooserYedek.setLocale(new Locale("tr", "TR"));

		labelYil.setLocation(100, 100);
		yilPanel.add(labelYil);
		yilPanel.add(spinYil);

		// textYil.setLocation(200, 100);
		// yilPanel.add(textYil);

		labelAna.setLocation(100, 300);
		anaPanel.add(labelAna);
		textAna.setLocation(200, 300);
		anaPanel.add(textAna);
		btnAna.setLocation(300, 300);
		anaPanel.add(btnAna);
		chooserAna.setLocation(400, 300);
		anaPanel.add(chooserAna);

		labelYedek.setLocation(100, 400);
		yedekPanel.add(labelYedek);
		textYedek.setLocation(200, 400);
		yedekPanel.add(textYedek);
		btnYedek.setLocation(300, 400);
		yedekPanel.add(btnYedek);
		chooserYedek.setLocation(400, 400);
		yedekPanel.add(chooserYedek);

		anaPanel.setLocation(0, 0);

		try {
			bilgiDosya.getParentFile().mkdirs();
		} catch (Exception e) {

		}
		if (bilgiDosya.exists()) {
			try {
				String jsonStr = new String(Util.getFileByteArray(bilgiDosya));
				JSONObject map = new JSONObject(jsonStr);
				if (map.has("yil"))
					spinYil.setValue(Integer.parseInt((String) map.get("yil")));
				if (map.has("anaKlasor")) {
					String klasor = (String) map.get("anaKlasor");
					File file = new File(klasor);
					if (file.exists()) {
						anaDosya = file;
						textAna.setText(klasor);
						textAna.setToolTipText(klasor);
						chooserAna.setSelectedFile(anaDosya);

					}

				}
				if (map.has("yedekKlasor")) {
					String klasor = (String) map.get("yedekKlasor");
					File file = new File(klasor);
					if (file.exists()) {
						yedekDosya = file;
						textYedek.setText(klasor);
						textYedek.setToolTipText(klasor);
						chooserYedek.setSelectedFile(yedekDosya);

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		saveBtn.setVisible(false);

		saveBtn.setVisible(anaDosya != null && yedekDosya != null && anaDosya.exists() && yedekDosya.exists());
		btnAna.setVisible(true);
		btnYedek.setVisible(true);
		chooserAna.setVisible(false);
		chooserYedek.setVisible(false);

		myFrame.add(yilPanel, BorderLayout.NORTH);
		// anaPanel.setLocation(0, 300);
		myFrame.add(anaPanel, BorderLayout.EAST);
		// yedekPanel.setLocation(0, 400);
		myFrame.add(yedekPanel, BorderLayout.WEST);

		// saveBtn.setLocation(200, 500);
		myFrame.add(saveBtn, BorderLayout.SOUTH);
		myFrame.setVisible(true);

		btnAna.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnAna.setVisible(false);
				chooserAna.setSelectedFile(null);
				chooserAna.setSelectedFiles(null);
				chooserAna.setVisible(true);
				chooserAna.showSaveDialog(null);

			}
		});

		btnYedek.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnYedek.setVisible(false);
				chooserYedek.setSelectedFile(null);
				chooserYedek.setSelectedFiles(null);
				chooserYedek.setVisible(true);
				chooserYedek.showSaveDialog(null);

			}
		});

		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!anaDosya.getPath().equalsIgnoreCase(yedekDosya.getPath())) {
					List<File> klasorler = new ArrayList<>(), dosyalar = null;
					LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
					map.put("anaKlasor", anaDosya.getPath());
					map.put("yedekKlasor", yedekDosya.getPath());
					map.put("yil", String.valueOf(spinYil.getValue()));
					Gson gs = new Gson();
					String content = gs.toJson(map);
					try {
						Util.fileWrite(Util.toPrettyFormat(content), bilgiDosya.getPath());
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					Util.dosyaBul(anaDosya, klasorler, "." + spinYil.getValue());

					if (!klasorler.isEmpty()) {
						int adet = 0;
						dosyalar = new ArrayList<>();
						for (File klasor : klasorler)
							Util.dosyaEkle(klasor, dosyalar);
						if (!dosyalar.isEmpty())
							adet = Util.dosyaKopyala(anaDosya.getPath(), yedekDosya.getPath(), dosyalar);
						if (dosyalar == null || dosyalar.isEmpty()) {
							JOptionPane.showMessageDialog(null, spinYil.getValue() + " ait dosya bulunmadý!");
						} else if (adet > 0)
							JOptionPane.showMessageDialog(null, spinYil.getValue() + " ait " + adet + " adet dosya bulundu.");
						else
							JOptionPane.showMessageDialog(null, spinYil.getValue() + " ait yedeklenecek yeni dosya yoktur!");
					} else {
						JOptionPane.showMessageDialog(null, spinYil.getValue() + "  yýlýna ait dönem dosyalarý bulunamadý!");
					}

				} else
					JOptionPane.showMessageDialog(null, "Yedek klasörünü farklý seçiniz!");
			}
		});

		chooserYedek.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				if (actionCommand.equals(JFileChooser.CANCEL_SELECTION)) {
					btnYedek.setVisible(true);
					chooserYedek.setVisible(false);
				} else {
					if (actionCommand.equals(JFileChooser.APPROVE_SELECTION)) {
						yedekDosya = chooserYedek.getSelectedFile();
						textYedek.setText(yedekDosya.getAbsoluteFile().getPath());
						textYedek.setToolTipText(textYedek.getText());
						btnYedek.setVisible(true);
						saveBtn.setVisible(anaDosya != null && anaDosya.exists() && spinYil.getValue() != null);
					}
				}
			}
		});

		chooserAna.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				if (actionCommand.equals(JFileChooser.CANCEL_SELECTION)) {
					btnAna.setVisible(true);
					chooserAna.setVisible(false);
				} else {
					if (actionCommand.equals(JFileChooser.APPROVE_SELECTION)) {
						anaDosya = chooserAna.getSelectedFile();
						textAna.setText(anaDosya.getAbsoluteFile().getPath());
						textAna.setToolTipText(textAna.getText());
						btnAna.setVisible(true);
						saveBtn.setVisible(yedekDosya != null && yedekDosya.exists() && spinYil.getValue() != null);
					}
				}
			}
		});
	}

}
