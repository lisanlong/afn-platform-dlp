package org.cloud.framework.utils;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*import org.bson.Document;*/
/*
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
*/

//
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.MongoIterable;

public class JdbcUtils {
	private static Connection conn=null;;
	

	public static Connection getConnection(DruidDataSource dss) throws Exception{
		if(conn==null){
			conn=dss.getConnection();
		}
		return conn;
	}
	static IdWorker aIdWorker = new IdWorker(2);
	/**
	 * 执行sql语句
	 * @param conn
	 * @param
	 * @return
	 */
	public static ResultSet executeSql(Connection conn,String sql){
		if(conn == null){
			return null;
		}
		ResultSet rs = null;
		Statement st = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * 查询sql语句
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static ResultSet searchSql(Connection conn,String tableName){
		if(conn == null){
			return null;
		}
		ResultSet rs = null;
		Statement st = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM " + tableName);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * 返回List< Map<String,String>>形式的结果
	 * @param rs
	 * @return
	 */
	public static List<Map<String, Object>> getResult(ResultSet rs){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		try {
			while(rs.next()){
				ResultSetMetaData rsmd = rs.getMetaData(); 
				map = new HashMap<String,Object>();
				for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getString(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  list;
	}
	public static List<Map<String, Object>> getListMapData(Connection conn,String sql){
		if(conn == null){
			return null;
		}
		ResultSet rs = null;
		Statement st = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				ResultSetMetaData rsmd = rs.getMetaData();
				map = new HashMap<String,Object>();
				for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getString(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(st);
		}
		return  list;
	}
	/**
	 * 关闭
	 * @param auto
	 */
	public static void close(AutoCloseable auto) {
		if (auto != null)
			try {
				auto.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	/**
	 * 批插入
	 * @param conn
	 * @throws SQLException 
	 */
	public static void batchInsert(Connection conn,List<String>sqls) throws SQLException{
		conn.setAutoCommit(false); 
		Statement st =conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  ResultSet.CONCUR_READ_ONLY);   
		for(int i = 0; i < sqls.size(); i++){  
			try {
				st.execute(sqls.get(i));
				//st.addBatch(sqls.get(i));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("插入错误语句："+sqls.get(i));
			}
		}  
		conn.commit();

		close(st);
	}
	/**
	 * 批插入
	 * @param conn
	 * @throws SQLException 
	 */
	public static void batchMutilValueInsert(Connection conn,String sqls) throws SQLException{
		conn.setAutoCommit(false); 
		Statement st =conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  ResultSet.CONCUR_READ_ONLY);
		st.execute(sqls.toString());
		
		//Statement st = conn.prepareStatement(sqls.toString());
		//st.executeUpdate();
		conn.commit();
		/*
		st.executeBatch();
		conn.commit();
		st.clearBatch();
		 */
		close(st);
	}
/**
 * 批量更新
 * @param conn
 * @param listMap
 * @param tableName
 * @throws SQLException
 */
	/*
	public static void batchUpdate(Connection conn,List<Map<String, String>>listMap,String tableName) throws SQLException{
		conn.setAutoCommit(false); 
		StringBuffer sql=new StringBuffer();
		sql.append("update ");
		sql.append(tableName);
		sql.append("	set word_pinyin=?	");
		sql.append("	 WHERE word_english=?	");
		
		PreparedStatement ps = conn.prepareStatement(sql.toString());
		int ind=0;
		for (Map<String, String> map2 : listMap) {
			ps.setObject(1, map2.get("word"));
			ps.setObject(2, map2.get("wordEnglish"));
			ps.addBatch();
			ind++;
			if(ind%50==0){
				ps.executeBatch();
				conn.commit();
				ps.clearBatch();
				System.out.println(ind);
			}
		}
		ps.executeBatch();
		conn.commit();
		ps.clearBatch();
		close(ps);
		//close(conn);
	}
	*/
	/**
	 * 单个更新
	 * @param conn
	 * @param listMap
	 * @param tableName
	 * @throws SQLException
	 */
	/*
	public static void update(Connection conn,Map<String, String>map,String tableName) throws SQLException{
		conn.setAutoCommit(false); 
		StringBuffer sql=new StringBuffer();
		sql.append("update ");
		sql.append(tableName);
		sql.append("	set word_pinyin=?	");
		sql.append("	 WHERE word_english=?	");
		
		PreparedStatement ps = conn.prepareStatement(sql.toString());
		
		ps.setObject(1, map.get("word"));
		ps.setObject(2, map.get("wordEnglish"));
			
		
		ps.executeUpdate();
		conn.commit();
		close(ps);
		//close(conn);
	}
	*/
	
	/**
	 * 替换字段
	 * @param map 对应关系
	 * @param result 查询结果
	 * @return
	 */
	public static List<Map<String,Object>>replaceField(Map<String,String>map,List<Map<String, Object>> result){
		List<Map<String,Object>>listMaps=new ArrayList<Map<String,Object>>();
		Map<String,Object> m=null;
		for (Map<String, Object> map2 : result) {
			m=new HashMap<String,Object>();
			for (String key : map.keySet()) {
				if(!key.equals("id")){
					m.put(key, map2.get(map.get(key)));
				}
			}
			listMaps.add(m);
		}
		return listMaps;
	}
	/**
	 * 批插入
	 * @param conn
	 * @param listMap
	 * @param map
	 * @param tableName
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	public static void batchInsert(Connection conn,List<Map<String, Object>>listMap,Map<String, String>map,String tableName,int classid) throws SQLException{
		
		String id = map.get("id");
		conn.setAutoCommit(false); 
		int index=0;
		StringBuffer sql=new StringBuffer();
		sql.append("insert into ");
		sql.append(tableName);
		sql.append(" (");
		for (String key : map.keySet()) {
			sql.append(key);
			sql.append(",");
		}
		if(classid!=-1){
			sql.append("create_time,");
			sql.append("create_user_id,");
			sql.append("status,");
			sql.append("version,");
			sql.append("classes_id,");
			sql.append("data_source,");
			sql.append("create_org_id,");
		}
		
		if(id == null){//没有id,则添加
			sql.append("id,");
		}
		
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		sql.append(" values ( ");
		for (String key : map.keySet()) {
			sql.append("?");
			sql.append(",");
		}
		if(classid!=-1){
			sql.append("?,");
			sql.append("?,");
			sql.append("?,");
			sql.append("?,");
			sql.append("?,");
			sql.append("?,");
			sql.append("?,");
		}
		if(id == null){//没有id,则添加
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		
		PreparedStatement ps = conn.prepareStatement(sql.toString());
		
		for (int i = 0; i < listMap.size(); i++) {
			index=1;
			for (String key : map.keySet()) {
				ps.setObject(index,listMap.get(i).get(map.get(key)));
				index++;
			}
			if(classid!=-1){
				ps.setObject(index, DateUtils.getDateTime());
				ps.setObject(++index, 1);
				ps.setObject(++index, 0);
				ps.setObject(++index,"1.0");
				ps.setObject(++index,classid);
				ps.setObject(++index, "init");
				ps.setObject(++index, 48);
			}
			if(id == null){//没有id,则添加
				ps.setObject(++index, String.valueOf(aIdWorker.nextId()));
			}
			
			
			ps.addBatch();
			if((i != 0 && i%10000==0) || i==listMap.size()-1){
				ps.executeBatch();
				conn.commit();
				ps.clearBatch();
			}
		}
		/*
		ps.executeBatch();
		conn.commit();
		ps.clearBatch();
		*/
		close(ps);
	}
	/**
	 * 组装sql语句，以便进行批插入
	 * @param tableName 被写入的数据库表名称
	 * @param map：对应字段--key:知识库字段，value：数据源字段
	 * @param
	 * @return
	 */
	public static List<String> getbatchSql(String tableName,Map<String, String> map,List<Map<String, String>> result){
		List<String>sqls=new ArrayList<String>();
		for (int i = 0; i < result.size(); i++) {
			StringBuffer insertb = new StringBuffer();
			StringBuffer inserta = new StringBuffer();
			insertb.append("INSERT INTO ");
			insertb.append(tableName);
			insertb.append(" (");
			
			inserta.append(" VALUES( ");
			for (String key : map.keySet()) {
				//insertb.append(map.get(key)+",");
				insertb.append(key+",");
				//if(result.get(i).get(key) == null){
				if(result.get(i).get(map.get(key)) == null){
					//inserta.append(result.get(i).get(key) +",");
					inserta.append(result.get(i).get(map.get(key)) +",");
				}else{
					//inserta.append("'"+result.get(i).get(key) +"',");
					inserta.append("'"+result.get(i).get(map.get(key)).replace("'", "\'") +"',");
				}
			}
			if(insertb.toString().endsWith(",")){
				insertb.delete(insertb.length()-1, insertb.length());
			}
			if(inserta.toString().endsWith(",")){
				inserta.delete(inserta.length()-1, inserta.length());
			}
			
			insertb.append(")");
			inserta.append(")");
			sqls.add(insertb.toString()+inserta.toString());
		}
		return sqls;
	}




    /**
     * 获取表信息->columnName:字段名称，columnClassName:class类型，columnTypeName:字段类型，columnSize:字段长度
     * @param conn
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>>getTableInfo(Connection conn,String tableName) throws SQLException{
    	List<Map<String, Object>>listMap=new ArrayList<Map<String,Object>>();
    	Map<String, Object>map=null;
    	ResultSet rs = executeSql(conn, "select * from "+tableName+" where 1=2");
    	ResultSetMetaData metaData = rs.getMetaData();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			map=new HashMap<String,Object>();
			map.put("columnName", metaData.getColumnName(i));
			map.put("columnClassName", metaData.getColumnClassName(i));
			map.put("columnTypeName", metaData.getColumnTypeName(i));
			map.put("columnSize", metaData.getColumnDisplaySize(i)+"");
			listMap.add(map);
		}
		close(rs);
    	return listMap;
    }
    

    /**
     * 获取总记录数
     * @param conn
     * @param tableName
     * @return
     */
    public static int getCount(Connection conn,String tableName){
    	ResultSet rs = executeSql(conn, "select count(*) as count from "+tableName);
    	List<Map<String, Object>> result = getResult(rs);
    	String string = result.get(0).get("count")+"";
    	close(rs);
    	if(string !=null && !string.equals("")){
    		return Integer.parseInt(string);
    	}else{
    		return 0;
    	}
    }

    //============================start===================================================
    public List<Map<String, String>> findToBean(Connection con, String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnLabel(i);
					String columnValue = rs.getString(i);
					map.put(columnName, columnValue);
				}
				maplist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maplist;
	}
	public void insertOrUpdate(Connection con, String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void batchInsertOrUpdate(Connection con, List<String> list) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			for (int i = 0; i < list.size(); i++) {
				stmt.addBatch(list.get(i));
			}
			stmt.executeBatch();
			list.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(Connection con, String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public  List<Map<String, String>> findToListMap(Connection con,String sql) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnLabel(i);
					String columnValue = rs.getString(i);
					map.put(columnName, columnValue);
				}
				maplist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null){
					stmt.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con!=null){
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		return maplist;
	}
	/**
	 * 获取list<map>
	 * @param
	 * @param sql
	 * @return
	 */
	public static List<Map<String, Object>> getListMap(Connection conn,String sql) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			Map<String, Object> map =null;
			while (rs.next()) {
				map = new HashMap<String, Object>();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnLabel(i);
					map.put(columnName,  rs.getObject(i));
				}
				maplist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null){
					stmt.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		return maplist;
	}

	public  Map<String, String> findToMap(Connection con,String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= data.getColumnCount(); i++) {
					// String columnName = data.getColumnName(i);
					String columnName = data.getColumnLabel(i);
					String columnValue = rs.getString(i);
					map.put(columnName, columnValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public static int findToInt(Connection con, String sql) {
		PreparedStatement stmt=null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql); 
			rs = stmt.executeQuery();   
		    if (rs.next()) {    
		    	 return rs.getInt(1);    
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				//con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	   //=========================end======================================================
}
