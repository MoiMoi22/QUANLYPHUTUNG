package com.tld_store.DemoDao.imp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import dao.Database;
import dao.StatisticDAO;
import dto.StatisticObject;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class StatisticDAOImp implements StatisticDAO {

    @Override
    public ArrayList<StatisticObject> getByDate(Date startDate, Date endDate) {
        String sql = "SELECT Transaction_Date as Date, " +
                "SUM(CASE WHEN Transaction_Type = 'THU' THEN Amount ELSE 0 END) AS Receives, " +
                "SUM(CASE WHEN Transaction_Type = 'CHI' THEN Amount ELSE 0 END) AS Spends, " +
                "SUM(CASE WHEN Transaction_Type = 'THU' THEN Amount ELSE 0 END) - " +
                "SUM(CASE WHEN Transaction_Type = 'CHI' THEN Amount ELSE 0 END) AS Profit " +
                "FROM Transactions " +
                "WHERE Transaction_Date BETWEEN ? AND ? " +
                "GROUP BY Transaction_Date";

        return executeStatisticQuery(sql, startDate, endDate);
    }

    @Override
    public ArrayList<StatisticObject> getByMonth(Date startMonth, Date endMonth) {
        String sql = "SELECT YEAR(Transaction_Date) AS Year, MONTH(Transaction_Date) AS Month, " +
        		"CAST(YEAR(Transaction_Date) AS VARCHAR) + '-' + RIGHT('0' + CAST(MONTH(Transaction_Date) AS VARCHAR), 2) + '-01' AS Date, " +
                "SUM(CASE WHEN Transaction_Type = 'THU' THEN Amount ELSE 0 END) AS Receives, " +
                "SUM(CASE WHEN Transaction_Type = 'CHI' THEN Amount ELSE 0 END) AS Spends, " +
                "SUM(CASE WHEN Transaction_Type = 'THU' THEN Amount ELSE 0 END) - " +
                "SUM(CASE WHEN Transaction_Type = 'CHI' THEN Amount ELSE 0 END) AS Profit " +
                "FROM Transactions " +
                "WHERE YEAR(Transaction_Date) * 12 + MONTH(Transaction_Date) BETWEEN " +
                "YEAR(?) * 12 + MONTH(?) AND YEAR(?) * 12 + MONTH(?) " +
                "GROUP BY YEAR(Transaction_Date), MONTH(Transaction_Date)";

        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<StatisticObject> statistics = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, startMonth);
            ps.setDate(2, startMonth);
            ps.setDate(3, endMonth);
            ps.setDate(4, endMonth);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                float receives = rs.getFloat("Receives");
                float spends = rs.getFloat("Spends");
                float profit = rs.getFloat("Profit");
                Date date = rs.getDate("Date");

                StatisticObject statistic = new StatisticObject(receives, spends, profit, date);
                statistics.add(statistic);
            }
        } catch (SQLException e) {
            Database.closeResultSet(rs);
            Database.closePreparedStatement(ps);
            Database.closeConnection(con);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return statistics;
        
    }

    @Override
    public ArrayList<StatisticObject> getByYear(Date startYear, Date endYear) {
        String sql = "SELECT YEAR(Transaction_Date) AS Year, " +
        		"CAST(YEAR(Transaction_Date) AS VARCHAR) + '-' + RIGHT('01', 2) + '-01' AS Date, " + 
                "SUM(CASE WHEN Transaction_Type = 'THU' THEN Amount ELSE 0 END) AS Receives, " +
                "SUM(CASE WHEN Transaction_Type = 'CHI' THEN Amount ELSE 0 END) AS Spends, " +
                "SUM(CASE WHEN Transaction_Type = 'THU' THEN Amount ELSE 0 END) - " +
                "SUM(CASE WHEN Transaction_Type = 'CHI' THEN Amount ELSE 0 END) AS Profit " +
                "FROM Transactions " +
                "WHERE YEAR(Transaction_Date) BETWEEN YEAR(?) AND YEAR(?) " +
                "GROUP BY YEAR(Transaction_Date)";

        return executeStatisticQuery(sql, startYear, endYear);
    }

    private ArrayList<StatisticObject> executeStatisticQuery(String sql, Date start, Date end) {
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<StatisticObject> statistics = new ArrayList<>();

        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, start);
            ps.setDate(2, end);
            rs = ps.executeQuery();

            while (rs.next()) {
                float receives = rs.getFloat("Receives");
                float spends = rs.getFloat("Spends");
                float profit = rs.getFloat("Profit");
                Date date = rs.getDate("Date");

                StatisticObject statistic = new StatisticObject(receives, spends, profit, date);
                statistics.add(statistic);
            }
        } catch (SQLException e) {
            Database.closeResultSet(rs);
            Database.closePreparedStatement(ps);
            Database.closeConnection(con);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
        }

        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return statistics;
    }
}
