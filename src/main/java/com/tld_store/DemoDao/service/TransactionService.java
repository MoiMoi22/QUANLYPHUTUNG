package com.tld_store.DemoDao.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.CombinedDAO;
import dao.TransactionDAO;
import dto.Shopper;
import dto.Transaction;

@Service
public class TransactionService {
	@Autowired
	TransactionDAO transDAO;
	
	@Autowired
	CombinedDAO combinedDAO;
	
	public void handleRequest(float amountPaid, int orderId, int accountantId, String transactionType) {
		Transaction tran = new Transaction();
		tran.setAccountantId(accountantId);
		tran .setAmount(amountPaid);
		tran.setDescription("Trả Nợ Thanh Toán hóa đơn");
		tran.setRelatedOrderId(orderId);
		tran.setTitle("Trả Nợ");
		tran.setTransactionDate(Date.valueOf(java.time.LocalDate.now()));
		tran.setTransactionType(transactionType);
		
		addTransaction(tran, tran.getTransactionType());
	}
	
	public void addTransaction(Transaction trans) {
		transDAO.insert(trans);
	}
	
	public void addTransaction(Transaction trans, String transactionType) {
		combinedDAO.insertTransaction(trans, transactionType);
	}
	
	public void updateTransaction(Transaction trans) {
		transDAO.update(trans);
	}
	
	public void deleteTransaction(Transaction trans) {
		transDAO.delete(trans);
	}
	
	public ArrayList<Transaction> get10Transaction(int numPage, AtomicBoolean isFinal){
		ArrayList<Transaction> arr = (ArrayList<Transaction>) transDAO.get10(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Transaction>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Transaction> get10TransactionByAccountantId(int accountantId, int numPage){
		return new ArrayList<Transaction>(transDAO.get10TransactionByAccountantId(accountantId, numPage));
	}
	
	public ArrayList<Transaction> get10TransactionByType(String type, int numPage, AtomicBoolean isFinal){
		ArrayList<Transaction> arr = transDAO.get10ByType(type, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Transaction>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public Transaction getTransactionById(int transactionId) {
		return transDAO.findById(transactionId);
	}
	
	public ArrayList<Transaction> get10TransactionByName(String searchName, String type, int numPage, AtomicBoolean isFinal){
		ArrayList<Transaction> arr = transDAO.get10ByName(searchName, type, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Transaction>( arr.subList(0, Math.min(arr.size(), 10)));
	}
}
