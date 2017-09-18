$(document).ready(function(){
	tradetypeinfo()
	userdistributioninfo()
	billdistributioninfo()
	consumptiontypeinfo()
	creditall()
	creditcity()
	company()
	shoppersonas()
	qqpersonas()
	wepersonas()
	foodpersonas()
	gamepersonas()
	testdata()

})


function tradetypeinfo() {
	$.ajax({
		type:"GET",
		url:"/report/tradetypeinfo",
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
			data: ['财付通', '微信支付', '支付宝',''],
			z: 10,
			show:false
		},
		polar: {
		},
		series: [{
			type: 'bar',
			data: [0,tenpay, 0, 0,0],
			coordinateSystem: 'polar',
			name: '财付通',
			stack:'s',
			barWidth:100,
		}, {
			type: 'bar',
			data: [0,0, wechatpay, 0],
			coordinateSystem: 'polar',
			name: '微信支付',
			stack:'s',
			barWidth:100
		}, {
			type: 'bar',
			data: [0,0, 0, alipay],
			coordinateSystem: 'polar',
			name: '支付宝',
			stack:'s',
			barWidth:100
		},{
          			type: 'bar',
          			data: [0, 0, 0,0],
          			coordinateSystem: 'polar',
          			name:"",
          			stack:'s',
          			barWidth:100
          		}],
		legend: {
			show: true,
			data: ['支付宝', '微信支付', '财付通'],
			orient:'vertical',
			left:'left',
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
			//roam:true,
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
			max: 3000,
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
            name: '消费类型',
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

function creditall() {
	$.ajax({
		type:"GET",
		url:"/report/creditall",
		dataType:"json",
		success:function(data){
		showcreditall(data)
		}

	})
}

function showcreditall(data) {
  var oTab = document.getElementById('banktable');

  var oTbody = oTab.tBodies[0];
  for (var index = 0; index < data.length; index++)
  {
    var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    var oTd = document.createElement('td');
    oTd.innerHTML = data[index].BillNumber;
    oTr.appendChild(oTd);

    oTd = document.createElement('td');
    oTr.appendChild(oTd);

    var oA =document.createElement('a');
    oA.innerHTML= data[index].Account;
    oA.href="javascript:;";
    oTd.appendChild(oA);


    oTd = document.createElement('td');
    oTd.innerHTML = data[index].Money;
    oTr.appendChild(oTd);

	oTd = document.createElement('td');
    oTd.innerHTML = data[index].BillDate;
    oTr.appendChild(oTd);

	oTd = document.createElement('td');
    oTd.innerHTML = data[index].BillExInfo;
    oTr.appendChild(oTd);

	oTd = document.createElement('td');
    oTd.innerHTML = data[index].BillSite;
    oTr.appendChild(oTd);

	oTd = document.createElement('td');
    oTd.innerHTML = data[index].ExReason;
    oTr.appendChild(oTd);



    /*var oA = document.createElement('a');
    oA.innerHTML = "删除";
    oA.href = "javascript:;";
    oTd.appendChild(oA);
    oA.onclick = function () {
      oTbody.removeChild(this.parentNode.parentNode);
    }*/
  }
}

/*$("a").each(click(function(){
var account=$(this).html();
								   })
)
*/

function creditcity() {
	$.ajax({
		type:"GET",
		url:"/report/creditcity",
		//data:"fromDate=2016-01&toDate=2016-12",
		dataType:"json",
		success:function(d){
			showcreditcity(d)

		}
	})
}


function showcreditcity(d){
	var creditcityChart=echarts.init(document.getElementById('creditcityChart'))

//	   $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
//        creditcityChart.resize();
//
//    });

	var option={
		legend:{
			show:true,
			data:['交易异常数量']
		},
		geo:{
			map:'china',
			//roam:true,
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
			name:"交易异常数量",
			type:'scatter',
			coordinateSystem:'geo',
			data:d
		}],
	}

	creditcityChart.setOption(option)

}

function company() {
	$.ajax({
		type:"GET",
		url:"/report/company",
		//data:"fromDate=2016-01&toDate=2016-12",
		dataType:"json",
		success:function(d){
			showcompany(d)
			showcompanytable(d)


		}
	})
}

//date value
function showcompany(d){

var companyChart=echarts.init(document.getElementById('companyChart'))

var option = {
    title: {
        text: 'XX公司收益折线图'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['实际收益','预估收益']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data:[d[0].date,d[1].date,d[2].date,d[3].date,d[4].date,d[5].date,d[6].date,d[7].date,d[8].date,d[9].date,d[10].date,
        d[11].date,d[12].date,d[13].date,d[14].date,d[15].date,d[16].date,d[17].date,d[18].date,d[19].date,d[20].date,d[21].date,d[22].date,
        d[23].date,d[24].date,d[25].date,d[26].date,d[27].date,d[28].date,d[29].date]
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            name:'实际收益',
            type:'line',
//            stack: '总量',
            data:[d[0].value,d[1].value,d[2].value,d[3].value,d[4].value,d[5].value,d[6].value,d[7].value,
            d[8].value,d[9].value,d[10].value,d[11].value,d[12].value,d[13].value,d[14].value,d[15].value,d[16].value,d[17].value,
            d[18].value,d[19].value,d[20].value,d[21].value,d[22].value,d[23].value,null,null,null,null,null,null]
        },
        {
            name:'预估收益',
            type:'line',
//            stack: '总量',
            data:[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
            null,null,null,null,null,null,null,null,d[23].value,
            d[24].value,d[25].value,d[26].value,d[27].value,d[28].value,d[29].value]
        }

    ]
};
companyChart.setOption(option)

}


function showcompanytable(d) {
  var oTab = document.getElementById('companytable');

//  var oTbody = oTab.tBodies[0];
//
//  var oTr = document.createElement('tr');
//      oTbody.appendChild(oTr);

var oTbody = oTab.tBodies[0];

    var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    var oTd = document.createElement('td');
    oTd.innerHTML = '2010年（实际收益）';
    oTr.appendChild(oTd);
    for (var index = 0; index < 12; index++)
      {


        var oTd = document.createElement('td');

        oTd.innerHTML = d[index].value;
        oTr.appendChild(oTd);
        }


var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    oTd = document.createElement('td');
    oTd.innerHTML = '2011年（实际收益）';
    oTr.appendChild(oTd);
    for (var index = 12; index < 24; index++)
                {


                  var oTd = document.createElement('td');

                  oTd.innerHTML = d[index].value;
                  oTr.appendChild(oTd);
                  }
     var oTr = document.createElement('tr');
                      oTbody.appendChild(oTr);

	oTd = document.createElement('td');
    oTd.innerHTML = '2012年（预估收益）';
    oTr.appendChild(oTd);
    for (var index = 24; index < 30; index++)
                              {


                                var oTd = document.createElement('td');
                                var s=d[index].value;
                                s=parseFloat(s).toFixed(1);

                                oTd.innerHTML = s;
                                oTr.appendChild(oTd);
                                }


  }



function shoppersonas() {
	$.ajax({
		type:"GET",
		url:"/report/personas",
		data:"purpose=购物",
		dataType:"json",
		success:function(d){
			showshoptable(d)

		}
	})
}


function showshoptable(d){

var oTab = document.getElementById('shoptable');

  var oTbody = oTab.tBodies[0];
  for (var index = 0; index < d.length; index++)
  {
    var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    var oTd = document.createElement('td');
    oTd.innerHTML = d[index].account;
    oTr.appendChild(oTd);

}
}

function qqpersonas() {
	$.ajax({
		type:"GET",
		url:"/report/personas",
		data:"purpose=QQ充值",
		dataType:"json",
		success:function(d){
			showqqtable(d)

		}
	})
}

function showqqtable(d){

var oTab = document.getElementById('qqtable');

  var oTbody = oTab.tBodies[0];
  for (var index = 0; index < d.length; index++)
  {
    var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    var oTd = document.createElement('td');
    oTd.innerHTML = d[index].account;
    oTr.appendChild(oTd);

}
}

function wepersonas() {
	$.ajax({
		type:"GET",
		url:"/report/personas",
		data:"purpose=水电费",
		dataType:"json",
		success:function(d){
			showwetable(d)

		}
	})
}

function showwetable(d){

var oTab = document.getElementById('wetable');

  var oTbody = oTab.tBodies[0];
  for (var index = 0; index < d.length; index++)
  {
    var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    var oTd = document.createElement('td');
    oTd.innerHTML = d[index].account;
    oTr.appendChild(oTd);

}
}

function foodpersonas() {
	$.ajax({
		type:"GET",
		url:"/report/personas",
		data:"purpose=外卖",
		dataType:"json",
		success:function(d){
			showfoodtable(d)

		}
	})
}

function showfoodtable(d){

var oTab = document.getElementById('foodtable');

  var oTbody = oTab.tBodies[0];
  for (var index = 0; index < d.length; index++)
  {
    var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    var oTd = document.createElement('td');
    oTd.innerHTML = d[index].account;
    oTr.appendChild(oTd);

}
}


function gamepersonas() {
	$.ajax({
		type:"GET",
		url:"/report/personas",
		data:"purpose=游戏",
		dataType:"json",
		success:function(d){
			showgametable(d)

		}
	})
}

function showgametable(d){

var oTab = document.getElementById('gametable');

  var oTbody = oTab.tBodies[0];
  for (var index = 0; index < d.length; index++)
  {
    var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    var oTd = document.createElement('td');
    oTd.innerHTML = d[index].account;
    oTr.appendChild(oTd);

}


}

function testdata(){

	$.ajax({
		type:"GET",
		url:"/report/testdata",

		dataType:"json",
		success:function(data){
			showtestdata(data)

		}
	})
}
function showtestdata(data){


  var oTab = document.getElementById('testdatatable');

  var oTbody = oTab.tBodies[0];
  for (var index = 0; index < data.length; index++)
  {
    var oTr = document.createElement('tr');
    oTbody.appendChild(oTr);

    var oTd = document.createElement('td');
    oTd.innerHTML = data[index].label;
    oTr.appendChild(oTd);

    oTd = document.createElement('td');
    oTd.innerHTML = data[index].id;
    oTr.appendChild(oTd);

	oTd = document.createElement('td');
    oTd.innerHTML = data[index].gender;
    oTr.appendChild(oTd);

	oTd = document.createElement('td');
    oTd.innerHTML = data[index].job;
    oTr.appendChild(oTd);

	oTd = document.createElement('td');
    oTd.innerHTML = data[index].education;
    oTr.appendChild(oTd);

	oTd = document.createElement('td');
    oTd.innerHTML = data[index].marriage;
    oTr.appendChild(oTd);

    oTd = document.createElement('td');
    oTd.innerHTML = data[index].residence;
    oTr.appendChild(oTd);

    oTd = document.createElement('td');
        oTd.innerHTML = data[index].hits;
        oTr.appendChild(oTd);

    oTd = document.createElement('td');
    var s1=data[index].avg_last_bill_amount;
     s1=parseFloat(s1).toFixed(1);
        oTd.innerHTML = s1;
        oTr.appendChild(oTd);


    oTd = document.createElement('td');
    var s2=data[index].avg_repayment_amount;
         s2=parseFloat(s2).toFixed(1);
            oTd.innerHTML = s2;

        oTr.appendChild(oTd);

    oTd = document.createElement('td');
    var s3=data[index].avg_credit_limit;
             s3=parseFloat(s3).toFixed(1);
                oTd.innerHTML = s3;
        oTr.appendChild(oTd);

    oTd = document.createElement('td');
    var s4=data[index].avg_balance;
                 s4=parseFloat(s4).toFixed(1);
                    oTd.innerHTML = s4;
            oTr.appendChild(oTd);

    oTd = document.createElement('td');
        var s5=data[index].avg_min_repay_amount;
                     s5=parseFloat(s5).toFixed(1);
                        oTd.innerHTML = s5;

            oTr.appendChild(oTd);


oTd = document.createElement('td');
 var s6=data[index].bill_amount;
                     s6=parseFloat(s6).toFixed(1);
                        oTd.innerHTML = s6;

        oTr.appendChild(oTd);

        oTd = document.createElement('td');
                oTd.innerHTML = data[index].repay_status;
                oTr.appendChild(oTd);

    /*var oA = document.createElement('a');
    oA.innerHTML = "删除";
    oA.href = "javascript:;";
    oTd.appendChild(oA);
    oA.onclick = function () {
      oTbody.removeChild(this.parentNode.parentNode);
    }*/
  }
}
