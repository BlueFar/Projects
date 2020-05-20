package in.trueowner.trueowner;

public class AdminPickupSeller_List_Get_Set {

    private String  SellerName, Imei1, productID;

    public AdminPickupSeller_List_Get_Set() {
    }

    public AdminPickupSeller_List_Get_Set(String SellererName, String Imei1, String productID) {
        this.SellerName = SellerName;
        this.Imei1 = Imei1;
        this.productID = productID;

    }

    public String getSellerName() { return SellerName; }

    public String getImei1() { return Imei1; }

    public String getProductID() { return productID; }
}