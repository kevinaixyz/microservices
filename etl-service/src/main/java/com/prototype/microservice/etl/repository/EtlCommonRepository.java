package com.prototype.microservice.etl.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("etlCommonRepository")
public class EtlCommonRepository {
    @PersistenceContext
    protected final EntityManager entityManager;
    DataSource dataSource;
    Connection conn = null;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            this.conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public EtlCommonRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public void test() {
        String queryStr = "select count(1) from warrant";
        Query query = entityManager.createNativeQuery(queryStr);
        Object res = query.getSingleResult();
        BigDecimal recNo = new BigDecimal(String.valueOf(res));
        System.out.println("==========>Test:" + recNo);
    }

    @Transactional(readOnly = true)
    public BigDecimal execCount(String sql) {
        //System.out.println("===>"+sql);
        Query query = entityManager.createNativeQuery(sql);
        Object res = query.getSingleResult();
        BigDecimal recNo = new BigDecimal(String.valueOf(res));
        return recNo;
    }

    @Transactional(readOnly = false)
    public int execUpdate(String sql) {
        //System.out.println("===>"+sql);
        Query query = entityManager.createNativeQuery(sql);
        int num = query.executeUpdate();
        //entityManager.flush();
        return num;
    }

    @Transactional(readOnly = false)
    public int insertDataByParams(String sql, List<? extends Object> values) {
        //System.out.println("===>"+sql);
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) instanceof String) {
                    pstmt.setString(i + 1, values.get(i).toString());
                    continue;
                }
                if (values.get(i) instanceof BigDecimal) {
                    pstmt.setBigDecimal(i + 1, new BigDecimal(values.get(i).toString()));
                    continue;
                }
                if (values.get(i) instanceof Integer) {
                    pstmt.setInt(i + 1, Integer.parseInt(values.get(i).toString()));
                    continue;
                }
                if (values.get(i) instanceof Double) {
                    pstmt.setDouble(i + 1, Double.parseDouble(values.get(i).toString()));
                    continue;
                }
                if (values.get(i) instanceof LocalDate) {
                    pstmt.setDate(i + 1, java.sql.Date.valueOf((LocalDate) values.get(i)));
                    continue;
                }
                if (values.get(i) instanceof LocalDateTime) {
                    pstmt.setTimestamp(i + 1, java.sql.Timestamp.valueOf((LocalDateTime) values.get(i)));
                    continue;
                }
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public void closeDbConn() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
