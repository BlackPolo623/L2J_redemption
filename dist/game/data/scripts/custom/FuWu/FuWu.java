/**
 * Jacky 制作,商业化专用脚本
 */

package custom.FuWu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.l2jmobius.Config;
import org.l2jmobius.commons.database.DatabaseFactory;
import org.l2jmobius.gameserver.data.xml.MultisellData;
import org.l2jmobius.gameserver.data.xml.SkillData;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.Elementals;
import org.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.InventoryUpdate;
import org.l2jmobius.gameserver.network.serverpackets.MagicSkillUse;
import org.l2jmobius.gameserver.network.serverpackets.UserInfo;
import org.l2jmobius.gameserver.network.Disconnection;
import org.l2jmobius.gameserver.model.holders.SubClassHolder;
import org.l2jmobius.gameserver.network.serverpackets.LeaveWorld;
import org.l2jmobius.commons.util.Rnd;
import ai.AbstractNpcAI;

// VIP 金币建议ＧＭ出售比例１：１００　 

public class FuWu extends AbstractNpcAI {
	
	private final static String qn = "FuWu";
	private final static int npcIds = 33333;
	private static Map<Integer, Map<Integer, Integer>> mapskills = new HashMap<Integer, Map<Integer, Integer>>();
	private static Date openServerDate = null; // 用来保存开区时间
	private static String fileName = "C:\\新区装备领取码1.txt";
	private static String[] msg = { "定向精炼", "元素强化包", "300会员金币", "+10礼包", "+15凭证3个", "+20凭证3个", "+25凭证3个", "毁灭首饰包", "毁灭武器包", "3个珍惜宝石", "10个稀有宝石", "1个英雄凭证", "1个顶级礼包", "10万钢铁碎片", "+30凭证", "永久死神凭证",
			"毁灭凭证礼包" };

	static {
		System.out.println("INFO 载入全能叮当猫系统");
		init();
	}

	private static void init() {
		Map<Integer, Integer> map88 = new HashMap<Integer, Integer>();
		mapskills.put(88, map88);
		map88.put(1, 730);
		map88.put(5, 730);
		map88.put(6, 730);
		map88.put(7, 730);
		map88.put(440, 115);
		map88.put(359, 115);
		map88.put(360, 115);
		map88.put(261, 730);
		map88.put(9, 730);
		map88.put(328, 315);
		map88.put(144, 230);
		map88.put(297, 230);
		map88.put(231, 230);
		map88.put(227, 230);
		map88.put(430, 215);
		map88.put(329, 215);
		map88.put(440, 115);
		map88.put(424, 130);
		map88.put(100, 130);
		map88.put(260, 130);
		map88.put(290, 130);
		map88.put(312, 130);
		map88.put(190, 130);
		map88.put(212, 130);
		map88.put(256, 130);
		Map<Integer, Integer> map89 = new HashMap<Integer, Integer>();
		mapskills.put(89, map89);
		map89.put(328, 315);
		map89.put(361, 315);
		map89.put(211, 230);
		map89.put(121, 130);
		map89.put(227, 130);
		map89.put(312, 130);
		map89.put(231, 130);
		map89.put(212, 130);
		map89.put(290, 130);
		map89.put(320, 130);
		map89.put(452, 130);
		map89.put(130, 130);
		map89.put(216, 130);
		map89.put(116, 130);
		map89.put(36, 730);
		map89.put(48, 130);
		map89.put(424, 130);
		map89.put(422, 130);
		map89.put(421, 130);
		map89.put(256, 130);
		map89.put(359, 115);
		map89.put(440, 115);
		map89.put(430, 115);
		map89.put(347, 115);
		map89.put(329, 115);
		map89.put(339, 115);
		map89.put(360, 115);
		map89.put(100, 130);
		Map<Integer, Integer> map90 = new HashMap<Integer, Integer>();
		mapskills.put(90, map90);
		map90.put(92, 730);
		map90.put(110, 330);
		map90.put(328, 315);
		map90.put(112, 230);
		map90.put(404, 230);
		map90.put(400, 230);
		map90.put(217, 230);
		map90.put(72, 230);
		map90.put(232, 230);
		map90.put(403, 230);
		map90.put(28, 230);
		map90.put(18, 230);
		map90.put(341, 215);
		map90.put(329, 215);
		map90.put(368, 215);
		map90.put(528, 215);
		map90.put(322, 130);
		map90.put(291, 130);
		map90.put(262, 130);
		map90.put(147, 130);
		map90.put(69, 130);
		map90.put(318, 130);
		map90.put(406, 130);
		map90.put(153, 130);
		map90.put(82, 130);
		map90.put(335, 115);
		map90.put(353, 115);
		map90.put(429, 115);
		map90.put(191, 130);
		map90.put(49, 130);
		map90.put(97, 130);
		map90.put(92, 130);
		map90.put(405, 230);
		Map<Integer, Integer> map91 = new HashMap<Integer, Integer>();
		mapskills.put(91, map91);
		map91.put(110, 330);
		map91.put(28, 330);
		map91.put(401, 330);
		map91.put(328, 315);
		map91.put(112, 230);
		map91.put(403, 230);
		map91.put(217, 230);
		map91.put(72, 230);
		map91.put(291, 230);
		map91.put(18, 230);
		map91.put(368, 215);
		map91.put(329, 215);
		map91.put(342, 215);
		map91.put(528, 215);
		map91.put(283, 130);
		map91.put(70, 130);
		map91.put(153, 130);
		map91.put(147, 130);
		map91.put(46, 130);
		map91.put(318, 130);
		map91.put(82, 130);
		map91.put(322, 130);
		map91.put(92, 130);
		map91.put(127, 130);
		map91.put(335, 115);
		map91.put(232, 130);
		map91.put(429, 115);
		map91.put(353, 115);
		map91.put(86, 230);
		map91.put(450, 230);
		map91.put(191, 130);
		Map<Integer, Integer> map92 = new HashMap<Integer, Integer>();
		mapskills.put(92, map92);
		map92.put(343, 715);
		map92.put(24, 130);
		map92.put(19, 130);
		map92.put(313, 230);
		map92.put(354, 215);
		map92.put(101, 130);
		map92.put(169, 130);
		map92.put(208, 130);
		map92.put(233, 130);
		map92.put(312, 130);
		map92.put(328, 315);
		map92.put(431, 115);
		map92.put(256, 130);
		map92.put(303, 130);
		map92.put(131, 130);
		map92.put(99, 130);
		map92.put(417, 130);
		Map<Integer, Integer> map93 = new HashMap<Integer, Integer>();
		mapskills.put(93, map93);
		map93.put(344, 715);
		map93.put(409, 730);
		map93.put(30, 730);
		map93.put(263, 730);
		map93.put(96, 330);
		map93.put(209, 330);
		map93.put(328, 315);
		map93.put(233, 230);
		map93.put(411, 230);
		map93.put(357, 215);
		map93.put(356, 215);
		map93.put(432, 215);
		map93.put(198, 130);
		map93.put(312, 130);
		map93.put(169, 130);
		map93.put(111, 130);
		map93.put(168, 130);
		map93.put(221, 115);
		map93.put(358, 115);
		map93.put(412, 230);
		map93.put(51, 130);
		map93.put(256, 130);
		Map<Integer, Integer> map94 = new HashMap<Integer, Integer>();
		mapskills.put(94, map94);
		map94.put(234, 330);
		map94.put(1074, 330);
		map94.put(1083, 330);
		map94.put(1160, 330);
		map94.put(1171, 330);
		map94.put(1232, 330);
		map94.put(1275, 330);
		map94.put(1288, 330);
		map94.put(1289, 330);
		map94.put(1292, 330);
		map94.put(1296, 330);
		map94.put(328, 315);
		map94.put(1339, 315);
		map94.put(1451, 315);
		map94.put(1452, 315);
		map94.put(1417, 230);
		map94.put(146, 130);
		map94.put(228, 130);
		map94.put(1069, 130);
		map94.put(1072, 130);
		map94.put(1169, 130);
		map94.put(1230, 130);
		map94.put(1231, 130);
		map94.put(337, 115);
		map94.put(433, 115);
		map94.put(1338, 115);
		map94.put(229, 130);
		map94.put(285, 130);
		map94.put(213, 130);
		map94.put(1338, 115);
		map94.put(1078, 230);
		Map<Integer, Integer> map95 = new HashMap<Integer, Integer>();
		mapskills.put(95, map95);
		map95.put(1167, 330);
		map95.put(1343, 315);
		map95.put(1382, 230);
		map95.put(1381, 230);
		map95.put(329, 215);
		map95.put(1269, 130);
		map95.put(1298, 130);
		map95.put(1263, 130);
		map95.put(1262, 130);
		map95.put(1334, 130);
		map95.put(1234, 130);
		map95.put(1222, 130);
		map95.put(1154, 130);
		map95.put(1069, 130);
		map95.put(1129, 130);
		map95.put(1170, 130);
		map95.put(1148, 130);
		map95.put(1169, 130);
		map95.put(1159, 130);
		map95.put(1164, 130);
		map95.put(1064, 130);
		map95.put(1155, 130);
		map95.put(234, 130);
		map95.put(228, 130);
		map95.put(146, 130);
		map95.put(1336, 115);
		map95.put(328, 315);
		map95.put(1337, 115);
		map95.put(337, 115);
		map95.put(434, 115);
		map95.put(1151, 130);
		map95.put(1157, 130);
		map95.put(229, 130);
		map95.put(213, 130);
		map95.put(285, 130);
		Map<Integer, Integer> map96 = new HashMap<Integer, Integer>();
		mapskills.put(96, map96);
		map96.put(1386, 330);
		map96.put(328, 315);
		map96.put(1383, 330);
		map96.put(10, 130);
		map96.put(1328, 130);
		map96.put(1127, 130);
		map96.put(228, 130);
		map96.put(1331, 130);
		map96.put(258, 130);
		map96.put(1299, 130);
		map96.put(234, 130);
		map96.put(1279, 130);
		map96.put(1276, 130);
		map96.put(146, 130);
		map96.put(1262, 130);
		map96.put(1225, 130);
		map96.put(338, 115);
		map96.put(435, 115);
		map96.put(1406, 115);
		map96.put(1144, 130);
		map96.put(1141, 130);
		map96.put(1111, 130);
		map96.put(1139, 130);
		map96.put(1126, 130);
		map96.put(229, 130);
		map96.put(213, 130);
		map96.put(1346, 115);
		map96.put(1349, 115);
		Map<Integer, Integer> map97 = new HashMap<Integer, Integer>();
		mapskills.put(97, map97);
		map97.put(1182, 330);
		map97.put(1189, 330);
		map97.put(1191, 330);
		map97.put(235, 330);
		map97.put(1259, 330);
		map97.put(1357, 315);
		map97.put(1356, 315);
		map97.put(1355, 315);
		map97.put(259, 230);
		map97.put(1394, 230);
		map97.put(329, 215);
		map97.put(1393, 130);
		map97.put(1219, 130);
		map97.put(1206, 130);
		map97.put(1201, 130);
		map97.put(1218, 130);
		map97.put(1539, 130);
		map97.put(236, 130);
		map97.put(146, 130);
		map97.put(1402, 130);
		map97.put(1401, 130);
		map97.put(228, 130);
		map97.put(1392, 130);
		map97.put(1258, 130);
		map97.put(328, 315);
		map97.put(1359, 115);
		map97.put(1358, 115);
		map97.put(1360, 115);
		map97.put(1361, 115);
		map97.put(1396, 330);
		map97.put(1459, 115);
		map97.put(1217, 130);
		map97.put(1034, 130);
		map97.put(1400, 130);
		map97.put(1204, 130);
		map97.put(1020, 130);
		map97.put(1069, 130);
		map97.put(1028, 130);
		map97.put(1043, 130);
		map97.put(1399, 130);
		map97.put(1042, 130);
		map97.put(1254, 130);
		map97.put(1395, 130);
		map97.put(229, 130);
		map97.put(1311, 130);
		map97.put(213, 130);
		map97.put(1353, 115);
		map97.put(1307, 130);
		map97.put(1398, 130);
		map97.put(436, 115);
		map97.put(336, 115);
		Map<Integer, Integer> map98 = new HashMap<Integer, Integer>();
		mapskills.put(98, map98);
		map98.put(1182, 330);
		map98.put(1189, 330);
		map98.put(1191, 330);
		map98.put(235, 330);
		map98.put(1259, 330);
		map98.put(1357, 315);
		map98.put(1356, 315);
		map98.put(1355, 315);
		map98.put(259, 230);
		map98.put(1394, 230);
		map98.put(329, 215);
		map98.put(1393, 130);
		map98.put(1219, 130);
		map98.put(1206, 130);
		map98.put(1201, 130);
		map98.put(1218, 130);
		map98.put(1539, 130);
		map98.put(236, 130);
		map98.put(146, 130);
		map98.put(1402, 130);
		map98.put(1401, 130);
		map98.put(228, 130);
		map98.put(1392, 130);
		map98.put(1258, 130);
		map98.put(328, 315);
		map98.put(1359, 115);
		map98.put(1358, 115);
		map98.put(1360, 115);
		map98.put(1361, 115);
		map98.put(1396, 330);
		map98.put(1459, 115);
		map98.put(1217, 130);
		map98.put(1034, 130);
		map98.put(1400, 130);
		map98.put(1204, 130);
		map98.put(1020, 130);
		map98.put(1069, 130);
		map98.put(1028, 130);
		map98.put(1043, 130);
		map98.put(1399, 130);
		map98.put(1042, 130);
		map98.put(1254, 130);
		map98.put(1395, 130);
		map98.put(229, 130);
		map98.put(1311, 130);
		map98.put(213, 130);
		map98.put(1353, 130);
		map98.put(1307, 130);
		map98.put(1398, 130);
		map98.put(436, 115);
		map98.put(336, 115);
		Map<Integer, Integer> map99 = new HashMap<Integer, Integer>();
		mapskills.put(99, map99);
		map99.put(110, 330);
		map99.put(18, 330);
		map99.put(28, 330);
		map99.put(230, 330);
		map99.put(328, 315);
		map99.put(112, 230);
		map99.put(400, 230);
		map99.put(402, 230);
		map99.put(217, 230);
		map99.put(291, 230);
		map99.put(329, 215);
		map99.put(341, 215);
		map99.put(368, 215);
		map99.put(528, 215);
		map99.put(232, 130);
		map99.put(288, 130);
		map99.put(153, 130);
		map99.put(147, 130);
		map99.put(322, 130);
		map99.put(10, 130);
		map99.put(58, 130);
		map99.put(449, 130);
		map99.put(102, 130);
		map99.put(107, 130);
		map99.put(67, 130);
		map99.put(429, 115);
		map99.put(335, 115);
		map99.put(352, 115);
		map99.put(123, 230);
		map99.put(191, 130);
		Map<Integer, Integer> map100 = new HashMap<Integer, Integer>();
		mapskills.put(100, map100);
		map100.put(70, 330);
		map100.put(105, 330);
		map100.put(230, 330);
		map100.put(765, 115);
		map100.put(328, 315);
		map100.put(123, 230);
		map100.put(217, 230);
		map100.put(144, 230);
		map100.put(402, 230);
		map100.put(367, 215);
		map100.put(329, 215);
		map100.put(58, 130);
		map100.put(122, 130);
		map100.put(147, 130);
		map100.put(191, 130);
		map100.put(102, 130);
		map100.put(223, 130);
		map100.put(115, 130);
		map100.put(84, 130);
		map100.put(428, 115);
		map100.put(437, 115);
		map100.put(272, 130);
		map100.put(274, 130);
		map100.put(277, 130);
		map100.put(270, 130);
		map100.put(271, 130);
		map100.put(276, 130);
		map100.put(273, 130);
		map100.put(275, 130);
		map100.put(304, 130);
		map100.put(364, 115);
		map100.put(363, 115);
		map100.put(349, 115);
		map100.put(311, 130);
		map100.put(309, 130);
		map100.put(308, 130);
		map100.put(307, 130);
		map100.put(366, 115);
		map100.put(268, 130);
		map100.put(305, 130);
		map100.put(365, 115);
		map100.put(269, 130);
		map100.put(306, 130);
		map100.put(764, 115);
		map100.put(530, 115);
		map100.put(529, 115);
		map100.put(310, 130);
		map100.put(264, 130);
		map100.put(265, 130);
		map100.put(266, 130);
		map100.put(267, 130);
		Map<Integer, Integer> map101 = new HashMap<Integer, Integer>();
		mapskills.put(101, map101);
		map101.put(263, 730);
		map101.put(30, 730);
		map101.put(221, 130);
		map101.put(344, 715);
		map101.put(209, 330);
		map101.put(230, 330);
		map101.put(96, 330);
		map101.put(111, 230);
		map101.put(233, 230);
		map101.put(355, 215);
		map101.put(356, 215);
		map101.put(432, 215);
		map101.put(412, 230);
		map101.put(410, 130);
		map101.put(137, 130);
		map101.put(321, 130);
		map101.put(102, 130);
		map101.put(169, 130);
		map101.put(312, 130);
		map101.put(58, 130);
		map101.put(198, 130);
		map101.put(328, 315);
		map101.put(358, 115);
		map101.put(123, 130);
		map101.put(51, 130);
		map101.put(256, 130);
		Map<Integer, Integer> map102 = new HashMap<Integer, Integer>();
		mapskills.put(102, map102);
		map102.put(369, 815);
		map102.put(19, 730);
		map102.put(343, 715);
		map102.put(230, 330);
		map102.put(328, 315);
		map102.put(208, 230);
		map102.put(354, 215);
		map102.put(169, 130);
		map102.put(233, 130);
		map102.put(102, 130);
		map102.put(101, 130);
		map102.put(413, 130);
		map102.put(58, 130);
		map102.put(24, 130);
		map102.put(312, 115);
		map102.put(431, 115);
		map102.put(99, 130);
		map102.put(303, 130);
		map102.put(123, 130);
		Map<Integer, Integer> map103 = new HashMap<Integer, Integer>();
		mapskills.put(103, map103);
		map103.put(1288, 330);
		map103.put(1236, 330);
		map103.put(234, 330);
		map103.put(1182, 330);
		map103.put(1275, 330);
		map103.put(1293, 330);
		map103.put(1237, 315);
		map103.put(328, 315);
		map103.put(1342, 315);
		map103.put(1238, 230);
		map103.put(1417, 230);
		map103.put(1169, 130);
		map103.put(1290, 130);
		map103.put(146, 130);
		map103.put(1295, 130);
		map103.put(1223, 130);
		map103.put(1265, 130);
		map103.put(1235, 130);
		map103.put(1047, 130);
		map103.put(1174, 130);
		map103.put(213, 130);
		map103.put(1071, 130);
		map103.put(1072, 130);
		map103.put(285, 130);
		map103.put(228, 130);
		map103.put(229, 130);
		map103.put(1164, 130);
		map103.put(1069, 130);
		map103.put(1231, 130);
		map103.put(1338, 115);
		map103.put(433, 115);
		map103.put(1455, 115);
		map103.put(337, 115);
		map103.put(1340, 115);
		map103.put(1453, 115);
		map103.put(1454, 115);
		Map<Integer, Integer> map105 = new HashMap<Integer, Integer>();
		mapskills.put(105, map105);
		map105.put(1182, 330);
		map105.put(1189, 330);
		map105.put(1191, 330);
		map105.put(235, 330);
		map105.put(1259, 330);
		map105.put(1357, 315);
		map105.put(1356, 315);
		map105.put(1355, 315);
		map105.put(259, 230);
		map105.put(1394, 230);
		map105.put(329, 215);
		map105.put(1393, 130);
		map105.put(1219, 130);
		map105.put(1206, 130);
		map105.put(1201, 130);
		map105.put(1218, 130);
		map105.put(1539, 130);
		map105.put(236, 130);
		map105.put(146, 130);
		map105.put(1402, 130);
		map105.put(1401, 130);
		map105.put(228, 130);
		map105.put(1392, 130);
		map105.put(1258, 130);
		map105.put(328, 315);
		map105.put(1359, 115);
		map105.put(1358, 115);
		map105.put(1360, 115);
		map105.put(1361, 115);
		map105.put(1396, 330);
		map105.put(1459, 115);
		map105.put(1217, 130);
		map105.put(1034, 130);
		map105.put(1400, 130);
		map105.put(1204, 130);
		map105.put(1020, 130);
		map105.put(1069, 130);
		map105.put(1028, 130);
		map105.put(1043, 130);
		map105.put(1399, 130);
		map105.put(1042, 130);
		map105.put(1254, 130);
		map105.put(1395, 130);
		map105.put(229, 130);
		map105.put(1311, 130);
		map105.put(213, 130);
		map105.put(1353, 130);
		map105.put(1307, 130);
		map105.put(1398, 130);
		map105.put(436, 115);
		map105.put(336, 115);
		Map<Integer, Integer> map106 = new HashMap<Integer, Integer>();
		mapskills.put(106, map106);
		map106.put(105, 330);
		map106.put(122, 330);
		map106.put(110, 330);
		map106.put(279, 330);
		map106.put(70, 330);
		map106.put(450, 330);
		map106.put(328, 315);
		map106.put(28, 230);
		map106.put(112, 230);
		map106.put(402, 230);
		map106.put(18, 230);
		map106.put(528, 215);
		map106.put(342, 215);
		map106.put(368, 215);
		map106.put(232, 130);
		map106.put(217, 130);
		map106.put(278, 130);
		map106.put(223, 130);
		map106.put(288, 130);
		map106.put(289, 130);
		map106.put(291, 130);
		map106.put(335, 130);
		map106.put(401, 130);
		map106.put(322, 130);
		map106.put(191, 130);
		map106.put(153, 130);
		map106.put(147, 130);
		map106.put(22, 130);
		map106.put(33, 130);
		map106.put(115, 130);
		map106.put(429, 115);
		map106.put(329, 115);
		map106.put(352, 115);
		map106.put(335, 115);
		Map<Integer, Integer> map107 = new HashMap<Integer, Integer>();
		mapskills.put(107, map107);
		map107.put(70, 330);
		map107.put(105, 330);
		map107.put(230, 330);
		map107.put(765, 115);
		map107.put(328, 315);
		map107.put(123, 230);
		map107.put(217, 230);
		map107.put(144, 230);
		map107.put(402, 230);
		map107.put(367, 215);
		map107.put(329, 215);
		map107.put(58, 130);
		map107.put(122, 130);
		map107.put(147, 130);
		map107.put(191, 130);
		map107.put(102, 130);
		map107.put(223, 130);
		map107.put(115, 130);
		map107.put(84, 130);
		map107.put(428, 115);
		map107.put(437, 115);
		map107.put(272, 130);
		map107.put(274, 130);
		map107.put(277, 130);
		map107.put(270, 130);
		map107.put(271, 130);
		map107.put(276, 130);
		map107.put(273, 130);
		map107.put(275, 130);
		map107.put(304, 130);
		map107.put(364, 115);
		map107.put(363, 115);
		map107.put(349, 115);
		map107.put(311, 130);
		map107.put(309, 130);
		map107.put(308, 130);
		map107.put(307, 130);
		map107.put(366, 115);
		map107.put(268, 130);
		map107.put(305, 130);
		map107.put(365, 115);
		map107.put(269, 130);
		map107.put(306, 130);
		map107.put(764, 115);
		map107.put(530, 115);
		map107.put(529, 115);
		map107.put(310, 130);
		map107.put(264, 130);
		map107.put(265, 130);
		map107.put(266, 130);
		map107.put(267, 130);
		Map<Integer, Integer> map108 = new HashMap<Integer, Integer>();
		mapskills.put(108, map108);
		map108.put(263, 730);
		map108.put(30, 730);
		map108.put(344, 715);
		map108.put(70, 130);
		map108.put(105, 330);
		map108.put(209, 330);
		map108.put(96, 330);
		map108.put(111, 230);
		map108.put(233, 230);
		map108.put(412, 230);
		map108.put(355, 215);
		map108.put(432, 215);
		map108.put(321, 130);
		map108.put(193, 130);
		map108.put(198, 130);
		map108.put(221, 130);
		map108.put(223, 130);
		map108.put(410, 130);
		map108.put(312, 130);
		map108.put(169, 130);
		map108.put(122, 130);
		map108.put(115, 130);
		map108.put(328, 115);
		map108.put(358, 115);
		map108.put(357, 115);
		map108.put(51, 130);
		map108.put(256, 130);
		Map<Integer, Integer> map109 = new HashMap<Integer, Integer>();
		mapskills.put(109, map109);
		map109.put(369, 815);
		map109.put(314, 730);
		map109.put(19, 730);
		map109.put(343, 715);
		map109.put(105, 330);
		map109.put(208, 230);
		map109.put(354, 215);
		map109.put(223, 130);
		map109.put(414, 130);
		map109.put(122, 130);
		map109.put(115, 130);
		map109.put(233, 130);
		map109.put(169, 130);
		map109.put(70, 130);
		map109.put(101, 130);
		map109.put(328, 315);
		map109.put(312, 130);
		map109.put(431, 115);
		map109.put(99, 130);
		map109.put(417, 130);
		map109.put(256, 130);
		map109.put(303, 130);
		Map<Integer, Integer> map110 = new HashMap<Integer, Integer>();
		mapskills.put(110, map110);
		map110.put(1288, 330);
		map110.put(1074, 330);
		map110.put(1294, 330);
		map110.put(328, 315);
		map110.put(1343, 315);
		map110.put(1341, 315);
		map110.put(1417, 230);
		map110.put(329, 215);
		map110.put(1234, 130);
		map110.put(1267, 130);
		map110.put(1291, 130);
		map110.put(1224, 130);
		map110.put(1069, 130);
		map110.put(1222, 130);
		map110.put(1169, 130);
		map110.put(1064, 130);
		map110.put(1239, 130);
		map110.put(146, 130);
		map110.put(1176, 130);
		map110.put(1160, 130);
		map110.put(1148, 130);
		map110.put(234, 130);
		map110.put(228, 130);
		map110.put(1338, 115);
		map110.put(1456, 115);
		map110.put(433, 115);
		map110.put(1458, 115);
		map110.put(337, 115);
		map110.put(1151, 130);
		map110.put(1157, 130);
		map110.put(1159, 130);
		map110.put(213, 130);
		map110.put(285, 130);
		map110.put(229, 130);
		map110.put(1167, 330);
		Map<Integer, Integer> map112 = new HashMap<Integer, Integer>();
		mapskills.put(112, map112);
		map112.put(1182, 330);
		map112.put(1189, 330);
		map112.put(1191, 330);
		map112.put(235, 330);
		map112.put(1259, 330);
		map112.put(1357, 315);
		map112.put(1356, 315);
		map112.put(1355, 315);
		map112.put(259, 230);
		map112.put(1394, 230);
		map112.put(329, 215);
		map112.put(1393, 130);
		map112.put(1219, 130);
		map112.put(1206, 130);
		map112.put(1201, 130);
		map112.put(1218, 130);
		map112.put(1539, 130);
		map112.put(236, 130);
		map112.put(146, 130);
		map112.put(1402, 130);
		map112.put(1401, 130);
		map112.put(228, 130);
		map112.put(1392, 130);
		map112.put(1258, 130);
		map112.put(328, 315);
		map112.put(1359, 115);
		map112.put(1358, 115);
		map112.put(1360, 115);
		map112.put(1361, 115);
		map112.put(1396, 330);
		map112.put(1459, 115);
		map112.put(1217, 130);
		map112.put(1034, 130);
		map112.put(1400, 130);
		map112.put(1204, 130);
		map112.put(1020, 130);
		map112.put(1069, 130);
		map112.put(1028, 130);
		map112.put(1043, 130);
		map112.put(1399, 130);
		map112.put(1042, 130);
		map112.put(1254, 130);
		map112.put(1395, 130);
		map112.put(229, 130);
		map112.put(1311, 130);
		map112.put(213, 130);
		map112.put(1353, 130);
		map112.put(1307, 130);
		map112.put(1398, 130);
		map112.put(436, 115);
		map112.put(336, 115);
		Map<Integer, Integer> map113 = new HashMap<Integer, Integer>();
		mapskills.put(113, map113);
		map113.put(315, 730);
		map113.put(36, 730);
		map113.put(362, 315);
		map113.put(420, 230);
		map113.put(94, 130);
		map113.put(329, 215);
		map113.put(320, 130);
		map113.put(216, 130);
		map113.put(227, 130);
		map113.put(231, 130);
		map113.put(260, 130);
		map113.put(293, 130);
		map113.put(424, 130);
		map113.put(422, 130);
		map113.put(212, 130);
		map113.put(211, 130);
		map113.put(121, 130);
		map113.put(190, 130);
		map113.put(100, 130);
		map113.put(312, 130);
		map113.put(440, 115);
		map113.put(339, 115);
		map113.put(134, 115);
		map113.put(335, 115);
		map113.put(328, 115);
		map113.put(347, 115);
		map113.put(430, 115);
		map113.put(256, 130);
		Map<Integer, Integer> map114 = new HashMap<Integer, Integer>();
		mapskills.put(114, map114);
		map114.put(17, 730);
		map114.put(35, 730);
		map114.put(284, 730);
		map114.put(54, 730);
		map114.put(420, 230);
		map114.put(329, 215);
		map114.put(430, 215);
		map114.put(281, 130);
		map114.put(233, 130);
		map114.put(319, 130);
		map114.put(210, 130);
		map114.put(168, 130);
		map114.put(424, 130);
		map114.put(280, 130);
		map114.put(95, 130);
		map114.put(81, 130);
		map114.put(328, 115);
		map114.put(134, 115);
		map114.put(335, 115);
		Map<Integer, Integer> map115 = new HashMap<Integer, Integer>();
		mapskills.put(115, map115);
		map115.put(134, 115);
		map115.put(146, 130);
		map115.put(213, 130);
		map115.put(228, 130);
		map115.put(229, 130);
		map115.put(251, 130);
		map115.put(252, 130);
		map115.put(253, 130);
		map115.put(328, 115);
		map115.put(329, 215);
		map115.put(336, 115);
		map115.put(337, 115);
		map115.put(436, 115);
		map115.put(1004, 130);
		map115.put(1005, 130);
		map115.put(1006, 130);
		map115.put(1007, 130);
		map115.put(1008, 130);
		map115.put(1009, 130);
		map115.put(1010, 130);
		map115.put(1003, 130);
		map115.put(1002, 130);
		map115.put(1001, 130);
		map115.put(1092, 130);
		map115.put(1099, 130);
		map115.put(1097, 130);
		map115.put(1096, 130);
		map115.put(1104, 130);
		map115.put(1208, 130);
		map115.put(1246, 130);
		map115.put(1256, 130);
		map115.put(1229, 130);
		map115.put(1247, 130);
		map115.put(1248, 130);
		map115.put(1249, 130);
		map115.put(1250, 130);
		map115.put(1282, 130);
		map115.put(1283, 130);
		map115.put(1284, 130);
		map115.put(1261, 130);
		map115.put(1251, 130);
		map115.put(1245, 130);
		map115.put(1252, 130);
		map115.put(1253, 130);
		map115.put(1414, 315);
		map115.put(1305, 130);
		map115.put(1306, 130);
		map115.put(1260, 130);
		map115.put(1390, 130);
		map115.put(1391, 130);
		map115.put(1310, 130);
		map115.put(1309, 130);
		map115.put(1308, 130);
		map115.put(1362, 115);
		map115.put(1363, 315);
		map115.put(1364, 115);
		map115.put(1365, 115);
		map115.put(1366, 115);
		map115.put(1367, 115);
		map115.put(1416, 115);
		map115.put(1461, 115);
		map115.put(1462, 115);
		map115.put(1415, 115);
		map115.put(1413, 115);
		Map<Integer, Integer> map116 = new HashMap<Integer, Integer>();
		mapskills.put(116, map116);
		map116.put(1363, 315);
		map116.put(1414, 315);
		map116.put(329, 215);
		map116.put(1245, 130);
		map116.put(1010, 130);
		map116.put(1009, 130);
		map116.put(1008, 130);
		map116.put(1007, 130);
		map116.put(1006, 130);
		map116.put(1005, 130);
		map116.put(1003, 130);
		map116.put(1002, 130);
		map116.put(1001, 130);
		map116.put(1282, 130);
		map116.put(1283, 130);
		map116.put(1284, 130);
		map116.put(1261, 130);
		map116.put(1092, 130);
		map116.put(1256, 130);
		map116.put(1246, 130);
		map116.put(1229, 130);
		map116.put(1247, 130);
		map116.put(1248, 130);
		map116.put(1208, 130);
		map116.put(1249, 130);
		map116.put(1250, 130);
		map116.put(1104, 130);
		map116.put(1251, 130);
		map116.put(1252, 130);
		map116.put(1253, 130);
		map116.put(1099, 130);
		map116.put(1097, 130);
		map116.put(1096, 130);
		map116.put(1305, 130);
		map116.put(1306, 130);
		map116.put(251, 130);
		map116.put(1260, 130);
		map116.put(1390, 130);
		map116.put(229, 130);
		map116.put(228, 130);
		map116.put(1391, 130);
		map116.put(213, 130);
		map116.put(252, 130);
		map116.put(146, 130);
		map116.put(253, 130);
		map116.put(1310, 130);
		map116.put(1004, 130);
		map116.put(1309, 130);
		map116.put(1308, 130);
		map116.put(1362, 115);
		map116.put(1416, 115);
		map116.put(1461, 115);
		map116.put(134, 115);
		map116.put(337, 115);
		map116.put(1462, 115);
		map116.put(1415, 115);
		map116.put(1364, 115);
		map116.put(336, 115);
		map116.put(1367, 115);
		map116.put(328, 115);
		map116.put(1413, 115);
		map116.put(436, 115);
		map116.put(1366, 115);
		map116.put(1365, 115);
		Map<Integer, Integer> map131 = new HashMap<Integer, Integer>();
		mapskills.put(131, map131);
		map131.put(493, 130);
		map131.put(477, 130);
		map131.put(495, 130);
		map131.put(496, 130);
		map131.put(793, 815);
		map131.put(497, 730);
		map131.put(494, 230);
		map131.put(526, 415);
		map131.put(626, 230);
		map131.put(1514, 230);
		map131.put(466, 130);
		map131.put(501, 130);
		map131.put(500, 130);
		map131.put(485, 130);
		map131.put(328, 315);
		map131.put(329, 215);
		map131.put(625, 130);
		map131.put(492, 130);
		map131.put(335, 115);
		map131.put(498, 130);
		map131.put(502, 130);
		map131.put(472, 130);
		map131.put(465, 130);
		map131.put(1444, 130);
		map131.put(483, 115);
		map131.put(794, 115);
		Map<Integer, Integer> map132 = new HashMap<Integer, Integer>();
		mapskills.put(132, map132);
		map132.put(504, 730);
		map132.put(505, 730);
		map132.put(1438, 430);
		map132.put(1436, 430);
		map132.put(506, 430);
		map132.put(1512, 415);
		map132.put(1469, 415);
		map132.put(1437, 330);
		map132.put(1513, 315);
		map132.put(328, 315);
		map132.put(1444, 230);
		map132.put(625, 230);
		map132.put(626, 230);
		map132.put(1435, 230);
		map132.put(1529, 230);
		map132.put(1445, 230);
		map132.put(1511, 230);
		map132.put(1448, 230);
		map132.put(465, 230);
		map132.put(474, 230);
		map132.put(1516, 215);
		map132.put(1515, 215);
		map132.put(329, 215);
		map132.put(502, 130);
		map132.put(1446, 130);
		map132.put(466, 130);
		map132.put(1474, 130);
		map132.put(1447, 130);
		map132.put(1442, 130);
		map132.put(1443, 130);
		map132.put(492, 130);
		Map<Integer, Integer> map133 = new HashMap<Integer, Integer>();
		mapskills.put(133, map133);
		map133.put(504, 730);
		map133.put(505, 730);
		map133.put(1438, 430);
		map133.put(1436, 430);
		map133.put(506, 430);
		map133.put(1512, 415);
		map133.put(1469, 415);
		map133.put(1437, 330);
		map133.put(1513, 315);
		map133.put(328, 315);
		map133.put(1444, 230);
		map133.put(625, 230);
		map133.put(626, 230);
		map133.put(1435, 230);
		map133.put(1529, 230);
		map133.put(1445, 230);
		map133.put(1511, 230);
		map133.put(1448, 230);
		map133.put(465, 230);
		map133.put(474, 230);
		map133.put(1516, 215);
		map133.put(1515, 215);
		map133.put(329, 215);
		map133.put(502, 130);
		map133.put(1446, 130);
		map133.put(466, 130);
		map133.put(1474, 130);
		map133.put(1447, 130);
		map133.put(1442, 130);
		map133.put(1443, 130);
		map133.put(492, 130);
		Map<Integer, Integer> map134 = new HashMap<Integer, Integer>();
		mapskills.put(134, map134);
		map134.put(502, 130);
		map134.put(625, 230);
		map134.put(507, 730);
		map134.put(508, 730);
		map134.put(627, 430);
		map134.put(1470, 315);
		map134.put(514, 230);
		map134.put(515, 230);
		map134.put(473, 230);
		map134.put(525, 230);
		map134.put(626, 230);
		map134.put(329, 215);
		map134.put(1514, 130);
		map134.put(509, 130);
		map134.put(510, 130);
		map134.put(521, 130);
		map134.put(516, 130);
		map134.put(517, 130);
		map134.put(522, 130);
		map134.put(1444, 130);
		map134.put(466, 130);
		map134.put(465, 130);
		map134.put(518, 130);
		map134.put(328, 115);
		map134.put(490, 130);
	}

	
	private FuWu() {
		addStartNpc(npcIds);
		addTalkId(npcIds);
		addFirstTalkId(npcIds);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player) {
		String htmltext = "";
		NpcHtmlMessage rateReply = new NpcHtmlMessage(0,1);
		rateReply.setFile(player, "data/scripts/custom/FuWu/main.htm");
		if (event.startsWith("upSkills")) {
			upSkills(event, npc, player);
		}
		if (event.startsWith("editBaseClass")) {
			editBaseClass(event, npc, player);
		}
		if (event.startsWith("editClassOverlord")) {
			if (player.getClassId().getId() == 116) {
				player.sendSkillList();
				player.setClassId(115);
				player.store(false);
				player.broadcastUserInfo();
				player.sendPacket(new ExShowScreenMessage("变更完成,请小退游戏生效", 3000));
			} else {
				player.sendPacket(new ExShowScreenMessage("只有三转职业'毁灭使者'才可变更为霸主", 3000));
			}
			return htmltext;
		}
		if (event.startsWith("eBC")) {
			if (player.isSubClassActive()) {
				player.sendPacket(new ExShowScreenMessage("您必须切换到主职业才可使用此服务", 3000));
				return htmltext;
			}
			if (player.getBaseClass() > 118) {
				player.sendPacket(new ExShowScreenMessage("暗天使系和梦魇系不可使用此服务", 3000));
				return htmltext;
			}
			rateReply = new NpcHtmlMessage(0,1);
			rateReply.setFile(player, "data/scripts/custom/FuWu/editBaseClass.htm");
			player.sendPacket(rateReply);
			return htmltext;
		}
		if (event.startsWith("veditElem ")) {
			rateReply = new NpcHtmlMessage(0,1);
			VeditElem(event, npc, player);
			return htmltext;
		}
		if (event.startsWith("vaddElems")) {
			rateReply = new NpcHtmlMessage(0,1);
			vaddElems(event, npc, player);
			return htmltext;
		}
		return htmltext;
	}

	private void upSkills(String event, Npc npc, Player player) {
		int ITEMID = 14679;
		int ITEMCOUNT = 3;
		if (player.getInventory().getInventoryItemCount(ITEMID, 0) >= ITEMCOUNT) {
			int count = 0;
			if (player.getClassId().getId() < 88) {
				player.sendPacket(new ExShowScreenMessage("您还未三转,无法强化,请先三转.", 3000));
				return;
			}
			if (player.getLevel() < 83) {
				player.sendPacket(new ExShowScreenMessage("您必须达到83级,并且三转才可以使用该功能.", 3000));
				return;
			}
			int classid = player.getClassId().getId();
			Map<Integer, Integer> map = mapskills.get(classid);
			if (map == null) {
				player.sendPacket(new ExShowScreenMessage("您当前的职业无法进行强化,请联系管理员.", 3000));
				return;
			}
			Set<Integer> st = map.keySet();
			for (Iterator<Integer> it = st.iterator(); it.hasNext();) {
				int sid = it.next();
				Skill skill = SkillData.getInstance().getSkill(sid, map.get(sid));
				player.addSkill(skill, true);
				count++;
			}
			if (count > 0) {
				player.sendSkillList();
				player.store(false);
				player.broadcastUserInfo();
				player.destroyItemByItemId(qn, ITEMID, ITEMCOUNT, npc, true);
				player.sendPacket(new ExShowScreenMessage("一共强化了您当前职业的" + count + "个技能,重新打开技能查看", 3000));
			} else {
				player.sendPacket(new ExShowScreenMessage("强化职业异常,请联系管理员.未收取您的珍惜宝石", 3000));
			}
		} else {
			player.sendPacket(new ExShowScreenMessage("一键强技能需"+ITEMCOUNT+"个珍惜宝石.", 3000));
			return;
		}
		return;
	}

	private void editBaseClass(String event, Npc npc, Player player) {
		int ITEMID = 38001;
		int ITEMCOUNT = 10000;
		if (player.getInventory().getInventoryItemCount(ITEMID, 0) >= ITEMCOUNT) {
			int newBaseClass = 0;
			String newBC = event.substring(14);
			if (newBC.startsWith("角斗士")) {
				newBaseClass = 88;
			}
			if (newBC.startsWith("勇士")) {
				newBaseClass = 89;
			}
			if (newBC.startsWith("神骑士")) {
				newBaseClass = 90;
			}
			if (newBC.startsWith("死亡骑士")) {
				newBaseClass = 91;
			}
			if (newBC.startsWith("神射手")) {
				newBaseClass = 92;
			}
			if (newBC.startsWith("冒险家")) {
				newBaseClass = 93;
			}
			if (newBC.startsWith("大法师")) {
				newBaseClass = 94;
			}
			if (newBC.startsWith("灵魂捕获者")) {
				newBaseClass = 95;
			}
			if (newBC.startsWith("秘术之王")) {
				newBaseClass = 96;
			}
			if (newBC.startsWith("大祭司")) {
				newBaseClass = 98;
			}
			if (newBC.startsWith("伊娃圣殿骑士")) {
				newBaseClass = 99;
			}
			if (newBC.startsWith("吟游剑士")) {
				newBaseClass = 100;
			}
			if (newBC.startsWith("风骑士")) {
				newBaseClass = 101;
			}
			if (newBC.startsWith("月光守护者")) {
				newBaseClass = 102;
			}
			if (newBC.startsWith("神圣诗人")) {
				newBaseClass = 103;
			}
			if (newBC.startsWith("元素大师")) {
				newBaseClass = 104;
			}
			if (newBC.startsWith("席琳圣骑士")) {
				newBaseClass = 106;
			}
			if (newBC.startsWith("幽灵舞者")) {
				newBaseClass = 107;
			}
			if (newBC.startsWith("幽灵猎人")) {
				newBaseClass = 108;
			}
			if (newBC.startsWith("幽灵守护者")) {
				newBaseClass = 109;
			}
			if (newBC.startsWith("暴风狂啸者")) {
				newBaseClass = 110;
			}
			if (newBC.startsWith("幽灵大师")) {
				newBaseClass = 111;
			}
			if (newBC.startsWith("巨人")) {
				newBaseClass = 113;
			}
			if (newBC.startsWith("武道家")) {
				newBaseClass = 114;
			}
			if (newBC.startsWith("统治者")) {
				newBaseClass = 115;
			}
			if (newBC.startsWith("探索者")) {
				newBaseClass = 117;
			}
			if (newBC.startsWith("巨匠")) {
				newBaseClass = 118;
			}
			if (newBaseClass == 0) {
				player.sendMessage("错误,请联系管理员!");
				return;
			}
			Map<Integer, SubClassHolder> sbClass = player.getSubClasses();
			for (SubClassHolder sc : sbClass.values()) {
				if (newBaseClass == sc.getClassId()) {
					player.sendPacket(new ExShowScreenMessage("您选择的职业为您的副职业,无法使用", 3000));
					return;
				}
			}
			/*
			 * if (newBaseClass >= 99 && newBaseClass <= 104) { for (SubClass sc
			 * : sbClass.values()) { if (sc.getClassId() >= 106 &&
			 * sc.getClassId() <= 111 || sc.getClassId() >= 31 &&
			 * sc.getClassId() <= 43) { player.sendPacket(new
			 * ExShowScreenMessage( "副职业有黑精灵,无法转换成白精灵", 3000)); return; } } } if
			 * (newBaseClass >= 106 && newBaseClass <= 111) { for (SubClass sc :
			 * sbClass.values()) { if (sc.getClassId() >= 99 && sc.getClassId()
			 * <= 104 || sc.getClassId() >= 20 && sc.getClassId() <= 30) {
			 * player.sendPacket(new ExShowScreenMessage( "副职业有白精灵,无法转换成黑精灵",
			 * 3000)); return; } } }
			 */
			if (newBaseClass == player.getBaseClass()) {
				player.sendPacket(new ExShowScreenMessage("这不正是你当前的主职业吗?", 3000));
				return;
			}
			player.destroyItemByItemId(qn, ITEMID, ITEMCOUNT, npc, true);
			player.sendPacket(new ExShowScreenMessage("恭喜!职业变更完成,请重新登陆", 3000));
			for (Skill skill : player.getAllSkills())
			{
				player.removeSkill(skill);
			}
			player.sendSkillList();
			player.setClassId(newBaseClass);
			player.setBaseClass(newBaseClass);
			player.store(false);
			player.broadcastUserInfo();
			Disconnection.of(player).defaultSequence(LeaveWorld.STATIC_PACKET);
		} else {
			player.sendPacket(new ExShowScreenMessage("变更主职业需要" + ITEMCOUNT + "个碎片", 3000));
		}
	}

	private void VeditElem(String event, Npc npc, Player player) {
		int ITEMID = 38001;
		int ITEMCOUNT = 0;
		Item item = player.getInventory().getPaperdollItem(5);
		if (player.getInventory().getInventoryItemCount(ITEMID, 0) >= ITEMCOUNT) {
			Elementals[] eles = null;
			try {
				eles = item.getElementals();
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请将武器拿手上", 3000));
				return;
			}
			byte elemType = -1;
			if (event.contains("火")) {
				elemType = 0;
			}
			if (event.contains("水")) {
				elemType = 1;
			}
			if (event.contains("风")) {
				elemType = 2;
			}
			if (event.contains("地")) {
				elemType = 3;
			}
			if (event.contains("神圣")) {
				elemType = 4;
			}
			if (event.contains("黑暗")) {
				elemType = 5;
			}
			if (elemType < 0) {
				elemType = 0;
			}
			try {
				if (eles == null) {
					if (player.getInventory().getInventoryItemCount(30051, 0) < 1) {
						player.sendPacket(new ExShowScreenMessage("你的武器没有任何属性,无法修改\n添加属性需要消耗1个武器属性强化凭证", 3000));
						return;
					}
					player.destroyItemByItemId(qn, 30051, 1, npc, true);
					item.setElementAttr(elemType, 450);
					// 取下武器
					player.getInventory().unEquipItemInSlotAndRecord(5);
					// 装上武器
					player.getInventory().equipItem(item);
					// 更新数据库
					InventoryUpdate iu = new InventoryUpdate();
					iu.addModifiedItem(item);
					player.sendPacket(iu);
					player.broadcastUserInfo();
					player.sendPacket(new ExShowScreenMessage("武器属性添加成功", 3000));
					return;
				} else {
					int oldValue = item.getElementals()[0].getValue();
					byte type = item.getElementals()[0].getElement();
					// 取下武器
					player.getInventory().unEquipItemInSlotAndRecord(5);
					item.clearElementAttr(type);
					Connection conn = DatabaseFactory.getConnection();
					PreparedStatement ps = conn.prepareStatement("delete from item_elementals where itemid = ?");
					ps.setInt(1, item.getId());
					ps.executeUpdate();
					if (ps != null) {
						ps.close();
					}
					if (conn != null) {
						conn.close();
					}
					// 修重新添加武器属性
					item.setElementAttr(elemType, oldValue);
					// 装上武器
					player.getInventory().equipItem(item);
					// 更新数据库
					InventoryUpdate iu = new InventoryUpdate();
					iu.addModifiedItem(item);
					player.sendPacket(iu);
					player.broadcastUserInfo();
					player.sendPacket(new ExShowScreenMessage("武器属性修改属性成功", 3000));
				}
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请求出现异常,请联系管理员!", 3000));
			}
		} else {
			player.sendPacket(new ExShowScreenMessage("修改武器属性需要" + ITEMCOUNT + "钢铁碎片", 3000));
		}
	}

	private void vaddElems(String event, Npc npc, Player player) {
		int ITEMID = 30052;
		int ITEMCOUNT = 1;
		int[] amors = new int[5];
		if (player.getInventory().getInventoryItemCount(ITEMID, 0) >= ITEMCOUNT) {
			try {
				if (player.getInventory().getPaperdollItem(1).getElementals() != null) {
					player.sendPacket(new ExShowScreenMessage("检查到您的头盔有属性,请先洗掉", 3000));
					return;
				}
				amors[0] = player.getInventory().getPaperdollItem(1).getObjectId();
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请将要强化的头盔穿身上", 3000));
				return;
			}
			try {
				if (player.getInventory().getPaperdollItem(6).getElementals() != null) {
					player.sendPacket(new ExShowScreenMessage("检查到您的上衣有属性,请先洗掉", 3000));
					return;
				}
				amors[1] = player.getInventory().getPaperdollItem(6).getObjectId();
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请将要强化的上衣穿身上", 3000));
				return;
			}
			try {
				if (player.getInventory().getPaperdollItem(10).getElementals() != null) {
					player.sendPacket(new ExShowScreenMessage("检查到您的手套有属性,请先洗掉", 3000));
					return;
				}
				amors[2] = player.getInventory().getPaperdollItem(10).getObjectId();
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请将要强化的手套穿身上", 3000));
				return;
			}
			try {
				if (player.getInventory().getPaperdollItem(11).getElementals() != null) {
					player.sendPacket(new ExShowScreenMessage("检查到您的裤子有属性,请先洗掉", 3000));
					return;
				}
				amors[3] = player.getInventory().getPaperdollItem(11).getObjectId();
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请将要强化的裤子穿身上", 3000));
				return;
			}
			try {
				if (player.getInventory().getPaperdollItem(12).getElementals() != null) {
					player.sendPacket(new ExShowScreenMessage("检查到您的手套有属性,请先洗掉", 3000));
					return;
				}
				amors[4] = player.getInventory().getPaperdollItem(12).getObjectId();
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请将要强化的鞋子穿身上", 3000));
				return;
			}
			Item item = null;
			// 强化防具属性
			item = player.getInventory().getPaperdollItem(1);
			item.setElementAttr((byte) 0, 180);
			item.setElementAttr((byte) 3, 180);
			item.setElementAttr((byte) 5, 180);
			// 取下
			player.getInventory().unEquipItemInSlotAndRecord(1);
			// 装上
			player.getInventory().equipItem(item);

			item = player.getInventory().getPaperdollItem(6);
			item.setElementAttr((byte) 0, 180);
			item.setElementAttr((byte) 3, 180);
			item.setElementAttr((byte) 5, 180);
			// 取下
			player.getInventory().unEquipItemInSlotAndRecord(6);
			// 装上
			player.getInventory().equipItem(item);

			item = player.getInventory().getPaperdollItem(10);
			item.setElementAttr((byte) 1, 180);
			item.setElementAttr((byte) 2, 180);
			item.setElementAttr((byte) 4, 180);
			// 取下
			player.getInventory().unEquipItemInSlotAndRecord(10);
			// 装上
			player.getInventory().equipItem(item);

			item = player.getInventory().getPaperdollItem(11);
			item.setElementAttr((byte) 0, 180);
			item.setElementAttr((byte) 3, 180);
			item.setElementAttr((byte) 5, 180);
			// 取下
			player.getInventory().unEquipItemInSlotAndRecord(11);
			// 装上
			player.getInventory().equipItem(item);

			item = player.getInventory().getPaperdollItem(12);
			item.setElementAttr((byte) 1, 180);
			item.setElementAttr((byte) 2, 180);
			item.setElementAttr((byte) 4, 180);
			// 取下
			player.getInventory().unEquipItemInSlotAndRecord(12);
			// 装上
			player.getInventory().equipItem(item);

			player.sendPacket(new ExShowScreenMessage("元素添加成功.", 3000));
			player.destroyItemByItemId(qn, ITEMID, ITEMCOUNT, npc, true);
		} else {
			player.sendPacket(new ExShowScreenMessage("添加属性需要" + ITEMCOUNT + "个防具属性强化凭证", 3000));
		}
	}

	private void addAttri(String event, Npc npc, Player player) {
		int ITEMID = 16178;
		int ITEMCOUNT = 1;
		if (player.getInventory().getInventoryItemCount(ITEMID, 0) >= ITEMCOUNT) {
			int objectId = 0;
			try {
				objectId = player.getInventory().getPaperdollItem(5).getObjectId();
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请将武器拿手上", 3000));
				return;
			}
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String attributeName = event.substring(9);
			int attriId = -1;
			if (attributeName.contains("被动PVP")) {
				attriId = 0;
			}
			if (attributeName.contains("被动还击")) {
				attriId = 1;
			}
			if (attributeName.contains("被动狂热")) {
				attriId = 2;
			}
			if (attributeName.contains("被动敏捷")) {
				attriId = 3;
			}
			if (attributeName.contains("+1Str")) {
				attriId = 4;
			}
			if (attributeName.contains("被动祈祷")) {
				attriId = 5;
			}
			if (attributeName.contains("无属性攻击")) {
				attriId = 6;
			}
			String sqlStr = "select augAttributes from item_attributes where itemId = ?";
			try {
				conn = DatabaseFactory.getConnection();
				ps = conn.prepareStatement(sqlStr);
				ps.setInt(1, objectId);
				rs = ps.executeQuery();
				if (rs.next()) {
					if (rs.getInt(1) > 0) {
						player.sendPacket(new ExShowScreenMessage("精炼过的武器无法再次精炼,请先解除当前精炼", 3000));
						return;
					}
				}
				sqlStr = "insert into item_attributes values(?,?)";
				ps = conn.prepareStatement(sqlStr);
				ps.setInt(1, objectId);
				switch (attriId) {
				case 0:
					ps.setInt(2, 1067258295);
					break;
				case 1:
					ps.setInt(2, 1070469532);
					break;
				case 2:
					ps.setInt(2, 1070599714);
					break;
				case 3:
					ps.setInt(2, 1070334854);
					break;
				case 4:
					ps.setInt(2, 1070930977);
					break;
				case 5:
					ps.setInt(2, 1066926987);
					break;
				case 6:
					ps.setInt(2, 1062015413);
					break;
				default:
					player.sendPacket(new ExShowScreenMessage("请求出现异常,请退出外挂再使用!", 3000));
					return;
				}
				if (ps.executeUpdate() > 0) {
					player.sendPacket(new ExShowScreenMessage("武器精炼成功,请小退游戏生效", 3000));
				}
				player.destroyItemByItemId(qn, ITEMID, ITEMCOUNT, npc, true);
			} catch (Exception e) {
				player.sendPacket(new ExShowScreenMessage("请求出现异常,请联系管理员!", 3000));
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (ps != null)
						ps.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					System.out.println(e.toString());
				}
			}
		} else {
			player.sendPacket(new ExShowScreenMessage("修改武器属性需要" + ITEMCOUNT + "个定向精炼石,可许愿获得", 3000));
		}
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player) {
		return "main.htm";
	}	

	public static void main(String[] args) {
		new FuWu();
		System.out.println("装备强化载入");
	}
}