package com.nhnacademy.springmvcfinal.repository.impl;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.springmvcfinal.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepository {
    private final DataSource dataSource;

    @Override
    public List<Inquiry> getInquiryList(String userId, Category category) {
        List<Inquiry> inquiries = new ArrayList<>();
        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql;

        if(category == null) {
            sql = "select inquiry_id, title, content, category, created_at, user_id from spring_inquiry where user_id = ?";
        }else {
            sql = "select inquiry_id, title, content, category, created_at, user_id from spring_inquiry where user_id = ? and category = ?";
        }
        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);

            if(category != null) {
                psmt.setInt(2, category.getValue());
            }

            try(ResultSet rs = psmt.executeQuery()) {
                while(rs.next()) {
                    inquiries.add(new Inquiry(
                            rs.getInt("inquiry_id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            Category.from(rs.getInt("category")),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getString("user_id")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // TODO-Q 이거 하는 이유
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return inquiries;
    }

    @Override
    public List<Inquiry> findUnansweredInquiries() {

        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = """
                    SELECT i.inquiry_id, i.title, i.content, i.category, i.created_at, i.user_id
                    FROM spring_inquiry i
                    LEFT JOIN spring_answer a
                        ON i.inquiry_id = a.inquiry_id
                    WHERE a.inquiry_id IS NULL
                    ORDER BY i.inquiry_id DESC
                """;

        List<Inquiry> result = new ArrayList<>();

        try (PreparedStatement psmt = connection.prepareStatement(sql);
             ResultSet rs = psmt.executeQuery()) {

            while (rs.next()) {

                Inquiry inquiry = new Inquiry(
                        rs.getInt("inquiry_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        Category.from(rs.getInt("category")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("user_id")
                );

                result.add(inquiry);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

        return result;
    }

    @Override
    public Inquiry getInquiry(int inquiryId) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        String sql = "select * from spring_inquiry where inquiry_id = ?";

        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setInt(1, inquiryId);

            try(ResultSet rs = psmt.executeQuery()) {
                if(rs.next()) {
                    return new Inquiry(
                            inquiryId,
                            rs.getString("title"),
                            rs.getString("content"),
                            Category.from(rs.getInt("category")),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getString("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return null;
    }

    @Override
    public int registerInquiry(Inquiry inquiry) {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = "insert into spring_inquiry(title, content, category, created_at, user_id) values(?,?,?,?,?)";

        try(PreparedStatement psmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setString(1, inquiry.getTitle());
            psmt.setString(2, inquiry.getContent());
            psmt.setInt(3, inquiry.getCategory().getValue());
            psmt.setTimestamp(4, Timestamp.valueOf(inquiry.getCreatedAt()));
            psmt.setString(5, inquiry.getUserId());

            psmt.executeUpdate();

            try(ResultSet rs = psmt.getGeneratedKeys()) {
                if (rs.next()) {
                    inquiry.setInquiryId(rs.getInt(1));
                }
            }
            return inquiry.getInquiryId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}
