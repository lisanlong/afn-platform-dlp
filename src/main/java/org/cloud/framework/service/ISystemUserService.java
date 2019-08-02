package org.cloud.framework.service;

import org.cloud.framework.model.SystemUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LKS
 * @since 2019-07-08
 */
public interface ISystemUserService extends IService<SystemUser> {

	/**
	 * 根据用户 id 获取对应的 部门 id
	 * @param userid
	 * @return
	 */
	String getDlpDepartmentIdByLoginUser(Integer userid);
}
