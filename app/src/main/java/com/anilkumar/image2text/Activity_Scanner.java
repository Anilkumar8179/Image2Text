package com.anilkumar.image2text;

import static com.anilkumar.image2text.MainActivity.REQUEST_IMAGE_CAPTURE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.CharArrayWriter;

public class Activity_Scanner extends AppCompatActivity {

    ImageView second_book;
    TextView textView_texView;
    Button snap;
    Button detect;
    
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        second_book=findViewById(R.id.book_Second_image);
        textView_texView=findViewById(R.id.textView_texView);
        snap=findViewById(R.id.button_snap);
        detect=findViewById(R.id.button_detect);

        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckPermission()){
                    CaptureImage();
                }
                else{
                    RequestPermission();
                }

            }
        });

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Activity_Scanner.this, "clicked", Toast.LENGTH_SHORT).show();

                detect();

            }


        });
    }

    private void detect() {


        Bitmap imageBitmap = bitmap;
        InputImage inputImage=InputImage.fromBitmap(imageBitmap,0);

        TextRecognizer recognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text>Result=recognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                StringBuilder result= new StringBuilder();

                for (Text.TextBlock block:text.getTextBlocks()){
                    String BlockText= block.getText();
                    Point[]blockCornerPoint= block.getCornerPoints();
                    Rect blockFrame=block.getBoundingBox();

                    for (Text.Line line: block.getLines()){
                        String LineText= line.getText();
                        Point[]lineCornerPoint=line.getCornerPoints();
                        Rect lineRect=line.getBoundingBox();

                        for (Text.Element element:line.getElements()){
                            String elementText= element.getText();
                            result.append(elementText);
                        }

                        textView_texView.setText(BlockText);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to detect image from Text ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private  void RequestPermission(){
        int PERMISSION_CODE=200;
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.CAMERA
        },PERMISSION_CODE);


    }

    private void CaptureImage() {
        Intent takePicture= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null);
        startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0){
            boolean cameraPermission= grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this, "permission is granted", Toast.LENGTH_SHORT).show();
                CaptureImage();
            }else {
                Toast.makeText(getApplicationContext(), "permission is not granted", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extra=data.getExtras();
            Bitmap imageBitmap = (Bitmap) extra.get("data");
            second_book.setImageBitmap(imageBitmap);



        }


    }

    private boolean CheckPermission() {
        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA_SERVICE);
        return cameraPermission== PackageManager.PERMISSION_GRANTED;

    }

}