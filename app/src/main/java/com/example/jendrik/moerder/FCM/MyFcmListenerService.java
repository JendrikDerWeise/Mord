
package com.example.jendrik.moerder.FCM;



import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.example.jendrik.moerder.GUI.Host.WaitForPlayers;
import com.example.jendrik.moerder.GUI.LittleHelpers.SuspectionHelpers.Suspection;
import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.GameHandler;
import com.example.jendrik.moerder.GameObjekts.Player;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "MyGcmListenerService";
    private static final String SENDER_ID = UUID.randomUUID().toString(); ;
    private static String topic;
    private static String gameName;
    private static final FirebaseMessaging fm = FirebaseMessaging.getInstance();
    public static final Object stopMarker =new Object();
    public static boolean anyBool = false;



    @Override
    public void onMessageReceived(RemoteMessage message){
        String from = message.getFrom();
        Map data = message.getData();
        Log.d(TAG, "From: " + message.getFrom());
        Log.d(TAG, "Notification Message Body: " + message.getNotification().getBody());


        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
        Game game;
        String keyword = (String) data.get("keyword"); //TODO keyword am anderen Ende einbauen
        switch(keyword){
            case "end": //TODO was passiert hier
                fm.unsubscribeFromTopic("/topics/" + topic);
                break;
            case"next":
                game = (Game) data.get("game");
                GameHandler.saveGame(game);
                //TODO hier menu drawer aufrufen
                break;
            case "playerUpdate":
                updatePlayer(new Player()); //TODO wo kriege ich den Player her?
                break;
            case "showClue":
                /**
                 * codewords
                 *  suspector
                 *  suspectedPlayer
                 *  suspectedRoom
                 *  suspectedWeapon
                 **/
                if((Integer) data.get("qrnr") == 47) { //TODO wo kriege ich meinen qr code her?
                    if(data.containsKey("failure")){
                        //TODO oeffne pop-up mit "dein verdacht war gut, niemand hat eine karte, mach in er nächsten runde eine anklage"
                    }
                    //TODO den show card screen oeffnen
                }
                break;
            case "showedClue":
                if((Integer) data.get("suspector") == 47) { //TODO wo kriege ich meinen qr code her?
                    //TODO der und der hat die und die karte gezeigt bla bla bla oeffnen
                    //code cardOwner für den der die karte gezeigt hat und card für die karte
                    sendSuspection(data);
                }
                break;
            case "playerCall":
                game = GameHandler.loadGame();
                String calledPlayer = game.getNameByNumber((Integer) data.get("qrnr"));
                if(calledPlayer == "Peter") { //TODO wo kriege ich meinen name her?
                    int room = (Integer) data.get("roomQR");
                    //TODO POPUP "begib dich in den raum room" + scan button
                }
                break;
            case "prosecution":
                //TODO POPUP "begib dich in den Gruppenraum" + scan button
                break;
            case "dead":
                //TODO was passiert hier, gibt es das?
                break;
            case "suspection":
                if((Integer) data.get("suspector") == 47) { //TODO wo kriege ich meinen qr code her?
                    sendGame();
                }else{
                    //TODO Popup mit den daten
                    /**
                     * codewords
                     *  suspector
                     *  suspectedPlayer
                     *  suspectedRoom
                     *  suspectedWeapon
                     *  cardOwner / the player that showed a card
                     **/
                }
                break;
            case "pause":
                int playerqr = (int) data.get("player");
                String playername = GameHandler.loadGame().getNameByNumber(playerqr);
                //TODO wenn ich = player, dann pause beenden pop-up mit knopf
                //TODO Popup mit name und hat pause gedrueckt
                break;
            case "welcome":
                fm.subscribeToTopic("/topics/" + topic);
                sendName("Peter"); //TODO wo wird der name gespeichert
                break;
            case "name":
                //TODO kann man das einfach mit zu den leuten auf den wartebildschirm schreiben?
                if((String) data.get("name") == "Peter") { //TODO Wo kriege ich meinen namen her? ANTWORT: setzt die Klasse selber ein
                    int j = 0;
                    while(data.containsKey("name"+ j)){
                        WaitForPlayers.addPlayer((String) data.get("name" + j));
                        j += 1;
                    }
                }
                WaitForPlayers.addPlayer((String) data.get("name")); //setzt beim Host neuen Spieler in die Liste... hoffentlich
                //WaitForServer.addPlayer((String) data.get("name")); Für die Client-Seite
                break;
            case "nameError":
                String name = (String) data.get("name");//wird diese Zeile benötigt?
                anyBool=false;
                synchronized (stopMarker){
                    stopMarker.notify();
                }
                //TODO neues name pop-up mit "name xyz schon vergeben"
            case "gameNameError":
                //TODO popup "name schon vegeben, ueberleg dir einen Neuen" /fuer spiel
                break;
            case "joinerror":
                //TODO popup "leider ist etwas schief gelaufen, waehle ein anderes spiel oder gib das richtige pw ein"
                break;
            case "sorry":
                break;
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        //sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    public static void callPlayer(int qrnr, int roomnr, AtomicInteger msgId){
        /**Bundle data = new Bundle();
        data.putString("message", "playerCall");
        data.putInt("qrnr", qrnr);
        data.putInt("roomQR", roomnr);**/

        JSONObject json = new JSONObject();
        try{
            json.put("gameName", gameName);
            json.put("qrnr", qrnr);
            json.put("roomQR", roomnr);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "playerCall");

        //TODO sinnvolle msgId uebergeben
    }

    public static void joinGame(String name){
        /**Bundle data = new Bundle();
        data.putString("message", "join");
        data.putString("gameName", name);**/
        topic = name;
        JSONObject json = new JSONObject();
        try{
            json.put("gameName", name);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "join");
    }

    public static void joinGame(String name, String password){
        /**Bundle data = new Bundle();
        data.putString("message", "join");
        data.putString("gameName", name);
        data.putString("password", password);**/
        topic = name;
        JSONObject json = new JSONObject();
        try{
            json.put("gameName", name);
            json.put("password", password);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "join");
    }

    public static void newGame(Game game){
        //TODO wo kriege ich das Game übergeben

        /**Bundle data = new Bundle();
        data.putString("message", "new");
        data.putString("gameName", game.getGameName());
        data.putSerializable("game", game);**/
        topic = game.getGameName();
        JSONObject json = new JSONObject();
        try{
            json.put("gameName", game.getGameName());
            json.put("game", game);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "new");

        //TODO in den Wartebildschirm
    }

    public static void pause(int playerQR){
        /**Bundle data = new Bundle();
        data.putString("message", "pause");**/
        JSONObject json = new JSONObject();
        try{
            json.put("player", playerQR);
            json.put("gameName", gameName);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "pause");
    }

    public static void prosecution(){
        /**Bundle data = new Bundle();
        data.putString("message", "prosecution");**/
        JSONObject json = new JSONObject();
        try{
            json.put("gameName", gameName);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "prosecution");
    }

    public static void sendGame(){
        Game game = GameHandler.loadGame();

        if(game.isGameOver()){
            //sendString("end");
        }else{
            /**Bundle data = new Bundle();
            data.putString("message", "next");
            data.putSerializable("game", game);**/
            JSONObject json = new JSONObject();
            try{
                json.put("gameName", gameName);
                json.put("game", game);
            }catch(JSONException e){
                e.printStackTrace();
            }
            sendData(json, new AtomicInteger(123456789), "next");
        }

    }

    public static void sendName(String playerName){
        JSONObject json = new JSONObject();
        try{
            json.put("name", playerName);
            json.put("gameName", gameName);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "name");
    }

    public static void sendShowClue(Suspection suspection){
       JSONObject json = new JSONObject();
        try{
            json.put("gameName", gameName);
            json.put("suspector", suspection.getSuspector() + 1);
            json.put("suspectedPlayer", suspection.getPlayer());
            json.put("suspectedRoom", suspection.getRoom());
            json.put("suspectedWeapon", suspection.getWeapon());
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "showCard");
    }

    public static void sendShowedClue(Suspection suspection){
        JSONObject json = new JSONObject();
        try{
            json.put("gameName", gameName);
            json.put("suspector", suspection.getSuspector() +1 );
            json.put("suspectedPlayer", suspection.getPlayer());
            json.put("suspectedRoom", suspection.getRoom());
            json.put("suspectedWeapon", suspection.getWeapon());
            json.put("cardOwner", suspection.getClueOwner());
            json.put("card", suspection.getClue());
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "showCard");
    }


    public static void sendSuspection(Map data){
        Game game = GameHandler.loadGame();
        JSONObject json = new JSONObject();
        try{
            String suspector = game.getNameByNumber((Integer)data.get("suspector")); //Suspector Nummer = QRNR -1
            json.put("gameName", gameName);
            json.put("suspector", suspector);
            json.put("suspectedPlayer", (String) data.get("suspectedPlayer"));
            json.put("suspectedRoom", (String) data.get("suspectedRoom"));
            json.put("suspectedWeapon", (String) data.get("suspectedWeapon"));
            json.put("cardOwner", (String) data.get("cardOwner"));
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "suspection");
    }

    public static void updatePlayer(Player player){
        Game game = GameHandler.loadGame();
        game.updatePlayer(player);
        GameHandler.saveGame(game);
        /**Bundle data = new Bundle();
        data.putString("message", "player");
        data.putSerializable("player", player);**/
        JSONObject json = new JSONObject();
        try{
            json.put("gameName", gameName);
            json.put("player", player);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sendData(json, new AtomicInteger(123456789), "player");
    }

    private static void sendData(JSONObject json, AtomicInteger msgId, String code){

        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("message", code)
                .addData("data",json.toString())
                .build());

    }


    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        //TODO wie baue ich die jeweilige Activity ein?
        //ist so schon richtig. Nur eben mit der richtigen Klasse. Anschließend den Intent starten "startActivity(intent);"
        //Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
        //   PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //      .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
        //      .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
