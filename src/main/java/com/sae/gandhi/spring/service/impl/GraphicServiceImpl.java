package com.sae.gandhi.spring.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.CursosDAO;
import com.sae.gandhi.spring.service.GraphicService;
import com.sae.gandhi.spring.vo.GraphStudentVO;

@Transactional
@Service
public class GraphicServiceImpl implements GraphicService {

	@Autowired
	private CursosDAO cursosDAO;
	
	@Override
	public List<GraphStudentVO> getCourseGraphData() {
		return cursosDAO.getCourseGraphData(Calendar.getInstance().getTime());
	}

}
