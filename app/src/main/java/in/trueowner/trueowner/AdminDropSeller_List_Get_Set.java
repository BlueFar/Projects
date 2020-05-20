package in.trueowner.trueowner;

public class AdminDropSeller_List_Get_Set {

    private String  SellerName, Imei1, productID;

    public AdminDropSeller_List_Get_Set() {
    }

    public AdminDropSeller_List_Get_Set(String SellererName, String Imei1, String productID) {
        this.SellerName = SellerName;
        this.Imei1 = Imei1;
        this.productID = productID;

    }

    public String getSellerName() { return SellerName; }

    public String getImei1() { return Imei1; }

    public String getProductID() { return productID; }
}