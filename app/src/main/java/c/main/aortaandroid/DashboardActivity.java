package c.main.aortaandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;



//vergelijkbaar met het dashboard in de web applicatie
//TODO momenteel staat er nog maar één button, misschien nog een tweede button bijplaatsen voor profielinformatie (eventueel ook statistieken?)
//TODO deze profiel informatie moet dan doorgestuurd worden na het inloggen (informatie opvragen vb zie vanaf lijn 39)


public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //layout file aan deze activity gekoppeld
        setContentView(R.layout.activity_dashboard);

        //als we elementen willen gebruiken die zijn geinitialiseerd in de layout file kunnen we ze op
        //deze manier aanspreken in klasse:
        Button taskButton = (Button) findViewById(R.id.takenButton);

        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lange processen moeten worden afgehandeld worden door AsyncTask (zie classe GetTakenTask extends AsyncTask....)
                //omdat deze niet op de main Thread mogen draaien

                GetTakenTask takenTask = new GetTakenTask();

                //hiermee wordt de opdracht uitgevoerd
                takenTask.execute((Void) null);

            }
        });

    }

    public class GetTakenTask extends AsyncTask<Void, Void, Boolean> {


        private JWT jwt = new JWT(((GlobaleVariabelen) getApplication()).getJWTKey());

        GetTakenTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                //url in geven die in de rest staat
                URL url = new URL("http://" + ((GlobaleVariabelen) getApplication()).getIpServer() + ":8080/DemoREST/rest_service/taken/get");
                System.out.println("url:"+"http://" + ((GlobaleVariabelen) getApplication()).getIpServer() + ":8080/DemoREST/rest_service/taken/get");

                //bij GET opdrachten is dit stuk steeds hetzelfde:
                //************************
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //JWT token, dit moet wel aangepast worden per request naar de REST (zie JWT) toevoegen aan http-get message

                connection.addRequestProperty("Authorization", "Bearer " + jwt.maakGetTaskJWT(((GlobaleVariabelen) getApplication()).getLoggedInUser(), ((GlobaleVariabelen) getApplication()).getId()));
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();
                //************************************

                int response = connection.getResponseCode();
                System.out.println("response: " + response);
                String header = connection.getHeaderField(2);
                System.out.println("header: " + header);
                //dit is het antwoord van de REST service
                header = header.substring("Bearer".length()).trim();


                Key key = ((GlobaleVariabelen) getApplication()).getJWTKey();
                Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(header);
                //deze methoden halen de specifieke data uit de JSON file en plaatsen deze in
                // een globale variabelen klassen dat elke klasse hier toegang tot kan krijgen
                ((GlobaleVariabelen) getApplication()).setTaskId(extractTaskId(claims.getBody()));
                ((GlobaleVariabelen) getApplication()).setType(extractRoomType(claims.getBody()));
                ((GlobaleVariabelen) getApplication()).setFromRoom(extractFromRoom(claims.getBody()));
                ((GlobaleVariabelen) getApplication()).setToRoom(extractToRoom(claims.getBody()));



                if (response != HttpURLConnection.HTTP_OK) {
                    System.out.println("doInBackground returnt response code: " + response);
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        //na het uitvoeren van de Asynctask wordt naar de takenpagina gegaan
        @Override
        protected void onPostExecute(final Boolean succes){
            if(succes){
                Intent i=new Intent(DashboardActivity.this, TakenActivity.class);
                startActivity(i);
            }
        }

        //methodes om informatie uit de json file te halen:


        private String extractRoomType(Map<String, Object> claims) {

            String roomType = null;
            roomType = (String) claims.get("Type");
            System.out.println("roomtype: "+roomType);
            return roomType;
        }

        private String extractFromRoom(Map<String, Object> claims) {

            String fromRoom = null;
            fromRoom = (String) claims.get("fromTag");
            System.out.println("fromRoom"+ fromRoom);
            return fromRoom;
        }

        private String extractToRoom(Map<String, Object> claims) {
            String toRoom = null;
            toRoom = (String) claims.get("toTag");
            System.out.println("toROom: "+toRoom);
            return toRoom;

        }
        private int extractTaskId(Map<String, Object> claims) {
            int taskId;
            taskId = (int) claims.get("id");
            System.out.println("taskId"+taskId);
            return taskId;

        }
    }
}
