package com.spring.tilestest.model;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// #28. DAO 선언
@Repository
public class TilestestDAO implements InterTilestestDAO {
	
	// ==== #29. 의존객체 주입하기(DI : Dependency Injection)
	@Autowired
	private SqlSessionTemplate sqlsession;

	@Override
	public List<String> getImgfilenameList() {
		List<String> imgfilenameList = sqlsession.selectList("tilestest.getImgFilenameList"); 
		return imgfilenameList;
	}

	@Override
	public List<HashMap<String, String>> getProductNameList() {
		List<HashMap<String, String>> productNameList = sqlsession.selectList("tilestest.getProductNameList");
		return productNameList;
	}

	@Override
	public List<HashMap<String, String>> getProductDetailNameList(String productname) {
		List<HashMap<String, String>> list = sqlsession.selectList("tilestest.getProductDetailNameList", productname);
		return list;
	}

	
	//-------------------------------------------------------------------
	
	@Override
	public String getProductno(String productname) {
		String productno = sqlsession.selectOne("tilestest.getProductno", productname);
		return productno;
	}

	@Override
	public String getProductDetailno(HashMap<String, String> map) {
		String productDetailno = sqlsession.selectOne("tilestest.getProductDetailno", map);
		return productDetailno;
	}

	@Override
	public int set_tilestest_order(String memberid) {
		int n = sqlsession.insert("tilestest.set_tilestest_order", memberid);
		return n;
	}

	@Override
	public int set_tilestest_orderDetail(HashMap<String, String> map) {
		int n = sqlsession.insert("tilestest.set_tilestest_orderDetail", map);
		return n;
	}
	//-------------------------------------------------------------------

	@Override
	public List<HashMap<String, String>> rankShowJSON() {
		List<HashMap<String, String>> list = sqlsession.selectList("tilestest.rankShowJSON");
		return list;
	}

	@Override
	public List<HashMap<String, String>> productDetailNameRankShowJSON(String productname) {
		List<HashMap<String, String>> list = sqlsession.selectList("tilestest.productDetailNameRankShowJSON", productname);
		return list;
	}

}
