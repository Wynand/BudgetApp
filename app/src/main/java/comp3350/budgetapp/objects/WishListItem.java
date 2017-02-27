package comp3350.budgetapp.objects;

public class WishListItem
{
    private String itemName = "";
    private int priority = 0;
    private int price = 0;

    public WishListItem(String itemName , int price)
    {
        this.itemName = itemName;
        this.price = price;
        this.priority = 0;
    }

    public WishListItem(String itemName , int price ,int priority)
    {
        this.itemName = itemName;
        this.price = price;
        this.priority = priority;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String toString()
    {
        return "Item Name: " +itemName +"With Priority " +priority;
    }

    public boolean equals(Object object)
    {
        boolean result;
        WishListItem item;

        result = false;

        if (object instanceof WishListItem)
        {
            item = (WishListItem) object;
            if (((item.itemName == null) && (itemName == null)) || (item.itemName.equals(itemName)))
            {
                result = true;
            }
        }
        return result;
    }
}