package com.tld_store.DemoDao.imp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import ch.qos.logback.core.subst.Token.Type;
import dao.Database;
import dao.TransactionDAO;
import dto.Transaction;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class TransactionDAOImp implements TransactionDAO {

	@Override
    public Transaction findById(Integer id)  {
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Transaction transaction = null;

        String sql = "SELECT * FROM Transactions WHERE Transaction_ID = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("Transaction_ID"));
                transaction.setTransactionDate(rs.getDate("Transaction_Date"));
                transaction.setTransactionType(rs.getString("Transaction_Type"));
                transaction.setAmount(rs.getFloat("Amount"));
                transaction.setTitle(rs.getString("Title"));
                transaction.setDescription(rs.getString("Description"));
                transaction.setAccountantId(rs.getInt("Accountant_ID"));
                transaction.setRelatedOrderId(rs.getInt("Related_Order_ID"));
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return transaction;
    }

    @Override
    public List<Transaction> getAll()  {
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM Transactions";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("Transaction_ID"));
                transaction.setTransactionDate(rs.getDate("Transaction_Date"));
                transaction.setTransactionType(rs.getString("Transaction_Type"));
                transaction.setAmount(rs.getFloat("Amount"));
                transaction.setTitle(rs.getString("Title"));
                transaction.setDescription(rs.getString("Description"));
                transaction.setAccountantId(rs.getInt("Accountant_ID"));
                transaction.setRelatedOrderId(rs.getInt("Related_Order_ID"));
                transactions.add(transaction);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return transactions;
    }

    @Override
    public int insert(Transaction t)  {
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        int result = 1;
        
        String sql = "INSERT INTO Transactions (Transaction_Date, Transaction_Type, Amount, Title, Description, Accountant_ID, Related_Order_ID) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            ps.setString(2, t.getTransactionType());
            ps.setFloat(3, t.getAmount());
            ps.setString(4, t.getTitle());
            ps.setString(5, t.getDescription());
            ps.setInt(6, t.getAccountantId());
            ps.setNull(7, Types.INTEGER);
            ps.executeUpdate();
        }
		catch(SQLException e)
		{
            Database.closePreparedStatement(ps);
            Database.closeConnection(con);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}     
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return result;
    }

    @Override
    public int update(Transaction t)  {
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        int result = 1;
        
        String sql = "UPDATE Transactions "
                   + "SET Transaction_Type = ?, Amount = ?, Title = ?, Description = ?, Accountant_ID = ?, Related_Order_ID = ? "
                   + "WHERE Transaction_ID = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, t.getTransactionType());
            ps.setFloat(2, t.getAmount());
            ps.setString(3, t.getTitle());
            ps.setString(4, t.getDescription());
            ps.setInt(5, t.getAccountantId());
            ps.setInt(6, t.getRelatedOrderId());
            ps.setInt(7, t.getTransactionId());
            ps.executeUpdate();
        }
		catch(SQLException e)
		{
            Database.closePreparedStatement(ps);
            Database.closeConnection(con);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}     
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return result;
    }


    @Override
    public int delete(Transaction t)  {
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Transactions");
			cbst.setString(2, Integer.toString(t.getTransactionId()));

			cbst.execute();			
		}
		catch(SQLException e)
		{
			Database.closeCallableStatement(cbst);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeCallableStatement(cbst);
		Database.closeConnection(con);
		return result;
    }

    @Override
    public ArrayList<Transaction> get10TransactionByAccountantId(int accountantId, int numPage)  {
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT *\n"
        		+ "FROM Transactions\n"
        		+ "WHERE Accountant_ID = ?\n"
        		+ "ORDER BY Transaction_ID\n"
				+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
				+ "FETCH NEXT 11 ROW ONLY";;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, accountantId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("Transaction_ID"));
                transaction.setTransactionDate(rs.getDate("Transaction_Date"));
                transaction.setTransactionType(rs.getString("Transaction_Type"));
                transaction.setAmount(rs.getFloat("Amount"));
                transaction.setTitle(rs.getString("Title"));
                transaction.setDescription(rs.getString("Description"));
                transaction.setAccountantId(rs.getInt("Accountant_ID"));
                transaction.setRelatedOrderId(rs.getInt("Related_Order_ID"));
                transactions.add(transaction);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return transactions;
    }

    @Override
    public ArrayList<Transaction> getAllTransactionByRelatedOrderIdAndType(int relatedOrderId){
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM Transactions WHERE Related_Order_ID = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, relatedOrderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("Transaction_ID"));
                transaction.setTransactionDate(rs.getDate("Transaction_Date"));
                transaction.setTransactionType(rs.getString("Transaction_Type"));
                transaction.setAmount(rs.getFloat("Amount"));
                transaction.setTitle(rs.getString("Title"));
                transaction.setDescription(rs.getString("Description"));
                transaction.setAccountantId(rs.getInt("Accountant_ID"));
                transaction.setRelatedOrderId(rs.getInt("Related_Order_ID"));
                transactions.add(transaction);
            }
        } 
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return transactions;
    }
	@Override
	public List<Transaction> get10(int numPage){
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Transaction> transactions = new ArrayList<>();

        String sql =  "SELECT *\n"
					+ "FROM Transactions\n"
					+ "ORDER BY Transaction_ID\n"
					+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
					+ "FETCH NEXT 11 ROW ONLY";
        
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("Transaction_ID"));
                transaction.setTransactionDate(rs.getDate("Transaction_Date"));
                transaction.setTransactionType(rs.getString("Transaction_Type"));
                transaction.setAmount(rs.getFloat("Amount"));
                transaction.setTitle(rs.getString("Title"));
                transaction.setDescription(rs.getString("Description"));
                transaction.setAccountantId(rs.getInt("Accountant_ID"));
                transaction.setRelatedOrderId(rs.getInt("Related_Order_ID"));
                transactions.add(transaction);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return transactions;
	}

	@Override
	public ArrayList<Transaction> get10ByName(String name, String type, int numPage) {
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        String sql = "SELECT *\n"
        		+ "FROM Transactions\n"
        		+ "LEFT JOIN Employees\r\n"
        		+ "ON Transactions.Accountant_ID = Employees.Employee_ID\n"
        		+ "WHERE (Last_Name + ' ' + First_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%') AND Transaction_Type = ?\n"
        		+ "ORDER BY Transaction_ID\n"
				+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
				+ "FETCH NEXT 11 ROW ONLY";;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, type);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("Transaction_ID"));
                transaction.setTransactionDate(rs.getDate("Transaction_Date"));
                transaction.setTransactionType(rs.getString("Transaction_Type"));
                transaction.setAmount(rs.getFloat("Amount"));
                transaction.setTitle(rs.getString("Title"));
                transaction.setDescription(rs.getString("Description"));
                transaction.setAccountantId(rs.getInt("Accountant_ID"));
                transaction.setRelatedOrderId(rs.getInt("Related_Order_ID"));
                transactions.add(transaction);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return transactions;
	}

	@Override
	public ArrayList<Transaction> get10ByType(String type, int numPage) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        String sql = "SELECT *\n"
        		+ "FROM Transactions\n"
        		+ "WHERE Transaction_Type = ?"
        		+ "ORDER BY Transaction_ID\n"
				+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
				+ "FETCH NEXT 11 ROW ONLY";;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, type);
            rs = ps.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("Transaction_ID"));
                transaction.setTransactionDate(rs.getDate("Transaction_Date"));
                transaction.setTransactionType(rs.getString("Transaction_Type"));
                transaction.setAmount(rs.getFloat("Amount"));
                transaction.setTitle(rs.getString("Title"));
                transaction.setDescription(rs.getString("Description"));
                transaction.setAccountantId(rs.getInt("Accountant_ID"));
                transaction.setRelatedOrderId(rs.getInt("Related_Order_ID"));
                transactions.add(transaction);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return transactions;
	}

}

