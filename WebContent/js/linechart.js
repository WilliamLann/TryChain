$(document).ready(function() {
	var perDayTxSum = $("#perDayTxSum").text();
	var data = JSON.parse(perDayTxSum);
	var time_arr = [];
	var tx_arr = [];
	var arr = [];
	for (var i = 0; i < data.length; i++) {
		arr = data[i];
		time_arr.push(arr.block_date);
		tx_arr.push(arr.txCount);
	}
	var title = {text : '最新交易情况'};
	var subtitle = {text : ''};
	var xAxis = {
		categories : time_arr,
		tickmarkPlacement : 'on'
	};
	var yAxis = {
		title : {text : '数量'},
		plotLines : [ {
			value : 0,
			width : 1,
			color : '#808080'
		} ]
	};
	var tooltip = {valueSuffix : ''};
	var credits = {enabled : false};

	var legend = {
		layout : 'vertical',
		align : 'right',
		verticalAlign : 'middle',
		borderWidth : 0
	};

	var series = [ {
		name : '交易数',
		data : tx_arr
	} ];

	var json = {};
	json.title = title;
	json.subtitle = subtitle;
	json.xAxis = xAxis;
	json.yAxis = yAxis;
	json.tooltip = tooltip;
	json.credits = credits;
	json.legend = legend;
	json.series = series;
	$('#linechart').highcharts(json);
});