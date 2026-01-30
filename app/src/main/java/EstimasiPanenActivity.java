package com.adif.budidaya;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.HashMap;

public class EstimasiPanenActivity extends AppCompatActivity {
    
    private HashMap<String, Double[]> ikanDatabase = new HashMap<>();
    private HashMap<String, Double[]> kolamDatabase = new HashMap<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimasi_panen);
        
        initDatabase();
        
        // Komponen
        Spinner spinnerIkan = findViewById(R.id.spinner_ikan);
        Spinner spinnerKolam = findViewById(R.id.spinner_kolam);
        EditText etJumlahBibit = findViewById(R.id.et_jumlah_bibit);
        EditText etBeratBibit = findViewById(R.id.et_berat_bibit);
        EditText etSurvival = findViewById(R.id.et_survival);
        EditText etFCR = findViewById(R.id.et_fcr);
        EditText etSuhu = findViewById(R.id.et_suhu);
        Button btnHitung = findViewById(R.id.btn_hitung);
        TextView tvHasil = findViewById(R.id.tv_hasil);
        TextView tvTanggal = findViewById(R.id.tv_tanggal);
        
        // Setup spinner
        String[] jenisIkan = {"Lele", "Nila", "Gurame", "Mas", "Bawal", "Patin"};
        String[] jenisKolam = {"Beton", "Terpal", "Tanah", "Karet", "Fiberglass"};
        
        ArrayAdapter<String> adapterIkan = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, jenisIkan);
        ArrayAdapter<String> adapterKolam = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, jenisKolam);
        
        spinnerIkan.setAdapter(adapterIkan);
        spinnerKolam.setAdapter(adapterKolam);
        
        // Set default FCR berdasarkan ikan
        spinnerIkan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ikan = spinnerIkan.getSelectedItem().toString();
                double fcrDefault = getFCRDefault(ikan);
                etFCR.setText(String.valueOf(fcrDefault));
                
                // Set default suhu optimal
                Double[] dataIkan = ikanDatabase.get(ikan);
                if (dataIkan != null) {
                    etSuhu.setText(String.valueOf(dataIkan[3]));
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungEstimasiPanen();
            }
        });
    }
    
    private void initDatabase() {
        // Format: {berat_panen_kg, hari_min, hari_max, suhu_optimal, fcr_default}
        ikanDatabase.put("Lele", new Double[]{0.25, 45.0, 60.0, 28.0, 1.2});
        ikanDatabase.put("Nila", new Double[]{0.35, 90.0, 120.0, 28.0, 1.5});
        ikanDatabase.put("Gurame", new Double[]{0.50, 180.0, 240.0, 28.0, 1.8});
        ikanDatabase.put("Mas", new Double[]{0.30, 90.0, 120.0, 25.0, 1.6});
        ikanDatabase.put("Bawal", new Double[]{0.40, 90.0, 120.0, 28.0, 1.4});
        ikanDatabase.put("Patin", new Double[]{0.60, 120.0, 150.0, 28.0, 1.3});
        
        // Format: {efisiensi, risiko, kebutuhan_air, biaya_per_m2}
        kolamDatabase.put("Beton", new Double[]{1.0, 0.05, 1.0, 350000.0});
        kolamDatabase.put("Terpal", new Double[]{0.95, 0.08, 1.2, 150000.0});
        kolamDatabase.put("Tanah", new Double[]{0.90, 0.10, 0.8, 80000.0});
        kolamDatabase.put("Karet", new Double[]{0.98, 0.06, 1.1, 250000.0});
        kolamDatabase.put("Fiberglass", new Double[]{1.02, 0.04, 0.9, 500000.0});
    }
    
    private double getFCRDefault(String ikan) {
        Double[] data = ikanDatabase.get(ikan);
        return data != null ? data[4] : 1.5;
    }
    
    private void hitungEstimasiPanen() {
        try {
            // Ambil input
            Spinner spinnerIkan = findViewById(R.id.spinner_ikan);
            Spinner spinnerKolam = findViewById(R.id.spinner_kolam);
            EditText etJumlahBibit = findViewById(R.id.et_jumlah_bibit);
            EditText etBeratBibit = findViewById(R.id.et_berat_bibit);
            EditText etSurvival = findViewById(R.id.et_survival);
            EditText etFCR = findViewById(R.id.et_fcr);
            EditText etSuhu = findViewById(R.id.et_suhu);
            TextView tvHasil = findViewById(R.id.tv_hasil);
            TextView tvTanggal = findViewById(R.id.tv_tanggal);
            
            String ikan = spinnerIkan.getSelectedItem().toString();
            String kolam = spinnerKolam.getSelectedItem().toString();
            
            if (etJumlahBibit.getText().toString().isEmpty() ||
                etBeratBibit.getText().toString().isEmpty() ||
                etSurvival.getText().toString().isEmpty() ||
                etFCR.getText().toString().isEmpty() ||
                etSuhu.getText().toString().isEmpty()) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int jumlahBibit = Integer.parseInt(etJumlahBibit.getText().toString());
            double beratBibit = Double.parseDouble(etBeratBibit.getText().toString()) / 1000; // ke kg
            double survival = Double.parseDouble(etSurvival.getText().toString());
            double fcr = Double.parseDouble(etFCR.getText().toString());
            double suhu = Double.parseDouble(etSuhu.getText().toString());
            
            // Data dari database
            Double[] dataIkan = ikanDatabase.get(ikan);
            Double[] dataKolam = kolamDatabase.get(kolam);
            
            // Perhitungan
            double ikanHidup = jumlahBibit * (survival / 100);
            double efisiensiKolam = dataKolam[0];
            double suhuOptimal = dataIkan[3];
            
            // Faktor suhu
            double faktorSuhu = 1.0;
            if (suhu < suhuOptimal - 2) faktorSuhu = 0.8;
            else if (suhu > suhuOptimal + 2) faktorSuhu = 0.9;
            
            // Berat panen per ekor
            double beratPanenPerEkor = dataIkan[0] * efisiensiKolam * faktorSuhu;
            
            // Total berat panen
            double totalBeratPanen = ikanHidup * beratPanenPerEkor;
            
            // Estimasi waktu panen
            double hariRata = (dataIkan[1] + dataIkan[2]) / 2;
            if (suhu > suhuOptimal) hariRata *= 0.9;
            if (suhu < suhuOptimal) hariRata *= 1.1;
            
            // Kebutuhan pakan
            double pertumbuhan = beratPanenPerEkor - beratBibit;
            double totalPakan = pertumbuhan * ikanHidup * fcr;
            
            // Tanggal panen
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, (int) hariRata);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
            String tanggalPanen = sdf.format(cal.getTime());
            
            // Hasil
            String hasil = String.format(
                "ESTIMASI PANEN %s\n" +
                "====================\n\n" +
                "DATA TEKNIS:\n" +
                "• Jenis Ikan: %s\n" +
                "• Jenis Kolam: %s\n" +
                "• Jumlah Bibit: %,d ekor\n" +
                "• Survival Rate: %.1f%%\n" +
                "• FCR: %.2f\n" +
                "• Suhu Air: %.1f°C\n\n" +
                "HASIL ESTIMASI:\n" +
                "• Ikan Hidup: %,.0f ekor\n" +
                "• Berat Panen/Ekor: %.3f kg\n" +
                "• Total Berat Panen: %,.2f kg\n" +
                "• Estimasi Waktu: %,.0f hari\n" +
                "• Kebutuhan Pakan: %,.2f kg\n\n" +
                "FAKTOR PENGARUH:\n" +
                "• Efisiensi Kolam: %.1f%%\n" +
                "• Faktor Suhu: %.1f%%\n" +
                "• Total Air: %,.1f m³",
                
                ikan.toUpperCase(),
                ikan, kolam, jumlahBibit, survival, fcr, suhu,
                ikanHidup, beratPanenPerEkor, totalBeratPanen,
                hariRata, totalPakan,
                efisiensiKolam * 100, faktorSuhu * 100,
                totalBeratPanen * 0.001 // asumsi 1 kg ikan ≈ 1 liter air
            );
            
            tvHasil.setText(hasil);
            tvTanggal.setText("Perkiraan Panen: " + tanggalPanen);
            
        } catch (Exception e) {
            Toast.makeText(this, "Error dalam perhitungan!", Toast.LENGTH_SHORT).show();
        }
    }
}