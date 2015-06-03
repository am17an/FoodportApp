package com.foodport.foodport;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.media.Image;
import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodport.foodport.model.Meal;
import com.foodport.foodport.service.CartService;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by amangupta on 01/06/15.
 */
public class MealAdapter extends BaseAdapter {

    private List<Meal> mMeals;
    private Activity mContext;
    private LayoutInflater mInflater;
    private CartService mCartService;

    static class ViewHolder {
        ImageView image;
        TextView text;
        TextView price;
        TextView description;
        TextView quantity;
        ImageButton removeMeal;
        ImageButton addMeal;
        ImageView mealType;
    }

    @InjectView(R.id.image)
    ImageView imageView;

    @InjectView(R.id.title)
    TextView titleView;

    public MealAdapter(Activity activity, List<Meal> meals) {
        mMeals = meals;
        mContext = activity;
        mCartService = CartService.getInstance(mContext);
        updateView();
    }
    @Override
    public int getCount() {
        return mMeals.size();
    }

    @Override
    public Object getItem(int i) {
        return mMeals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if(mInflater == null) {
            mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null) {
            view = mInflater.inflate(R.layout.meal_view, null);

            holder = new ViewHolder();
            holder.image = (ImageView)view.findViewById(R.id.image);
            holder.text  = (TextView)view.findViewById(R.id.title);
            holder.description = (TextView)view.findViewById(R.id.description);
            holder.price = (TextView)view.findViewById(R.id.price);
            holder.removeMeal = (ImageButton)view.findViewById(R.id.remove_meal);
            holder.addMeal = (ImageButton)view.findViewById(R.id.add_meal);
            holder.quantity = (TextView)view.findViewById(R.id.quantity);
            holder.mealType = (ImageView)view.findViewById(R.id.meal_type_image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Meal meal = mMeals.get(i);

        holder.text.setText(meal.getName());
        holder.description.setText(meal.getDescription());
        holder.price.setText(mContext.getString(R.string.rupee) + meal.getPrice());
        holder.quantity.setText(Integer.toString(mCartService.getMealQuantity(meal)));
        holder.mealType.setImageDrawable(
              meal.getVeg()?  mContext.getResources().getDrawable(R.drawable.veg)
                      : mContext.getResources().getDrawable(R.drawable.non_veg)
        );

        holder.addMeal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCartService.addMeal(meal);
                        updateView(holder, meal);
                    }
                }
        );

        holder.removeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCartService.removeMeal(meal);
                updateView(holder, meal);
            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.description.getVisibility() == View.VISIBLE) {
                    holder.description.setVisibility(View.GONE);
                } else {
                    holder.description.setVisibility(View.VISIBLE);
                }
            }
        });

        // 2 is the medium size image of roughly 740x400 -- will work well for mobile.
        Picasso.with(mContext)
                .load(meal.getMealImages().get(2).getUrl())
                .placeholder(R.drawable.ic_launcher)
                .transform(new FloorFadeTransformation())
                .fit()
                .centerCrop()
                .into(holder.image);

        return view;
    }

    private void updateView(ViewHolder view, Meal meal) {
        view.quantity.setText(Integer.toString(mCartService.getMealQuantity(meal)));
        if(mCartService.getTotalItems() > 0) {
            mContext.findViewById(R.id.checkout_button).setVisibility(View.VISIBLE);
        } else {
            mContext.findViewById(R.id.checkout_button).setVisibility(View.GONE);
        }
    }

    public void updateView() {
        if(mCartService.getTotalItems() > 0) {
            mContext.findViewById(R.id.checkout_button).setVisibility(View.VISIBLE);
        } else {
            mContext.findViewById(R.id.checkout_button).setVisibility(View.GONE);
        }
    }

    public static class FloorFadeTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            LinearGradient shader = new LinearGradient(0,
                    source.getHeight()/2,
                    0,
                    source.getHeight(),
                    Color.TRANSPARENT,
                    Color.parseColor("#333333"),
                    Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);

            Canvas canvas = new Canvas(source);
            canvas.drawPaint(paint);
            return source;
        }

        @Override
        public String key() {
            return "floorFade()";
        }
    }
}
