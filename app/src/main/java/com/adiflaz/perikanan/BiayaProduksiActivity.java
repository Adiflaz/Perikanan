package com.adif.budidaya;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import java.text.DecimalFormat;
import java.util.HashMap;

public class BiayaProduksiActivity extends AppCompatActivity {
    
    private HashMap<String, Double[]> kolamDatabase = new HashMap<>();
    private DecimalFormat df = new DecimalFormat("#,##0");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biaya_produksi);
        
        initDatabase();
        
        // Komponen
        Spinner spinnerKolam = findViewById(R.id.spinner_kolam);
        EditText etLuas = findViewById(R.id.et_luas);
        EditText etKedalaman = findViewById(R.id.et_kedalaman);
        EditText etBenih = findViewById(R.id.et_benih);
        EditText etPakan = findViewById(R.id.et_pakan);
        EditText etVitamin = findViewById(R.id.et_vitamin);
        EditText etListrik = findViewById(R.id.et_listrik);
        EditText etTenaga = findViewById(R.id.et_tenaga);
        EditText etLain = findViewById(R.id.et_lain);
        EditText etWaktu = findViewById(R.id.et_waktu);
        Button btnHitung = findViewById(R.id.btn_hitung);
        TextView tvHasil = findViewById(R.id.tv_hasil);
        TextView tvVolume = findViewById(R.id.tv_volume);
        
        // Setup spinner
        String[] jenisKolam = {"Beton", "Terpal", "Tanah", "Karet", "Fiberglass"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, jenisKolam);
        spinnerKolam.setAdapter(adapter);
        
        // Hitung volume otomatis
        TextWatcher volumeWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitungVolume();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        };
        
        etLuas.addTextChangedListener(volumeWatcher);
        etKedalaman.addTextChangedListener(volumeWatcher);
        
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungBiayaProduksi();
            }
        });
    }
    
    private void initDatabase() {
        // Format: {biaya_per_m2, umur_teknis, perawatan_per_bulan, risiko}
        kolamDatabase.put("Beton", new Double[]{350000.0, 10.0, 50000.0, 0.05});
        kolamDatabase.put("Terpal", new Double[]{150000.0, 3.0, 20000.0, 0.08});
        kolamDatabase.put("Tanah", new Double[]{80000.0, 5.0, 30000.0, 0.10});
        kolamDatabase.put("Karet", new Double[]{250000.0, 8.0, 40000.0, 0.06});
        kolamDatabase.put("Fiberglass", new Double[]{500000.0, 15.0, 60000.0, 0.04});
    }
    
    private void hitungVolume() {
        try {
            EditText etLuas = findViewById(R.id.et_luas);
            EditText etKedalaman = findViewById(R.id.et_kedalaman);
            TextView tvVolume = findViewById(R.id.tv_volume);
            
            if (!etLuas.getText().toString().isEmpty() && 
                !etKedalaman.getText().toString().isEmpty()) {
                
                double luas = Double.parseDouble(etLuas.getText().toString());
                double kedalaman = Double.parseDouble(etKedalaman.getText().toString());
                double volume = luas * kedalaman;
                
                tvVolume.setText("Volume Air: " + df.format(volume) + " m³");
            }
        } catch (Exception e) {
            // Ignore
        }
    }
    
    private void hitungBiayaProduksi() {
        try {
            // Ambil input
            Spinner spinnerKolam = findViewById(R.id.spinner_kolam);
            EditText etLuas = findViewById(R.id.et_luas);
            EditText etKedalaman = findViewById(R.id.et_kedalaman);
            EditText etBenih = findViewById(R.id.et_benih);
            EditText etPakan = findViewById(R.id.et_pakan);
            EditText etVitamin = findViewById(R.id.et_vitamin);
            EditText etListrik = findViewById(R.id.et_listrik);
            EditText etTenaga = findViewById(R.id.et_tenaga);
            EditText etLain = findViewById(R.id.et_lain);
            EditText etWaktu = findViewById(R.id.et_waktu);
            TextView tvHasil = findViewById(R.id.tv_hasil);
            
            // Validasi
            if (etLuas.getText().toString().isEmpty() ||
                etKedalaman.getText().toString().isEmpty() ||
                etBenih.getText().toString().isEmpty() ||
                etPakan.getText().toString().isEmpty() ||
                etWaktu.getText().toString().isEmpty()) {
                
                Toast.makeText(this, "Isi data utama terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String kolam = spinnerKolam.getSelectedItem().toString();
            double luas = Double.parseDouble(etLuas.getText().toString());
            double kedalaman = Double.parseDouble(etKedalaman.getText().toString());
            double biayaBenih = parseDouble(etBenih.getText().toString());
            double biayaPakan = parseDouble(etPakan.getText().toString());
            double biayaVitamin = parseDouble(etVitamin.getText().toString());
            double biayaListrik = parseDouble(etListrik.getText().toString());
            double biayaTenaga = parseDouble(etTenaga.getText().toString());
            double biayaLain = parseDouble(etLain.getText().toString());
            int waktuBulan = Integer.parseInt(etWaktu.getText().toString());
            
            // Data kolam
            Double[] dataKolam = kolamDatabase.get(kolam);
            
            // Perhitungan
            double volume = luas * kedalaman;
            double biayaKonstruksi = dataKolam[0] * luas;
            double penyusutan = (biayaKonstruksi / (dataKolam[1] * 12)) * waktuBulan;
            double perawatan = dataKolam[2] * waktuBulan;
            
            // Biaya variabel
            double totalVariabel = biayaBenih + biayaPakan + biayaVitamin + 
                                 biayaListrik + biayaTenaga + biayaLain;
            
            // Biaya tetap
            double biayaAdmin = totalVariabel * 0.05; // 5% administrasi
            double totalTetap = penyusutan + perawatan + biayaAdmin;
            
            // Total biaya
            double totalBiaya = totalVariabel + totalTetap;
            
            // Biaya per m³ air
            double biayaPerM3 = totalBiaya / volume;
            
            // Hasil
            String hasil = String.format(
                "ANALISIS BIAYA PRODUKSI\n" +
                "=======================\n\n" +
                "DATA TEKNIS KOLAM:\n" +
                "• Jenis Kolam: %s\n" +
                "• Luas Kolam: %,.1f m²\n" +
                "• Kedalaman: %,.1f m\n" +
                "• Volume Air: %,.1f m³\n" +
                "• Waktu Produksi: %d bulan\n\n" +
                "BIAYA VARIABEL:\n" +
                "• Benih/Bibit: Rp %s\n" +
                "• Pakan: Rp %s\n" +
                "• Vitamin & Obat: Rp %s\n" +
                "• Listrik & BBM: Rp %s\n" +
                "• Tenaga Kerja: Rp %s\n" +
                "• Lain-lain: Rp %s\n" +
                "➤ TOTAL VARIABEL: Rp %s\n\n" +
                "BIAYA TETAP:\n" +
                "• Penyusutan Kolam: Rp %s\n" +
                "• Perawatan: Rp %s\n" +
                "• Administrasi (5%%): Rp %s\n" +
                "➤ TOTAL TETAP: Rp %s\n\n" +
                "TOTAL BIAYA PRODUKSI:\n" +
                "➤ Rp %s\n\n" +
                "ANALISIS:\n" +
                "• Biaya per m³ Air: Rp %s\n" +
                "• Risiko Kegagalan: %.1f%%\n" +
                "• Umur Teknis Kolam: %,.0f tahun",
                
                kolam, luas, kedalaman, volume, waktuBulan,
                df.format(biayaBenih), df.format(biayaPakan),
                df.format(biayaVitamin), df.format(biayaListrik),
                df.format(biayaTenaga), df.format(biayaLain),
                df.format(totalVariabel),
                df.format(penyusutan), df.format(perawatan),
                df.format(biayaAdmin), df.format(totalTetap),
                df.format(totalBiaya),
                df.format(biayaPerM3),
                dataKolam[3] * 100,
                dataKolam[1]
            );
            
            tvHasil.setText(hasil);
            
        } catch (Exception e) {
            Toast.makeText(this, "Error dalam perhitungan!", Toast.LENGTH_SHORT).show();
        }
    }
    
    private double parseDouble(String text) {
        return text.isEmpty() ? 0 : Double.parseDouble(text);
    }
}