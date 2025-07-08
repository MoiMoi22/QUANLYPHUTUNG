package dao;

import java.sql.Date;
import java.util.ArrayList;

import dto.StatisticObject;

public interface StatisticDAO {
	
	ArrayList<StatisticObject> getByDate(Date startDate, Date endDate);
	
	ArrayList<StatisticObject> getByMonth(Date startMonth, Date endMonth);
	
	ArrayList<StatisticObject> getByYear(Date startYear, Date endYear);
	
}
