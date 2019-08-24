package anidiot.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import anidiot.game.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
   ImageButton cross;
   ImageButton o;

   Button restart;
    int player=3;
    static BT_CanvasView btc;
    static int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        cross = findViewById(R.id.cross);
//        o = findViewById(R.id.o);
          btc = findViewById(R.id.btc);
//        cross.setOnClickListener(this);
//        o.setOnClickListener(this);

        restart = findViewById(R.id.restart);
        restart.setOnClickListener(this);

        Log.e("aaaa",BluetoothActivity.bluetoothDevice.getAddress());
        flag=1;
    }

    public static void update(){
        if (flag==1){
            btc.update();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.cross:
//                o.setVisibility(View.GONE);
//                player=0;
////                btc.restart();
//                //set it to non clickable
//                break;
//            case R.id.o:
//                cross.setVisibility(View.GONE);
//                player=1;
//                break;
            case R.id.restart:
                btc.restart();

        }
    }
}

