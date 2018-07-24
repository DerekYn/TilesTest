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

// #27. Service ���� 
@Service
public class TilestestService implements InterTilestestService {
	
	// ==== #30. ������ü �����ϱ�(DI : Dependency Injection)
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
		// tilestest_product ���̺��� ��ǰ�� �ش��ϴ� ��ǰ��ȣ ������
		map.put("productno", productno);	// Contriller�� �ִ� map���� "��ǰ��ȣ"�� �����ϱ� ���־��ش�.
		
		
		String productDetailno = dao.getProductDetailno(map);
		// tilestest_productDetail ���̺��� "��ǰ��ȣ"�� "��ǰ��Ÿ�Թ�ȣ"�� �ش��ϴ� ��ǰ�󼼹�ȣ(Primary Key��) ������
		map.put("productDetailno", productDetailno);
		
		
		String memberid = "yoonchanyoung";
		int n = dao.set_tilestest_order(memberid);
		// tilestest_product ���̺� insert �ϱ�
		
		
		// �ֹ��󼼹�ȣ(������)�ʿ�X, �ֹ���ȣ(��ǥ)�ʿ�X, ��ǰ�󼼹�ȣ, �ֹ��� //
		int m = dao.set_tilestest_orderDetail(map);
		// tilestest_productDetail ���̺� insert �ϱ�
				
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
