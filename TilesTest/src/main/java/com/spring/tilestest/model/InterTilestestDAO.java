package com.spring.tilestest.model;

import java.util.HashMap;
import java.util.List;

public interface InterTilestestDAO {

	List<String> getImgfilenameList();

	List<HashMap<String, String>> getProductNameList();

	List<HashMap<String, String>> getProductDetailNameList(String productname);

	//-------------------------------------------------------------------
	String getProductno(String productname);
	String getProductDetailno(HashMap<String, String> map);
	int set_tilestest_order(String memberid);
	int set_tilestest_orderDetail(HashMap<String, String> map);
	//-------------------------------------------------------------------

	List<HashMap<String, String>> rankShowJSON();

	List<HashMap<String, String>> productDetailNameRankShowJSON(String productname);
	

}
