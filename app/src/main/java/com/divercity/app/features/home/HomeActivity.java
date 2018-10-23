package com.divercity.app.features.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.divercity.app.R;
import com.divercity.app.databinding.ActivityHomeBinding;
import com.divercity.app.features.home.groups.GroupsFragment;

import javax.inject.Inject;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class HomeActivity extends AppCompatActivity implements HasSupportFragmentInjector, GroupsFragment.OnFragmentInteractionListener {

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;

    ActivityHomeBinding binding;
    NavController navController;

    public static Intent getCallingIntent(Context context){
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        navController = Navigation.findNavController(HomeActivity.this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        Typeface typeface = ResourcesCompat.getFont(this, R.font.avenir_medium);
        binding.bottomNavigationView.setTypeface(typeface);
        binding.bottomNavigationView.enableShiftingMode(false);
        binding.bottomNavigationView.setItemIconTintList(null);

//        Glide
//                .with(this)
//                .load("https://apinew.pincapp.com/images/default_avatar.png")
//                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        StateListDrawable states = new StateListDrawable();
//                        states.addState(new int[]{android.R.attr.state_pressed}, resource);
//                        states.addState(new int[]{android.R.attr.state_checked}, resource);
//                        states.addState(new int[]{}, resource);
//                        Menu menu = binding.bottomNavigationView.getMenu();
//                        MenuItem te = menu.findItem(R.id.notificationsFragment);
//                        te.setIcon(states);
//                        binding.imgTest.setImageDrawable(resource);
//                    }
//                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_toolbar, menu);
//
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setIconifiedByDefault(false);
//        searchView.setQueryHint(getString(R.string.search));
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showBottomNavigationView() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationView() {
        binding.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

}
