<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- ======= #22. tiles 중 sideinfo 페이지 만들기  ======= --%>



<!-- 차트그리기 --> 
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/data.js"></script>
<script src="https://code.highcharts.com/modules/drilldown.js"></script>




<script type="text/javascript">

	$(document).ready(function() {
		loopshowNowTime();
		showRank();
	
		
	}); // end of ready(); ---------------------------------

	function showNowTime() {
		
		var now = new Date();
	
		var strNow = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate();
		
		var hour = "";
		if(now.getHours() > 12) {
			hour = " 오후 " + (now.getHours() - 12);
		} else if(now.getHours() < 12) {
			hour = " 오전 " + now.getHours();
		} else {
			hour = " 정오 " + now.getHours();
		}
		
		var minute = "";
		if(now.getMinutes() < 10) {
			minute = "0"+now.getMinutes();
		} else {
			minute = ""+now.getMinutes();
		}
		
		var second = "";
		if(now.getSeconds() < 10) {
			second = "0"+now.getSeconds();
		} else {
			second = ""+now.getSeconds();
		}
		
		strNow += hour + ":" + minute + ":" + second;
		
		$("#clock").html("<span style='color:green; font-weight:bold;'>"+strNow+"</span>");
	
	}// end of function showNowTime() -----------------------------


	function loopshowNowTime() {
		showNowTime();
		
		var timeCycle = 1000;   // 시간을 1초 마다 자동 갱신하려고.
		
		setTimeout(function() {
						loopshowNowTime();	
					}, timeCycle);
		
	}// end of loopshowNowTime() --------------------------
	
	
	function getTableRank() {
		
		$.ajax({
			
			url: "rankShowJSON.action",
			type: "GET",
			dataType: "JSON",
			success: function(json){
				$("#displayTable").empty();
				
				var html =  "<table class='table table-hover' align='center' width='250px' height='180px' >";
				    html += "<tr>";
				    html += "<th class='myaligncenter'>등수</th>";
				    html += "<th class='myaligncenter'>제품명</th>";
				    html += "<th class='myaligncenter'>총 주문량</th>";
				    html += "</tr>";
				    
				$.each(json, function(entryIndex, entry) {
					
					html += "<tr>";
					html += "<td class='myaligncenter myrank'>"+entry.RANK+"</td>";
					html += "<td class='myaligncenter'>"+entry.PRODUCTNAME+"</td>";
					html += "<td class='myaligncenter'>"+entry.TOTALQTY+"</td>";
					html += "</tr>";
					
				});
				
				html += "</table>";				    
				
				$("#displayTable").append(html);
			},
			error: function(request, status, error){
	 			   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	 		}
			
		});
		
		
		
	}// end of function getTableRank()-------------------------------------------------------------
	
	
	
	function getChartRank() {
		
		$.ajax({
			url: "rankShowJSON.action",
			type: "GET",
			datatType: "JSON",
			success: function(json){
				
				var productObjArr = [];
				
				
	            
				$.each(json, function(entryIndex, entry) {
	            	productObjArr.push({
	                       "name": entry.PRODUCTNAME,
	                       "y": parseFloat(entry.PERCENT),
	                       "drilldown": entry.PRODUCTNAME
	                   });
	            });//end of $.each()------------------------------------------------------------------
				
	            
	            var productObjDetailArr = [];
		
				$.each(json, function(entryIndex, entry) {

					var form_data = { productname : entry.PRODUCTNAME };
					
					$.ajax({
						url: "productDetailNameRankShowJSON.action",
						data: form_data,
						type: "GET",
						dataType: "JSON",
						success: function(json2){
							
							var subArr = [];
							
							$.each(json2, function(entryIndex2, entry2) {
								subArr.push([
				                    entry2.PRODUCTDETAILNAME,
				                    parseFloat(entry2.PERCENT)
								]);
								
							});// end of $.each(json2, function(entryIndex2, entry2)---------------------
							
							productObjDetailArr.push({
								"name": entry.PRODUCTNAME,
				                "id": entry.PRODUCTNAME,
				                "data": subArr
							});
							
						},
						error: function(request, status, error){
				 			   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				 		}
					});	
					
				});
				
				
/////////////////////////////////////
				// Create the chart
				Highcharts.chart('chart-container', {
				    chart: {
				        type: 'column'
				    },
				    title: {
				        text: '제품별 판매 점유율'
				    },
				    subtitle: {
				       // text: 'Click the columns to view versions. Source: <a href="http://statcounter.com" target="_blank">statcounter.com</a>'
				    },
				    xAxis: {
				        type: 'category'
				    },
				    yAxis: {
				        title: {
				            text: '점유율(%)'
				        }
		
				    },
				    legend: {
				        enabled: false
				    },
				    plotOptions: {
				        series: {
				            borderWidth: 0,
				            dataLabels: {
				                enabled: true,
				                format: '{point.y:.1f}%'
				            }
				        }
				    },
		
				    tooltip: {
				        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
				        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
				    },
		
				    "series": [
				        {
				            "name": "판매제품",
				            "colorByPoint": true,
				            "data": productObjArr  // *** 위에서 구한값을 대입시킴. *** 
				         
				        }
				    ],
				    "drilldown": {
				    	"series": productObjDetailArr  // *** 위에서 구한값을 대입시킴. *** 
				   
				    }
				});
		    	/////////////////////////////////////////
		    },
		    error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
		
		
	}// end of getChartRank()-------------------------------
	
	function showRank() {
		getTableRank();
		getChartRank();
		
		var timeCycle = 10000;   // 시간을 10초 마다 자동 갱신하려고.
		
		setTimeout(function() {
			           showRank();	
					}, timeCycle);
	}
	
</script>


<div style="margin: 0 auto;" align="center">
	현재시각 :&nbsp; 
	<div id="clock" style="display:inline;"></div>
</div>
	
<div id="displayTable" style="min-width: 90%; margin-top: 50px; margin-bottom: 50px; padding-left: 20px; padding-right: 20px; "></div>

<div id="chart-container" style="min-width: 90%; min-height: 400px; margin: 0 auto"></div>
	
	
	
	
	