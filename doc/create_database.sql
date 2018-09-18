create table mutant_code (
    id bigint not null auto_increment,
    filename varchar(255),
    filepath varchar(255),
    hashkey varchar(255) not null,
    dead bit,
    original_code_id bigint not null,
    primary key (id)
) engine=InnoDB

create table mutant_report (
    id bigint not null auto_increment,
    analysis_time bigint,
    difficulty integer,
    equivalence integer,
    registration_date datetime not null,
    mutant_code_id bigint,
    user_id bigint,
    primary key (id)
) engine=InnoDB

create table original_code (
    id bigint not null auto_increment,
    filename varchar(255),
    filepath varchar(255),
    hashkey varchar(255) not null,
    project_id bigint not null,
    primary key (id)
) engine=InnoDB

create table project (
    id bigint not null auto_increment,
    creation_date datetime not null,
    hashkey varchar(255) not null,
    name varchar(255) not null,
    directory_source varchar(255),
    giturl varchar(255),
    with_code bit not null,
    owner_id bigint,
    primary key (id)
) engine=InnoDB

create table project_admin (
    project_id bigint not null,
    admin_id bigint not null,
    primary key (project_id, admin_id)
) engine=InnoDB

create table project_group (
    id bigint not null auto_increment,
    creation_date datetime not null,
    hashkey varchar(255) not null,
    name varchar(255) not null,
    owner_id bigint,
    primary key (id)
) engine=InnoDB

create table project_group_admin (
    project_group_id bigint not null,
    admin_id bigint not null,
    primary key (project_group_id, admin_id)
) engine=InnoDB

create table project_group_rel_project (
    project_group_id bigint not null,
    project_id bigint not null,
    primary key (project_group_id, project_id)
) engine=InnoDB

create table user (
    id bigint not null auto_increment,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    registration_date datetime not null,
    primary key (id)
) engine=InnoDB

alter table project 
    drop index UK_tcrqp1tporbbmp3l5y9pkv76k

alter table project 
    add constraint UK_tcrqp1tporbbmp3l5y9pkv76k unique (hashkey)

alter table project_group 
    drop index UK_ri5ne07bg3w3whsml48yki2sh

alter table project_group 
    add constraint UK_ri5ne07bg3w3whsml48yki2sh unique (hashkey)

alter table user 
    drop index UK_ob8kqyqqgmefl0aco34akdtpe

alter table user 
    add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email)

alter table mutant_code 
    add constraint FKsqe0yebwhf0dl19q1kok60cyj 
    foreign key (original_code_id) 
    references original_code (id)

alter table mutant_report 
    add constraint FKgw3qbocwgyxig1h9m6ds4s856 
    foreign key (mutant_code_id) 
    references mutant_code (id)

alter table mutant_report 
    add constraint FKseaxfaxn06hxam0u0fkc2a3y 
    foreign key (user_id) 
    references user (id)

alter table original_code 
    add constraint FK877bxr85ohagxq7pmtr5wu52d 
    foreign key (project_id) 
    references project (id)

alter table project 
    add constraint FK9ydhxbq67a3m0ek560r2fq38g 
    foreign key (owner_id) 
    references user (id)

alter table project_admin 
    add constraint FKhwqdy6kbc6g50o6dqig2g2hrd 
    foreign key (project_id) 
    references project (id)

alter table project_admin 
    add constraint FKoeea6tnhihvmb5qy1heh2xa 
    foreign key (admin_id) 
    references user (id)

alter table project_group 
    add constraint FKcwo2ukwrrhtn9ujw547ml17na 
    foreign key (owner_id) 
    references user (id)

alter table project_group_admin 
    add constraint FKrad01okvmba0f57o6xleai3ir 
    foreign key (project_group_id) 
    references project_group (id)

alter table project_group_admin 
    add constraint FK20mx97ia986mhpayrgfoow6sr 
    foreign key (admin_id) 
    references user (id)

alter table project_group_rel_project 
    add constraint FKa0f48l03nhlwr6a22dc94xln 
    foreign key (project_group_id) 
    references project_group (id)

alter table project_group_rel_project 
    add constraint FK2sco1ls9f48lmefinmfmg7m85 
    foreign key (project_id) 
    references project (id)
