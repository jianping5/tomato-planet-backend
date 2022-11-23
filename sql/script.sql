create table tomato_planet.collection
(
    id           bigint auto_increment comment '主键'
        primary key,
    user_id      bigint                             not null comment '用户id',
    topic_id     bigint                             not null comment '主题id',
    is_collect   tinyint  default 0                 not null comment '是否收藏（0：未收藏 1：已收藏）',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    collect_time datetime default CURRENT_TIMESTAMP null comment '收藏时间'
)
    comment '收藏表';

create table tomato_planet.comment
(
    id                bigint auto_increment comment '主键'
        primary key,
    user_id           bigint                             not null comment '评论发布用户id',
    topic_id          bigint                             not null comment '评论主题id',
    comment_content   varchar(2048)                      not null comment '评论内容',
    location          varchar(255)                       null comment '发布者地理位置',
    parent_comment_id bigint                             null comment '父评论id',
    comment_time      datetime default CURRENT_TIMESTAMP null comment '评论时间'
)
    comment '评论表';

create table tomato_planet.focus_record
(
    id             bigint auto_increment comment '主键'
        primary key,
    user_id        bigint                             not null comment '用户id',
    todo_item_id   bigint                             not null comment '待办项id',
    focus_time     int                                not null comment '待办专注时长（分钟）',
    focus_end_time datetime default CURRENT_TIMESTAMP not null comment '待办专注结束时间'
)
    comment '专注记录表';

create table tomato_planet.todo_item
(
    id                        bigint auto_increment comment '主键'
        primary key,
    user_id                   bigint                             not null comment '用户id',
    todo_name                 varchar(512)                       not null comment '待办名称',
    todo_total_time           int                                not null comment '待办总时长（分钟）',
    todo_background_image_url varchar(255)                       null comment '待办背景图片',
    todo_create_time          datetime default CURRENT_TIMESTAMP null comment '待办创建时间',
    todo_update_time          datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '待办修改时间',
    is_deleted                tinyint  default 0                 not null comment '是否删除（逻辑删除）'
)
    comment '待办项表';

create table tomato_planet.topic
(
    id                bigint auto_increment comment '主键'
        primary key,
    user_id           bigint                             not null comment '用户id',
    topic_title       varchar(255)                       not null comment '主题标题',
    topic_content     varchar(2048)                      not null comment '主题内容',
    tag               varchar(512)                       null comment '标签',
    image_url         varchar(2048)                      null comment '图片url',
    like_count        bigint   default 0                 not null comment '点赞数',
    collect_count     bigint   default 0                 not null comment '收藏数',
    comment_count     bigint   default 0                 not null comment '评论数',
    location          varchar(255)                       null comment '发布者地理位置',
    topic_create_time datetime default CURRENT_TIMESTAMP null comment '主题发表时间',
    topic_update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '主题修改时间'
)
    comment '主题表';

create table tomato_planet.topic_like
(
    id          bigint auto_increment comment '主键'
        primary key,
    user_id     bigint                             not null comment '用户id',
    topic_id    bigint                             not null comment '主题id',
    is_like     tinyint  default 0                 not null comment '是否点赞（0：未点赞 1：已点赞）',
    create_time datetime default CURRENT_TIMESTAMP null comment '点赞时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '点赞表';

create table tomato_planet.user
(
    id               bigint auto_increment comment '主键'
        primary key,
    user_account     varchar(255)                       not null comment '账号',
    user_password    varchar(255)                       null comment '密码',
    user_name        varchar(255)                       null comment '用户昵称',
    avatar_url       varchar(255)                       null comment '头像url',
    user_Email       varchar(255)                       not null comment '邮箱',
    user_role        int      default 0                 not null comment '用户角色（0：普通用户 1：管理员）',
    user_create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    user_update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted       tinyint  default 0                 not null comment '是否删除（逻辑删除）'
)
    comment '用户表';


