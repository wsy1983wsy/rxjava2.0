package cn.wangsye.myrxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.wangsye.myrxjava.chapter02.Chapter02Activity;
import cn.wangsye.myrxjava.chapter04.OperatorActivity;
import cn.wangsye.myrxjava.thread.TheadActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void create() {

    }

    public void onChapter02Clicked(View view) {
        Intent intent = new Intent(this, Chapter02Activity.class);
        startActivity(intent);
    }

    public void onThreadClicke(View view) {
        Intent intent = new Intent(this, TheadActivity.class);
        startActivity(intent);
    }

    public void onFourClicke(View view) {
        Intent intent = new Intent(this, OperatorActivity.class);
        startActivity(intent);
    }

}
