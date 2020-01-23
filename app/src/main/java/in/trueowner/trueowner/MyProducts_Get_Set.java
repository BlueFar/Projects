package in.trueowner.trueowner;

public class MyProducts_Get_Set {

    private String Name,ProductID;
    private long PurchasedPrice;
    private Boolean Sale, VerificationStatus;

    public MyProducts_Get_Set() {
    }

    public MyProducts_Get_Set( String Name, String ProductID, long PurchasedPrice, Boolean Sale, Boolean VerificationStatus) {

        this.Name = Name;
        this.ProductID = ProductID;
         this.PurchasedPrice = PurchasedPrice;
        this.Sale = Sale;
        this.VerificationStatus = VerificationStatus;
    }

     public long getPurchasedPrice() {
        return PurchasedPrice;
    }

    public String getName() {
        return Name;
    }

    public String getProductID() { return ProductID; }

    public Boolean getSale() {   return Sale; }

    public Boolean getVerificationStatus() {  return VerificationStatus; }
}
