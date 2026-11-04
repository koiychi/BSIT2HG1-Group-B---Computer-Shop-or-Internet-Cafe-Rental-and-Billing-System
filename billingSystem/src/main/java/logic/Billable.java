/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package logic;

import java.math.*;

public interface Billable {
    double calculateTotalCharge();
    String generateInvoice();
    
        // line 1 of the invoice text (the label)
    default String getInvoiceTitle() {
        String invoice = generateInvoice();
        int i = invoice.indexOf('\n');
        return i < 0 ? invoice : invoice.substring(0, i);
    }

    // everything after line 1
    default String getInvoiceDetails() {
        String invoice = generateInvoice();
        int i = invoice.indexOf('\n');
        return i < 0 ? "" : invoice.substring(i + 1);
    }

    // total rounded to 2 decimals; a bill of 0 or less is rejected
    default BigDecimal getAmountDue() {
        BigDecimal due = BigDecimal.valueOf(calculateTotalCharge()).setScale(2, RoundingMode.HALF_UP);
        if (due.signum() <= 0) {
            throw new IllegalArgumentException("Total amount due must be greater than 0.");
        }
        return due;
    }

    // the customer decides its own rate, the bill only multiplies
    default BigDecimal getDiscountFor(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        return getAmountDue().multiply(BigDecimal.valueOf(customer.getDiscountRate()))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
