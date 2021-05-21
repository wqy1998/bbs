# 校园论坛

## 资料

[Spring 文档](https://spring.io/guides)  
[Spring Web](https://spring.io/guides/gs/serving-web-content/)  
[es 社区](https://elasticsearch.cn/explore)  
[GitHub OAuth](https://docs.github.com/en/developers/apps/creating-an-oauth-app)  
[GitHub deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)  
[BootStrap](https://v3.bootcss.com/getting-started/)  
[Spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)  
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)  
[Mybatis generator](https://mybatis.org/generator/running/runningWithMaven.html)  
[Markdown 插件](http://editor.md.ipandao.com/)  

## 工具

[Git](https://git-scm.com/download)  
[Visual Paradigm](https://www.visual-paradigm.com)  
[Lombok](https://www.projectlombok.org)

## 脚本

```sql
create table USER
(
    ID           BIGINT auto_increment,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    BIO          VARCHAR(256),
    AVATAR_URL   VARCHAR(100),
    PASSWORD     VARCHAR(20),
    EMAIL        VARCHAR(100),
    constraint USER_PK
        primary key (ID)
);

create unique index USER_EMAIL_UINDEX
    on USER (EMAIL);
```

```sql
create table COMMENT
(
    ID            BIGINT auto_increment,
    PARENT_ID     BIGINT        not null,
    TYPE          INT           not null,
    COMMENTATOR   BIGINT        not null,
    GMT_CREATE    BIGINT        not null,
    GMT_MODIFIED  BIGINT        not null,
    LIKE_COUNT    INT default 0 not null,
    CONTENT       VARCHAR(1024) not null,
    COMMENT_COUNT INT default 0,
    constraint COMMENT_PK
        primary key (ID)
);

comment on column COMMENT.PARENT_ID is '父类id';

comment on column COMMENT.TYPE is '父类类型';

comment on column COMMENT.GMT_CREATE is '创建时间';

comment on column COMMENT.GMT_MODIFIED is '更新时间';
```

```sql
create table NOTIFICATION
(
    ID            BIGINT auto_increment,
    NOTIFIER      BIGINT        not null,
    RECEIVER      BIGINT        not null,
    OUTER_ID      BIGINT        not null,
    TYPE          INT           not null,
    GMT_CREATE    BIGINT        not null,
    STATUS        INT default 0 not null,
    NOTIFIER_NAME VARCHAR(100),
    OUTER_TITLE   VARCHAR(256),
    constraint NOTIFICATION_PK
        primary key (ID)
);
```

```sql
create table QUESTION
(
    ID            BIGINT auto_increment,
    TITLE         VARCHAR(50),
    DESCRIPTION   TEXT,
    GMT_CREATE    BIGINT,
    GMT_MODIFIED  BIGINT,
    CREATOR       BIGINT,
    COMMENT_COUNT INT default 0,
    VIEW_COUNT    INT default 0,
    LIKE_COUNT    INT default 0,
    TAG           VARCHAR(256),
    constraint QUESTION_PK
        primary key (ID)
);
```
```sql
create table SPECIALTY
(
    ID         BIGINT auto_increment,
    NAME       VARCHAR(256) not null,
    GMT_CREATE BIGINT,
    constraint SPECIALTY_PK
        primary key (ID)
);

create unique index SPECIALTY_NAME_UINDEX
    on SPECIALTY (NAME);


```
```sql
create table STUDENT_CLASS
(
    ID           BIGINT auto_increment,
    SPECIALTY_ID BIGINT       not null,
    CLASS_NAME   VARCHAR(256) not null,
    GMT_CREATE   BIGINT,
    constraint STUDENT_CLASS_PK
        primary key (ID)
);
```

```bash
mvn -Dmybatis.generator.overwrite=true  mybatis-generator:generate
```
