package anidiot.game;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BT_CanvasView extends View { //you have to create a new java file and then insert the same file in the xml of the page in which you want the canvas
    Paint paint = new Paint();
    Paint paintx = new Paint();
    Paint painto = new Paint();
    Paint painto1 = new Paint();
    boolean oncewin = false;
    boolean oncedrawen = false;
    float[][] midx = new float[3][3];
    float[][] midy = new float[3][3];
    Context ctx;
    float canvasSide, cellSide;
    boolean touchEnabled = true;
    boolean oppontentRematch = false;
    boolean playerRematch = false;
    int cnt = 0;
    int[] time = {1000};
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    String TAG = "BT_CanvasView";
    String p1Name = "1";
    String p2Name = "2";
    public static int[][] a = new int[3][3];
    public static int turn = 0;

    int flag=1;


    int x;
//    public static ConnectedThread connectedThread = null;

    public void init() {
        cnt = 0;
        time[0] = 1000;
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                a[row][col] = 0;
                midx[row][col] = ((px / 6) + (col * (px / 3)));
                midy[row][col] = ((px / 6) + (row * (px / 3)));
            }
        }
        touchEnabled = true;
        oncedrawen = false;
        oncewin = false;
        turn = 0;
        x=BluetoothActivity.x;
    }

    public BT_CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        paintx.setStrokeWidth(15f);
        paintx.setColor(Color.CYAN);
        paintx.setStyle(Paint.Style.STROKE);
        paintx.setStrokeJoin(Paint.Join.ROUND);

        painto.setColor(Color.CYAN);
        painto.setStyle(Paint.Style.FILL);

        painto1.setColor(Color.rgb(57,84,166));
        painto1.setStyle(Paint.Style.FILL);


        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Resources r = getResources();
        float pxi = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());
        canvasSide = pxi;
        cellSide = canvasSide / 3;
        canvas.drawLine(cellSide, 0, cellSide, canvasSide, paint);
        canvas.drawLine(2 * cellSide, 0, 2 * cellSide, canvasSide, paint);
        canvas.drawLine(0, cellSide, canvasSide, cellSide, paint);
        canvas.drawLine(0, 2 * cellSide, canvasSide, 2 * cellSide, paint);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (a[row][col] == 1) {
                    canvas.drawLine((midx[row][col] - ((4 * cellSide) / 11)), (midy[row][col] - ((4 * cellSide) / 11)), (midx[row][col] + ((4 * cellSide) / 11)), (midy[row][col] + ((4 * cellSide) / 11)), paintx);
                    canvas.drawLine((midx[row][col] + ((4 * cellSide) / 11)), (midy[row][col] - ((4 * cellSide) / 11)), (midx[row][col] - ((4 * cellSide) / 11)), (midy[row][col] + ((4 * cellSide) / 11)), paintx);
                } else if (a[row][col] == 2) {
                    canvas.drawCircle(midx[row][col], midy[row][col], (4 * cellSide) / 11, painto);
                    canvas.drawCircle(midx[row][col], midy[row][col], (13 * cellSide) / 44, painto1);
                }
            }
        }
        check();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            float touchX = event.getX();
            float touchY = event.getY();
            if (touchX < canvasSide && touchX > 0 && touchY < canvasSide && touchX > 0) {
                int col = (int) (touchX / cellSide);
                int row = (int) (touchY / cellSide);
                if (a[row][col] == 0) {
                        if(x==0){
                            a[row][col]=1;
                        }
                        else{
                            a[row][col]=2;
                        }



                    BluetoothActivity.sendMessage(String.valueOf(row)+"" + String.valueOf(col));

                    invalidate();
                    check();

                }
            }
        }
        return true;
    }

    public void showAlert(final String str) {
//        Toast.makeText(ctx,str,Toast.LENGTH_LONG).show();
        touchEnabled = true;
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                        try {
                            String endMsg = "END";

                        } catch (Exception e) {
                            Log.d(TAG, "exception " + e.getMessage());
                        }
                        Intent intent = new Intent();

                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE: {

                        init();
                        postInvalidate();
                        break;
                    }
                }
            }
        };

    }

    public void check() {
        Log.e("check","check");
//        if (!oncewin) {
            if (a[0][0] == a[0][1] && a[0][1] == a[0][2]) {
                if (a[0][0] == 1) {
                    showAlert( p1Name+ " wins! ");
                    touchEnabled=false;
                    oncewin = true;
                } else if (a[0][0] == 2) {
                    showAlert(p2Name + " wins! ");
                    touchEnabled=false;
                    oncewin = true;
                }
            }

            if (a[1][0] == a[1][1] && a[1][1] == a[1][2]) {
                if (a[1][0] == 1) {
                    showAlert(p1Name + " wins! ");
                    touchEnabled=false;
                   oncewin = true;
                } else if (a[1][0] == 2) {
                    showAlert(p2Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                }
            }

            if (a[2][0] == a[2][1] && a[2][1] == a[2][2]) {
                if (a[2][0] == 1) {
                    showAlert(p1Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                } else if (a[2][0] == 2) {
                    showAlert(p2Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                }
            }

            if (a[0][0] == a[1][0] && a[1][0] == a[2][0]) {
                if (a[0][0] == 1) {
                    showAlert(p1Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                } else if (a[0][0] == 2) {
                    showAlert(p2Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                }
            }

            if (a[0][1] == a[1][1] && a[1][1] == a[2][1]) {
                if (a[0][1] == 1) {
                    showAlert(p1Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                } else if (a[0][1] == 2) {
                    showAlert(p2Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                }
            }

            if (a[0][2] == a[1][2] && a[1][2] == a[2][2]) {
                if (a[0][2] == 1) {
                    showAlert(p1Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                } else if (a[0][2] == 2) {
                    showAlert(p2Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                }
            }

            if (a[0][0] == a[1][1] && a[1][1] == a[2][2]) {
                if (a[0][0] == 1) {
                    showAlert(p1Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                } else if (a[0][0] == 2) {
                    showAlert(p2Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                }
            }

            if (a[0][2] == a[1][1] && a[1][1] == a[2][0]) {
                if (a[0][2] == 1) {
                    showAlert(p1Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                } else if (a[0][2] == 2) {
                    showAlert(p2Name + " wins! ");

                    touchEnabled=false;oncewin = true;
                }
            }

            if (turn == 9 && !oncewin) {
                showAlert("Match results in a draw!");
                oncedrawen = true;

            }
        }

    public void restart(){
        Log.e("restart","restart");
        init();
        invalidate();
    }
    public void update(){
        String message= BluetoothActivity.messg;
        String r=message.substring(0, 1);
        String c=message.substring(1,2);
        Log.e("messg",r);
        Log.e("messg",c);

        int row = Integer.valueOf(r);
        int cl = Integer.valueOf(c);
        if(x==1){
            a[row][cl]=1;
        }
        else {
            a[row][cl]=2;
        }
        invalidate();
        check();
    }


}