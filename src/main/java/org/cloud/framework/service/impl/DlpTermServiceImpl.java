package org.cloud.framework.service.impl;

import org.cloud.framework.model.DlpTerm;
import org.cloud.framework.mapper.DlpTermMapper;
import org.cloud.framework.service.IDlpTermService;
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
public class DlpTermServiceImpl extends ServiceImpl<DlpTermMapper, DlpTerm> implements IDlpTermService {

}
