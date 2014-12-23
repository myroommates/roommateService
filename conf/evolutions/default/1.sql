# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table event (
  id                        bigint not null,
  description               Text not null,
  start_date                timestamp not null,
  end_date                  timestamp not null,
  repeatable_frequency      integer,
  home_id                   bigint not null,
  creator_id                bigint not null,
  constraint ck_event_repeatable_frequency check (repeatable_frequency in (0,1,2,3)),
  constraint pk_event primary key (id))
;

create table home (
  id                        bigint not null,
  constraint pk_home primary key (id))
;

create table roommate (
  id                        bigint not null,
  name                      varchar(255) not null,
  name_abrv                 varchar(255) not null,
  email                     varchar(255) not null,
  reactivation_key          varchar(255),
  home_id                   bigint not null,
  authenticationKey         varchar(255),
  language                  integer not null,
  icon_color                float not null,
  keep_session_open         boolean,
  cookie_value              varchar(255),
  password                  varchar(255) not null,
  is_admin                  boolean default false not null,
  constraint ck_roommate_language check (language in (0,1)),
  constraint uq_roommate_email unique (email),
  constraint pk_roommate primary key (id))
;

create table shopping_item (
  id                        bigint not null,
  description               Text not null,
  creation_date             timestamp not null,
  home_id                   bigint not null,
  creator_id                bigint not null,
  was_bought                 boolean default false not null,
  constraint pk_shopping_item primary key (id))
;

create table ticket (
  id                        bigint not null,
  description               Text not null,
  category                  varchar(255),
  date                      timestamp not null,
  home_id                   bigint not null,
  payer_id                  bigint not null,
  constraint pk_ticket primary key (id))
;

create table ticket_debtor (
  id                        bigint not null,
  ticket_id                 bigint,
  roommate_id               bigint,
  value                     float not null,
  constraint pk_ticket_debtor primary key (id))
;

create sequence event_seq;

create sequence home_seq;

create sequence roommate_seq;

create sequence shopping_item_seq;

create sequence ticket_seq;

create sequence ticket_debtor_seq;

alter table event add constraint fk_event_home_1 foreign key (home_id) references home (id);
create index ix_event_home_1 on event (home_id);
alter table event add constraint fk_event_creator_2 foreign key (creator_id) references roommate (id);
create index ix_event_creator_2 on event (creator_id);
alter table roommate add constraint fk_roommate_home_3 foreign key (home_id) references home (id);
create index ix_roommate_home_3 on roommate (home_id);
alter table shopping_item add constraint fk_shopping_item_home_4 foreign key (home_id) references home (id);
create index ix_shopping_item_home_4 on shopping_item (home_id);
alter table shopping_item add constraint fk_shopping_item_creator_5 foreign key (creator_id) references roommate (id);
create index ix_shopping_item_creator_5 on shopping_item (creator_id);
alter table ticket add constraint fk_ticket_home_6 foreign key (home_id) references home (id);
create index ix_ticket_home_6 on ticket (home_id);
alter table ticket add constraint fk_ticket_payer_7 foreign key (payer_id) references roommate (id);
create index ix_ticket_payer_7 on ticket (payer_id);
alter table ticket_debtor add constraint fk_ticket_debtor_ticket_8 foreign key (ticket_id) references ticket (id);
create index ix_ticket_debtor_ticket_8 on ticket_debtor (ticket_id);
alter table ticket_debtor add constraint fk_ticket_debtor_roommate_9 foreign key (roommate_id) references roommate (id);
create index ix_ticket_debtor_roommate_9 on ticket_debtor (roommate_id);



# --- !Downs

drop table if exists event cascade;

drop table if exists home cascade;

drop table if exists roommate cascade;

drop table if exists shopping_item cascade;

drop table if exists ticket cascade;

drop table if exists ticket_debtor cascade;

drop sequence if exists event_seq;

drop sequence if exists home_seq;

drop sequence if exists roommate_seq;

drop sequence if exists shopping_item_seq;

drop sequence if exists ticket_seq;

drop sequence if exists ticket_debtor_seq;

