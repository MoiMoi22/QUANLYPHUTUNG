package dao;

import java.util.ArrayList;

import dto.CommuneWard;

public interface CommuneDAO extends DAO<CommuneWard, Integer>{

	ArrayList<CommuneWard> getAllCommuneWardByDistrictId(int districtId);
	
}
