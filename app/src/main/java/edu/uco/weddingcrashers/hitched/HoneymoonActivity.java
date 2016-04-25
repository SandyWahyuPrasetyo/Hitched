package edu.uco.weddingcrashers.hitched;

import android.app.Activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class HoneymoonActivity extends Activity implements View.OnClickListener{

    ArrayList<HoneymoonItem> honeymoonItems;
    HoneymoonAdapter honeymoonAdapter;
    ListView honeymoonView;

    private AutoScrollViewPager pager;

    private TextView tv;


    private ImagePage pagerAdapter;

    private CirclePageIndicator indicator;

    private int[] imgs = {R.drawable.yuxia1,R.drawable.yuxia3,R.drawable.yuxia5};

    private List<ImageView> imageViews;

    private Button flight,hotel,area;


    final Context context = this;
    private DisplayMetrics metric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honeymoon);

       /* temp=new HoneymoonItem("Tahiti",R.mipmap.tahiti,"This is a good place to travel when you are about dieing.");
        honeymoonItems.add(temp);*/
            tv = (TextView) findViewById(R.id.more);
            tv.setOnClickListener(this);
            flight = (Button) findViewById(R.id.flight);
            flight.setOnClickListener(this);
            hotel = (Button) findViewById(R.id.hotel);
            hotel.setOnClickListener(this);
            area = (Button) findViewById(R.id.area);
            area.setOnClickListener(this);
            final GridView gridView = (GridView) findViewById(R.id.gv);
            pager = (AutoScrollViewPager) findViewById(R.id.autopager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                honeymoonItems=new ArrayList<>();
                HoneymoonItem temp=new HoneymoonItem("Santorini",R.mipmap.santori,"Known for its brilliant sunsets, rich Greek food and romantic hotels, Santorini is almost tailor-made for those who've just said I do Honeymooners can lounge on red- and black-sand beaches or visit the island's wineries.");
                honeymoonItems.add(temp);
                temp=new HoneymoonItem("Bora Bora",R.mipmap.bora,"This is a good place to travel when you are about dieing.");
                honeymoonItems.add(temp);
                temp=new HoneymoonItem("Maldives",R.mipmap.maldives,"This is a good place to travel when you are about dieing.");
                honeymoonItems.add(temp);
                temp=new HoneymoonItem("Maui",R.mipmap.maui,"This is a good place to travel when you are about dieing.");
                honeymoonItems.add(temp);
                honeymoonAdapter = new HoneymoonAdapter(HoneymoonActivity.this, honeymoonItems);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridView.setAdapter(honeymoonAdapter);
                    }
                });

            }
        }).start();
           // gridView.setAdapter(honeymoonAdapter);
            honeymoonView = (ListView) findViewById(R.id.itemizedHoneymoonList);
            honeymoonView.setAdapter(honeymoonAdapter);
            indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        Log.v("rush_yu",System.currentTimeMillis()+"");
        long a = System.currentTimeMillis();
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageViews = new ArrayList<ImageView>();
                        for(int i:imgs){
                            ImageView iv = new ImageView(HoneymoonActivity.this);
                            Picasso.with(HoneymoonActivity.this).load(i).resize(metric.widthPixels,metric.heightPixels/4).into(iv);
                            iv.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageViews.add(iv);
                            pagerAdapter = new ImagePage();
                        }
                        pager.setPageTransformer(true, new ViewPagerRoation());
                        pager.setAdapter(pagerAdapter);
                        indicator.setViewPager(pager);
                        pager.setAutoScrollDurationFactor(3);
                        pager.startAutoScroll();
                        indicator.setSnap(true);
                    }
                });
            }
        }).start();

        Log.v("rush_yu",System.currentTimeMillis()+"|"+(System.currentTimeMillis()-a));




        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.honeymoon_dialog);
                dialog.setTitle("My Title");
                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.honeymoonDescription);
                text.setText(honeymoonItems.get(position).getDescription());
                dialog.show();
                return false;
            }
        });


       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position,
                                   long id) {
               // custom dialog

               Intent intent = new Intent(HoneymoonActivity.this, HoneymoonGalleryActivity.class);
               intent.putExtra("flag",(position+1)+"");
               startActivity(intent);
           }
       });





    }



    public void honeymoongalleryActivity(View view){
        Intent intent = new Intent(HoneymoonActivity.this, HoneymoonGalleryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()){

            case R.id.flight:
                intent = new Intent(this,Flight.class);
                startActivity(intent);

                break;
            case R.id.area:

                 intent = new Intent(this,AreaActivity.class);
                startActivity(intent);

                break;
            case R.id.hotel:
                intent = new Intent(this,HotelActivity.class);
                startActivity(intent);
                break;

            case R.id.more:
                intent = new Intent(HoneymoonActivity.this,MoreActivity.class);
                startActivity(intent);

                break;
        }
    }

    class ImagePage extends PagerAdapter {

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(imageViews.get(position));
            return  imageViews.get(position);
            /*container.removeView(imageViews.get(position));
            container.addView(imageViews.get(position));*/


//            Bitmap bitmap = getBitmapFromMemCache(imgs[position]);
//            if(bitmap==null){
//                ImageView iv1 = new ImageView(getActivity());



            //imgView.setImageResource(imgs[i]);
            /*Bitmap bm = BitmapFactory.decodeResource(this.getResources(), imgs[i]);
            BitmapDrawable bd = new BitmapDrawable(this.getResources(), bm);
            imgView.setImageDrawable(bd);
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);*/
            // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小

               /* final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                options.inSampleSize = Utils.calculateInSampleSize(options, width, height / 20);
                options.inJustDecodeBounds = false;
                bitmap =  BitmapFactory.decodeResource(getResources(), imgs[position], options);
                BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
                iv1.setImageDrawable(bd);

                 iv1.setScaleType(ImageView.ScaleType.FIT_XY);
                addBitmapToMemoryCache(imgs[position],bitmap);*/

            //}
           /* BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
            Log.v("log", "hhh");
            iv.setImageDrawable(bd);

            container.addView(iv);*/

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));

        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

}