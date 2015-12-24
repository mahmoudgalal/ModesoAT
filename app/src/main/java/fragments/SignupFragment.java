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
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private EditText userTxt,emailTxt,passwordTxt,jobPositionTxt,rfidTxt;
    private Button signupBtn;
    public static  final String REG_DATE = "RegistrationDate";
    public static  final String USER_RFID_KEY = "RfidTag";
    public static  final String USER_POSITION_KEY = "Position";
    public static  final String USER_CHANNEL_KEY = "Channel";

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_signup, container, false);
        userTxt= (EditText) root.findViewById(R.id.user_txt);
        emailTxt= (EditText) root.findViewById(R.id.email_txt);
        passwordTxt= (EditText) root.findViewById(R.id.pass_txt);
        jobPositionTxt = (EditText) root.findViewById(R.id.jobpos_txt);
        rfidTxt = (EditText) root.findViewById(R.id.rfid_txt);
        signupBtn = (Button) root.findViewById(R.id.signup_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        return  root;
    }
    private void signup()
    {

        final String userName = userTxt.getText().toString().trim();
        final String userEmail = emailTxt.getText().toString().trim();
        final String userPassword = passwordTxt.getText().toString().trim();
        final String userPosition = jobPositionTxt.getText().toString().trim();
        final String userRfid = rfidTxt.getText().toString().trim();
        if(userName.length()<6 || userEmail.length() <8 || userPassword.length()<4 || userRfid.length()<4)
        {
            Toast.makeText(SignupFragment.this.getActivity(),"Enter Valid Data !!",Toast.LENGTH_LONG).show();
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(userPassword);
        user.setEmail(userEmail);

    // other fields can be set just like with ParseObject
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        String date = sdf.format(new Date());
        user.put(REG_DATE, date);
        user.put(USER_POSITION_KEY,userPosition);
        user.put(USER_RFID_KEY,userRfid);
        final String userChannel = userName;//+userRfid;
        user.put(USER_CHANNEL_KEY,userChannel);

        final ProgressDialog pd = ProgressDialog.show(this.getActivity(), "", "Processing", true, false);
        final SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.

                    if(sp != null)
                    {
                        sp.edit().putBoolean(Utils.Constants.SIGNEDUP_KEY,true).apply();
                    }

                    //Toast.makeText(SignupFragment.this.getActivity(),"Done !!",Toast.LENGTH_LONG).show();
                    pd.setMessage("Signup Done !!");
                    pd.setMessage("Login Now !!! ");
                    Utils.parseUserLogin(userName,userPassword,new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // Hooray! The user is logged in.
                                Toast.makeText(SignupFragment.this.getActivity(), "Login Done !!", Toast.LENGTH_LONG).show();
                                MainActivity mainActivity = (MainActivity) getActivity();
                                if(mainActivity != null)
                                    mainActivity.loadFragment(Utils.Constants.START_FRAGMENT);
                                if(sp != null)
                                {
                                    sp.edit().putLong(Utils.Constants.LAST_LOGIN_TIME, new Date().getTime()).apply();
                                }
                                pd.setMessage("Subscribing now !!! ");
                                Utils.subscribeParseChannel(userChannel, new SaveCallback() {
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
                                Toast.makeText(SignupFragment.this.getActivity(), "Login Failed !!", Toast.LENGTH_LONG).show();
                                if(pd != null)
                                    pd.dismiss();
                            }

                        }
                    });

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    if(pd != null)
                        pd.dismiss();
                    Toast.makeText(SignupFragment.this.getActivity(),e!= null?e.getMessage():"Error !!",Toast.LENGTH_LONG).show();
                }

            }
        });

    }



}
