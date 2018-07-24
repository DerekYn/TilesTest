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


// ==== #26. 컨트롤러 선언
@Controller
public class TilestestController {
	
	// ==== #31. 의존객체 주입하기(DI : Dependency Injection)
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
	  @ExceptionHandler 에 대해서...
	  ==> 어떤 컨트롤러내에서 발생하는 익셉션이 있을시 익셉션 처리를 해주려고 한다면
	      @ExceptionHandler 어노테이션을 적용한 메소드를 구현해주면 된다.
	      
	    컨트롤러내에서 @ExceptionHandler 어노테이션을 적용한 메소드가 존재하면,
	    스프링은 익셉션 발생시 @ExceptionHandler 어노테이션을 적용한 메소드가 처리해준다.     
	 */
	@ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
	public String hadleSQLIntegrityConstraintViolationException(java.sql.SQLIntegrityConstraintViolationException e, HttpServletRequest req) {  
		String msg = "";
		
		switch (e.getErrorCode()) {
			case 2291:
				msg = "전표가 없으므로 주문이 실패되었습니다.";
				break;
	
			default:
				msg = "SQL오류발생 >>오류번호: " + e.getErrorCode();
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











