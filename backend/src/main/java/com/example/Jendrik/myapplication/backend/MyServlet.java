/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.Jendrik.myapplication.backend;


import com.example.Jendrik.myapplication.backend.Server.MoerderServer;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import java.io.IOException;

import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import javax.servlet.http.*;

public class MyServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        //Create a new Firebase instance and subscribe on child events.
        Firebase firebase = new Firebase("https://[YOUR-FIREBASE-APP].firebaseio.com/todoItems");
        final MoerderServer moerderServer = new MoerderServer();

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot todoItem : dataSnapshot.getChildren()) {
                    for (DataSnapshot field : todoItem.getChildren()) {
                       /* String topic = field.getKey(); //welcher Teil wäre das?
                        String content = field.getValue().toString();

                        switch(topic){
                            case "new":
                                moerderServer.newGame(topic, content);
                                break;
                            case "blabla"...
                        }*/
                    }
                }


            }
            public void onCancelled(FirebaseError firebaseError) { }
        });
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        /*String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if(name == null) {
            resp.getWriter().println("Please enter a name");
        }
        resp.getWriter().println("Hello " + name);*/

    }
}
