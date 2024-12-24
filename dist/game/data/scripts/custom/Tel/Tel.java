/**
 * Jacky 制作,商业化专用脚本
 */

package custom.Tel;

import java.util.concurrent.ScheduledFuture;
import java.util.StringTokenizer;
import java.lang.Long;
import java.util.List;
import java.util.Calendar;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.Party;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.data.ItemTable;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.commons.util.Rnd;
import ai.AbstractNpcAI;

// VIP 金币建议ＧＭ出售比例１：１００　 

public class Tel extends AbstractNpcAI {
	
	private final static String qn = "Tel";
	private final static int npcIds = 33335;

		
	private Tel() {
		addStartNpc(npcIds);
		addTalkId(npcIds);
		addFirstTalkId(npcIds);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player) {
		String htmltext = "";
	if (event.startsWith("DAN")) {
			StringTokenizer st = new StringTokenizer(event.substring(4)," ");
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int z = Integer.parseInt(st.nextToken());			
			tel(player,x,y,z);
	}
	if (event.startsWith("Dui")) {
			StringTokenizer st = new StringTokenizer(event.substring(4)," ");
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int z = Integer.parseInt(st.nextToken());			
			Dui(player,x,y,z);
	}
	if (event.startsWith("A")) {
		return "A.htm";
	}
	if (event.startsWith("B")) {
		return "B.htm";
	}
	if (event.startsWith("C")) {
		return "C.htm";
	}
	if (event.startsWith("D")) {
		return "D.htm";
	}
	if (event.startsWith("E")) {
		return "E.htm";
	}
	if (event.startsWith("F")) {
		return "F.htm";
	}
	if (event.startsWith("G")) {
		return "G.htm";
	}
	if (event.startsWith("H")) {
		return "H.htm";
	}
	return htmltext;
	}
	
	private void tel(Player player,int x, int y, int z){
		
		if (!player.isInCombat())
        {
			if(player.getInventory().getInventoryItemCount(57, 0) >= 1000){
				player.teleToLocation(x, y, z);
                player.getInventory().destroyItemByItemId("传送需求", 57, 1000, null, true);
			}else{
				player.sendPacket(new ExShowScreenMessage("没有足够的金币无法传送", 3000));
			}
        }
		else
		{
			player.sendPacket(new ExShowScreenMessage("您在战斗中，无法进行传送", 3000));
		}

	}

	private void Dui(Player player,int x, int y, int z){		
		Party party = player.getParty();
		if(player.getInventory().getInventoryItemCount(57, 0) >= 1000){
			if (checkPartyConditions(player))
			{
                player.getInventory().destroyItemByItemId("传送需求", 57, 1000, null, true);
                for (Iterator localIterator = player.getParty().getMembers().iterator(); localIterator.hasNext();)
                {
				  int a = Rnd.get(1,50);
				  int b = Rnd.get(1,50);	
                  Player member = (Player)localIterator.next();
                  member.teleToLocation(x+a, y+b, z);
                }				
			}
		}else{
			player.sendPacket(new ExShowScreenMessage("没有足够的金币无法传送", 3000));
		}

	}

	private static boolean checkPartyConditions(Player player)
	{
		final Party party = player.getParty();
		// player must be in party
		if (party == null)
		{
			player.sendPacket(new ExShowScreenMessage("你没有队伍无法进行队传", 3000));				
			return false;
		}
		// ...and be party leader
		if (!player.getParty().isLeader(player))
		{
			player.sendPacket(new ExShowScreenMessage("需要队长才能传送，你不是队长", 3000));
			return false;
		}
		// party must not exceed max size for selected instance
		Iterator<Player> localIterator1 = party.getMembers().iterator();
		while (localIterator1.hasNext())
			{
				Player partyMember = (Player)localIterator1.next();
				if ((partyMember.isInCombat()))
				{
					player.sendPacket(new ExShowScreenMessage("队员 " + partyMember.getName() + " 在战斗中,无法使用", 3000));
					partyMember.sendPacket(new ExShowScreenMessage("你的队长在组队传送,请你停止战斗", 3000));
					return false;
				}
		}
		return true;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player) {
		return "start.htm";
	}	

	public static void main(String[] args) {
		new Tel();
		System.out.println("传送npc载入");
	}
}