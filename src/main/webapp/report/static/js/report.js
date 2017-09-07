$(document).ready(function(){
	tradetypeinfo();
	userdistributioninfo();
})

function tradetypeinfo() {
	$.ajax({
		type:"GET",
		url:"/report/tradetypeinfo",
		data: {
			"fromDate":"2016-01",
		    "toDate":"2016-12"
		}, 
		dataType:'json',
		success:function(d){
		    console.log(d)
			showTradeTypeInfo(d.alipay,d.wechatpay,d.tenpay)
		}
	})
}

function userdistributioninfo(){
	$.ajax({
		type:"GET",
		url:"/report/userdistributioninfo",
		dataType:"json",
		success:function(d){
		    console.log(d)
			showUserDistributionInfo(d)
		}
	})
}

function showTradeTypeInfo(alipay,wechatpay,tenpay){
	var tradeTypeChart=echarts.init(document.getElementById('tradeTypeChart'))
	var option = {
		tooltip:{},
    angleAxis:{
        type:'value',
        max:alipay+wechatpay+tenpay,
        show:false
    },
    radiusAxis: {
        type: 'category',
        data: ['财付通', '微信支付', '支付宝'],
        z: 10,
        show:false
    },
    polar: {
    },
    series: [{
        type: 'bar',
        data: [tenpay, 0, 0],
        coordinateSystem: 'polar',
        name: '财付通',
        stack:'s'
    }, {
        type: 'bar',
        data: [0, wechatpay, 0],
        coordinateSystem: 'polar',
        name: '微信支付',
        stack:'s'
    }, {
        type: 'bar',
        data: [0, 0, alipay],
        coordinateSystem: 'polar',
        name: '支付宝',
        stack:'s',
        barWidth:100
    }],
    legend: {
        show: true,
        data: ['支付宝', '微信支付', '财付通']
    }
}
tradeTypeChart.setOption(option)
}

function showUserDistributionInfo(d){
	var userDistributionChart=echarts.init(document.getElementById('userDistributionChart'))
	var option={
		type:'map',
		map:'china',
	}
	userDistributionChart.setOption(option)
}