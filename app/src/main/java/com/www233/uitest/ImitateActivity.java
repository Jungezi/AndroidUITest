package com.www233.uitest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.www233.uitest.tabletest.TableActivity;

public class ImitateActivity extends AppCompatActivity {

    private static final String TAG = "ImitateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_imitate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.e(TAG, "onCreate: done.");
    }

    public void MoreList(View view) {
        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }

    int num = 1;
    public void showViewStub(View view) {
        ViewStub vs = null;
        switch (num)
        {
            case 1:
                vs = findViewById(R.id.vs1);num++;
                break;
            case 2:
                vs = findViewById(R.id.vs2);num++;
                break;
            case 3:
                vs = findViewById(R.id.vs3);num++;
                break;
            case 4:
                vs = findViewById(R.id.vs4);num++;
                break;
        }
        if(vs != null)
        {
            vs.inflate();
        }
    }

//    public void dialog(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("标题")
//                .setMessage("内容")
//                        .setNegativeButton("name", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//        builder.create().show();
//
//    }
}