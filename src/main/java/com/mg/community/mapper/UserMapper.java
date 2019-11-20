package com.mg.community.mapper;

import com.mg.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user (ACCOUNT_ID,NAME,TOKEN,GMT_CREATE,GMT_MODIFIED) " +
            "values (#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified})")
    public void insertUser(User user);

    @Select("select * from user where token = #{token}")
    User findUserByToken(String token);
}
