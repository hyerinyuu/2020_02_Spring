package com.biz.bbs.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

import com.biz.bbs.domain.BBsVO;
import com.biz.bbs.repository.BBsDao;

@Service
public class BBsServiceImpl implements BBsService{

	private final BBsDao bbsDao;
	
	public BBsServiceImpl(BBsDao bbsDao) {
		this.bbsDao = bbsDao;
	}
	
	@Override
	public List<BBsVO> selectAll() {
		return bbsDao.selectAll();
	}

	@Override
	public BBsVO findById(long b_id) {
		return bbsDao.findById(b_id);
	}

	@Override
	public int insert(BBsVO bbsVO) {
		
		// 작성일자를 현재 저장하는 날짜로 세팅
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
		bbsVO.setB_date_time(ldt.format(df).toString());
		return bbsDao.insert(bbsVO); 
	}

	@Override
	public int delete(long b_id) {
		return bbsDao.delete(b_id);
	}

	@Override
	public int update(BBsVO bbsVO) {
		
		// 작성일자를 현재 저장하는 날짜로 세팅
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
		bbsVO.setB_date_time(ldt.format(df).toString());
		return bbsDao.update(bbsVO);
	}

	@Override
	public List<BBsVO> findBySubject(String b_subject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BBsVO> findByWriter(String b_writer) {
		// TODO Auto-generated method stub
		return null;
	}

}
