create table lc_bd_user(
	id int primary key auto_increment,
	skey varchar(100),
	createtime char(19),
	lastvisittime char(19),
	sessionkey varchar(100),
	appsession varchar(50),
	openid varchar(100)
);
