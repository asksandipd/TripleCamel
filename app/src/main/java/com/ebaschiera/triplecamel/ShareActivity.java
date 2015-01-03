package com.ebaschiera.triplecamel;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.content.pm.*;
import android.widget.Toast;
import java.util.regex.*;
import java.util.List;


/**
 * Created by ebaschiera on 01/01/15.
 */
public class ShareActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.main);

        // Get the intent that started this activity
        Intent intent = getIntent();
        Uri data = intent.getData();

        String amazon_share_text = intent.getStringExtra(Intent.EXTRA_TEXT);
        //if (amazon_share_text.matches(".*https://www\\.amazon\\.(?:[a-zA-Z]{2}|[a-zA-Z]{2}\\.[a-zA-Z]{2}|com)/.*")) {
        if (amazon_share_text.matches(".*https://www\\.amazon\\.(?:ca|cn|de|es|fr|in|it|co\\.jp|co\\.uk|com)/.*")) {
            Log.d("triple", "It matches!");
            Log.d("triple", amazon_share_text);
            Pattern p = Pattern.compile(".*(https://www\\.amazon\\.(?:ca|cn|de|es|fr|in|it|co\\.jp|co\\.uk|com)/.*)");
            Matcher m = p.matcher(amazon_share_text);
            String amazon_share_url = "";
            while (m.find()) { // Find each match in turn; String can't do this.
                amazon_share_url = m.group(1); // Access a submatch group; String can't do this.
            }
            if (amazon_share_url == "") {
                //return a warning and stop the intent
                Context context = getApplicationContext();
                CharSequence text = "Sorry, no Amazon link found. Triple Camel will work only sharing an Amazon URL.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();
            }
            amazon_share_url = Uri.encode(amazon_share_url);
            Uri camel_search_uri = Uri.parse("http://camelcamelcamel.com/search?sq=" + amazon_share_url);
            Log.d("triple", amazon_share_url);
            Log.d("new_intent", camel_search_uri.toString());
            Intent webIntent = new Intent(Intent.ACTION_VIEW, camel_search_uri);

            // Verify it resolves
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
            boolean isIntentSafe = activities.size() > 0;

// Start an activity if it's safe
            if (isIntentSafe) {
                startActivity(webIntent);
                finish();
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Sorry, no suitable app found to open an URL.";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();
            }
        } else {
            //return a warning and stop the intent
            Context context = getApplicationContext();
            CharSequence text = "Sorry, no Amazon link found. Triple Camel will work only sharing an Amazon URL.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            finish();
        }



    }
}
