-- ----------------------------
-- 4、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
  menu_name         varchar(50)     not null                   comment '菜单名称',
  parent_id         bigint(20)      default 0                  comment '父菜单ID',
  order_num         int(4)          default 0                  comment '显示顺序',
  path              varchar(200)    default ''                 comment '路由地址',
  component         varchar(255)    default null               comment '组件路径',
  query             varchar(255)    default null               comment '路由参数',
  is_frame          int(1)          default 1                  comment '是否为外链（0是 1否）',
  is_cache          int(1)          default 0                  comment '是否缓存（0缓存 1不缓存）',
  menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
  visible           char(1)         default '0'                comment '菜单状态（0显示 1隐藏）',
  status            char(1)         default '0'                comment '菜单状态（0正常 1停用）',
  perms             varchar(100)    default null               comment '权限标识',
  icon              varchar(100)    default '#'                comment '菜单图标',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values('1', '系统管理', '0', '1', 'system',           null, '', 1, 0, 'M', '0', '0', '', 'system',   'admin', sysdate(), '', null, '系统管理目录');
insert into sys_menu values('2', '系统监控', '0', '2', 'monitor',          null, '', 1, 0, 'M', '0', '0', '', 'monitor',  'admin', sysdate(), '', null, '系统监控目录');
insert into sys_menu values('3', '系统工具', '0', '3', 'tool',             null, '', 1, 0, 'M', '0', '0', '', 'tool',     'admin', sysdate(), '', null, '系统工具目录');
insert into sys_menu values('4', '若依官网', '0', '4', 'http://ruoyi.vip', null, '', 0, 0, 'M', '0', '0', '', 'guide',    'admin', sysdate(), '', null, '若依官网地址');

-- 二级菜单
insert into sys_menu values('100',  '用户管理', '1',   '1', 'user',           'system/user/index',        '', 1, 0, 'C', '0', '0', 'system:user:list',        'user',          'admin', sysdate(), '', null, '用户管理菜单');
insert into sys_menu values('101',  '角色管理', '1',   '2', 'role',           'system/role/index',        '', 1, 0, 'C', '0', '0', 'system:role:list',        'peoples',       'admin', sysdate(), '', null, '角色管理菜单');
insert into sys_menu values('102',  '菜单管理', '1',   '3', 'menu',           'system/menu/index',        '', 1, 0, 'C', '0', '0', 'system:menu:list',        'tree-table',    'admin', sysdate(), '', null, '菜单管理菜单');
insert into sys_menu values('103',  '部门管理', '1',   '4', 'dept',           'system/dept/index',        '', 1, 0, 'C', '0', '0', 'system:dept:list',        'tree',          'admin', sysdate(), '', null, '部门管理菜单');
insert into sys_menu values('104',  '岗位管理', '1',   '5', 'post',           'system/post/index',        '', 1, 0, 'C', '0', '0', 'system:post:list',        'post',          'admin', sysdate(), '', null, '岗位管理菜单');
insert into sys_menu values('105',  '字典管理', '1',   '6', 'dict',           'system/dict/index',        '', 1, 0, 'C', '0', '0', 'system:dict:list',        'dict',          'admin', sysdate(), '', null, '字典管理菜单');
insert into sys_menu values('106',  '参数设置', '1',   '7', 'config',         'system/config/index',      '', 1, 0, 'C', '0', '0', 'system:config:list',      'edit',          'admin', sysdate(), '', null, '参数设置菜单');
insert into sys_menu values('107',  '通知公告', '1',   '8', 'notice',         'system/notice/index',      '', 1, 0, 'C', '0', '0', 'system:notice:list',      'message',       'admin', sysdate(), '', null, '通知公告菜单');
insert into sys_menu values('108',  '日志管理', '1',   '9', 'log',            '',                         '', 1, 0, 'M', '0', '0', '',                        'log',           'admin', sysdate(), '', null, '日志管理菜单');
insert into sys_menu values('109',  '在线用户', '2',   '1', 'online',         'monitor/online/index',     '', 1, 0, 'C', '0', '0', 'monitor:online:list',     'online',        'admin', sysdate(), '', null, '在线用户菜单');
insert into sys_menu values('110',  '定时任务', '2',   '2', 'job',            'monitor/job/index',        '', 1, 0, 'C', '0', '0', 'monitor:job:list',         'job',           'admin', sysdate(), '', null, '定时任务菜单');
insert into sys_menu values('111',  '数据监控', '2',   '3', 'druid',          'monitor/druid/index',      '', 1, 0, 'C', '0', '0', 'monitor:druid:list',       'druid',         'admin', sysdate(), '', null, '数据监控菜单');
insert into sys_menu values('112',  '服务监控', '2',   '4', 'server',         'monitor/server/index',     '', 1, 0, 'C', '0', '0', 'monitor:server:list',      'server',        'admin', sysdate(), '', null, '服务监控菜单');
insert into sys_menu values('113',  '缓存监控', '2',   '5', 'cache',          'monitor/cache/index',      '', 1, 0, 'C', '0', '0', 'monitor:cache:list',       'redis',         'admin', sysdate(), '', null, '缓存监控菜单');
insert into sys_menu values('114',  '表单构建', '3',   '1', 'build',          'tool/build/index',         '', 1, 0, 'C', '0', '0', 'tool:build:list',          'build',         'admin', sysdate(), '', null, '表单构建菜单');
insert into sys_menu values('115',  '代码生成', '3',   '2', 'gen',            'tool/gen/index',           '', 1, 0, 'C', '0', '0', 'tool:gen:list',            'code',          'admin', sysdate(), '', null, '代码生成菜单');
insert into sys_menu values('116',  '系统接口', '3',   '3', 'swagger',        'tool/swagger/index',       '', 1, 0, 'C', '0', '0', 'tool:swagger:list',        'swagger',       'admin', sysdate(), '', null, '系统接口菜单');

-- 用户管理按钮
insert into sys_menu values('1001', '用户查询', '100', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1002', '用户新增', '100', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1003', '用户修改', '100', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1004', '用户删除', '100', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1005', '用户导出', '100', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:export',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1006', '用户导入', '100', '6',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:import',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1007', '重置密码', '100', '7',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd',     '#', 'admin', sysdate(), '', null, '');

-- 角色管理按钮
insert into sys_menu values('1008', '角色查询', '101', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1009', '角色新增', '101', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1010', '角色修改', '101', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1011', '角色删除', '101', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1012', '角色导出', '101', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单管理按钮
insert into sys_menu values('1013', '菜单查询', '102', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1014', '菜单新增', '102', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1015', '菜单修改', '102', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1016', '菜单删除', '102', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove',       '#', 'admin', sysdate(), '', null, '');

-- 部门管理按钮
insert into sys_menu values('1017', '部门查询', '103', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1018', '部门新增', '103', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1019', '部门修改', '103', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1020', '部门删除', '103', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove',       '#', 'admin', sysdate(), '', null, '');

-- 岗位管理按钮
insert into sys_menu values('1021', '岗位查询', '104', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1022', '岗位新增', '104', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1023', '岗位修改', '104', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1024', '岗位删除', '104', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1025', '岗位导出', '104', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:export',       '#', 'admin', sysdate(), '', null, '');

-- 字典管理按钮
insert into sys_menu values('1026', '字典查询', '105', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1027', '字典新增', '105', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1028', '字典修改', '105', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1029', '字典删除', '105', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1030', '字典导出', '105', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export',       '#', 'admin', sysdate(), '', null, '');

-- 参数设置按钮
insert into sys_menu values('1031', '参数查询', '106', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:config:query',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1032', '参数新增', '106', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:config:add',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1033', '参数修改', '106', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1034', '参数删除', '106', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove',     '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1035', '参数导出', '106', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:config:export',     '#', 'admin', sysdate(), '', null, '');

-- 通知公告按钮
insert into sys_menu values('1036', '公告查询', '107', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1037', '公告新增', '107', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1038', '公告修改', '107', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1039', '公告删除', '107', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove',     '#', 'admin', sysdate(), '', null, '');

-- 操作日志按钮
insert into sys_menu values('1040', '操作查询', '500', '1',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query',    '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1041', '操作删除', '500', '2',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1042', '日志导出', '500', '3',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export',   '#', 'admin', sysdate(), '', null, '');

-- 登录日志按钮
insert into sys_menu values('1043', '登录查询', '501', '1',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1044', '登录删除', '501', '2',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1045', '日志导出', '501', '3',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', sysdate(), '', null, '');

-- 在线用户按钮
insert into sys_menu values('1046', '在线查询', '109', '1',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1047', '批量强退', '109', '2',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout','#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1048', '单条强退', '109', '3',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout','#', 'admin', sysdate(), '', null, '');

-- 定时任务按钮
insert into sys_menu values('1049', '任务查询', '110', '1',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1050', '任务新增', '110', '2',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1051', '任务修改', '110', '3',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1052', '任务删除', '110', '4',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1053', '状态修改', '110', '5',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1054', '任务导出', '110', '6',  '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export',        '#', 'admin', sysdate(), '', null, '');

-- 代码生成按钮
insert into sys_menu values('1055', '生成查询', '115', '1',  '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1056', '生成修改', '115', '2',  '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1057', '生成删除', '115', '3',  '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1058', '导入代码', '115', '4',  '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1059', '预览代码', '115', '5',  '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1060', '生成代码', '115', '6',  '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',            '#', 'admin', sysdate(), '', null, ''); 