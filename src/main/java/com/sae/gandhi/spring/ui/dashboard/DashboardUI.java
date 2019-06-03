package com.sae.gandhi.spring.ui.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.GraphicService;
import com.sae.gandhi.spring.vo.GraphStudentVO;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import be.ceau.chart.BarChart;
import be.ceau.chart.color.Color;
import be.ceau.chart.data.BarData;
import be.ceau.chart.dataset.BarDataset;
import be.ceau.chart.javascript.JavaScriptFunction;
import be.ceau.chart.options.BarOptions;
import be.ceau.chart.options.Legend;
import be.ceau.chart.options.Title;

@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Dashboars")
public class DashboardUI extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1424862921169684218L;
	
	private GraphicService graphicService;

	public DashboardUI(GraphicService graphicService){
		this.graphicService = graphicService;
		ChartJs barChartJs = new ChartJs(getCoursesBarChart());
        Div div2 = new Div();
        div2.add(barChartJs);
        div2.setHeight("800px");
        div2.setWidth("800px");
        add(div2);
	}
	
	private String getCoursesBarChart(){
		List<GraphStudentVO> lstCourseStudent = graphicService.getCourseGraphData();
		
		List<Long> lstStudents = new ArrayList<>();
		List<String> lstCourses = new ArrayList<>();
				
		for(GraphStudentVO vo : lstCourseStudent){
			lstCourses.add(vo.getCourse());
			lstStudents.add(vo.getStudents());
		}
		
        BarDataset dataset = new BarDataset()
                .setLabel("Cursos")
                //.addBackgroundColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.GRAY, Color.BLACK)
                .setBorderWidth(2);
        
        for(GraphStudentVO vo : lstCourseStudent){
        	dataset.addData(vo.getStudents());
        	dataset.addBackgroundColor(Color.random());
        }

        BarData data = new BarData()
                //.addLabels(lstCourses)
                .addDataset(dataset);
        
        for(GraphStudentVO vo : lstCourseStudent){
        	data.addLabel(vo.getCourse());
        }

        JavaScriptFunction label = new JavaScriptFunction(
                "\"function(chart) {console.log('test legend');}\""
        );

        BarOptions barOptions = new BarOptions()
                .setResponsive(true)
                .setTitle(new Title().setText("test"))
                .setLegend(new Legend().setDisplay(true)
                        .setOnClick(label));

        //System.out.printf(new BarChart(data,barOptions).toJson());
        //System.out.printf(StringUtils.deleteWhitespace(new BarChart(data,barOptions).toJson()));
        return new BarChart(data,barOptions).toJson();
    }
	
}
