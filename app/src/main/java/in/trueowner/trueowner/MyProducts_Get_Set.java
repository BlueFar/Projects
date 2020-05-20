package in.trueowner.trueowner;

public class MyProducts_Get_Set {

    private String ProductName,ProductID, UserID;
    private long PurchasedPrice;
    private Boolean SaleStatus, VerificationStatus;

    public MyProducts_Get_Set() {
    }

    public MyProducts_Get_Set( String ProductName, String ProductID, long PurchasedPrice, Boolean SaleStatus, Boolean VerificationStatus, String UserID) {

        this.ProductName = ProductName;
        this.ProductID = ProductID;
         this.PurchasedPrice = PurchasedPrice;
        this.SaleStatus = SaleStatus;
        this.VerificationStatus = VerificationStatus;
        this.UserID = UserID;

    }

     public long getPurchasedPrice() {
        return PurchasedPrice;
    }

    public String getName() {
        return ProductName;
    }

    public String getProductID() { return ProductID; }

    public Boolean getSale() {   return SaleStatus; }

    public Boolean getVerificationStatus() {  return VerificationStatus; }

    public String getUserID() { return UserID; }
}
