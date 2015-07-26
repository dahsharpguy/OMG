package com.dasharpguy.omg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

/**
 * Created by eit on 7/26/15.
 */
public class DetailActivity extends ActionBarActivity {
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
    String mImageURL;
    ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //choosing layout
        setContentView(R.layout.activity_detail);
        Slidr.attach(this);

        //enable the "up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //access the imageview from xml
        ImageView imageView = (ImageView) findViewById(R.id.img_cover);

        //unpack the coverID from its trip inside Intent
        String coverID = this.getIntent().getExtras().getString("coverID");

        //check if there is a valid coverID
        if (coverID.length() > 0){
            //Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverID + "-L.jpg";

            //use Picasso to load the image
            Picasso.with(this).load(mImageURL).placeholder(R.mipmap.img_books_loading).into(imageView);
        }
    }

    private void setShareIntent(){
        //create an Intent with the contents of the TextView
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Book Recommendation");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mImageURL);

        //make sure the provider knows
        //it should work with that intent
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        //this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //access the share item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        //access the object responsible for
        //putting together the sharing submenu

        if (shareItem != null){
            mShareActionProvider
                    = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        setShareIntent();
        return true;

    }
}
