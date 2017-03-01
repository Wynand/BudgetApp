package comp3350.budgetapp.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;

import java.util.ArrayList;

import comp3350.budgetapp.R;
import comp3350.budgetapp.objects.WishListItem;
import comp3350.budgetapp.business.AccessWishListItems;

public class WishlistActivity extends Activity {

    private AccessWishListItems accessWishListItems;
    private ArrayList<WishListItem> itemList;
    private ArrayAdapter<WishListItem> itemArrayAdapter;
    private int selectedItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        accessWishListItems = new AccessWishListItems();

        itemList = new ArrayList<WishListItem>();
        String result = accessWishListItems.getWishListItems(itemList);
        if (result != null)
        {
            Messages.fatalError(this, result);
        }
        else
        {
            itemArrayAdapter = new ArrayAdapter<WishListItem>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, itemList)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(itemList.get(position).getItemName());
                    text2.setText(String.format("%.2f", itemList.get(position).getPrice()));

                    return view;
                }
            };

            final ListView listView = (ListView)findViewById(R.id.listWishes);
            listView.setAdapter(itemArrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Button updateButton = (Button)findViewById(R.id.buttonItemUpdate);
                    Button deleteButton = (Button)findViewById(R.id.buttonItemDelete);

                    if (position == selectedItemPosition) {
                        listView.setItemChecked(position, false);
                        updateButton.setEnabled(false);
                        deleteButton.setEnabled(false);
                        selectedItemPosition = -1;
                    } else {
                        listView.setItemChecked(position, true);
                        updateButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        selectedItemPosition = position;
                        selectItemAtPosition(position);
                    }
                }
            });

        }
    }

    public void selectItemAtPosition(int position)
    {
        WishListItem selected = itemArrayAdapter.getItem(position);

        EditText editName = (EditText)findViewById(R.id.editItemName);
        EditText editPrice = (EditText)findViewById(R.id.editPrice);

        editName.setText(selected.getItemName());
        editPrice.setText(String.valueOf(selected.getPrice()));
    }

    public void buttonItemAddOnClick(View v)
    {
        WishListItem item = createItemFromEditText();
        String result;

        result = validateItemData(item, true);
        if (result == null)
        {
            result = accessWishListItems.addWishListItem(item);
            if (result == null)
            {
                accessWishListItems.getWishListItems(itemList);
                itemArrayAdapter.notifyDataSetChanged();
                int pos = itemList.indexOf(item);
                if (pos >= 0)
                {
                    ListView listView = (ListView) findViewById(R.id.listWishes);
                    listView.setSelection(pos);
                }
            }
            else
            {
                Messages.fatalError(this, result);
            }
        }
        else
        {
            Messages.warning(this, result);
        }

    }

    public void buttonItemDeleteOnClick(View v)
    {
        WishListItem item = createItemFromEditText();
        String result;

        result = accessWishListItems.deleteWishListItem(item);

        if(result == null)
        {
            int pos = itemList.indexOf(item);
            if(pos >=0)
            {
                ListView listView = (ListView) findViewById(R.id.listWishes);
                listView.setSelection(pos);
            }
            accessWishListItems.getWishListItems(itemList);
            itemArrayAdapter.notifyDataSetChanged();
        }
        else
        {
            Messages.warning(this, result);
        }
    }

    public void buttonItemUpdateOnClick(View v)
    {
        WishListItem item = createItemFromEditText();
        String result;

        result = validateItemData(item, false);
        if(result == null)
        {
            result = accessWishListItems.updateWishListItem(item);
            if(result == null)
            {
                accessWishListItems.getWishListItems(itemList);
                itemArrayAdapter.notifyDataSetChanged();
                int pos = itemList.indexOf(item);
                if(pos >= 0)
                {
                    ListView listView = (ListView) findViewById(R.id.listWishes);
                    listView.setSelection(pos);
                }
            }
            else
            {
                Messages.fatalError(this, result);
            }
        }
        else
        {
            Messages.fatalError(this, result);
        }
    }

    private WishListItem createItemFromEditText()
    {
        EditText editItemName = (EditText) findViewById(R.id.editItemName);
        EditText editPrice = (EditText) findViewById(R.id.editPrice);

        WishListItem item = new WishListItem(editItemName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));

        return item;
    }

    private String validateItemData(WishListItem item, boolean isNewItem)
    {
        if (item.getItemName().length() == 0)
        {
            return "Item Name required!";
        }

        if (isNewItem && accessWishListItems.getRandom(item.getItemName()) != null)
        {
            return "Item " + item.getItemName() + " already exists";
        }

        return null;
    }
}


