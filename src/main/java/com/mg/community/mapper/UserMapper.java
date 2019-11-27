package com.mg.community.mapper;

import com.mg.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user (ACCOUNT_ID,NAME,TOKEN,GMT_CREATE,GMT_MODIFIED,AVATAR_URL) " +
            "values (#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    public void insertUser(User user);

    @Select("select * from user where token = #{token}")
    User findUserByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findUserById(@Param("id") int id);

    @Select("select * from user where account_id = #{account_id}")
    User findUserByAccountId(@Param("account_id")  String account_id);
}
