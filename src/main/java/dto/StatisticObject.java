package dto;

import java.sql.Date;

public class StatisticObject {
	float receives;
	float spends;
	float profit;
	Date currentDate;
	
	public StatisticObject() {
		super();
	}
	
	public StatisticObject(float receives, float spends, float profit, Date currentDate) {
		super();
		this.receives = receives;
		this.spends = spends;
		this.profit = profit;
		this.currentDate = currentDate;
	}
	
	public float getReceives() {
		return receives;
	}
	public void setReceives(float receives) {
		this.receives = receives;
	}
	public float getSpends() {
		return spends;
	}
	public void setSpends(float spends) {
		this.spends = spends;
	}
	public float getProfit() {
		return profit;
	}
	public void setProfit(float profit) {
		this.profit = profit;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	
}
