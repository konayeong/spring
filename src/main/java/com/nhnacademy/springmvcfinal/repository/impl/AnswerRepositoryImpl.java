package com.nhnacademy.springmvcfinal.repository.impl;

import com.nhnacademy.springmvcfinal.domain.inquiry.Answer;
import com.nhnacademy.springmvcfinal.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final DataSource dataSource;

    @Override
    public boolean existsByInquiryId(int inquiryId) {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = "select 1 from spring_answer where inquiry_id = ? limit 1";

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setInt(1, inquiryId);

            try (ResultSet rs = psmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Optional<Answer> findByInquiryId(int inquiryId) {

        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = """
                    select answer_id, inquiry_id, admin_id, content, created_at
                    from spring_answer
                    where inquiry_id = ?
                """;

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {

            psmt.setInt(1, inquiryId);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    Answer answer = new Answer(
                            rs.getInt("answer_id"),
                            rs.getInt("inquiry_id"),
                            rs.getString("admin_id"),
                            rs.getString("content"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    return Optional.of(answer);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public void save(Answer answer) {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = "insert into spring_answer(inquiry_id, admin_id, content, created_at) values(?,?,?,?)";

        try (PreparedStatement psmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1, answer.getInquiryId());
            psmt.setString(2, answer.getAdminId());
            psmt.setString(3, answer.getContent());
            psmt.setTimestamp(4, Timestamp.valueOf(answer.getCreatedAt()));

            psmt.executeUpdate();

            try(ResultSet rs = psmt.getGeneratedKeys()) {
                if(rs.next()) {
                    answer.setAnswerId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}