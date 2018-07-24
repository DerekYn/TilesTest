package com.spring.tilestest.service;

import java.util.HashMap;
import java.util.List;

public interface InterTilestestService {

	List<String> getImgfilenameList();

	List<HashMap<String, String>> getProductNameList();

	String getProductDetailNameList(String productname);

	int setOrder(HashMap<String, String> map) throws Throwable;

	String rankShowJSON();

	String productDetailNameRankShowJSON(String productname);

}
