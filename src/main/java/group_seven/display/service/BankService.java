package group_seven.display.service;

import java.util.List;

import group_seven.display.model.*;

public interface BankService {
	List<TradeType> findTradeTypeList(String fromDate,String toDate);

	List<CityUser> getCityUserList();
}