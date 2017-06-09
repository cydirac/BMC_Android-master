package com.liuyufei.bmc_android.admin;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.liuyufei.bmc_android.R;
import com.liuyufei.bmc_android.data.BMCContract;
import com.liuyufei.bmc_android.model.Staff;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StaffFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private OnListFragmentInteractionListener mListener;

    ContentResolver contentResolver;
    private static final int URL_LOADER = 0;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StaffFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StaffFragment newInstance(int columnCount) {
        StaffFragment fragment = new StaffFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }


    Cursor cursor;
    StaffCursorAdapter adapter;
    ListView lv;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_list, container, false);
        contentResolver = view.getContext().getContentResolver();
        lv = (ListView) view;
        adapter = new StaffCursorAdapter(getContext(), cursor, false);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //move the cursor to the selected row
                cursor = (Cursor) adapterView.getItemAtPosition(pos);
                //get the object data from the cursor
                int staffID = cursor.getInt(cursor.getColumnIndex(BMCContract.StaffEntry._ID));
                String staffMobile = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_MOBILE));
                String staffName = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_NAME));
                String staffPhoto = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_PHOTO));
                String staffDepartment = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_DEPARTMENT));
                String staffTitle = cursor.getString(cursor.getColumnIndex(BMCContract.StaffEntry.COLUMN_TITLE));
                //create the object that will be passed to the todoActivity
                Staff staff = new Staff(staffID, staffName, staffPhoto, staffDepartment, staffTitle, staffMobile);
                Intent intent = new Intent(getActivity(), EditStaffActivity.class);
                //pass the ID to the todoActivity
                intent.putExtra("staff", staff);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //change selection
        Log.i("onCreateLoader", "onCreateLoader selection changed");
        Uri resourceUri = BMCContract.StaffEntry.CONTENT_URI;
        String[] selectionArgs = null;
        String selection = "";
        String orderBy = null;

        if (args != null) {
            selectionArgs = args.getStringArray("selectionArgs");
            selection = args.getString("selection");
            String table = args.getString("table");

            switch (table) {
                case BMCContract.StaffEntry.TABLE_NAME:
                    resourceUri = BMCContract.StaffEntry.CONTENT_URI;
                    orderBy = BMCContract.StaffEntry.COLUMN_NAME;
                    break;
                case BMCContract.VisitorEntry.TABLE_NAME:
                    resourceUri = BMCContract.VisitorEntry.CONTENT_URI;
                    break;
                case BMCContract.AppointmentEntry.TABLE_NAME:
                    resourceUri = BMCContract.AppointmentEntry.CONTENT_URI;
                    break;
            }
        }

        Loader<Cursor> lc = new CursorLoader(getActivity(), resourceUri, null, selection, selectionArgs, orderBy);
        return lc;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    Handler handler = new Handler();
    boolean canRun = true;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText.length() >=1) {
                    if (canRun) {
                        canRun = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                canRun = true;
                                String selection = BMCContract.StaffEntry.COLUMN_NAME + " like ?";
                                String[] selectionArgs = {"%"+newText+"%"};
                                Bundle bundle = new Bundle();
                                bundle.putString("selection", selection);
                                bundle.putStringArray("selectionArgs", selectionArgs);
                                bundle.putString("table", BMCContract.StaffEntry.TABLE_NAME);
                                getLoaderManager().restartLoader(URL_LOADER, bundle, StaffFragment.this);
                            }
                        }, 2000);
                    }
                }else{
                    //select all records
                    getLoaderManager().restartLoader(URL_LOADER, null, StaffFragment.this);
                }
                return false;
            }
        });
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}
