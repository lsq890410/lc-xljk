package com.lc.xljk.dao.mapper.user;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lc.xljk.vo.user.UserVO;

public interface UserMapper {
	@Select("SELECT * FROM lc_bd_user")
    List<UserVO> getAll();

    @Select("SELECT * FROM lc_bd_user WHERE id = #{id}")
    HashMap<String, Object> getMap(int id);

    @Insert("INSERT INTO lc_bd_user(skey,createtime,lastvisittime,sessionkey,appsession,openid) "
    		+ "VALUES(#{skey}, #{createtime}, #{lastvisittime},#{sessionkey}, #{appsession}, #{openid})")
    void insert(UserVO user);

    @Update("UPDATE lc_bd_user SET sessionkey=1 WHERE id =#{id}")
    void update(UserVO user);

    @Delete("DELETE FROM lc_bd_user WHERE id =#{id}")
    void delete(int id);
    
    @Select("SELECT * FROM lc_bd_user WHERE id = #{id}")
    UserVO getOne(int id);
    
    @Select("SELECT id FROM lc_bd_user WHERE id = #{id}")
    int getId(int id);
    
    @Select("SELECT * FROM lc_bd_user WHERE openid = #{openId}")
    UserVO getOneByOpenID(String openId);
}
