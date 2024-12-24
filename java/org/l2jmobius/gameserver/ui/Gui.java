package org.l2jmobius.gameserver.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.l2jmobius.Config;
import org.l2jmobius.commons.ui.LimitLinesDocumentListener;
import org.l2jmobius.commons.ui.SplashScreen;
import org.l2jmobius.gameserver.Shutdown;
import org.l2jmobius.gameserver.cache.HtmCache;
import org.l2jmobius.gameserver.data.xml.AdminData;
import org.l2jmobius.gameserver.data.xml.BuyListData;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.data.xml.PrimeShopData;
import org.l2jmobius.gameserver.util.Broadcast;
import org.l2jmobius.gameserver.util.Util;

public class Gui {
	// 定義顏色常量
	private static final Color BACKGROUND_COLOR = new Color(32, 34, 37);
	private static final Color MENU_BACKGROUND = new Color(47, 49, 54);
	private static final Color TEXT_COLOR = new Color(220, 221, 222);
	private static final Color BORDER_COLOR = new Color(66, 70, 77);

	private JTextArea txtrConsole;
	private JFrame frame;

	static final String[] shutdownOptions = {"關閉", "取消"};
	static final String[] restartOptions = {"重啟", "取消"};
	static final String[] abortOptions = {"終止", "取消"};
	static final String[] confirmOptions = {"確認", "取消"};

	public Gui() {
		setupLookAndFeel();
		initializeConsole();
		createMainFrame();
	}

	private void setupLookAndFeel() {
		try {
			// 設置全局UI屬性
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
			UIManager.put("OptionPane.background", BACKGROUND_COLOR);
			UIManager.put("OptionPane.foreground", TEXT_COLOR);
			UIManager.put("OptionPane.buttonBackground", MENU_BACKGROUND);
			UIManager.put("OptionPane.buttonForeground", TEXT_COLOR);
			UIManager.put("TextField.background", MENU_BACKGROUND);
			UIManager.put("TextField.foreground", TEXT_COLOR);
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
		frame = new JFrame("救贖天堂2 - 盟戰長久服版本 - 遊戲伺服器");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setBackground(BACKGROUND_COLOR);
		frame.setJMenuBar(createMenuBar());

		// 設置圖標
		List<Image> icons = new ArrayList<>();
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "l216x16.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "l232x32.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "l264x64.png").getImage());
		icons.add(new ImageIcon(".." + File.separator + "images" + File.separator + "l2128x128.png").getImage());
		frame.setIconImages(icons);

		// 設置關閉視窗行為
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				if (JOptionPane.showOptionDialog(null, "確定要立即關閉伺服器嗎？", "選擇一個選項",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, shutdownOptions, shutdownOptions[1]) == 0) {
					Shutdown.getInstance().startShutdown(null, 1, false);
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

		// 執行菜單
		addActionsMenu(menuBar);

		// 重讀資料菜單
		addReloadMenu(menuBar);

		// 公告菜單
		addAnnounceMenu(menuBar);

		// 字體大小菜單
		addFontMenu(menuBar);

		// 幫助菜單
		addHelpMenu(menuBar);

		return menuBar;
	}

	private void addActionsMenu(JMenuBar menuBar) {
		JMenu menu = createMenu("執行");
		menuBar.add(menu);

		menu.add(createMenuItem("關閉伺服器", e -> {
			if (JOptionPane.showOptionDialog(null, "確定要關閉伺服器嗎？", "選擇一個選項",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, shutdownOptions, shutdownOptions[1]) == 0) {
				final String input = showDelayInputDialog("關閉延遲（秒）");
				if (input != null && Util.isDigit(input)) {
					final int delay = Integer.parseInt(input);
					if (delay > 0) {
						Shutdown.getInstance().startShutdown(null, delay, false);
					}
				}
			}
		}));

		menu.add(createMenuItem("重啟伺服器", e -> {
			if (JOptionPane.showOptionDialog(null, "確定要重啟伺服器嗎？", "選擇一個選項",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, restartOptions, restartOptions[1]) == 0) {
				final String input = showDelayInputDialog("重啟延遲（秒）");
				if (input != null && Util.isDigit(input)) {
					final int delay = Integer.parseInt(input);
					if (delay > 0) {
						Shutdown.getInstance().startShutdown(null, delay, true);
					}
				}
			}
		}));

		menu.add(createMenuItem("終止操作", e -> {
			if (JOptionPane.showOptionDialog(null, "確定要終止關閉/重啟操作嗎？", "選擇一個選項",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, abortOptions, abortOptions[1]) == 0) {
				Shutdown.getInstance().abort(null);
			}
		}));
	}

	private void addReloadMenu(JMenuBar menuBar) {
		JMenu menu = createMenu("重讀資料");
		menuBar.add(menu);

		menu.add(createMenuItem("設定檔", e -> {
			if (confirmReload("設定檔")) {
				Config.load(Config.SERVER_MODE);
			}
		}));

		menu.add(createMenuItem("權限", e -> {
			if (confirmReload("權限")) {
				AdminData.getInstance().load();
			}
		}));

		menu.add(createMenuItem("HTML", e -> {
			if (confirmReload("HTML檔案")) {
				HtmCache.getInstance().reload();
			}
		}));

		menu.add(createMenuItem("多重販售", e -> {
			if (confirmReload("多重販售資料")) {
				MultisellData.getInstance().load();
			}
		}));

		menu.add(createMenuItem("商店清單", e -> {
			if (confirmReload("商店清單")) {
				BuyListData.getInstance().load();
			}
		}));

		menu.add(createMenuItem("商城", e -> {
			if (confirmReload("商城資料")) {
				PrimeShopData.getInstance().load();
			}
		}));
	}

	private void addAnnounceMenu(JMenuBar menuBar) {
		JMenu menu = createMenu("公告");
		menuBar.add(menu);

		menu.add(createMenuItem("一般公告", e -> {
			final String input = JOptionPane.showInputDialog(null, "請輸入公告內容", "輸入",
					JOptionPane.INFORMATION_MESSAGE);
			if (input != null && !input.trim().isEmpty()) {
				Broadcast.toAllOnlinePlayers(input.trim(), false);
			}
		}));

		menu.add(createMenuItem("重要公告", e -> {
			final String input = JOptionPane.showInputDialog(null, "請輸入重要公告內容", "輸入",
					JOptionPane.INFORMATION_MESSAGE);
			if (input != null && !input.trim().isEmpty()) {
				Broadcast.toAllOnlinePlayers(input.trim(), true);
			}
		}));
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

	private void setupMainPanel() {
		JScrollPane scrollPane = new JScrollPane(txtrConsole);
		scrollPane.setBorder(new LineBorder(BORDER_COLOR));
		scrollPane.getVerticalScrollBar().setBackground(MENU_BACKGROUND);
		scrollPane.getHorizontalScrollBar().setBackground(MENU_BACKGROUND);

		// 創建系統面板
		final JPanel systemPanel = new SystemPanel();

		// 創建分層面板
		final JLayeredPane layeredPane = new JLayeredPane();
		scrollPane.setBounds(0, 0, 800, 550);
		layeredPane.add(scrollPane, 0, 0);
		layeredPane.add(systemPanel, 1, 0);

		// 添加大小調整監聽器
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent ev) {
				scrollPane.setSize(frame.getContentPane().getSize());
				systemPanel.setLocation(frame.getContentPane().getWidth() - systemPanel.getWidth() - 34, systemPanel.getY());
			}
		});

		frame.add(layeredPane, BorderLayout.CENTER);
	}

	private String showDelayInputDialog(String message) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(BACKGROUND_COLOR);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		JTextArea textArea = new JTextArea("600");
		textArea.setBackground(MENU_BACKGROUND);
		textArea.setForeground(TEXT_COLOR);
		textArea.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER_COLOR),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));

		panel.add(textArea, gbc);

		Object[] options = {"確認", "取消"};
		int result = JOptionPane.showOptionDialog(
				null,
				panel,
				message,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]
		);

		if (result == JOptionPane.OK_OPTION) {
			return textArea.getText().trim();
		}
		return null;
	}

	private boolean confirmReload(String type) {
		String message = String.format("確定要重新載入%s嗎？", type);
		return JOptionPane.showOptionDialog(
				null,
				message,
				"選擇一個選項",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				confirmOptions,
				confirmOptions[1]
		) == 0;
	}

	// 重定向系統輸出到文本區域
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

	// 自定義確認對話框樣式
	private int showCustomDialog(String message, String title, String[] options) {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(BACKGROUND_COLOR);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.setBackground(BACKGROUND_COLOR);
		JTextArea messageArea = new JTextArea(message);
		messageArea.setBackground(BACKGROUND_COLOR);
		messageArea.setForeground(TEXT_COLOR);
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		messagePanel.add(messageArea, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(BACKGROUND_COLOR);

		JButton[] buttons = new JButton[options.length];
		for (int i = 0; i < options.length; i++) {
			buttons[i] = new JButton(options[i]);
			buttons[i].setBackground(MENU_BACKGROUND);
			buttons[i].setForeground(TEXT_COLOR);
			buttonPanel.add(buttons[i]);
		}

		panel.add(messagePanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		JOptionPane optionPane = new JOptionPane(
				panel,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.DEFAULT_OPTION,
				null,
				options,
				options[0]
		);

		JDialog dialog = optionPane.createDialog(title);
		dialog.setBackground(BACKGROUND_COLOR);
		dialog.setVisible(true);

		Object selectedValue = optionPane.getValue();
		if (selectedValue == null) {
			return JOptionPane.CLOSED_OPTION;
		}

		for (int i = 0; i < options.length; i++) {
			if (options[i].equals(selectedValue)) {
				return i;
			}
		}

		return JOptionPane.CLOSED_OPTION;
	}
}