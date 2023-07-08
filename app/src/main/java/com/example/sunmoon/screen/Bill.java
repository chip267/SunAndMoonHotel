package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Employee;
import com.example.sunmoon.models.UserSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Bill extends AppCompatActivity implements Serializable {
    private TextView tvCheckin, tvCheckout, tvRoomNum, tvType, tvRoomCharge, tvDetails, tvSurcharge, tvTotal, tvBookID, tvNameCus, tvPhoneCus;
    private Button btn_export;
    final  static  int REQUEST_CODE = 1232;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_bill_large);
        ImageView imageViewBack;
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bill.this, SalesReport.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        tvCheckin = findViewById(R.id.tv_checkintime);
        tvCheckout = findViewById(R.id.tv_checkouttime);
        tvRoomNum = findViewById(R.id.tv_roomnum);
        tvType = findViewById(R.id.tv_tob);
        tvRoomCharge = findViewById(R.id.tv_roomchargemoney);
        tvDetails = findViewById(R.id.tv_details);
        tvSurcharge = findViewById(R.id.tv_surchargemoney);
        tvTotal = findViewById(R.id.tv_totalsurchargemoney);
        tvBookID = findViewById(R.id.tv_idbill);
        tvNameCus = findViewById(R.id.tv_nameCus);
        tvPhoneCus = findViewById(R.id.tv_phonenocus);
        Intent i = getIntent();
        String bcheckIn = i.getStringExtra("CheckIn");
        String bcheckOut = i.getStringExtra("CheckOut");
        String broomNum = i.getStringExtra("RoomNum");
        String btypeBook = i.getStringExtra("Type");
        String broomCharge = i.getStringExtra("RoomCharge");
        String bdetails = i.getStringExtra("Details");
        String bsurCharge = i.getStringExtra("Surcharge");
        String btotal = i.getStringExtra("Total");
        String bbookID = i.getStringExtra("BookID");
        String bnameCus = i.getStringExtra("NameCus");
        String bphoneCus = i.getStringExtra("PhoneCus");
        tvCheckin.setText(bcheckIn);
        tvCheckout.setText(bcheckOut);
        tvRoomNum.setText(broomNum);
        tvType.setText(btypeBook);
        tvRoomCharge.setText(broomCharge);
        tvDetails.setText(bdetails);
        tvSurcharge.setText(bsurCharge);
        tvTotal.setText(btotal);
        tvBookID.setText(bbookID);
        tvNameCus.setText(bnameCus);
        tvPhoneCus.setText(bphoneCus);
        askPermission();
        btn_export = findViewById(R.id.btn_exportBill);
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWriteExternalPermission())
                     convertXMLtoPDF();
                else askPermission();
            }
        });
    }

    private boolean checkWriteExternalPermission()
    {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    @SuppressLint("MissingInflatedId")
    private void convertXMLtoPDF() {
        View view = LayoutInflater.from(this).inflate(R.layout.booking_bill,null);
        DisplayMetrics displayMetrics = new DisplayMetrics();

       view.findViewById(R.id.layout_visible).setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            this.getDisplay().getRealMetrics(displayMetrics);

        }else this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));

        view.layout(0,0,displayMetrics.widthPixels, displayMetrics.heightPixels);
        PdfDocument document = new PdfDocument();

        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();
        Log.d("my log", "Width: "+viewWidth);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        view.draw(canvas);

        document.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String fileName = tvBookID.getText() + ".pdf";
        File file = new File(downloadsDir, fileName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Convert to XML Sucessfully!!!", Toast.LENGTH_SHORT).show();

        }catch (FileNotFoundException e){
            Log.d("my log", "Error while writting"+ e.toString());
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        view.findViewById(R.id.layout_visible).setVisibility(View.VISIBLE);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

    }

    private  void createPDF(){

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080,1920,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(42);

        String text = "Hello, World";
        float x = 500;
        float y = 900;

        canvas.drawText(text, x,y, paint);
        document.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example.pdf";
        File file = new File(downloadsDir, fileName);
        try{
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Written Sucessfully!!!", Toast.LENGTH_SHORT).show();

        }catch (FileNotFoundException e){
            Log.d("my log", "Error while writting"+ e.toString());
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        convertXMLtoPDF();

    }
}
