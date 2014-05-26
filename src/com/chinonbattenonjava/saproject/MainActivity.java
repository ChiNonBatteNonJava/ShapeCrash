package com.chinonbattenonjava.saproject;

import java.io.IOException;
import java.io.Serializable;

import ClientSide.Client;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Client c1 = Client.getInstance();
        Thread a = new Thread(c1);
        a.start();
        long time = 0;
        time = System.currentTimeMillis();
        while (c1.getConnectionStatus()==0 && (System.currentTimeMillis()-time)<2000){
        	;
        }
        if (c1.getConnectionStatus()==-1){
        	Toast.makeText(MainActivity.this, "errore nella connessione",Toast.LENGTH_SHORT).show();
        }
        if (c1.getConnectionStatus()==1){
        	Toast.makeText(MainActivity.this, "connessooooooooooooooooo",Toast.LENGTH_SHORT).show();
        }
        
        Button bt1 = (Button) findViewById(R.id.button1);
        bt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Settings.class);
				startActivity(intent);
				
				
			}
		});
        Button bt2 = (Button) findViewById(R.id.button2);
        bt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, Credits.class);
				startActivity(intent);
				
			}
		});

        Button bt3 = (Button) findViewById(R.id.button3);
        bt3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(MainActivity.this, Play.class);
				startActivity(intent);
				
			}
		});
        /*
        Button bt4 = (Button) findViewById(R.id.button4);
        bt4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Waiting_room.class);
				startActivity(intent);

			}
		});*/
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
