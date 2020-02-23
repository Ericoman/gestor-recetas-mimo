# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                            bigserial not null,
  name                          varchar(255),
  popularity                    float,
  constraint pk_category primary key (id)
);

create table ingredient (
  id                            bigserial not null,
  name                          varchar(255) not null,
  category                      varchar(255),
  properties                    varchar(255),
  constraint uq_ingredient_name unique (name),
  constraint pk_ingredient primary key (id)
);

create table nutrition (
  id                            bigserial not null,
  portion_size                  varchar(255) not null,
  calories                      float not null,
  total_fat                     float,
  saturated_fat                 float,
  cholesterol                   float,
  total_carbohydrates           float,
  fiber                         float,
  sugar                         float,
  protein                       float,
  constraint pk_nutrition primary key (id)
);

create table recipe (
  id                            bigserial not null,
  title                         varchar(255) not null,
  source                        varchar(255),
  portions                      integer,
  time                          float not null,
  calification                  float,
  id_nutrition                  bigint,
  constraint uq_recipe_id_nutrition unique (id_nutrition),
  constraint pk_recipe primary key (id)
);

create table recipe_category (
  recipe_id                     bigint not null,
  category_id                   bigint not null,
  constraint pk_recipe_category primary key (recipe_id,category_id)
);

create table recipe_ingredient (
  recipe_id                     bigint not null,
  ingredient_id                 bigint not null,
  constraint pk_recipe_ingredient primary key (recipe_id,ingredient_id)
);

create table step (
  id                            bigserial not null,
  number                        bigint not null,
  title                         varchar(255) not null,
  description                   varchar(255),
  time                          float,
  recipe_id                     bigint,
  constraint uq_step_number unique (number),
  constraint pk_step primary key (id)
);

alter table recipe add constraint fk_recipe_id_nutrition foreign key (id_nutrition) references nutrition (id) on delete restrict on update restrict;

create index ix_recipe_category_recipe on recipe_category (recipe_id);
alter table recipe_category add constraint fk_recipe_category_recipe foreign key (recipe_id) references recipe (id) on delete restrict on update restrict;

create index ix_recipe_category_category on recipe_category (category_id);
alter table recipe_category add constraint fk_recipe_category_category foreign key (category_id) references category (id) on delete restrict on update restrict;

create index ix_recipe_ingredient_recipe on recipe_ingredient (recipe_id);
alter table recipe_ingredient add constraint fk_recipe_ingredient_recipe foreign key (recipe_id) references recipe (id) on delete restrict on update restrict;

create index ix_recipe_ingredient_ingredient on recipe_ingredient (ingredient_id);
alter table recipe_ingredient add constraint fk_recipe_ingredient_ingredient foreign key (ingredient_id) references ingredient (id) on delete restrict on update restrict;

create index ix_step_recipe_id on step (recipe_id);
alter table step add constraint fk_step_recipe_id foreign key (recipe_id) references recipe (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists recipe drop constraint if exists fk_recipe_id_nutrition;

alter table if exists recipe_category drop constraint if exists fk_recipe_category_recipe;
drop index if exists ix_recipe_category_recipe;

alter table if exists recipe_category drop constraint if exists fk_recipe_category_category;
drop index if exists ix_recipe_category_category;

alter table if exists recipe_ingredient drop constraint if exists fk_recipe_ingredient_recipe;
drop index if exists ix_recipe_ingredient_recipe;

alter table if exists recipe_ingredient drop constraint if exists fk_recipe_ingredient_ingredient;
drop index if exists ix_recipe_ingredient_ingredient;

alter table if exists step drop constraint if exists fk_step_recipe_id;
drop index if exists ix_step_recipe_id;

drop table if exists category cascade;

drop table if exists ingredient cascade;

drop table if exists nutrition cascade;

drop table if exists recipe cascade;

drop table if exists recipe_category cascade;

drop table if exists recipe_ingredient cascade;

drop table if exists step cascade;

