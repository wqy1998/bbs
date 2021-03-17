# 校园论坛

## 资料

[Spring 文档](https://spring.io/guides)  
[Spring Web](https://spring.io/guides/gs/serving-web-content/)  
[es 社区](https://elasticsearch.cn/explore)  
[GitHub OAuth](https://docs.github.com/en/developers/apps/creating-an-oauth-app)  
[GitHub deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)  
[BootStrap](https://v3.bootcss.com/getting-started/)  
[Spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)

## 工具

[Git](https://git-scm.com/download)  
[Visual Paradigm](https://www.visual-paradigm.com)  
[Lombok](https://www.projectlombok.org)  

## 脚本

```sql
create table USER
(
    ID           INT auto_increment,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    BIO          VARCHAR(256),
    AVATAR_URL   VARCHAR(100),
    constraint USER_PK
        primary key (ID)
);
```

```sql
create table question
(
    id            int auto_increment,
    title         varchar(50),
    description   text,
    gmt_create    bigint,
    gmt_modified  bigint,
    creator       int,
    comment_count int default 0,
    view_count    int default 0,
    like_count    int default 0,
    tag           varchar(256),
    constraint QUESTION_PK
        primary key (id)
);
```
