/**
 * Jacky 制作,商业化专用脚本
 */

package custom.huodongbs;

import java.util.concurrent.ScheduledFuture;
import java.util.StringTokenizer;
import java.lang.Long;
import java.util.List;
import java.util.Calendar;
import java.sql.Connection;
import java.util.concurrent.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

import org.l2jmobius.Config;
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
import org.l2jmobius.gameserver.model.item.ItemTemplate;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.model.item.Weapon;
import org.l2jmobius.gameserver.model.item.Armor;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.commons.util.Rnd;
import org.l2jmobius.gameserver.util.Broadcast;
import org.l2jmobius.gameserver.model.Spawn;
import org.l2jmobius.gameserver.enums.ChatType;
import org.l2jmobius.gameserver.model.World;
import ai.AbstractNpcAI;

// VIP 金币建议ＧＭ出售比例１：１００　 

public class huodongbs extends AbstractNpcAI {
	
	private final static String qn = "huodongbs";
	private final static int npcid = 100046;//npcid
	private final ScheduledExecutorService schedulera = Executors.newScheduledThreadPool(1);

	
	private huodongbs() {
	scheduleDailyCleanup();
   }
		
	public void scheduleDailyCleanup() {
    schedulera.scheduleAtFixedRate(this::dailyCleanup, 1, 1, TimeUnit.SECONDS);
	}
	
    private void dailyCleanup() {
		for(Player player : World.getInstance().getPlayers()){
			player.broadcastUserInfo();
		}		
    }
	
	public static void main(String[] args) {
		new huodongbs();
		System.out.println("shuaxin载入");
	}
}