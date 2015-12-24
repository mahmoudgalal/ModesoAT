package fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.belalmohamed.modesoat.MainActivity;
import com.example.belalmohamed.modesoat.R;
import com.example.belalmohamed.modesoat.Utils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText userTxt,passwordTxt;
    private Button signupBtn;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =   inflater.inflate(R.layout.fragment_login, container, false);
        userTxt= (EditText) root.findViewById(R.id.user_txt);
        passwordTxt= (EditText) root.findViewById(R.id.pass_txt);
        signupBtn = (Button) root.findViewById(R.id.login_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return  root;
    }

    private void login()
    {

        final String userName = userTxt.getText().toString().trim();
        String userPass = passwordTxt.getText().toString().trim();
        if(userName.length()<6 || userPass.length()<4)
        {
            Toast.makeText(LoginFragment.this.getActivity(),"Enter Valid Data !!",Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog pd = ProgressDialog.show(this.getActivity(),"","Processing",true,false);
        final SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        ParseUser.logInInBackground(userName, userPass, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    if(sp != null)
                    {
                        sp.edit().putLong(Utils.Constants.LAST_LOGIN_TIME, new Date().getTime()).apply();
                    }
                    Toast.makeText(LoginFragment.this.getActivity(), "Done !!", Toast.LENGTH_LONG).show();
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if(mainActivity != null)
                        mainActivity.loadFragment(Utils.Constants.START_FRAGMENT);
                    pd.setMessage("Subscribing now !!! ");

                    Utils.subscribeParseChannel(userName, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");

                            } else {
                                Log.e("com.parse.push", "failed to subscribe for push", e);
                            }
                            if(pd != null)
                                pd.dismiss();
                        }
                    });
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    if(sp != null)
                    {
                        sp.edit().putLong(Utils.Constants.LAST_LOGIN_TIME, -1).apply();
                    }
                    Toast.makeText(LoginFragment.this.getActivity(), "Login Failed !! ,"+(e.getMessage()), Toast.LENGTH_LONG).show();
                    if(pd != null)
                        pd.dismiss();
                }

            }
        });
    }


}
