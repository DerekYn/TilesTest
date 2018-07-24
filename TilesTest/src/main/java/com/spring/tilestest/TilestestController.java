package com.spring.tilestest;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.tilestest.service.InterTilestestService;


// ==== #26. ��Ʈ�ѷ� ����
@Controller
public class TilestestController {
	
	// ==== #31. ������ü �����ϱ�(DI : Dependency Injection)
	@Autowired
	private InterTilestestService service;
	
	@RequestMapping(value="/test1.action", method= {RequestMethod.GET})
	public String test1() {
		
		return "test/test1.tiles";
	}
	
	@RequestMapping(value="/test2.action", method= {RequestMethod.GET})
	public String test2() {
		
		return "test2.notiles";
	}
	
	@RequestMapping(value="/test3.action", method= {RequestMethod.GET})
	public String test3() {
		
		return "test3.tiles";
	}
	
	
	@RequestMapping(value="/index.action", method= {RequestMethod.GET})
	public String index(HttpServletRequest req) {
		
		List<String> imgfilenameList = service.getImgfilenameList();
		
		req.setAttribute("imgfilenameList", imgfilenameList);
		
		return "main/index.tiles";
	}
	
	@RequestMapping(value="/order.action", method= {RequestMethod.GET})
	public String order(HttpServletRequest req) {
		
		List<HashMap<String, String>> productNameList = service.getProductNameList();
		
		req.setAttribute("productNameList", productNameList);
		
		return "order/order.tiles";
	}
	
	
	@RequestMapping(value="/productDetailName.action", method= {RequestMethod.GET})
	public String productDetailName(HttpServletRequest req) {
		
		String productname = req.getParameter("productname");
		
		String str_jsonArray = service.getProductDetailNameList(productname);
		
		req.setAttribute("str_jsonArray", str_jsonArray);
		
		return "productDetailNameJSON.notiles";
	}
	
	
	/*
	  @ExceptionHandler �� ���ؼ�...
	  ==> � ��Ʈ�ѷ������� �߻��ϴ� �ͼ����� ������ �ͼ��� ó���� ���ַ��� �Ѵٸ�
	      @ExceptionHandler ������̼��� ������ �޼ҵ带 �������ָ� �ȴ�.
	      
	    ��Ʈ�ѷ������� @ExceptionHandler ������̼��� ������ �޼ҵ尡 �����ϸ�,
	    �������� �ͼ��� �߻��� @ExceptionHandler ������̼��� ������ �޼ҵ尡 ó�����ش�.     
	 */
	@ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
	public String hadleSQLIntegrityConstraintViolationException(java.sql.SQLIntegrityConstraintViolationException e, HttpServletRequest req) {  
		String msg = "";
		
		switch (e.getErrorCode()) {
			case 2291:
				msg = "��ǥ�� �����Ƿ� �ֹ��� ���еǾ����ϴ�.";
				break;
	
			default:
				msg = "SQL�����߻� >>������ȣ: " + e.getErrorCode();
				break;
		}
		
		String loc = "/tilestest/order.action";
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "msg.notiles";
	}
	
	
	@RequestMapping(value="/orderEnd.action", method= {RequestMethod.POST})
	public String orderEnd(HttpServletRequest req) throws Throwable {
		
		String productname = req.getParameter("productname");
		String productDetailTypeNo = req.getParameter("productDetailTypeNo");
		String oqty = req.getParameter("oqty");
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("productname", productname);
		map.put("productDetailTypeNo", productDetailTypeNo);
		map.put("oqty", oqty);
		
		int n = service.setOrder(map);
		
		req.setAttribute("n", n);
		
		return "order/orderEnd.tiles";
	}
	
	
	
	@RequestMapping(value="/rankShowJSON.action", method= {RequestMethod.GET})
	public String rankShowJSON(HttpServletRequest req) {
		
		String str_jsonArray = service.rankShowJSON();
		
		req.setAttribute("str_jsonArray", str_jsonArray);
		
		return "rankShowJSON.notiles";
	}

	
	
	@RequestMapping(value="/productDetailNameRankShowJSON.action", method= {RequestMethod.GET})
	public String productDetailNameRankShowJSON(HttpServletRequest req) {
		
		String productname = req.getParameter("productname");
		
		String str_jsonArray = service.productDetailNameRankShowJSON(productname);
		
		req.setAttribute("str_jsonArray", str_jsonArray);
		
		return "productDetailNameRankShowJSON.notiles";
	}
	
	
}











