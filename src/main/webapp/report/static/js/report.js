$(document).ready(function(){
	tradetypeinfo()
	userdistributioninfo()
	billdistributioninfo()
	consumptiontypeinfo()
})

function tradetypeinfo() {
	$.ajax({
		type:"GET",
		url:"/report/tradetypeinfo",
		data:"fromDate=2016-01&toDate=2016-12",
		dataType:"json",
		success:function(d){
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
			showUserDistributionInfo(d)
		}
	})
}

function billdistributioninfo(){
	$.ajax({
		type:"GET",
		url:"/report/billdistributioninfo",
		dataType:"json",
		success:function(d){
			showBillDistributionInfo(d)
		}
	})
}

function consumptiontypeinfo(){
	$.ajax({
		type:"GET",
		url:"/report/consumptiontypeinfo",
		data:"cardNumber=6542661784689656633",
		dataType:"json",
		success:function(d){
			showConsumptionTypeInfo(d)
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
			stack:'s',
			barWidth:100,
		}, {
			type: 'bar',
			data: [0, wechatpay, 0],
			coordinateSystem: 'polar',
			name: '微信支付',
			stack:'s',
			barWidth:100
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
		legend:{
			show:true,
			data:['用户数量']
		},
		geo:{
			map:'china',
			roam:true,
			label:{
				emphasis:{
					show:false
				}
			},

		},
		symbolSize:22,
		tooltip:{
			trigger:'item',
			formatter:function(params){
				return params.name+':'+params.value[2];
			}
		},
		series:[{
			map:'china',
			name:"用户数量",
			type:'scatter',
			coordinateSystem:'geo',
			data:d
		}],
	}
	userDistributionChart.setOption(option)
}
function showBillDistributionInfo(d){
	var billDistributionChart=echarts.init(document.getElementById('billDistributionChart'))
	var option = {
		title : {
			text: '账单交易分布',
			left: 'center'
		},
		tooltip : {
			trigger: 'item'
		},
		legend: {
			orient: 'vertical',
			left: 'left',
			data:['支出','存入']
		},
		visualMap: {
			min: 0,
			max: 2500,
			left: 'left',
			top: 'bottom',
        text:['高','低'],           // 文本，默认为数值文本
        calculable : true
    },
    /*toolbox: {
    	show: true,
    	orient : 'vertical',
    	left: 'right',
    	top: 'center',
    	feature : {
    		mark : {show: true},
    		dataView : {show: true, readOnly: false},
    		restore : {show: true},
    		saveAsImage : {show: true}
    	}
    },*/
    series : [
    {
    	name: '支出',
    	type: 'map',
    	mapType: 'china',
    	roam: false,
    	label: {
    		normal: {
    			show: false
    		},
    		emphasis: {
    			show: true
    		}
    	},
    	data:d[1]
    },
    {
    	name: '存入',
    	type: 'map',
    	mapType: 'china',
    	label: {
    		normal: {
    			show: false
    		},
    		emphasis: {
    			show: true
    		}
    	},
    	data:d[0]
    },

    ]
};
billDistributionChart.setOption(option)
}

function showConsumptionTypeInfo(d){
	var consumptionTypeChart=echarts.init(document.getElementById('consumptionTypeChart'))
	var option = {
    title : {
        text: '资金走向',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: ['购物','外卖','水电费','游戏','QQ充值']
    },
    series : [
        {
            name: '资金走向',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:d,
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};

	consumptionTypeChart.setOption(option)
}
