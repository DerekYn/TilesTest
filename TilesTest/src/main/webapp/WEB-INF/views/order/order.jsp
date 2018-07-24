<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<div align="center" style="margin-bottom: 20px;">
	<h2>주 문 하 기</h2>
</div>

<div style="width: 80%; margin: 0 auto;">

	<div style="width: 20%; float: left; margin-left: 10%; border: solid orange 0px;">
		<label for="productname">제품명</label>
		<select class="form-control" name="productname" id="productname">
			<c:if test="${productNameList != null}">
				<c:forEach var="map" items="${productNameList}">
					<option>${map.PRODUCTNAME}</option>
				</c:forEach>
			</c:if>
		</select>
	</div>
	
	
	<div style="width: 20%; float: left; margin-left: 10%; border: solid orange 0px;">
		<label for="productDetailTypeNo">타입</label>
		<div id="selectProductDetailTypeNo">
			
		</div>
		
	</div>
	
	
	<div style="width: 20%; float: left; margin-left: 10%; border: solid orange 0px;">
		<label for="oqty">주문량</label>
		<div id="selectOqty">
		</div>
	</div>
	
	
	&nbsp;
	<div style="clear: both; margin-top: 100px; padding-left: 40%; border: solid orange 0px;">
		<button type="button" class="btn btn-success btn-sm" onClick="goOrder();">주 문 하 기</button>
	</div>
	
</div>




<script type="text/javascript">

	$(document).ready(function(){
		
		goGetproductDetailName();
		goGetOqty();
		
		$("#productname").bind("change", function(){
			goGetproductDetailName();
			goGetOqty();
		});
		
		$("#selectProductDetailTypeNo").bind("change", function(){
			goGetOqty();
		});
		
		
	});
	
	function goGetproductDetailName() {
		
		var form_data = { productname : $("#productname").val() };
		
		$.ajax({
			
			url: "productDetailName.action",
			type: "GET",
			data: form_data,
			dataType: "JSON",
			success: function(json){
				$("#selectProductDetailTypeNo").empty();
				
				var html =  "<select class='form-control' name='productDetailTypeNo' id='productDetailTypeNo'>";
					
				$.each(json, function(entryIndex, entry){
					html += "<option value=\""+entry.PRODUCTDETAILTYPENO+"\">"+entry.PRODUCTDETILNAME+"</option>";
				});
				
				html +=	"</select>";
				
				$("#selectProductDetailTypeNo").append(html);
							
			},
			error: function(request, status, error){
	 			   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	 		}
			
			
		});
		
	}// end of goGetproductDetailName()--------------------------------------------------------------------------------
	
	
	
	function goGetOqty() {
		
		$("#selectOqty").empty();
		
		var html =  "<select class='form-control' name='oqty' id='oqty'>";
		
		<c:forEach var="n" begin="1" end="5">
			html += "<option value='${n}'>${n}</option>";
		</c:forEach>
		
		html +=	"</select>";
		
		$("#selectOqty").html(html);
		
	}// end of function goGetOqty()----------------------------------------------------------------
	
	
	
	function goOrder() {
		
		var form_data = { productname : $("#productname").val(),
				          productDetailTypeNo : $("#productDetailTypeNo").val(),
				          oqty : $("#oqty").val() };
		
		$.ajax({
			url: "orderEnd.action",
			type: "POST",
			data: form_data,
			success: function(){
				getTableRank();     // 등수를 나타내어주는 테이블 결과를 갱신해서 보여준다.(함수로)
				getChartRank();     // 등수를 나타내어주는 chart 결과를 갱신해서 보여준다.(함수로)
			},
			error: function(request, status, error){
	 			   alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	 		}
		});
		
	}// end of function goOrder()------------------------------------------------------------------
	
	
	
	
	
	

</script>




















