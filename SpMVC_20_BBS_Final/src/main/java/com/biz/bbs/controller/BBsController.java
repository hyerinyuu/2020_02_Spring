package com.biz.bbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.biz.bbs.domain.BBsVO;
import com.biz.bbs.service.BBsService;


@Controller
public class BBsController {

	/*
	 * 현재 BBsController와 BBsService* 는 
	 * BBsService Interface를 거쳐서 연결이 되어있다.
	 * BBsService Interface를 implement한 클래스의 코드가 미완성 상태이지만
	 * BBsController 입장에서는 BBsServicve*의 코드가 완성되어있다라는 전제하에 Controller 코드 작성을 진행할 수 있다.
	 * 
	 * 만약 이후에 BBsService*의 코드가 완성되고
	 * 		여러개의 BBsServiceImp*가 작성되면
	 * 		필요한 클래스를 가져다가 부착만 하면 프로젝트가 완성됨
	 * 
	 * 결합도를 낮추는 결과가 됨.
	 * -- 결합도 : 모듈간의 의존도, 모듈 코드 작성의 유연성과 관련
	 * -- 응집도 : 
	 * ※※※※ 결합도는 낮게 응집도는 높은 모듈간 연계가 좋은 설계임
	 */
	@Autowired
	private BBsService bbsService;
	
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list() {
		bbsService.selectAll();
		return "bbs_list";
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.GET)
	public String insert() {
		return "bbs_write";
	}

	public String insert(BBsVO bbsVO) {
		bbsService.insert(bbsVO);
		return null;
	}
	
	public String update(BBsVO bbsVO) {
		bbsService.update(bbsVO);
		return null;
	}
	
	public String delete(String strId) {
		bbsService.delete(Long.valueOf(strId));
		return null;
	}
	
	
	
}
