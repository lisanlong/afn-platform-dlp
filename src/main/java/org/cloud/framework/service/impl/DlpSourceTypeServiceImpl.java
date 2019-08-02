package org.cloud.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cloud.framework.mapper.DlpSourceTypeMapper;
import org.cloud.framework.model.DlpSourceType;
import org.cloud.framework.service.IDlpSourceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wgsh
 * @Date wgshb on 2019/7/18 10:22
 */
@Service
public class DlpSourceTypeServiceImpl extends ServiceImpl<DlpSourceTypeMapper, DlpSourceType> implements IDlpSourceTypeService {
	@Autowired
	private DlpSourceTypeMapper dlpSourceTypeMapper;

	@Override
	public String getSourceTypeNameBySourceCode(Integer code) {
		QueryWrapper<DlpSourceType> wrapper = new QueryWrapper<DlpSourceType>().eq("code", code);
		DlpSourceType sourceType = dlpSourceTypeMapper.selectOne(wrapper);
		return sourceType.getName();
	}
}
