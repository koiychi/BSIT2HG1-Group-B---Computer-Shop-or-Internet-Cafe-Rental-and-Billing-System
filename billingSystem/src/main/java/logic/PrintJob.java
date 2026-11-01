package logic;

/**
 *
 * @author MaryJane
 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class PrintJob implements Billable {

 
    private String serviceType;
    private String pageSize;
    private String paperType;
    private int pageCount;
    private boolean isColored;
    private double pricePerPage;
    private final List<Object> printJobs;

    //public static List<String> AVAILABLE_PAPER_SIZES;
    //public static List<String> AVAILABLE_PAPER_TYPES;

    //CONSTRUCTORS
    public PrintJob(String serviceType, String pageSize, String paperType, int pageCount, boolean isColored, double pricePerPage) {
        this.printJobs = new ArrayList<>();
        setServiceType(serviceType);
        setPageSize(pageSize);
        setPaperType(paperType);
        setPageCount(pageCount);
        setColored(isColored);
        setPricePerPage(pricePerPage);
    }


    //GETTERS
 
    public String getServiceType() {
        return serviceType;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getPaperType() {
        return paperType;
    }

    public int getPageCount() {
        return pageCount;
    }

    public boolean getIsColored() {
        return isColored;
    }

    public double getPricePerPage() {
        return pricePerPage;
    }
    
    public List<Object> getPrintJobs() {
        return Collections.unmodifiableList(printJobs);
    }

    //SETTERS
  
    public void setServiceType(String serviceType) {
        if (serviceType == null || serviceType.isBlank()) {
            throw new IllegalArgumentException("Service type is required.");
        }
        this.serviceType = serviceType.trim();
    }

    public void setPageSize(String pageSize) {
        if (pageSize == null || pageSize.isBlank()) {
            throw new IllegalArgumentException("Page size is required.");
        }
        this.pageSize = pageSize.trim();
    }

    public void setPaperType(String paperType) {
        if (paperType == null || paperType.isBlank()) {
            throw new IllegalArgumentException("Paper type is required.");
        }
        this.paperType = paperType.trim();
    }

    public void setPageCount(int pageCount) {
        if (pageCount <= 0) {
            throw new IllegalArgumentException("Page count must be greater than zero.");
        }
        this.pageCount = pageCount;
    }

    public void setColored(boolean colored) {
        isColored = colored;
    }

    public void setPricePerPage(double pricePerPage) {
        if (!Double.isFinite(pricePerPage) || pricePerPage < 0) {
            throw new IllegalArgumentException("Price per page cannot be negative.");
        }
        this.pricePerPage = pricePerPage;
    }
    
    public void addPrintJob(PrintJob toAdd) {
        if (toAdd == null) {
            throw new IllegalArgumentException("Print job cannot be null.");
        }
        printJobs.add(toAdd);
    }

    @Override
    public double calculateTotalCharge() {
        double total = pageCount * pricePerPage;
        for (Object job : printJobs) {
            total += ((PrintJob) job).calculateTotalCharge();
        }
        return total;
    }

    @Override
    public String generateInvoice() {
        return "===== PRINT INVOICE =====\n"
                + "Service: " + serviceType + "\n"
                + "Page size: " + pageSize + "\n"
                + "Paper type: " + paperType + "\n"
                + "Pages: " + pageCount + "\n"
                + "Colored: " + (isColored ? "Yes" : "No") + "\n"
                + String.format("Price per page: %.2f%n", pricePerPage)
                + String.format("Total charge: %.2f%n", calculateTotalCharge())
                + "========================";
    }

    //OTHER METHODS


} //END OF CLASS
