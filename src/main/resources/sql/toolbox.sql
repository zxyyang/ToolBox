-- --------------------------------------------------------
-- 主机:                           47.117.143.194
-- 服务器版本:                        5.7.24 - MySQL Community Server (GPL)
-- 服务器操作系统:                      linux-glibc2.12
-- HeidiSQL 版本:                  10.3.0.5794
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 toolbox 的数据库结构
CREATE DATABASE IF NOT EXISTS `toolbox` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `toolbox`;

-- 导出  表 toolbox.file 结构
CREATE TABLE IF NOT EXISTS `file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) NOT NULL,
  `file_path` varchar(100) NOT NULL DEFAULT '/',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.file 的数据：~9 rows (大约)
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` (`id`, `file_name`, `file_path`) VALUES
	(132, 'java工具/', '/'),
	(133, '毕业设计.zip', '/java工具/'),
	(135, '1.png', '/'),
	(136, '1', '/'),
	(138, '···/', '/'),
	(139, 'To The.png', '/···/'),
	(140, 'asd/', '/···/'),
	(141, 'zzzz/', '/···/asd/'),
	(142, '新建文本文档 (2).txt', '/');
/*!40000 ALTER TABLE `file` ENABLE KEYS */;

-- 导出  表 toolbox.note 结构
CREATE TABLE IF NOT EXISTS `note` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `note_name` text,
  `note_content` longtext,
  `note_remark` text,
  `note_type` varchar(50) DEFAULT NULL,
  `note_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='笔记列表';

-- 正在导出表  toolbox.note 的数据：~22 rows (大约)
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
INSERT INTO `note` (`id`, `note_name`, `note_content`, `note_remark`, `note_type`, `note_time`) VALUES
	(4, '服务器IP', '47.117.143.194', NULL, '1', '2021-08-26 00:00:00'),
	(5, 'java报文的收发', 'https://blog.csdn.net/lolly1023/article/details/113112403', '从事csdn', '1', '2021-08-26 00:00:00'),
	(6, '前端表格上加深颜色', ':show-overflow-tooltip="true"', 'vue', '1', '2021-08-26 00:00:00'),
	(7, '前段添加表头', 'https://www.cnblogs.com/yhleng/p/13048931.html', NULL, '2', '2021-08-26 00:00:00'),
	(8, 'Vue -- table多表头，在表头中添加按钮', '<el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">\n      <el-table-column label="全部用户">\n        <el-table-column type="selection" align="center" label-class-name="allSelection" width="80" />\n        <el-table-column :render-header="renderHeader">\n          <template slot-scope="scope">\n            <div class="user-wrapper">\n                <img :src="scope.row.photo" alt="">\n                  {{ scope.row.nickName }}\n            </div>\n          </template>\n        </el-table-column>\n      </el-table-column>\n</el-table>\n renderHeader(h) {\n      return (\n        <el-button class=\'filter-item\' type=\'default\' size="mini" icon=\'el-icon-delete\' onClick={() => this.handleAddBlackList()} loading={this.changeListLoading}>加入黑名单</el-button>\n      )\n}\n\n\n但是这种方法element-ui会提示一个报错[Element Warn][TableColumn]Comparing to render-header, scoped-slot header is easier to use. We recommend users to use scoped-slot header.，根据提示我们使用插槽形式\n\n<el-table-column scoped-slot>\n  <template slot="header">\n    <div class="px14">\n      <el-button class="filter-item" type="default" size="mini" icon="el-icon-delete" :loading="changeListLoading" :disabled="disabledBtn" @click="handleRemoveBlacklist">移出黑名单</el-button>\n    </div>\n  </template>\n  <template slot-scope="{row}">\n    <div class="user-wrapper flex items-center px14">\n      <div class="inline-block mr8" style="height: 36px;">\n        <el-avatar :key="row.id" :size="36" :src="row.photo" />\n      </div>\n      <div class="pro-wrapper">{{ scope.row.nickName }}</div>\n    </div>\n  </template>\n</el-table-column>\n注意：如果使用el-avatar组件，必须定义key', 'https://www.cnblogs.com/lisaShare/p/13398560.html', '2', '2021-08-26 00:00:00'),
	(9, '前段表单自动序号递增', '<el-table-column fixed label="序号" min-width="10%"   align="center">\n        <template scope="scope">\n          <span>{{(queryParams.pageNum-1)*queryParams.pageSize+(scope.$index + 1)}} </span>\n        </template>\n      </el-table-column>', '前段', '2', '2021-08-26 00:00:00'),
	(10, 'idea项目搭建错误', 'https://www.cnblogs.com/ae6623/p/14096342.html', NULL, '2', '2021-08-26 00:00:00'),
	(11, 'Lombok', '（compiler里面加上）-Djps.track.ap.dependencies=false', NULL, '2', '2021-08-26 00:00:00'),
	(12, '端口占用', 'netstat -ano |findstr "端口号"\ntaskkill /f /t /im "进程id或者进程名称"', NULL, '1', '2021-08-26 00:00:00'),
	(13, 'The driver has not received any packets from the server', 'url: jdbc:mysql://10.40.2.23:3306/synapsis_bg?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8', 'ssl=false', '1', '2021-08-26 00:00:00'),
	(14, '单元测试', '3.4.2 断言的常用方法\n\n\nassertEquals(100, x): 断言相等\n\nassertArrayEquals({1, 2, 3}, x): 断言数组相等\n\nassertEquals(3.1416, x, 0.0001): 浮点数组断言相等\n\nassertNull(x): 断言为null\n\nassertTrue(x > 0): 断言为true\n\nassertFalse(x < 0): 断言为false;\n\nassertNotEquals: 断言不相等\n\nassertNotNull: 断言不为null\n\n3.5 使用@Before和@After\n在@Before方法中初始化测试资源\n在@After方法中释放测试资源\n@BeforeClass: 初始化非常耗时的资源, 例如创建数据库\n@AfterClass: 清理@BeforeClass创建的资源, 例如创建数据库\n3.5.1 对于每一个@Test方法的执行顺序\n注意:** 单个@Test方法执行前会创建新的XxxTest实例, 实例变量的状态不会传递给下一个@Test方法, 单个@Test方法执行前后会执行@Before和@After方法\n\n执行类的构造函数\n\n执行@Before方法\n\n执行@Test方法\n\n执行@After方法', 'https://www.cnblogs.com/lingshang/p/10950947.html', '1', '2021-08-26 00:00:00'),
	(15, '安全验证', 'spring security配置', '矿建框架', '1', '2021-08-26 00:00:00'),
	(17, '启动redis', '/etc/init.d/redis-server start', 'https://blog.csdn.net/panlee1991/article/details/82931582', '1', '2021-08-26 00:00:00'),
	(18, '‘vue-cli-service’ 不是内部或外部命令，也不是可运行的程序', 'npm install --global vue-cli', 'https://blog.csdn.net/i6725545/article/details/89953673', '1', '2021-08-26 00:00:00'),
	(19, 'java学习', 'https://space.bilibili.com/95256449/video?tid=0&page=1&keyword=&order=pubdate', 'b站视频', '1', '2021-08-26 00:00:00'),
	(20, '文件管理', 'python -m SimpleHTTPServer 8000', 'nohup', '1', '2021-08-26 00:00:00'),
	(21, 'mybatis对象映射/注解/默认就有查询条件', 'mybatis.type-aliases-package=com.neo.entity\nmybatis.configuration.map-underscore-to-camel-case=true', NULL, '1', '2021-08-26 00:00:00'),
	(22, '七牛', 'https://developer.qiniu.com/kodo/1239/java#6', NULL, '1', '2021-08-26 00:00:00'),
	(23, '分页', '        <dependency>\r\n            <groupId>com.github.pagehelper</groupId>\r\n            <artifactId>pagehelper-spring-boot-starter</artifactId>\r\n            <version>1.2.5</version>\r\n        </dependency>\r\n        <dependency>\r\n            <groupId>com.github.pagehelper</groupId>\r\n            <artifactId>pagehelper-spring-boot-starter</artifactId>\r\n            <version>1.2.5</version>\r\n        </dependency>\r\n        <dependency>\r\n            <groupId>com.github.pagehelper</groupId>\r\n            <artifactId>pagehelper-spring-boot-starter</artifactId>\r\n            <version>1.2.5</version>\r\n        </dependency>\r\n', 'https://www.cnblogs.com/LiangPF/p/10764852.html', NULL, NULL),
	(24, 'python实现文件下载', '> # python -m SimpleHTTPServer', 'linux', '1', '2021-08-26 00:00:00'),
	(25, 'zxyang.cn', NULL, '项目介绍', NULL, NULL);
/*!40000 ALTER TABLE `note` ENABLE KEYS */;

-- 导出  表 toolbox.permissions 结构
CREATE TABLE IF NOT EXISTS `permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permissions_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.permissions 的数据：~6 rows (大约)
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` (`id`, `permissions_name`) VALUES
	(1, 'add'),
	(2, 'delete'),
	(3, 'update'),
	(4, 'select'),
	(5, 'list'),
	(6, 'super'),
	(7, 'query'),
	(8, 'down');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;

-- 导出  表 toolbox.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.role 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`, `role_name`) VALUES
	(1, 'admin'),
	(2, 'test'),
	(3, 'else');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- 导出  表 toolbox.role_permission 结构
CREATE TABLE IF NOT EXISTS `role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `permissions_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.role_permission 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` (`id`, `role_id`, `permissions_id`) VALUES
	(1, 1, 1),
	(2, 1, 2),
	(3, 1, 3),
	(4, 1, 4),
	(5, 1, 5),
	(6, 1, 6),
	(7, 2, 4),
	(8, 2, 5),
	(9, 2, 7),
	(10, 1, 7),
	(11, 1, 8),
	(12, 2, 8);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;

-- 导出  表 toolbox.users 结构
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL COMMENT '盐',
  `label` varchar(255) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.users 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `user_name`, `password`, `salt`, `label`) VALUES
	(1, 'zxyang', '123456', '123', '管理员'),
	(2, 'test', 'test', '123', '测试');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- 导出  表 toolbox.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.user_role 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES
	(1, 1, 1),
	(2, 1, 3),
	(3, 2, 2);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
