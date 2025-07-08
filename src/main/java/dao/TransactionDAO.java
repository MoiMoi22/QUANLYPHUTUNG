package dao;

import java.util.ArrayList;

import dto.Transaction;

public interface TransactionDAO extends DAO<Transaction, Integer> {
	
	ArrayList<Transaction> get10TransactionByAccountantId(int accountantId, int numPage);
	
	ArrayList<Transaction> getAllTransactionByRelatedOrderIdAndType(int relatedOrderId);
	
	ArrayList<Transaction> get10ByName(String name, String type, int numPage);
	
	ArrayList<Transaction> get10ByType(String type, int numPage);
	
}
