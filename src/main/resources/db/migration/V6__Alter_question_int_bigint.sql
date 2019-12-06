alter table QUESTION alter column ID BIGINT default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_990E5ED5_6D6B_4BD7_BD82_C313C485607C) auto_increment;

alter table QUESTION alter column COMMENT_COUNT BIGINT default 0;

alter table QUESTION alter column VIEW_COUNT BIGINT default 0;

alter table QUESTION alter column LIKE_COUNT BIGINT default 0;

