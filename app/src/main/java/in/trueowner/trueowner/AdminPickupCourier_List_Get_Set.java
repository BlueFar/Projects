package in.trueowner.trueowner;

public class AdminPickupCourier_List_Get_Set {

    private String  ProductName, Imei1, ProductID;

    public AdminPickupCourier_List_Get_Set() {
    }

    public AdminPickupCourier_List_Get_Set(String ProductName, String Imei1, String ProductID) {
        this.ProductName = ProductName;
        this.Imei1 = Imei1;
        this.ProductID = ProductID;

    }

    public String getProductName() { return ProductName; }

    public String getImei1() { return Imei1; }

    public String getProductID() { return ProductID; }
}