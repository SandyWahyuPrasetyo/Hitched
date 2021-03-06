package edu.uco.weddingcrashers.hitched;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC User on 2/19/2016.
 */
public class PlaceFetchr {

    private static final String TAG = "PlaceFetchr";
    private static final String API_KEY = "AIzaSyB3Zguv8wJcA5Kqjbk-HaVbmycjB4IPQdA";
    private static final String MUSIC_API_KEY="30233dd4d3780583b8741e26f6defd82";
    private static final String DEALS_API_KEY="87w4czzpy3";
    private static final String RING_API_KEY="AIzaSyBOb62v2yNyx-DC23tIeax9RXwA9ZpnJmk";
    private static final String SEARCH_ENGINE_ID="014766176905852190470:yobmpbyohpm";

    public byte[] getURLBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ":with" + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    String getURLString(String urlSpec) throws IOException {
        return new String(getURLBytes(urlSpec));
    }
    public List<Ring> fetchRing(String query){
        List<Ring> ringList = new ArrayList<>();
        try{
            String url = Uri.parse("https://www.googleapis.com/customsearch/v1")
                    .buildUpon()
                    .appendQueryParameter("q",query)
                    .appendQueryParameter("cx",SEARCH_ENGINE_ID)
                    .appendQueryParameter("key",RING_API_KEY)
                    .build().toString();
            String jsonString = getURLString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            Log.i("RING","Received Ring JSON: " + jsonString);
            parseRing(ringList,jsonBody);

        }catch (JSONException je){
            Log.e("RING","Failed to parseJSON",je);
        }catch (IOException ioe){
            Log.e("RING","Failed to fetch rating",ioe);
        }
        return ringList;
    }

    public List<Coupon> fetchCoupon(String searchTerm, String state) {
        List<Coupon> couponList = new ArrayList<>();
        try{
            String url = Uri.parse("http://api2.yp.com/listings/v1/coupons")
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("key",DEALS_API_KEY)
                    .appendQueryParameter("searchloc",state)
                    .appendQueryParameter("term",searchTerm).build().toString();
            String jsonString = getURLString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            Log.i("Coupon", "Received JSON: " + jsonString);
            parseCoupon(couponList, jsonBody);
        }catch (JSONException je){
            Log.e("Coupon","Failed to parseJSON",je);
        }catch (IOException ioe){
            Log.e("Coupon","Failed to fetch rating",ioe);
        }
        return couponList;
    }

    public List<Review> fetchVendorReview(String placeid){
        List<Review> reviewList = new ArrayList<>();
        try{
            String url = Uri.parse("https://maps.googleapis.com/maps/api/place/details/json")
                    .buildUpon()
                    .appendQueryParameter("placeid",placeid)
                    .appendQueryParameter("key",API_KEY).build().toString();
            String jsonString = getURLString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseReview(reviewList, jsonBody);
        } catch (JSONException je){
            Log.e("VendorReview","Failed to parseJSON",je);
        }catch (IOException ioe){
            Log.e("VendorReview","Failed to fetch rating",ioe);
        }
        return reviewList;
    }

    public List<Song> fetchSearchSong(String trackName, String artist){
        List<Song> songList = new ArrayList<>();
        try{
            String url = Uri.parse("http://ws.audioscrobbler.com/2.0/")
                    .buildUpon()
                    .appendQueryParameter("method","track.search")
                    .appendQueryParameter("track", trackName)
                    .appendQueryParameter("artist", artist)
                            //.appendQueryParameter("limit","1")
                    .appendQueryParameter("api_key", MUSIC_API_KEY)
                    .appendQueryParameter("format","json").build().toString();
            String jsonString = getURLString(url);
            Log.i("Song", "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseSong(songList, jsonBody);
        }catch (JSONException je){
            Log.e("Song","Failed to parseJSON",je);
        }catch (IOException ioe){
            Log.e("Song","Failed to fetch rating",ioe);
        }
        return songList;
    }

    public List<VendorPlace> fetchItems(String querySearch) {
        List<VendorPlace> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://maps.googleapis.com/maps/api/place/textsearch/json")
                    .buildUpon()
                    .appendQueryParameter("query", querySearch)
                    .appendQueryParameter("key", API_KEY).build().toString();
            String jsonString = getURLString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }

    private void parseRing(List<Ring> items, JSONObject jsonBody) throws IOException,JSONException{
        JSONArray itemJsonArray = jsonBody.getJSONArray("items");
        for(int i = 0;i<itemJsonArray.length();i++){
            Ring mRing = new Ring();
            JSONObject itemJsonObject = itemJsonArray.getJSONObject(i);
            JSONObject pagemapJsonObject = itemJsonObject.getJSONObject("pagemap");
            JSONArray productJsonArray = pagemapJsonObject.getJSONArray("hproduct");
            for(int j = 0;j<productJsonArray.length();j++){
                JSONObject productJsonObject = productJsonArray.getJSONObject(j);
                mRing.setName(productJsonObject.getString("fn"));
                mRing.setDescription(productJsonObject.getString("description"));
                mRing.setImgURL(productJsonObject.getString("photo"));
                mRing.setWebURL(productJsonObject.getString("url"));
                mRing.setPrice(productJsonObject.getString("currency_iso4217")+" "+productJsonObject.getString("currency"));
            }
            items.add(mRing);
        }
    }
    private void parseCoupon(List<Coupon> items, JSONObject jsonBody) throws IOException,JSONException{
        JSONObject resultJsonObject = jsonBody.getJSONObject("searchResult");
        JSONObject searchJsonObject = resultJsonObject.getJSONObject("searchListings");
        JSONArray searchJsonArray = searchJsonObject.getJSONArray("searchListing");
        for(int i = 0;i<searchJsonArray.length();i++){
            Coupon mCoupon = new Coupon();
            JSONObject couponJsonObject = searchJsonArray.getJSONObject(i);
            mCoupon.setBusinessName(couponJsonObject.getString("businessName"));
            mCoupon.setCouponURL(couponJsonObject.getString("websiteURL"));
            mCoupon.setCouponPhone(couponJsonObject.getString("phone"));
            mCoupon.setCouponAddress(couponJsonObject.getString("street") + " - " + couponJsonObject.getString("state"));
            JSONObject couponJson = couponJsonObject.getJSONObject("coupons");
            JSONArray couponJsonArray = couponJson.getJSONArray("coupon");
            for(int j = 0;j<couponJsonArray.length();j++){
                JSONObject couponElementJsonObject = couponJsonArray.getJSONObject(j);
                mCoupon.setLogoURL(couponElementJsonObject.getString("couponBusinessLogo"));
                mCoupon.setCouponDescription(couponElementJsonObject.getString("couponDescription"));
                mCoupon.setCouponDisclaimer(couponElementJsonObject.getString("couponDisclaimer"));
                mCoupon.setCouponEndDate(couponElementJsonObject.getString("couponExpiration"));
                mCoupon.setCouponStartDate(couponElementJsonObject.getString("couponCreatedAt"));
                mCoupon.setCouponTitle(couponElementJsonObject.getString("couponTitle"));
            }
            items.add(mCoupon);
        }
    }

    private void parseReview(List<Review> items,JSONObject jsonBody) throws IOException,JSONException{
        JSONObject resultJsonObject = jsonBody.getJSONObject("result");
        JSONArray reviewJsonArray = resultJsonObject.getJSONArray("reviews");
        for(int i = 0;i<reviewJsonArray.length();i++){
            Review mReview = new Review();
            JSONObject reviewJsonObject = reviewJsonArray.getJSONObject(i);
            Log.i("TAG","review found:" + reviewJsonObject.getString("text"));
            mReview.setAuthorName(reviewJsonObject.getString("author_name"));
            mReview.setRating(reviewJsonObject.getString("rating"));
            mReview.setText(reviewJsonObject.getString("text"));
            mReview.setTime(reviewJsonObject.getString("time"));
            mReview.setUrl(resultJsonObject.getString("url"));
            items.add(mReview);
//            reviews.add(reviewJasonObject.getString("author_name"));
//            reviews.add(reviewJasonObject.getString("rating"));
//            reviews.add(reviewJasonObject.getString("text"));
        }
    }
    private void parseSong(List<Song> items, JSONObject jsonBody) throws  IOException,JSONException{
        if(jsonBody.has("results")) {
            JSONObject resultSongJsonObject = jsonBody.getJSONObject("results");
            JSONObject songMatchJsonObject = resultSongJsonObject.getJSONObject("trackmatches");
            JSONArray songJsonArray = songMatchJsonObject.getJSONArray("track");
            for(int i = 0;i<songJsonArray.length();i++) {
                Song mSong = new Song();
                JSONObject songFoundJsonObject = songJsonArray.getJSONObject(i);
                mSong.setName(songFoundJsonObject.getString("name"));
                mSong.setSinger(songFoundJsonObject.getString("artist"));
                mSong.setStreamingURL(songFoundJsonObject.getString("url"));
                mSong.setListener(songFoundJsonObject.getString("listeners"));
                JSONArray songImageJsonArray = songFoundJsonObject.getJSONArray("image");
                JSONObject songImageJsonObject = songImageJsonArray.getJSONObject(3);
                mSong.setImgURL(songImageJsonObject.getString("#text"));
                items.add(mSong);
            }
        }
    }
    private void parseItems(List<VendorPlace> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray resultJsonArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < resultJsonArray.length(); i++) {
            VendorPlace item = new VendorPlace();
            JSONObject resultJsonObject = resultJsonArray.getJSONObject(i);
            if (resultJsonObject.has("photos")) {
                JSONArray photoJsonArray = resultJsonObject.getJSONArray("photos");
                if (photoJsonArray.length() > 0) {
                    for (int j = 0; j < photoJsonArray.length(); j++) {
                        JSONObject photoJsonObject = photoJsonArray.getJSONObject(j);
                        item.setIconURL(photoJsonObject.getString("photo_reference"));
                    }
                }
            }
            item.setAddress(resultJsonObject.getString("formatted_address"));
            item.setName(resultJsonObject.getString("name"));
            item.setID(resultJsonObject.getString("place_id"));

            // item.setPriceLevel(resultJsonObject.getString("price_level"));
            //item.setRating(resultJsonObject.getString("rating"));
            items.add(item);
        }

    }
}
