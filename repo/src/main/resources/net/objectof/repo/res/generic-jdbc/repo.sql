
create table chars
(
  id bigint not null,
  hash integer not null,
  chars text not null,
  constraint chars_pk primary key (id)
);

create table blobs
(
	id bigint not null,
	hash integer not null,
	bytes blob not null,
	constraint blobs_pk primary key (id)
);

create table datatypes
(
  stereotype varchar(255) not null,
  category text not null,
  constraint datatypes_pk primary key (stereotype)
);

insert into datatypes values ('COMPOSED', 'INSTANCE');
insert into datatypes values ('INDEXED', 'INSTANCE');
insert into datatypes values ('MAPPED', 'INSTANCE');
insert into datatypes values ('INT', 'SCALAR');
insert into datatypes values ('NUM', 'SCALAR');
insert into datatypes values ('MOMENT', 'SCALAR');
insert into datatypes values ('BN', 'SCALAR');
insert into datatypes values ('TEXT', 'TXT');
insert into datatypes values ('MEDIA', 'BIN');
insert into datatypes values ('REF', 'REF');


create table repositories
(
  uniform_name_txt bigint not null,
  impl_txt bigint not null,
  schema_name text,
  schema_version text,
  schema_impl text,
  schema_metadata text,
  constraint repositories_pk primary key (uniform_name_txt)
);

create table types
(
  id smallint not null,
  repository bigint not null,
  path text not null,
  stereotype varchar(255) not null,
  target smallint,
  constraint types_pk 
  primary key (id)
);
