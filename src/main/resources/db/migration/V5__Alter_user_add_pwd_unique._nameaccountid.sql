alter table USER
	add pwd varchar(50);

create unique index USER_ACCOUNT_ID_uindex
	on USER (ACCOUNT_ID);

create unique index USER_NAME_uindex
	on USER (NAME);

create unique index USER_TOKEN_uindex
	on USER (TOKEN);

