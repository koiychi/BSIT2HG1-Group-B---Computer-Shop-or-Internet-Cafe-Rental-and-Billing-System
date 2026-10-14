package bsit2hg1groupb.billingSystem;

/**
 *
 * @author MaryJane and Koi
 */
public class PrintJob implements Billable {

    private int printJobID;
    private RegularStation regularStation;
    private Customer customer;
    private String serviceType;
    private String pageSize;
    private String paperType;
    private int pageCount;
    private boolean isColored;
    private double pricePerPage;

    //public static List<String> AVAILABLE_PAPER_SIZES;
    //public static List<String> AVAILABLE_PAPER_TYPES;

    //CONSTRUCTORS
    public PrintJob(int printJobID, RegularStation regularStation, Customer customer, String serviceType, String pageSize, String paperType, int pageCount, boolean isColored) {
        this.printJobID = printJobID;
        this.regularStation = regularStation;
        this.customer = customer;
        this.serviceType = serviceType;
        this.pageSize = pageSize;
        this.paperType = paperType;
        this.pageCount = pageCount;
        this.isColored = isColored;
    }


    //GETTERS
    public int getPrintJobID() {
        return printJobID;
    }

    public RegularStation getRegularStation() {
        return regularStation;
    }

    public Customer getCustomer() {
        return customer;
    }

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

    //SETTERS
    public void setPrintJobID(int printJobID) {
        this.printJobID = printJobID;
    }

    public void setRegularStation(RegularStation regularStation) {
        this.regularStation = regularStation;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

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
