package example.nottyo.org.espressocucumberexample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by traitanit on 9/10/2016 AD.
 */

public class WelcomeActivity extends Activity {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd'T'HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView message = (TextView) findViewById(R.id.welcome_message);
        Date date = new Date();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            message.setText("Welcome "+ bundle.getString("USER")+ " current time is "+dateFormat.format(date));
        }
    }
}
