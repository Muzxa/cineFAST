package com.example.cinefast;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SnackAdapter extends BaseAdapter {

    Context context;
    ArrayList<Snack> snacks;
    private final int QUANTITY_LIMIT;

    public SnackAdapter(Context context, ArrayList<Snack> snacks, int QUANTITY_LIMIT)
    {
        this.context = context;
        this.snacks = snacks;
        this.QUANTITY_LIMIT = QUANTITY_LIMIT;
    }

    @Override
    public int getCount() {
        return snacks.size();
    }

    @Override
    public Object getItem(int position) {
        return snacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.snack_card_view, null);

            EditText etSnackQuantity = convertView.findViewById(R.id.et_snack_quantity);
            TextView tvSnackTitle = convertView.findViewById(R.id.tv_snack_title);
            TextView tvSnackDescription  = convertView.findViewById(R.id.tv_snack_description);
            Button btnAddSnack = convertView.findViewById(R.id.btn_add_snack);
            Button btnRemoveSnack = convertView.findViewById(R.id.btn_remove_snack);
            ImageView ivSnackImage = convertView.findViewById(R.id.iv_snack_image);

            Snack snack = snacks.get(position);

            tvSnackTitle.setText(snack.getName());
            tvSnackDescription.setText(snack.getDescription());
            ivSnackImage.setImageResource(snack.getImageId());
            etSnackQuantity.setText(String.valueOf(snack.getQuantity()));

            btnAddSnack.setOnClickListener(v -> {

                int currentQuantity = snack.getQuantity();

                if(currentQuantity < QUANTITY_LIMIT)
                {
                    currentQuantity += 1;
                    snack.setQuantity(currentQuantity);
                    etSnackQuantity.setText(String.valueOf(currentQuantity));
                }
            });

            btnRemoveSnack.setOnClickListener(v -> {

                int currentQuantity = snack.getQuantity();

                if(currentQuantity > 0)
                {
                    currentQuantity -= 1;
                    snack.setQuantity(currentQuantity);
                    etSnackQuantity.setText(String.valueOf(snack.getQuantity()));
                }
            });

            etSnackQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()) {

                        int value = Integer.parseInt(s.toString());

                        if (value > QUANTITY_LIMIT) {
                            value = QUANTITY_LIMIT;
                            etSnackQuantity.setText(String.valueOf(value));
                            etSnackQuantity.setSelection(String.valueOf(value).length());
                        }

                        snack.setQuantity(value);
                    }
                }

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}
            });

        return convertView;
    }
}
