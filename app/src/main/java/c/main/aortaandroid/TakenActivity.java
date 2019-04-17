package c.main.aortaandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class TakenActivity extends AppCompatActivity {
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken);
        TextView fromRoomTag = (TextView) findViewById(R.id.FromRoomTag);
        fromRoomTag.setText(((GlobaleVariabelen) getApplication()).getFromRoom());

        TextView toRoomTag = (TextView) findViewById(R.id.ToRoomTag);
        toRoomTag.setText(((GlobaleVariabelen) getApplication()).getToRoom());

        TextView roomType = (TextView) findViewById(R.id.roomType);
        roomType.setText(((GlobaleVariabelen) getApplication()).getType());

        final Button acceptButton = (Button) findViewById(R.id.acceptButton);
        Button cancel = (Button) findViewById(R.id.cancelButton);


        //finish button zorgt ervoor dat er een dialoog venster opspringt waarbij de gebruiker
        //de kans krijgt om een barcode in te scannen
        final Button finnishButton = (Button) findViewById(R.id.finishedButton);
        finnishButton.setEnabled(false);
        finnishButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 AlertDialog.Builder mBuilder = new AlertDialog.Builder(TakenActivity.this);
                                                 View mView = getLayoutInflater().inflate(R.layout.dialog_qr, null);
                                                 Button qrButton = (Button) mView.findViewById(R.id.QRscan);
                                                 dialogText=(TextView) mView.findViewById(R.id.textView);

                                                 mBuilder.setView(mView);
                                                 final AlertDialog dialog = mBuilder.create();
                                                 dialog.show();
                                                 qrButton.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
                                                         IntentIntegrator scanIntegrator = new IntentIntegrator(TakenActivity.this);
                                                         scanIntegrator.initiateScan();
                                                         ((GlobaleVariabelen) getApplication()).setDialog(dialog);

                                                     }
                                                 });
                                             }
                                         });


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptTask acceptTask = new AcceptTask(((GlobaleVariabelen) getApplication()).getTaskId());
                acceptTask.execute((Void) null);
                acceptButton.setVisibility(View.GONE);
                finnishButton.setEnabled(true);

                    }
                });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent=scanningResult.getContents();
            int controleId=Integer.parseInt(scanContent);
            if(controleId==((GlobaleVariabelen) getApplication()).getTaskId()) {
                CompleteTask completeTask = new CompleteTask(((GlobaleVariabelen) getApplication()).getTaskId());
                completeTask.execute((Void) null);
                ((GlobaleVariabelen) getApplication()).getDialog().dismiss();

            }
            else{
                dialogText.setText("verkeerde code");

            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public class AcceptTask extends AsyncTask<Void, Void, Boolean> {


        public HashMap<String, Object> claims;
        private JWT jwt = new JWT(((GlobaleVariabelen) getApplication()).getJWTKey());

        public AcceptTask(int task) {
            claims=new HashMap<String, Object>();
            //AfstandIngevuld a=new AfstandIngevuld(filiaalId,afstand);
            System.out.println("taskid:"+task);
            claims.put("taskid",task);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss",Locale.FRANCE);
            LocalTime time = LocalTime.now();
            System.out.println("first time="+time);
            String f = formatter.format(time);
            System.out.println("time: "+f);
            
            claims.put("time",f);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
                try {
                    String requestUrl = "http://" + ((GlobaleVariabelen) getApplication()).getIpServer() + ":8080/DemoREST/rest_service/taken/accept";
                    String payload = "Bearer " + jwt.maakJWTVoorPostEigenschappen(claims);
                    URL url = new URL(requestUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "Bearer " + jwt.maakAcceptJWT(((GlobaleVariabelen) getApplication()).getLoggedInUser(), ((GlobaleVariabelen) getApplication()).getId()));
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                    writer.write(payload);
                    writer.close();
                    int response = connection.getResponseCode();
                    if(response != HttpURLConnection.HTTP_OK) {
                        System.out.println("doInBackground in AreaMapActivity returnt response code: " + response);
                        return false;
                    }

                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            return null;
        }

    }
    public class CompleteTask extends AsyncTask<Void, Void, Boolean> {

        public HashMap<String, Object> claims;
        private JWT jwt = new JWT(((GlobaleVariabelen) getApplication()).getJWTKey());

        public CompleteTask(int task) {
            claims=new HashMap<String, Object>();
            //AfstandIngevuld a=new AfstandIngevuld(filiaalId,afstand);
            System.out.println("taskid:"+task);
            claims.put("taskid",task);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss",Locale.FRANCE);
            LocalTime time = LocalTime.now();
            System.out.println("first detime="+time);
            String f = formatter.format(time);
            System.out.println("detime: "+f);

            claims.put("time",f);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String requestUrl = "http://" + ((GlobaleVariabelen) getApplication()).getIpServer() + ":8080/DemoREST/rest_service/taken/complete";
                String payload = "Bearer " + jwt.maakJWTVoorPostEigenschappen(claims);
                URL url = new URL(requestUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + jwt.maakCompleteJWT(((GlobaleVariabelen) getApplication()).getLoggedInUser(), ((GlobaleVariabelen) getApplication()).getId()));
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(payload);
                writer.close();
                int response = connection.getResponseCode();
                if(response != HttpURLConnection.HTTP_OK) {
                    System.out.println("doInBackground reageert: " + response);
                    return false;
                }

                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        //TODO na het finnishen van een bepaalde taak moet de volgende taak worden ingeladen
            //TODO indien er geen taak meer is moet dit ook duidelijk worden gemaakt
        }

    }
}
