
-- ----------------------------
-- 语料库
-- ----------------------------
CREATE TABLE`dlp_corpus`(
`id` int(11) NOT NULL AUTO_INCREMENT,
`cnum` varchar(10) DEFAULT NULL COMMENT '密级编号',
`file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
`file_size` varchar(255) DEFAULT NULL COMMENT '文件大小',
`file_type` varchar(255) DEFAULT NULL COMMENT '文件类型',
`file_path` varchar(255) DEFAULT NULL COMMENT '语料文件相对路径',
`content` longtext DEFAULT NULL COMMENT '文件正文',
`feature` text DEFAULT NULL COMMENT '特征词',
`is_stoped` tinyint(4) DEFAULT NULL COMMENT '是否停用，0否1是',
`create_uid` int(11) DEFAULT NULL COMMENT '创建人',
`create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- 模型管理
-- ----------------------------
CREATE TABLE`dlp_model`(
`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(10) DEFAULT NULL COMMENT '模型名称',
`labels` varchar(255) DEFAULT NULL COMMENT '预测分类类别（密级范围）',
`version` decimal(10,0) DEFAULT NULL COMMENT '版本号',
`model_status` tinyint(4) DEFAULT NULL COMMENT '处理进度（0：未处理；1：文本解析2：模型训练3：训练结束）',
`start_time` varchar(20) DEFAULT NULL COMMENT '训练开始时间',
`end_time` varchar(20) DEFAULT NULL COMMENT '训练结束时间',
`detail` text DEFAULT NULL COMMENT '模型说明',
`is_stoped` tinyint(4) DEFAULT NULL COMMENT '是否停用，0否1是',
`create_uid` int(11) DEFAULT NULL COMMENT '创建人',
`create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;


-- ----------------------------
-- 密级表
-- ----------------------------
CREATE TABLE`dlp_classify`(
`id` int(11) NOT NULL AUTO_INCREMENT ,
`name` varchar(255)  DEFAULT NULL COMMENT  ' 密级名称 ',
`cnum` varchar(10)  DEFAULT NULL COMMENT  ' 密级编号 ',
`detail` text  DEFAULT NULL COMMENT  ' 说明',
`is_stoped` tinyint(4) DEFAULT NULL COMMENT '是否停用，0否1是',
`create_time` varchar(20)  DEFAULT NULL COMMENT  ' 创建时间 ',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;


-- ----------------------------
-- 术语分类
-- ----------------------------
CREATE TABLE`dlp_term_class`(`id` int(11) NOT NULL AUTO_INCREMENT ,
`name` varchar(255)  DEFAULT NULL COMMENT  ' 类型名称 ',
`detail` text  DEFAULT NULL COMMENT  ' 描述 ',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- 术语表
-- ----------------------------
CREATE TABLE`dlp_term`(
`id` int(11) NOT NULL AUTO_INCREMENT ,
`name` varchar(255)  DEFAULT NULL COMMENT  ' 名称 ',
`termclass_id` int(11)  DEFAULT NULL COMMENT  ' 术语分类 ',
`is_stoped` tinyint(4) DEFAULT NULL COMMENT '是否停用，0否1是',
`create_uid` int(11)  DEFAULT NULL COMMENT  ' 创建人 ',
`create_time` varchar(20)  DEFAULT NULL COMMENT  ' 创建时间 ',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- 密级触发词表
-- ----------------------------
CREATE TABLE`dlp_cword`(
`id` int(11) NOT NULL AUTO_INCREMENT ,
`word` varchar(200)  DEFAULT NULL COMMENT  ' 密级词 ',
`cnum` varchar(10)  DEFAULT NULL COMMENT  ' 密级编号 ',
`is_stoped` tinyint(4) DEFAULT NULL COMMENT '是否停用，0否1是',
`create_uid` int(11)  DEFAULT NULL COMMENT  ' 创建人 ',
`create_time` varchar(20)  DEFAULT NULL COMMENT  ' 创建时间 ',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- 数据源表
-- ----------------------------
CREATE TABLE`dlp_data_source`(
`id` int(11) NOT NULL AUTO_INCREMENT ,
`name` varchar(100)  DEFAULT NULL COMMENT  ' 数据源名称 ',
`detail` varchar(255)  DEFAULT NULL COMMENT  ' 数据源描述 ',
`source_type` varchar(10)  DEFAULT NULL COMMENT  ' excel/mysql/oracle/sqlServer/xml/zip/FTP ',
`url` varchar(500)  DEFAULT NULL COMMENT  ' 链接地址 ',
`user` varchar(15)  DEFAULT NULL COMMENT  ' 用户名 ',
`password` varchar(64)  DEFAULT NULL COMMENT  ' 密码 ',
`is_stoped` tinyint(4) DEFAULT NULL COMMENT '是否停用，0否1是',
`coding` varchar(10)  DEFAULT NULL COMMENT  ' 编码 ',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- 任务列表
-- ----------------------------
CREATE TABLE`dlp_check_task`(
`id` int(11) NOT NULL AUTO_INCREMENT ,
`model_id` int(11)  DEFAULT NULL COMMENT  ' 模型id ',
`name` varchar(50)  DEFAULT NULL COMMENT  ' 任务名称 ',
`datasource_id` int(11) DEFAULT NULL COMMENT  ' 数据源ID ',
`file_path` varchar(50)  DEFAULT NULL COMMENT  ' 预测文件路径 ',
`start_time` varchar(20)  DEFAULT NULL COMMENT  ' 检查开始时间 ',
`end_time` varchar(20)  DEFAULT NULL COMMENT  ' 检查结束时间 ',
`task_status` int(11)  DEFAULT NULL COMMENT  ' 任务执行状态，0未开始，1检查中，2检查完成 ',
`file_total` int(11)  DEFAULT NULL COMMENT  ' 检查文件总数 ',
`warn_file_total` int(11)  DEFAULT NULL COMMENT  ' 预警文件总数 ',
`detail` text  DEFAULT NULL COMMENT  ' 任务描述 ',
`create_time` varchar(20)  DEFAULT NULL COMMENT  ' 创建时间 ',
`create_uid` int(11)  DEFAULT NULL COMMENT  ' 创建人 ',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- 任务预警表
-- ----------------------------
CREATE TABLE`dlp_check_record`(
`id` int(11) NOT NULL AUTO_INCREMENT ,
`file_id` varchar(50)  DEFAULT NULL COMMENT  ' 文件第三方ID ',
`file_bar_code` varchar(255)  DEFAULT NULL COMMENT  ' 文件条码ID ',
`file_name` varchar(500)  DEFAULT NULL COMMENT  ' 文件名称 ',
`file_source` int(11)  DEFAULT NULL COMMENT  ' 文件来源类型，1刻录，2打印 ',
`file_type` int(11)  DEFAULT NULL COMMENT  ' 文件类型 ',
`file_size` varchar(255)  DEFAULT NULL COMMENT  ' 文件大小 ',
`file_disk_path` varchar(500)  DEFAULT NULL COMMENT  ' 文件在第三方系统存放位置 ',
`origin_label` varchar(255)  DEFAULT NULL COMMENT  ' 原密级 ',
`content` longtext  DEFAULT NULL COMMENT  ' 文件正文 ',
`feature` text DEFAULT NULL COMMENT  ' 特征词 ',
`predict_type` int(11)  DEFAULT NULL COMMENT  ' 预测方式，0模型预测；1触发词匹配 ',
`cwords` text  DEFAULT NULL COMMENT  ' 密级触发词 ',
`create_time` varchar(20)  DEFAULT NULL COMMENT  ' 文件提交时间 ',
`create_uid` int(11)  DEFAULT NULL COMMENT  ' 文件提交人 ',
`dpt_id` varchar(255)  DEFAULT NULL COMMENT  ' 文件提交人部门 ',
`task_id` int(11)  DEFAULT NULL COMMENT  ' 任务ID ',
`check_time` varchar(20)  DEFAULT NULL COMMENT  ' 检查时间 ',
`check_status` int(11)  DEFAULT NULL COMMENT  ' 检查状态，0尚未检查，1正常，2预警 ',
`check_label` varchar(255)  DEFAULT NULL COMMENT  ' 预测密级 ',
`check_detail` varchar(255)  DEFAULT NULL COMMENT  ' 检查结果说明 ',
`review_status` int(11)  DEFAULT NULL COMMENT  ' 人工校核，0尚未查实，1查实取消，2查实确认 ',
`review_uid` int(11)  DEFAULT NULL COMMENT  ' 审核人ID ',
`review_time` varchar(20)  DEFAULT NULL COMMENT  ' 审核时间 ',
`review_detail` varchar(255)  DEFAULT NULL COMMENT  ' 审核结果说明 ',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- 部门表
-- ----------------------------
CREATE TABLE`dlp_department`(
`id` int(11) NOT NULL AUTO_INCREMENT ,
`name` varchar(255)  DEFAULT NULL COMMENT  ' 部门名称 ',
`detail` text  DEFAULT NULL COMMENT  ' 描述 ',
`create_time` varchar(20)  DEFAULT NULL COMMENT  ' 创建时间 ',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

