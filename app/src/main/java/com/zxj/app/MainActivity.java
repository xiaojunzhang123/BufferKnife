package com.zxj.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zxj.annotations.BindView;
import com.zxj.bufferknife.BufferKnife;
import com.zxj.bufferknife.annotation.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BufferKnife.inject(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"aaaa",Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }


    @OnClick(values = {R.id.btn1})
    public void onClicked(View v){
        Toast.makeText(MainActivity.this,"bbbb",Toast.LENGTH_SHORT).show();
    }
}