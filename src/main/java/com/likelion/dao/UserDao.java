 package com.likelion.dao;

import com.likelion.domain.User;
import java.sql.*;

 public class UserDao {

    private AwsConnectionMaker connectionMaker;

    public UserDao(AwsConnectionMaker awsConnectionMaker){
        this.connectionMaker = new AwsConnectionMaker();
    }

    public void add(User user) {
        try {

            Connection c = connectionMaker.makeConnection();

            PreparedStatement pstmt = c.prepareStatement("INSERT INTO users(id, name, password) VALUES(?,?,?);");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());

            pstmt.executeUpdate();

            pstmt.close();
            c.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(String id) {
        Connection c;
        try {
            c = connectionMaker.makeConnection();

            PreparedStatement pstmt = c.prepareStatement("SELECT * FROM users WHERE id = ?");
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            User user = new User(rs.getString("id"), rs.getString("name"),
                    rs.getString("password"));

            rs.close();
            pstmt.close();
            c.close();

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao(new AwsConnectionMaker());
        User user = userDao.findById("6");
        System.out.println(user.getName());
    }
}