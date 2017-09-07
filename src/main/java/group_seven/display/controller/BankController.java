package group_seven.display.controller;

import group_seven.display.model.CityUser;
import group_seven.display.model.TradeType;
import group_seven.display.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
//		response.setHeader("Access-Control-Allow-Origin", "*");

//		String fromDate = (String)request.getParameter("fromDate");
//		String toDate = (String)request.getParameter("toDate");
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
		Map<String, ArrayList<Float>> map = new HashMap<String,ArrayList<Float>>();
		ArrayList<Float> array=new ArrayList<>();
		for(CityUser bean : list) {
		    array.clear();
		    array.add(Float.valueOf(bean.getyData()));
            array.add(Float.valueOf(bean.getxData()));
            array.add(Float.valueOf(bean.getCardAmount()));
			map.put(bean.getCity(),array);
		}
		return map;
	}
}
