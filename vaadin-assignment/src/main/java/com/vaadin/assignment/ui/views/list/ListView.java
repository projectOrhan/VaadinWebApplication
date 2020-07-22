package com.vaadin.assignment.ui.views.list;

import com.vaadin.assignment.backend.entity.BirthCity;
import com.vaadin.assignment.backend.entity.Customer;
import com.vaadin.assignment.backend.service.BirthCityService;
import com.vaadin.assignment.backend.service.CustomerService;
import com.vaadin.assignment.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;


@Route(value = "",layout = MainLayout.class)
@PageTitle("Customer Information | Vaadin Assignment")
public class ListView extends VerticalLayout {

    private final CustomerForm form;
    Grid<Customer> grid = new Grid<>(Customer.class);
    TextField filterText = new TextField();

    private CustomerService customerService;

    public ListView(CustomerService customerService,
                    BirthCityService birthCityService) {
        this.customerService = customerService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new CustomerForm(birthCityService.findAll());
        form.addListener(CustomerForm.SaveEvent.class, this::saveCustomer);
        form.addListener(CustomerForm.DeleteEvent.class, this::deleteCustomer);
        form.addListener(CustomerForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();

        //eğer herhangi bir item seçilmediyse formu kapat.
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("customer-grid");
        grid.setSizeFull();
        grid.removeColumnByKey("birthCity");
        grid.setColumns("firstName", "lastName", "gender", "flag", "birthDate");
        grid.addColumn(customer -> {
            BirthCity birthCity = customer.getBirthCity();
            return birthCity == null ? "-" : birthCity.getName();
        }).setHeader("Birth City");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editCustomer(evt.getValue()));
    }

    private void editCustomer(Customer customer) {
        if (customer == null) {
            closeEditor();
        } else {
            form.setCustomer(customer);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void deleteCustomer(CustomerForm.DeleteEvent evt) {
        customerService.delete(evt.getCustomer());
        updateList();
        closeEditor();
    }

    private void saveCustomer(CustomerForm.SaveEvent evt) {
        customerService.save(evt.getCustomer());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setCustomer(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCustomerButton = new Button("Add Customer", click -> addCustomer());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCustomerButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCustomer() {
        grid.asSingleSelect().clear();
        editCustomer(new Customer());
    }

    private void updateList() {
        grid.setItems(customerService.findAll(filterText.getValue()));
    }

}