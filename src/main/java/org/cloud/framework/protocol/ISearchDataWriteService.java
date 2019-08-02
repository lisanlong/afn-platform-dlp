package org.cloud.framework.protocol;

import org.cloud.framework.model.*;

import java.util.List;
import java.util.Map;

/**
 * 数据存储
 */

public interface ISearchDataWriteService {

    /**  日志表 start **/
    SystemLog saveSystemLog(SystemLog systemLog);
    SystemLog editSystemLog(SystemLog systemLog);
    void deleteSystemLog(SystemLog systemLog);
    /**  日志表 end **/

    /**  用户表 start **/
    SystemUser saveSystemUser(SystemUser systemUser);
    SystemUser editSystemUser(SystemUser systemUser);
    void deleteSystemUser(SystemUser systemUser);
    /**  用户表 end **/

    /**  密级表 start **/
    DlpClassify saveDlpClassify(DlpClassify dlpClassify);
    DlpClassify editDlpClassify(DlpClassify dlpClassify);
    void deleteDlpClassify(DlpClassify dlpClassify);
    /**  密级表 end **/

    /**  术语表 start **/
    DlpTerm saveDlpTerm(DlpTerm dlpTerm);
    DlpTerm editDlpTerm(DlpTerm dlpTerm);
    void deleteDlpTerm(DlpTerm dlpTerm);
    /**  术语表 end **/

    /**  术语分类表 start **/
    DlpTermClass saveDlpTermClass(DlpTermClass dlpTermClass);
    DlpTermClass editDlpTermClass(DlpTermClass dlpTermClass);
    void deleteDlpTermClass(DlpTermClass dlpTermClass);
    /**  术语分类表 end **/

    /**  触发词表 start **/
    DlpCword saveDlpCword(DlpCword dlpCword);
    DlpCword editDlpCword(DlpCword dlpCword);
    void deleteDlpCword(DlpCword dlpCword);
    /**  触发词表 end **/

    /**  语料表 start **/
    DlpCorpus saveDlpCorpus(DlpCorpus dlpCorpus);
    DlpCorpus editDlpCorpus(DlpCorpus dlpCorpus);
    void deleteDlpCorpus(DlpCorpus dlpCorpus);
    /**  语料表 end **/

    /**  模型表 start **/
    DlpModel saveDlpModel(DlpModel dlpModel);
    DlpModel editDlpModel(DlpModel dlpModel);
    void deleteDlpModel(DlpModel dlpModel);
    /**  模型表 end **/

    /**  部门表 start **/
    DlpDepartment saveDlpDepartment(DlpDepartment dlpDepartment);
    DlpDepartment editDlpDepartment(DlpDepartment dlpDepartment);
    void deleteDlpDepartment(DlpDepartment dlpDepartment);
    /**  部门表 end **/

	/**  数据源表 start **/
	DlpDataSource saveDlpDataSource(DlpDataSource dataSource);
	DlpDataSource editDlpDataSource(DlpDataSource dataSource);
	void deleteDlpDataSource(Integer id);
	/**  数据源表 end **/

	/**  任务表 start **/
	void saveDlpCheckTask(DlpCheckTask checkTask);
	void deleteDlpCheckTaskById(Integer id);
	/**  任务表 end **/

	/**  记录表 start **/
	void saveDlpCheckRecord(DlpCheckRecord record);
    Map<String, Object> recordReview(Integer id, String viewResult, Integer userId);
    // 批量插入
    void saveDlpCheckRecordBatch(List<DlpCheckRecord> subList, int batchNum);
    /**  记录表 end **/
}
