package in.trueowner.trueowner;

public class Marketplace_Get_Set {

    private String Category,Description,Name,ProductID,OwnerName, State, City;
    private long Price,Condition;
    private String Image1;

    public Marketplace_Get_Set() {
    }

    public Marketplace_Get_Set(String Category, String Description, String Name, String ProductID, String OwnerName, long Price, long Condition, String Image1, String State, String City) {
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
}
