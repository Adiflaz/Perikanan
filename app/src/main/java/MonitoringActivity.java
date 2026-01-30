package com.adif.budidaya;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MonitoringActivity extends AppCompatActivity {
    
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        
        // Komponen
        EditText etTanggal = findViewById(R.id.et_tanggal);
        Spinner spinnerKegiatan = findViewById(R.id.spinner_kegiatan);
        EditText etCatatan = findViewById(R.id.et_catatan);
        EditText etSuhu = findViewById(R.id.et_suhu);
        EditText etpH = findViewById(R.id.et_ph);
        EditText etOksigen = findViewById(R.id.et_oksigen);
        EditText etAmonia = findViewById(R.id.et_amonia);
        Button btnSimpan = findViewById(R.id.btn_simpan);
        Button btnLihat = findViewById(R.id.btn_lihat);
        TextView tvRiwayat = findViewById(R.id.tv_riwayat);
        
        // Set tanggal hari ini
        etTanggal.setText(dateFormat.format(calendar.getTime()));
        
        // DatePicker untuk tanggal
        etTanggal.setOnClickListener(v -> showDatePicker());
        
        // Setup spinner kegiatan
        String[] kegiatanItems = {
            "PEMBERIAN PAKAN",
            "PENGAWASAN KESEHATAN",
            "PENGUKURAN KUALITAS AIR",
            "PENGGANTIAN AIR",
            "PEMBERIAN VITAMIN",
            "PEMBERSIHAN KOLAM",
            "SORTIR IKAN",
            "LAINNYA"
        };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, kegiatanItems);
        spinnerKegiatan.setAdapter(adapter);
        
        // Tombol Simpan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanMonitoring();
            }
        });
        
        // Tombol Lihat Riwayat
        btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lihatRiwayat();
            }
        });
    }
    
    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                EditText etTanggal = findViewById(R.id.et_tanggal);
                etTanggal.setText(dateFormat.format(calendar.getTime()));
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }
    
    private void simpanMonitoring() {
        try {
            // Ambil data
            EditText etTanggal = findViewById(R.id.et_tanggal);
            Spinner spinnerKegiatan = findViewById(R.id.spinner_kegiatan);
            EditText etCatatan = findViewById(R.id.et_catatan);
            EditText etSuhu = findViewById(R.id.et_suhu);
            EditText etpH = findViewById(R.id.et_ph);
            EditText etOksigen = findViewById(R.id.et_oksigen);
            EditText etAmonia = findViewById(R.id.et_amonia);
            TextView tvRiwayat = findViewById(R.id.tv_riwayat);
            
            String tanggal = etTanggal.getText().toString();
            String kegiatan = spinnerKegiatan.getSelectedItem().toString();
            String catatan = etCatatan.getText().toString();
            String suhu = etSuhu.getText().toString();
            String pH = etpH.getText().toString();
            String oksigen = etOksigen.getText().toString();
            String amonia = etAmonia.getText().toString();
            
            // Validasi
            if (tanggal.isEmpty() || kegiatan.isEmpty()) {
                Toast.makeText(this, "Tanggal dan kegiatan harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Format data
            String data = String.format(
                "📅 %s\n" +
                "• Kegiatan: %s\n" +
                "• Suhu Air: %s°C\n" +
                "• pH: %s\n" +
                "• Oksigen: %s mg/L\n" +
                "• Amonia: %s mg/L\n" +
                "• Catatan: %s\n" +
                "────────────────────\n",
                
                tanggal, kegiatan,
                suhu.isEmpty() ? "-" : suhu,
                pH.isEmpty() ? "-" : pH,
                oksigen.isEmpty() ? "-" : oksigen,
                amonia.isEmpty() ? "-" : amonia,
                catatan.isEmpty() ? "Tidak ada catatan" : catatan
            );
            
            // Tambah ke riwayat
            String riwayatLama = tvRiwayat.getText().toString();
            tvRiwayat.setText(data + riwayatLama);
            
            // Clear form
            etCatatan.setText("");
            etSuhu.setText("");
            etpH.setText("");
            etOksigen.setText("");
            etAmonia.setText("");
            
            Toast.makeText(this, "Data monitoring disimpan!", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void lihatRiwayat() {
        TextView tvRiwayat = findViewById(R.id.tv_riwayat);
        String riwayat = tvRiwayat.getText().toString();
        
        if (riwayat.isEmpty()) {
            tvRiwayat.setText("Belum ada data monitoring.\nMulai dengan mengisi form di atas.");
        }
    }
}