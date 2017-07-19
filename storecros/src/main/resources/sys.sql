USE `oauth2`;
/*插入客户端认证的数据 `oauth_client_details` */
INSERT  INTO `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`)
VALUES ('memoryclient','restResource','123456','read,write','password,refresh_token',NULL,'USER',NULL,NULL,NULL,NULL);

/*建一个用户表，以此作为登录的用户名密码,模拟权限 */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
   `id` varchar(128) COLLATE utf8_bin DEFAULT NULL,
   `username` varchar(256) COLLATE utf8_bin DEFAULT NULL,
   `password` varchar(10) COLLATE utf8_bin DEFAULT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into user(id, username, password) values (1,'panda','spring');