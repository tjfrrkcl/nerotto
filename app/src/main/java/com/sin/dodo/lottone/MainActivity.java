package com.sin.dodo.lottone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity  implements  OnClickListener {

    //버튼객체 참조 변수 선언
    Button[][] btn;
    Button btn1, btn2, btn3;
    //랜덤 seed 변수 선언
    long rndSeed;
    private final int DYNAMIC_VIEW_ID = 0x8000;
    private LinearLayout dynamicLayout;
    LinearLayout[] dynamicLayouts;
    List<List> lottoLists = new ArrayList<List>();
    LottoAdapter adapter;
    ArrayList<Lotto> lottoArrayList;
    DBHandler handler;
    Context mContext = this;
    int currentDrwNo = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //현재 시간을 초기 값으로 세팅 (값이 계속 바뀌어야 하기 때문에..현재 시간은 계속 흘러 가므로~~)
        rndSeed = System.currentTimeMillis();

        btn = new Button[5][6];
        dynamicLayouts = new LinearLayout[5];

        //--- 로또 번호가 담길 버튼 객체 얻기 --------//
        btn1 = (Button) findViewById(R.id.bt1);
        btn2 = (Button) findViewById(R.id.bt2);
        btn3 = (Button) findViewById(R.id.bt3);
        //생성과, 초기화 버튼 리스너 등록
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        handler = new DBHandler(this);
        if(handler.getLottoCount() == 0 )
        {
            new DataFetcherTask().execute();
        } else {

        }
//        else
//        {
//            ArrayList<Lotto> lottoList = handler.getAllLotto();
//            adapter = new LottoAdapter(MainActivity.this, lottoList);
//        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onClickRotto(View v) {
        // TODO Auto-generated method stub
        dynamicLayout = (LinearLayout)findViewById(R.id.dynamicArea);
        dynamicLayout.removeAllViews();
        lottoLists.clear();
        int numButton;
        numButton = 0;
//        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
//        sd.getPaint().setShader(new RadialGradient(60, 30, 50, Color.LTGRAY, Color.LTGRAY, Shader.TileMode.CLAMP));
        ShapeDrawable sYellow = new ShapeDrawable(new OvalShape());
        sYellow.getPaint().setShader(new RadialGradient(30, 30, 30, Color.WHITE, Color.rgb(255,215,0), Shader.TileMode.CLAMP));
        ShapeDrawable sBlue = new ShapeDrawable(new OvalShape());
        sBlue.getPaint().setShader(new RadialGradient(30, 30, 30, Color.WHITE, Color.BLUE, Shader.TileMode.CLAMP));
        ShapeDrawable sRed = new ShapeDrawable(new OvalShape());
        sRed.getPaint().setShader(new RadialGradient(30, 30, 30, Color.WHITE, Color.RED, Shader.TileMode.CLAMP));
        ShapeDrawable sBlack = new ShapeDrawable(new OvalShape());
        sBlack.getPaint().setShader(new RadialGradient(30, 30, 30, Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP));
        ShapeDrawable sGreen = new ShapeDrawable(new OvalShape());
        sGreen.getPaint().setShader(new RadialGradient(30, 30, 30, Color.WHITE, Color.rgb(50,205,50), Shader.TileMode.CLAMP));

        float[] outerR = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
        RectF inset = new RectF(0, 0, 0, 0);
        float[] innerR = new float[] { 12, 12, 0, 0, 12, 12, 0, 0 };
        ShapeDrawable recSaveButton = new ShapeDrawable(new RoundRectShape(outerR, inset, innerR));
//        recSaveButton.setIntrinsicWidth(200);
//        recSaveButton.setIntrinsicHeight(100);
//        recSaveButton.getPaint().setShader(new LinearGradient(0, 0, 50, 50, new int[] {0xFFFF0000, 0XFF00FF00, 0XFF0000FF }, null,Shader.TileMode.REPEAT));
        recSaveButton.getPaint().setShader(new RadialGradient(60, 30, 30, Color.WHITE, Color.LTGRAY, Shader.TileMode.CLAMP));

        //버튼 객체 배열 사이즈 만큼 루프를 돌면서
        //랜덤값을 뽑아내고 각각의 버튼에 숫자를 넣어준다.
        //끝내기 버튼을 눌렀을시에 종료~
        if (v == btn1) {
            for (int i = 0; i < 5; i++) {
                dynamicLayouts[i] = new LinearLayout(this);
                Set<Integer> lottoSet = new HashSet<Integer>();
                while( true ) {
                    lottoSet.add( getRand(0, 45) + 1 );
                    if( lottoSet.size() == 3 ) {
                        ArrayList<Integer> tempList = new ArrayList<Integer>(lottoSet);
                        Collections.sort(tempList);
                        if ( handler.getLottoNumber3Exists(tempList.toString()) != 0 ) lottoSet.clear();
                    } else if( lottoSet.size() == 4 ) {
                        ArrayList<Integer> tempList = new ArrayList<Integer>(lottoSet);
                        Collections.sort(tempList);
                        if ( handler.getLottoNumber4Exists(tempList.toString()) == 0 ) break;
                        else lottoSet.clear();
                    }
                }
                while( lottoSet.size() < 6 ) {
                    lottoSet.add( getRand(0, 45) + 1 );
                }
                List<Integer> lottoList = new ArrayList<Integer>(lottoSet);
                Collections.sort(lottoList);
                lottoLists.add(lottoList);

                for (int j = 0; j < 6; j++) {
                    //number을 문자열로 캐스팅~
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    //LinearLayout.LayoutParams btn_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams btn_param = new LinearLayout.LayoutParams(110, 110);
                    btn_param.rightMargin = Math.round(5 * dm.density);
                    if ( j == 0 ) btn_param.setMargins(Math.round( 10 * dm.density), 20, Math.round( 5 * dm.density), 20);
                    else btn_param.setMargins(0, 20, Math.round( 5 * dm.density), 20);
                    btn[i][j] = new Button(this, null, android.R.attr.buttonStyleSmall);
                    btn[i][j].setClickable(false);
                    btn[i][j].setHeight(10);
                    btn[i][j].setWidth(10);
                    btn[i][j].setRight(10);
                    btn[i][j].setId(DYNAMIC_VIEW_ID + (numButton++));
                    btn[i][j].setTextSize(14);
                    int number =lottoList.get(j);
                    btn[i][j].setText(String.valueOf(number));
                    btn[i][j].setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    if( number < 11 ) {
                        btn[i][j] = setBackgroundButton(btn[i][j], sYellow);
                        btn[i][j].setTextColor(Color.BLUE);
                    } else if( number < 21 ) {
                        btn[i][j] = setBackgroundButton(btn[i][j], sBlue);
                        btn[i][j].setTextColor(Color.WHITE);
                    } else if( number < 31 ) {
                        btn[i][j] = setBackgroundButton(btn[i][j], sRed);
                        btn[i][j].setTextColor(Color.WHITE);
                    } else if( number < 41 ) {
                        btn[i][j] = setBackgroundButton(btn[i][j], sBlack);
                        btn[i][j].setTextColor(Color.WHITE);
                    } else {
                        btn[i][j] = setBackgroundButton(btn[i][j], sGreen);
                        btn[i][j].setTextColor(Color.BLUE);
                    }
                    btn[i][j].setLayoutParams(btn_param);

                    dynamicLayouts[i].addView(btn[i][j]);
                }
                Button saveButton = new Button(this, null, android.R.attr.buttonStyleSmall);
                saveButton.setClickable(true);
                saveButton.setHeight(10);
                saveButton.setWidth(10);
                saveButton.setRight(10);
                saveButton.setId(DYNAMIC_VIEW_ID + (numButton++));
//                saveButton.setRight(20); //.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                saveButton.setText("저장");
                LinearLayout.LayoutParams btn_param = new LinearLayout.LayoutParams(110, 110);
//                btn_param.rightMargin = Math.round(5 * getResources().getDisplayMetrics().density);
                btn_param.setMargins(20, 20, 20, 20);
                saveButton.setLayoutParams(btn_param);
                setBackgroundButton(saveButton, recSaveButton);
//                setBackgroundButton(saveButton, sYellow);

                final int position = i;
                List<Object> sendList = new ArrayList<Object>();
                sendList.add(0, position);
                sendList.add(1, lottoList);
                sendList.add(2, recSaveButton);
                sendList.add(3, currentDrwNo);
                saveButton.setTag(sendList);
                saveButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        List<Object> sendList = (ArrayList<Object>) v.getTag();
                        int sposition = (int)sendList.get(0);
                        List<Integer> lottoList = (ArrayList<Integer>) sendList.get(1);
                        Log.d("log", "position :" + sposition);
                        Button thisButton = (Button) v;
                        if("완료".equals(thisButton.getText())) {
                            Toast.makeText(getApplicationContext(), "이미 저장 하였습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Click position:" + position, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Click position:" + lottoList, Toast.LENGTH_SHORT).show();
                            UserLotto uLotto = new UserLotto();
                            uLotto.setRecu_no(currentDrwNo);
                            uLotto.setK1(lottoList.get(0));
                            uLotto.setK2(lottoList.get(1));
                            uLotto.setK3(lottoList.get(2));
                            uLotto.setK4(lottoList.get(3));
                            uLotto.setK5(lottoList.get(4));
                            uLotto.setK6(lottoList.get(5));

                            int checkCount = handler.getUserLottoCount(uLotto);
                            if( checkCount > 0 ) {
                                Toast.makeText(getApplicationContext(), "이미 저장된 번호 입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                handler.addUserLotto(uLotto);
                            }
                            thisButton.setText("완료");
                        }
                    }
                });
                saveButton.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Button saveButton = (Button) v;
                        int action = event.getAction();
                        List<Object> sendList = (ArrayList<Object>) v.getTag();
                        ShapeDrawable recSaveButton = (ShapeDrawable)sendList.get(2);
                        if(action == MotionEvent.ACTION_DOWN) {
                            // 버튼이 눌렸을때 효과
                            saveButton.setBackgroundColor(Color.GRAY);
                        } else if (action == MotionEvent.ACTION_UP) {
                            // 버튼에서 손을 떼었을 때 효과
//                            saveButton.setBackgroundColor(Color.LTGRAY);
                            saveButton.setBackgroundDrawable(recSaveButton);
                        }
                        return false;
                    }
                });
                dynamicLayouts[i].addView(saveButton);
                dynamicLayouts[i].setBackgroundColor(Color.WHITE);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                param.setMargins(10, 10, 10, 10);
                dynamicLayouts[i].setLayoutParams(param);
                dynamicLayout.addView(dynamicLayouts[i]);
//                LinearLayout.LayoutParams sparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                sparam.gravity = Gravity.CENTER_VERTICAL;
                dynamicLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
        else if (v == btn2) {

            System.exit(0);
        }
    }

    public Button setBackgroundButton( Button btn, ShapeDrawable sd ) {
        if(android.os.Build.VERSION.SDK_INT < 16) {
            btn.setBackgroundDrawable(sd);
        }
        else {
            btn.setBackground(sd);
        }
        return btn;
    }

    //---랜덤난수~ 원하는 숫자에서~ 숫자만큼의 사이에서 발생 시키는 method!---//
    public int setRandSeed() {
        rndSeed = (rndSeed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        return (int) ((rndSeed >> (48 - 32)) & 2147483647);
    }

    public int getRand(int a, int b) {
        return ((setRandSeed() % (b - a)) + a);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.sin.dodo.lottone/http/host/path")
        );
        new WebLottoNumberGetter(this).execute("aaaa");
        Log.i("onStart", "START");
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.sin.dodo.lottone/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onClick(View v) {
        Log.i("onClick", "Start....");
        String ssss = null;
        if( v == btn3) {
            Intent intent = new Intent(getApplicationContext(), UserSaveLottoNumListScrollingActivity.class);
            startActivity(intent);
            Log.i("WebLottoNumberGetter", "End");
        } else {
            GetLottoHttp getLottoHttp = new GetLottoHttp(mContext);
            if( getLottoHttp.getConnectivityService() ) {
                try {
                    String response = getLottoHttp.getLottoNumber(0);
                    Log.d("onClick", "jsondata = " + response);
                    JSONObject jSonObject = new JSONObject(response);
                    currentDrwNo = jSonObject.getInt("drwNo");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            onClickRotto(v);
        }
    }

    class DataFetcherTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String lottoJsonData = null;// String object to store fetched data from server
            // Http Request Code start
            BufferedReader lottoBr = null;
            try {
                lottoBr = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.nanumlotto)));
                StringBuffer lottoSb = new StringBuffer();
                while( ( lottoJsonData = lottoBr.readLine() ) != null ) {
                    Log.d("first read == ", lottoJsonData);
                    lottoSb.append(lottoJsonData);
                }
                lottoJsonData = lottoSb.toString();
                Log.d("LottoJsonData = ", lottoJsonData);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try{
                    if( lottoBr != null ) lottoBr.close();
                } catch( Exception e) {
                    e.printStackTrace();
                }
            }
            // Http Request Code end
            // Json Parsing Code Start
            try {
                lottoArrayList = new ArrayList<Lotto>();
                Log.d("lottoJsonData = ", lottoJsonData);
                JSONObject jsonObject = new JSONObject(lottoJsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONArray lottoArray = jsonArray.getJSONArray(i);
                    int recu_no = lottoArray.getInt(0);
                    String com_yn = lottoArray.getString(1);
                    int k1 = lottoArray.getInt(2);
                    int k2 = lottoArray.getInt(3);
                    int k3 = lottoArray.getInt(4);
                    int k4 = lottoArray.getInt(5);
                    int k5 = lottoArray.getInt(6);
                    int k6 = lottoArray.getInt(7);
                    Lotto lotto = new Lotto();
                    lotto.setRecu_no(recu_no);
                    lotto.setCom_yn(com_yn);
                    lotto.setK1(k1);
                    lotto.setK2(k2);
                    lotto.setK3(k3);
                    lotto.setK4(k4);
                    lotto.setK5(k5);
                    lotto.setK6(k6);
                    handler.addLotto(lotto);// Inserting into DB
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Json Parsing code end
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<Lotto> cityList = handler.getAllLotto();
            adapter = new LottoAdapter(MainActivity.this,cityList);
        }
    }
}
