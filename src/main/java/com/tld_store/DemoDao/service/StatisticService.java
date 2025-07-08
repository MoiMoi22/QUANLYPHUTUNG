package com.tld_store.DemoDao.service;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.StatisticDAO;
import dto.StatisticObject;

@Service
public class StatisticService {
	
	@Autowired
	StatisticDAO statisticDAO;
	
	public ArrayList<StatisticObject> getByDate(String start, String end){
		
		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);
		
		return statisticDAO.getByDate(startDate, endDate);
		
	}
	
	public ArrayList<StatisticObject> getByMonth(String start, String end){
			
			Date startDate = Date.valueOf(start);
			Date endDate = Date.valueOf(end);
			
			return statisticDAO.getByMonth(startDate, endDate);
			
		}
	
	public ArrayList<StatisticObject> getByYear(String start, String end){
		
		Date startDate = Date.valueOf(start);
		Date endDate = Date.valueOf(end);
		
		return statisticDAO.getByYear(startDate, endDate);
		
	}
	
}
