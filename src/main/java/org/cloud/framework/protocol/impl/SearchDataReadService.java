package org.cloud.framework.protocol.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.framework.mapper.DlpClassifyMapper;
import org.cloud.framework.model.*;
import org.cloud.framework.protocol.INlpService;
import org.cloud.framework.protocol.ISearchDataReadService;
import org.cloud.framework.service.*;
import org.cloud.framework.utils.FileUtils;
import org.cloud.framework.utils.MybatisPlusTools;
import org.cloud.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class SearchDataReadService implements ISearchDataReadService {


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
    private IDlpSourceTypeService dlpSourceTypeService;
    @Autowired
    private IDlpCheckRecordService dlpCheckRecordService;
    @Autowired
    private INlpService nlpService;
    @Autowired
    private DlpClassifyMapper dlpClassifyMapper;

	@Value("${data.upload.file}")
	private String dataUploadFile;

    @Override
    public SystemLog findSystemLog(Integer id) {
        return systemLogService.getById(id);
    }

    @Override
    public List<SystemLog> getSystemLog() {
        return systemLogService.list(null);
    }

    @Override
    public List<SystemLog> getSystemLog(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return systemLogService.page(
                new Page<SystemLog>(pageNumber, pageSize),
                new MybatisPlusTools<SystemLog>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getSystemLogCount(HashMap<String, Object> queryParams) {
        return systemLogService.count(new MybatisPlusTools<SystemLog>()
                .getEwByCount(queryParams));
    }


    @Override
    public SystemUser findSystemUser(Integer id) {
        return systemUserService.getById(id);
    }

    @Override
    public List<SystemUser> getSystemUser() {
        return systemUserService.list(null);
    }

    @Override
    public List<SystemUser> getSystemUser(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return systemUserService.page(
                new Page<SystemUser>(pageNumber, pageSize),
                new MybatisPlusTools<SystemUser>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getSystemUserCount(HashMap<String, Object> queryParams) {
        return systemUserService.count(new MybatisPlusTools<SystemUser>()
                .getEwByCount(queryParams));
    }

	@Override
	public String getDlpDepartmentIdByLoginUser(Integer userid) {
		return systemUserService.getDlpDepartmentIdByLoginUser(userid);
	}

	@Override
    public DlpClassify findDlpClassify(Integer id) {
        return dlpClassifyService.getById(id);
    }

    @Override
    public List<DlpClassify> getDlpClassify() {
        return dlpClassifyService.list(null);
    }

    @Override
    public List<DlpClassify> getDlpClassify(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return dlpClassifyService.page(
                new Page<DlpClassify>(pageNumber, pageSize),
                new MybatisPlusTools<DlpClassify>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getDlpClassifyCount(HashMap<String, Object> queryParams) {
        return dlpClassifyService.count(new MybatisPlusTools<DlpClassify>()
                .getEwByCount(queryParams));
    }

    @Override
    public DlpTerm findDlpTerm(Integer id) {
        return dlpTermService.getById(id);

    }

    @Override
    public List<DlpTerm> getDlpTerm() {
        return dlpTermService.list(null);
    }

    @Override
    public List<DlpTerm> getDlpTerm(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return dlpTermService.page(
                new Page<DlpTerm>(pageNumber, pageSize),
                new MybatisPlusTools<DlpTerm>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getDlpTermCount(HashMap<String, Object> queryParams) {
        return dlpTermService.count(new MybatisPlusTools<DlpTerm>()
                .getEwByCount(queryParams));
    }
    @Override
    public DlpTermClass findDlpTermClass(Integer id) {
        return dlpTermClassService.getById(id);

    }

    @Override
    public List<DlpTermClass> getDlpTermClass() {
        return dlpTermClassService.list(null);
    }

    @Override
    public List<DlpTermClass> getDlpTermClass(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return dlpTermClassService.page(
                new Page<DlpTermClass>(pageNumber, pageSize),
                new MybatisPlusTools<DlpTermClass>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getDlpTermClassCount(HashMap<String, Object> queryParams) {
        return dlpTermClassService.count(new MybatisPlusTools<DlpTermClass>()
                .getEwByCount(queryParams));
    }

    @Override
    public DlpCword findDlpCword(Integer id) {
        return dlpCwordService.getById(id);
    }

    @Override
    public List<DlpCword> getDlpCword() {
        return dlpCwordService.list(null);
    }

    @Override
    public List<DlpCword> getDlpCword(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return dlpCwordService.page(
                new Page<DlpCword>(pageNumber, pageSize),
                new MybatisPlusTools<DlpCword>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getDlpCwordCount(HashMap<String, Object> queryParams) {
        return dlpCwordService.count(new MybatisPlusTools<DlpCword>()
                .getEwByCount(queryParams));
    }

    @Override
    public DlpCorpus findDlpCorpus(Integer id) {
        return dlpCorpusService.getById(id);
    }

    @Override
    public List<DlpCorpus> getDlpCorpus() {
        return dlpCorpusService.list(null);
    }

    @Override
    public List<DlpCorpus> getDlpCorpus(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return dlpCorpusService.page(
                new Page<DlpCorpus>(pageNumber, pageSize),
                new MybatisPlusTools<DlpCorpus>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getDlpCorpusCount(HashMap<String, Object> queryParams) {
        return dlpCorpusService.count(new MybatisPlusTools<DlpCorpus>()
                .getEwByCount(queryParams));
    }

    @Override
    public DlpModel findDlpModel(Integer id) {
        return dlpModelService.getById(id);
    }

    @Override
    public List<DlpModel> getDlpModel() {
        return dlpModelService.list(null);
    }

    @Override
    public List<DlpModel> getDlpModel(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return dlpModelService.page(
                new Page<DlpModel>(pageNumber, pageSize),
                new MybatisPlusTools<DlpModel>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getDlpModelCount(HashMap<String, Object> queryParams) {
        return dlpModelService.count(new MybatisPlusTools<DlpModel>()
                .getEwByCount(queryParams));
    }

    @Override
    public DlpDepartment findDlpDepartment(Integer id) {
        return dlpDepartmentService.getById(id);
    }

    @Override
    public List<DlpDepartment> getDlpDepartment() {
        return dlpDepartmentService.list(null);
    }

    @Override
    public List<DlpDepartment> getDlpDepartment(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
        return dlpDepartmentService.page(
                new Page<DlpDepartment>(pageNumber, pageSize),
                new MybatisPlusTools<DlpDepartment>().getEwByList(queryParams,pageNumber))
                .getRecords();
    }

    @Override
    public Integer getDlpDepartmentCount(HashMap<String, Object> queryParams) {
        return dlpDepartmentService.count(new MybatisPlusTools<DlpDepartment>()
                .getEwByCount(queryParams));
    }

	@Override
	public DlpDataSource findDlpDataSource(Integer id) {
		return dlpDataSourceService.getById(id);
	}

	@Override
	public List<DlpDataSource> getDlpDataSource() {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("is_stoped", 0);
		return dlpDataSourceService.list(qw);
	}

	@Override
	public List<DlpDataSource> getDataSourceList(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
		return dlpDataSourceService.page(
				new Page<DlpDataSource>(pageNumber, pageSize),
				new MybatisPlusTools<DlpDataSource>().getEwByList(queryParams,pageNumber))
				.getRecords();
	}

	@Override
	public Integer getDlpDataSourceCount(HashMap<String, Object> queryParams) {
		return dlpDataSourceService.count(new MybatisPlusTools<DlpDataSource>()
				.getEwByCount(queryParams));
	}

	@Override
	public List<DlpSourceType> getDataSourceTypeList() {
		return dlpSourceTypeService.list(null);
	}

	@Override
	public DlpDataSource getDataSourceById(Integer id) {
		return dlpDataSourceService.getById(id);
	}

	@Override
	public String getSourceTypeNameBySourceCode(Integer code) {
		return dlpSourceTypeService.getSourceTypeNameBySourceCode(code);
	}

	@Override
	public DlpCheckTask findDlpCheckTask(Integer id) {
		return dlpCheckTaskService.getById(id);
	}

	@Override
	public List<DlpCheckTask> getDlpCheckTask() {
		return dlpCheckTaskService.list(null);
	}

	@Override
	public List<DlpCheckTask> getDlpCheckTaskList(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
		return dlpCheckTaskService.page(new Page<DlpCheckTask>(pageNumber, pageSize),
				new MybatisPlusTools<DlpCheckTask>().getEwByList(queryParams,pageNumber))
				.getRecords();
	}

	@Override
	public Integer getDlpCheckTaskCount(HashMap<String, Object> queryParams) {
		return dlpCheckTaskService.count(new MybatisPlusTools<DlpCheckTask>().getEwByCount(queryParams));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findDlpCheckRecord(Integer id) {
		DlpCheckRecord record = dlpCheckRecordService.getById(id);
		Integer taskId = record.getTaskId();
		String modelId = dlpCheckTaskService.getById(taskId).getModelId();
		DlpModel model = dlpModelService.getById(modelId);
        DlpCheckTask task = dlpCheckTaskService.getById(taskId);
        Integer datasourceId = task.getDatasourceId();
        Integer sourceType = dlpDataSourceService.getById(datasourceId).getSourceType();

        // 返回测试结果
        Map<String, Object> stringObjectMap = new HashMap<>();
        String orignContent = "";
        if(sourceType == 1) {
            String checkPath = task.getFilePath();
            if(checkPath.endsWith(File.separator)) {
                checkPath = checkPath.substring(0, checkPath.length() - 1);
            }
            stringObjectMap = nlpService.onlineFilePredict(checkPath + File.separator + record.getFileDiskPath(), model);
            orignContent = nlpService.getFileContent(checkPath + File.separator + record.getFileDiskPath());
        } else {
            stringObjectMap = nlpService.onlineFilePredict(dataUploadFile + File.separator + record.getFileDiskPath(), model);
            orignContent = nlpService.getFileContent(dataUploadFile + File.separator + record.getFileDiskPath());
        }
//        orignContent = orignContent.replace("　","").replace(" ", "");
        Map<String, Object> result = new HashMap<>();
        ArrayList<String> maxSentences = (ArrayList<String>) stringObjectMap.get("maxSentences");
        /*HashMap<String,Double> wordWeights = (HashMap<String, Double>) stringObjectMap.get("wordWeight");
        StringBuilder sb = new StringBuilder();
        for(String sen : maxSentences) {
            if(StringUtils.isEmpty(sen)) {
                continue;
            }
            sb.append(sen.trim().replace("　",""));
        }
        List<String> keywords = new ArrayList<>();
        for(Entry<String, Double> entry : wordWeights.entrySet()) {
            keywords.add((String) entry.getKey());
        }
        String resultStr = sb.toString();
        for(String label : keywords){
            resultStr = resultStr.replace(label, "<strong  style=\"color: red\">"+label+"</strong>");
        }*/
        if(maxSentences == null || maxSentences.size() == 0) {
            result.put("content", "");
        } else {
            for(String sen : maxSentences) {
//            sen = sen.replace("　","").replace(" ", "");
                if(!StringUtils.isEmpty(sen.trim())) {

                    orignContent = orignContent.replace(sen, "<strong  style=\"color: red\">"+sen+"</strong>");
                }
            }
            result.put("content", orignContent);
        }


        // 2. 返回 modelId 对应的所有的 classify 的对象
        List<String> labelList = Arrays.asList(model.getLabels().split(";"));
        List<String> classifyList = new ArrayList<>();
        for(String label : labelList) {
            QueryWrapper<DlpClassify> queryWrapper = new QueryWrapper<DlpClassify>().eq("cnum", label);
            DlpClassify classify = dlpClassifyMapper.selectOne(queryWrapper);
            classifyList.add(classify.getName());
        }
        result.put("classify",classifyList);

        // 3. 返回原始密级与预测密级
        if(!StringUtils.isEmpty(record.getOriginLabel())) {
            result.put("originalLabel", getDlpClassifyByCnum(record.getOriginLabel()).getName());
        }
        if(!StringUtils.isEmpty(record.getCheckLabel())) {
            result.put("checkLabel", getDlpClassifyByCnum(record.getCheckLabel()).getName());
        }
        if(!StringUtils.isEmpty(record.getReviewLabel())) {
            result.put("reviewLabel", getDlpClassifyByCnum(record.getReviewLabel()).getName());
        }

        return result;
	}

	@Override
	public List<DlpCheckRecord> getDlpCheckRecord() {
		return dlpCheckRecordService.list(null);
	}

	@Override
	public List<DlpCheckRecord> getDlpCheckRecordList(HashMap<String, Object> queryParams, int pageNumber, int pageSize) {
		return dlpCheckRecordService.page(new Page<DlpCheckRecord>(pageNumber, pageSize),
				new MybatisPlusTools<DlpCheckRecord>().getEwByList(queryParams,pageNumber))
				.getRecords();
	}

	@Override
	public Integer getDlpCheckRecordCount(HashMap<String, Object> queryParams) {
		return dlpCheckRecordService.count(new MybatisPlusTools<DlpCheckRecord>().getEwByCount(queryParams));
	}

	// 查询不符合条件的检测类型
    @Override
    public List<String> findBadInfo(String modelId, String uploadDirectory) {
        List<String> badinfos = new ArrayList<>();
        // 中文的集合
        List<String> goodLabels = getChinLabelsByModelId(modelId);
        File file = new File(uploadDirectory);
        File[] files = file.listFiles();
        for(File f : files) {
            if(!goodLabels.contains(f.getName())) {
                badinfos.add(f.getName());
            }
        }
        return badinfos;
    }

    // 根据 modelId 获取 cnum 对应的中文的名称
    public List<String> getChinLabelsByModelId(String modelId) {
        DlpModel model = dlpModelService.getById(modelId);
        List<String> labels = Arrays.asList(model.getLabels().split(";"));
        List<DlpClassify> dlpClassifyList = dlpClassifyMapper.selectList(null);
        // 排除的集合
        List<String> goodLabels = new ArrayList<>();
        if(null != dlpClassifyList && dlpClassifyList.size() > 0 && null != labels && labels.size() > 0) {
            for(String label : labels) {
                for(DlpClassify classify : dlpClassifyList) {
                    if(label.trim().equals(classify.getCnum().trim())) {
                        goodLabels.add(classify.getName().trim());
                        continue;
                    }
                }
            }

        }
        return goodLabels;

    }

    // 查询文件下面的数量
    @Override
    public Integer getTotalFileOnly(String modelId, String targetPath) {
        // 中文的集合
        List<String> goodLabels = getChinLabelsByModelId(modelId);

        int total = 0;
        File uploadDirectory = new File(targetPath);
        if(!uploadDirectory.exists() || uploadDirectory.listFiles().length == 0) {
            return total;
        }
        File[] files = uploadDirectory.listFiles();
        boolean dirFlag = false;
        for(File f : files) {
            if(f.isDirectory()) {
                dirFlag = true;
                break;
            }
        }
        if(!dirFlag) {
            // 目录结构不符合要求，返回!
            return total;
        }

        for(File dir : files) {

            if(dir.isFile() || !goodLabels.contains(dir.getName())) {
                continue;
            }
            File[] files1 = dir.listFiles();
            for(File corpus : files1) {
                if(corpus.isDirectory()) {
                    continue;
                }
                total++;
            }
        }
        return total;
    }

    @Override
    public Integer getTotalCkeckNum(String modelId, File destFile) {
        // 1. 解压
        String targetPath = destFile.getAbsolutePath().substring(0, destFile.getAbsolutePath().lastIndexOf("."));
        FileUtils.unZip(destFile, targetPath);
        // 2. 获取文件内的文件的数量
        Integer totalFileOnly = getTotalFileOnly(modelId, targetPath);
        // 3. 删除临时文件
        FileUtils.deleteFile(new File(targetPath));
        return totalFileOnly;
    }

    @Override
    public DlpClassify getDlpClassifyByName(String name) {
        QueryWrapper qw = (QueryWrapper) new QueryWrapper().eq("name", name);
        DlpClassify classify = dlpClassifyMapper.selectOne(qw);
        return classify;
    }

    @Override
    public DlpClassify getDlpClassifyByCnum(String cnum) {
        QueryWrapper qw = (QueryWrapper) new QueryWrapper().eq("cnum", cnum);
        DlpClassify classify = dlpClassifyMapper.selectOne(qw);
        return classify;
    }

    @Override
    public List<LinkedHashMap<String, Object>> getAllTaskList() {
        List<DlpCheckTask> tasks = dlpCheckTaskService.list(null);
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        for(DlpCheckTask task : tasks) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("key", task.getName());
            map.put("value", task.getId());
            result.add(map);
        }
        return result;
    }

    /**
     * 获取压缩包的有效检测密级
     * @param modelId
     * @param destFile
     * @return
     */
    @Override
    public List<String> getTargetCheckRange(String modelId, File destFile) {
        // 1. 解压
        String targetPath = destFile.getAbsolutePath().substring(0, destFile.getAbsolutePath().lastIndexOf("."));
        FileUtils.unZip(destFile, targetPath);
        // 2. 获取文件内的文件的数量
        List<String> checkRange = getGoodCheckRange(modelId, targetPath);
        // 3. 删除临时文件
        FileUtils.deleteFile(new File(targetPath));
        return checkRange;
    }


    public List<String> getGoodCheckRange(String modelId, String targetPath) {
        List<String> result = new ArrayList<>();
        // 中文的集合
        List<String> goodLabels = getChinLabelsByModelId(modelId);

        File uploadDirectory = new File(targetPath);
        if(!uploadDirectory.exists() || uploadDirectory.listFiles().length == 0) {
            return result;
        }
        File[] files = uploadDirectory.listFiles();
        for(File f : files) {
            if(goodLabels.contains(f.getName())) {
                result.add(f.getName());
            }
        }

        return result;
    }


}
