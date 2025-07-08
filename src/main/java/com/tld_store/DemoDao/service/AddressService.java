package com.tld_store.DemoDao.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.AddressDAO;
import dao.CommuneDAO;
import dao.DistrictDAO;
import dao.ProvinceDAO;
import dto.Address;
import dto.CommuneWard;
import dto.District;
import dto.Province;

@Service
public class AddressService {
	
	@Autowired
	AddressDAO addressDAO;
	@Autowired
	CommuneDAO communeDAO;
	@Autowired
	DistrictDAO districtDAO;
	@Autowired
	ProvinceDAO provinceDAO;
	@Autowired
	OrderService orderService;
	
	public String getFullPathAddress(Address address){
		
		CommuneWard commune = communeDAO.findById(address.getCommuneWardId());
		District district = districtDAO.findById(commune.getDistrictId());
		Province province = provinceDAO.findById(district.getProvinceId());
		
		return address.getAddressName() + ", " 
				+ commune.getCommuneName() + ", "
				+ district.getDistrictName() + ", "
				+ province.getProvinceName();
	}
	
	public void addAddress(Address address) {
		addressDAO.insert(address);
	}
	
	public Address getAddressByAddressId(int addressId) {
		return addressDAO.findById(addressId);
	}
	
	public CommuneWard getCommuneById(int communeId) {
		return communeDAO.findById(communeId);
	}
	
	public District getDistrictById(int districtId) {
		return districtDAO.findById(districtId);
	}
	
	public Province getProvinceById(int provinceId) {
		return provinceDAO.findById(provinceId);
	}
	
	public ArrayList<Province> getAllProvince(){
		return new ArrayList<Province>(provinceDAO.getAll());
	}
	
	public ArrayList<District> getAllDistrict(){
		return new ArrayList<District>(districtDAO.getAll());
	}
	
	public ArrayList<District> getAllDistrictByProvinceId(int provinceId){
		return new ArrayList<District>(districtDAO.getAllDistrictByProvinceById(provinceId));
	}
	
	public ArrayList<CommuneWard> getAllCommuneWard(){
		return new ArrayList<CommuneWard>(communeDAO.getAll());
	}
	
	public ArrayList<CommuneWard> getAllCommuneWardByDistrictId(int districtId){
		return new ArrayList<CommuneWard>(communeDAO.getAllCommuneWardByDistrictId(districtId));
	}
	
	public ArrayList<Address> getAddressByShopperId(int shopperId){
		ArrayList<Integer> addressIdList = orderService.getAllAddressIdByShopperId(shopperId);
		ArrayList<Address> addressList = new ArrayList<Address>();
		for (Integer addressId: addressIdList) {
			addressList.add(getAddressByAddressId(addressId));
		}
		for (Address address: addressList) {
			address.setAddressName(getFullPathAddress(address));;
		}
		return addressList;
	}
}
