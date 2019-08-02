package org.cloud.framework.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 创建数据库字段工具类
 */
public class DBUtils {

	// 表名称和表字段禁用词

	// 公用字段
	public static String[] commonFields = new String[] { "create_time#20",
			"create_user_id#11", "update_time#20", "update_user_id#11",
			"submit_time#20", "submit_user_id#11","status#2",
			"version#5", "database_id#11", "data_source#10", "workflow_node_id#11","audit_user_id#11" ,"workflow_node_type#11"};

	public static void main(String[] args) {

//		HashMap<String, String> fields = new HashMap<String, String>();
//		fields.put("username", "string/500");

//		 String sql = DBUtils.createTable("mysql", "hytable_person"); // 创建表
//		 System.out.println(sql);
		 
		// 删除表 @
//		 String sql = DBUtils.updateTablename("mysql", "hytable_person",
//		 "hytable_person_123"); //更新表
//		 String sql2 = DBUtils.updateName("mysql", "hytable_person_123", "add",
//		 fields); // 新增字段
		// String sql = DBUtils.updateName("mysql", "hytable_person_123",
		// "delete", fields); // 删除字段

		String sql_full_index = DBUtils.addFullIndex("mysql", "bs_xwzx","content");
		System.out.println(sql_full_index);

	}

	/**
	 * 根据数据库类型得到需要操作的列名的拼接
	 * 
	 * @param dbType
	 *            数据库类型
	 * @param fields
	 *            属性集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String getPropertyTypes(String dbType,
			HashMap<String, String> fields) {
		StringBuffer sql = new StringBuffer("");
		Iterator iter = fields.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			String[] vals = val.toString().split("/");
			String ptype = vals[0];
			String num = vals[1];
			if ("id".equals(key.toString())) { // 主键不参与拼接
				sql.append("");
			} else {
				if ("string".equals(ptype)) {
					if ("oracle".equals(dbType)) { // 判断是否是oracle数据库
						sql.append("" + key.toString() + " varchar2("
								+ Integer.parseInt(num) + "),");
					} else if ("mysql".equals(dbType)
							|| "sqlserver".equals(dbType)) { // MySQL数据库或者SqlServer数据库
						sql.append(" `" + key.toString() + "` varchar("
								+ Integer.parseInt(num) + ") DEFAULT '' ,");
					} else {
						return null;
					}
				} else if ("int".equals(ptype)) {
					if ("oracle".equals(dbType)) { // 判断是否是oracle数据库
						sql.append("" + key.toString() + " number("
								+ Integer.parseInt(num) + "),");
					} else if ("mysql".equals(dbType)) { // MySQL数据库
						sql.append("" + key.toString() + " int("
								+ Integer.parseInt(num) + "),");
					} else if ("sqlserver".equals(dbType)) { // SqlServer数据库
						sql.append("" + key.toString() + " int,");
					} else {
						return null;
					}
				} else if ("date".equals(ptype)) {
					sql.append(" `" + key.toString() + "` " + ptype + ",");
				} else if ("text".equals(ptype)) { // 文本类型
					if ("oracle".equals(dbType)) { // 判断是否是oracle数据库
						sql.append("" + key.toString() + " CLOB("
								+ Integer.parseInt(num) + "),");
					} else if ("mysql".equals(dbType)) { // MySQL数据库
						sql.append(" `" + key.toString() + "` longtext,");
					} else if ("sqlserver".equals(dbType)) { // SqlServer数据库{
						sql.append("" + key.toString() + " TEXT("
								+ Integer.parseInt(num) + "),");
					} else {
						return null;
					}
				}
			}
		}
		return sql.toString();
	}

	// CREATE TABLE USERS (ID NUMBER(2) NOT NULL PRIMARY KEY,AGE NUMBER(10),NAME
	// VARCHAR2(10) DEFAULT NULL,CREATE_TIME DATE DEFAULT NULL,PASSWORD
	// VARCHAR2(10) DEFAULT NULL);
	/**
	 * 根据数据库类型创建表
	 * 
	 * @param dbType
	 *            数据库类型 【MySQL(mysql),oracle(oracle),SqlServer(sqlserver)】
	 * @param tablename
	 *            表名
	 * @return sql 创建表的sql语句
	 */
	public static String createTable(String dbType, String tablename) {
		StringBuffer sql = new StringBuffer("");
		sql.append("CREATE TABLE `" + tablename + "` (");
		// 主键为id,int型
		if ("mysql".equals(dbType)) { // mysql数据库
			sql.append("`id` varchar(50) NOT NULL,");
			// sql.append("id int auto_increment primary key not null,");
		} else if ("oracle".equals(dbType)) { // oracle数据库
			sql.append("ID varchar2(50) NOT NULL PRIMARY KEY,");
		} else if ("sqlserver".equals(dbType)) { // sqlserver数据库
			sql.append("id varchar(50) primary key not null,");
			// sql.append("id int identity(1,1) primary key not null,");
		} else {
			return null;
		}
		HashMap<String, String> fields = new HashMap<String, String>();
		for (int i = 0; i < commonFields.length; i++) {
			fields.put(commonFields[i].split("#")[0], "string/" + commonFields[i].split("#")[1]);
		}
		String sq = getPropertyTypes(dbType, fields);
		sql.append(sq);
		sql.append(" PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8; ");
		return sql.toString();
	}
	
	/**
	 * 根据数据库类型删除数据表操作
	 * 
	 * @param dbType
	 *            数据库类型 【MySQL(mysql),oracle(oracle),SqlServer(sqlserver)】
	 * @param tablename
	 *            表名称
	 * @return sql
	 */
	public static String deleteTable(String dbType, String tablename) {
		// 三种表的删除语句都相同
		String sql = "drop table " + tablename + ";";
		return sql;
	}

	/**
	 * 根据数据库类型修改数据库表名操作
	 * 
	 * @param dbType
	 *            数据库类型 【MySQL(mysql),oracle(oracle),SqlServer(sqlserver)】
	 * @param old_tablename
	 *            旧表名称
	 * @param new_tablename
	 *            新表名称
	 * @return sql
	 */
	public static String updateTablename(String dbType, String old_tablename,
			String new_tablename) {
		String sql = "";
		if ("mysql".equals(dbType)) { // 数据库为MySQL
			sql = "RENAME TABLE " + old_tablename + " TO " + new_tablename
					+ ";";
		}
		if ("oracle".equals(dbType)) { // 数据库为oracle
			sql = "alter table " + old_tablename + " rename to "
					+ new_tablename + ";";
		}
		if ("sqlserver".equals(dbType)) { // 数据库为sql server
			sql = "EXEC sp_rename " + old_tablename + ", " + new_tablename
					+ ";";
		}
		return sql;
	}

	/**
	 * 删除表字段时，拼接要删除的字段
	 * 
	 * @param fields
	 *            字段集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String getPropertys(HashMap<String, String> fields) {
		StringBuffer sql = new StringBuffer("");
		Iterator iter = fields.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
//			Object val = entry.getValue();
//			String[] vals = val.toString().split("/");
//			String ptype = vals[0];
//			String num = vals[1];

			sql.append("" + key.toString() + ",");
		}
		String sql2 = sql.toString();
		return sql2.substring(0, sql2.length() - 1);
	}

	/**
	 * 得到mysql数据库和SqlServer数据库删除多列的拼接字符串
	 * 
	 * @param dbType
	 *            表类型
	 * @param fields
	 *            字段集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String getvarchars(String dbType,
			HashMap<String, String> fields) {
		StringBuffer sql = new StringBuffer("");
		Iterator iter = fields.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
//			Object val = entry.getValue();
//			String[] vals = val.toString().split("/");
//			String ptype = vals[0];
//			String num = vals[1];

			if ("mysql".equals(dbType)) {
				sql.append(" DROP " + key.toString() + ",");
			}
			if ("sqlserver".equals(dbType)) {
				sql.append(" column " + key.toString() + ",");
			}
			if ("oracle".equals(dbType)) {
				return null;
			}
		}
		String sql2 = sql.toString();
		return sql2.substring(0, sql2.length() - 1);
	}

	/**
	 * 更新数据库表的普通字段名操作
	 * 
	 * @param dbType
	 *            数据库类型 【MySQL(mysql),oracle(oracle),SqlServer(sqlserver)】
	 * @param tablename
	 *            表名
	 * @param op
	 *            操作类型，【新增(add)，删除(delete)】
	 * @param fields
	 *            表的字段集合
	 * @return sql
	 */
	public static String updateName(String dbType, String tablename, String op,
			HashMap<String, String> fields) {
		StringBuffer sql = new StringBuffer();
		String ps = getPropertyTypes(dbType, fields);
		ps = ps.substring(0, ps.length()-1);
		String pp = getPropertys(fields);
		String ss = getvarchars(dbType, fields);
		if ("mysql".equals(dbType)) { // 数据库为MySQL
			if ("add".equals(op)) { // 增加字段
				sql.append("ALTER TABLE " + tablename + "  " + op + " column("
						+ ps + ");");
			}
			if ("delete".equals(op)) { // 删除字段
				sql.append("ALTER TABLE " + tablename + " " + ss + ";");
			}
		}
		if ("oracle".equals(dbType)) { // 数据库为oracle
			if ("add".equals(op)) { // 增加字段和修改字段
				sql.append("ALTER TABLE " + tablename + "  " + op + " (" + ps
						+ ");");
			}
			if ("delete".equals(op)) { // 删除字段
				sql.append("ALTER TABLE " + tablename + "  drop (" + pp + ");");
			}
		}
		if ("sqlserver".equals(dbType)) { // 数据库为sql server
			if ("add".equals(op)) { // 增加字段
				sql.append("ALTER TABLE " + tablename + "  ADD " + ps + ";");
			}
			if ("delete".equals(op)) { // 删除字段
				sql.append("ALTER TABLE " + tablename + "  drop " + ss + ";");
			}
		}
		return sql.toString();
	}

	/**
	 * 修改表的字段名
	 * 
	 * @param dbType
	 *            数据库类型
	 * @param tablename
	 *            表名称
	 * @param old_name
	 *            旧字段名
	 * @param new_name
	 *            新字段名
	 * @return
	 */
	public static String modifyName(String dbType, String tablename,
			String old_name, String new_name) {
		StringBuffer sql = new StringBuffer();
		if ("mysql".equals(dbType)) { // 数据库为MySQL
			sql.append("ALTER TABLE " + tablename + " change column "
					+ old_name + " " + new_name + " " + ";");
		}
		if ("oracle".equals(dbType)) { // 数据库为oracle
			sql.append("ALTER TABLE " + tablename + " RENAME COLUMN "
					+ old_name + " TO " + new_name + ";");
		}
		if ("sqlserver".equals(dbType)) { // 数据库为sql server
			// EXEC sp_rename 'user1.[nnn]', 'namename', 'COLUMN'
			sql.append("EXEC sp_rename " + tablename + ".[" + old_name + "],'"
					+ new_name + "','COLUMN';");
		}
		return sql.toString();
	}

	/**
	 * 更新索引字段操作
	 * 
	 * @param dbType
	 *            数据库类型
	 * @param tablename
	 *            表名称
	 * @param op
	 *            操作类型【新增(add),删除(delete)】
	 * @param indexType
	 *            索引类型 【普通索引(nomalIndex),主键索引(uniqueIndex)】
	 * @param indexname
	 *            所以名称
	 * @param fields
	 *            需要操作的列名称集合
	 * @return sql
	 */
	public static String updateIndex(String dbType, String tablename,
			String op, String indexType, String indexname,
			HashMap<String, String> fields) {
		StringBuffer sql = new StringBuffer();
		String pp = getPropertys(fields);
		if ("mysql".equals(dbType)) { // 数据库为MySQL
			if ("add".equals(op)) { // 增加索引
				if ("uniqueIndex".equals(indexType)) { // 主键索引
					sql.append("ALTER TABLE " + tablename
							+ "  ADD PRIMARY KEY " + indexname + " (" + pp
							+ ");");
				}
				if ("nomalIndex".equals(indexType)) { // 普通索引
					sql.append("ALTER TABLE " + tablename + "  ADD INDEX "
							+ indexname + " (" + pp + ");");
				}
			}
			if ("delete".equals(op)) { // 删除索引
				sql.append("ALTER TABLE " + tablename + "  DROP INDEX "
						+ indexname + ";");
			}
		}
		if ("oracle".equals(dbType)) { // 数据库为oracle
			if ("add".equals(op)) { // 增加索引
				if ("uniqueIndex".equals(indexType)) { // 主键索引
					sql.append("ALTER TABLE " + tablename + "  ADD CONSTRAINT "
							+ indexname + " primary key(" + pp
							+ ") using index;");
				}
				if ("nomalIndex".equals(indexType)) { // 普通索引
					sql.append("CREATE INDEX " + indexname + "  ON "
							+ tablename + " (" + pp + " DESC);");
				}
			}
			if ("delete".equals(op)) { // 删除索引
				sql.append("DROP INDEX " + indexname + ";");
			}
		}
		if ("sqlserver".equals(dbType)) { // 数据库为sql server
			if ("add".equals(op)) { // 增加索引
				if ("uniqueIndex".equals(indexType)) { // 主键索引
					sql.append("ALTER TABLE " + tablename
							+ " add primary key (" + pp + ");");
				}
				if ("nomalIndex".equals(indexType)) { // 普通索引
					sql.append("CREATE INDEX " + indexname + "  ON "
							+ tablename + " (" + pp + ")");
				}
			}
			if ("delete".equals(op)) { // 删除索引
				sql.append("DROP INDEX " + tablename + "." + indexname + ";");
			}
		}
		return sql.toString();
	}

	/**
	 * 构建全文索引
	 * @param dbType
	 * @param tablename
	 * @param field
	 * @return
	 */
	public static String addFullIndex(String dbType, String tablename,
									 String field) {
		StringBuffer sql = new StringBuffer();

		if ("mysql".equals(dbType)) { // 数据库为MySQL
			sql.append("ALTER TABLE " + tablename + " ADD FULLTEXT ft_" + tablename + "_"
					+ field + "(" + field + ") with parser ngram;");
		}

		return sql.toString();
	}

}