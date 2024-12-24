/**
 * Jacky 制作,商业化专用脚本
 */

package custom.VipServer;

import java.util.concurrent.ScheduledFuture;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import ai.AbstractNpcAI;

// VIP 金币建议ＧＭ出售比例１：１００　 

public class VipServer extends AbstractNpcAI {
	private final static String qn = "VipServer";
	private final static int npcIds = 33334;
	private static Date openServerDate = null;
	private final static int[] buwei = {5,1,4,6,7,8,9,10,11,12,13,14,0,24};

	
	private static ScheduledFuture<?> _selfDestructionTask = null;
	
	private VipServer() {
		addStartNpc(npcIds);
		addTalkId(npcIds);
		addFirstTalkId(npcIds);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player) {
		String htmltext = "";
		if (event.startsWith("getvip")) {
			ViP(event, npc, player);
			return htmltext;
		} else if (event.equalsIgnoreCase("No")) {
			MultisellData.getInstance().separateAndSend(60001, player, npc, false);
		} else if (event.equalsIgnoreCase("No10")) {
			MultisellData.getInstance().separateAndSend(60004, player, npc, false);
		} else if (event.equalsIgnoreCase("No15")) {
			MultisellData.getInstance().separateAndSend(60007, player, npc, false);
		} else if (event.equalsIgnoreCase("No20")) {
			MultisellData.getInstance().separateAndSend(60008, player, npc, false);
		}else if (event.equalsIgnoreCase("NoPZ")) {
			MultisellData.getInstance().separateAndSend(80006, player, npc, false);
		} else if (event.equalsIgnoreCase("NoLB")) {
			MultisellData.getInstance().separateAndSend(80007, player, npc, false);
		} else if (event.equalsIgnoreCase("NoBuy")) {
			player.sendMessage("暂未出售!");
		}
		else if(event.equalsIgnoreCase("16zb")){
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (23 * 3600 * 1000))) {
					MultisellData.getInstance().separateAndSend(60008, player, npc, false);
				}else{
				player.sendPacket(new ExShowScreenMessage("11点开放+16套装购买，现在还没开放", 5000));	
			}				
			}
		}
		else if(event.equalsIgnoreCase("ZSZB")){
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.KDL_F * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					MultisellData.getInstance().separateAndSend(60004, player, npc, false);
				}else{
				player.sendPacket(new ExShowScreenMessage("止水装备还没有开放购买", 5000));	
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
					MultisellData.getInstance().separateAndSend(60006, player, npc, false);
				}else{
				player.sendPacket(new ExShowScreenMessage("虎魄装备还没有开放购买", 5000));	
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
					MultisellData.getInstance().separateAndSend(60007, player, npc, false);
				}else{
				player.sendPacket(new ExShowScreenMessage("破星装备还没有开放升级", 5000));	
			}				
			}
		}
		else if (event.equalsIgnoreCase("25QIANGHA"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.ENCHANT_15 * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					QIANG25(player);
				}else{
				player.sendPacket(new ExShowScreenMessage("强化+25还没开放", 5000));	
			}				
			}
		}
		else if (event.equalsIgnoreCase("35QIANGHA"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.ENCHANT_20 * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					QIANG35(player);
				}else{
				player.sendPacket(new ExShowScreenMessage("强化+35还没开放", 5000));	
			}				
			}
		}
		else if (event.equalsIgnoreCase("45QIANGHA"))
		{
			if (Config.CAT_AUTO_UP) {
			try {
				openServerDate = new SimpleDateFormat("yyyyMMdd").parse(Config.NEW_SERVER_DATE);
			} catch (ParseException e) {
				System.out.println("自动装备更新系统异常");
			}
			if (System.currentTimeMillis() >= (openServerDate.getTime() + (Config.ENCHANT_25 * 24 * 3600 * 1000) + (Config.CAT_AUTO_UP_HOURS * 3600 * 1000))) {
					QIANG45(player);
				}else{
				player.sendPacket(new ExShowScreenMessage("强化+45还没开放", 5000));	
			}				
			}
		}
		else if (event.equalsIgnoreCase("NoZSBF")) {
		if (player.getInventory().getInventoryItemCount(23015, 0) >= 1)
		{	
			GetBuff(player,
					"4703,130 914,1 1259,330 268,1 269,1 271,1 274,1 275,1 304,1 310,1 1062,2 1500,1 1353,1 529,1 1461,1 1323,1 1374,1 1388,3 1501,1 982,3 1502,1 1035,4 264,1 267,1 1504,1 4699,13 1364,1 364,1 1519,1 1499,1 1542,1 1549,1 828,1 825,1 1238,330 1363,315");
		}
		} else if (event.equalsIgnoreCase("NoFSBF")) {
		if (player.getInventory().getInventoryItemCount(23015, 0) >= 1)
		{
			GetBuff(player,
					"914,1 268,1 1259,330 273,1 276,1 304,1 365,1 1499,1 1389,3 1501,1 1062,2 1085,3 1303,2 267,1 1364,1 1504,1 1323,1 1374,1 305,1 349,1 4703,13 1500,1 264,1 1355,1 1542,1 1549,1 1078,6 1353,1 1461,1 1035,1 529,1 530,1 830,1 1238,330 1363,315");
		}
		}
		return htmltext;
	}
	
	
	private void ViP(String event, Npc npc, Player player) {
		QuestState st = player.getQuestState(getName());
		if (player.getInventory().getInventoryItemCount(10639, 0) >= 10000){
			if(player.getInventory().getInventoryItemCount(23015, 0) <= 0){
				player.destroyItemByItemId("道具", 10639, 10000, npc, true);
				player.getInventory().addItem("物品", 23015, 1, player, null);
				player.sendMessage("你已经获得VIP身份卡");
			}
			else{
				player.sendMessage("您已经是会员,不用重新办理");								
			}		
			}
			else
			{
				player.sendMessage("您没有足够的道具");				
			}
	}


	private void QIANG25(Player player) {
		int itemid = 30015;
		int count = 1;
		if (player.getInventory().getInventoryItemCount(itemid, 0) >= count){
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item == null){
			player.sendPacket(new ExShowScreenMessage("请把装备5件,首饰5件,披风,腰带,内衣,手镯,武器,盾牌装备上", 5000));	
			return;
			}			
		}
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item.getEnchantLevel() < 16){
			player.sendPacket(new ExShowScreenMessage("装备【"+item.getName()+"】强化等级不足16", 5000));	
			return;
			}			
		}
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item.getEnchantLevel() >= 16){
			item.setEnchantLevel(25);	
			player.sendPacket(new ExShowScreenMessage("全体装备已强化到25", 5000));	
			}			
		}
		player.destroyItemByItemId("道具", itemid, count, null, true);
	   }else{
		player.sendPacket(new ExShowScreenMessage("强化25凭证不足!", 5000));	   
	   }
	   
	}
	private void QIANG35(Player player) {
		int itemid = 30020;
		int count = 1;
		if (player.getInventory().getInventoryItemCount(itemid, 0) >= count){
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item == null){
			player.sendPacket(new ExShowScreenMessage("请把装备5件,首饰5件,披风,腰带,内衣,手镯,武器,盾牌装备上", 5000));	
			return;
			}			
		}
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item.getEnchantLevel() < 25){
			player.sendPacket(new ExShowScreenMessage("装备【"+item.getName()+"】强化等级不足25", 5000));	
			return;
			}			
		}
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item.getEnchantLevel() >= 25){
			item.setEnchantLevel(35);	
			player.sendPacket(new ExShowScreenMessage("全体装备已强化到35", 5000));	
			}			
		}
		player.destroyItemByItemId("道具", itemid, count, null, true);
	   }else{
		player.sendPacket(new ExShowScreenMessage("强化35凭证不足!", 5000));	   
	   }
	   
	}
	private void QIANG45(Player player) {
		int itemid = 30025;
		int count = 1;
		if (player.getInventory().getInventoryItemCount(itemid, 0) >= count){
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item == null){
			player.sendPacket(new ExShowScreenMessage("请把装备5件,首饰5件,披风,腰带,内衣,手镯,武器,盾牌装备上", 5000));	
			return;
			}			
		}
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item.getEnchantLevel() < 35){
			player.sendPacket(new ExShowScreenMessage("装备【"+item.getName()+"】强化等级不足35", 5000));	
			return;
			}			
		}
		for(int buweiid : buwei){
			Item item = player.getInventory().getPaperdollItem(buweiid);
			if(item.getEnchantLevel() >= 35){
			item.setEnchantLevel(45);	
			player.sendPacket(new ExShowScreenMessage("全体装备已强化到45", 5000));	
			}			
		}
		player.destroyItemByItemId("道具", itemid, count, null, true);
	   }else{
		player.sendPacket(new ExShowScreenMessage("强化45凭证不足!", 5000));	   
	   }
	   
	}

	
	
	
	
	@Override
	public String onFirstTalk(Npc npc, Player player) {
		return "Vip.htm";		
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
		new VipServer();
		System.out.println("VIP载入成功");
	}
}