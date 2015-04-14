create table elements
(
  aid bigint not null,
  k bigint not null,
  v bigint not null,
  constraint elements_pk primary key (aid, k)
);

create view parts as 
select 
 aid,
 types.id as kid,
 types.path,
 types.stereotype,
 bitand(elements.aid, 0xFFFFFFFFFFFF) as num,
 elements.k,
 elements.v
from
 elements,
 types
where
 aid / power(2, 48) = types.id;

 
 
 CREATE VIEW members AS 
 SELECT parts.aid,
    parts.path AS object_path,
    parts.stereotype AS object_stereotype,
    parts.num as label,
    types.path AS member_path,
    types.stereotype AS member_stereotype,
    parts.v AS value,
    types.repository
   FROM parts,
    types
  WHERE parts.stereotype = 'COMPOSED' AND parts.k = types.id;
 
 create view fields as
 select 
  parts.path as kind, 
  parts.num as label, 
  types.path as member, 
  chars.chars as value 
 from 
  parts 
  inner join chars on parts.v = chars.id 
  inner join types on types.id = parts.k
 where
  parts.stereotype = 'COMPOSED';


create table versions
(
  id integer not null,
  time_stamp bigint not null,
  user_txt bigint not null,
  constraint tx_pk primary key (id)
);


create table version_elements
(
  tx integer not null,
  op character(1) not null,
  aid bigint not null,
  k bigint not null,
  v bigint not null,
  constraint version_elements_pk primary key (aid, k, tx)
);