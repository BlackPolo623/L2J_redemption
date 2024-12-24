/**
 * Jacky 制作,商业化专用脚本
 */

package custom.suijizza;

import java.util.concurrent.ScheduledFuture;
import java.util.StringTokenizer;
import java.lang.Long;
import java.util.List;
import java.util.ArrayList;
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
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.util.Broadcast;
import org.l2jmobius.gameserver.network.serverpackets.Earthquake;
import org.l2jmobius.gameserver.data.xml.suijizz;
import org.l2jmobius.gameserver.model.holders.suijizzHolder;
import ai.AbstractNpcAI;





// VIP 金币建议ＧＭ出售比例１：１００　 

public class suijizza extends AbstractNpcAI {
	
	private final static String qn = "suijizza";
	private final static int npcIds = 33336;
	//物品道具id                            材料                         装备首饰                                  腰带衬衣    手镯龙武器   武器碎片                护身符                      披风                    
	//物品道具id                            0   1     2     3   4    5    6     7     8     9    10    11    12    13     14    15    16    17    18    19    20    21    22    23    24    25   26
	private final int[] item = new int[]{38001,38002,6657,6658,6659,6660,6656,15871,15572,15575,15578,15581,15584,33327,33329,33330,38010,6577,6578,17051,17052,17053,17054,17055,17056,17061,24128,10639};
	private final int[] count = new int[]{500,500,1,1,1,1,1,1,1,1,1,1,1,1,1,1,10,1,1,1,1,1,1,1,1,1,1,200};
		
	private suijizza() {
		addStartNpc(npcIds);
		addTalkId(npcIds);
		addFirstTalkId(npcIds);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player) {
		String htmltext = "";
		if (event.startsWith("one")) {
			String lb = player.getVariables().getString("suijiliebiao", "1000");
			if(lb.length() < 5){
				inter(player);
				view(player);
			}else{
				view(player);
			}
	   }
	   	if (event.startsWith("make")) {
		if(player.getInventory().getInventoryItemCount(38002, 0) >= 2000){
				make(player);
				inter(player);
				view(player);
		}else{
			player.sendMessage("制作道具需要2000个制作种子");			
		}
		}
		if (event.startsWith("shuaxin")) {
			if(player.getInventory().getInventoryItemCount(38001, 0) >= 1000){
				player.destroyItemByItemId("道具", 38001, 1000, npc, true);
				inter(player);
				view(player);				
		}else{
			player.sendMessage("刷新列表需要1000个灵魂碎片");			
		}
	}
	return htmltext;
	}

	private void inter(Player player){
		StringBuilder sb = new StringBuilder();		
		List<suijizzHolder> zlb = suijizz.getInstance().getjclists();
		if (zlb != null){
		List<suijizzHolder> flb = new ArrayList<>(5);
		for(int i = 0; i<5;i++){
            			
			 flb.add(getNewRewardG(flb));
		}
      for(suijizzHolder element : flb){
		  sb.append(element.getId()).append(",").append(element.getcount()).append(";");
	  }
	  String result = sb.toString();
      player.getVariables().set("suijiliebiao", result);
	}
	}

	public synchronized suijizzHolder getNewReward(List<suijizzHolder> zis)
	{
		int _randomRewardIndex = 0;
		suijizzHolder reward = null;
		suijizzHolder reward2 = null;
		final double random = Rnd.get(0,1000);
		List<suijizzHolder> zlb = suijizz.getInstance().getjclists();
		while(reward2 == null){			
			if(_randomRewardIndex < (zlb.size()-1)){
			_randomRewardIndex++;
			reward = zlb.get(_randomRewardIndex);
			if (random < reward.getchane())
			{
				reward2 = new suijizzHolder(reward.getId(), reward.getcount(), reward.getchane());
			}
			for(suijizzHolder element:zis){
				if(element.getId() == reward.getId()){
					reward2 = null;
				}
			}
			}else{
				reward = zlb.get(_randomRewardIndex);
				reward2 = new suijizzHolder(reward.getId(), reward.getcount(), reward.getchane());
			}
		}
		return reward2;
	}
	
	public suijizzHolder getNewRewardG(List<suijizzHolder> zis)
	{
		suijizzHolder result = null;
		if(result == null){
			 result = getNewReward(zis);
		}
		return result;
	}
	
	private void view(Player player){		
		NpcHtmlMessage rateReply = new NpcHtmlMessage(0, 1);
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
        String zhiuo = player.getVariables().getString("suijiliebiao", "1000");
        String[] itemlisto = zhiuo.split(";");
		for (String ineg : itemlisto) {
			String[] parts = ineg.split(","); // 拆分每个元素的内容
			int itemid = Integer.parseInt(parts[0]);
			int count = Integer.parseInt(parts[1]);
			ItemTemplate it = ItemTable.getInstance().getTemplate(itemid);
			sb.append("<td><tr><center><img src=\"" + it.getIcon() + "\" width=32 height=32></center></tr>");
			sb.append("<tr><center><font color=\"0099FF\">" + it.getName() + "</font></center></tr>");
			sb.append("<tr><center><font color=\"0099FF\">" + count + "个</font></center></tr></td>");
		}
		sb.append("</tr>");	
		rateReply.setFile(player, "data/scripts/custom/suijizza/index.htm");
		rateReply.replace("%list%", sb.toString());
		player.sendPacket(rateReply);		
	}
	
	private void make(Player player) {		
    int[] zzitem = new int[]{};
    int[] zzcount = new int[]{};
    int rnd88 = Rnd.get(0, 4);
    String zhiuo = player.getVariables().getString("suijiliebiao", "1000");
    String[] itemlisto = zhiuo.split(";");
    for (String ineg : itemlisto) {
        String[] parts = ineg.split(","); // 拆分每个元素的内容
        int itemid = Integer.parseInt(parts[0]);
        int count = Integer.parseInt(parts[1]);
        zzitem = addItemToArray(zzitem, itemid);
        zzcount = addItemToArray(zzcount, count);
    }		
    player.destroyItemByItemId("道具", 38002, 2000, null, true);
    Item itemd = player.addItem("物品", zzitem[rnd88], zzcount[rnd88], player, true);
    if((zzitem[rnd88] == 10639) || ((zzitem[rnd88] >= 24128)&&(zzitem[rnd88] < 24135)) || (zzitem[rnd88] == 65544) || ((zzitem[rnd88] >= 33327)&&(zzitem[rnd88] < 33331))){
        Broadcast.toAllOnlinePlayers("玩家[" + player.getName() + " ]通过随机制作获得"+ zzcount[rnd88] +"个[" + itemd.getName() +"].", false);
    }
}
	


	private int[] addItemToArray(int[] array, int element) {
    int[] newArray = new int[array.length + 1];
    System.arraycopy(array, 0, newArray, 0, array.length);
    newArray[array.length] = element;
    return newArray;
	}

	@Override
	public String onFirstTalk(Npc npc, Player player) {
		return "start.htm";
	}	

	public static void main(String[] args) {
		new suijizza();
		System.out.println("随机制作NPC载入");
	}
}