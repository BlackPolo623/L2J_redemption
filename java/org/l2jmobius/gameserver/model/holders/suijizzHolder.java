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
package org.l2jmobius.gameserver.model.holders;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.l2jmobius.gameserver.model.skill.Skill;

/**
 * @author Mobius
 */
public class suijizzHolder
{
	private int _id;
	private int _count;
	private int _chane;
	
	public suijizzHolder(int id,int count,int chane)
	{
		_id = id;
		_count = count;
		_chane = chane;
	}
	
	public int getId()
	{
		return _id;
	}

	public int getcount()
	{
		return _count;
	}
	
	public int getchane()
	{
		return _chane;
	}
	
}
