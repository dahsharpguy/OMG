package com.dasharpguy.omg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by eit on 7/25/15.
 */
public class JSONAdapter extends BaseAdapter{
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJSONArray;

    public JSONAdapter(Context context, LayoutInflater inflater){
        mContext = context;
        mInflater = inflater;
        mJSONArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJSONArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJSONArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //check if the view already exists
        //if so, no need to inflate and findByViewId again
        if (convertView == null){
            //inflate the custom row layout from XML
            convertView = mInflater.inflate(R.layout.row_book, null);

            //create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView)convertView.findViewById(R.id.img_thumbnail);
            holder.titleTextView = (TextView)convertView.findViewById(R.id.text_title);
            holder.authorTextView = (TextView)convertView.findViewById(R.id.text_author);

            //hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {
            //skip all the expensive inflation/findViewById
            //and just get the already made holder
            holder = (ViewHolder)convertView.getTag();
        }

        //get the current book's data in JSON form
        JSONObject jsonObject = (JSONObject)getItem(position);

        //see if there is a cover id in the Object
        if (jsonObject.has("cover_i")){
            //if so grab the cover ID out from the object
            String imageID = jsonObject.optString("cover_i");

            //construct the image URL
            String imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";

            //use picasso to load the image
            //temporarily have a placeholder in case it loads slow
            Picasso.with(mContext).load(imageURL).placeholder(R.mipmap.ic_books).into(holder.thumbnailImageView);
        } else {
            //if there is no cover ID in the object,
            //use a placeholder
            holder.thumbnailImageView.setImageResource(R.mipmap.ic_books);
        }

        //grab the title and author from the JSON
        String bookTitle = "";
        String authorName = "";

        if (jsonObject.has("title")){
            bookTitle = jsonObject.optString("title");
        }

        if (jsonObject.has("author_name")){
            authorName = jsonObject.optJSONArray("author_name").optString(0);
        }

        //send these strings to the textviews for display
        holder.titleTextView.setText(bookTitle);
        holder.authorTextView.setText(authorName);


        return convertView;
    }

    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }

    public void updateData(JSONArray jsonArray){
        //update the adapter's dataset
        mJSONArray = jsonArray;
        notifyDataSetChanged();
    }
}
