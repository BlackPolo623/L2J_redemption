package org.l2jmobius.gameserver.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import org.l2jmobius.Config;
import org.l2jmobius.gameserver.GameServer;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.util.Locator;

public class SystemPanel extends JPanel {
	protected static final Logger LOGGER = Logger.getLogger(SystemPanel.class.getName());
	protected static final long START_TIME = System.currentTimeMillis();

	// 定義顏色常量
	private static final Color BACKGROUND_COLOR = new Color(32, 34, 37); // Discord風格的深色背景
	private static final Color LABEL_COLOR = new Color(220, 221, 222); // 淺灰色文字
	private static final Color BORDER_COLOR = new Color(47, 49, 54); // 稍微淺一點的邊框顏色
	private static final Color VALUE_COLOR = new Color(114, 137, 218); // Discord藍色，用於強調數值

	public SystemPanel() {
		setupPanel();
		initializeLabels();
	}

	private void setupPanel() {
		setBackground(BACKGROUND_COLOR);
		setBounds(500, 20, 300, 160); // 稍微加大面板尺寸
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER_COLOR, 1),
				BorderFactory.createEmptyBorder(10, 15, 10, 15)
		));
		setLayout(null);
	}

	private JLabel createStyledLabel(String text, int y) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14)); // 使用微軟正黑體
		label.setForeground(LABEL_COLOR);
		label.setBounds(15, y, 270, 20);
		return label;
	}

	private void initializeLabels() {
		final JLabel lblProtocol = createStyledLabel("版本: ", 10);
		final JLabel lblConnected = createStyledLabel("連接數量: ", 30);
		final JLabel lblMaxConnected = createStyledLabel("最大連接數量: ", 50);
		final JLabel lblOfflineShops = createStyledLabel("離線交易: ", 70);
		final JLabel lblElapsedTime = createStyledLabel("伺服器運行時間: ", 90);
		final JLabel lblJavaVersion = createStyledLabel("JDK版本: ", 110);
		final JLabel lblBuildDate = createStyledLabel("核心建構時間: ", 130);

		add(lblProtocol);
		add(lblConnected);
		add(lblMaxConnected);
		add(lblOfflineShops);
		add(lblElapsedTime);
		add(lblJavaVersion);
		add(lblBuildDate);

		// Set initial values
		lblProtocol.setText("版本: 確認中...");
		lblConnected.setText("連接數量: 0");
		lblMaxConnected.setText("最大連接數量: 0");
		lblOfflineShops.setText("離線交易: 0");
		lblElapsedTime.setText("伺服器運行時間: 0 sec");
		lblJavaVersion.setText("JDK版本: " + System.getProperty("java.version"));
		lblBuildDate.setText("核心建構時間: 確認中...");

		try {
			final File jarName = Locator.getClassSource(GameServer.class);
			final JarFile jarFile = new JarFile(jarName);
			final Attributes attrs = jarFile.getManifest().getMainAttributes();
			lblBuildDate.setText("核心建構時間: " + attrs.getValue("Build-Date").split(" ")[0]);
			jarFile.close();
		} catch (Exception e) {
			// Handled above.
		}

		// Update protocol task
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				lblProtocol.setText((Config.PROTOCOL_LIST.size() > 1 ? "版本: " : "版本: ") +
						(Config.SERVER_LIST_TYPE >= 400 ? "亞丁 " : "") + Config.PROTOCOL_LIST.toString());
			}
		}, 4500);

		// Repeating stats update task
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(() -> {
					final int playerCount = World.getInstance().getPlayers().size();
					if (World.MAX_CONNECTED_COUNT < playerCount) {
						World.MAX_CONNECTED_COUNT = playerCount;
						if (playerCount > 1) {
							LOGGER.info("新的最大連接數量: " + playerCount + "!");
						}
					}

					updateLabelWithValue(lblConnected, "連接數量: ", playerCount);
					updateLabelWithValue(lblMaxConnected, "最大連接數量: ", World.MAX_CONNECTED_COUNT);
					updateLabelWithValue(lblOfflineShops, "離線交易: ", World.OFFLINE_TRADE_COUNT);
					lblElapsedTime.setText("伺服器運行時間: " + getDurationBreakdown(System.currentTimeMillis() - START_TIME));
				});
			}
		}, 1000, 1000);
	}

	private void updateLabelWithValue(JLabel label, String prefix, int value) {
		label.setText(prefix + value);
		// 當數值改變時添加淡入效果
		label.setForeground(VALUE_COLOR);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(() -> label.setForeground(LABEL_COLOR));
			}
		}, 500);
	}

	static String getDurationBreakdown(long millis) {
		long remaining = millis;
		final long days = TimeUnit.MILLISECONDS.toDays(remaining);
		remaining -= TimeUnit.DAYS.toMillis(days);
		final long hours = TimeUnit.MILLISECONDS.toHours(remaining);
		remaining -= TimeUnit.HOURS.toMillis(hours);
		final long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
		remaining -= TimeUnit.MINUTES.toMillis(minutes);
		final long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining);
		return (days + "天 " + hours + "時 " + minutes + "分 " + seconds + "秒");
	}
}