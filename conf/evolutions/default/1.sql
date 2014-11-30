# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table home (
  id                        bigint not null,
  creation_date             timestamp not null,
  last_update               timestamp not null,
  constraint pk_home primary key (id))
;

create table roommate (
  id                        bigint not null,
  creation_date             timestamp not null,
  last_update               timestamp not null,
  name                      varchar(255) not null,
  email                     varchar(255) not null,
  password                  varchar(255) not null,
  home_id                   bigint not null,
  authenticationKey         varchar(255),
  authentication_time       timestamp,
  icon_color                float not null,
  constraint uq_roommate_email unique (email),
  constraint pk_roommate primary key (id))
;

create table ticket (
  id                        bigint not null,
  creation_date             timestamp not null,
  last_update               timestamp not null,
  description               Text not null,
  value                     float not null,
  category                  varchar(255),
  date                      timestamp not null,
  home_id                   bigint not null,
  creator_id                bigint not null,
  constraint pk_ticket primary key (id))
;


create table mm_ticket_prayers (
  ticket_id                      bigint not null,
  roommate_id                    bigint not null,
  constraint pk_mm_ticket_prayers primary key (ticket_id, roommate_id))
;
create sequence home_seq;

create sequence roommate_seq;

create sequence ticket_seq;

alter table roommate add constraint fk_roommate_home_1 foreign key (home_id) references home (id);
create index ix_roommate_home_1 on roommate (home_id);
alter table ticket add constraint fk_ticket_home_2 foreign key (home_id) references home (id);
create index ix_ticket_home_2 on ticket (home_id);
alter table ticket add constraint fk_ticket_creator_3 foreign key (creator_id) references roommate (id);
create index ix_ticket_creator_3 on ticket (creator_id);



alter table mm_ticket_prayers add constraint fk_mm_ticket_prayers_ticket_01 foreign key (ticket_id) references ticket (id);

alter table mm_ticket_prayers add constraint fk_mm_ticket_prayers_roommate_02 foreign key (roommate_id) references roommate (id);

# --- !Downs

drop table if exists home cascade;

drop table if exists roommate cascade;

drop table if exists ticket cascade;

drop table if exists mm_ticket_prayers cascade;

drop sequence if exists home_seq;

drop sequence if exists roommate_seq;

drop sequence if exists ticket_seq;

