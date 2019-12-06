package com.example.countdown;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<CharSequence> adapter;
    private Spinner sp1,sp2,sp3,sp4;
    private TextView t1,t2,t3,t4,now;
    private int n1,n2,n3,n4,count,begin,p;
    private Button btn1,btn2,btn3;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private MediaPlayer player = new MediaPlayer();
    private ProgressBar plan;
    private TextView percent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.help_item:
                Toast.makeText(this,"此为帮助", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit_item:
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        sp1 = (Spinner)findViewById(R.id.spinner1);
        sp2 = (Spinner)findViewById(R.id.spinner2);
        sp3 = (Spinner)findViewById(R.id.spinner3);
        sp4 = (Spinner)findViewById(R.id.spinner4);
        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.hour,
                android.R.layout.simple_spinner_item);
        sp1.setAdapter(adapter);
        sp2.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.minute,
                android.R.layout.simple_spinner_item);
        sp3.setAdapter(adapter);
        sp4.setAdapter(adapter);
        t1 = findViewById(R.id.day);
        t2 = findViewById(R.id.hour);
        t3 = findViewById(R.id.minute);
        t4 = findViewById(R.id.second);
        now = findViewById(R.id.textView);
        plan=findViewById(R.id.plan);
        percent=findViewById(R.id.perc);
        initMediaPlayer();
        btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(!btn3.getText().equals("暂停"))
                    btn3.setText("暂停");
                btn3.setEnabled(false);
                sp1.setVisibility(View.VISIBLE);
                sp2.setVisibility(View.VISIBLE);
                sp3.setVisibility(View.VISIBLE);
                sp4.setVisibility(View.VISIBLE);
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
                t3.setVisibility(View.VISIBLE);
                t4.setVisibility(View.VISIBLE);
                sp1.setSelection(0);
                sp2.setSelection(0);
                sp3.setSelection(0);
                sp4.setSelection(0);
                stopTime();
                plan.setProgress(0);
                percent.setText(0+"%");
                if(player.isPlaying()) {
                    player.reset();
                    initMediaPlayer();
                }
                else{
                    player.start();
                    player.reset();
                    initMediaPlayer();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                btn2.setEnabled(false);
                btn3.setEnabled(true);
                if(!btn3.getText().equals("暂停"))
                    btn3.setText("暂停");
                sp1.setVisibility(View.INVISIBLE);
                sp2.setVisibility(View.INVISIBLE);
                sp3.setVisibility(View.INVISIBLE);
                sp4.setVisibility(View.INVISIBLE);
                t1.setVisibility(View.INVISIBLE);
                t2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
                t4.setVisibility(View.INVISIBLE);
                startTime();
                if(!player.isPlaying())
                    player.start();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(btn3.getText().equals("暂停")) {
                    btn3.setText("继续");
                    stopTime();
                    if(player.isPlaying())
                        player.pause();
                }
                else {
                    btn3.setText("暂停");
                    startTime();
                    if(!player.isPlaying())
                        player.start();
                }
            }
        });
        sp1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                change();
            }
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
        sp2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                change();
            }
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
        sp3.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                change();
            }
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
        sp4.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                change();
            }
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
    }

    private void change(){
        n1 = Integer.parseInt((String)sp1.getSelectedItem());
        n2 = Integer.parseInt((String)sp2.getSelectedItem());
        n3 = Integer.parseInt((String)sp3.getSelectedItem());
        n4 = Integer.parseInt((String)sp4.getSelectedItem());
        StringBuilder str = new StringBuilder();
        count = n1*86400+n2*3600+n3*60+n4;
        count = count * 10;
        begin = count;
        plan.setMax(begin);
        if(count==0){
            str.append("00:00:00");
            btn2.setEnabled(false);
            now.setText(str);
        }
        else {
            btn2.setEnabled(true);
            setNow(n1, n2, n3, n4);
        }
    }

    private void setNow(int n1,int n2,int n3,int n4){
        StringBuilder str = new StringBuilder();
        if (n1 != 0)
            str.append(n1 + "天");
        if (n2 < 10)
            str.append("0" + n2 + ":");
        else
            str.append(n2 + ":");
        if (n3 < 10)
            str.append("0" + n3 + ":");
        else
            str.append(n3 + ":");
        if (n4 < 10)
            str.append("0" + n4);
        else
            str.append(n4 + "");
        now.setText(str);
    }

    private void setNow(int n){
        int n1 = n / 86400;
        int n2 = n % 86400 / 3600;
        int n3 = n % 3600 / 60;
        int n4 = n % 60 ;
        setNow(n1,n2,n3,n4);
    }

    private void startTime() {
        if(timer==null){
            timer = new Timer();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                count--;
                setNow(count/10);
                p = (begin-count)*100/begin;
                plan.setProgress(begin-count);
                percent.setText(p+"%");
                if(count==0){
                    timer.cancel();
                    timer = null;
                    if(!player.isPlaying()) {
                        player.start();
                    }
                    player.seekTo(5000);
                }
                else if(begin-50==count){
                    player.reset();
                    initMediaPlayer();
                }
            }
        };
        timer.schedule(timerTask, 100,100);
    }

    private void stopTime() {
        if(timer!=null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initMediaPlayer(){
        try{
            player = MediaPlayer.create(this, R.raw.count);
            player.prepare();
        }
        catch(Exception e){ }
    }

}