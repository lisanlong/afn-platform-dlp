package org.cloud.framework.service.impl;

import org.cloud.framework.model.SystemLog;
import org.cloud.framework.mapper.SystemLogMapper;
import org.cloud.framework.service.ISystemLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LKS
 * @since 2019-07-08
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements ISystemLogService {

}
