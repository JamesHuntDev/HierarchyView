package me.jameshunt.hierarchyexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.jameshunt.hierarchyexample.common.CommonActivity;
import me.jameshunt.hierarchyexample.nested.NestedActivity;

/**
 * Created by James on 8/28/2017.
 */

public class SelectExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_example);

        findViewById(R.id.common).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectExampleActivity.this, CommonActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.nested).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectExampleActivity.this, NestedActivity.class);
                startActivity(intent);
            }
        });
    }
}
