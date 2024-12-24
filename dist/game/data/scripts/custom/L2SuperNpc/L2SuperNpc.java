

package custom.L2SuperNpc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import javolution.text.TextBuilder;
import java.util.*;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.model.olympiad.Hero;
import org.l2jmobius.gameserver.model.olympiad.Olympiad;
import org.l2jmobius.gameserver.model.World;
import org.l2jmobius.gameserver.data.xml.NpcData;
import org.l2jmobius.gameserver.instancemanager.GrandBossManager;
import org.l2jmobius.gameserver.instancemanager.QuestManager;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowBaseAttributeCancelWindow;
import org.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.util.Broadcast;

import ai.AbstractNpcAI;
/**
 * 
 * @author Jacky
 *
 */
 
public class L2SuperNpc extends AbstractNpcAI
{
	private final static String qn = "L2SuperNpc";
	private final static int QuestId = 20001;
	private final static int npcIds = 955;
	private final static int[] BOSSES = {29001,29006,29014,29019,29020,29022,29028,29067,29068,29066,33349,33350,33351};
	private final static String Pos1Color = "FF6A6A";
	private final static String Pos2Color = "ffff00";
	private final static String Pos3Color = "00ff00";
	private static Date openServerDate = null;
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = "";
		
		if (event.equalsIgnoreCase("Main"))
		{
			NpcHtmlMessage rateReply = new NpcHtmlMessage(0, 1);
			rateReply.setFile(player, "data/html/supernpc/955.htm");
			player.sendPacket(rateReply);
		}		
		else if (event.equalsIgnoreCase("Developers"))
		{
			NpcHtmlMessage rateReply = new NpcHtmlMessage(0, 1);
			rateReply.setFile(player, "data/html/supernpc/Developers.htm");
			player.sendPacket(rateReply);
		}
		
		else if (event.startsWith("MultId "))
		{
			String val = event.substring(7);
			StringTokenizer st = new StringTokenizer(val);
			String Multid = st.nextToken();
			int Multidval = Integer.parseInt(Multid);
			MultisellData.getInstance().separateAndSend(Multidval, player, npc , false);
		}

		else if (event.startsWith("ExcMultId "))
		{
			String val = event.substring(10);
			StringTokenizer st = new StringTokenizer(val);
			String Multid = st.nextToken();
			int Multidval = Integer.parseInt(Multid);
			MultisellData.getInstance().separateAndSend(Multidval, player, npc , true);
		}
		
		else if (event.equalsIgnoreCase("Version"))
		{
			NpcHtmlMessage rateReply = new NpcHtmlMessage(0, 1);
			rateReply.setFile(player, "data/html/supernpc/Version.htm");
			player.sendPacket(rateReply);
		}

		else if (event.equalsIgnoreCase("Boss"))
		{
			TextBuilder tb = new TextBuilder();
			tb.append("<html><title>终极BOSS信息采集员</title><body><br><center>");
			tb.append("<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>");
			tb.append("安塔瑞斯中其中一只被狩猎,其余均不可狩猎<br>");
			for(int boss : BOSSES)
			{
			Connection con = null;
			try
			{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM grandboss_data WHERE boss_id = ?");
			statement.setInt(1, boss);
			ResultSet rset = statement.executeQuery();
			if(rset != null && rset.next()) {
			String name = NpcData.getInstance().getTemplate(boss).getName();
			int STATUS = rset.getInt("status");
			long delay = rset.getLong("respawn_time");
			if(STATUS == 0)
			{
				tb.append("<font color=\"00C3FF\">" + name + "</color>: " + "<font color=\"9CC300\">沉睡中(可挑战)</color>"+"<br1>");
			}else if (delay*1000 > System.currentTimeMillis())
			{
				int hours = (int) ((delay - System.currentTimeMillis()) / 1000 / 60 / 60);
				int mins = (int) (((delay - (hours * 60 * 60 * 1000)) - System.currentTimeMillis()) / 1000 / 60);
				int seconts = (int) (((delay - ((hours * 60 * 60 * 1000) + (mins * 60 * 1000))) - System.currentTimeMillis()) / 1000);
				tb.append("<font color=\"00C3FF\">" + name + "</color>" + "<font color=\"FFFFFF\">" +" " + "刷出时间:</color>" + " " + " <font color=\"32C332\">" + hours + "小时: " + mins + "分: " + seconts + "秒后</color><br1>");
			}
			}
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			LOGGER.warning("获取道具列表失败!" + e.getMessage());
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception localException3)
			{
			}
		}
		}
			tb.append("</body></html>");
			NpcHtmlMessage msg = new NpcHtmlMessage(0, 1);
			msg.setHtml(tb.toString());
			player.sendPacket(msg);
		}
				
		else if(event.equalsIgnoreCase("RichesRank"))
		{
			byte pos = 0;
			
			TextBuilder tb = new TextBuilder();
			tb.append("<html><title>连击排行榜</title><body><br1>");
			tb.append("<center><table width=300><tr><td><center><font color=CAFF70>排名</center></td><td><center>玩家名</center></td><td><center>最高连续击杀数</font></center></td></tr>");
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT char_name,maxckills FROM characters WHERE maxckills>0 and accesslevel=0 order by maxckills desc limit 20");
				ResultSet rset = statement.executeQuery();
				while (rset.next())
				{
					String char_name = rset.getString("char_name");
					String char_maxckills = rset.getString("maxckills");
					pos += 1;
					if (pos ==1)
						tb.append("<tr><td><center><font color="+ Pos1Color +">" + pos + "</center></td><td><center>" + char_name + "</center></td><td><center>" + char_maxckills + "</font></center></td></tr>");
					else if (pos ==2)
						tb.append("<tr><td><center><font color="+ Pos2Color +">" + pos + "</center></td><td><center>" + char_name + "</center></td><td><center>" + char_maxckills + "</font></center></td></tr>");
					else if (pos ==3)
						tb.append("<tr><td><center><font color="+ Pos3Color +">" + pos + "</center></td><td><center>" + char_name + "</center></td><td><center>" + char_maxckills + "</font></center></td></tr>");
					else
						tb.append("<tr><td><center>" + pos + "</center></td><td><center>" + char_name + "</center></td><td><center>" + char_maxckills + "</center></td></tr>");

				}
				rset.close();
				statement.close();
			}
			catch (Exception e)
			{			
			}
			finally
			{
			try
			{
				con.close();
			}
			catch (Exception localException3)
			{
			}
			}
			tb.append("</body></html>");
			NpcHtmlMessage msg = new NpcHtmlMessage(0, 1);
			msg.setHtml(tb.toString());
			player.sendPacket(msg);
		}
		else if (event.startsWith("ShopId "))
		{
			String val = event.substring(7);
			StringTokenizer st = new StringTokenizer(val);
			String Shopid = st.nextToken();
			int Shopidval = Integer.parseInt(Shopid);
			NpcHtmlMessage rateReply = new NpcHtmlMessage(0,1);
			rateReply.setFile(player, "data/html/supernpc/Shop/ShopId-"+ Shopidval +".htm");
			player.sendPacket(rateReply);
		}
		else if (event.startsWith("Noble"))
		{
			if(player.isNoble()){
				player.sendMessage("你已经是贵族了，无需再操作");
			}else if(player.getLevel() >= 76 && player.getInventory().getInventoryItemCount(57,0) >= 10000000){
				player.setNoble(true);
				Broadcast.toAllOnlinePlayers("恭喜玩家[" + player.getName() + " ]成功转为贵族.", false);
			}else{
				player.sendMessage("转为贵族需要金币1000w且人物等级大于或等于76");
			}
		}
		else if (event.startsWith("Hero"))
		{
			if(player.isHero()){
				player.sendMessage("你已经是英雄了，无需再操作");
			}else if(player.getLevel() >= 85 && player.getInventory().getInventoryItemCount(9143,0) >= 1){
				player.setHero(true);
				Broadcast.toAllOnlinePlayers("恭喜玩家[" + player.getName() + " ]成功转为英雄.", false);
			}else{
				player.sendMessage("转为英雄需要英雄币且人物等级大于或等于85");
			}
		}
		else if(event.equalsIgnoreCase("PKRank"))
		{
			byte pos = 0;
			
			TextBuilder tb = new TextBuilder();
			tb.append("<html><title>PK排行榜</title><body>");
			tb.append("<center><table width=300><tr><td><center><font color=FF6A6A>排名</center></td><td><center>玩家名</center></td><td><center>PK值</center></td><td><center>Online</font></center></td></font></tr>");
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT char_name,pkkills,online FROM characters WHERE pkkills>0 and accesslevel=0 order by pkkills desc limit 20");
				ResultSet rset = statement.executeQuery();
				while (rset.next())
				{
					String char_name = rset.getString("char_name");
					String char_pkkills = rset.getString("pkkills");
					String OnlineStat ="";
					String OnlineClolor ="";
					int isOnline = rset.getInt("online");
					if (isOnline == 1)
					{
						OnlineStat = "Yes";
						OnlineClolor = "00ff00";
					}
					else
					{
						OnlineStat = "No";
						OnlineClolor = "ff0000";
					}	
					pos += 1;
					if (pos ==1)
						tb.append("<tr><td><center><font color="+ Pos1Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + char_pkkills + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else if (pos ==2)
						tb.append("<tr><td><center><font color="+ Pos2Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + char_pkkills + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else if (pos ==3)
						tb.append("<tr><td><center><font color="+ Pos3Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + char_pkkills + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else
						tb.append("<tr><td><center><font color=\"FFFFFF\">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + char_pkkills + "</center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
}
				rset.close();
				statement.close();
			}
			catch (Exception e)
			{
				
			}
			finally
			{
			try
			{
				con.close();
			}
			catch (Exception localException3)
			{
			}
			}	
			tb.append("</table></center></body></html>");
			NpcHtmlMessage msg = new NpcHtmlMessage(0, 1);
			msg.setHtml(tb.toString());
			player.sendPacket(msg);			
		}
		else if(event.equalsIgnoreCase("PvPRank"))
		{
			byte pos = 0;
			
			TextBuilder tb = new TextBuilder();
			tb.append("<html><title>PvP排行榜</title><body>");
			tb.append("<center><table width=300><tr><td><center><font color=20B2AA>排名</center></td><td><center>玩家名</center></td><td><center>PvP值</center></td><td><center>Online</font></center></td></tr>");
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT char_name,pvpkills,online FROM characters WHERE pvpkills>0 and accesslevel=0 order by pvpkills desc limit 20");
				ResultSet rset = statement.executeQuery();
				while (rset.next())
				{
					String char_name = rset.getString("char_name");
					String char_pvpkills = rset.getString("pvpkills");
					String OnlineStat ="";
					String OnlineClolor ="";
					int isOnline = rset.getInt("online");
					if (isOnline == 1)
					{
						OnlineStat = "Yes";
						OnlineClolor = "00ff00";
					}
					else
					{
						OnlineStat = "No";
						OnlineClolor = "ff0000";
					}	
					pos += 1;
					if (pos ==1)
						tb.append("<tr><td><center><font color="+ Pos1Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + char_pvpkills + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else if (pos ==2)
						tb.append("<tr><td><center><font color="+ Pos2Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + char_pvpkills + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else if (pos ==3)
						tb.append("<tr><td><center><font color="+ Pos3Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + char_pvpkills + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else
						tb.append("<tr><td><center><font color=\"FFFFFF\">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + char_pvpkills + "</center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					}
				rset.close();
				statement.close();
			}
			catch (Exception e)
			{
				
			}
			finally
			{
			try
			{
				con.close();
			}
			catch (Exception localException3)
			{
			}			}	
			tb.append("</table></center></body></html>");
			NpcHtmlMessage msg = new NpcHtmlMessage(0, 1);
			msg.setHtml(tb.toString());
			player.sendPacket(msg);	
		}
		else if(event.equalsIgnoreCase("FameRank"))
		{
			byte pos = 0;
			
			TextBuilder tb = new TextBuilder();
			tb.append("<html><title>荣誉排行榜</title><body>");
			tb.append("<center><table width=300><tr><td><center><font color=ffff00>排名</center></td><td><center>玩家名</center></td><td><center>荣誉值</center></td><td><center>Online</font></center></td></tr>");		
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT char_name,fame,online FROM characters WHERE fame>0 and accesslevel=0 order by fame desc limit 20");
				ResultSet rset = statement.executeQuery();
				while (rset.next())
				{
					String char_name = rset.getString("char_name");
					String fame = rset.getString("fame");
					String OnlineStat ="";
					String OnlineClolor ="";
					int isOnline = rset.getInt("online");
					if (isOnline == 1)
					{
						OnlineStat = "Yes";
						OnlineClolor = "00ff00";
					}
					else
					{
						OnlineStat = "No";
						OnlineClolor = "ff0000";
					}	
					pos += 1;
					if (pos ==1)
						tb.append("<tr><td><center><font color="+ Pos1Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + fame + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else if (pos ==2)
						tb.append("<tr><td><center><font color="+ Pos2Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + fame + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else if (pos ==3)
						tb.append("<tr><td><center><font color="+ Pos3Color +">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + fame + "</font></center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
					else
						tb.append("<tr><td><center><font color=\"FFFFFF\">" + pos + "</center></td><td><center><a action=\"bypass -h Quest L2SuperNpc CharInfor "+char_name+"\">" + char_name + "</a></center></td><td><center>" + fame + "</center></td><td><center><font color="+OnlineClolor+">"+OnlineStat+"</center></font></td></tr>");
				}
				rset.close();
				statement.close();
			}
			catch (Exception e)
			{}
			finally
			{
			try
			{
				con.close();
			}
			catch (Exception localException3)
			{
			}
			}	
			tb.append("</table></center></body></html>");
			NpcHtmlMessage msg = new NpcHtmlMessage(0, 1);
			msg.setHtml(tb.toString());
			player.sendPacket(msg);	
		}				
		else if (event.equalsIgnoreCase("Dlv"))
		{
			int level = player.getLevel();
			NpcHtmlMessage rateReply = new NpcHtmlMessage(0, 1);
			rateReply.setFile(player, "data/html/supernpc/Dlv.htm");
			rateReply.replace("%ItemCounts%", String.valueOf(100 * 100 * level));
			player.sendPacket(rateReply);
		}
		else if (event.startsWith("ReleaseAttribute"))
		{		
			player.sendPacket(new ExShowBaseAttributeCancelWindow(player));
		}	
		else if (event.equalsIgnoreCase("ZSWQ"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.ATRS_WEAPON * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					MultisellData.getInstance().separateAndSend(59111111, player, npc, false);
				}else{
				player.sendPacket(new ExShowScreenMessage("止水武器还没有开放兑换", 5000));	
			}				
			}
		}

		else if (event.equalsIgnoreCase("HPWQ"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.BLKS_WEAPON * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					MultisellData.getInstance().separateAndSend(59111112, player, npc, true);
				}else{
				player.sendPacket(new ExShowScreenMessage("虎魄武器还没有开放兑换", 5000));	
			}				
			}
		}
		else if (event.equalsIgnoreCase("PXWQ"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.LDBE_WEAPON * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					MultisellData.getInstance().separateAndSend(59111113, player, npc, true);
				}else{
				player.sendPacket(new ExShowScreenMessage("破星武器还没有开放兑换", 5000));	
			}				
			}
		}
		else if (event.equalsIgnoreCase("ZSZB"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.KDL_F * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					MultisellData.getInstance().separateAndSend(58111113, player, npc, true);
				}else{
				player.sendPacket(new ExShowScreenMessage("止水装备还没有开放升级", 5000));	
			}				
			}
		}
		else if (event.equalsIgnoreCase("HPZB"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.ADO_F * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					MultisellData.getInstance().separateAndSend(58111114, player, npc, true);
				}else{
				player.sendPacket(new ExShowScreenMessage("虎魄装备还没有开放升级", 5000));	
			}				
			}
		}
		else if (event.equalsIgnoreCase("PXZB"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.HMSS * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					MultisellData.getInstance().separateAndSend(58111115, player, npc, true);
				}else{
				player.sendPacket(new ExShowScreenMessage("破星装备还没有开放升级", 5000));	
			}				
			}
		}		
		return htmltext;
	}

	public void inter(Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			
			PreparedStatement statement = con.prepareStatement("INSERT INTO heroes (charId, class_id, count, played, claimed) VALUES (?,?,?,?,?)");
			
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, player.getClassId().getId());			
			statement.setInt(3, 1);
			statement.setInt(4, 1);			
			statement.setString(5, "false");
			statement.executeUpdate();			
			statement.close();
		}catch (Exception e)
		{
			LOGGER.warning("写入yingxiong失败!" + e.getMessage());
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception localException3)
			{
			}
		}
	}

	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		String htmltext = "";
		QuestState qs = player.getQuestState(qn);
		if (qs == null)
			qs = newQuestState(player);
		if(npc.getId()==955)
		{
			NpcHtmlMessage rateReply = new NpcHtmlMessage(0, 1);
			rateReply.setFile(player, "data/html/supernpc/955.htm");
			player.sendPacket(rateReply);
		}
		return htmltext;
	}
	
	public L2SuperNpc() 
	{
		super();
		addStartNpc(npcIds);
		addTalkId(npcIds);
		addFirstTalkId(npcIds);
	}
	
	public static void main(String[] args)
	{
		new L2SuperNpc();
		System.out.println("INFO 多功能NPC V2.0版读取成功");
	}		
}