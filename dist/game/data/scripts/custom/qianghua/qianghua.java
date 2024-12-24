/**
 * Jacky 制作,商业化专用脚本
 */

package custom.qianghua;

import java.util.concurrent.ScheduledFuture;
import java.util.StringTokenizer;
import java.lang.Long;
import java.util.List;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.data.ItemTable;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.model.item.Weapon;
import org.l2jmobius.gameserver.model.item.Armor;
import org.l2jmobius.commons.util.Rnd;
import ai.AbstractNpcAI;

// VIP 金币建议ＧＭ出售比例１：１００　 

public class qianghua extends AbstractNpcAI {
	
	private final static String qn = "qianghua";
	private final static int npcIds = 33340;

		
	private qianghua() {
		addStartNpc(npcIds);
		addTalkId(npcIds);
		addFirstTalkId(npcIds);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player) {
		String htmltext = "";
	if (event.startsWith("QIANG")) {
		StringTokenizer st = new StringTokenizer(event.substring(6)," ");
		int itemid = Integer.parseInt(st.nextToken());
		ZL(player,itemid);
	}
	if (event.startsWith("two")) {
		XuanB(player);
	}
	return htmltext;
	}
	
	private void ZL(Player player,int itemid){
		int itema = 57;
		Item itemb = player.getInventory().getItemByObjectId(itemid);
		if(itemb.getTemplate() instanceof Armor){
			itema = 960;
		}
		if(itemb.getTemplate() instanceof Weapon){
			itema = 959;
		}
		if(itemb.getId() <= 24135 && itemb.getId() >= 24128){
			itema = 6570;
		}
		if(itemb.getTemplate() instanceof Armor || itemb.getTemplate() instanceof Weapon){
			int lv = 6-itemb.getEnchantLevel();
			if (player.getInventory().getInventoryItemCount(itema, 0) >= lv){
				player.destroyItemByItemId("道具", itema, lv, null, true);
				itemb.setEnchantLevel(itemb.getEnchantLevel() +(6 - itemb.getEnchantLevel()));
				player.sendMessage("强化成功.");
		}		
		else{
			player.sendMessage("强化道具需要强化"+lv+"个强化卷.");
		}
		}
		else{
			player.sendMessage("物品道具无法进行强化.");
		}
		XuanB(player);
	}

	
	private void XuanB(Player player){
		NpcHtmlMessage rateReply = new NpcHtmlMessage(0, 1);
		int count = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("<center>");
		sb.append("<table width=\"260px\" border=\"0\">");
		sb.append("<tr>");
		sb.append("<td width=80 height=40><center>").append("道具名称").append("</center></td>");
		sb.append("<td width=60 height=40><center>").append("强化值").append("</center></td>");
		sb.append("<td width=60 height=40><center>").append("强化").append("</center></td>");
		sb.append("</tr>");
		for(Item item : player.getInventory().getItems())
		{
				if(item.getEnchantLevel() >= 0 && item.getEnchantLevel() < 6){
					if(((item.getTemplate() instanceof Armor || item.getTemplate() instanceof Weapon)) && !item.isEquipped()){
					count++;
					if(count < 31){
						sb.append("<center>");
						sb.append("<tr>");
						sb.append("<td width=80 height=40><center><font color=\"0099FF\">" + item.getName()+ "</font></center></td>");
						sb.append("<td width=60 height=40><center><font color=\"0099FF\">" + item.getEnchantLevel()+ "</font></center></td>");
						sb.append("<td width=60 height=40><center><button value=\"强化\" action=\"bypass -h Quest qianghua QIANG " + item.getObjectId() + "\" width=40 height=20 back=\"L2UI_CT1.ListCTRL_DF_Title_Down\" fore=\"L2UI_ct1.ListCTRL_DF_Title\"></center></td>");
						sb.append("</tr>");	
						sb.append("</center>");		
						}						
					}
				}
			}
			sb.append("</table>");
			sb.append("</center>");
			rateReply.setFile(player, "data/scripts/custom/qianghua/indexa.htm");
			rateReply.replace("%list%", sb.toString());
			player.sendPacket(rateReply);		
	}	
	
	@Override
	public String onFirstTalk(Npc npc, Player player) {
		return "start.htm";
	}	

	public static void main(String[] args) {
		new qianghua();
		System.out.println("装备强化载入");
	}
}