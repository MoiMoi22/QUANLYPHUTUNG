package dao;

import java.util.ArrayList;

import dto.Address;

public interface AddressDAO extends DAO<Address, Integer>{
	
	ArrayList<Address> getAllAddressesByCommuneId(int communeId);
	
}
