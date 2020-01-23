package in.trueowner.trueowner;

public class Cart_Get_Set {

    private String Category,Description,Name,ProductID,OwnerName, State, City,Status;
    private long Price,Condition, MyOffer, AskedPrice;
    private String Image1;

    public Cart_Get_Set() {
    }

    public Cart_Get_Set(String Category, String Description, String Name, String ProductID, String OwnerName, long Price, long Condition,
                        String Image1, String State, String City, String Status, long MyOffer, long AskedPrice) {
        this.Category = Category;
        this.Description = Description;
        this.Name = Name;
        this.ProductID = ProductID;
        this.OwnerName = OwnerName;
         this.Price = Price;
        this.Condition = Condition;
        this.Image1 = Image1;
        this.State = State;
        this.City = City;
        this.Status = Status;
        this.MyOffer = MyOffer;
        this.AskedPrice = AskedPrice;
    }

     public long getPrice() {
        return Price;
    }

    public String getCategory() {
        return Category;
    }

    public String getDescription() {
        return Description;
    }

    public String getName() {
        return Name;
    }

    public long getCondition() { return Condition;  }

    public String getProductID() { return ProductID; }

    public String getOwnerName() { return OwnerName; }

    public String getImage1() { return Image1; }

    public String getState() {  return State; }

    public String getCity() {   return City;    }

    public String getStatus() { return Status; }

    public long getMyOffer() {  return MyOffer; }

    public long getAskedPrice() {return AskedPrice; }
}
