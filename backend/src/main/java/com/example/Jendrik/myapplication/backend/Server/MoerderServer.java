package com.example.Jendrik.myapplication.backend.Server;

import com.example.Jendrik.myapplication.backend.GameObjekts.Player;
import com.example.Jendrik.myapplication.backend.GameObjekts.Clue;
import com.example.Jendrik.myapplication.backend.other.Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;




public class MoerderServer {

	public final static int DEFAULT_PORT = 2342;
	protected int port;
	protected ServerSocket serverSocket;
	private HashMap<String, Game> games;

	private HashMap<String,  ArrayList<String>> newPlayers;
	private HashMap<String, Integer> updates;
	public static final String API_KEY = "AIzaSyBBRO0ezLXVL_Gl2JPi0DxmJYT9gHvJ5Cg";
	private HttpURLConnection conn;
	private Game game;
	private String gameName;
	private Player player;
	private JSONObject jResponse;
	
	public MoerderServer(){
		try {
			URL url = new URL("https://android.googleapis.com/gcm/send");
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestProperty("Authorization", "key=" + API_KEY);
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	    } catch (IOException e) {
			System.err.println("Das hat nicht geklappt: " + e);
			System.exit(1);
		}
	}
	
	public Set<String> searchGame(String searchString){
		Set<String> setty = games.keySet();
        for(String s : setty){
            if(!s.contains(searchString)){
                setty.remove(s);
            }
        }
		return setty;
	}
	
	public void startGame(String key){
		updates.put(key, new Integer(0));
		game = games.get(key);
		game.startGame(newPlayers.get(key));
		newPlayers.remove(key);
		games.remove(key);
		games.put(key, game);
		sendGameToGroup(game.getGameName());
	}
	
	private void sendGameToGroup(String gameName){
		game = games.get(gameName);
		try{
			jResponse = new JSONObject();
			jResponse.put("message", "next");
			jResponse.put("game", game);
		}catch(JSONException e){
			e.printStackTrace();
		}
		this.sendData(jResponse, game.getGameName());
	}
	
	public void sendData(JSONObject jData, String game){
		// Prepare JSON containing the GCM message content. What to send and where to send.
        try{
		JSONObject jFcmData = new JSONObject();
        
        jFcmData.put("to", "/topics/"+ game); //sendet an topic
        jFcmData.put("data", jData);

        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(jFcmData.toString().getBytes());

        }catch(IOException e){
        	e.printStackTrace();
        } catch (JSONException e) {
			e.printStackTrace();
		}
        
	}
	
	public void sendMessage(String code, String gameName){
		try{
			jResponse = new JSONObject();
			jResponse.put("message", code);
			jResponse.put("gameName", gameName);
		}catch(JSONException e){
			e.printStackTrace();
		}
		this.sendData(jResponse, gameName);
	}
	
	public JSONObject receiveData(){
		try{
			InputStream inputStream = conn.getInputStream();
			String resp = IOUtils.toString(inputStream);
			//TODO how to convert String back to JSONObject
		}catch(IOException e){
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	public static void main(String[] args){
		MoerderServer server = new MoerderServer();
		server.run();
	}


	private void run() {
		// TODO ist mein server runable oder muss ich ne do while schleife machen?
		try{
			JSONObject jData = (JSONObject) receiveData().get("data");
			switch((String) jData.get("message")){
			case "end":
				//an alle end senden?
				break;
			case "next":
				game = (Game) jData.get("game");
				games.put(game.getGameName(), game);
				sendGameToGroup(game.getGameName());
				break;
			case "player":
				gameName = jData.getString("gameName");
				player = (Player) jData.get("player");
				games.get(gameName).updatePlayer(player);
				if(games.get(gameName).getPlayerAmount() == updates.get(gameName)){
					//wenn alle player geupdatet wurden, wird das spiel wieder verschickt
					updates.remove(gameName);
					updates.put(gameName, new Integer(0));
					game = (Game) jData.get("game");
					games.put(game.getGameName(),  game);
					sendGameToGroup(game.getGameName());
				}else{
					int i = updates.get(gameName);
					updates.remove(gameName);
					updates.put(gameName, new Integer(i+1));
				}
				break;
			case "playerCall":
				int playerQR = (Integer) jData.get("playerQR");
				int roomQR = (Integer) jData.get("roomQR");
				//callPlayer(playerQR, roomQR);
				break;
			case "prosecution":
				sendData(jData, jData.getString(gameName));
				break;
			case "suspection":
				sendData(jData, jData.getString(gameName));
				break;
			case "showClue":
				game = games.get(jData.getString("gameName"));
				int suspector = jData.getInt("suspector"); //QR code of suspector
				ArrayList<Integer> qrCodesOfPlayersWithCards = new ArrayList<Integer>();
				for(Player p : game.getPlayers()) {
		            if (p.isActive()) {
		                if(suspector != p.getpNumber()){
		                    for(Clue c : p.getGivenClues()){
		                        if(c.getName().equals(jData.getString("suspectedPlayer")) 
		                        		|| c.getName().equals(jData.getString("suspectedRoom")) 
		                        		|| c.getName().equals(jData.getString("suspectedWeapon"))){
		                        	qrCodesOfPlayersWithCards.add(p.getQrCode());
		                        }
		                    }
		                }
		            }
		        }
				int i = suspector;
				int qrnr = 0;
				while(qrnr == 0){
					i += 1;
					if(i <= game.getPlayerAmount() ){
						if(qrCodesOfPlayersWithCards.contains(i)){
							qrnr = i;
						}
					}else if(i < suspector){
						i = 0;
					}else if(i == suspector){
						qrnr = suspector;
					}
				}
				jData.append("qrnr", qrnr); 
				if(qrnr == suspector){
					jData.append("failure", "failure");
				}
				sendData(jData, jData.getString(gameName));
				break;
			case "showedClue":
				sendData(jData, jData.getString(gameName));
				break;
			case "dead":
				//TODO was passiert hier eigentlich
				break;
			case "pause":
				sendData(jData, jData.getString(gameName));
				break;
			case "join":
				if(jData.has("password")){ //TODO anlegen
					if(checkPassword(jData.getString("gameName"), jData.getString("password"))){
						sendMessage("welcome", jData.getString("gameName"));
					}else{ 
						sendMessage("passwordError", jData.getString("gameName"));
					}
				}else{
					sendMessage("welcome", jData.getString("gameName"));
				}
				break;
			case "newGame": //TODO stimmt dieser Code?
				game = (Game) jData.get("game");
				String message;
				if(games.containsKey(game.getGameName())){
					message = "gameNameError";
				}else{
					games.put(game.getGameName(), game);
					sendMessage("waitForPlayers", game.getGameName());
				}
				break;
			case "search":
				jResponse = new JSONObject();
				jResponse.put("search", searchGame((String) jData.get("searchString")));
				this.sendData(jResponse, "FUCK"); //TODO AN Individuum nicht an Gruppe senden
				break;
			case "name":
				gameName = jData.getString("gameName");
				String name = jData.getString("name");
				if(!newPlayers.containsKey(gameName)){
					newPlayers.put(gameName, new ArrayList<String>());
					newPlayers.get(gameName).add(name);
				}else{
					if(!newPlayers.get(gameName).contains(name)){
						newPlayers.get(gameName).add(name);
						//sends name to all players
						//sends other names to new player
						try{
							jResponse = new JSONObject();
							jResponse.put("message", "name");
							jResponse.put("name", name);
							for(int j = 0; j < newPlayers.get(gameName).size(); j++){
								jResponse.put("name" + j, newPlayers.get(j));
							}
							jResponse.put("gameName", gameName);
							sendData(jResponse, gameName);
						}catch(JSONException e){
							e.printStackTrace();
						}
						if(games.get(gameName).getPlayerAmount() == newPlayers.get(gameName).size()){
							startGame(gameName);
						}
					}else{
						sendMessage("nameError", gameName);
					}
					
				}
				break;
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		
	}


	private boolean checkPassword(String gamename, String password) {
		return games.get(gamename).checkPwd(password);
	}
}
