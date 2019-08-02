package org.cloud.framework.protocol.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cloud.framework.model.*;
import org.cloud.framework.protocol.ISearchDataWriteService;
import org.cloud.framework.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchDataWriteService implements ISearchDataWriteService {

//    private String repositoryPrefix = "bs";
//    private String dbStyle = "mysql";

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Autowired
    private ISystemLogService systemLogService;
    @Autowired
    private ISystemUserService systemUserService;
    @Autowired
    private IDlpClassifyService dlpClassifyService;
    @Autowired
    private IDlpTermService dlpTermService;
    @Autowired
    private IDlpTermClassService dlpTermClassService;
    @Autowired
    private IDlpCwordService dlpCwordService;
    @Autowired
    private IDlpCorpusService dlpCorpusService;
    @Autowired
    private IDlpModelService dlpModelService;
    @Autowired
    private IDlpDepartmentService dlpDepartmentService;
    @Autowired
    private IDlpDataSourceService dlpDataSourceService;
    @Autowired
    private IDlpCheckTaskService dlpCheckTaskService;
    @Autowired
    private IDlpCheckRecordService dlpCheckRecordService;
    @Autowired
    private SearchDataReadService searchDataReadService;

    //通过jdbc执行语句
    @SuppressWarnings("unused")
	private void executeUpdateSQL(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            ps = conn.prepareStatement(sql);
            ps.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            rs = null;
            ps = null;
            conn = null;
        }
    }

    @Override
    public SystemLog saveSystemLog(SystemLog systemLog) {
        boolean r = systemLogService.save(systemLog);
        if (r) {
            return systemLog;
        }
        return null;
    }

    @Override
    public SystemLog editSystemLog(SystemLog systemLog) {
        boolean r = systemLogService.updateById(systemLog);
        if (r) {
            return systemLog;
        }
        return null;
    }

    @Override
    public void deleteSystemLog(SystemLog systemLog) {
        boolean r = systemLogService.removeById(systemLog.getId());
        if (r) {
            System.out.println("systemLog 删除成功>>>" + systemLog.getId());
        }
    }

    @Override
    public SystemUser saveSystemUser(SystemUser systemUser) {
        boolean r = systemUserService.save(systemUser);
        if (r) {
            return systemUser;
        }
        return null;
    }

    @Override
    public SystemUser editSystemUser(SystemUser systemUser) {
        boolean r = systemUserService.updateById(systemUser);
        if (r) {
            return systemUser;
        }
        return null;
    }

    @Override
    public void deleteSystemUser(SystemUser systemUser) {
        boolean r = systemUserService.removeById(systemUser.getId());
        if (r) {
            System.out.println("systemUser 删除成功>>>" + systemUser.getId());
        }
    }

    @Override
    public DlpClassify saveDlpClassify(DlpClassify dlpClassify) {
        boolean r = dlpClassifyService.save(dlpClassify);
        if (r) {
            return dlpClassify;
        }
        return null;
    }

    @Override
    public DlpClassify editDlpClassify(DlpClassify dlpClassify) {
        boolean r = dlpClassifyService.updateById(dlpClassify);
        if (r) {
            return dlpClassify;
        }
        return null;
    }

    @Override
    public void deleteDlpClassify(DlpClassify dlpClassify) {
        boolean r = dlpClassifyService.removeById(dlpClassify.getId());
        if (r) {
            System.out.println("systemUser 删除成功>>>" + dlpClassify.getId());
        }
    }

    @Override
    public DlpTerm saveDlpTerm(DlpTerm dlpTerm) {
        boolean r = dlpTermService.save(dlpTerm);
        if (r) {
            return dlpTerm;
        }
        return null;
    }

    @Override
    public DlpTerm editDlpTerm(DlpTerm dlpTerm) {
        boolean r = dlpTermService.updateById(dlpTerm);
        if (r) {
            return dlpTerm;
        }
        return null;
    }

    @Override
    public void deleteDlpTerm(DlpTerm dlpTerm) {
        boolean r = dlpTermService.removeById(dlpTerm.getId());
        if (r) {
            System.out.println("systemUser 删除成功>>>" + dlpTerm.getId());
        }
    }
    @Override
    public DlpTermClass saveDlpTermClass(DlpTermClass dlpTermClass) {
        boolean r = dlpTermClassService.save(dlpTermClass);
        if (r) {
            return dlpTermClass;
        }
        return null;
    }

    @Override
    public DlpTermClass editDlpTermClass(DlpTermClass dlpTermClass) {
        boolean r = dlpTermClassService.updateById(dlpTermClass);
        if (r) {
            return dlpTermClass;
        }
        return null;
    }

    @Override
    public void deleteDlpTermClass(DlpTermClass dlpTermClass) {
        boolean r = dlpTermClassService.removeById(dlpTermClass.getId());
        if (r) {
            System.out.println("systemUser 删除成功>>>" + dlpTermClass.getId());
        }
    }

    @Override
    public DlpCword saveDlpCword(DlpCword dlpCword) {
        boolean r = dlpCwordService.save(dlpCword);
        if (r) {
            return dlpCword;
        }
        return null;
    }

    @Override
    public DlpCword editDlpCword(DlpCword dlpCword) {
        boolean r = dlpCwordService.updateById(dlpCword);
        if (r) {
            return dlpCword;
        }
        return null;
    }

    @Override
    public void deleteDlpCword(DlpCword dlpCword) {
        boolean r = dlpCwordService.removeById(dlpCword.getId());
        if (r) {
            System.out.println("systemUser 删除成功>>>" + dlpCword.getId());
        }
    }

    @Override
    public DlpCorpus saveDlpCorpus(DlpCorpus dlpCorpus) {
        boolean r = dlpCorpusService.save(dlpCorpus);
        if (r) {
            return dlpCorpus;
        }
        return null;
    }

    @Override
    public DlpCorpus editDlpCorpus(DlpCorpus dlpCorpus) {
        boolean r = dlpCorpusService.updateById(dlpCorpus);
        if (r) {
            return dlpCorpus;
        }
        return null;
    }

    @Override
    public void deleteDlpCorpus(DlpCorpus dlpCorpus) {
        boolean r = dlpCorpusService.removeById(dlpCorpus.getId());
        if (r) {
            System.out.println("systemUser 删除成功>>>" + dlpCorpus.getId());
        }
    }

    @Override
    public DlpModel saveDlpModel(DlpModel dlpModel) {
        boolean r = dlpModelService.save(dlpModel);
        if (r) {
            return dlpModel;
        }
        return null;
    }

    @Override
    public DlpModel editDlpModel(DlpModel dlpModel) {
        boolean r = dlpModelService.updateById(dlpModel);
        if (r) {
            return dlpModel;
        }
        return null;
    }

    @Override
    public void deleteDlpModel(DlpModel dlpModel) {
        boolean r = dlpModelService.removeById(dlpModel.getId());
        if (r) {
            System.out.println("systemUser 删除成功>>>" + dlpModel.getId());
        }
    }

    @Override
    public DlpDepartment saveDlpDepartment(DlpDepartment dlpDepartment) {
        boolean r = dlpDepartmentService.save(dlpDepartment);
        if (r) {
            return dlpDepartment;
        }
        return null;
    }

    @Override
    public DlpDepartment editDlpDepartment(DlpDepartment dlpDepartment) {
        boolean r = dlpDepartmentService.updateById(dlpDepartment);
        if (r) {
            return dlpDepartment;
        }
        return null;
    }

    @Override
    public void deleteDlpDepartment(DlpDepartment dlpDepartment) {
        boolean r = dlpDepartmentService.removeById(dlpDepartment.getId());
        if (r) {
            System.out.println("systemUser 删除成功>>>" + dlpDepartment.getId());
        }
    }

	@Override
	public DlpDataSource saveDlpDataSource(DlpDataSource dataSource) {
    	dataSource.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    	dataSource.setCoding("UTF-8");
    	dlpDataSourceService.save(dataSource);
		return dataSource;
	}

	@Override
	public DlpDataSource editDlpDataSource(DlpDataSource dataSource) {
		dlpDataSourceService.saveOrUpdate(dataSource);
		return dataSource;
	}

	@Override
	public void deleteDlpDataSource(Integer id) {
		dlpDataSourceService.removeById(id);
	}

	@Override
	public void saveDlpCheckTask(DlpCheckTask checkTask) {
		dlpCheckTaskService.save(checkTask);
	}

	@Override
	public void deleteDlpCheckTaskById(Integer id) {
		dlpCheckTaskService.removeById(id);

		// 删除当前的任务 id 对应的所有的 record
        QueryWrapper<DlpCheckRecord> wrapper = new QueryWrapper<DlpCheckRecord>().eq("task_id", id);
        dlpCheckRecordService.remove(wrapper);
	}

	@Override
	public void saveDlpCheckRecord(DlpCheckRecord record) {
		dlpCheckRecordService.save(record);
	}

    @Override
    public Map<String, Object> recordReview(Integer id, String viewResult, Integer userId) {
        DlpCheckRecord record = dlpCheckRecordService.getById(id);
        record.setReviewTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        // 查实确认状态
        record.setReviewStatus(2);
        DlpClassify classify = searchDataReadService.getDlpClassifyByName(viewResult);
        Map<String, Object> result = new HashMap<>();
        if(null != classify) {
            if(record.getOriginLabel().equals(classify.getCnum())) {
                record.setReviewStatus(1);
            } else {
                // 确认密级标记错误
                record.setReviewStatus(2);
            }
            record.setReviewLabel(classify.getCnum());
            record.setReviewUid(userId);
            dlpCheckRecordService.updateById(record);
            result.put("result", "修改成功!!!");
        }
        return result;
    }

    @Override
    public void saveDlpCheckRecordBatch(List<DlpCheckRecord> subList, int batchNum) {
        dlpCheckRecordService.saveBatch(subList, batchNum);
    }
}
