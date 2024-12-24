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
package ai.bosses.BEL;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.ChatType;
import org.l2jmobius.gameserver.instancemanager.GlobalVariablesManager;
import org.l2jmobius.gameserver.instancemanager.GrandBossManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Attackable;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.actor.instance.GrandBoss;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.NpcSay;
import org.l2jmobius.gameserver.network.serverpackets.PlaySound;

import ai.AbstractNpcAI;

/**
 * LINDE AI.
 * @author DrLecter, Emperorc, Mobius
 */
public class BEL extends AbstractNpcAI
{
	// NPCs
	private static final int LINDE = 33349;
	// Spawns
	// Misc
	private static final byte ALIVE = 0;
	private static final byte DEAD = 1;
	private static boolean _firstAttacked;
	
	private BEL()
	{
		registerMobs(LINDE);
		_firstAttacked = false;
		final StatSet info = GrandBossManager.getInstance().getStatSet(LINDE);
		if (GrandBossManager.getInstance().getStatus(LINDE) == DEAD)
		{
			// Load the unlock date and time for LINDE from DB.
			final long temp = info.getLong("respawn_time") - System.currentTimeMillis();
			// If LINDE is locked until a certain time, mark it so and start the unlock timer the unlock time has not yet expired.
			if (temp > 0)
			{
				startQuestTimer("LINDE_unlock", temp, null, null);
			}
			else
			{
				// The time has already expired while the server was offline. Immediately spawn LINDE.
				final GrandBoss linel = (GrandBoss) addSpawn(LINDE, 153576, 142072, -12739, 0, false, 0);
				GrandBossManager.getInstance().setStatus(LINDE, ALIVE);
				spawnBoss(linel);
			}
		}
		else
		{
			final boolean test = GlobalVariablesManager.getInstance().getBoolean("linde_Attacked", false);
			if (test)
			{
				_firstAttacked = true;
			}
			final int loc_x = info.getInt("loc_x");
			final int loc_y = info.getInt("loc_y");
			final int loc_z = info.getInt("loc_z");
			final int heading = info.getInt("heading");
			final double hp = info.getDouble("currentHP");
			final double mp = info.getDouble("currentMP");
			final GrandBoss linel = (GrandBoss) addSpawn(LINDE, loc_x, loc_y, loc_z, heading, false, 0);
			linel.setCurrentHpMp(hp, mp);
			spawnBoss(linel);
		}
	}
	
	@Override
	public void onSave()
	{
		GlobalVariablesManager.getInstance().set("linde_Attacked", _firstAttacked);
	}
	
	public void spawnBoss(GrandBoss npc)
	{
		GrandBossManager.getInstance().addBoss(npc);
		npc.broadcastPacket(new PlaySound(1, "BS01_A", 1, npc.getObjectId(), npc.getX(), npc.getY(), npc.getZ()));
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		if (event.equalsIgnoreCase("LINDE_unlock"))
		{
			final GrandBoss linel = (GrandBoss) addSpawn(LINDE, 153576, 142072, -12739, 0, false, 0);
			GrandBossManager.getInstance().setStatus(LINDE, ALIVE);
			spawnBoss(linel);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(Npc npc, Player attacker, int damage, boolean isSummon)
	{
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		if (npc.getId() == LINDE)
		{
			final int objId = npc.getObjectId();
			npc.broadcastPacket(new PlaySound(1, "BS02_D", 1, objId, npc.getX(), npc.getY(), npc.getZ()));
			_firstAttacked = false;
			GrandBossManager.getInstance().setStatus(LINDE, DEAD);
			// Calculate Min and Max respawn times randomly.
			final long respawnTime = (6 + getRandom(-1, 1)) * 3600000;
			startQuestTimer("LINDE_unlock", respawnTime, null, null);
			// Also save the respawn time so that the info is maintained past reboots.
			final StatSet info = GrandBossManager.getInstance().getStatSet(LINDE);
			info.set("respawn_time", System.currentTimeMillis() + respawnTime);
			GrandBossManager.getInstance().setStatSet(LINDE, info);
			startQuestTimer("despawn_minions", 20000, null, null);
			cancelQuestTimers("spawn_minion");
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		if (npc.getId() == LINDE)
		{
			npc.setImmobilized(true);
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new BEL();
	}
}
