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



}
