package com.spring.tilestest.service;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.tilestest.model.InterTilestestDAO;

// #27. Service 선언 
@Service
public class TilestestService implements InterTilestestService {
	
	// ==== #30. 의존객체 주입하기(DI : Dependency Injection)
	@Autowired
	private InterTilestestDAO dao;

	@Override
	public List<String> getImgfilenameList() {
		List<String> imgfilenameList = dao.getImgfilenameList();
		return imgfilenameList;
	}

	@Override
	public List<HashMap<String, String>> getProductNameList() {
		List<HashMap<String, String>> productNameList = dao.getProductNameList();
		return productNameList;
	}

	@Override
	public String getProductDetailNameList(String productname) {
		JSONArray jsonArray = new JSONArray();
		String str_jsonArray = "";
		
		if(productname != null && !productname.trim().isEmpty()) {
			
			List<HashMap<String, String>> list = dao.getProductDetailNameList(productname);
			
			if(list != null && list.size() > 0) {
				for(HashMap<String, String> map : list) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("PRODUCTDETAILTYPENO", map.get("PRODUCTDETAILTYPENO"));
					jsonObj.put("PRODUCTDETILNAME", map.get("PRODUCTDETILNAME"));
					
					jsonArray.put(jsonObj);
				}
			}
			
		}
		
		str_jsonArray = jsonArray.toString();
		System.out.println("getProductDetailNameList : " + str_jsonArray);
		
		return str_jsonArray;
	}

	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor={Throwable.class} )    
	public int setOrder(HashMap<String, String> map) throws Throwable {
		
		String productname = map.get("productname");
		String productno = dao.getProductno(productname);
		// tilestest_product 테이블에서 제품명에 해당하는 제품번호 얻어오기
		map.put("productno", productno);	// Contriller에 있던 map에는 "제품번호"가 없으니까 껴넣어준다.
		
		
		String productDetailno = dao.getProductDetailno(map);
		// tilestest_productDetail 테이블에서 "제품번호"와 "제품상세타입번호"에 해당하는 제품상세번호(Primary Key값) 얻어오기
		map.put("productDetailno", productDetailno);
		
		
		String memberid = "yoonchanyoung";
		int n = dao.set_tilestest_order(memberid);
		// tilestest_product 테이블에 insert 하기
		
		
		// 주문상세번호(시퀀스)필요X, 주문번호(전표)필요X, 제품상세번호, 주문량 //
		int m = dao.set_tilestest_orderDetail(map);
		// tilestest_productDetail 테이블에 insert 하기
				
		return (n + m);
		
	}

	@Override
	public String rankShowJSON() {
		
		List<HashMap<String, String>> list = dao.rankShowJSON();
		
		JSONArray jsonArray = new JSONArray();
		String str_jsonArray = "";
		
		if(list != null && list.size() > 0) {
			for(HashMap<String, String> map : list) {
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("RANK", map.get("RANK"));
				jsonObj.put("PRODUCTNAME", map.get("PRODUCTNAME"));
				jsonObj.put("TOTALQTY", map.get("TOTALQTY"));
				jsonObj.put("PERCENT", map.get("PERCENT"));
				
				jsonArray.put(jsonObj);
			}
		}
		
		str_jsonArray = jsonArray.toString();
		System.out.println("rankShowJSON" + str_jsonArray);
		
		return str_jsonArray;
	}

	@Override
	public String productDetailNameRankShowJSON(String productname) {

		List<HashMap<String, String>> list = dao.productDetailNameRankShowJSON(productname);
		
		JSONArray jsonArray = new JSONArray();
		String str_jsonArray = "";
		
		if(list != null && list.size() > 0) {
			for(HashMap<String, String> map : list) {
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("PRODUCTDETAILNAME", map.get("PRODUCTDETAILNAME"));
				jsonObj.put("PERCENT", map.get("PERCENT"));
				
				jsonArray.put(jsonObj);
			}
		}
		
		str_jsonArray = jsonArray.toString();
		System.out.println("productDetailNameRankShowJSON" + str_jsonArray);
		
		return str_jsonArray;
		
	}

	
	
}
