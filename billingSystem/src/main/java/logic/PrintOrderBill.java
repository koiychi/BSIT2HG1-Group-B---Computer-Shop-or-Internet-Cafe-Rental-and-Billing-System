/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

/**
 *
 * @author Cals
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrintOrderBill implements Billable {

    private final List<PrintJob> jobs = new ArrayList<>();

    public List<PrintJob> getJobs() {
        return Collections.unmodifiableList(jobs);
    }

    public void addJob(PrintJob job) {
        if (job == null) {
            throw new IllegalArgumentException("Print job cannot be null.");
        }
        jobs.add(job);
    }

    @Override
    public double calculateTotalCharge() {
        double total = 0.0;
        for (PrintJob job : jobs) {
            total += job.calculateTotalCharge();
        }
        return total;
    }

    // line 1 = label, the other lines = details
    @Override
    public String generateInvoice() {
        if (jobs.isEmpty()) {
            throw new IllegalStateException("There are no print jobs.");
        }
        List<String> services = new ArrayList<>();
        String title = "";
        String details = "";
        for (PrintJob job : jobs) {
            String service = job.getServiceType();
            if (!services.contains(service)) {
                services.add(service);
                title = title.isEmpty() ? service : title + " + " + service;
            }
            if (!details.isEmpty()) {
                details += "\n";
            }
            details += service + ": " + job.getPageSize() + " - "
                    + (job.getIsColored() ? "Colored" : "B/W")
                    + " (" + job.getPageCount() + ")";
        }
        return title + "\n" + details;
    }
}