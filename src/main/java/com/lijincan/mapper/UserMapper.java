package com.lijincan.mapper;

import com.lijincan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    public User queryUserByName(@Param("name") String name, @Param("password") String password);
}
