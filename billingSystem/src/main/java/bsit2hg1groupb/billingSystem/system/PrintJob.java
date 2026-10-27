package bsit2hg1groupb.billingSystem.system;

/**
 *
 * @author MaryJane and Koi
 
 */
import java.util.ArrayList;
import java.util.List;
public class PrintJob implements Billable {

 
    private String serviceType;
    private String pageSize;
    private String paperType;
    private int pageCount;
    private boolean isColored;
    private double pricePerPage;
    private List<Object> printJobs = new ArrayList<>();

    //public static List<String> AVAILABLE_PAPER_SIZES;
    //public static List<String> AVAILABLE_PAPER_TYPES;

    //CONSTRUCTORS
    public PrintJob(String serviceType, String pageSize, String paperType, int pageCount, boolean isColored, double pricePerPage1) {
       
        this.serviceType = serviceType;
        this.pageSize = pageSize;
        this.paperType = paperType;
        this.pageCount = pageCount;
        this.isColored = isColored;
        this.pricePerPage = pricePerPage;
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
        return printJobs;
    }

    //SETTERS
  
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setPageSize(int PageSize) {
        this.pageSize = pageSize;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setColored(boolean colored) {
        isColored = colored;
    }

    public void setPricePerPage(double pricePerPage) {
        this.pricePerPage = pricePerPage;
    }
    
    public void addPrintJob(PrintJob toAdd) {
        printJobs.add(toAdd);
    }

    @Override
    public double calculateTotalCharge() {
        return 0;
    }

    @Override
    public String generateInvoice() {
        return "";
    }

    //OTHER METHODS


} //END OF CLASS
