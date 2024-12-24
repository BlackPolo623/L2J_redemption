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
package org.l2jmobius.gameserver.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import org.l2jmobius.commons.util.IXmlReader;
import org.l2jmobius.gameserver.model.StatSet;
import org.l2jmobius.gameserver.model.holders.suijizzHolder;
import org.l2jmobius.gameserver.model.skill.Skill;

/**
 * @author Mobius
 */
public class suijizz implements IXmlReader
{
	private static final Logger LOGGER = Logger.getLogger(suijizz.class.getName());
	
	private static final Map<Integer, suijizzHolder> Item_JC = new HashMap<>();
	
	private static final List<suijizzHolder> suijizzlist = new ArrayList<>();
	
	
	protected suijizz()
	{
		load();
	}
	
	@Override
	public void load()
	{
		suijizzlist.clear();
		parseDatapackFile("data/suijizz.xml");
		LOGGER.info("随机制作: 读取 " + suijizzlist.size() + " 个物品制作数据.");
	}
	
	@Override
	public void parseDocument(Document doc, File f)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "suijizz", itemjiacNode ->
		{
			final StatSet set = new StatSet(parseAttributes(itemjiacNode));
			
			final int id = set.getInt("id");
			final int count = set.getInt("count");
			final int chane = set.getInt("chane");
			suijizzlist.add(new suijizzHolder(id, count,chane));
		}));
	}
	
	public List<suijizzHolder> getjclists()
	{
		return suijizzlist;		
	}
	
	public static suijizz getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final suijizz INSTANCE = new suijizz();
	}
}
