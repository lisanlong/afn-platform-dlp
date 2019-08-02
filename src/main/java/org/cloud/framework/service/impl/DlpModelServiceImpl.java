package org.cloud.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cloud.framework.mapper.DlpClassifyMapper;
import org.cloud.framework.model.DlpClassify;
import org.cloud.framework.model.DlpModel;
import org.cloud.framework.mapper.DlpModelMapper;
import org.cloud.framework.protocol.INlpService;
import org.cloud.framework.service.IDlpModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cloud.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LKS
 * @since 2019-07-08
 */
@Service
public class DlpModelServiceImpl extends ServiceImpl<DlpModelMapper, DlpModel> implements IDlpModelService {

	@Autowired
	private DlpModelMapper dlpModelMapper;

	@Autowired
	private DlpClassifyMapper dlpClassifyMapper;

	@Autowired
	private INlpService iNlpService;

	@Override
	public List<LinkedHashMap<String, Object>> getAllModel() {
	    QueryWrapper<DlpModel> queryWrapper = new QueryWrapper<>();
	    queryWrapper.eq("model_status", 5);
	    queryWrapper.eq("is_stoped", 0);
		List<DlpModel> dlpModels = dlpModelMapper.selectList(queryWrapper);
		List<LinkedHashMap<String, Object>> result = new ArrayList<>();
		for(DlpModel model : dlpModels) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			map.put("key", model.getVersion());
			map.put("value", model.getId());
			result.add(map);
		}
		return result;
	}

	@Override
	public List<String> getSelectedLabels(String idStr) {
		List<String> result = new ArrayList<>();
		if(!StringUtils.isEmpty(idStr)) {
			// 根据id获取对应的模型
			DlpModel dlpModel = dlpModelMapper.selectOne(new QueryWrapper<DlpModel>().eq("id", Integer.parseInt(idStr)));
			List<String> labelList = Arrays.asList(dlpModel.getLabels().split(";"));
			for(String label : labelList) {
				DlpClassify classify = dlpClassifyMapper.selectOne(new QueryWrapper<DlpClassify>().eq("cnum", label));
				result.add(classify.getName());
			}
		}

		return result;
	}

	@Override
	public Map<String, Object> filePredict(String fileUrl, String version) {
		DlpModel dlpModel = dlpModelMapper.selectOne(new QueryWrapper<DlpModel>().eq("version", version));
		Map<String, Object> stringObjectMap = iNlpService.onlineFilePredict(fileUrl, dlpModel);
		return stringObjectMap;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> textPredict(String textArea, String version) {
		DlpModel dlpModel = dlpModelMapper.selectOne(new QueryWrapper<DlpModel>().eq("version", version));
		Map<String, Object> stringObjectMap = iNlpService.onlineTextPredict(textArea, dlpModel);
		return stringObjectMap;
	}
}
