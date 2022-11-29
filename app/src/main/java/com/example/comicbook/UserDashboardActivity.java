package com.example.comicbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comicbook.models.ModelCategorys;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserDashboardActivity extends AppCompatActivity {

    //to show in tabs
    public ArrayList<ModelCategorys> categorysArrayList;
    public ViewPagerAdapter viewPagerAdapter;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    TextView login_user;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        //getting data
        login_user=(TextView)findViewById(R.id.login_user);
        logout=(ImageView)findViewById(R.id.logout_user);

        //btn click handle
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });


        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
    }

    private void setupViewPagerAdapter(ViewPager viewPager){


    }

    public  class ViewPagerAdapter extends FragmentPagerAdapter{


        private ArrayList<BookUserFragment> fragmentList= new ArrayList<>();
        private ArrayList<String> fragementTitleList=new ArrayList<>();
        private Context context;
        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            this.context=context;
        }


        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private void addFragment(BookUserFragment fragment,String title ){
            //add fragement passed as parameter in fragmentList
            fragmentList.add(fragment);

            //add fragement passed as parameter in fragmenTitletList
            fragementTitleList.add(title);

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return fragementTitleList.get(position);
        }
    }

    private void checkUser() {

        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser==null){
            //goto main screen
            startActivity(new Intent(UserDashboardActivity.this,WelcomeActivity.class));
            finish();
        }
        else{
            //logged in, got get user info
            String email = firebaseUser.getEmail();

            //set in textview of toolbar
            login_user.setText(email);

        }
    }
}