package com.example.basicexaple.searchable_spinner;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.basicexaple.R;

import java.util.ArrayList;
import java.util.List;


public class SearchSpinner extends android.support.v7.widget.AppCompatSpinner implements View.OnTouchListener,
        SearchSpinnerListDialog.SearchableItem {

    String selectedItem;
    public static final int NO_ITEM_SELECTED = -1;
    private Context context;
    private List items;
    private SearchSpinnerListDialog searchableListDialog;
    private boolean isDirty;
    private ArrayAdapter arrayAdapter;
    private String _strHintText;
    private boolean isFromInit;

    public SearchSpinner(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SearchSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SearchSpinner_hintText) {
                _strHintText = a.getString(attr);
            }
        }
        a.recycle();
        init();
    }

    public SearchSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        items = new ArrayList();
        searchableListDialog = SearchSpinnerListDialog.newInstance
                (items);
        searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);

        arrayAdapter = (ArrayAdapter) getAdapter();
        if (!TextUtils.isEmpty(_strHintText)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout
                    .simple_list_item_1, new String[]{_strHintText});
            isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (null != arrayAdapter) {
                items.clear();
                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    items.add(arrayAdapter.getItem(i));
                }
                searchableListDialog.show(scanForActivity(context).getFragmentManager(), "TAG");
            }
        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (!isFromInit) {
            arrayAdapter = (ArrayAdapter) adapter;
            if (!TextUtils.isEmpty(_strHintText) && !isDirty) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, new String[]{_strHintText});
                super.setAdapter(arrayAdapter);
            } else {
                super.setAdapter(adapter);
            }
        } else {
            isFromInit = false;
            super.setAdapter(adapter);
        }
    }

    @Override
    public void onSearchableItemClicked(Object item, int position) {
        setSelection(items.indexOf(item));

        if (!isDirty) {
            isDirty = true;
            setAdapter(arrayAdapter);
            setSelection(items.indexOf(item));
        }
        selectedItem = getItemAtPosition(position).toString();
        Toast.makeText(getContext(), "You selected " + selectedItem, Toast.LENGTH_LONG).show();
    }


    private Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(_strHintText) && !isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(_strHintText) && !isDirty) {
            return null;
        } else {

            return super.getSelectedItem();
        }
    }
}
