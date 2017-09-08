package group_seven.display.service;

import java.util.List;

import group_seven.display.model.*;

public interface BankService {
	List<TradeType> findTradeTypeList(String fromDate,String toDate);

	List<CityUser> getCityUserList();

	List<BillInfo> getBillInfoList();

    List<ProvinceIDToName> getProvinceIDToNameList();

	List<PersonalConsumption> getPersonalConsumptionList(String cardNumber);
}