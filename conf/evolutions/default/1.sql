# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ingredient (
  id                            bigserial not null,
  name                          varchar(255),
  category                      varchar(255),
  properties                    varchar(255),
  constraint pk_ingredient primary key (id)
);

create table nutrition (
  id                            bigserial not null,
  portion_size                  varchar(255),
  calories                      float not null,
  total_fat                     float not null,
  saturated_fat                 float not null,
  cholesterol                   float not null,
  total_carbohydrates           float not null,
  fiber                         float not null,
  sugar                         float not null,
  protein                       float not null,
  constraint pk_nutrition primary key (id)
);

create table recipe (
  id                            bigserial not null,
  title                         varchar(255),
  categories                    varchar(255),
  source                        varchar(255),
  portions                      integer,
  time                          float,
  calification                  float,
  steps                         varchar(255),
  id_nutrition                  bigint,
  constraint uq_recipe_id_nutrition unique (id_nutrition),
  constraint pk_recipe primary key (id)
);

create table step (
  id                            bigserial not null,
  title                         varchar(255),
  description                   varchar(255),
  time                          float,
  constraint pk_step primary key (id)
);

alter table recipe add constraint fk_recipe_id_nutrition foreign key (id_nutrition) references nutrition (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists recipe drop constraint if exists fk_recipe_id_nutrition;

drop table if exists ingredient cascade;

drop table if exists nutrition cascade;

drop table if exists recipe cascade;

drop table if exists step cascade;

