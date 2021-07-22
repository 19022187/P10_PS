package com.myapplicationdev.android.p10_ps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CheckRecords extends AppCompatActivity {

    TextView tvRecords;
    ListView lvLocation;
    ArrayAdapter aa;
    ArrayList al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_records);

        tvRecords = findViewById(R.id.tvRecords);
        lvLocation = findViewById(R.id.lvLocation);

        aa = new ArrayAdapter(CheckRecords.this, android.R.layout.simple_list_item_1, al);

        String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Folder";
        File targetFile = new File(folderLocation, "Coordinates.txt");
        if (targetFile.exists() == true) {
            String data = "";
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    data += line + "\n";
                    line = br.readLine();
                }
                br.close();
                reader.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            al.add(data);
            lvLocation.setAdapter(aa);
            tvRecords.setText(data);
        }

        lvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}