package com.nhnacademy.springmvcfinal.repository.impl;

import com.nhnacademy.springmvcfinal.domain.inquiry.InquiryFile;
import com.nhnacademy.springmvcfinal.repository.InquiryFileRepository;
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
public class InquiryFileRepositoryImpl implements InquiryFileRepository {

    private final DataSource dataSource;

    @Override
    public void save(InquiryFile inquiryFile) {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = "insert into spring_inquiry_files(inquiry_id, file_name) values(?,?)";

        try(PreparedStatement psmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1, inquiryFile.getInquiryId());
            psmt.setString(2, inquiryFile.getFileName());

            try(ResultSet rs = psmt.getGeneratedKeys()) {
                if(rs.next()) {
                    inquiryFile.setFileId(rs.getInt(1));
                }
            }
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<InquiryFile> findByInquiryId(int inquiryId) {

        Connection connection = DataSourceUtils.getConnection(dataSource);

        String sql = "select file_id, inquiry_id, file_name from spring_inquiry_files where inquiry_id = ?";

        List<InquiryFile> result = new ArrayList<>();

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {

            psmt.setInt(1, inquiryId);
            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    InquiryFile file = new InquiryFile(
                            rs.getInt("file_id"),
                            rs.getInt("inquiry_id"),
                            rs.getString("file_name")
                    );

                    result.add(file);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

        return result;
    }
}
