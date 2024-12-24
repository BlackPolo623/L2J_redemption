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
package handlers.effecthandlers;

import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Playable;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.conditions.Condition;
import org.l2jmobius.gameserver.model.effects.AbstractEffect;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.skill.BuffInfo;
import org.l2jmobius.gameserver.model.skill.Skill;


/**
 * @author Mobius
 */
public class ReplaceSkillBySkill extends AbstractEffect
{
	private final SkillHolder _existingSkill;
	private final SkillHolder _replacementSkill;
	
	public ReplaceSkillBySkill(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
		_existingSkill = new SkillHolder(params.getInt("existingSkillId"), params.getInt("existingSkillLevel", 0));
		_replacementSkill = new SkillHolder(params.getInt("replacementSkillId"), params.getInt("replacementSkillLevel", 0));
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		final Playable playable = (Playable) info.getEffected();
		final Skill knownSkill = playable.getKnownSkill(_existingSkill.getSkillId());
		if ((knownSkill == null) || (knownSkill.getLevel() < _existingSkill.getSkillLevel()))
		{
			return;
		}
		
		final Skill addedSkill = SkillData.getInstance().getSkill(_replacementSkill.getSkillId(), knownSkill.getLevel());
		if (playable.isPlayer())
		{
			final Player player =  info.getEffected().getActingPlayer();
			player.addSkill(addedSkill, true);	
			player.removeSkill(knownSkill, true);
			player.sendSkillList();
		}
	}

	@Override	
	public void onExit(BuffInfo info)
	{
		final Playable playable = (Playable) info.getEffected();
		final int existingSkillId = _existingSkill.getSkillId();
		
		final Skill knownSkill = playable.getKnownSkill(_replacementSkill.getSkillId());
		if (knownSkill == null)
		{
			info.getEffected().getActingPlayer().sendMessage("Jineng weikong");
			return;
		}
		
		final Skill addedSkill = SkillData.getInstance().getSkill(existingSkillId, knownSkill.getLevel());
		if (playable.isPlayer())
		{
			final Player player =  info.getEffected().getActingPlayer();
			player.addSkill(addedSkill, true);	
			player.removeSkill(knownSkill, true);
			player.sendSkillList();
		}
	}
}
