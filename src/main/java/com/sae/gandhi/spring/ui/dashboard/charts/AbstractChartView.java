package com.sae.gandhi.spring.ui.dashboard.charts;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractChartView extends VerticalLayout implements ChartView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3186078729859841820L;

	@PostConstruct
    public void postConstruct() {
        setSizeFull();
        setMargin(true);
        Component layout = getChart();
        //layout.setWidth(100, Unit.PERCENTAGE);
        add(layout);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

}
