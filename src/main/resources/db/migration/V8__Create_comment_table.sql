create table comment
(
	id BIGINT auto_increment,
	parent_id BIGINT not null,
	content varchar(1024) not null,
	type int not null,
	like_count BIGINT default 0,
	commentator BIGINT,
	gmt_create BIGINT,
	gmt_modified BIGINT,
	constraint comment_pk
		primary key (id)
);

comment on table comment is '评论表';

comment on column comment.parent_id is '父类id';

comment on column comment.type is '父类类型-指明一二级';

comment on column comment.commentator is '评论者';

