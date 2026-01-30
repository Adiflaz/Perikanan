package com.adif.budidaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Inisialisasi semua CardView
        CardView cvKalkulator = findViewById(R.id.cv_kalkulator);
        CardView cvEstimasi = findViewById(R.id.cv_estimasi);
        CardView cvBiaya = findViewById(R.id.cv_biaya);
        CardView cvKeuntungan = findViewById(R.id.cv_keuntungan);
        CardView cvDatabase = findViewById(R.id.cv_database);
        CardView cvMonitoring = findViewById(R.id.cv_monitoring);
        TextView tvCredit = findViewById(R.id.tv_credit);
        
        // Set kredit
        tvCredit.setText("Aplikasi Budidaya Ikan Air Tawar\nVersi 2.0\n© Adif Lazuardi Imani");
        
        // Set onClickListener untuk setiap menu
        cvKalkulator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KalkulatorActivity.class));
            }
        });
        
        cvEstimasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EstimasiPanenActivity.class));
            }
        });
        
        cvBiaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BiayaProduksiActivity.class));
            }
        });
        
        cvKeuntungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnalisisKeuntunganActivity.class));
            }
        });
        
        cvDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DatabaseActivity.class));
            }
        });
        
        cvMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MonitoringActivity.class));
            }
        });
    }
}