package com.vaadin.assignment.ui.views.reports;

import com.vaadin.assignment.backend.service.BirthCityService;
import com.vaadin.assignment.backend.service.CustomerService;
import com.vaadin.assignment.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Map;

//Window Title
@PageTitle("Reports | Vaadin Assignment")
@Route(value = "reports",layout = MainLayout.class)
public class ReportsView extends VerticalLayout {

    private final CustomerService customerService;
    private final BirthCityService birthCityService;

    public ReportsView(CustomerService customerService,
                       BirthCityService birthCityService) {
        this.customerService = customerService;
        this.birthCityService = birthCityService;

        addClassName("reports-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(
                getCustomersStats(),
                //pie chart of companies
                getBirthCityChart()
        );

    }

    private Component getBirthCityChart() {
        Chart chart = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries();
        Map<String, Integer> stats = birthCityService.getStats();
        stats.forEach((name, number) ->
                dataSeries.add(new DataSeriesItem(name, number)));

        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }

    private Span getCustomersStats() {
        Span stats = new Span(customerService.count() + " Customers");
        stats.addClassName("customer-stats");

        return stats;
    }
}
