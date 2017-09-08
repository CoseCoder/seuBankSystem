package group_seven.display.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group_seven.display.mapper.*;
import group_seven.display.model.*;
import group_seven.display.service.BankService;

@Service
public class BankServiceImpl implements BankService{

	@Autowired
	private TradeTypeMapper tradeTypeMapper;

	@Override
	public List<TradeType> findTradeTypeList(String fromDate, String toDate) {
		// TODO Auto-generated method stub
		return tradeTypeMapper.findTradeTypeList(fromDate, toDate);
	}

	@Autowired
	private CityUserMapper cityUserMapper;

	@Override
	public List<CityUser> getCityUserList() {
        // TODO Auto-generated method stub
		return cityUserMapper.getCityUserList();
	}

	@Autowired
	private BillInfoMapper billInfoMapper;
	@Override
	public List<BillInfo> getBillInfoList() {
		return billInfoMapper.getBillInfoList();
	}

	@Autowired
	private ProvinceIDToNameMapper provinceIDToNameMapper;
	@Override
	public List<ProvinceIDToName> getProvinceIDToNameList() {
		return provinceIDToNameMapper.getProvinceIDToNameList();
	}


	@Autowired
	private PersonalConsumptionMapper personalConsumptionMapper;
    @Override
	public List<PersonalConsumption> getPersonalConsumptionList(String cardNumber) {
		return personalConsumptionMapper.getPersonalConsumptionList(cardNumber);
	}
}
