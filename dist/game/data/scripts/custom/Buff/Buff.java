/**
 * Jacky 制作,商业化专用脚本
 */

package custom.Buff;

import java.util.concurrent.ScheduledFuture;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.actor.status.PlayerStatus;
import org.l2jmobius.gameserver.model.actor.stat.PlayerStat;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import ai.AbstractNpcAI;

// VIP 金币建议ＧＭ出售比例１：１００　 

public class Buff extends AbstractNpcAI {
	private final static String qn = "Buff";
	private final static int npcIds = 33342;
	private static Date openServerDate = null;
	
	private static ScheduledFuture<?> _selfDestructionTask = null;
	
	private Buff() {
		addStartNpc(npcIds);
		addTalkId(npcIds);
		addFirstTalkId(npcIds);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player) {
		String htmltext = "";
		if (event.equalsIgnoreCase("jinzhan")) {
			GetBuff(player,
					"1085,3 4703,13 1259,330 1062,2 1500,1 1353,1 1461,1 275,1 1323,1 1374,1 1388,3 1501,1 982,3 1502,1 1035,4 4699,13 1364,1 1519,1 1499,1 1542,1 1549,1 1238,330 1363,315 1307,130 1392,130 1393,130 1191,330 1189,330 1548,330 1182,330 829,1 825,1 914,1 268,1 269,1 271,1 274,1 304,1 310,1 264,1 267,1 915,1 349,1");
		} else if (event.equalsIgnoreCase("qishi")) {
			GetBuff(player,
					"1085,3 4703,13 1259,330 1500,1 1353,1 1461,1 1323,1 1374,1 1501,1 982,3 1502,1 1035,4 1504,1 4699,13 1364,1 1519,1 1499,1 1542,1 1549,1 1238,330 1363,315 1307,130 1392,130 1393,130 1191,330 1189,330 1548,330 1182,330 825,1 1389,3 828,1 914,1 268,1 269,1 271,1 274,1 275,1 304,1 264,1 267,1 349,1 364,1 764,1");
		}
		 else if (event.equalsIgnoreCase("gongshou")) {
			GetBuff(player,
					"1323,1 1388,3 1461,1 1499,1 1500,1 827,1 1501,1 1502,1 1504,1 1519,1 1062,2 1363,315 4699,13 1035,4 1259,330 1352,1 1353,1 1364,1 1542,1 267,1 268,1 269,1 304,1 764,1 271,1 272,1 274,1 275,1 915,1 4703,13 1374,1 982,3 1238,330 829,1 1085,3 1307,3 1189,330 1191,330 1393,130 1392,130 1548,330 1182,330 914,1 264,1");
		}
		 else if (event.equalsIgnoreCase("fashi")) {
			GetBuff(player,
					"1323,1 1461,1 1499,1 1500,1 1501,1 1504,1 1062,2 1035,1 1259,330 1353,1 1364,1 1542,1 267,1 268,1 304,1 764,1 915,1 4703,13 1374,1 1238,330 1085,3 1307,130 1189,330 1191,330 1393,130 1392,130 1548,330 1182,330 914,1 264,1 1389,3 1303,2 1549,1 1078,6 830,1 1357,315 273,1 276,1 365,1 349,1 529,1");
		}
		 else if (event.equalsIgnoreCase("jiandou")) {
			GetBuff(player,
					"1085,3 4703,13 1259,330 1062,2 1500,1 1353,1 1461,1 1323,11374,1 1388,3 1501,1 982,3 1502,1 1035,4 1504,1 4699,131364,1 1519,1 1499,1 1542,1 1549,1 828,1 825,1 1238,330 1363,315 1307,130 1392,130 1393,130 1182,330 1548,330 1191,330 1189,330 914,1 268,1 269,1 271,1 274,1 275,1 304,1 264,1 267,1 349,1 764,1 915,1");
		}
		 else if (event.equalsIgnoreCase("cike")) {
			GetBuff(player,
					"1085,3 4703,13 1259,330 1062,2 1500,1 1353,1 1461,1 1323,1 1374,1 1388,3 1501,1 982,3 1502,1 1035,4 1504,1 4699,13 1364,1 1519,1 1499,1 1542,1 1549,1 1238,330 1363,315 1363,315 1307,130 1392,130 1393,130 1182,330 1548,330 1191,330 1189,330 829,1 825,1 914,1 268,1 269,1 271,1 274,1 275,1 304,1 264,1 267,1 349,1 364,1 915,1");
		}
		 else if (event.equalsIgnoreCase("naima")) {
			GetBuff(player,
					"1035,4 1259,330 1499,1 1501,1 1085,3 1303,2 1364,1 1504,1 1323,1 1374,1 4703,13 1500,1 1542,1 1549,1 1078,6 1353,1 1461,1 1238,330 1363,315 1307,130 1392,130 1393,130 1191,330 1189,330 1548,330 1182,330 828,1 1389,3 914,1 268,1 273,1 276,1 304,1 365,1 267,1 349,1 264,1 529,1 530,1 764,1");
		}
		 else if (event.equalsIgnoreCase("huifu")) {
			if (!player.isInCombat()){
			PlayerStatus PlayerStatus = player.getStatus();
			PlayerStat PlayerStat = player.getStat();
			PlayerStatus.setCurrentHp(PlayerStat.getMaxHp());
			PlayerStatus.setCurrentMp(PlayerStat.getMaxMp());
			PlayerStatus.setCurrentCp(PlayerStat.getMaxCp());			
			 }else{
				 player.sendPacket(new ExShowScreenMessage("您在战斗中，无法回复生命值", 3000));				 
			 }
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player) {
			return "Buff.htm";			
		
	}
	
	private void GetBuff(Player activeChar, String buff) {
		Creature target = activeChar;
		String[] buffGroupArray = buff.split(" ");
		for (String buffGoupList : buffGroupArray) {
			if (buffGoupList == null)
				return;

			String[] skl = buffGoupList.split(",");
			int skill_id = Integer.parseInt(skl[0]);
			int skill_level = Integer.parseInt(skl[1]);
			if ((skill_level > 100) && activeChar.getInventory().getInventoryItemCount(23015, 0) < 1)
				skill_level = 1;
			Skill skill = SkillData.getInstance().getSkill(skill_id,skill_level);
			if (skill != null) {
				skill.applyEffects(activeChar, target);
			}
		}
	}

	public static void main(String[] args) {
		new Buff();
		System.out.println("一键状态载入成功");
	}
}