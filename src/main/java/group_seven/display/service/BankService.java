package group_seven.display.service;

import java.util.List;

import group_seven.display.model.*;

public interface BankService {
	List<TradeType> findTradeTypeList();

	List<CityUser> getCityUserList();

	List<BillInfo> getBillInfoList();

	List<PersonalConsumption> getPersonalConsumptionList();

	List<CreditAll> getCreditAllList();

	List<CreditCity> getCreditCityList();

	List<CreditOne> getCreditOneList(String account);

	List<TimeSeries> getTimeSeriesList();

	List<Personas> getPersonasList(String purpose);

	List<TestData> getTestDataList(String label);
}