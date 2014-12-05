/*
drop database "objectof.repo";

create database "objectof.repo"
  with owner = postgres
       encoding = 'UTF8'
       tablespace = pg_default
       connection limit  = -1;
*/
drop schema public cascade;
create schema public authorization postgres;

create table repositories
(
  id serial not null,
  uniform_name text not null,
  schema_name text,
  schema_version text,
  schema_impl text,
  schema_metadata text,
  constraint repositories_pk primary key (id)
);

-- drop table datatypes cascade;

create table datatypes
(
  stereotype text not null,
  category text not null,
  constraint datatypes_pk primary key (stereotype)
);

create index datatypes_category_ix
  on datatypes using btree
  (category);

-- truncate datatypes;

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

-- drop table types;

create table types
(
  id smallint not null,
  repository_id integer not null,
  path text not null,
  stereotype text not null,
  constraint types_pk primary key (id)
);

create index types_stereotype_ix
  on types using btree
  (stereotype);

create index types_repository_ix
  on types using btree
  (repository_id);

create index types_path_ix
  on types using btree
  (path);

-- drop view types_vw;

create or replace view types_vw as 
 select 
    repositories.uniform_name as repository,
    types.id, types.path, 
    datatypes.stereotype, datatypes.category
   from types, datatypes, repositories
  where types.stereotype = datatypes.stereotype
  and types.repository_id = repositories.id
;

--drop table chars;

create table chars
(
  id bigint not null,
  hash integer not null,
  chars text not null,
  constraint chars_pk primary key (id)
);

create index chars_hash_ix
  on chars using btree
  (hash);