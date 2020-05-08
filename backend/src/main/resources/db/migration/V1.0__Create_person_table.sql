create table ACCOUNT(
  ID bigint not null auto_increment,
  PASSPORT varchar(40) not null,
  PASSWORD varchar(40) not null,
  CREATE_AT timestamp not null default current_timestamp,
  ENABLED tinyint(1) not null default 1,
  primary key (ID)
)