package com.evanpenner.novusgis.views;

import com.evanpenner.novusgis.entities.Customer;
import com.evanpenner.novusgis.entities.layouts.MainLayout;
import com.evanpenner.novusgis.repositories.CustomerRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

@PageTitle("Customers")
@Route(value = "customers", layout = MainLayout.class)
public class CustomersView extends VerticalLayout {
    private final CustomerRepository customerRepository;

    public CustomersView(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        buildBody();
    }

    private void buildBody() {
        Grid<Customer> grid = new Grid<>();
        grid.setItems(query -> customerRepository.findAll(PageRequest.of(query.getPage(), query.getPageSize())).stream());
        grid.addColumn(Customer::getName).setHeader("Name");
        grid.addComponentColumn(customer -> {
            Button button = new Button("Delete", event -> {
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("Delete \"" + customer.getName() + "\"?");
                dialog.setText("Are you sure you want to permanently delete this customer? All subprojects will be lost.");

                dialog.setCancelable(true);
                dialog.addCancelListener(e -> Notification.show("Canceled!"));

                dialog.setConfirmText("Delete");
                dialog.setConfirmButtonTheme("error primary");
                dialog.addConfirmListener(event2 -> {
                    customerRepository.delete(customer);
                    grid.getLazyDataView().refreshItem(customer);
                    Notification.show("Successfully deleted '" + customer.getName() + "'");
                });
                dialog.open();


            });
            button.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return button;
        });
        Button createCustomerButton = new Button("Create Customer", event -> {
            Dialog createCustomerDialog = new Dialog();
            createCustomerDialog.setHeaderTitle("Create Customer");
            TextField custName = new TextField("Customer Name");
            createCustomerDialog.add(custName);
            Button submit = new Button("Submit", e -> {
                if (custName.isEmpty()) {
                    Notification.show("Customer name can't be empty");
                    return;
                }
                if (customerRepository.existsByNameEqualsIgnoreCase(custName.getValue())) {
                    Notification.show("A customer by that name already exists.");
                    return;
                }
                Customer customer = new Customer();
                customer.setName(custName.getValue());
                customer.setProjects(new ArrayList<>());
                customerRepository.save(customer);
                grid.getLazyDataView().refreshItem(customer);
                createCustomerDialog.close();
                Notification.show("Customer successfully created.");
            });
            createCustomerDialog.getFooter().add(submit);
            createCustomerDialog.open();
        });
        add(createCustomerButton, grid);
    }
}
