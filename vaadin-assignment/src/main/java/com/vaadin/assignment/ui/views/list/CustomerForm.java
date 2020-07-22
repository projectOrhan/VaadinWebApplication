package com.vaadin.assignment.ui.views.list;



import com.vaadin.assignment.backend.entity.BirthCity;
import com.vaadin.assignment.backend.entity.Customer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;


import java.util.List;


public class CustomerForm extends FormLayout {

    TextField firstName = new TextField("Name");
    TextField lastName = new TextField("Surname");
    ComboBox<Customer.Gender> gender = new ComboBox<>("Gender");
    ComboBox<Customer.Flag> flag = new ComboBox<>("Flag");
    ComboBox<BirthCity> birthCity = new ComboBox<>("Birth City");
    DatePicker birthDate = new DatePicker("Birth Date");

    Button save = new Button("Update");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);

    public CustomerForm(List<BirthCity> companies) {
        addClassName("customer-form");

        binder.bindInstanceFields(this);
        flag.setItems(Customer.Flag.values());
        gender.setItems(Customer.Gender.values());
        birthCity.setItems(companies);
        birthCity.setItemLabelGenerator(BirthCity::getName);
        add(
                firstName,
                lastName,
                flag,
                gender,
                birthCity,
                birthDate,
                createButtonsLayout()
        );
    }

    public void setCustomer(Customer customer) {
        binder.setBean(customer);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        //butonlara tıklanıldığında yapılacak işlemler.
        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        cancel.addClickListener(click -> fireEvent(new CloseEvent(this)));

        //save butonunu aktif ve pasif yapmak için binder ı kontrol etmek.
        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {

        private Customer customer;

        protected CustomerFormEvent(CustomerForm source, Customer customer) {
            super(source, false);
            this.customer = customer;
        }

        public Customer getCustomer() {
            return customer;
        }
    }

    public static class SaveEvent extends CustomerFormEvent {
        SaveEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }
    public static class DeleteEvent extends CustomerFormEvent {
        DeleteEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }
    public static class CloseEvent extends CustomerFormEvent {
        CloseEvent(CustomerForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType,listener);
    }

}
