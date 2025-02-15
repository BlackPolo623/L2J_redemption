/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jmobius.gameserver;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.io.BufferedReader;
import java.util.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.lang.Object;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.commons.enums.ServerMode;
import org.l2jmobius.commons.network.NetServer;
import org.l2jmobius.commons.threads.ThreadPool;
import org.l2jmobius.commons.util.DeadLockDetector;
import org.l2jmobius.commons.util.PropertiesParser;
import org.l2jmobius.gameserver.cache.HtmCache;
import org.l2jmobius.gameserver.data.AugmentationData;
import org.l2jmobius.gameserver.data.BotReportTable;
import org.l2jmobius.gameserver.data.ItemTable;
import org.l2jmobius.gameserver.data.MerchantPriceConfigTable;
import org.l2jmobius.gameserver.data.SchemeBufferTable;
import org.l2jmobius.gameserver.data.SpawnTable;
import org.l2jmobius.gameserver.data.sql.AnnouncementsTable;
import org.l2jmobius.gameserver.data.sql.CharInfoTable;
import org.l2jmobius.gameserver.data.sql.CharSummonTable;
import org.l2jmobius.gameserver.data.sql.ClanHallTable;
import org.l2jmobius.gameserver.data.sql.ClanTable;
import org.l2jmobius.gameserver.data.sql.CrestTable;
import org.l2jmobius.gameserver.data.sql.OfflineTraderTable;
import org.l2jmobius.gameserver.data.sql.TeleportLocationTable;
import org.l2jmobius.gameserver.data.xml.AdminData;
import org.l2jmobius.gameserver.data.xml.ArmorSetData;
import org.l2jmobius.gameserver.data.xml.BuyListData;
import org.l2jmobius.gameserver.data.xml.CategoryData;
import org.l2jmobius.gameserver.data.xml.ClassListData;
import org.l2jmobius.gameserver.data.xml.DoorData;
import org.l2jmobius.gameserver.data.xml.suijizz;
import org.l2jmobius.gameserver.data.xml.ElementalAttributeData;
import org.l2jmobius.gameserver.data.xml.EnchantItemData;
import org.l2jmobius.gameserver.data.xml.EnchantItemGroupsData;
import org.l2jmobius.gameserver.data.xml.EnchantItemHPBonusData;
import org.l2jmobius.gameserver.data.xml.EnchantItemOptionsData;
import org.l2jmobius.gameserver.data.xml.EnchantSkillGroupsData;
import org.l2jmobius.gameserver.data.xml.ExperienceData;
import org.l2jmobius.gameserver.data.xml.FakePlayerData;
import org.l2jmobius.gameserver.data.xml.FenceData;
import org.l2jmobius.gameserver.data.xml.FishData;
import org.l2jmobius.gameserver.data.xml.FishingMonstersData;
import org.l2jmobius.gameserver.data.xml.FishingRodsData;
import org.l2jmobius.gameserver.data.xml.HennaData;
import org.l2jmobius.gameserver.data.xml.HitConditionBonusData;
import org.l2jmobius.gameserver.data.xml.InitialEquipmentData;
import org.l2jmobius.gameserver.data.xml.InitialShortcutData;
import org.l2jmobius.gameserver.data.xml.KarmaData;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.data.xml.NpcData;
import org.l2jmobius.gameserver.data.xml.NpcNameLocalisationData;
import org.l2jmobius.gameserver.data.xml.OptionData;
import org.l2jmobius.gameserver.data.xml.PetDataTable;
import org.l2jmobius.gameserver.data.xml.PetSkillData;
import org.l2jmobius.gameserver.data.xml.PlayerTemplateData;
import org.l2jmobius.gameserver.data.xml.PlayerXpPercentLostData;
import org.l2jmobius.gameserver.data.xml.PrimeShopData;
import org.l2jmobius.gameserver.data.xml.RecipeData;
import org.l2jmobius.gameserver.data.xml.SecondaryAuthData;
import org.l2jmobius.gameserver.data.xml.SendMessageLocalisationData;
import org.l2jmobius.gameserver.data.xml.SiegeScheduleData;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.data.xml.SkillLearnData;
import org.l2jmobius.gameserver.data.xml.SkillTreeData;
import org.l2jmobius.gameserver.data.xml.StaticObjectData;
import org.l2jmobius.gameserver.data.xml.TransformData;
import org.l2jmobius.gameserver.data.xml.UIData;
import org.l2jmobius.gameserver.geoengine.GeoEngine;
import org.l2jmobius.gameserver.handler.EffectHandler;
import org.l2jmobius.gameserver.instancemanager.AirShipManager;
import org.l2jmobius.gameserver.instancemanager.AntiFeedManager;
import org.l2jmobius.gameserver.instancemanager.BoatManager;
import org.l2jmobius.gameserver.instancemanager.CHSiegeManager;
import org.l2jmobius.gameserver.instancemanager.CastleManager;
import org.l2jmobius.gameserver.instancemanager.CastleManorManager;
import org.l2jmobius.gameserver.instancemanager.ClanHallAuctionManager;
import org.l2jmobius.gameserver.instancemanager.CoupleManager;
import org.l2jmobius.gameserver.instancemanager.CursedWeaponsManager;
import org.l2jmobius.gameserver.instancemanager.CustomMailManager;
import org.l2jmobius.gameserver.instancemanager.DayNightSpawnManager;
import org.l2jmobius.gameserver.instancemanager.DimensionalRiftManager;
import org.l2jmobius.gameserver.instancemanager.EventDropManager;
import org.l2jmobius.gameserver.instancemanager.FakePlayerChatManager;
import org.l2jmobius.gameserver.instancemanager.FishingChampionshipManager;
import org.l2jmobius.gameserver.instancemanager.FortManager;
import org.l2jmobius.gameserver.instancemanager.FortSiegeManager;
import org.l2jmobius.gameserver.instancemanager.FourSepulchersManager;
import org.l2jmobius.gameserver.instancemanager.GlobalVariablesManager;
import org.l2jmobius.gameserver.instancemanager.GrandBossManager;
import org.l2jmobius.gameserver.instancemanager.IdManager;
import org.l2jmobius.gameserver.instancemanager.InstanceManager;
import org.l2jmobius.gameserver.instancemanager.ItemAuctionManager;
import org.l2jmobius.gameserver.instancemanager.ItemsOnGroundManager;
import org.l2jmobius.gameserver.instancemanager.MailManager;
import org.l2jmobius.gameserver.instancemanager.MapRegionManager;
import org.l2jmobius.gameserver.instancemanager.MercTicketManager;
import org.l2jmobius.gameserver.instancemanager.PcCafePointsManager;
import org.l2jmobius.gameserver.instancemanager.PetitionManager;
import org.l2jmobius.gameserver.instancemanager.PrecautionaryRestartManager;
import org.l2jmobius.gameserver.instancemanager.PremiumManager;
import org.l2jmobius.gameserver.instancemanager.PunishmentManager;
import org.l2jmobius.gameserver.instancemanager.QuestManager;
import org.l2jmobius.gameserver.instancemanager.RaidBossPointsManager;
import org.l2jmobius.gameserver.instancemanager.RaidBossSpawnManager;
import org.l2jmobius.gameserver.instancemanager.SellBuffsManager;
import org.l2jmobius.gameserver.instancemanager.ServerRestartManager;
import org.l2jmobius.gameserver.instancemanager.SiegeManager;
import org.l2jmobius.gameserver.instancemanager.SoDManager;
import org.l2jmobius.gameserver.instancemanager.SoIManager;
import org.l2jmobius.gameserver.instancemanager.TerritoryWarManager;
import org.l2jmobius.gameserver.instancemanager.WalkingManager;
import org.l2jmobius.gameserver.instancemanager.ZoneManager;
import org.l2jmobius.gameserver.instancemanager.games.Lottery;
import org.l2jmobius.gameserver.instancemanager.games.MonsterRace;
import org.l2jmobius.gameserver.model.AutoSpawnHandler;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.model.events.EventDispatcher;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.impl.OnServerStart;
import org.l2jmobius.gameserver.model.olympiad.Hero;
import org.l2jmobius.gameserver.model.olympiad.Olympiad;
import org.l2jmobius.gameserver.model.partymatching.PartyMatchRoomList;
import org.l2jmobius.gameserver.model.partymatching.PartyMatchWaitingList;
import org.l2jmobius.gameserver.model.sevensigns.SevenSigns;
import org.l2jmobius.gameserver.model.sevensigns.SevenSignsFestival;
import org.l2jmobius.gameserver.network.GameClient;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.PacketHandler;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.scripting.ScriptEngineManager;
import org.l2jmobius.gameserver.taskmanager.GameTimeTaskManager;
import org.l2jmobius.gameserver.taskmanager.ItemsAutoDestroyTaskManager;
import org.l2jmobius.gameserver.taskmanager.TaskManager;
import org.l2jmobius.gameserver.ui.Gui;
import org.l2jmobius.gameserver.util.Broadcast;

public class GameServer
{
	private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
	
	private DeadLockDetector _deadDetectThread = null; 
	private static GameServer INSTANCE;
	public static final Calendar dateTimeServerStarted = Calendar.getInstance();
	private static boolean isfufei = true;
	private static final String[] jiqima =
	{
	 "00000000000000018CE38E030040CFFFBFEBFBFF000A0652",//小天電腦
	 "YS202010001110AABFEBFBFF000206D7"//小天服務器
	};

	public long getUsedMemoryMB()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
	}

	public DeadLockDetector getDeadLockDetectorThread()
	{
		return _deadDetectThread;
	}

	public GameServer() throws Exception
	{
		final long serverLoadStart = System.currentTimeMillis();

		// GUI
		final PropertiesParser interfaceConfig = new PropertiesParser(Config.INTERFACE_CONFIG_FILE);
		Config.ENABLE_GUI = interfaceConfig.getBoolean("EnableGUI", true);
		if (Config.ENABLE_GUI && !GraphicsEnvironment.isHeadless())
		{
			Config.DARK_THEME = interfaceConfig.getBoolean("DarkTheme", true);
			System.out.println("GameServer: Running in GUI mode.");
			new Gui();
		}

		// Create log folder
		final File logFolder = new File(".", "log");
		logFolder.mkdir();

		// Create input stream for log file -- or store file data into memory
		try (InputStream is = new FileInputStream(new File("./log.cfg")))
		{
			LogManager.getLogManager().readConfiguration(is);
		}

		if(isfufei){
		// Initialize config
		Config.load(ServerMode.GAME);

		printSection("Database");
		DatabaseFactory.init();

		printSection("ThreadPool");
		ThreadPool.init();

		// Start game time task manager early
		GameTimeTaskManager.getInstance();

		printSection("IdManager");
		IdManager.getInstance();

		printSection("Scripting Engine");
		EventDispatcher.getInstance();
		ScriptEngineManager.getInstance();

		printSection("World");
		InstanceManager.getInstance();
		World.getInstance();
		MapRegionManager.getInstance();
		AnnouncementsTable.getInstance();
		GlobalVariablesManager.getInstance();

		printSection("Data");
		CategoryData.getInstance();
		SecondaryAuthData.getInstance();

		printSection("Skills");
		EffectHandler.getInstance().executeScript();
		EnchantSkillGroupsData.getInstance();
		SkillTreeData.getInstance();
		SkillData.getInstance();
		PetSkillData.getInstance();

		printSection("Items");
		ItemTable.getInstance();
		EnchantItemGroupsData.getInstance();
		EnchantItemData.getInstance();
		EnchantItemOptionsData.getInstance();
		ElementalAttributeData.getInstance();
		OptionData.getInstance();
		EnchantItemHPBonusData.getInstance();
		MerchantPriceConfigTable.getInstance().loadInstances();
		BuyListData.getInstance();
		MultisellData.getInstance();
		RecipeData.getInstance();
		ArmorSetData.getInstance();
		FishData.getInstance();
		FishingMonstersData.getInstance();
		FishingRodsData.getInstance();
		HennaData.getInstance();
		PrimeShopData.getInstance();
		PcCafePointsManager.getInstance();

		printSection("Characters");
		ClassListData.getInstance();
		InitialEquipmentData.getInstance();
		InitialShortcutData.getInstance();
		ExperienceData.getInstance();
		PlayerXpPercentLostData.getInstance();
		KarmaData.getInstance();
		HitConditionBonusData.getInstance();
		PlayerTemplateData.getInstance();
		CharInfoTable.getInstance();
		AdminData.getInstance();
		RaidBossPointsManager.getInstance();
		PetDataTable.getInstance();
		CharSummonTable.getInstance().init();

		if (Config.PREMIUM_SYSTEM_ENABLED)
		{
			LOGGER.info("PremiumManager: Premium system is enabled.");
			PremiumManager.getInstance();
		}

		printSection("Clans");
		ClanTable.getInstance();
		CHSiegeManager.getInstance();
		ClanHallTable.getInstance();
		ClanHallAuctionManager.getInstance();

		printSection("Geodata");
		GeoEngine.getInstance();

		printSection("NPCs");
		DoorData.getInstance();
		FenceData.getInstance();
		SkillLearnData.getInstance();
		NpcData.getInstance();
		FakePlayerData.getInstance();
		FakePlayerChatManager.getInstance();
		WalkingManager.getInstance();
		StaticObjectData.getInstance();
		ItemAuctionManager.getInstance();
		CastleManager.getInstance().loadInstances();
		SchemeBufferTable.getInstance();
		ZoneManager.getInstance();
		GrandBossManager.getInstance().initZones();
		EventDropManager.getInstance();

		printSection("Olympiad");
		Olympiad.getInstance();
		Hero.getInstance();

		printSection("Seven Signs");
		SevenSigns.getInstance();

		// Call to load caches
		printSection("Cache");
		HtmCache.getInstance();
		CrestTable.getInstance();
		TeleportLocationTable.getInstance();
		UIData.getInstance();
		PartyMatchWaitingList.getInstance();
		PartyMatchRoomList.getInstance();
		PetitionManager.getInstance();
		AugmentationData.getInstance();
		CursedWeaponsManager.getInstance();
		TransformData.getInstance();
		BotReportTable.getInstance();
		if (Config.SELLBUFF_ENABLED)
		{
			SellBuffsManager.getInstance();
		}
		if (Config.MULTILANG_ENABLE)
		{
			SystemMessageId.loadLocalisations();
			NpcStringId.loadLocalisations();
			SendMessageLocalisationData.getInstance();
			NpcNameLocalisationData.getInstance();
		}

		printSection("Scripts");
		QuestManager.getInstance();
		BoatManager.getInstance();
		AirShipManager.getInstance();
		SoDManager.getInstance();
		SoIManager.getInstance();
		suijizz.getInstance().load();

		try
		{
			LOGGER.info("Loading server scripts...");
			ScriptEngineManager.getInstance().executeScript(ScriptEngineManager.MASTER_HANDLER_FILE);
			ScriptEngineManager.getInstance().executeScriptList();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Failed to execute script list!", e);
		}

		SpawnTable.getInstance().load();
		DayNightSpawnManager.getInstance().trim().notifyChangeMode();
		FourSepulchersManager.getInstance().init();
		DimensionalRiftManager.getInstance();
		RaidBossSpawnManager.getInstance();

		printSection("Siege");
		SiegeManager.getInstance().getSieges();
		CastleManager.getInstance().activateInstances();
		FortManager.getInstance().loadInstances();
		FortManager.getInstance().activateInstances();
		FortSiegeManager.getInstance();
		SiegeScheduleData.getInstance();

		MerchantPriceConfigTable.getInstance().updateReferences();
		TerritoryWarManager.getInstance();
		CastleManorManager.getInstance();
		MercTicketManager.getInstance();

		QuestManager.getInstance().report();

		if (Config.SAVE_DROPPED_ITEM)
		{
			ItemsOnGroundManager.getInstance();
		}

		if ((Config.AUTODESTROY_ITEM_AFTER > 0) || (Config.HERB_AUTO_DESTROY_TIME > 0))
		{
			ItemsAutoDestroyTaskManager.getInstance();
		}

		MonsterRace.getInstance();
		Lottery.getInstance();

		SevenSigns.getInstance().spawnSevenSignsNPC();
		SevenSignsFestival.getInstance();
		AutoSpawnHandler.getInstance();

		LOGGER.info("AutoSpawnHandler: Loaded " + AutoSpawnHandler.getInstance().size() + " handlers in total.");

		if (Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
		}

		if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
		{
			FishingChampionshipManager.getInstance();
		}

		TaskManager.getInstance();

		AntiFeedManager.getInstance().registerEvent(AntiFeedManager.GAME_ID);

		if (Config.ALLOW_MAIL)
		{
			MailManager.getInstance();
		}
		if (Config.CUSTOM_MAIL_MANAGER_ENABLED)
		{
			CustomMailManager.getInstance();
		}

		if (EventDispatcher.getInstance().hasListener(EventType.ON_SERVER_START))
		{
			EventDispatcher.getInstance().notifyEventAsync(new OnServerStart());
		}

		PunishmentManager.getInstance();

		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

		LOGGER.info("IdManager: Free ObjectID's remaining: " + IdManager.getInstance().size());

		if ((Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) && Config.RESTORE_OFFLINERS)
		{
			OfflineTraderTable.getInstance().restoreOfflineTraders();
		}

		if (Config.SERVER_RESTART_SCHEDULE_ENABLED)
		{
			ServerRestartManager.getInstance();
		}

		if (Config.PRECAUTIONARY_RESTART_ENABLED)
		{
			PrecautionaryRestartManager.getInstance();
		}
		if (Config.DEADLOCK_DETECTOR)
		{
			_deadDetectThread = new DeadLockDetector(Duration.ofSeconds(Config.DEADLOCK_CHECK_INTERVAL), () ->
			{
				if (Config.RESTART_ON_DEADLOCK)
				{
					Broadcast.toAllOnlinePlayers("Server has stability issues - restarting now.");
					Shutdown.getInstance().startShutdown(null, 60, true);
				}
			});
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		}
		else
		{
			_deadDetectThread = null;
		}
		System.gc();
		final long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
		LOGGER.info(getClass().getSimpleName() + "：已用內存 - " + getUsedMemoryMB() + " MB.總內存 - " + totalMem + "MB.");
		LOGGER.info(getClass().getSimpleName() + "：服務器最大連接數為 - " + Config.MAXIMUM_ONLINE_USERS + ".");
		LOGGER.info(getClass().getSimpleName() + "：服務器啟動耗費時間 - " + ((System.currentTimeMillis() - serverLoadStart) / 1000) + " 秒.");
		LOGGER.info(getClass().getSimpleName() + "：伺服器已經啟動完畢！");
		LOGGER.info("------------------ " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ------------------");

		final NetServer<GameClient> server = new NetServer<>(Config.GAMESERVER_HOSTNAME, Config.PORT_GAME, new PacketHandler(), GameClient::new);
		server.setName(getClass().getSimpleName());
		server.getNetConfig().setReadPoolSize(Config.CLIENT_READ_POOL_SIZE);
		server.getNetConfig().setExecutePoolSize(Config.CLIENT_EXECUTE_POOL_SIZE);
		server.getNetConfig().setPacketQueueLimit(Config.PACKET_QUEUE_LIMIT);
		server.getNetConfig().setPacketFloodDisconnect(Config.PACKET_FLOOD_DISCONNECT);
		server.getNetConfig().setPacketFloodDrop(Config.PACKET_FLOOD_DROP);
		server.getNetConfig().setPacketFloodLogged(Config.PACKET_FLOOD_LOGGED);
		server.getNetConfig().setFailedDecryptionLogged(Config.FAILED_DECRYPTION_LOGGED);
		server.getNetConfig().setTcpNoDelay(Config.TCP_NO_DELAY);
		server.start();

		LoginServerThread.getInstance().start();

		Toolkit.getDefaultToolkit().beep();
		}
	}

	public long getStartedTime()
	{
		return ManagementFactory.getRuntimeMXBean().getStartTime();
	}

	public String getUptime()
	{
		final long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
		final long hours = uptime / 3600;
		final long mins = (uptime - (hours * 3600)) / 60;
		final long secs = ((uptime - (hours * 3600)) - (mins * 60));
		if (hours > 0)
		{
			return hours + "小時" + mins + "分" + secs + "秒";
		}
		return mins + "分" + secs + "秒";
	}

	public String getjiqima(){
			String property= System.getProperty("os.name").toLowerCase();
			Map<String,Object> codeMap=new HashMap<>(2);
			String result = "";
			if(property.contains("windows")){
            String processorId = getCPUSerialNumber();
            codeMap.put("ProcessorId",processorId);
            String serialNumber = getHardDiskSerialNumber();
            codeMap.put("SerialNumber",serialNumber);
            String codeMapStr = codeMap.toString();
            result= getSplitString(codeMapStr, "", 4);
		   }else if(property.contains("linux")){// 獲取cpu序列號
            String boisVersion = getBoisVersion();
            codeMap.put("boisVersion",boisVersion);
            System.out.println("boisVersion：" + boisVersion);
            String uuid = getUUID();
            codeMap.put("uuid",uuid);
            System.out.println("uuid：" + uuid);
            String codeMapStr = codeMap.toString();
            result= getSplitString(codeMapStr, "", 0);
		   }
		return result;
	 }

	 public static String getCPUSerialNumber() {
        String next;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String serial = sc.next();
            next = sc.next();
        } catch (IOException e) {
            throw new RuntimeException("獲取CPU序列號失敗");
        }
        return next;
    }

    public static String getHardDiskSerialNumber() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "path", "win32_physicalmedia", "get", "serialnumber"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String serial = sc.next();
            return sc.next();
        } catch (IOException e) {
            throw new RuntimeException("獲取硬盤序列號失敗");
        }
    }

    private static String getSplitString(String str, String split, int length) {
		String intm = "";
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i % length == 0 && i > 0) {
                temp.append(split);
            }
            temp.append(str.charAt(i));
        }
		intm = temp.toString();
		intm = intm.replace("SerialNumber", "");
		intm = intm.replace("ProcessorId", "");
		intm = intm.replace("=", "");
		intm = intm.replace("_", "");
 		intm = intm.replace(".", "");
		intm = intm.replace(",", "");
		intm = intm.replace("}", "");
 		intm = intm.replace("{", "");
 		intm = intm.replace(" ", "");
      return intm;
    }

    public static String getBoisVersion() {
        String result = "";
        Process p;
        try {
            // 管道
            p = Runtime.getRuntime().exec("sudo dmidecode -s bios-version");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
                break;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("獲取主板信息錯誤");
        }
        return result;
    }

    public static String getUUID() {
        String result = "";
        try {
            Process process = Runtime.getRuntime().exec("sudo dmidecode -s system-uuid");
            InputStream in;
            BufferedReader br;
            in = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            while (in.read() != -1) {
                result = br.readLine();
            }
            br.close();
            in.close();
            process.destroy();
            System.out.println("獲取序列號："+result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
		return result;
    }


	private void jiqimas() {
	    StringBuffer sb1 = new StringBuffer();
		sb1.append("當前電腦機器碼:"+getjiqima()+",請聯繫QQ:619028749獲取權限開啟服務端"+"\r\n");
		File file = new File("D:\\機器碼.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(file,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb1.toString());
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public static void main(String[] args) throws Exception
	{
		INSTANCE = new GameServer();
	}
	
	private void printSection(String section)
	{
		String s = "=[ " + section + " ]";
		while (s.length() < 61)
		{
			s = "-" + s;
		}
		LOGGER.info(s);
	}
	
	public static GameServer getInstance()
	{
		return INSTANCE;
	}
}
