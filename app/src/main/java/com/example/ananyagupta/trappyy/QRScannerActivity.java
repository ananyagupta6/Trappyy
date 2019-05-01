package com.example.ananyagupta.trappyy;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import static android.Manifest.permission_group.CAMERA;

public class QRScannerActivity extends AppCompatActivity {

    private static final int WIDTH_PX = 500;
    private static final int HEIGHT_PX = 500;
    final int w=WIDTH_PX;
    final int h=HEIGHT_PX;
    //private static final int REQUEST_CAMERA = 1;
    EditText e1;
    ImageView i1;
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        b1=(Button)findViewById(R.id.scan);
        b2=(Button)findViewById(R.id.gen);
        b3=(Button)findViewById(R.id.clear);
        e1=(EditText)findViewById(R.id.ed);
        i1=(ImageView)findViewById(R.id.imageView);




        b2.setVisibility(View.INVISIBLE);
        b3.setVisibility(View.INVISIBLE);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e1.setText("");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=e1.getText().toString();
                if(text!=null && !text.isEmpty()){
                    try{
                        i1.setAlpha(1f);
                        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
                        BitMatrix bitMatrix=multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                        Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                        i1.setImageBitmap(bitmap);
                    }
                    catch (WriterException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bitmap.Config conf=Bitmap.Config.ARGB_8888;
//                Bitmap bitmap=Bitmap.createBitmap(w,h,conf);
//                Canvas canvas=new Canvas(bitmap);
//                i1.setImageBitmap(bitmap);
                i1.setAlpha(0.3f);
                IntentIntegrator intentIntegrator=new IntentIntegrator(QRScannerActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning ...");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });


        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!e1.getText().toString().equals("")){
                    b2.setVisibility(View.VISIBLE);
                    b3.setVisibility(View.VISIBLE);
                    //b2.setEnabled(true);
                }
                else{
                    b2.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    //b2.setEnabled(false);
                    i1.setAlpha(0.3f);
//                        Toast.makeText(getApplicationContext(),"Please Enter Text ...",Toast.LENGTH_SHORT).show();
//                        Bitmap.Config conf=Bitmap.Config.ARGB_8888;
//                        Bitmap bitmap=Bitmap.createBitmap(w,h,conf);
//                        Canvas canvas=new Canvas(bitmap);
//                        i1.setImageBitmap(bitmap);
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null && result.getContents()!=null){


            new AlertDialog.Builder(QRScannerActivity.this)
                    .setTitle("Scan Result")
                    .setMessage(result.getContents())
                    .setNeutralButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, result.getContents().toString());
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);

//                            Bitmap.Config conf=Bitmap.Config.ARGB_8888;
//                            Bitmap bitmap=Bitmap.createBitmap(w,h,conf);
//                            try{
//                                MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
//                                BitMatrix bitMatrix=multiFormatWriter.encode(result.getContents(), BarcodeFormat.QR_CODE,500,500);
//                                BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
//                                bitmap=barcodeEncoder.createBitmap(bitMatrix);
//                                i1.setImageBitmap(bitmap);
//                            }
//                            catch (WriterException e){
//                                e.printStackTrace();
//                            }
//                                    String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
//                                    Uri bitmapUri = Uri.parse(bitmapPath);
//
//                                    Intent intent = new Intent(Intent.ACTION_SEND);
//                                    intent.setType("image/png");
//                                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
//                                    startActivity(Intent.createChooser(intent, "Share"));

                        }
                    })
                    .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                            ClipData data=ClipData.newPlainText("Result",result.getContents());
                            clipboardManager.setPrimaryClip(data);
                            Toast.makeText(getApplicationContext(),"Text Copied",Toast.LENGTH_SHORT).show();

                        }
                    }).setNegativeButton("Visit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent browse= new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents().toString()));
                    startActivity(browse);
                    //for cancel -- dialogInterface.dismiss();
                }
            }).create().show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

