package fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belalmohamed.modesoat.MainActivity;
import com.example.belalmohamed.modesoat.R;
import com.example.belalmohamed.modesoat.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {

    private Button logoutBtn;
    private ListView usersList;
    private TextView currentUserTxt;
    private UsersListAdapter adapter;
    private static  final String TAG = WelcomeFragment.class.getSimpleName();
    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        usersList = (ListView) rootView.findViewById(R.id.users_list);
        currentUserTxt = (TextView) rootView.findViewById(R.id.user_txt);
        logoutBtn = (Button) rootView.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if(mainActivity != null) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        Log.i(TAG,"Starting logout, unsubscribing push ....");
                        String channel = (String) currentUser.get(SignupFragment.USER_CHANNEL_KEY);
                        if(channel != null)
                             Utils.unSubscribeParseChannel(channel,null);
                        ParseUser.logOut();
                    }
                    Log.i(TAG,"Starting logout, Navigating to Login Screen...");
                    mainActivity.loadFragment(Utils.Constants.LOGIN_FRAGMENT);
                }
            }
        });
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUserTxt.setText("User: "+currentUser.getUsername());
        }
        else
            currentUserTxt.setText("");
        adapter = new UsersListAdapter(getActivity());
        usersList.setAdapter(adapter);
        fetchUsers();
        return  rootView;
    }

    private void fetchUsers()
    {
        final ProgressDialog pd = ProgressDialog.show(this.getActivity(),"","Loading...",true,false);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    adapter.addAll(objects);
                    adapter.notifyDataSetChanged();
                    pd.setMessage("Done !");
                } else {
                    // Something went wrong.
                    pd.setMessage("Error !");
                    Toast.makeText(WelcomeFragment.this.getActivity(), "Fetching Users Failed !! ," + (e.getMessage()), Toast.LENGTH_LONG).show();
                }

                if(pd != null)
                    pd.dismiss();
            }
        });
    }


    public static  class  UsersListAdapter extends ArrayAdapter<ParseUser>
    {
        public UsersListAdapter(Context context)
        {
            this(context,0,new ArrayList<ParseUser>());
        }

        public UsersListAdapter(Context context, int resource, List<ParseUser> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ParseUser user = getItem(position );
            View view = convertView;
            if(view == null)
            {
                view = LayoutInflater.from(getContext()).inflate(R.layout.user_layout,null);
            }
            TextView userNameTxt = (TextView) view.findViewById(R.id.user_name);
            TextView userRfidTxt = (TextView) view.findViewById(R.id.user_rfid);
            userNameTxt.setText(user.getUsername());
            userRfidTxt.setText("RFID:"+user.get(SignupFragment.USER_RFID_KEY));
            return view;//
        }
    }


}
