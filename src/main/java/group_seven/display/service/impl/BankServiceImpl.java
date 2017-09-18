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
	public List<TradeType> findTradeTypeList() {
		// TODO Auto-generated method stub
		return tradeTypeMapper.findTradeTypeList();
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
	private PersonalConsumptionMapper personalConsumptionMapper;
    @Override
	public List<PersonalConsumption> getPersonalConsumptionList() {
		return personalConsumptionMapper.getPersonalConsumptionList();
	}

	@Autowired
	private CreditAllMapper creditAllMapper;
	@Override
	public List<CreditAll> getCreditAllList() {
		return creditAllMapper.getCreditAllList();
	}

	@Autowired
	private  CreditCityMapper creditCityMapper;
	@Override
	public List<CreditCity> getCreditCityList() {
		return creditCityMapper.getCreditCityList();
	}

	@Autowired
	private CreditOneMapper creditOneMapper;
	@Override
	public List<CreditOne> getCreditOneList(String account) {
		return creditOneMapper.getCreditOneList(account);
	}

	@Autowired
	private TimeSeriesMapper timeSeriesMapper;
	@Override
	public List<TimeSeries> getTimeSeriesList(){
		return timeSeriesMapper.getTimeSeriesList();
	}

	@Autowired
	private PersonasMapper personasMapper;
	@Override
	public List<Personas> getPersonasList(String purpose){
		return personasMapper.getPersonasList(purpose);
	}

	@Autowired
	private TestDataMapper testDataMapper;
	@Override
	public List<TestData> getTestDataList(String label) {
		return testDataMapper.getTestDataList(label);
	}
}
