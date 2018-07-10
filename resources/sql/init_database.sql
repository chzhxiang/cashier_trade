SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for SYS_USER 作废
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER`;
CREATE TABLE `SYS_USER` (
  `ID` varchar(32) NOT NULL DEFAULT '',
  `USER_NAME` varchar(40) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(500) NOT NULL COMMENT '密码',
  `FULLNAME` varchar(100) DEFAULT NULL COMMENT '全名',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `COMMENT` varchar(500) DEFAULT NULL COMMENT '备注',
  `TYPE` int(11) DEFAULT '1' COMMENT '账号类型，1平台、2商户',
  `OWNED_MERCHANT` varchar(32) DEFAULT NULL COMMENT '所属商户(ID)',
  `STATUS` int(11) NOT NULL DEFAULT '1' COMMENT '用户状态 1:启用 2:停用 默认为1',
  `CP` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CP_NAME` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `CT` datetime DEFAULT NULL COMMENT '创建时间',
  `EP` varchar(32) DEFAULT NULL COMMENT '修改人',
  `EP_NAME` varchar(50) DEFAULT NULL COMMENT '修改名称',
  `ET` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`),
  KEY `INX_SYS_USER_NAME` (`USER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SYS_USER  作废 
-- ----------------------------
INSERT INTO `SYS_USER` VALUES ('02cc58cc32834e8890e6a4b971cba7fd', 'admin', 'dQfzInbssuHMhqiYhXSeRUHXBOMkwhYZX40LjKGAKL4wqs4fSxQaL220VX7jTQEdpewqrQpq2pAQ+qgZ/DIwOekVQjnWHPR8lU2Q4j+dgRiHcNR7e0nG2xMArXgvBPUTLzdnr2lV1QPiltkFDNWVlTig9U+12+I7DHqHjrO4SkisvyFZGKJ5PLP1u0517sxPPaiAG0qPYDUOGHogyYNVBIKzlHvL8/pNX0HCXt7b/34xJRv+XbfFToORqAG1Jl1IpD5TIWnHGADIlK4sY/v4m3F230/GJl9d5JwNLE30Ti5xsoTKlgn9jDICSPgcHFETa7Jlwvmc3N0kBeyJa1NVgw==', '炒鸡管理员', null, null, '1', null, '1', null, null, null, 'cda90a440ac04831a14873d7a1bf75fc', 'baibu', '2017-09-20 16:35:05');

-- ----------------------------
-- Table structure for BIZ_MERCHANT
-- ----------------------------
DROP TABLE IF EXISTS `BIZ_MERCHANT`;
CREATE TABLE `BIZ_MERCHANT` (
  `ID` varchar(32) NOT NULL COMMENT '主键ID',
  `MERCHANT_NO` varchar(50) DEFAULT NULL COMMENT '商户号',
  `MERCHANT_NAME` varchar(255) DEFAULT NULL COMMENT '商户名称',
  `MERCHANT_ADDRESS` varchar(255) DEFAULT NULL COMMENT '商户联系地址',
  `LINKMAN_NAME` varchar(100) DEFAULT NULL COMMENT '联系人姓名',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '联系人电话',
  `QQ` varchar(20) DEFAULT NULL COMMENT '联系人QQ',
  `EMAIL` varchar(50) DEFAULT NULL COMMENT '联系人邮箱',
  `STATUS` int(11) DEFAULT '1' COMMENT '状态，0停用、1启用',
  `CP` varchar(32) DEFAULT NULL,
  `CP_NAME` varchar(100) DEFAULT NULL,
  `CT` datetime DEFAULT NULL,
  `EP` varchar(32) DEFAULT NULL,
  `EP_NAME` varchar(100) DEFAULT NULL,
  `ET` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户基本信息表';

-- ----------------------------
-- Table structure for BIZ_MERCHANT_APPLICATION
-- ----------------------------
DROP TABLE IF EXISTS `BIZ_MERCHANT_APPLICATION`;
CREATE TABLE `BIZ_MERCHANT_APPLICATION` (
  `ID` varchar(32) NOT NULL COMMENT '主键ID',
  `MERCHANT_NO` varchar(50) DEFAULT NULL COMMENT '商户号',
  `APP_IDENTIFY` varchar(30) DEFAULT NULL COMMENT '应用标识',
  `APP_NAME` varchar(255) DEFAULT NULL COMMENT '应用名称',
  `COMMENT` varchar(255) DEFAULT NULL COMMENT '项目备注',
  `VALIDATE_TIME` int(11) DEFAULT NULL COMMENT '时间校验,0否、1是',
  `APP_ID` varchar(50) DEFAULT NULL COMMENT 'APP ID',
  `APP_SECRET` varchar(50) DEFAULT NULL COMMENT 'APP Secret',
  `MASTER_SECRET` varchar(50) DEFAULT NULL COMMENT 'Master Secret',
  `PUBLIC_KEY` varchar(2000) DEFAULT NULL COMMENT '商户公钥,支付平台用此公钥对商户发起的交易数据进行验签',
  `APP_PUBLIC_KEY` varchar(2000) DEFAULT NULL COMMENT '阳光康众支付平台公钥',
  `APP_PRIVATE_KEY` varchar(2000) DEFAULT NULL COMMENT '阳光康众支付平台私钥',
  `STATUS` int(11) DEFAULT '1' COMMENT '状态，0停用、1启用',
  `CP` varchar(32) DEFAULT NULL,
  `CP_NAME` varchar(100) DEFAULT NULL,
  `CT` datetime DEFAULT NULL,
  `EP` varchar(32) DEFAULT NULL,
  `EP_NAME` varchar(100) DEFAULT NULL,
  `ET` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户应用表';

-- ----------------------------
-- Table structure for BIZ_TRADE_CHANNEL
-- ----------------------------
DROP TABLE IF EXISTS `BIZ_TRADE_CHANNEL`;
CREATE TABLE `BIZ_TRADE_CHANNEL` (
  `ID` varchar(32) NOT NULL COMMENT '主键ID',
  `CODE` varchar(50) DEFAULT NULL COMMENT '支付渠道编码',
  `VALUE` int(11) DEFAULT NULL COMMENT '支付渠道标识值',
  `NAME` varchar(50) DEFAULT NULL COMMENT '支付渠道名称',
  `ICON` varchar(100) DEFAULT NULL COMMENT '支付渠道图标',
  `SCENARIOS` varchar(100) DEFAULT NULL COMMENT '应用场景',
  `SETTLEMENT_CYCLE` varchar(30) DEFAULT NULL COMMENT '结算周期',
  `TYPE` int(11) DEFAULT NULL COMMENT '1手机WAP、2APP、3公众号、4服务窗',
  `SEQ` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `CP` varchar(32) DEFAULT NULL,
  `CP_NAME` varchar(100) DEFAULT NULL,
  `CT` datetime DEFAULT NULL,
  `EP` varchar(32) DEFAULT NULL,
  `EP_NAME` varchar(100) DEFAULT NULL,
  `ET` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `SEQ` (`SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='支付渠道表';

-- ----------------------------
-- Records of BIZ_TRADE_CHANNEL
-- ----------------------------
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('273f312b29974532a04cb506f40c95f0', 'unionpay_apple_pay', '12', '银联applepay支付', '/platform/images/demo.png', '移动网页', 'T+1', '3', '11', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('304d5865fadb489190d872d8a6871c7a', 'unionpay_sdk', '11', '银联SDK支付', '/platform/images/demo.png', '移动网页', 'T+1', '3', '10', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('41805e23e01f40e08454efea22ebd89f', 'alipay_h5', '8', '支付宝H5', '/platform/images/demo.png', '移动网页', 'T+1', '3', '6', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('496c805730a249bd853c7df04fb3cae8', 'wechat_instcard', '3', '微信医保', '/platform/images/demo.png', '移动网页', 'T+1', '3', '4', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('54e063f2ce9644e7949b8b7ce4a65425', 'alipay_instcard', '10', '支付宝医保', '/platform/images/demo.png', '移动网页', 'T+1', '3', '8', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('7d793ebafa4543ff969888d03a895e0d', 'wechat_app', '2', '微信APP支付(普通商户)', '/platform/images/demo.png', '移动网页', 'T+1', '3', '2', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('90780ee8e79c4e128f217a490f624be9', 'unionpay_ds', '14', '银联代收', '/platform/images/demo.png', '移动网页', 'T+1', '3', '12', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('9273a2ca34524d67b3fdb7ac75178256', 'alipay_app', '9', '支付宝APP', '/platform/images/demo.png', '移动网页', 'T+1', '3', '7', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('ba2bfdc9-c78a-407a-8d3f-fbe05452', 'wechat_jsapi', '1', '微信支付(普通商户)', '/platform/images/demo.png', '移动网页', 'T+1', '3', '1', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-13 10:58:27', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-13 10:58:30');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('bbee1b1ab88c4d3f956d03f2f56a22b8', 'sunshien_instcard', '16', '阳光康众医保支付', '/platform/images/demo.png', '移动网页', 'T+1', '3', '14', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('ccd8e93ec46a4f8a96b38106337e46a3', 'wechat_h5', '5', '微信H5', '/platform/images/demo.png', '移动网页', 'T+1', '3', '3', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('d4f52fa4cc7c49a3a7e58b4449ea76b8', 'wechat_scan_code', '4', '微信扫码', '/platform/images/demo.png', '移动网页', 'T+1', '3', '5', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('d901e763f42a4d278ccb7f521a0dc9fc', 'sunshien_wallet', '15', '医程通钱包', '/platform/images/demo.png', '移动网页', 'T+1', '5', '13', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-08 10:58:27', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-08 10:58:30');
INSERT INTO `BIZ_TRADE_CHANNEL` VALUES ('e523926a39ee4de0ae28def4962a82b7', 'unionpay_h5', '13', '银联H5支付', '/platform/images/demo.png', '移动网页', 'T+1', '3', '9', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32', '02cc58cc32834e8890e6a4b971cba7fd', 'admin', '2017-09-25 16:51:32');

-- ----------------------------
-- Table structure for BIZ_APPLICATION_TRADE_CHANNEL
-- ----------------------------
DROP TABLE IF EXISTS `BIZ_APPLICATION_TRADE_CHANNEL`;
CREATE TABLE `BIZ_APPLICATION_TRADE_CHANNEL` (
  `ID` varchar(32) NOT NULL COMMENT '主键ID',
  `APP_ID` varchar(50) DEFAULT NULL COMMENT 'APP_ID',
  `CHANNEL_CODE` varchar(50) DEFAULT NULL COMMENT '支付渠道编码',
  `VIEW_NAME` varchar(50) DEFAULT NULL COMMENT '前端显示名称',
  `PARAMS_JSON` varchar(4000) DEFAULT NULL COMMENT '支付渠道参数',
  `SEQ` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `IS_OPEN` int(32) NOT NULL DEFAULT '0' COMMENT '是否开启，0：未开启  1：开启',
  `CP` varchar(32) DEFAULT NULL,
  `CP_NAME` varchar(100) DEFAULT NULL,
  `CT` datetime DEFAULT NULL,
  `EP` varchar(32) DEFAULT NULL,
  `EP_NAME` varchar(100) DEFAULT NULL,
  `ET` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `SEQ` (`SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8 COMMENT='应用、支付渠道关联表';

------------------------------xiongdan 2017-11-19 15:34:00 --start--------------------------
-- ----------------------------
-- Table structure for `SYS_RESOURCE`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_RESOURCE`;
CREATE TABLE `SYS_RESOURCE` (
  `ID` varchar(32) NOT NULL DEFAULT '',
  `NAME` varchar(100) NOT NULL,
  `TYPE` varchar(40) NOT NULL,
  `STATUS` int(11) NOT NULL COMMENT '资源状态 1:正常 0:删除 2:禁用 默认为1',
  `CODE` varchar(50) NOT NULL DEFAULT '',
  `VALUE` varchar(255) DEFAULT NULL COMMENT '资源url',
  `CLASS_NAME` varchar(100) DEFAULT NULL COMMENT '对应的实体对象类',
  `SORT` int(11) DEFAULT NULL COMMENT '资源排序',
  `OPERATION_TYPE` int(11) DEFAULT NULL COMMENT '按钮显示方式 1 界面上端  2操作栏  3 两种都有 ',
  `DATA_AUTHORITY_CODE` varchar(500) DEFAULT NULL COMMENT '数据权限码',
  `PARENT_ID` varchar(32) DEFAULT '',
  `REMARK` varchar(500) DEFAULT NULL,
  `CP` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CP_NAME` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `CT` datetime DEFAULT NULL COMMENT '创建时间',
  `EP` varchar(32) DEFAULT NULL COMMENT '修改人',
  `EP_NAME` varchar(50) DEFAULT NULL COMMENT '修改名称',
  `ET` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限-资源';

-- ----------------------------
-- Records of SYS_RESOURCE
-- ----------------------------
INSERT INTO `SYS_RESOURCE` VALUES ('0d9e2fd564414746af50eb768a9e8271', '角色编辑', '1', '1', 'jsbj', '', '', null, null, '', '4', null, null, null, '2017-08-09 15:18:37', null, null, '2017-09-20 21:20:48');
INSERT INTO `SYS_RESOURCE` VALUES ('1', '系统管理', '1', '1', 'xtgl', '', null, null, '1', null, '', null, null, null, null, null, null, '2017-08-03 10:33:59');
INSERT INTO `SYS_RESOURCE` VALUES ('1755f84118af4a5da68c0cb4adf76f48', '取消', '2', '1', 'sys:role:edit:cancel', 'cancel', null, '2', '2', null, '0d9e2fd564414746af50eb768a9e8271', null, null, null, '2017-08-09 15:18:37', null, null, '2017-09-20 21:20:48');
INSERT INTO `SYS_RESOURCE` VALUES ('178c17ef21384428a2d012755fca0856', '用户新增', '2', '1', 'sys:user:list:create', 'toAdd', null, '1', '2', null, '43efcbd695904b5ea23fe0d8ac6ed590', null, null, null, '2017-08-08 10:50:46', null, null, '2017-09-20 21:18:45');
INSERT INTO `SYS_RESOURCE` VALUES ('248e50c603464935ad7bf46d2308ce49', '订单管理', '1', '1', 'ddgl', '', '', null, '1', '', '', null, null, null, '2017-11-01 19:46:09', null, null, null);
INSERT INTO `SYS_RESOURCE` VALUES ('2f4adc76ea2a427cb53aa953d3c1c261', '取消', '2', '1', 'sys:user:edit:cancel', 'cancel', null, '2', '2', null, '4fdb0f7864e047169cc5bddce638b2c4', null, null, null, '2017-08-08 11:03:35', null, null, '2017-09-20 21:19:34');
INSERT INTO `SYS_RESOURCE` VALUES ('3', '账号管理', '1', '1', 'zhgl', '/platform/user/list', '', '1', '1', '', '1', null, null, null, null, null, null, '2017-10-27 15:56:06');
INSERT INTO `SYS_RESOURCE` VALUES ('3653ec01f21e4920a5bc10a2a83f05ab', '银行卡管理', '1', '1', 'yhkgl', '', '', '4', '1', '', '', null, null, null, '2017-11-18 10:32:52', null, null, null);
INSERT INTO `SYS_RESOURCE` VALUES ('4', '角色管理', '1', '1', 'jsgl', '/platform/role/list', null, '2', '1', null, '1', null, null, null, null, null, null, '2017-08-09 15:19:31');
INSERT INTO `SYS_RESOURCE` VALUES ('43efcbd695904b5ea23fe0d8ac6ed590', '账号列表', '1', '1', 'zhlb', '/platform/user/list', '', null, null, '', '3', null, null, null, '2017-08-08 10:50:46', null, null, '2017-09-20 21:18:45');
INSERT INTO `SYS_RESOURCE` VALUES ('4fd01857249f462084bdda5e48d692c5', '用户删除', '2', '1', 'sys:user:list:delete', 'toDelete', null, '3', '2', null, '43efcbd695904b5ea23fe0d8ac6ed590', null, null, null, '2017-08-08 10:55:26', null, null, '2017-09-20 21:18:45');
INSERT INTO `SYS_RESOURCE` VALUES ('4fdb0f7864e047169cc5bddce638b2c4', '账号编辑', '1', '1', 'zhbj', '', '', null, null, '', '3', null, null, null, '2017-08-08 11:03:35', null, null, '2017-09-20 21:19:34');
INSERT INTO `SYS_RESOURCE` VALUES ('5', '资源管理', '1', '1', 'zygl', '/platform/resource/list', null, '3', '1', null, '1', null, null, null, '2016-04-12 17:44:56', null, null, '2017-08-04 12:05:14');
INSERT INTO `SYS_RESOURCE` VALUES ('56e22a3aa1b0454ba7186091035b495e', '角色修改', '2', '1', 'sys:role:list:update', 'toEdit', null, '2', '2', null, '8a04cd97bcd14036b089c1beca55977c', null, null, null, '2017-08-09 15:15:57', null, null, '2017-09-20 21:19:51');
INSERT INTO `SYS_RESOURCE` VALUES ('6dc1587ac28e44a2b3a6b97518c035ae', '资源列表', '1', '1', 'zylb', '', '', null, null, '', '5', null, null, null, '2017-08-08 11:50:23', null, null, '2017-09-20 21:16:32');
INSERT INTO `SYS_RESOURCE` VALUES ('7e505441cc314f14a94c28278005e2ca', '银行卡列表', '1', '1', 'yhklb', '/bankCard/bankCardList', '', '1', '1', '', '3653ec01f21e4920a5bc10a2a83f05ab', null, null, null, '2017-11-18 10:34:24', null, null, null);
INSERT INTO `SYS_RESOURCE` VALUES ('8a04cd97bcd14036b089c1beca55977c', '角色列表', '1', '1', 'jslb', '', '', null, null, '', '4', null, null, null, '2017-08-09 15:15:57', null, null, '2017-09-20 21:19:51');
INSERT INTO `SYS_RESOURCE` VALUES ('a6288e325139480ab8e065e4e03d625f', '商户管理', '1', '1', 'shgl', '', '', '3', '1', '', '', null, null, null, '2017-11-18 09:54:25', null, null, '2017-11-18 10:33:08');
INSERT INTO `SYS_RESOURCE` VALUES ('a694d2f956b349f483a5e8be72ae87c9', '资源修改', '2', '1', 'sys:resource:list:update', 'toEdit', null, '2', '2', null, '6dc1587ac28e44a2b3a6b97518c035ae', null, null, null, '2017-08-08 11:51:57', null, null, '2017-09-20 21:16:32');
INSERT INTO `SYS_RESOURCE` VALUES ('b02f3e35742b49a8aebe636a97ee2b8a', '商户列表', '1', '1', 'shlb', '/merchant/list', '', '1', '1', '', 'a6288e325139480ab8e065e4e03d625f', null, null, null, '2017-11-18 09:54:54', null, null, '2017-11-18 09:58:19');
INSERT INTO `SYS_RESOURCE` VALUES ('bd2c63ecf9794f46b848f884d24e6097', '用户修改', '2', '1', 'sys:user:list:update', 'toEdit', null, '2', '2', null, '43efcbd695904b5ea23fe0d8ac6ed590', null, null, null, '2017-08-08 10:55:26', null, null, '2017-09-20 21:18:45');
INSERT INTO `SYS_RESOURCE` VALUES ('bf3fe1e28c4e474bb68c98a43d2612f1', '保存', '2', '1', 'sys:resource:edit:save', 'saveOrUpdate', null, '1', '2', null, 'd462f998a5f5404aab37e3996ef71e3b', null, null, null, '2017-08-08 11:58:44', null, null, '2017-09-20 21:15:18');
INSERT INTO `SYS_RESOURCE` VALUES ('ca32249a68064f068acc874e714c1a86', '查看资料', '2', '1', 'viewDoctorInfo', '/platform/doctor/toCompleteDoctorInfo', null, '0', '1', null, '352ed2c279e34f2a9d797363693717d0', null, null, null, '2017-08-31 11:35:50', null, null, null);
INSERT INTO `SYS_RESOURCE` VALUES ('d26d2d55ff3149bd9677f9397d39b80f', '保存', '2', '1', 'sys:user:edit:save', 'saveOrUpdate', null, '1', '2', null, '4fdb0f7864e047169cc5bddce638b2c4', null, null, null, '2017-08-08 11:03:35', null, null, '2017-09-20 21:19:34');
INSERT INTO `SYS_RESOURCE` VALUES ('d462f998a5f5404aab37e3996ef71e3b', '资源编辑', '1', '1', 'zybj', '', '', null, null, '', '5', null, null, null, '2017-08-08 11:58:44', null, null, '2017-09-20 21:15:18');
INSERT INTO `SYS_RESOURCE` VALUES ('dfd8b569e48a4d108b65047e81be70d7', '订单列表', '1', '1', 'ddlb', '/order/orderList', '', '1', '1', '', '248e50c603464935ad7bf46d2308ce49', null, null, null, '2017-11-18 09:50:12', null, null, '2017-11-18 09:58:11');
INSERT INTO `SYS_RESOURCE` VALUES ('e42d46becb554851a2884e107244e800', '资源新增', '2', '1', 'sys:resource:list:create', 'toAdd', null, '1', '2', null, '6dc1587ac28e44a2b3a6b97518c035ae', null, null, null, '2017-08-08 11:51:57', null, null, '2017-09-20 21:16:32');
INSERT INTO `SYS_RESOURCE` VALUES ('e5a74030d6474e51ae32546588874b65', '资源删除', '2', '1', 'sys:resource:list:delete', 'toDelete', null, '3', '2', null, '6dc1587ac28e44a2b3a6b97518c035ae', null, null, null, '2017-08-08 11:51:57', null, null, '2017-09-20 21:16:32');
INSERT INTO `SYS_RESOURCE` VALUES ('e879564b1f5c415db967ea522b2d84fc', '保存', '2', '1', 'sys:role:edit:save', 'saveOrUpdate', null, '1', '2', null, '0d9e2fd564414746af50eb768a9e8271', null, null, null, '2017-08-09 15:18:37', null, null, '2017-09-20 21:20:48');
INSERT INTO `SYS_RESOURCE` VALUES ('eb224027fb064dbc9bf70c6aa289d7c6', '取消', '2', '1', 'sys:resource:edit:cancel', 'cancel', null, '2', '2', null, 'd462f998a5f5404aab37e3996ef71e3b', null, null, null, '2017-08-08 11:58:44', null, null, '2017-09-20 21:15:18');
INSERT INTO `SYS_RESOURCE` VALUES ('ee576a0e7014448c8cff626fa980625e', '角色新增', '2', '1', 'sys:role:list:create', 'toAdd', null, '1', '2', null, '8a04cd97bcd14036b089c1beca55977c', null, null, null, '2017-08-09 15:15:57', null, null, '2017-09-20 21:19:51');
INSERT INTO `SYS_RESOURCE` VALUES ('fc09bcc5528f45f5a08487a01c89935d', '角色删除', '2', '1', 'sys:role:list:delete', 'toDelete', null, '3', '2', null, '8a04cd97bcd14036b089c1beca55977c', null, null, null, '2017-08-09 15:15:57', null, null, '2017-09-20 21:19:51');

-- ----------------------------
-- Table structure for `SYS_ROLE`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE`;
CREATE TABLE `SYS_ROLE` (
  `ID` varchar(32) NOT NULL DEFAULT '',
  `ROLE_NAME` varchar(40) NOT NULL,
  `ROLE_DESC` varchar(500) DEFAULT '',
  `STATUS` int(11) NOT NULL COMMENT '角色状态 1:正常 0:删除 2:禁用 默认为1',
  `ROLE_CODE` varchar(50) NOT NULL DEFAULT '',
  `CP` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CP_NAME` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `CT` datetime DEFAULT NULL COMMENT '创建时间',
  `EP` varchar(32) DEFAULT NULL COMMENT '修改人',
  `EP_NAME` varchar(50) DEFAULT NULL COMMENT '修改名称',
  `ET` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限-角色';

-- ----------------------------
-- Records of SYS_ROLE
-- ----------------------------
INSERT INTO `SYS_ROLE` VALUES ('1', '系统管理员', null, '1', 'systemAdmin', null, null, '2016-04-11 09:33:12', null, null, null);

-- ----------------------------
-- Table structure for `SYS_ROLE_RESOURCE`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_RESOURCE`;
CREATE TABLE `SYS_ROLE_RESOURCE` (
  `ID` varchar(32) NOT NULL,
  `ROLE_ID` varchar(32) NOT NULL,
  `RESOURCE_ID` varchar(32) NOT NULL,
  `STATUS` int(11) NOT NULL COMMENT '状态 1:正常 0:删除 2:禁用 默认为1',
  PRIMARY KEY (`ID`),
  KEY `FK_PUB_ROLE_RE_AU` (`ROLE_ID`) USING BTREE,
  KEY `FK_PUB_ROLE_RE_RE` (`RESOURCE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限-角色资源关系';

-- ----------------------------
-- Records of SYS_ROLE_RESOURCE
-- ----------------------------
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('088f5c6606b343e28a9904bfed2537db', '1', '1755f84118af4a5da68c0cb4adf76f48', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('095aee46e6124ad38b18f785acb653da', '1', 'e5a74030d6474e51ae32546588874b65', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('0f47d0eabf98440e9209e14739885ca3', '1', '248e50c603464935ad7bf46d2308ce49', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('1a48c6554fac4b5a87e235ef2fa29b38', '1', 'fc09bcc5528f45f5a08487a01c89935d', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('2163f132de3a4e85b9fc479181c6e53e', '1', 'e42d46becb554851a2884e107244e800', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('236ee194ff01400fab72f85fd9e2cbe5', '1', '0d9e2fd564414746af50eb768a9e8271', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('2ae9b78fa625475b97333a5a54692382', '1', '1', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('2da9134252ee499d80d6658ebfb6dc73', '1', '3653ec01f21e4920a5bc10a2a83f05ab', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('35c7722664634e59885022fcf4a72d6c', '1', 'bd2c63ecf9794f46b848f884d24e6097', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('464b5379f8834c19ada45f538c2b67c5', '1', 'dfd8b569e48a4d108b65047e81be70d7', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('4ae8e09dbecd4b179a6ae32a48c40908', '1', 'a6288e325139480ab8e065e4e03d625f', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('4fdef360e07c4d61aeaab43c88ab09f2', '1', '8a04cd97bcd14036b089c1beca55977c', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('5ce88025e4844efc85ecbe2e6cf816f2', '1', '56e22a3aa1b0454ba7186091035b495e', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('5dd79c8f8273485f81359d7d0950747d', '1', '43efcbd695904b5ea23fe0d8ac6ed590', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('633f89f8a8a94220b6329fb1d6aa8e72', '1', 'bf3fe1e28c4e474bb68c98a43d2612f1', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('69d044625cc6433394b1e8422a01e045', '1', 'a694d2f956b349f483a5e8be72ae87c9', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('893f1e92ce8645878983f92cd694bf0f', '1', 'b02f3e35742b49a8aebe636a97ee2b8a', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('8e831e9286154d6f844446344488e016', '1', '7e505441cc314f14a94c28278005e2ca', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('a163128af5584aca8d01a5af4d0192b2', '1', '2f4adc76ea2a427cb53aa953d3c1c261', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('a60117a2f9a744948e4b1d842c283d99', '1', 'ee576a0e7014448c8cff626fa980625e', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('b84eefbd88804f59959e00eb1ebe7688', '1', '5', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('cb6f6e34a34043dc8d12f11beaa79b60', '1', '178c17ef21384428a2d012755fca0856', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('cbf73566c68e4352b7a2435b3b0053bc', '1', '3', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('d64d5bb6ff9940a0807b7389f3f853eb', '1', 'd26d2d55ff3149bd9677f9397d39b80f', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('dfa63a4c26fa4487aef22dfaf9cd1277', '1', '4fdb0f7864e047169cc5bddce638b2c4', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('eae95cc153584aaaa863e84b06346d79', '1', '4', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('eb485b20b6ea4e0ea6c7b6158aafda6f', '1', '6dc1587ac28e44a2b3a6b97518c035ae', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('ec85bf2bcef84c46aff71ba35c1109ec', '1', 'd462f998a5f5404aab37e3996ef71e3b', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('ee28d3513fb946798c12f54a47071fbf', '1', 'eb224027fb064dbc9bf70c6aa289d7c6', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('f4ebacd7946b4773a8e36aca7b984f57', '1', 'e879564b1f5c415db967ea522b2d84fc', '1');
INSERT INTO `SYS_ROLE_RESOURCE` VALUES ('f6983f5eede746149486f54a90e113e5', '1', '4fd01857249f462084bdda5e48d692c5', '1');

-- ----------------------------
-- Table structure for `SYS_USER`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER`;
CREATE TABLE `SYS_USER` (
  `ID` varchar(32) NOT NULL DEFAULT '',
  `ACCOUNT` varchar(40) NOT NULL COMMENT '帐号',
  `PASSWORD` varchar(500) NOT NULL COMMENT '密码',
  `NAME` varchar(50) DEFAULT NULL COMMENT '药材名',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `REMARK` varchar(500) DEFAULT NULL COMMENT '备注',
  `TYPE` int(11) DEFAULT '1' COMMENT '账号类型，1平台、2商户',
  `ROLE_IDS` varchar(250) DEFAULT NULL,
  `OWNED_MERCHANT` varchar(32) DEFAULT NULL COMMENT '所属商户(ID)',
  `OWNED_MERCHANT_NAME` varchar(100) DEFAULT NULL COMMENT '所属商户(NAME)',
  `OWNED_APPLICATION` varchar(50) DEFAULT NULL COMMENT '所属应用(ID)',
  `OWNED_APPLICATION_NAME` varchar(100) DEFAULT NULL COMMENT '所属应用(NAME)',
  `STATUS` int(11) NOT NULL DEFAULT '1' COMMENT '用户状态 1:正常 0:删除 2:禁用 默认为1',
  `CP` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CP_NAME` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `CT` datetime DEFAULT NULL COMMENT '创建时间',
  `EP` varchar(32) DEFAULT NULL COMMENT '修改人',
  `EP_NAME` varchar(50) DEFAULT NULL COMMENT '修改名称',
  `ET` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`),
  KEY `INX_SYS_USER_NAME` (`USER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SYS_USER
-- ----------------------------
INSERT INTO `SYS_USER` VALUES ('1', 'admin', '2e8991cbe62d5b84b793', '超级管理员', '18888888888', null, '1', '[\"1\"]', null, '', null, '', '1', null, null, '2017-11-18 18:04:02', null, null, '2017-11-18 18:07:24');

-- ----------------------------
-- Table structure for `SYS_USER_ROLE`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER_ROLE`;
CREATE TABLE `SYS_USER_ROLE` (
  `ID` varchar(32) NOT NULL,
  `USER_ID` varchar(32) NOT NULL,
  `ROLE_ID` varchar(32) NOT NULL,
  `STATUS` int(11) NOT NULL COMMENT '状态 1:正常 0:删除 2:禁用 默认为1',
  PRIMARY KEY (`ID`),
  KEY `FK_USERS_ROLES_ROLES` (`ROLE_ID`),
  KEY `FK_USERS_ROLES_USERS` (`USER_ID`),
  CONSTRAINT `FK_USERS_ROLES_ROLES` FOREIGN KEY (`ROLE_ID`) REFERENCES `SYS_ROLE` (`ID`),
  CONSTRAINT `FK_USERS_ROLES_USERS` FOREIGN KEY (`USER_ID`) REFERENCES `SYS_USER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限-用户角色关系';

-- ----------------------------
-- Records of SYS_USER_ROLE
-- ----------------------------
INSERT INTO `SYS_USER_ROLE` VALUES ('8c0e6b0280514b70927c2da9d1ef8f94', '1', '1', '1');


-- ----------------------------
-- Table structure for `SYS_RULE_CONFIG`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_RULE_CONFIG`;
CREATE TABLE `SYS_RULE_CONFIG` (
  `ID` varchar(32) NOT NULL,
  `RULE_CODE` varchar(50) NOT NULL COMMENT '规则编码',
  `RULE_VALUE` varchar(500) DEFAULT NULL COMMENT '规则值',
  `ATTR1` varchar(500) DEFAULT NULL COMMENT '扩展属性1',
  `ATTR2` varchar(500) DEFAULT NULL COMMENT '扩展属性2',
  `ATTR3` varchar(500) DEFAULT NULL COMMENT '扩展属性3',
  `ATTR4` varchar(500) DEFAULT NULL COMMENT '扩展属性4',
  `ATTR5` varchar(500) DEFAULT NULL COMMENT '扩展属性5',
  `ATTR6` varchar(500) DEFAULT NULL COMMENT '扩展属性6',
  `FLAG` int(1) DEFAULT NULL COMMENT '是否有效 1 有效 0 无效',
  `REMARK` varchar(100) DEFAULT NULL COMMENT '备注',
  `CP` varchar(32) DEFAULT NULL,
  `CT` datetime DEFAULT NULL,
  `EP` varchar(32) DEFAULT NULL,
  `ET` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SYS_RULE_CONFIG
-- ----------------------------
INSERT INTO `SYS_RULE_CONFIG` VALUES ('805870fea3494aae958d881a78b48869', 'consumption_amount_limit_rule', '2000', null, null, null, null, null, null, '1', null, null, '2017-11-30 17:31:16', null, '2017-12-05 11:50:18');
INSERT INTO `SYS_RULE_CONFIG` VALUES ('9bfd6dcc1f5e46efa62ab77fff253eda', 'bankcard_balance_limit_rule', '250', null, null, null, null, null, null, '1', null, null, '2017-11-30 17:30:44', null, '2017-12-05 11:50:42');


-- 创建函数

set global log_bin_trust_function_creators=TRUE;

-- ----------------------------
-- Function structure for getChildList
-- ----------------------------
DROP FUNCTION IF EXISTS getResourceChildList;
CREATE FUNCTION getResourceChildList(rootId text) RETURNS text CHARSET utf8
BEGIN   
DECLARE str text;  
DECLARE cid varchar(2000);   
SET str = '';   
SET cid = rootId;   
WHILE cid is not null DO 
    IF str != '' and str != cid THEN
			SET str = concat(str, ',', cid);   
		ELSE
       IF cid != rootId THEN
           SET str = cid;
			 END IF;	
    END IF;  
    
    SELECT group_concat(id) INTO cid FROM SYS_RESOURCE where FIND_IN_SET(PARENT_ID, cid) > 0;   
END WHILE;   
RETURN str;   
END;


-- ----------------------------
-- Function structure for getParentList
-- ----------------------------
DROP FUNCTION IF EXISTS getResourceParentList;
CREATE FUNCTION getResourceParentList(rootId varchar(100)) RETURNS text CHARSET utf8
BEGIN   
DECLARE fid varchar(100) default '';   
DECLARE str text;
SET str = ''; 
WHILE rootId is not null  do   
    SET fid =(SELECT PARENT_ID FROM SYS_RESOURCE  WHERE id = rootId);   
    IF fid is not null THEN  
        IF str = '' THEN  
				    SET str = fid;
				ELSE
						IF fid != '' THEN
               SET str = concat(str, ',', fid); 
            END IF;
				END IF;
        SET rootId = fid;   
    ELSE   
        SET rootId = fid;   
    END IF;   
END WHILE;   
return str;  
END;
------------------------------xiongdan 2017-11-19 15:34:00 --end--------------------------
