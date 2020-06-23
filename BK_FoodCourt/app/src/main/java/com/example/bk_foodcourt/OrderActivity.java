package com.example.bk_foodcourt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends Activity {


    TextView menu, suonBiView, suonView, suonChaView, saBiChuongView, phoVienView, phoNamView, phoTaiView;
    //TextView hptv, bptv, fptv, rptv, fiptv, dptv;
    String choices = "";
    String prices, usd_price_string;
    double usd_price = 0;
    Button comSuon, comSuonBi, comSuonCha, comSaBiChuong, phoVien, phoNam, phoTai;

    //removing button

    Button rm_comSuon, rm_comSuonBi, rm_comSuonCha, rm_phoVien, rm_comSaBiChuong, rm_phoNam, rm_phoTai, order;

    int SLComSuon = 0, SLSuonBi = 0, SLSaBiChuong = 0, SLPhoNam = 0, SLSuonCha = 0, SLPhoVien = 0, SLPhoTai;
    int ComSuonPrice = 0, SuonBiPrice = 0, SabiChuongPrice = 0, PhoNamPrice = 0, SuonChaPrice = 0, PhoVienPrice = 0, PhoTaiPrice;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        menu = findViewById(R.id.menuTv);
        suonBiView = findViewById(R.id.comSuonBi_textView);
        suonView = findViewById(R.id.comSuon_textView);
        suonChaView = findViewById(R.id.comSuonCha_textView);
        saBiChuongView = findViewById(R.id.comSaBiChuong_textView);
        phoVienView = findViewById(R.id.phoVien_textView);
        phoNamView = findViewById(R.id.phoNam_textView);
        phoTaiView = findViewById(R.id.phoTai_textView);
//button of iteams add

        comSuon = (Button) findViewById(R.id.comSuon_button);
        comSuonBi = (Button) findViewById(R.id.comSuonBi_button);
        comSuonCha = (Button) findViewById(R.id.comSuonCha_button);
        comSaBiChuong = (Button) findViewById(R.id.comSaBiChuong_button);
        phoVien = (Button) findViewById(R.id.phoVien_button);
        phoNam = (Button) findViewById(R.id.phoNam_button);
        phoTai = (Button) findViewById(R.id.phoTai_button);

        // removing button identifing

        rm_comSuon = (Button) findViewById(R.id.comSuon_button_rm);
        rm_comSuonBi = (Button) findViewById(R.id.comSuonBi_button_rm);
        rm_comSuonCha = (Button) findViewById(R.id.comSuonCha_button_rm);
        rm_phoVien = (Button) findViewById(R.id.phoVien_button_rm);
        rm_comSaBiChuong = (Button) findViewById(R.id.comSaBiChuong_button_rm);
        rm_phoNam = (Button) findViewById(R.id.phoVien_button_rm);
        rm_phoTai = (Button) findViewById(R.id.phoTai_button_rm);


        /// order Button

        order = (Button) findViewById(R.id.order_button);

/*
        hptv = findViewById(R.id.comSuonBi_price);
        bptv = findViewById(R.id.comSuon_price);
        fptv = findViewById(R.id.comSuonCha_price);
        rptv = findViewById(R.id.comSaBiChuong_price);
        fiptv = findViewById(R.id.phoVien_price);
        dptv = findViewById(R.id.phoNam_price);

        --------------------------------------------------------------------


        Typeface french_font = ResourcesCompat.getFont(this, R.font.french);
        Typeface gatholic = ResourcesCompat.getFont(this, R.font.gatholic);

        menu.setTypeface(french_font);

        suonBiView.setTypeface(french_font);
        suonView.setTypeface(french_font);
        suonChaView.setTypeface(french_font);
        saBiChuongView.setTypeface(french_font);
        phoVienView.setTypeface(french_font);
        phoNamView.setTypeface(french_font);


        hptv.setTypeface(gatholic);
        bptv.setTypeface(gatholic);
        fptv.setTypeface(gatholic);
        rptv.setTypeface(gatholic);
        fiptv.setTypeface(gatholic);
        dptv.setTypeface(gatholic);

        // setting order button font

        order.setTypeface(gatholic);


        --------------------------------------------------*/
    }


    public void place_order(View view) {

        balancesheet();
        Intent i = new Intent(OrderActivity.this, OrderDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString("choice_iteams", choices);
        bundle.putString("bdt_price", prices);
        bundle.putString("usd_price", usd_price_string);
        i.putExtras(bundle);
        startActivity(i);
        choices = "";
    }


    public void add_to_list(View view) {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor editor = prefs.edit();


        if (view == findViewById(R.id.comSuonBi_button)) {
            Toast.makeText(this, "Cơm Sườn Bì đã được thêm!!", Toast.LENGTH_SHORT).show();
            SLSuonBi++;


            comSuonBi.setText(Integer.toString(SLSuonBi));

        } else if (view == findViewById(R.id.comSuon_button)) {
            Toast.makeText(this, "Cơm Sườn đã được thêm!!", Toast.LENGTH_SHORT).show();
            // = (choices+"\nBiriyani\t (1) plate.").toString();
            SLComSuon++;

            comSuon.setText(Integer.toString(SLComSuon));

        } else if (view == findViewById(R.id.comSuonCha_button)) {
            Toast.makeText(this, "Cơm Sườn Chả đã được thêm!!", Toast.LENGTH_SHORT).show();
            //choices = (choices+"\nTikkas\t (1) pieces.").toString();
            SLSuonCha++;
            comSuonCha.setText(Integer.toString(SLSuonCha));

        } else if (view == findViewById(R.id.comSaBiChuong_button)) {
            Toast.makeText(this, "Cơm Sà Bì Chưởng đã được thêm!!", Toast.LENGTH_SHORT).show();
            //choices = (choices+"\nBiriyani\t (1) plate.").toString();
            SLSaBiChuong++;

            comSaBiChuong.setText(Integer.toString(SLSaBiChuong));
        } else if (view == findViewById(R.id.phoVien_button)) {
            Toast.makeText(this, "Phở Bò Viên đã được thêm!!", Toast.LENGTH_SHORT).show();
            //choices = (choices+"\nTikkas\t (1) pieces.").toString();
            SLPhoVien++;

            phoVien.setText(Integer.toString(SLPhoVien));
        } else if (view == findViewById(R.id.phoNam_button)) {
            Toast.makeText(this, "Phở Bò Viên đã được thêm!!", Toast.LENGTH_SHORT).show();
            // choices = (choices+"\nTikkas\t (1) pieces.").toString();

            SLPhoNam++;

            phoNam.setText(Integer.toString(SLPhoNam));
        } else if (view == findViewById(R.id.phoTai_button)) {
            Toast.makeText(this, "Phở Bò Tái đã được thêm!!", Toast.LENGTH_SHORT).show();;
            SLPhoTai++;
            phoTai.setText(Integer.toString(SLPhoTai));
        }

    }

    // removing iteam

    public void rmv_from_list(View view) {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor editor = prefs.edit();


        if (view == findViewById(R.id.comSuonBi_button_rm)) {
            if (SLSuonBi > 0) {

                SLSuonBi--;
                comSuonBi.setText(Integer.toString(SLSuonBi));
                Toast.makeText(this, "Cơm Sườn Bì đã bị xóa!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn chưa chọn món!!", Toast.LENGTH_SHORT).show();
            }

        } else if (view == findViewById(R.id.comSuon_button_rm)) {

            // = (choices+"\nBiriyani\t (1) plate.").toString();
            if (SLComSuon > 0) {
                SLComSuon--;

                comSuon.setText(Integer.toString(SLComSuon));
                Toast.makeText(this, "Cơm Sườn đã bị xóa!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn chưa chọn món!!", Toast.LENGTH_SHORT).show();
            }
        } else if (view == findViewById(R.id.comSuonCha_button_rm)) {

            //choices = (choices+"\nTikkas\t (1) pieces.").toString();

            if (SLSuonCha > 0) {

                SLSuonCha--;
                comSuonCha.setText(Integer.toString(SLSuonCha));
                Toast.makeText(this, "Cơm Sườn Chả đã bị xóa!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn chưa chọn món!!", Toast.LENGTH_SHORT).show();
            }
        } else if (view == findViewById(R.id.comSaBiChuong_button_rm)) {

            //choices = (choices+"\nBiriyani\t (1) plate.").toString();
            if (SLSaBiChuong > 0) {

                comSaBiChuong.setText(Integer.toString(SLSaBiChuong));
                SLSaBiChuong--;
                Toast.makeText(this, "Cơm Sà Bì Chưởng đã bị xóa!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn chưa chọn món!!", Toast.LENGTH_SHORT).show();
            }

        } else if (view == findViewById(R.id.phoVien_button_rm)) {

            //choices = (choices+"\nTikkas\t (1) pieces.").toString();
            if (SLPhoVien > 0) {

                SLPhoVien--;
                phoVien.setText(Integer.toString(SLPhoVien));
                Toast.makeText(this, "Phở Bò Viên đã bị xóa", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn chưa chọn món!!", Toast.LENGTH_SHORT).show();
            }

        } else if (view == findViewById(R.id.phoNam_button_rm)) {

            // choices = (choices+"\nTikkas\t (1) pieces.").toString();
            if (SLPhoNam > 0) {
                SLPhoNam--;
                phoNam.setText(Integer.toString(SLPhoNam));
                Toast.makeText(this, "Phở Bò Viên đã bị xóa", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn chưa chọn món!!", Toast.LENGTH_SHORT).show();
            }

        } else if (view == findViewById(R.id.phoTai_button_rm)) {
            if (SLPhoTai > 0) {
                SLPhoTai--;
                phoTai.setText(Integer.toString(SLPhoTai));
                Toast.makeText(this, "Phở Bò Tái đã bị xóa!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bạn chưa chọn món!!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void balancesheet() {
        ComSuonPrice = SLComSuon * 250;
        PhoNamPrice = SLPhoNam * 35;
        PhoVienPrice = SLPhoVien * 50;
        SuonChaPrice = SLSuonCha * 220;
        SuonBiPrice = SLSuonBi * 80;
        SabiChuongPrice = SLSaBiChuong * 150;
        PhoTaiPrice = SLPhoTai * 25000;
        total = ComSuonPrice + PhoNamPrice + PhoVienPrice + SuonBiPrice + SabiChuongPrice + SuonChaPrice + PhoTaiPrice;
        usd_price = total / 22000;
        prices = Integer.toString(total);
        usd_price_string = Double.toString(usd_price);

        if (SLComSuon > 0) {
            choices = choices + "Cơm Sườn       (" + SLComSuon + " x 250) = " + ComSuonPrice;
        }

        if (SLPhoNam > 0) {
            choices = choices + "\n\nPhở Bò Viên     (" + SLPhoNam + " x 35) = " + PhoNamPrice;
        }

        if (SLPhoVien > 0) {
            choices = choices + "\n\nPhở Bò Viên      (" + SLPhoVien + " x 50) = " + PhoVienPrice;
        }

        if (SLSuonCha > 0) {
            choices = choices + "\n\nCơm Sườn Chả (" + SLSuonCha + " x 220) = " + SuonChaPrice;
        }
        if (SLSuonBi > 0) {
            choices = choices + "\n\nCởm Sườn Bì     (" + SLSuonBi + " x 80) = " + SuonBiPrice;
        }

        if (SLSaBiChuong > 0) {
            choices = choices + "\n\nCơm Sà Bì Chưởng        (" + SLSaBiChuong + " x 150) = " + SabiChuongPrice;
        }
        if (SLPhoTai > 0) {
            choices = choices + "\n\n Phở Bò Tái        (" + SLPhoTai + " x 25000 = " + PhoTaiPrice;
        }

    }


}
