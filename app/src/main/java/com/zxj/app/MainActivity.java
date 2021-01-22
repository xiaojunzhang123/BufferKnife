package com.zxj.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zxj.annotations.BindView;
import com.zxj.bufferknife.BufferKnife;

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
    }
}