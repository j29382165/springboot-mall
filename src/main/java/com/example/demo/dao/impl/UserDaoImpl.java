package com.example.demo.dao.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.dto.UserRegisterRequest;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.example.demo.rowmapper.UserRowMapper;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao { //加Component,讓UserDaoImpl成為Bean

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO user(email, password, created_date,last_modified_date) " +
                "VALUES(:email,:password,:createdDate,:lastModifiedDate)";
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int userId = keyHolder.getKey().intValue();
        return userId;


    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM user WHERE user_id = :userId";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userId", userId);
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM user WHERE email = :email";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("email", email);
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    //整批加密之前明碼,執行過就關閉
//    @Override
//    public List<User> getAllUsers() {
//        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM user";
//        return namedParameterJdbcTemplate.query(sql, new UserRowMapper());
//    }
//
//    @Override
//    public void updateUserPassword(Integer userId, String hashedPassword) {
//        String sql = "UPDATE user SET password = :hashedPassword WHERE user_id = :userId";
//        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("userId", userId);
//        map.put("hashedPassword", hashedPassword);
//        namedParameterJdbcTemplate.update(sql, map);
//    }


}
