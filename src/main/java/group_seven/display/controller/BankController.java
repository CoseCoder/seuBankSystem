package group_seven.display.controller;

import group_seven.display.model.*;
import group_seven.display.service.BankService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件声量
 */
@Controller
@RequestMapping(value = "/report")
public class BankController {
	@Autowired
	private BankService bankService;

	@RequestMapping(value = "/tradetypeinfo", method = RequestMethod.GET)
	@ResponseBody
	public Object tradeTypeInfo(String fromDate, String toDate) {
		String fromD=fromDate.concat("-01 00:00:00");
		String toD=toDate.concat("-01 00:00:00");
		List<TradeType> list= bankService.findTradeTypeList(fromD, toD);
		Map<String,Long> result=new HashMap<String,Long>();
		for (TradeType item:list) {
			if(item.getTradetype().equals("支付宝"))
				result.put("alipay",item.getNumber());
			else if(item.getTradetype().equals("微信"))
				result.put("wechatpay",item.getNumber());
			else
				result.put("tenpay",item.getNumber());
		}
		return result;
	}
	@RequestMapping(value="/userdistributioninfo",method = RequestMethod.GET)
	@ResponseBody
	public Object userDistributionInfo() {
		List<CityUser> list = bankService.getCityUserList();
        JSONArray result=new JSONArray();
		for(CityUser item : list) {
            JSONArray array=new JSONArray();
            JSONObject obj=new JSONObject();
		    array.add(Float.valueOf(item.getyData()));
            array.add(Float.valueOf(item.getxData()));
            array.add(Float.valueOf(item.getCardAmount()));
            obj.put("name",item.getCity());
            obj.put("value",array);
			result.add(obj);
		}
		return result;
	}

    @RequestMapping(value="/billdistributioninfo",method = RequestMethod.GET)
    @ResponseBody
    public Object billDistributionInfo() {
        List<BillInfo> billInfoList=bankService.getBillInfoList();
        JSONArray result=new JSONArray();
        JSONArray depositList=new JSONArray();
        JSONArray withdrawlList=new JSONArray();
        for(BillInfo item:billInfoList){
            JSONObject obj=new JSONObject();
            obj.put("name",item.getProvince());
            obj.put("value",item.getMoney());
            if(item.getBillType()==1)
                depositList.add(obj);
            else
                withdrawlList.add(obj);
        }
        result.add(depositList);
        result.add(withdrawlList);
        return result;
    }


    @RequestMapping(value="/consumptiontypeinfo",method = RequestMethod.GET)
    @ResponseBody
    public Object consumptionTypeInfo(String cardNumber) {
        List<PersonalConsumption> list = bankService.getPersonalConsumptionList(cardNumber);
        JSONArray result = new JSONArray();
        for(PersonalConsumption item : list) {
            JSONObject object = new JSONObject();
            object.put("name", item.getType());
            object.put("value", Double.valueOf(item.getValue()));
            result.add(object);
        }
        return result;
    }
    @RequestMapping(value = "/creditall",method=RequestMethod.GET)
    @ResponseBody
    public Object creditAll(){
        List<CreditAll> list = bankService.getCreditAllList();
        JSONArray result = new JSONArray();
        for(CreditAll item : list){
            JSONObject object = new JSONObject();
            object.put("BillNumber",item.getBillNumber());
            object.put("Account",item.getAccount());
            object.put("Money",item.getMoney());
            object.put("BillDate",item.getBillDate());
            object.put("BillExInfo",item.getBillExInfo());
            object.put("BillSite",item.getBillSite());
            object.put("ExReason",item.getExReason());
            result.add(object);
        }

        return result;
    }

    @RequestMapping(value = "/creditone", method = RequestMethod.GET)
    @ResponseBody
    public Object creditOne(String account){
        List<CreditOne> list = bankService.getCreditOneList(account);
        JSONArray result = new JSONArray();
        for(CreditOne item : list){
            JSONObject object = new JSONObject();
            object.put("BillNumber",item.getBillNumber());
            object.put("Account",item.getAccount());
            object.put("Money",item.getMoney());
            object.put("BillDate",item.getBillDate());
            object.put("BillExInfo",item.getBillExInfo());
            object.put("BillSite",item.getBillSite());
            object.put("ExReason",item.getExReason());
            result.add(object);
        }
        return result;
    }

    @RequestMapping(value = "/creditcity",method = RequestMethod.GET)
    @ResponseBody
    public Object creditCity(){
        List<CreditCity> list = bankService.getCreditCityList();
        JSONArray result = new JSONArray();
        for(CreditCity item: list){
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            array.add(item.getyData());
            array.add(item.getxData());
            array.add(item.getAmount());
            object.put("name",item.getCity());
            object.put("value",array);
            result.add(object);
        }
        return result;
    }
}
