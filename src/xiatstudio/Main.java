package xiatstudio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Main extends JFrame {
	/* Default loading data */
	static String data = "./Benson_Data/empty.txt";
	static JPanel panel;
	static int displayMode = 0;

	public static void main(String[] args) {
		/* Load GUI component */
		GUISetup();
	}

	public static void GUISetup() {
		/* Windows look and feel */
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Initialize JFrame and Menu bar */
		JFrame frame = new JFrame();
		JMenuBar menuBar = new JMenuBar();
		JMenu menu, menu2, menu3, exportMenu, exportAllMenu;
		JMenuItem menuItem, menuItem2, menuItem3, menuItem4, menuItem5;
		JMenuItem mode1, mode2, mode3, mode4;
		JMenuItem pen_offON, pen_offOFF;

		/* Background color */
		Color bg = new Color(54, 63, 70);

		menu = new JMenu("File");
		menu2 = new JMenu("Component");
		menu3 = new JMenu("Off-paper tracking");
		pen_offON = new JMenuItem("ON");
		pen_offOFF = new JMenuItem("OFF");
		menu3.add(pen_offON);
		menu3.add(pen_offOFF);

		menuBar.add(menu);
		menuBar.add(menu2);
		menuBar.add(menu3);
		menuItem = new JMenuItem("Open");
		mode1 = new JMenuItem("All");
		mode2 = new JMenuItem("Horizontal");
		mode3 = new JMenuItem("Vertical");
		mode4 = new JMenuItem("Oblique");
		exportMenu = new JMenu("Export as...");
		exportAllMenu = new JMenu("Export all as...");
		menuItem2 = new JMenuItem("PNG Image");
		menuItem3 = new JMenuItem("CSV File");
		menuItem4 = new JMenuItem("PNG Image");
		menuItem5 = new JMenuItem("CSV File");
		menu.add(menuItem);
		menu.add(exportMenu);
		menu.add(exportAllMenu);
		menu2.add(mode1);
		menu2.add(mode2);
		menu2.add(mode3);
		menu2.add(mode4);
		exportMenu.add(menuItem2);
		exportMenu.add(menuItem3);

		exportAllMenu.add(menuItem4);
		exportAllMenu.add(menuItem5);

		panel = new GPanel();
		frame.setJMenuBar(menuBar);
		frame.add(panel);
		frame.setTitle("Currently viewing: " + data);
		panel.setBackground(bg);
		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		mode1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMode = 0;
				panel.repaint();

			}
		});

		mode2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMode = 1;
				panel.repaint();

			}
		});

		mode3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMode = 2;
				panel.repaint();

			}
		});

		mode4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMode = 3;
				panel.repaint();

			}
		});

		pen_offON.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMode = 4;
				panel.repaint();

			}
		});

		pen_offOFF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayMode = 0;
				panel.repaint();

			}
		});

		/* Open file action */
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/* Default path */
				JFileChooser fileChooser = new JFileChooser(".\\Benson_Data");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files(*.txt, *.text)", "txt",
						"text");
				fileChooser.setFileFilter(filter);

				switch (fileChooser.showOpenDialog(panel)) {
				case JFileChooser.APPROVE_OPTION:
					data = fileChooser.getSelectedFile().getPath();
					/* Replace backslash in the path */
					data = data.replace("\\", "/");
					/* Update content */
					displayMode = 0;
					panel.repaint();
					frame.setTitle("Currently viewing: " + data);
					// System.out.println(data);
					break;
				}
			}
		});

		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BufferedImage imagebuf = null;
				imagebuf = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);

				// Graphics2D g2d = imagebuf.getGraphics();
				panel.paint(imagebuf.getGraphics());
				try {
					ImageIO.write(imagebuf, "png", new File(data + ".png"));
				} catch (Exception e1) {
					System.out.println("error");
				}
			}
		});

		menuItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Benson b = new Benson(data);
				exportSingleData(b, data.substring(0, data.lastIndexOf('.')) + ".csv");
			}

		});

		menuItem4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] controlDataList = getDataList("M:\\eclipse-workspace\\bensonFigure\\Benson_Data\\Controls\\");
				outputPNGInBatch(controlDataList, panel);
				String[] patientDataList = getDataList("M:\\eclipse-workspace\\bensonFigure\\Benson_Data\\Patients\\");
				outputPNGInBatch(patientDataList, panel);
			}
		});

		menuItem5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] controlDataList = getDataList("M:\\eclipse-workspace\\bensonFigure\\Benson_Data\\Controls\\");
				exportAllData(controlDataList, ".\\Sheets\\control_"
						+ new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) + ".csv");
				String[] patientDataList = getDataList("M:\\eclipse-workspace\\bensonFigure\\Benson_Data\\Patients\\");
				exportAllData(patientDataList, ".\\Sheets\\patient_"
						+ new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) + ".csv");
			}
		});
	}

	public static void objectCSVFileCreation(String fileName) {
		File f = new File(fileName);

		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void outputPNGInBatch(String[] dataList, JPanel p) {
		BufferedImage imagebuf = null;

		for (int i = 0; i < dataList.length; i++) {
			data = dataList[i];
			data = data.replace("\\", "/");

			p.repaint();

			imagebuf = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_RGB);
			p.paint(imagebuf.getGraphics());
			try {
				ImageIO.write(imagebuf, "png",
						new File(dataList[i].substring(0, dataList[i].lastIndexOf('.')) + "-drawing.png"));
				System.out.println("Generating image " + dataList[i].substring(0, dataList[i].lastIndexOf('.'))
						+ "-drawing.png ...");
			} catch (Exception e1) {
				System.out.println("error");
			}
		}
	}

	public static String[] getDataList(String path) {
		File folder = new File(path);

		FilenameFilter fileNameFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {

					// get last index for '.' char
					int lastIndex = name.lastIndexOf('.');

					// get extension
					String str = name.substring(lastIndex);

					// match path name extension
					if (str.equals(".txt")) {
						return true;
					}
				}

				return false;
			}
		};

		File[] listOfFiles = folder.listFiles(fileNameFilter);

		String[] fileList = new String[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
			fileList[i] = path + listOfFiles[i].getName();
		}

		return fileList;
	}

	public static void exportAllData(String[] dataList, String fileName) {
		objectCSVFileCreation(fileName);
		FileWriter writer;

		try {
			writer = new FileWriter(fileName, true);
			String[] title = { "Subject ID", "Mode", "Total time", "Total length", "Size", "Aspect Ratio",
					"Velocity Stability", "Angular Stability", "Pen Off %","Hori Portion","Vert Portion","Obli Portion",
					"Hori SD","Vert SD","Obli SD"};
			writeData(writer, title);
			writer.append("\r\n");

			for (int i = 0; i < dataList.length; i++) {

				Benson b = new Benson(dataList[i].replace("\\", "/"));
				b.calcThreeLength();
				String[] dataPending = { b.getID(), b.getFigureMode(), String.valueOf(b.timeSpent),
						String.valueOf(b.getTotalLength()), String.valueOf(b.getSize()[0] * b.getSize()[1]),
						String.valueOf((double) (b.getSize()[0] / b.getSize()[1])), String.valueOf(b.getVelocitySD()),
						String.valueOf(b.getAngleSD()), String.valueOf(b.penoffCount() / (b.getTimeStamp() + 1)),
						String.valueOf((double)(b.getHoriPortion())),String.valueOf((double)b.getVertPortion()), String.valueOf((double)b.getObliPortion()),
						String.valueOf((double)b.getThreeSD()[0]), String.valueOf((double)b.getThreeSD()[1]),String.valueOf((double)b.getThreeSD()[2])};

				System.out.println("Exporting data from " + b.getID() + "_" + b.getFigureMode());

				writeData(writer, dataPending);

				writer.append("\r\n");

			}

			System.out.println("File " + fileName + " created");
			System.out.println(" ");

			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeData(FileWriter writer, String[] data) throws IOException {
		for (int i = 0; i < data.length; i++) {
			writer.append(data[i]);
			writer.append(',');
		}
	}

	public static void exportSingleData(Benson b, String fileName) {
		objectCSVFileCreation(fileName);

		FileWriter writer;
		try {
			writer = new FileWriter(fileName, true);
			String[] title = { "Subject ID", "Mode", "Total time", "Total length", "Size", "Aspect Ratio",
					"Velocity Stability", "Angular Stability", "Pen Off %" };
			writeData(writer, title);
			writer.append("\r\n");

			String[] dataPending = { b.getID(), b.getFigureMode(), String.valueOf(b.timeSpent),
					String.valueOf(b.getTotalLength()), String.valueOf(b.getSize()[0] * b.getSize()[1]),
					String.valueOf((double) (b.getSize()[0] / b.getSize()[1])), String.valueOf(b.getVelocitySD()),
					String.valueOf(b.getAngleSD()), String.valueOf(b.penoffCount() / (b.getTimeStamp() + 1)) };

			writeData(writer, dataPending);

			writer.append("\r\n");

			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class GPanel extends JPanel {
		public void Panel() {
			super.setPreferredSize(new Dimension(1280, 720));
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2.setRenderingHints(hints);
			Benson testFigure = new Benson(data);
			testFigure.drawBenson(g2, displayMode);
		}
	}
}
