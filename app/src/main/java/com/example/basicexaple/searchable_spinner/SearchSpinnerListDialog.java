package com.example.basicexaple.searchable_spinner;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.basicexaple.R;

import java.io.Serializable;
import java.util.List;

public class SearchSpinnerListDialog extends DialogFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final String ITEMS = "items";

    private ArrayAdapter listAdapter;

    private ListView listViewItems;

    private SearchableItem searchableItem;

    private OnSearchTextChanged onSearchTextChanged;

    private SearchView searchView;

    private String strTitle;

    private String strPositiveButtonText;

    private DialogInterface.OnClickListener onClickListener;

    public SearchSpinnerListDialog() {

    }

    public static SearchSpinnerListDialog newInstance(List items) {
        SearchSpinnerListDialog multiSelectExpandableFragment = new SearchSpinnerListDialog();
        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);
        multiSelectExpandableFragment.setArguments(args);
        return multiSelectExpandableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (null != savedInstanceState) {
            searchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
        }
        View rootView = inflater.inflate(R.layout.search_list_dialog, null);
        setData(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(rootView);

        String strPositiveButton = strPositiveButtonText == null ? "Ok" : strPositiveButtonText;
        alertDialog.setPositiveButton(strPositiveButton, onClickListener);

        String strTitle = this.strTitle == null ? "Select Language" : this.strTitle;
        alertDialog.setTitle(strTitle);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", searchableItem);
        super.onSaveInstanceState(outState);
    }

    public void setTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public void setPositiveButton(String strPositiveButtonText) {
        this.strPositiveButtonText = strPositiveButtonText;
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        this.strPositiveButtonText = strPositiveButtonText;
        this.onClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem searchableItem) {
        this.searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;
    }

    private void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) rootView.findViewById(R.id.search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        List items = (List) getArguments().getSerializable(ITEMS);
        listViewItems = (ListView) rootView.findViewById(R.id.listItems);
        listAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);        //attach the adapter to the list
        listViewItems.setAdapter(listAdapter);
        listViewItems.setTextFilterEnabled(true);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchableItem.onSearchableItemClicked(listAdapter.getItem(position), position);
                getDialog().dismiss();
            }
        });
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            ((ArrayAdapter) listViewItems.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter) listViewItems.getAdapter()).getFilter().filter(s);
        }
        if (null != onSearchTextChanged) {
            onSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchableItemClicked(T item, int position);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }
}
