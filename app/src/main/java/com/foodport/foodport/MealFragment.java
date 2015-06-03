package com.foodport.foodport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.foodport.foodport.model.Meal;
import com.foodport.foodport.service.CartService;
import com.foodport.foodport.service.MealService;
import com.foodport.foodport.utils.MealUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RestAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by amangupta on 01/06/15.
 */

public class MealFragment extends Fragment {

    private static String MEALS = "meals";
    private static String APPNAME ="foodport";





    private List<Meal> mMealList = new ArrayList<Meal>();
    public MealFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        //getActivity().setContentView(R.layout.fragment_main);


        final ListView listView = (ListView)getActivity().findViewById(R.id.meal_list);

        FloatingActionButton checkoutButton = (FloatingActionButton)getActivity().findViewById(R.id.checkout_button);
        checkoutButton.setSize(FloatingActionButton.SIZE_NORMAL);
        checkoutButton.setIcon(R.drawable.ic_shopping_cart_white_48dp);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Checkout Btn Clicked", "Checkout Btn Clicked");
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences settings = getActivity().getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

        if(settings.contains(MEALS)){
            Log.v("Meal Fragment", "Picking Up meals from Shared Prefs");
            String jsonMeals = settings.getString(MEALS, null);
            Gson gson = new Gson();
            Meal[] mealArray = gson.fromJson(jsonMeals, Meal[].class);
            mMealList = Arrays.asList(mealArray);
            final MealAdapter mealAdapter = new MealAdapter(getActivity(), mMealList);
            listView.setAdapter(mealAdapter);


        } else {
            callApi();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        CartService.getInstance(getActivity()).saveState();
    }

    private void callApi() {
        final ListView listView = (ListView)getActivity().findViewById(R.id.meal_list);
        RestAdapter restAdapter = new RestAdapter
                .Builder()
                .setEndpoint("http://foodport.co.in")
                .build();

        MealService mealService = restAdapter.create(MealService.class);


        mealService.listMeals()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Meal>>() {
                    @Override
                    public void call(List<Meal> meals) {
                        Log.i("Meal Fragment", "Got the Meals");
                        Log.i("Meal Fragment", Boolean.toString(meals.get(0).getVeg()));
                        mMealList = meals;
                        final MealAdapter mealAdapter = new MealAdapter(getActivity(), mMealList);
                        listView.setAdapter(mealAdapter);

                        saveMeals(meals);

                    }
                });

    }

    private void saveMeals(List<Meal> meals) {
        SharedPreferences settings = getActivity().getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        String jsonMeals = gson.toJson(meals);

        editor.putString(MEALS, jsonMeals);
        editor.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(getActivity());
        return rootView;
    }


}