package c.main.aortaandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


//wordt niet meer gebruikt

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText message=(EditText)findViewById(R.id.tryText);
        Button sendButton=(Button)findViewById(R.id.tryButton);
        TextView text=(TextView) findViewById(R.id.tryLabel);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RESTSend(message.getText().toString());
            }
        });
    }
    public void RESTSend(String message){

    }

}
