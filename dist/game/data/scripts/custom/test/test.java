/**
 * Jacky 制作,商业化专用脚本
 */

package custom.test;

import java.util.concurrent.ScheduledFuture;


import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import ai.AbstractNpcAI;

// VIP 金币建议ＧＭ出售比例１：１００　 

public class test extends AbstractNpcAI {
	private final static String qn = "test";
	private final static int npcIds = 33339;
	
	private static ScheduledFuture<?> _selfDestructionTask = null;
	
	private test() {
		addStartNpc(npcIds);
		addTalkId(npcIds);
		addFirstTalkId(npcIds);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player) {
		String htmltext = "";
		if (event.startsWith("shengji")) {
			player.addExpAndSp(25000000000L, 2000000000);
			return htmltext;
		} else if (event.equalsIgnoreCase("No")) {
			MultisellData.getInstance().separateAndSend(60005, player, npc, false);
		} else if (event.equalsIgnoreCase("No10")) {
			MultisellData.getInstance().separateAndSend(60004, player, npc, false);
		} else if (event.equalsIgnoreCase("No15")) {
			MultisellData.getInstance().separateAndSend(60007, player, npc, false);
		} else if (event.equalsIgnoreCase("No20")) {
			MultisellData.getInstance().separateAndSend(60005, player, npc, false);
		}else if (event.equalsIgnoreCase("NoPZ")) {
			MultisellData.getInstance().separateAndSend(80006, player, npc, false);
		} else if (event.equalsIgnoreCase("NoLB")) {
			MultisellData.getInstance().separateAndSend(80007, player, npc, false);
		} else if (event.equalsIgnoreCase("NoBuy")) {
			player.sendMessage("暂未出售!");
		}
		return htmltext;
	}

	@Override
	public String onFirstTalk(Npc npc, Player player) {		
		return "Vip.htm";				
	}

	public static void main(String[] args) {
		new test();
		System.out.println("测试管理载入成功");
	}
}