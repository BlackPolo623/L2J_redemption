package org.l2jmobius.loginserver.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.l2jmobius.Config;
import org.l2jmobius.commons.ui.LimitLinesDocumentListener;
import org.l2jmobius.commons.ui.SplashScreen;
import org.l2jmobius.loginserver.GameServerTable;
import org.l2jmobius.loginserver.GameServerTable.GameServerInfo;
import org.l2jmobius.loginserver.LoginController;
import org.l2jmobius.loginserver.LoginServer;
import org.l2jmobius.loginserver.network.gameserverpackets.ServerStatus;

public class Gui {
	// Define color constants
	private static final Color BACKGROUND_COLOR = new Color(32, 34, 37);
	private static final Color MENU_BACKGROUND = new Color(47, 49, 54);
	private static final Color TEXT_COLOR = new Color(220, 221, 222);
	private static final Color BORDER_COLOR = new Color(66, 70, 77);

	private JTextArea txtrConsole;
	private JFrame frame;
	private JCheckBoxMenuItem chckbxmntmEnabled;
	private JCheckBoxMenuItem chckbxmntmDisabled;
	private JCheckBoxMenuItem chckbxmntmGmOnly;

	static final String[] shutdownOptions = {"關閉", "取消"};
	static final String[] restartOptions = {"重啟", "取消"};

	public Gui() {
		setupLookAndFeel();
		initializeConsole();
		createMainFrame();
	}

	private void setupLookAndFeel() {
		try {
			// Set global UI properties
			UIManager.put("Menu.selectionBackground", MENU_BACKGROUND.brighter());
			UIManager.put("MenuItem.selectionBackground", MENU_BACKGROUND.brighter());
			UIManager.put("MenuItem.background", MENU_BACKGROUND);
			UIManager.put("Menu.background", MENU_BACKGROUND);
			UIManager.put("MenuBar.background", MENU_BACKGROUND);
			UIManager.put("PopupMenu.background", MENU_BACKGROUND);
			UIManager.put("Menu.foreground", TEXT_COLOR);
			UIManager.put("MenuItem.foreground", TEXT_COLOR);
			UIManager.put("MenuBar.foreground", TEXT_COLOR);
			UIManager.put("OptionPane.background", BACKGROUND_COLOR);
			UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
			UIManager.put("Panel.background", BACKGROUND_COLOR);
			UIManager.put("Button.background", MENU_BACKGROUND);
			UIManager.put("Button.foreground", TEXT_COLOR);
			UIManager.put("CheckBoxMenuItem.background", MENU_BACKGROUND);
			UIManager.put("CheckBoxMenuItem.foreground", TEXT_COLOR);
			UIManager.put("CheckBoxMenuItem.selectionBackground", MENU_BACKGROUND.brighter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeConsole() {
		txtrConsole = new JTextArea();
		txtrConsole.setEditable(false);
		txtrConsole.setLineWrap(true);
		txtrConsole.setWrapStyleWord(true);
		txtrConsole.setBackground(BACKGROUND_COLOR);
		txtrConsole.setForeground(TEXT_COLOR);
		txtrConsole.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 16));
		txtrConsole.setMargin(new Insets(5, 5, 5, 5));
		txtrConsole.getDocument().addDocumentListener(new LimitLinesDocumentListener(500));
		txtrConsole.setDropMode(DropMode.INSERT);
	}

	private void createMainFrame() {
		frame = new JFrame("Lineage II - LoginServer");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setBackground(BACKGROUND_COLOR);
		frame.setJMenuBar(createMenuBar());

		// Set icons
		List<Image> icons = new ArrayList<>();
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "l216x16.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "l232x32.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "l264x64.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "l2128x128.png").getImage());
		frame.setIconImages(icons);

		// Set close window behavior
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				if (JOptionPane.showOptionDialog(null, "確定要立即關閉伺服器嗎？", "選擇一個選項",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, shutdownOptions, shutdownOptions[1]) == 0) {
					LoginServer.getInstance().shutdown(false);
				}
			}
		});

		setupMainPanel();

		frame.setPreferredSize(new Dimension(1000, 550));
		frame.pack();
		frame.setLocationRelativeTo(null);

		redirectSystemStreams();
		new SplashScreen(".." + File.separator + "images" + File.separator + "splash.png", 500, frame);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(new EmptyBorder(3, 3, 3, 3));
		menuBar.setBackground(MENU_BACKGROUND);

		// Actions menu
		addActionsMenu(menuBar);

		// Reload menu
		addReloadMenu(menuBar);

		// Status menu
		addStatusMenu(menuBar);

		// Font menu
		addFontMenu(menuBar);

		// Help menu
		addHelpMenu(menuBar);

		return menuBar;
	}

	private void addActionsMenu(JMenuBar menuBar) {
		JMenu menu = createMenu("執行");
		menuBar.add(menu);

		menu.add(createMenuItem("關閉伺服器", e -> {
			if (JOptionPane.showOptionDialog(null, "確定要關閉伺服器嗎？", "選擇一個選項",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, shutdownOptions, shutdownOptions[1]) == 0) {
				LoginServer.getInstance().shutdown(false);
			}
		}));

		menu.add(createMenuItem("重啟伺服器", e -> {
			if (JOptionPane.showOptionDialog(null, "確定要重啟伺服器嗎？", "選擇一個選項",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, restartOptions, restartOptions[1]) == 0) {
				LoginServer.getInstance().shutdown(true);
			}
		}));
	}

	private void addReloadMenu(JMenuBar menuBar) {
		JMenu menu = createMenu("重讀資料");
		menuBar.add(menu);

		menu.add(createMenuItem("Banned IPs", e -> {
			LoginController.getInstance().getBannedIps().clear();
			LoginServer.getInstance().loadBanFile();
		}));
	}

	private void addStatusMenu(JMenuBar menuBar) {
		JMenu menu = createMenu("登入狀態");
		menuBar.add(menu);

		chckbxmntmEnabled = createCheckBoxMenuItem("全部允許登入", true);
		chckbxmntmEnabled.addActionListener(e -> {
			updateLoginStatus(ServerStatus.STATUS_NORMAL);
			LoginServer.LOGGER.info("目前伺服器狀態：全部允許登入");
		});
		menu.add(chckbxmntmEnabled);

		chckbxmntmDisabled = createCheckBoxMenuItem("全部禁止登入", false);
		chckbxmntmDisabled.addActionListener(e -> {
			updateLoginStatus(ServerStatus.STATUS_DOWN);
			LoginServer.LOGGER.info("目前伺服器狀態：全部禁止登入");
		});
		menu.add(chckbxmntmDisabled);

		chckbxmntmGmOnly = createCheckBoxMenuItem("只限GM登入", false);
		chckbxmntmGmOnly.addActionListener(e -> {
			updateLoginStatus(ServerStatus.STATUS_GM_ONLY);
			LoginServer.LOGGER.info("目前伺服器狀態：只限GM登入");
		});
		menu.add(chckbxmntmGmOnly);
	}

	private void updateLoginStatus(int status) {
		chckbxmntmEnabled.setSelected(status == ServerStatus.STATUS_NORMAL);
		chckbxmntmDisabled.setSelected(status == ServerStatus.STATUS_DOWN);
		chckbxmntmGmOnly.setSelected(status == ServerStatus.STATUS_GM_ONLY);

		LoginServer.getInstance().setStatus(status);
		for (GameServerInfo gsi : GameServerTable.getInstance().getRegisteredGameServers().values()) {
			gsi.setStatus(status);
		}
	}

	private void addFontMenu(JMenuBar menuBar) {
		JMenu menu = createMenu("字體大小");
		menuBar.add(menu);

		String[] fonts = {"16", "21", "27", "33"};
		for (String size : fonts) {
			menu.add(createMenuItem(size, e ->
					txtrConsole.setFont(new Font("Microsoft JhengHei", Font.PLAIN, Integer.parseInt(size)))
			));
		}
	}

	private void addHelpMenu(JMenuBar menuBar) {
		JMenu menu = createMenu("幫助");
		menuBar.add(menu);
		menu.add(createMenuItem("關於我", e -> new frmAbout()));
	}

	private JMenu createMenu(String text) {
		JMenu menu = new JMenu(text);
		menu.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
		menu.setForeground(TEXT_COLOR);
		menu.setBackground(MENU_BACKGROUND);
		return menu;
	}

	private JMenuItem createMenuItem(String text, java.awt.event.ActionListener listener) {
		JMenuItem item = new JMenuItem(text);
		item.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
		item.setForeground(TEXT_COLOR);
		item.setBackground(MENU_BACKGROUND);
		item.addActionListener(listener);
		return item;
	}

	private JCheckBoxMenuItem createCheckBoxMenuItem(String text, boolean selected) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(text);
		item.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 13));
		item.setForeground(TEXT_COLOR);
		item.setBackground(MENU_BACKGROUND);
		item.setSelected(selected);
		return item;
	}

	private void setupMainPanel() {
		JScrollPane scrollPane = new JScrollPane(txtrConsole);
		scrollPane.setBorder(new LineBorder(BORDER_COLOR));
		scrollPane.getVerticalScrollBar().setBackground(MENU_BACKGROUND);
		scrollPane.getHorizontalScrollBar().setBackground(MENU_BACKGROUND);

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent ev) {
				scrollPane.setSize(frame.getContentPane().getSize());
			}
		});

		frame.add(scrollPane, BorderLayout.CENTER);
	}

	void updateTextArea(String text) {
		SwingUtilities.invokeLater(() -> {
			txtrConsole.append(text);
			txtrConsole.setCaretPosition(txtrConsole.getText().length());
		});
	}

	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) {
				updateTextArea(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) {
				updateTextArea(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}
}