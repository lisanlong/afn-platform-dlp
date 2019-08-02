package org.cloud.framework.protocol;

import org.cloud.framework.model.*;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据存储
 */

public interface ISearchDataReadService {


    /**  日志表 start **/
    SystemLog findSystemLog(Integer id);
    List<SystemLog> getSystemLog();
    List<SystemLog> getSystemLog(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getSystemLogCount(HashMap<String, Object> queryParams);
    /**  日志表 end **/

    /**  用户表 start **/
    SystemUser findSystemUser(Integer id);
    List<SystemUser> getSystemUser();
    List<SystemUser> getSystemUser(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getSystemUserCount(HashMap<String, Object> queryParams);
	String getDlpDepartmentIdByLoginUser(Integer userid);
    /**  用户表 end **/

    /**  密级表 start **/
    DlpClassify findDlpClassify(Integer id);
    List<DlpClassify> getDlpClassify();
    List<DlpClassify> getDlpClassify(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getDlpClassifyCount(HashMap<String, Object> queryParams);
    /**  密级表 end **/

    /**  术语表 start **/
    DlpTerm findDlpTerm(Integer id);
    List<DlpTerm> getDlpTerm();
    List<DlpTerm> getDlpTerm(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getDlpTermCount(HashMap<String, Object> queryParams);
    /**  术语表 end **/

    /**  术语分类表 start **/
    DlpTermClass findDlpTermClass(Integer id);
    List<DlpTermClass> getDlpTermClass();
    List<DlpTermClass> getDlpTermClass(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getDlpTermClassCount(HashMap<String, Object> queryParams);
    /**  术语分类表 end **/

    /**  触发词表 start **/
    DlpCword findDlpCword(Integer id);
    List<DlpCword> getDlpCword();
    List<DlpCword> getDlpCword(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getDlpCwordCount(HashMap<String, Object> queryParams);
    /**  术语分类表 end **/

    /**  语料表 start **/
    DlpCorpus findDlpCorpus(Integer id);
    List<DlpCorpus> getDlpCorpus();
    List<DlpCorpus> getDlpCorpus(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getDlpCorpusCount(HashMap<String, Object> queryParams);
    /**  语料表 end **/

    /**  模型表 start **/
    DlpModel findDlpModel(Integer id);
    List<DlpModel> getDlpModel();
    List<DlpModel> getDlpModel(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getDlpModelCount(HashMap<String, Object> queryParams);
    /**  模型表 end **/

    /**  部门表 start **/
    DlpDepartment findDlpDepartment(Integer id);
    List<DlpDepartment> getDlpDepartment();
    List<DlpDepartment> getDlpDepartment(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
    Integer getDlpDepartmentCount(HashMap<String, Object> queryParams);
    /**  部门表 end **/

	/** 数据源表 **/
	DlpDataSource findDlpDataSource(Integer id);
	List<DlpDataSource> getDlpDataSource();
	List<DlpDataSource> getDataSourceList(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
	Integer getDlpDataSourceCount(HashMap<String, Object> queryParams);
	List<DlpSourceType> getDataSourceTypeList();
	DlpDataSource getDataSourceById(Integer id);
	String getSourceTypeNameBySourceCode(Integer code);
	/** 数据源表 **/

	/** 任务表 **/
	DlpCheckTask findDlpCheckTask(Integer id);
	List<DlpCheckTask> getDlpCheckTask();
	List<DlpCheckTask> getDlpCheckTaskList(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
	Integer getDlpCheckTaskCount(HashMap<String, Object> queryParams);
	/** 任务表 **/

	/** 任务记录表 **/
    Map<String, Object> findDlpCheckRecord(Integer id);
	List<DlpCheckRecord> getDlpCheckRecord();
	List<DlpCheckRecord> getDlpCheckRecordList(HashMap<String, Object> queryParams, int pageNumber, int pageSize);
	Integer getDlpCheckRecordCount(HashMap<String, Object> queryParams);
    List<String> findBadInfo(String modelId, String uploadDirectory);
    Integer getTotalFileOnly(String modelId, String uploadDirectory);
    Integer getTotalCkeckNum(String modelId, File destFile);
    DlpClassify getDlpClassifyByName(String name);
    DlpClassify getDlpClassifyByCnum(String cnum);
    List<LinkedHashMap<String, Object>> getAllTaskList();
    List<String> getTargetCheckRange(String modelId, File destFile);
    /** 任务记录表 **/

}
