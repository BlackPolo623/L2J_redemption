package custom.NpcBuffers;

import static org.l2jmobius.gameserver.util.Util.formatAdena;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.data.ItemTable;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.instancemanager.QuestManager;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Summon;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.Pet;
import org.l2jmobius.gameserver.model.actor.instance.Cubic;
import org.l2jmobius.gameserver.model.actor.instance.Servitor;
import org.l2jmobius.gameserver.model.actor.stat.PlayerStat;
import org.l2jmobius.gameserver.model.actor.stat.SummonStat;
import org.l2jmobius.gameserver.model.actor.status.PlayerStatus;
import org.l2jmobius.gameserver.model.actor.status.SummonStatus;
import org.l2jmobius.gameserver.model.effects.EffectType;
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.model.olympiad.OlympiadManager;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.network.SystemMessageId;
import org.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.network.serverpackets.SetSummonRemainTime;
import org.l2jmobius.gameserver.network.serverpackets.SetupGauge;
import org.l2jmobius.commons.util.Rnd;
import ai.AbstractNpcAI;


/**
 * @author RobíkBobík, Charlie
 */
public class PremiumNpcBuffer extends AbstractNpcAI
{
	private static final boolean DEBUG = false;
	
	private static void print(Exception e)
	{
		if (DEBUG)
		{
			e.printStackTrace();
		}
	}
	
	private static final String QUEST_LOADING_INFO = "PremiumNpcBuffer";
	private static final int NPC_ID = 33338;
	
	private static final String PREMIUM_TITLE_NAME = "天堂II豪华辅助状态师";
	private static final boolean PREMIUM_SCRIPT_RELOAD = false;
	private static final boolean PREMIUM_SMART_WINDOW = false;
	private static final boolean PREMIUM_ENABLE_VIP_BUFFER = true;
	private static final boolean PREMIUM_ENABLE_BUFF_SECTION = true;
	private static final boolean PREMIUM_ENABLE_SCHEME_SYSTEM = true;
	private static final boolean PREMIUM_ENABLE_HEAL = true;
	private static final boolean PREMIUM_ENABLE_BUFFS = true;
	private static final boolean PREMIUM_ENABLE_RESIST = true;
	private static final boolean PREMIUM_ENABLE_SONGS = true;
	private static final boolean PREMIUM_ENABLE_DANCES = true;
	private static final boolean PREMIUM_ENABLE_CHANTS = true;
	private static final boolean PREMIUM_ENABLE_OTHERS = true;
	private static final boolean PREMIUM_ENABLE_SPECIAL = true;
	private static final boolean PREMIUM_ENABLE_CUBIC =true;
	private static final boolean PREMIUM_ENABLE_BUFF_REMOVE = true;
	private static final boolean PREMIUM_ENABLE_BUFF_SET = true;
	private static final boolean PREMIUM_BUFF_WITH_KARMA = true;
	private static final boolean PREMIUM_FREE_BUFFS = true;
	private static final boolean PREMIUM_TIME_OUT = true;
	private static final int PREMIUM_TIME_OUT_TIME = 1;
	private static final int PREMIUM_MIN_LEVEL = 1;
	private static final int PREMIUM_BUFF_REMOVE_PRICE =1;
	private static final int PREMIUM_HEAL_PRICE = 10000;
	private static final int PREMIUM_BUFF_PRICE = 1000;
	private static final int PREMIUM_RESIST_PRICE = 10000;
	private static final int PREMIUM_SONG_PRICE = 10000;
	private static final int PREMIUM_DANCE_PRICE = 10000;
	private static final int PREMIUM_CHANT_PRICE = 10000;
	private static final int PREMIUM_OTHERS_PRICE = 10000;
	private static final int PREMIUM_SPECIAL_PRICE = 10000;
	private static final int PREMIUM_CUBIC_PRICE = 10000;
	private static final int PREMIUM_BUFF_SET_PRICE = 10000;
	private static final int PREMIUM_SCHEME_BUFF_PRICE = 10000;
	private static final int PREMIUM_SCHEMES_PER_PLAYER = 10000;
	private static final int PREMIUM_CONSUMABLE_ID = 57;
	private static final int PREMIUM_MAX_SCHEME_BUFFS = 24;
	private static final int PREMIUM_MAX_SCHEME_DANCES = 12;
	
	private static final String PREMIUM_SET_FIGHTER = "战士";
	private static final String PREMIUM_SET_MAGE = "法师";
	private static final String PREMIUM_SET_ALL = "通用";
	private static final String PREMIUM_SET_NONE = "无";
	
	private String rebuildMainHtml(QuestState st)
	{
		String MAIN_HTML_MESSAGE = "<html><body scroll=\"no\"><head><title>" + PREMIUM_TITLE_NAME + "</title></head><center>";
		String MESSAGE = "";
		int td = 0;
		String[] TRS =
		{
			"<tr><td height=23>",
			"</td>",
			"<td height=23>",
			"</td></tr>"
		};
		
		final String bottonA, bottonB, bottonC;
		if (st.getInt("Pet-On-Off") == 1)
		{
			bottonA = "一键豪华状态" + Rnd.get(0, 10000);
			bottonB = "治愈宠物" + Rnd.get(0, 10000);
			bottonC = "移除状态" + Rnd.get(0, 10000);
			MAIN_HTML_MESSAGE += "<button value=\"切　换　至　角　色　模　式\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " buffpet 0 0 0\" width=190 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		else
		{
			bottonA = "一键豪华状态" + Rnd.get(0, 10000);
			bottonB = "自我治愈" + Rnd.get(0, 10000);
			bottonC = "移除状态" + Rnd.get(0, 10000);
			MAIN_HTML_MESSAGE += "<button value=\"切　换　至　宠　物　模　式\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " buffpet 1 0 0\" width=190 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		
		if (PREMIUM_ENABLE_BUFF_SECTION)
		{
			if (PREMIUM_ENABLE_BUFFS)
			{
				if (td > 2)
				{
					td = 0;
				}
				MESSAGE += TRS[td] + "<button value=\"一般状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_buffs 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
				td += 2;
			}
			if (PREMIUM_ENABLE_SPECIAL)
			{
				if (td > 2)
				{
					td = 0;
				}
				MESSAGE += TRS[td] + "<button value=\"综合状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_special 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
				td += 2;
			}
			if (PREMIUM_ENABLE_RESIST)
			{
				if (td > 2)
				{
					td = 0;
				}
				MESSAGE += TRS[td] + "<br><button value=\"抵抗属性\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_resists 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
				td += 2;
			}
			if (PREMIUM_ENABLE_SONGS)
			{
				if (td > 2)
				{
					td = 0;
				}
				MESSAGE += TRS[td] + "<br><button value=\"诗歌状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_songs 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
				td += 2;
			}
			if (PREMIUM_ENABLE_DANCES)
			{
				if (td > 2)
				{
					td = 0;
				}
				MESSAGE += TRS[td] + "<br><button value=\"舞蹈状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_dances 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
				td += 2;
			}
			if (PREMIUM_ENABLE_CHANTS)
			{
				if (td > 2)
				{
					td = 0;
				}
				MESSAGE += TRS[td] + "<br><button value=\"战狂状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_chants 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
				td += 2;
			}
			// if (PREMIUM_ENABLE_BUFFS)
			// {
			// if (td > 2)
			// {
			// td = 0;
			// }
			// MESSAGE += TRS[td] + "<button value=\"天使状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_kamael 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
			// td += 2;
			// }
			if (PREMIUM_ENABLE_OTHERS)
			{
				if (td > 2)
				{
					td = 0;
				}
				MESSAGE += TRS[td] + "<br><button value=\"矮人状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_others 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
				td += 2;
			}
		}
		
		// if (PREMIUM_ENABLE_CUBIC)
		// {
		// if (td > 2)
		// {
		// td = 0;
		// }
		// MESSAGE += TRS[td] + "<button value=\"晶体召唤\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect view_cubic 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
		// td += 2;
		// }
		
		if (MESSAGE.length() > 0)
		{
			MAIN_HTML_MESSAGE += "<BR1><table width=100% border=0 cellspacing=0 cellpadding=1 bgcolor=444444><tr>" + "<td><font color=00FFFF>状态:</font></td><td align=right></td></tr></table>" + "<BR1><table cellspacing=0 cellpadding=0>" + MESSAGE + "</table>";
			MESSAGE = "";
			td = 0;
		}
		
		if (PREMIUM_ENABLE_BUFF_SET)
		{
			if (td > 2)
			{
				td = 0;
			}
			MESSAGE += TRS[td] + "<button value=\"" + bottonA + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " castBuffSet 0 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
			td += 2;
		}
		
		if (PREMIUM_ENABLE_HEAL)
		{
			if (td > 2)
			{
				td = 0;
			}
			MESSAGE += TRS[td] + "<button value=\"" + bottonB + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " heal 0 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
			td += 2;
		}
		
		if (PREMIUM_ENABLE_BUFF_REMOVE)
		{
			if (td > 2)
			{
				td = 0;
			}
			MESSAGE += TRS[td] + "<br><button value=\"" + bottonC + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " removeBuffs 0 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
			td += 2;
		}
		
		if (MESSAGE.length() > 0)
		{
			MAIN_HTML_MESSAGE += "<BR1><table width=100% border=0 cellspacing=0 cellpadding=1 bgcolor=444444><tr>" + "<td><font color=00FFFF>预设:</font></td><td align=right><font color=LEVEL>" + "</font></td></tr></table>" + "<BR1><table cellspacing=0 cellpadding=0>" + MESSAGE + "</table>";
			MESSAGE = "";
			td = 0;
		}
		
		if (PREMIUM_ENABLE_SCHEME_SYSTEM)
		{
			MAIN_HTML_MESSAGE += generateScheme(st);
		}
		
		if (st.getPlayer().isGM())
		{
			MAIN_HTML_MESSAGE += "<br><button value=\"管理辅助状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect manage_buffs 0 0\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		MAIN_HTML_MESSAGE += "<br1><font color=303030>" + PREMIUM_TITLE_NAME + "</font>" + "</center></body></html>";
		return MAIN_HTML_MESSAGE;
	}
	
	private String generateScheme(QuestState st)
	{
		List<String> schemeName = new ArrayList<>();
		List<String> schemeId = new ArrayList<>();
		String HTML = "";
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement rss = con.prepareStatement("SELECT * FROM premium_scheme_list WHERE player_id=?");
			rss.setInt(1, st.getPlayer().getObjectId());
			ResultSet action = rss.executeQuery();
			while (action.next())
			{
				schemeName.add(action.getString("scheme_name"));
				schemeId.add(action.getString("id"));
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		
		HTML += "<BR><table cellspacing=0 cellpadding=0>";
		
		if (schemeName.size() > 0)
		{
			String MESSAGE = "";
			int td = 0;
			String[] TRS =
			{
				"<tr><td>",
				"</td>",
				"<td>",
				"</td></tr>"
			};
			for (int i = 0; i < schemeName.size(); ++i)
			{
				if (td > 2)
				{
					td = 0;
				}
				MESSAGE += TRS[td] + "<button value=\"" + schemeName.get(i) + "" + Rnd.get(0, 10000) + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " cast " + schemeId.get(i) + " x x\" width=130 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + TRS[td + 1];
				td += 2;
			}
			
			if (MESSAGE.length() > 0)
			{
				HTML += "<table>" + MESSAGE + "</table>";
			}
		}
		
		if (schemeName.size() < PREMIUM_SCHEMES_PER_PLAYER)
		{
			HTML += "<BR1><table><tr><td><button value=\"建立自动模式\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " create_1 x x x\" width=90 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\"></td>";
		}
		else
		{
			HTML += "<BR1><table width=100><tr>";
		}
		
		if (schemeName.size() > 0)
		{
			HTML += "<td><button value=\"编辑模式\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_1 x x x\" width=90 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\"></td>" + "<td><button value=\"删除模式\" action=\"bypass -h Quest " + QUEST_LOADING_INFO
				+ " delete_1 x x x\" width=90 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\"></td></tr></table>";
		}
		else
		{
			HTML += "</tr></table>";
		}
		return HTML;
	}
	
	private String reloadPanel(QuestState st)
	{
		return "<html><body scroll=\"no\"><head><title>" + PREMIUM_TITLE_NAME + "</title></head><table border=0 cellpadding=0 cellspacing=0 width=292 height=358 background=\"L2UI_CH3.refinewnd_back_Pattern\"><body><center><tr><td><center><br><br><br><br><br><br><br><br>"
			+ "<table width=260 border=0 bgcolor=444444>" 
			+ "<tr><td><br></td></tr>" + "<tr><td align=center><font color=00FF00>这个选项只有</font><font color=LEVEL>「GM」</font><font color=\"00FF00\">能看到与使用<br1>这个能更新您在这脚本中变动到的部份<br1>您可以在这脚本的设定功能内将这个选项关掉</font><br><font color=FF3333>您要更新这个脚本的设定吗？</font></td></tr>" + "<tr><td></td></tr></table><br>"
			+ "<br><br>" + "<button value=\"更新\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " reloadscript 1 0 0\" width=74 height=21 back=\"l2ui_ch3.Btn1_normalOn\" fore=\"l2ui_ch3.Btn1_normal\">" + "<button value=\"取消\" action=\"bypass -h Quest "
			+ QUEST_LOADING_INFO + " reloadscript 0 0 0\" width=74 height=21 back=\"l2ui_ch3.Btn1_normalOn\" fore=\"l2ui_ch3.Btn1_normal\">" + "</center></body></html>";	
	}
	
	private String getItemNameHtml(QuestState st, int itemval)
	{
		return "&#" + itemval + ";";
	}
	
	@SuppressWarnings("unused")
	private String getItemName(QuestState st, int itemval)
	{
		ItemTemplate t = ItemTable.getInstance().getTemplate(itemval);
		return t != null ? t.getName() : "No Name";
	}
	
	private int getBuffCount(String scheme)
	{
		int count = 0;
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement rss = con.prepareStatement("SELECT buff_class FROM premium_scheme_contents WHERE scheme_id=?");
			rss.setString(1, scheme);
			ResultSet action = rss.executeQuery();
			while (action.next())
			{
				++count;
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		return count;
	}
	
	private String getBuffType(int id)
	{
		String val = "none";
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement act = con.prepareStatement("SELECT buffType FROM premium_buff_list WHERE buffId=? LIMIT 1");
			act.setInt(1, id);
			ResultSet rs = act.executeQuery();
			if (rs.next())
			{
				val = rs.getString("buffType");
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		return val;
	}
	
	private boolean isEnabled(int id, int level)
	{
		boolean val = false;
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement act = con.prepareStatement("SELECT canUse FROM premium_buff_list WHERE buffId=? AND buffLevel=? LIMIT 1");
			act.setInt(1, id);
			act.setInt(2, level);
			ResultSet rs = act.executeQuery();
			if (rs.next())
			{
				if ("1".equals(rs.getString("canUse")))
				{
					val = true;
				}
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		return val;
	}
	
	private boolean isUsed(String scheme, int id, int level)
	{
		boolean used = false;
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement rss = con.prepareStatement("SELECT id FROM premium_scheme_contents WHERE scheme_id=? AND skill_id=? AND skill_level=? LIMIT 1");
			rss.setString(1, scheme);
			rss.setInt(2, id);
			rss.setInt(3, level);
			ResultSet action = rss.executeQuery();
			if (action.next())
			{
				used = true;
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		return used;
	}
	
	private int getClassBuff(String id)
	{
		int val = 0;
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement getTipo = con.prepareStatement("SELECT buff_class FROM premium_buff_list WHERE buffId=?");
			getTipo.setString(1, id);
			ResultSet gt = getTipo.executeQuery();
			if (gt.next())
			{
				val = gt.getInt("buff_class");
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		return val;
	}
	
	private String showText(QuestState st, String type, String text, boolean buttonEnabled, String buttonName, String location)
	{
		String MESSAGE = "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>";
		MESSAGE += "<font color=LEVEL>" + type + "</font><br>" + text + "<br>";
		if (buttonEnabled)
		{
			MESSAGE += "<button value=\"" + buttonName + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect " + location + " 0 0\" width=100 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		MESSAGE += "<font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
		return MESSAGE;
	}
	
	private String reloadConfig(QuestState st)
	{
		try
		{
			if (QuestManager.getInstance().reload(QUEST_LOADING_INFO))
			{
				st.getPlayer().sendMessage("「豪华状态师」脚本设置已经重新安装.");
			}
			else
			{
				st.getPlayer().sendMessage("脚本错误!您编辑错误某个部份,请修复它然后重新启动服务端.");
			}
		}
		catch (Exception e)
		{
			st.getPlayer().sendMessage("脚本错误!您编辑错误某个部份,请修复它然后重新启动服务端.");
			print(e);
		}
		return rebuildMainHtml(st);
	}
	
	private PremiumNpcBuffer()
	{
		super();
		addStartNpc(NPC_ID);
		addFirstTalkId(NPC_ID);
		addTalkId(NPC_ID);
	}
	
	private boolean isPetBuff(QuestState st)
	{
		return st.getInt("Pet-On-Off") != 0;
	}
	
	private String createScheme()
	{
		return "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>「创建自动辅助」<br><br>模式名称: <edit var=\"name\" width=100><br><br>" + "<button value=\"创建\" action=\"bypass -h Quest " + QUEST_LOADING_INFO
			+ " create $name no_name x x\" width=150 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<br><button value=\"返回\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect main 0 0\" width=100 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">"
			+ "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
	}
	
	private String deleteScheme(Player player)
	{
		String HTML = "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>下列中，要删除哪个可用模式<br><br>";
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement rss = con.prepareStatement("SELECT * FROM premium_scheme_list WHERE player_id=?");
			rss.setInt(1, player.getObjectId());
			ResultSet action = rss.executeQuery();
			while (action.next())
			{
				HTML += "<button value=\"" + action.getString("scheme_name") + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " delete_c " + action.getString("id") + " " + action.getString("scheme_name") + " x\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		HTML += "<br><button value=\"上一页\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect main 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
		return HTML;
	}
	
	private String editScheme(Player player)
	{
		String HTML = "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=206 height=32><br>下列中,要编辑哪个「模式」呢？<br><br>";
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement rss = con.prepareStatement("SELECT * FROM premium_scheme_list WHERE player_id=?");
			rss.setInt(1, player.getObjectId());
			ResultSet action = rss.executeQuery();
			while (action.next())
			{
				String name = action.getString("scheme_name");
				String id = action.getString("id");
				HTML += "<button value=\"" + name + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " manage_scheme_select " + id + " x x\" width=200 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		HTML += "<br><button value=\"上一页\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect main 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
		return HTML;
	}
	
	private String getOptionList(String scheme)
	{
		int bcount = getBuffCount(scheme);
		String HTML = "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>目前自订状态<font color=LEVEL>" + bcount + "</font>!<br><br>";
		if (bcount < (PREMIUM_MAX_SCHEME_BUFFS + PREMIUM_MAX_SCHEME_DANCES))
		{
			HTML += "<button value=\"增加状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " manage_scheme_1 " + scheme + " 1 x\" width=200 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (bcount > 0)
		{
			HTML += "<button value=\"移除状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " manage_scheme_2 " + scheme + " 1 x\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		HTML += "<br><button value=\"返回\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_1 0 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<button value=\"首页\" action=\"bypass -h Quest " + QUEST_LOADING_INFO
			+ " redirect main 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
		return HTML;
	}
	
	private String buildHtml(String buffType)
	{
		String HTML_MESSAGE = "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><br>";
		
		List<String> availableBuffs = new ArrayList<>();
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement getList = con.prepareStatement("SELECT buffId,buffLevel FROM premium_buff_list WHERE buffType=\"" + buffType + "\" AND canUse=1  ORDER BY Buff_Class ASC, id");
			ResultSet rs = getList.executeQuery();
			while (rs.next())
			{
				// try :
				int bId = rs.getInt("buffId");
				int bLevel = rs.getInt("buffLevel");
				String bName = SkillData.getInstance().getSkill(bId, bLevel).getName();
				bName = bName.replace(" ", "+");
				availableBuffs.add(bName + "_" + bId + "_" + bLevel);
				// except: HTML_MESSAGE += "Error loading buff list...<br>"
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		
		if (availableBuffs.size() == 0)
		{
			HTML_MESSAGE += "此选项目前暂不开放哦!";
		}
		else
		{
			if (PREMIUM_FREE_BUFFS)
			{
				HTML_MESSAGE += "所有状态将 <font color=LEVEL>收取少量服务费</font>!";
			}
			else
			{
				int price = 0;
				switch (buffType)
				{
					case "buff":
						price = PREMIUM_BUFF_PRICE;
						break;
					case "kamael":
						price = PREMIUM_BUFF_PRICE;
						break;
					case "resist":
						price = PREMIUM_RESIST_PRICE;
						break;
					case "song":
						price = PREMIUM_SONG_PRICE;
						break;
					case "dance":
						price = PREMIUM_DANCE_PRICE;
						break;
					case "chant":
						price = PREMIUM_CHANT_PRICE;
						break;
					case "others":
						price = PREMIUM_OTHERS_PRICE;
						break;
					case "special":
						price = PREMIUM_SPECIAL_PRICE;
						break;
					case "cubic":
						price = PREMIUM_CUBIC_PRICE;
						break;
					default:
						if (DEBUG)
						{
							throw new RuntimeException();
						}
				}
				HTML_MESSAGE += "以下每个状态单加需要 <font color=LEVEL>1000</font> 金币!";
			}
			HTML_MESSAGE += "<BR1><table>";
			for (String buff : availableBuffs)
			{
				buff = buff.replace("_", " ");
				String[] buffSplit = buff.split(" ");
				String name = buffSplit[0];
				int id = Integer.parseInt(buffSplit[1]);
				int level = Integer.parseInt(buffSplit[2]);
				name = name.replace("+", " ");
				HTML_MESSAGE += "<tr><td>" + getSkillIconHtml(id, level) + "</td><td><button value=\"" + name + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " giveBuffs " + id + " " + level + " " + buffType
					+ "\" width=190 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
			}
			HTML_MESSAGE += "</table>";
		}
		
		HTML_MESSAGE += "<br><button value=\"返回\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect main 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
		return HTML_MESSAGE;
	}
	
	private String generateQuery(int case1, int case2)
	{
		StringBuilder qry = new StringBuilder();
		if (PREMIUM_ENABLE_BUFFS)
		{
			if (case1 < PREMIUM_MAX_SCHEME_BUFFS)
			{
				qry.append(",\"buff\"");
			}
		}
		if (PREMIUM_ENABLE_BUFFS)
		{
			if (case1 < PREMIUM_MAX_SCHEME_BUFFS)
			{
				qry.append(",\"kamael\"");
			}
		}
		if (PREMIUM_ENABLE_RESIST)
		{
			if (case1 < PREMIUM_MAX_SCHEME_BUFFS)
			{
				qry.append(",\"resist\"");
			}
		}
		if (PREMIUM_ENABLE_SONGS)
		{
			if (case2 < PREMIUM_MAX_SCHEME_DANCES)
			{
				qry.append(",\"song\"");
			}
		}
		if (PREMIUM_ENABLE_DANCES)
		{
			if (case2 < PREMIUM_MAX_SCHEME_DANCES)
			{
				qry.append(",\"dance\"");
			}
		}
		if (PREMIUM_ENABLE_CHANTS)
		{
			if (case1 < PREMIUM_MAX_SCHEME_BUFFS)
			{
				qry.append(",\"chant\"");
			}
		}
		if (PREMIUM_ENABLE_OTHERS)
		{
			if (case1 < PREMIUM_MAX_SCHEME_BUFFS)
			{
				qry.append(",\"others\"");
			}
		}
		if (PREMIUM_ENABLE_SPECIAL)
		{
			if (case1 < PREMIUM_MAX_SCHEME_BUFFS)
			{
				qry.append(",\"special\"");
			}
		}
		if (qry.length() > 0)
		{
			qry.deleteCharAt(0);
		}
		return qry.toString();
	}
	
	private String viewAllSchemeBuffs$getBuffCount(String scheme)
	{
		int count = 0;
		int D_S_Count = 0;
		int B_Count = 0;
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement rss = con.prepareStatement("SELECT buff_class FROM premium_scheme_contents WHERE scheme_id=?");
			rss.setString(1, scheme);
			ResultSet action = rss.executeQuery();
			while (action.next())
			{
				++count;
				int val = action.getInt("buff_class");
				if ((val == 1) || (val == 2))
				{
					++D_S_Count;
				}
				else
				{
					++B_Count;
				}
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		String res = count + " " + B_Count + " " + D_S_Count;
		return res;
	}
	
	private String viewAllSchemeBuffs(String scheme, String page, String action)
	{
		List<String> buffList = new ArrayList<>();
		String HTML_MESSAGE = "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><br>";
		String[] eventSplit = viewAllSchemeBuffs$getBuffCount(scheme).split(" ");
		int TOTAL_BUFF = Integer.parseInt(eventSplit[0]);
		int BUFF_COUNT = Integer.parseInt(eventSplit[1]);
		int DANCE_SONG = Integer.parseInt(eventSplit[2]);
		try (Connection con = DatabaseFactory.getConnection())
		{
			if (action.equals("add"))
			{
				HTML_MESSAGE += "此模式还可以添加 <font color=LEVEL>" + (PREMIUM_MAX_SCHEME_BUFFS - BUFF_COUNT) + "</font> 个状态和 <font color=LEVEL>" + (PREMIUM_MAX_SCHEME_DANCES - DANCE_SONG) + "</font> 个歌舞效果!";
				String QUERY = "SELECT * FROM premium_buff_list WHERE buffType IN (" + generateQuery(BUFF_COUNT, DANCE_SONG) + ") AND canUse=1 ORDER BY Buff_Class ASC, id";
				PreparedStatement getBuffCount = con.prepareStatement(QUERY);
				ResultSet rss = getBuffCount.executeQuery();
				while (rss.next())
				{
					String name = SkillData.getInstance().getSkill(rss.getInt("buffId"), rss.getInt("buffLevel")).getName();
					name = name.replace(" ", "+");
					buffList.add(name + "_" + rss.getInt("buffId") + "_" + rss.getInt("buffLevel"));
				}
			}
			else if (action.equals("remove"))
			{
				HTML_MESSAGE += "此模式已拥有 <font color=LEVEL>" + BUFF_COUNT + "</font> 个状态和 <font color=LEVEL>" + DANCE_SONG + "</font> 个歌舞效果";
				String QUERY = "SELECT * FROM premium_scheme_contents WHERE scheme_id=? ORDER BY Buff_Class ASC, id";
				PreparedStatement getBuffCount = con.prepareStatement(QUERY);
				getBuffCount.setString(1, scheme);
				ResultSet rss = getBuffCount.executeQuery();
				while (rss.next())
				{
					String name = SkillData.getInstance().getSkill(rss.getInt("skill_id"), rss.getInt("skill_level")).getName();
					name = name.replace(" ", "+");
					buffList.add(name + "_" + rss.getInt("skill_id") + "_" + rss.getInt("skill_level"));
				}
			}
			else if (DEBUG)
			{
				throw new RuntimeException();
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		
		HTML_MESSAGE += "<BR1><table border=0><tr>";
		final int buffsPerPage = 20;
		final String width, pageName;
		int pc = ((buffList.size() - 1) / buffsPerPage) + 1;
		if (pc > 5)
		{
			width = "25";
			pageName = "P";
		}
		else
		{
			width = "50";
			pageName = "页面 ";
		}
		for (int ii = 1; ii <= pc; ++ii)
		{
			if (ii == Integer.parseInt(page))
			{
				HTML_MESSAGE += "<td width=" + width + " align=center><font color=LEVEL>" + pageName + ii + "</font></td>";
			}
			else if (action.equals("add"))
			{
				HTML_MESSAGE += "<td width=" + width + ">" + "<button value=\"" + pageName + ii + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " manage_scheme_1 " + scheme + " " + ii + " x\" width=" + width + " height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			}
			else if (action.equals("remove"))
			{
				HTML_MESSAGE += "<td width=" + width + ">" + "<button value=\"" + pageName + ii + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " manage_scheme_2 " + scheme + " " + ii + " x\" width=" + width + " height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			}
			else if (DEBUG)
			{
				throw new RuntimeException();
			}
		}
		HTML_MESSAGE += "</tr></table>";
		
		int limit = buffsPerPage * Integer.parseInt(page);
		int start = limit - buffsPerPage;
		int end = Math.min(limit, buffList.size());
		int k = 0;
		for (int i = start; i < end; ++i)
		{
			String value = buffList.get(i);
			value = value.replace("_", " ");
			String[] extr = value.split(" ");
			String name = extr[0];
			name = name.replace("+", " ");
			int id = Integer.parseInt(extr[1]);
			int level = Integer.parseInt(extr[2]);
			/*--String page = extr[3];--*/
			if (action.equals("add"))
			{
				if (!isUsed(scheme, id, level))
				{
					if ((k % 2) != 0)
					{
						HTML_MESSAGE += "<BR1><table border=0 bgcolor=333333>";
					}
					else
					{
						HTML_MESSAGE += "<BR1><table border=0 bgcolor=292929>";
					}
					HTML_MESSAGE += "<tr><td width=35>" + getSkillIconHtml(id, level) + "</td><td fixwidth=170>" + name + "</td><td><button value=\"增加\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " add_buff " + scheme + "_" + id + "_" + level + " " + page + " " + TOTAL_BUFF
						+ "\" width=65 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "</tr></table>";
					k += 1;
				}
			}
			else if (action.equals("remove"))
			{
				if ((k % 2) != 0)
				{
					HTML_MESSAGE += "<BR1><table border=0 bgcolor=333333>";
				}
				else
				{
					HTML_MESSAGE += "<BR1><table border=0 bgcolor=292929>";
				}
				HTML_MESSAGE += "<tr><td width=35>" + getSkillIconHtml(id, level) + "</td><td fixwidth=170>" + name + "</td><td><button value=\"删除\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " remove_buff " + scheme + "_" + id + "_" + level + " " + page + " " + TOTAL_BUFF
					+ "\" width=65 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "</table>";
				k += 1;
			}
		}
		HTML_MESSAGE += "<br><br><button value=\"返回\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " manage_scheme_select " + scheme + " x x\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<button value=\"首页\" action=\"bypass -h Quest " + QUEST_LOADING_INFO
			+ " redirect main 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
		return HTML_MESSAGE;
	} // viewAllSchemeBuffs
	
	private String viewAllBuffTypes()
	{
		String HTML_MESSAGE = "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>";
		HTML_MESSAGE += "<font color=LEVEL>[编辑状态]</font><br>";
		if (PREMIUM_ENABLE_BUFFS)
		{
			HTML_MESSAGE += "<button value=\"一般状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list buff 一般状态 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_BUFFS)
		{
			HTML_MESSAGE += "<button value=\"天使状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list kamael 天使状态 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_RESIST)
		{
			HTML_MESSAGE += "<button value=\"抵抗属性\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list resist 抵抗属性 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_SONGS)
		{
			HTML_MESSAGE += "<button value=\"诗歌状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list song 诗歌状态 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_DANCES)
		{
			HTML_MESSAGE += "<button value=\"舞蹈状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list dance 舞蹈状态 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_CHANTS)
		{
			HTML_MESSAGE += "<button value=\"战狂状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list chant 战狂状态 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_SPECIAL)
		{
			HTML_MESSAGE += "<button value=\"综合状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list special 综合状态 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_OTHERS)
		{
			HTML_MESSAGE += "<button value=\"矮人状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list others 矮人状态 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_CUBIC)
		{
			HTML_MESSAGE += "<button value=\"晶体招唤\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list cubic 晶体招唤 1\" width=200 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"L2UI_ct1.ListCTRL_DF_Title\">";
		}
		if (PREMIUM_ENABLE_BUFF_SET)
		{
			HTML_MESSAGE += "<button value=\"职业状态\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list set 职业状态 1\" width=200 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>";
		}
		HTML_MESSAGE += "<button value=\"返回\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect main 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
		return HTML_MESSAGE;
	}
	
	private String viewAllBuffs(String type, String typeName, String page)
	{
		List<String> buffList = new ArrayList<>();
		String HTML_MESSAGE = "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>";
		typeName = typeName.replace("_", " ");
		try (Connection con = DatabaseFactory.getConnection())
		{
			final PreparedStatement getBuffCount;
			if (type.equals("set"))
			{
				getBuffCount = con.prepareStatement("SELECT * FROM premium_buff_list WHERE buffType IN (" + generateQuery(0, 0) + ") AND canUse=1");
			}
			else
			{
				getBuffCount = con.prepareStatement("SELECT * FROM premium_buff_list WHERE buffType=?");
				getBuffCount.setString(1, type);
			}
			ResultSet rss = getBuffCount.executeQuery();
			while (rss.next())
			{
				String name = SkillData.getInstance().getSkill(rss.getInt("buffId"), rss.getInt("buffLevel")).getName();
				name = name.replace(" ", "+");
				String usable = rss.getString("canUse");
				String forClass = rss.getString("forClass");
				String skill_id = rss.getString("buffId");
				String skill_level = rss.getString("buffLevel");
				buffList.add(name + "_" + forClass + "_" + page + "_" + usable + "_" + skill_id + "_" + skill_level);
			}
		}
		catch (SQLException e)
		{
			print(e);
		}
		Collections.sort(buffList);
		
		HTML_MESSAGE += "<font color=LEVEL>[状态管理 - " + typeName + " - 第 " + page + " 页]</font><br><table border=0><tr>";
		final int buffsPerPage;
		if (type.equals("set"))
		{
			buffsPerPage = 12;
		}
		else
		{
			buffsPerPage = 20;
		}
		final String width, pageName;
		int pc = ((buffList.size() - 1) / buffsPerPage) + 1;
		if (pc > 5)
		{
			width = "25";
			pageName = "P";
		}
		else
		{
			width = "50";
			pageName = "页面 ";
		}
		typeName = typeName.replace(" ", "_");
		for (int ii = 1; ii <= pc; ++ii)
		{
			if (ii == Integer.parseInt(page))
			{
				HTML_MESSAGE += "<td width=" + width + " align=center><font color=LEVEL>" + pageName + ii + "</font></td>";
			}
			else
			{
				HTML_MESSAGE += "<td width=" + width + "><button value=\"" + pageName + "" + ii + "\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " edit_premium_buff_list " + type + " " + typeName + " " + ii + "\" width=" + width
					+ " height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			}
		}
		HTML_MESSAGE += "</tr></table><br>";
		
		int limit = buffsPerPage * Integer.parseInt(page);
		int start = limit - buffsPerPage;
		int end = Math.min(limit, buffList.size());
		for (int i = start; i < end; ++i)
		{
			String value = buffList.get(i);
			value = value.replace("_", " ");
			String[] extr = value.split(" ");
			String name = extr[0];
			name = name.replace("+", " ");
			int forClass = Integer.parseInt(extr[1]);
			/* page = extr[2]; */
			int usable = Integer.parseInt(extr[3]);
			String skillPos = extr[4] + "_" + extr[5];
			if ((i % 2) != 0)
			{
				HTML_MESSAGE += "<BR1><table border=0 bgcolor=333333>";
			}
			else
			{
				HTML_MESSAGE += "<BR1><table border=0 bgcolor=292929>";
			}
			if (type.equals("set"))
			{
				String listOrder = null;
				if (forClass == 0)
				{
					listOrder = "List=\"" + PREMIUM_SET_FIGHTER + ";" + PREMIUM_SET_MAGE + ";" + PREMIUM_SET_ALL + ";" + PREMIUM_SET_NONE + ";\"";
				}
				else if (forClass == 1)
				{
					listOrder = "List=\"" + PREMIUM_SET_MAGE + ";" + PREMIUM_SET_FIGHTER + ";" + PREMIUM_SET_ALL + ";" + PREMIUM_SET_NONE + ";\"";
				}
				else if (forClass == 2)
				{
					listOrder = "List=\"" + PREMIUM_SET_ALL + ";" + PREMIUM_SET_FIGHTER + ";" + PREMIUM_SET_MAGE + ";" + PREMIUM_SET_NONE + ";\"";
				}
				else if (forClass == 3)
				{
					listOrder = "List=\"" + PREMIUM_SET_NONE + ";" + PREMIUM_SET_FIGHTER + ";" + PREMIUM_SET_MAGE + ";" + PREMIUM_SET_ALL + ";\"";
				}
				HTML_MESSAGE += "<tr><td fixwidth=145>" + name + "</td><td width=70><combobox var=\"newSet" + i + "\" width=70 " + listOrder + "></td>" + "<td width=50><button value=\"设置\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " changeBuffSet " + skillPos + " $newSet" + i + " " + page
					+ "\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
			}
			else
			{
				HTML_MESSAGE += "<tr><td fixwidth=170>" + name + "</td><td width=80>";
				if (usable == 1)
				{
					HTML_MESSAGE += "<button value=\"关闭\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " editSelectedBuff " + skillPos + " 0-" + page + " " + type + "\" width=80 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
				}
				else if (usable == 0)
				{
					HTML_MESSAGE += "<button value=\"打开\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " editSelectedBuff " + skillPos + " 1-" + page + " " + type + "\" width=80 height=23 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
				}
			}
			HTML_MESSAGE += "</table>";
		}
		HTML_MESSAGE += "<br><br><button value=\"返回\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " redirect manage_buffs 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<button value=\"主页\" action=\"bypass -h Quest " + QUEST_LOADING_INFO
			+ " redirect main 0 0\" width=100 height=20 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
		return HTML_MESSAGE;
	}
	
	private void manageSelectedBuff(String buffPosId, String canUseBuff)
	{
		String[] bpid = buffPosId.split("_");
		String bId = bpid[0];
		String bLvl = bpid[1];
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement upd = con.prepareStatement("UPDATE premium_buff_list SET canUse=? WHERE buffId=? AND buffLevel=? LIMIT 1");
			upd.setString(1, canUseBuff);
			upd.setString(2, bId);
			upd.setString(3, bLvl);
			upd.executeUpdate();
			upd.close();
		}
		catch (SQLException e)
		{
			print(e);
		}
	}
	
	private String manageSelectedSet(String id, String newVal, String opt3)
	{
		String[] bpid = id.split("_");
		String bId = bpid[0];
		String bLvl = bpid[1];
		try (Connection con = DatabaseFactory.getConnection())
		{
			PreparedStatement upd = con.prepareStatement("UPDATE premium_buff_list SET forClass=? WHERE buffId=? AND bufflevel=?");
			upd.setString(1, newVal);
			upd.setString(2, bId);
			upd.setString(3, bLvl);
			upd.executeUpdate();
			upd.close();
		}
		catch (SQLException e)
		{
			print(e);
		}
		return viewAllBuffs("set", "Buff Sets", opt3);
	}
	
	private void addTimeout(QuestState st, Player player, int amount, int offset)
	{
		int endtime = (int) ((System.currentTimeMillis() + (amount * 1000)) / 1000);
		st.set("blockUntilTime", String.valueOf(endtime));
		st.getPlayer().sendPacket(new SetupGauge(player.getObjectId(), 3 , (amount * 1000) + offset));
	}
	
	private void heal(Player player, boolean isPet)
	{
		Summon target;
		if (!isPet)
		{
			PlayerStatus PlayerStatus = player.getStatus();
			PlayerStat PlayerStat = player.getStat();
			PlayerStatus.setCurrentHp(PlayerStat.getMaxHp());
			PlayerStatus.setCurrentMp(PlayerStat.getMaxMp());
			PlayerStatus.setCurrentCp(PlayerStat.getMaxCp());
		}
		else if (/* isPet && */(target = player.getSummon()) != null)
		{
			SummonStatus petStatus = target.getStatus();
			SummonStat petStat = target.getStat();
			petStatus.setCurrentHp(petStat.getMaxHp());
			petStatus.setCurrentMp(petStat.getMaxMp());
			if (target instanceof Pet)
			{
				Pet pet = (Pet) target;
				pet.setCurrentFed(pet.getMaxFed());
				player.sendPacket(new SetSummonRemainTime(pet.getMaxFed(), pet.getCurrentFed()));
			}
			else if (target instanceof Servitor)
			{
				Servitor summon = (Servitor) target;
				summon.setLifeTimeRemaining(summon.getLifeTimeRemaining() - summon.getLifeTime());
				player.sendPacket(new SetSummonRemainTime(summon.getLifeTime(), summon.getLifeTimeRemaining()));
			}
			else if (DEBUG)
			{
				throw new RuntimeException();
			}
		}
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		if (DEBUG)
		{
			System.out.println(getName() + "#onAdvEvent('" + event + "'," + (npc == null ? "NULL" : npc.getId() + npc.getName()) + "," + (player == null ? "NULL" : player.getName()) + ")");
		}
		QuestState st = player.getQuestState(QUEST_LOADING_INFO);
		String[] eventSplit = event.split(" ", 4);
		if (eventSplit.length != 4)
		{
			player.sendMessage("名字出错,重新输入名称");
			return null;
		}
		String eventParam0 = eventSplit[0];
		String eventParam1 = eventSplit[1];
		String eventParam2 = eventSplit[2];
		String eventParam3 = eventSplit[3];
		
		switch (eventParam0)
		{
			case "reloadscript":
				if (eventParam1.equals("1"))
				{
					return reloadConfig(st);
				}
				if (eventParam1.equals("0"))
				{
					return rebuildMainHtml(st);
				}
				if (DEBUG)
				{
					throw new RuntimeException();
				}
				
			case "redirect":
				switch (eventParam1)
				{
					case "main":
						return rebuildMainHtml(st);
					case "manage_buffs":
						return viewAllBuffTypes();
						/**/
					case "view_buffs":
						return buildHtml("buff");
					case "view_kamael":
						return buildHtml("kamael");
					case "view_resists":
						return buildHtml("resist");
					case "view_songs":
						return buildHtml("song");
					case "view_dances":
						return buildHtml("dance");
					case "view_chants":
						return buildHtml("chant");
					case "view_others":
						return buildHtml("others");
					case "view_special":
						return buildHtml("special");
					case "view_cubic":
						return buildHtml("cubic");
					default:
						
						if (DEBUG)
						{
							throw new RuntimeException();
						}
				}
				/* break; */
				
			case "buffpet":
				if ((int) (System.currentTimeMillis() / 1000) > st.getInt("blockUntilTime"))
				{
					st.set("Pet-On-Off", eventParam1);
					if (PREMIUM_TIME_OUT)
					{
						addTimeout(st, player, PREMIUM_TIME_OUT_TIME / 2, 600);
					}
				}
				return rebuildMainHtml(st);
				
			case "create":
			{
				String param = eventParam1.replaceAll("[ !" + "\"" + "#$%&'()*+,/:;<=>?@" + "\\[" + "\\\\" + "\\]" + "\\^" + "`{|}~]", ""); // JOJO
				if ((param.length() == 0) || param.equals("no_name"))
				{
					player.sendMessage("名字出错,重新输入名称");
					return showText(st, "注意", "请输入模式名称!", true, "返回", "main");
				}
				try (Connection con = DatabaseFactory.getConnection())
				{
					PreparedStatement ins = con.prepareStatement("INSERT INTO premium_scheme_list (player_id,scheme_name) VALUES (?,?)");
					ins.setInt(1, player.getObjectId());
					ins.setString(2, param);
					ins.executeUpdate();
					ins.close();
				}
				catch (SQLException e)
				{
					print(e);
				}
				return rebuildMainHtml(st);
			}
			
			case "delete":
				try (Connection con = DatabaseFactory.getConnection())
				{
					PreparedStatement rem;
					rem = con.prepareStatement("DELETE FROM premium_scheme_list WHERE id=? LIMIT 1");
					rem.setString(1, eventParam1);
					rem.executeUpdate();
					rem.close();
					rem = con.prepareStatement("DELETE FROM premium_scheme_contents WHERE scheme_id=?");
					rem.setString(1, eventParam1);
					rem.executeUpdate();
					rem.close();
				}
				catch (SQLException e)
				{
					print(e);
				}
				return rebuildMainHtml(st);
				
			case "delete_c":
				return "<html><head><title>" + PREMIUM_TITLE_NAME + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>你确定要删除 '" + eventParam2 + "' 快捷模式吗?<br><br>" + "<button value=\"是的,我要删除\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " delete "
					+ eventParam1 + " x x\" width=100 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">" + "<button value=\"我在想想吧\" action=\"bypass -h Quest " + QUEST_LOADING_INFO + " delete_1 x x x\" width=100 height=23 back=\"L2UI_ct1.ListCTRL_DF_Title\" fore=\"listctrl_df_title\">"
					+ "<br><font color=303030>" + PREMIUM_TITLE_NAME + "</font></center></body></html>";
				
			case "create_1":
				return createScheme();
			case "edit_1":
				return editScheme(player);
			case "delete_1":
				return deleteScheme(player);
			case "manage_scheme_1":
				return viewAllSchemeBuffs(eventParam1, eventParam2, "add");
			case "manage_scheme_2":
				return viewAllSchemeBuffs(eventParam1, eventParam2, "remove");
			case "manage_scheme_select":
				return getOptionList(eventParam1);
				
			case "remove_buff":
			{
				String[] split = eventParam1.split("_");
				String scheme = split[0];
				String skill = split[1];
				String level = split[2];
				try (Connection con = DatabaseFactory.getConnection())
				{
					PreparedStatement rem = con.prepareStatement("DELETE FROM premium_scheme_contents WHERE scheme_id=? AND skill_id=? AND skill_level=? LIMIT 1");
					rem.setString(1, scheme);
					rem.setString(2, skill);
					rem.setString(3, level);
					rem.executeUpdate();
				}
				catch (SQLException e)
				{
					print(e);
				}
				int temp = Integer.parseInt(eventParam3) - 1;
				final String HTML;
				if (temp <= 0)
				{
					HTML = getOptionList(scheme);
				}
				else
				{
					HTML = viewAllSchemeBuffs(scheme, eventParam2, "remove");
				}
				return HTML;
			}
			
			case "add_buff":
			{
				String[] split = eventParam1.split("_");
				String scheme = split[0];
				String skill = split[1];
				String level = split[2];
				int idbuffclass = getClassBuff(skill);
				try (Connection con = DatabaseFactory.getConnection())
				{
					PreparedStatement ins = con.prepareStatement("INSERT INTO premium_scheme_contents (scheme_id,skill_id,skill_level,buff_class) VALUES (?,?,?,?)");
					ins.setString(1, scheme);
					ins.setString(2, skill);
					ins.setString(3, level);
					ins.setInt(4, idbuffclass);
					ins.executeUpdate();
					ins.close();
				}
				catch (SQLException e)
				{
					print(e);
				}
				int temp = Integer.parseInt(eventParam3) + 1;
				final String HTML;
				if (temp >= (PREMIUM_MAX_SCHEME_BUFFS + PREMIUM_MAX_SCHEME_DANCES))
				{
					HTML = getOptionList(scheme);
				}
				else
				{
					HTML = viewAllSchemeBuffs(scheme, eventParam2, "add");
				}
				return HTML;
			}
			
			case "edit_premium_buff_list":
				return viewAllBuffs(eventParam1, eventParam2, eventParam3);
				
			case "changeBuffSet":
				if (eventParam2.equals(PREMIUM_SET_FIGHTER))
				{
					eventParam2 = "0";
				}
				else if (eventParam2.equals(PREMIUM_SET_MAGE))
				{
					eventParam2 = "1";
				}
				else if (eventParam2.equals(PREMIUM_SET_ALL))
				{
					eventParam2 = "2";
				}
				else if (eventParam2.equals(PREMIUM_SET_NONE))
				{
					eventParam2 = "3";
				}
				else if (DEBUG)
				{
					throw new RuntimeException();
				}
				return manageSelectedSet(eventParam1, eventParam2, eventParam3);
				
			case "editSelectedBuff":
			{
				eventParam2 = eventParam2.replace("-", " ");
				String[] split = eventParam2.split(" ");
				String action = split[0];
				String page = split[1];
				manageSelectedBuff(eventParam1, action);
				final String typeName;
				switch (eventParam3)
				{
					case "buff":
						typeName = "Buffs";
						break;
					case "kamael":
						typeName = "Kamael";
						break;
					case "resist":
						typeName = "Resists";
						break;
					case "song":
						typeName = "Songs";
						break;
					case "dance":
						typeName = "Dances";
						break;
					case "chant":
						typeName = "Chants";
						break;
					case "others":
						typeName = "Others_Buffs";
						break;
					case "special":
						typeName = "Special_Buffs";
						break;
					case "cubic":
						typeName = "Cubics";
						
						break;
					default: /* if (DEBUG) */
						throw new RuntimeException();
				}
				return viewAllBuffs(eventParam3, typeName, page);
			}
			
			case "viewSelectedConfig":
				throw new RuntimeException();
				
			case "changeConfig":
				throw new RuntimeException();
				
			case "heal":
				if ((int) (System.currentTimeMillis() / 1000) > st.getInt("blockUntilTime"))
				{
					if (player.getInventory().getInventoryItemCount(PREMIUM_CONSUMABLE_ID,0) < PREMIUM_HEAL_PRICE)
					{
						return showText(st, "对不起", "你没有足够的物品:<br>需要: <font color=LEVEL>" + PREMIUM_HEAL_PRICE + " " + getItemNameHtml(st, PREMIUM_CONSUMABLE_ID) + "!", false, "0", "0");
					}
					final boolean getSummonbuff = isPetBuff(st);
					if (getSummonbuff)
					{
						if (player.getSummon() != null)
						{
							heal(player, getSummonbuff);
						}
						else
						{
							return showText(st, "注意", "无法使用宠物状态.<br>先召唤出你的宠物!", false, "返回", "main");
						}
					}
					else
					{
						heal(player, getSummonbuff);
					}
					player.getInventory().destroyItemByItemId("传送需求", PREMIUM_CONSUMABLE_ID,PREMIUM_HEAL_PRICE, null, true);
					if (PREMIUM_TIME_OUT)
					{
						addTimeout(st, player, PREMIUM_TIME_OUT_TIME / 2, 600);
					}
				}
				return PREMIUM_SMART_WINDOW ? null : rebuildMainHtml(st);
				
			case "removeBuffs":
				if ((int) (System.currentTimeMillis() / 1000) > st.getInt("blockUntilTime"))
				{
					if (player.getInventory().getInventoryItemCount(PREMIUM_CONSUMABLE_ID,0) < PREMIUM_BUFF_REMOVE_PRICE)
					{
						return showText(st, "对不起", "你没有足够的道具:<br>您需要: <font color=LEVEL>" + PREMIUM_BUFF_REMOVE_PRICE + " " + getItemNameHtml(st, PREMIUM_CONSUMABLE_ID) + "!", false, "0", "0");
					}
					final boolean getSummonbuff = isPetBuff(st);
					if (getSummonbuff)
					{
						if (player.getSummon() != null)
						{
							player.getSummon().stopAllEffects();
						}
						else
						{
							return showText(st, "对不起", "无法使用宠物状态.<br>请先行召唤宠物!", false, "返回", "main");
						}
					}
					else
					{
						player.stopAllEffects();
						if (player.getCubics() != null)
						{
							for (Cubic cubic : player.getCubics().values())
							{
								cubic.stopAction();
								player.getCubics().remove(cubic);
							}
						}
					}
					player.destroyItemByItemId("道具", PREMIUM_CONSUMABLE_ID,PREMIUM_BUFF_REMOVE_PRICE, npc, true);
					if (PREMIUM_TIME_OUT)
					{
						addTimeout(st, player, PREMIUM_TIME_OUT_TIME / 2, 600);
					}
				}
				return PREMIUM_SMART_WINDOW ? null : rebuildMainHtml(st);
				
			case "cast":
				if ((int) (System.currentTimeMillis() / 1000) > st.getInt("blockUntilTime"))
				{
					List<Integer> buffs = new ArrayList<>();
					List<Integer> levels = new ArrayList<>();
					try (Connection con = DatabaseFactory.getConnection())
					{
						PreparedStatement rss = con.prepareStatement("SELECT * FROM premium_scheme_contents WHERE scheme_id=? ORDER BY id");
						rss.setString(1, eventParam1);
						ResultSet action = rss.executeQuery();
						while (action.next())
						{
							int id = Integer.parseInt(action.getString("skill_id"));
							int level = Integer.parseInt(action.getString("skill_level"));
							switch (getBuffType(id))
							{
								case "buff":
									if (PREMIUM_ENABLE_BUFFS)
									{
										if (isEnabled(id, level))
										{
											buffs.add(id);
											levels.add(level);
										}
									}
									break;
								case "kamael":
									if (PREMIUM_ENABLE_BUFFS)
									{
										if (isEnabled(id, level))
										{
											buffs.add(id);
											levels.add(level);
										}
									}
									break;
								case "resist":
									if (PREMIUM_ENABLE_RESIST)
									{
										if (isEnabled(id, level))
										{
											buffs.add(id);
											levels.add(level);
										}
									}
									break;
								case "song":
									if (PREMIUM_ENABLE_SONGS)
									{
										if (isEnabled(id, level))
										{
											buffs.add(id);
											levels.add(level);
										}
									}
									break;
								case "dance":
									if (PREMIUM_ENABLE_DANCES)
									{
										if (isEnabled(id, level))
										{
											buffs.add(id);
											levels.add(level);
										}
									}
									break;
								case "chant":
									if (PREMIUM_ENABLE_CHANTS)
									{
										if (isEnabled(id, level))
										{
											buffs.add(id);
											levels.add(level);
										}
									}
									break;
								case "others":
									if (PREMIUM_ENABLE_OTHERS)
									{
										if (isEnabled(id, level))
										{
											buffs.add(id);
											levels.add(level);
										}
									}
									break;
								case "special":
									if (PREMIUM_ENABLE_SPECIAL)
									{
										if (isEnabled(id, level))
										{
											buffs.add(id);
											levels.add(level);
										}
									}
									break;
								default:
									if (DEBUG)
									{
										throw new RuntimeException();
									}
							}
						}
					}
					catch (SQLException e)
					{
						print(e);
					}
					
					if (buffs.size() == 0)
					{
						return viewAllSchemeBuffs(eventParam1, "1", "add");
					}
					if (!PREMIUM_FREE_BUFFS)
					{
						if (player.getInventory().getInventoryItemCount(PREMIUM_CONSUMABLE_ID,0) < PREMIUM_SCHEME_BUFF_PRICE)
						{
							return showText(st, "对不起", "你没有足够的道具:<br>您需要: <font color=LEVEL>" + PREMIUM_SCHEME_BUFF_PRICE + " " + getItemNameHtml(st, PREMIUM_CONSUMABLE_ID) + "!", false, "0", "0");
						}
					}
					
					final boolean getSummonbuff = isPetBuff(st);
					for (int i = 0; i < buffs.size(); ++i)
					{
						if (!getSummonbuff)
						{
							SkillData.getInstance().getSkill(buffs.get(i), levels.get(i)).applyEffects(player, player);
						}
						else
						{
							if (player.getSummon() != null)
							{
								SkillData.getInstance().getSkill(buffs.get(i), levels.get(i)).applyEffects(player.getSummon(), player.getSummon());
							}
							else
							{
								return showText(st, "对不起", "无法使用宠物状态<br>请先行召唤宠物!", false, "返回", "main");
							}
						}
					}
					player.destroyItemByItemId("道具", PREMIUM_CONSUMABLE_ID,PREMIUM_SCHEME_BUFF_PRICE, npc, true);
					if (PREMIUM_TIME_OUT)
					{
						addTimeout(st, player, PREMIUM_TIME_OUT_TIME, 600);
					}
				}
				return PREMIUM_SMART_WINDOW ? null : rebuildMainHtml(st);
				
			case "giveBuffs":
			{
				final int cost;
				switch (eventParam3)
				{
					case "buff":
						cost = PREMIUM_BUFF_PRICE;
						break;
					case "kamael":
						cost = PREMIUM_BUFF_PRICE;
						break;
					case "resist":
						cost = PREMIUM_RESIST_PRICE;
						break;
					case "song":
						cost = PREMIUM_SONG_PRICE;
						break;
					case "dance":
						cost = PREMIUM_DANCE_PRICE;
						break;
					case "chant":
						cost = PREMIUM_CHANT_PRICE;
						break;
					case "others":
						cost = PREMIUM_OTHERS_PRICE;
						break;
					case "special":
						cost = PREMIUM_SPECIAL_PRICE;
						break;
					case "cubic":
						cost = PREMIUM_CUBIC_PRICE;
						break;
					default:
						throw new RuntimeException();
				}
				
				if ((int) (System.currentTimeMillis() / 1000) > st.getInt("blockUntilTime"))
				{
					if (!PREMIUM_FREE_BUFFS)
					{
						if (player.getInventory().getInventoryItemCount(PREMIUM_CONSUMABLE_ID,0) < cost)
						{
							return showText(st, "对不起", "你没有足够的道具:<br>您需要: <font color=LEVEL>" + cost + " " + getItemNameHtml(st, PREMIUM_CONSUMABLE_ID) + "!", false, "0", "0");
						}
					}
					Skill skill = SkillData.getInstance().getSkill(Integer.parseInt(eventParam1), Integer.parseInt(eventParam2));
					if (skill.hasEffectType(EffectType.SUMMON))
					{
						if (player.getInventory().getInventoryItemCount(skill.getItemConsumeId(),0) < skill.getItemConsumeCount())
						{
							return showText(st, "对不起", "你没有足够的道具:<br>您需要: <font color=LEVEL>" + skill.getItemConsumeCount() + " " + getItemNameHtml(st, skill.getItemConsumeId()) + "!", false, "0", "0");
						}
					}
					final boolean getSummonbuff = isPetBuff(st);
					if (!getSummonbuff)
					{
						if (eventParam3.equals("cubic"))
						{
							if (player.getCubics() != null)
							{
								for (Cubic  cubic : player.getCubics().values())
								{
									cubic.stopAction();
									player.getCubics().remove(cubic);
								}
							}
							player.useMagic(SkillData.getInstance().getSkill(Integer.parseInt(eventParam1), Integer.parseInt(eventParam2)), false, false);
						}
						else
						{
							SkillData.getInstance().getSkill(Integer.parseInt(eventParam1), Integer.parseInt(eventParam2)).applyEffects(player, player);
						}
					}
					else
					{
						if (eventParam3.equals("cubic"))
						{
							if (player.getCubics() != null)
							{
								for (Cubic  cubic : player.getCubics().values())
								{
									cubic.stopAction();
									player.getCubics().remove(cubic);
								}
							}
							player.useMagic(SkillData.getInstance().getSkill(Integer.parseInt(eventParam1), Integer.parseInt(eventParam2)), false, false);
						}
						else
						{
							if (player.getSummon() != null)
							{
								SkillData.getInstance().getSkill(Integer.parseInt(eventParam1), Integer.parseInt(eventParam2)).applyEffects(player.getSummon(), player.getSummon());
							}
							else
							{
								return showText(st, "对不起", "无法使用宠物状态<br>请先行召唤宠物!", false, "返回", "main");
							}
						}
					}
					player.destroyItemByItemId("道具", PREMIUM_CONSUMABLE_ID, cost, npc, true);
					if (PREMIUM_TIME_OUT)
					{
						addTimeout(st, player, PREMIUM_TIME_OUT_TIME / 10, 600);
					}
				}
				return PREMIUM_SMART_WINDOW ? null : buildHtml(eventParam3);
			}
			
			case "castBuffSet":
				if ((int) (System.currentTimeMillis() / 1000) > st.getInt("blockUntilTime"))
				{
					if (!PREMIUM_FREE_BUFFS)
					{
						if (player.getInventory().getInventoryItemCount(PREMIUM_CONSUMABLE_ID,0) < PREMIUM_BUFF_SET_PRICE)
						{
							return showText(st, "对不起", "你没有足够的道具:<br>您需要: <font color=LEVEL>" + PREMIUM_BUFF_SET_PRICE + " " + getItemNameHtml(st, PREMIUM_CONSUMABLE_ID) + "!", false, "0", "0");
						}
					}
					List<int[]> buff_sets = new ArrayList<>();
					final int player_class;
					if (player.isMageClass())
					{
						player_class = 1;
					}
					else
					{
						player_class = 0;
					}
					final boolean getSummonbuff = isPetBuff(st);
					if (!getSummonbuff)
					{
						try (Connection con = DatabaseFactory.getConnection())
						{
							PreparedStatement getSimilarNameCount = con.prepareStatement("SELECT buffId,buffLevel FROM premium_buff_list WHERE forClass IN (?,?) ORDER BY id ASC");
							getSimilarNameCount.setInt(1, player_class);
							getSimilarNameCount.setString(2, "2");
							ResultSet rss = getSimilarNameCount.executeQuery();
							while (rss.next())
							{
								int id = rss.getInt("buffId");
								int lvl = rss.getInt("buffLevel");
								buff_sets.add(new int[]
								{
									id,
									lvl
								});
							}
						}
						catch (SQLException e)
						{
							print(e);
						}
						for (int[] i : buff_sets)
						{
							SkillData.getInstance().getSkill(i[0], i[1]).applyEffects(player, player);
						}
					}
					else
					{
						if (player.getSummon() != null)
						{
							try (Connection con = DatabaseFactory.getConnection())
							{
								PreparedStatement getSimilarNameCount = con.prepareStatement("SELECT buffId,buffLevel FROM premium_buff_list WHERE forClass IN (?,?) ORDER BY id ASC");
								getSimilarNameCount.setString(1, "0");
								getSimilarNameCount.setString(2, "2");
								ResultSet rss = getSimilarNameCount.executeQuery();
								while (rss.next())
								{
									int id = rss.getInt("buffId");
									int lvl = rss.getInt("buffLevel");
									buff_sets.add(new int[]
									{
										id,
										lvl
									});
								}
							}
							catch (SQLException e)
							{
								print(e);
							}
							for (int[] i : buff_sets)
							{
								SkillData.getInstance().getSkill(i[0], i[1]).applyEffects(player.getSummon(), player.getSummon());
							}
						}
						else
						{
							return showText(st, "对不起", "无法使用宠物状态<br>请先行召唤宠物!", false, "返回", "main");
						}
					}
					player.destroyItemByItemId("道具", PREMIUM_CONSUMABLE_ID,PREMIUM_BUFF_SET_PRICE, npc, true);
					if (PREMIUM_TIME_OUT)
					{
						addTimeout(st, player, PREMIUM_TIME_OUT_TIME, 600);
					}
				}
				return PREMIUM_SMART_WINDOW ? null : rebuildMainHtml(st);
				
		}
		return rebuildMainHtml(st);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		QuestState st = player.getQuestState(QUEST_LOADING_INFO);
		if (st == null)
		{
			st = newQuestState(player);
		}
		if (player.isGM())
		{
			if (PREMIUM_SCRIPT_RELOAD)
			{
				return reloadPanel(st);
			}
			return rebuildMainHtml(st);
		}
		
		else if ((int) (System.currentTimeMillis() / 1000) > st.getInt("blockUntilTime"))
		{
			if (player.getInventory().getInventoryItemCount(23015, 0) >= 1)
			{
				if ((PREMIUM_ENABLE_VIP_BUFFER))
				{
					if (!PREMIUM_BUFF_WITH_KARMA && (player.getKarma() > 0))
					{
						return showText(st, "对不起", "你的性向值大于 <font color=FF0000>0!</font><br>因此无法使用我的服务!", false, "返回", "main");
					}
					else if (OlympiadManager.getInstance().isRegistered(player))
					{
						return showText(st, "对不起", "You can't buff while you are <font color=FF0000>registred in games olympics!</font><br>Come back,<br>when you don't have registred in games olympics!", false, "返回", "main");
					}
					else if (player.getLevel() < PREMIUM_MIN_LEVEL)
					{
						return showText(st, "对不起", "等级要高于 <font color=LEVEL>" + PREMIUM_MIN_LEVEL + "</font>级以上,<br>才可以使用我的服务!", false, "返回", "main");
					}
					else if (player.isInCombat())
					{
						return showText(st, "对不起", "战斗中无法使用我的服务!<br>停止您的战斗再试看看!", false, "返回", "main");
					}
					else
					{
						return rebuildMainHtml(st);
					}
				}
			}
			
			else if ((PREMIUM_ENABLE_VIP_BUFFER) && (player.getInventory().getInventoryItemCount(23015, 0) >= 1))
			{
				if (!PREMIUM_BUFF_WITH_KARMA && (player.getKarma() > 0))
				{
					return showText(st, "注意", "你的性向值大于 <font color=FF0000>0!</font><br>因此无法使用我的服务!", false, "返回", "main");
				}
				else if (OlympiadManager.getInstance().isRegistered(player))
				{
					return showText(st, "注意", "You can't buff while you are <font color=FF0000>registred in games olympics!</font><br>Come back,<br>when you don't have registred in games olympics!", false, "Return", "main");
				}
				else if (player.getLevel() < PREMIUM_MIN_LEVEL)
				{
					return showText(st, "注意", "Your level is too low!<br>You have to be at least level <font color=LEVEL>" + PREMIUM_MIN_LEVEL + "</font>,<br>to use my services!", false, "Return", "main");
				}
				else if (player.isInCombat())
				{
					return showText(st, "注意", "你处于战斗状态哦!<br>停止战斗状态后重新尝试!", false, "返回", "main");
				}
				else
				{
					return rebuildMainHtml(st);
				}
			}
			else if ((PREMIUM_ENABLE_VIP_BUFFER) && (player.getInventory().getInventoryItemCount(23015, 0) >= 1))
			{
				return rebuildMainHtml(st);
			}
			else if (!PREMIUM_BUFF_WITH_KARMA && (player.getKarma() > 0))
			{
				return showText(st, "对不起！", "你的<font color=FF0000>性向</font><br>值大于0<br>因此无法使用我的服务", false, "返回", "main");
			}
			else if (OlympiadManager.getInstance().isRegistered(player))
			{
				return showText(st, "对不起！", "You can't buff while you are <font color=FF0000>registred in games olympics!</font><br>Come back,<br>when you don't have registred in games olympics!", false, "Return", "main");
			}
			else if (player.getLevel() < PREMIUM_MIN_LEVEL)
			{
				return showText(st, "对不起！", "等级要高于 <font color=LEVEL>" + PREMIUM_MIN_LEVEL + "</font>级以上,<br>才可以使用我的服务!", false, "返回", "main");
			}
			else if (player.isInCombat())
			{
				return showText(st, "对不起！", "战斗中无法使用我的服务!<br>停止您的战斗再试看看!", false, "返回", "main");
			}
			else
			{
				return rebuildMainHtml(st);
			}
			return showText(st, "对不起！", "只有「会员玩家」<br>才可以使用我的服务!", false, "返回", "main");
		}
		else
		{
			return showText(st, "对不起！", "您无法使用我的服务!<br>请稍候再试看看!", false, "返回", "main");
		}
	}
	
	@Override
	public boolean showResult(Player player, String res)
	{
		if (PREMIUM_SMART_WINDOW)
		{
			if ((player != null) && (res != null) && res.startsWith("<html>"))
			{
				NpcHtmlMessage npcReply = new NpcHtmlMessage(5, 1); /* ! */
				npcReply.setHtml(res);
				player.sendPacket(npcReply);
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}
		return super.showResult(player, res);
	}
	
	private String getSkillIconHtml(int id, int level)
	{
		String iconNumber = getSkillIconNumber(id, level);
		return "<button action=\"bypass -h Quest " + QUEST_LOADING_INFO + " description " + id + " " + level + " x\" width=32 height=32 back=\"Icon.skill" + iconNumber + "\" fore=\"Icon.skill" + iconNumber + "\">";
	}
	
	private String getSkillIconNumber(int id, int level)
	{
		String formato;
		if (id == 4)
		{
			formato = "0004";
		}
		else if ((id > 9) && (id < 100))
		{
			formato = "00" + id;
		}
		else if ((id > 99) && (id < 1000))
		{
			formato = "0" + id;
		}
		else if (id == 1517)
		{
			formato = "1536";
		}
		else if (id == 1518)
		{
			formato = "1537";
		}
		else if (id == 1547)
		{
			formato = "0065";
		}
		else if (id == 2076)
		{
			formato = "0195";
		}
		else if ((id > 4550) && (id < 4555))
		{
			formato = "5739";
		}
		else if ((id > 4698) && (id < 4701))
		{
			formato = "1331";
		}
		else if ((id > 4701) && (id < 4704))
		{
			formato = "1332";
		}
		else if (id == 6049)
		{
			formato = "0094";
		}
		else
		{
			formato = String.valueOf(id);
		}
		return formato;
	}
	
	static public void main(String[] args)
	{
		new PremiumNpcBuffer();
	}
}
