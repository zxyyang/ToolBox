/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 toolbox.file 结构
CREATE TABLE IF NOT EXISTS `file`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `file_name` varchar
(
    100
) NOT NULL,
    `file_path` varchar
(
    100
) NOT NULL DEFAULT '/',
    PRIMARY KEY
(
    `id`
)
    ) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.file 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` (`id`, `file_name`, `file_path`)
VALUES (132, 'java工具/', '/'),
       (133, '毕业设计.zip', '/java工具/');
/*!40000 ALTER TABLE `file` ENABLE KEYS */;

-- 导出  表 toolbox.note 结构
CREATE TABLE IF NOT EXISTS `note`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `note_name` text,
    `note_content` longtext,
    `note_remark` text,
    `note_type` varchar
(
    50
) DEFAULT NULL,
    `note_time` date DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    UNIQUE KEY `id`
(
    `id`
)
    ) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='笔记列表';

-- 正在导出表  toolbox.note 的数据：~24 rows (大约)
/*!40000 ALTER TABLE `note` DISABLE KEYS */;


-- 导出  表 toolbox.permissions 结构
CREATE TABLE IF NOT EXISTS `permissions`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `permissions_name` varchar
(
    255
) DEFAULT NULL,
    PRIMARY KEY
(
    `id`
)
    ) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.permissions 的数据：~6 rows (大约)
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` (`id`, `permissions_name`)
VALUES (1, 'add'),
       (2, 'delete'),
       (3, 'update'),
       (4, 'select'),
       (5, 'qurry'),
       (6, 'super');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;

-- 导出  表 toolbox.role 结构
CREATE TABLE IF NOT EXISTS `role`
(
    `id` int
(
    11
) NOT NULL AUTO_INCREMENT,
    `role_name` varchar
(
    255
) NOT NULL,
    `permissions_id` varchar
(
    20
) DEFAULT NULL,
    PRIMARY KEY
(
    `id`
)
    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.role 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`, `role_name`, `permissions_id`)
VALUES (1, 'admin', '6'),
       (2, 'A', '1,2'),
       (3, 'B', '3,4,5');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- 导出  表 toolbox.users 结构
CREATE TABLE IF NOT EXISTS `users`
(
    `id` int
(
    10
) NOT NULL AUTO_INCREMENT,
    `user_name` varchar
(
    255
) DEFAULT NULL,
    `password` varchar
(
    255
) NOT NULL,
    `salt` varchar
(
    255
) NOT NULL COMMENT '盐',
    `label` varchar
(
    255
) DEFAULT NULL COMMENT '类型',
    `role_id` varchar
(
    11
) DEFAULT NULL,
    PRIMARY KEY
(
    `id`
)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  toolbox.users 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `user_name`, `password`, `salt`, `label`, `role_id`)
VALUES (1, 'zxyang', '123456', '123', '管理员', '1'),
       (2, 'sa', '123456', '123', '测试', '2,3');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
