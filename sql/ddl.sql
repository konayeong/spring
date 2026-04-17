## 사용자 (고객, 관리자)
create table spring_users
(
    user_id   varchar(50)  not null primary key,
    password varchar(255) not null,
    name     varchar(100) not null,
    role     tinyint      not null
);

## 문의
create table spring_inquiry
(
    inquiry_id  int         auto_increment primary key,
    title      varchar(255) not null,
    content    text         null,
    category   tinyint      not null,
    created_at datetime     not null,
    user_id    varchar(50)  not null,
    constraint fk_inquiry_users
        foreign key (user_id) references spring_users (user_id)
);

## 문의-파일
create table spring_inquiry_files
(
    file_id      int          auto_increment primary key,
    inquiry_id   int not null,
    file_name    varchar(255) not null,
    constraint fk_inquiry_files_inquiry
        foreign key (inquiry_id) references spring_inquiry (inquiry_id)
);

## 답변
create table spring_answer
(
    answer_id   int        auto_increment primary key,
    inquiry_id  int        not null,
    admin_id    varchar(50)        not null,
    content     text       not null,
    created_at  datetime   not null,
    constraint fk_answer_inquiry
        foreign key (inquiry_id) references spring_inquiry (inquiry_id),
    constraint fk_answer_admin
        foreign key (admin_id) references spring_users (user_id)
)