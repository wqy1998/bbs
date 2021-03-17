package com.wqy.campusbbs.mapper;

import com.wqy.campusbbs.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name, account_id, token, gmt_create, gmt_modified， avatar_url) values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}), #{avatarUrl}")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where email = #{email}")
    User findByEmail(@Param("email") String email);

    @Insert("insert into user (name, account_id, token, gmt_create, gmt_modified, password, email) values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{password}, #{email})")
    void register(User user);

    @Select("select * from user where email = #{email} and password = #{password}")
    User findByEmailAndPassword(User user);
}
