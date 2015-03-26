
create table chars
(
  id bigint not null,
  hash integer not null,
  chars text not null,
  constraint chars_pk primary key (id)
);

create index chars_hash_ix
  on chars (hash)
  using btree;

create table datatypes
(
  stereotype varchar(255) not null, /*mysql requires varchar for indexing, rather than text*/ 
  category text not null,
  constraint datatypes_pk primary key (stereotype)
);

/* -- Cannot create this index, as MySQL cannot index text fields 
create index datatypes_category_ix
  on datatypes (category)
  using btree;
*/

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
  stereotype varchar(255) not null, /*stereotype is varchar for datatypes table, as mysql requires varchar for indexing, rather than text*/
  constraint types_pk primary key (id)
);

create index types_stereotype_ix
  on types (stereotype)
  using btree;

create index types_repository_ix
  on types (repository)
  using btree;

/* -- Cannot create this index, as MySQL cannot index text fields 
create index types_path_ix
  on types (path)
  using btree;
*/
