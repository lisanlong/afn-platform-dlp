package org.cloud.framework.service.impl;

import org.cloud.framework.model.SystemUser;
import org.cloud.framework.mapper.SystemUserMapper;
import org.cloud.framework.service.ISystemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LKS
 * @since 2019-07-09
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

	@Autowired
	private SystemUserMapper systemUserMapper;

	@Override
	public String getDlpDepartmentIdByLoginUser(Integer userid) {
		return systemUserMapper.selectById(userid).getDptId() + "";
	}
}
