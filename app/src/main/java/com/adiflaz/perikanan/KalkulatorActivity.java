package com.adif.budidaya;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.text.DecimalFormat;
import java.util.HashMap;

public class KalkulatorActivity extends AppCompatActivity {
    
    // Database kebutuhan protein
    private HashMap<String, Double[]> proteinDatabase = new HashMap<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalkulator);
        
        // Inisialisasi database
        initDatabase();
        
        // Komponen UI
        Spinner spinnerIkan = findViewById(R.id.spinner_ikan);
        Spinner spinnerFase = findViewById(R.id.spinner_fase);
        Spinner spinnerPakan = findViewById(R.id.spinner_pakan);
        EditText etKadarProtein = findViewById(R.id.et_kadar_protein);
        EditText etJumlahIkan = findViewById(R.id.et_jumlah_ikan);
        EditText etBerat = findViewById(R.id.et_berat);
        EditText etHargaPakan = findViewById(R.id.et_harga_pakan);
        Button btnHitung = findViewById(R.id.btn_hitung);
        Button btnReset = findViewById(R.id.btn_reset);
        TextView tvHasil = findViewById(R.id.tv_hasil);
        
        // Setup spinner
        String[] jenisIkan = {"Lele", "Nila", "Gurame", "Mas", "Bawal", "Patin"};
        String[] fasePertumbuhan = {"Larva (0-30 hari)", "Juvenil (31-60 hari)", "Pembesaran (61+ hari)"};
        String[] jenisPakan = {
            "Pelet 28% Protein",
            "Pelet 32% Protein", 
            "Pelet 35% Protein",
            "Pelet 40% Protein",
            "Pakan Custom (Input manual)"
        };
        
        ArrayAdapter<String> adapterIkan = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, jenisIkan);
        ArrayAdapter<String> adapterFase = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, fasePertumbuhan);
        ArrayAdapter<String> adapterPakan = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, jenisPakan);
        
        spinnerIkan.setAdapter(adapterIkan);
        spinnerFase.setAdapter(adapterFase);
        spinnerPakan.setAdapter(adapterPakan);
        
        // Tampilkan/sembunyikan input kadar protein
        spinnerPakan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etKadarProtein.setVisibility(position == 4 ? View.VISIBLE : View.GONE);
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        // Tombol Hitung
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungKebutuhanPakan();
            }
        });
        
        // Tombol Reset
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerIkan.setSelection(0);
                spinnerFase.setSelection(0);
                spinnerPakan.setSelection(0);
                etKadarProtein.setText("");
                etJumlahIkan.setText("");
                etBerat.setText("");
                etHargaPakan.setText("");
                tvHasil.setText("Hasil akan ditampilkan di sini");
                etKadarProtein.setVisibility(View.GONE);
            }
        });
    }
    
    private void initDatabase() {
        // Format: {protein_larva, protein_juvenil, protein_pembesaran}
        proteinDatabase.put("Lele", new Double[]{48.0, 37.5, 30.0});
        proteinDatabase.put("Nila", new Double[]{42.5, 33.5, 26.5});
        proteinDatabase.put("Gurame", new Double[]{37.5, 31.0, 26.5});
        proteinDatabase.put("Mas", new Double[]{40.0, 32.5, 27.5});
        proteinDatabase.put("Bawal", new Double[]{42.5, 36.5, 31.0});
        proteinDatabase.put("Patin", new Double[]{47.5, 40.0, 33.5});
    }
    
    private void hitungKebutuhanPakan() {
        try {
            // Ambil komponen
            Spinner spinnerIkan = findViewById(R.id.spinner_ikan);
            Spinner spinnerFase = findViewById(R.id.spinner_fase);
            Spinner spinnerPakan = findViewById(R.id.spinner_pakan);
            EditText etKadarProtein = findViewById(R.id.et_kadar_protein);
            EditText etJumlahIkan = findViewById(R.id.et_jumlah_ikan);
            EditText etBerat = findViewById(R.id.et_berat);
            EditText etHargaPakan = findViewById(R.id.et_harga_pakan);
            TextView tvHasil = findViewById(R.id.tv_hasil);
            
            // Ambil input
            String ikan = spinnerIkan.getSelectedItem().toString();
            int faseIndex = spinnerFase.getSelectedItemPosition();
            int pakanIndex = spinnerPakan.getSelectedItemPosition();
            
            // Validasi input
            if (etJumlahIkan.getText().toString().isEmpty() ||
                etBerat.getText().toString().isEmpty() ||
                etHargaPakan.getText().toString().isEmpty()) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int jumlahIkan = Integer.parseInt(etJumlahIkan.getText().toString());
            double berat = Double.parseDouble(etBerat.getText().toString()); // dalam gram
            double hargaPakan = Double.parseDouble(etHargaPakan.getText().toString()); // per kg
            
            // Tentukan kadar protein pakan
            double kadarProteinPakan;
            if (pakanIndex == 4) { // Pakan custom
                if (etKadarProtein.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Masukkan kadar protein pakan!", Toast.LENGTH_SHORT).show();
                    return;
                }
                kadarProteinPakan = Double.parseDouble(etKadarProtein.getText().toString());
            } else {
                // Protein sesuai pilihan
                double[] proteinPakan = {28.0, 32.0, 35.0, 40.0};
                kadarProteinPakan = proteinPakan[pakanIndex];
            }
            
            // Tentukan kebutuhan protein ikan
            Double[] kebutuhanIkan = proteinDatabase.get(ikan);
            double kebutuhanProtein = kebutuhanIkan[faseIndex]; // dalam %
            
            // PERHITUNGAN DETAIL
            // 1. Kebutuhan protein per ekor per hari (gram)
            double proteinPerEkor = (kebutuhanProtein / 100) * berat;
            
            // 2. Kebutuhan pakan per ekor per hari (gram)
            double pakanPerEkor = (proteinPerEkor / (kadarProteinPakan / 100));
            
            // 3. Total pakan per hari (gram)
            double totalPakanGram = pakanPerEkor * jumlahIkan;
            double totalPakanKg = totalPakanGram / 1000;
            
            // 4. Total pakan per bulan (30 hari)
            double totalPakanBulan = totalPakanKg * 30;
            
            // 5. Biaya pakan per hari
            double biayaPakanHari = totalPakanKg * hargaPakan;
            
            // 6. Biaya pakan per bulan
            double biayaPakanBulan = biayaPakanHari * 30;
            
            // 7. FCR (Feed Conversion Ratio) estimasi
            double pertumbuhanHarian = pakanPerEkor * 0.3; // asumsi 30% konversi
            double fcr = pakanPerEkor / pertumbuhanHarian;
            
            // Format angka
            DecimalFormat df = new DecimalFormat("#,##0.00");
            DecimalFormat dfGram = new DecimalFormat("#,##0");
            
            // Tampilkan hasil
            String hasil = String.format(
                "HASIL PERHITUNGAN PAKAN\n" +
                "=====================\n\n" +
                "DATA INPUT:\n" +
                "• Jenis Ikan: %s\n" +
                "• Fase: %s\n" +
                "• Kadar Protein Pakan: %.1f%%\n" +
                "• Jumlah Ikan: %s ekor\n" +
                "• Berat Rata: %s gram\n" +
                "• Harga Pakan: Rp %s/kg\n\n" +
                "HASIL PERHITUNGAN:\n\n" +
                "Kebutuhan Protein:\n" +
                "• Kebutuhan Protein Ikan: %.1f%%\n" +
                "• Protein per Ekor/Hari: %s gram\n\n" +
                "Kebutuhan Pakan:\n" +
                "• Pakan per Ekor/Hari: %s gram\n" +
                "• Total Pakan/Hari: %s kg\n" +
                "• Total Pakan/Bulan: %s kg\n\n" +
                "Biaya Pakan:\n" +
                "• Biaya/Hari: Rp %s\n" +
                "• Biaya/Bulan: Rp %s\n\n" +
                "Parameter Teknis:\n" +
                "• Estimasi FCR: %.2f\n" +
                "• Pertumbuhan/Hari: %.2f gram\n\n" +
                "REKOMENDASI:\n" +
                "• Berikan pakan 3-4 kali sehari\n" +
                "• Pantau sisa pakan di dasar kolam\n" +
                "• Sesuaikan pakan dengan suhu air",
                
                ikan,
                spinnerFase.getSelectedItem().toString(),
                kadarProteinPakan,
                dfGram.format(jumlahIkan),
                dfGram.format(berat),
                df.format(hargaPakan),
                
                kebutuhanProtein,
                df.format(proteinPerEkor),
                
                df.format(pakanPerEkor),
                df.format(totalPakanKg),
                df.format(totalPakanBulan),
                
                df.format(biayaPakanHari),
                df.format(biayaPakanBulan),
                
                fcr,
                pertumbuhanHarian
            );
            
            tvHasil.setText(hasil);
            
        } catch (Exception e) {
            Toast.makeText(this, "Terjadi kesalahan dalam perhitungan!", Toast.LENGTH_SHORT).show();
        }
    }
}