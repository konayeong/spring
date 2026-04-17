package com.nhnacademy.springmvcfinal.repository.impl;

import com.nhnacademy.springmvcfinal.domain.user.Role;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource; // TODO-Q 과정 (RootConfig랑 차이)

    @Override
    public boolean matches(String id, String password) {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = "select password from spring_users where user_id = ?";

        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, id);

            try(ResultSet rs = psmt.executeQuery()) {
                if(rs.next()) {
                    return rs.getString("password").equals(password);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return false;
    }

    @Override
    public Optional<User> getUser(String id) {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = "select user_id, password, name, role from spring_users where user_id = ?";

        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, id);

            try(ResultSet rs = psmt.executeQuery()) {
                if(rs.next()) {
                    User user = User.create(
                            rs.getString("user_id"),
                            rs.getString("password"),
                            rs.getString("name"),
                            Role.from(rs.getInt("role"))
                    );
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return Optional.empty();
    }


}
