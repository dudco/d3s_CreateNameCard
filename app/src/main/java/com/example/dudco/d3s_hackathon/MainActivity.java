package com.example.dudco.d3s_hackathon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.OnColorSelected;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private StickerView stickerView;
    private ImageView color_back;
    private FloatingActionButton fab_menu, fab_back;
    private ImageView image_photo, image_text, image_bold, image_italic, image_underline, image_font;
    private LinearLayout b_menu;
    private RelativeLayout color_container;

    private boolean isShow = true;

    private Sticker c_sticer;

    private static final int REQ_CODE_ALBUM = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_back = (FloatingActionButton) findViewById(R.id.back);
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getNewFile(MainActivity.this, "Sticker");
                stickerView.save(file);

                //TODO 서버연동
            }
        });
        fab_menu = (FloatingActionButton) findViewById(R.id.menu);
        image_bold = (ImageView) findViewById(R.id.bold);
        image_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dudco", c_sticer + "");
                if(c_sticer != null && c_sticer.toString().contains("Text")){
                    Log.d("dudco", "isText");
                    ((TextSticker)c_sticer).setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
                }
            }
        });
        image_italic = (ImageView) findViewById(R.id.italic);
        image_italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c_sticer != null && c_sticer.toString().contains("Text")){
                    ((TextSticker)c_sticer).setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.ITALIC));
                }
            }
        });
        image_font = (ImageView) findViewById(R.id.font);
        image_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c_sticer != null && c_sticer.toString().contains("Text")){
                    new BottomSheet.Builder(MainActivity.this)
                            .setSheet(R.menu.bottom_sheet_font)
//                            .setTitle(R.string.options)
                            .setListener(new BottomSheetListener() {
                                @Override
                                public void onSheetShown(@NonNull BottomSheet bottomSheet) {

                                }

                                @Override
                                public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {
                                    Typeface tf = null;
                                    Toast.makeText(getApplicationContext(), menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                    switch (menuItem.getTitle().toString()){
                                        case "우아한 형제-도현" : tf = Typeface.createFromAsset(getAssets(), "fonts/BMDOHYEON_ttf.ttf"); break;
                                        case "우아한 형제-연성" : tf = Typeface.createFromAsset(getAssets(), "fonts/BMYEONSUNG_ttf.ttf"); break;
                                        case "우아한 형제-주아" : tf = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf"); break;
                                        case "우아한 형제-한나는 11살" : tf = Typeface.createFromAsset(getAssets(), "fonts/BMHANNA_11yrs_ttf.ttf"); break;
                                        case "노토 산스" : tf = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Medium.otf"); break;
                                    }
                                    if(tf != null){
                                        ((TextSticker)c_sticer).setTypeface(tf);
                                    }
                                }

                                @Override
                                public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @DismissEvent int i) {

                                }
                            })
                            .show();
                }
            }
        });
        image_text = (ImageView) findViewById(R.id.text);
        image_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final View inflate = getLayoutInflater().from(MainActivity.this).inflate(R.layout.alert_text, null);
                new AlertDialog.Builder(MainActivity.this).setTitle("텍스트")
                        .setView(inflate)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edit = (EditText) inflate.findViewById(R.id.edit_alert);
                        Log.d("dudco", "edit : " + edit.getText().toString());

                        TextSticker sticker = new TextSticker(MainActivity.this);
                        sticker.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black_85));
                        sticker.setText(edit.getText().toString());
                        sticker.resizeText();

                        stickerView.addSticker(sticker);
                        dialog.dismiss();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        image_photo = (ImageView) findViewById(R.id.photo);
        image_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_ALBUM);
            }
        });

        image_underline = (ImageView) findViewById(R.id.underline);
        image_underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c_sticer.toString().contains("Text") && c_sticer != null){
                    //TODO underline
                }
            }
        });
        b_menu = (LinearLayout) findViewById(R.id.b_menu);

        color_back = (ImageView) findViewById(R.id.color_back);

        stickerView = (StickerView) findViewById(R.id.stickerview);

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dudco", "click!");
                if(isShow){
                    hide();
                    isShow = false;
                }
                else{
                    show();
                    isShow = true;
                }
            }
        });

        color_container = (RelativeLayout) findViewById(R.id.color_container);
        color_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c_sticer != null && c_sticer.toString().contains("Text")) {
                    final ColorPicker cp = new ColorPicker(MainActivity.this, 1, 1, 1);
                    cp.show();

                    cp.setOnColorSelected(new OnColorSelected() {
                        @Override
                        public void returnColor(int col) {
                            ((TextSticker) c_sticer).setTextColor(col);
                            cp.dismiss();
                        }
                    });
                }
            }
        });
        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerClicked(Sticker sticker) {
                Log.d("dudco", "click on "+sticker);
                c_sticer = sticker;
            }

            @Override
            public void onStickerDeleted(Sticker sticker) {
                Log.d("dudco", "deleted on "+sticker);

            }

            @Override
            public void onStickerDragFinished(Sticker sticker) {
                Log.d("dudco", "drag on "+sticker);
                c_sticer = sticker;
            }

            @Override
            public void onStickerZoomFinished(Sticker sticker) {
                Log.d("dudco", "zoom on "+sticker);
                c_sticer = sticker;
            }

            @Override
            public void onStickerFlipped(Sticker sticker) {
                Log.d("dudco", "flip on "+sticker);
                c_sticer = sticker;
            }

            @Override
            public void onStickerDoubleTapped(Sticker sticker) {
                Log.d("dudco", "double tap on "+sticker);
                c_sticer = sticker;
            }
        });

        TextSticker t_Sticker = new TextSticker(this);
        t_Sticker.setTextColor(Color.BLACK);
        t_Sticker.setText("asdf");
        t_Sticker.resizeText();

        stickerView.addSticker(t_Sticker);
    }
    int b_margin;
    int f_margin;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("dudco",""+color_back.getHeight());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(color_back.getHeight(),color_back.getHeight());
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        color_back.setLayoutParams(params);

        CoordinatorLayout.LayoutParams l_params = (CoordinatorLayout.LayoutParams) b_menu.getLayoutParams();
        b_margin = l_params.bottomMargin + l_params.height;
        Log.d("dudco", "magin : " + b_margin);
//        l_params.bottomMargin = -b_margin;

        ViewGroup.LayoutParams f_params = (ViewGroup.LayoutParams) fab_back.getLayoutParams();
        ViewGroup.MarginLayoutParams f_margin_params = (ViewGroup.MarginLayoutParams) fab_back.getLayoutParams();
        f_margin = f_margin_params.leftMargin + fab_back.getWidth();
        Log.d("dudco", "margin2 : " + f_margin);
//        f_margin_params.leftMargin = -f_margin;
    }

    public void hide(){
        Log.d("dudco","asdf");
        Log.d("dudco", "b_margin : " + b_margin + " f_margin : " + f_margin);
        b_menu.animate().translationY(b_margin);
        fab_back.animate().translationX(-f_margin);
    }
    public void show(){
        b_menu.animate().translationY(-b_margin/32);
        fab_back.animate().translationX(f_margin/32);
    }
    Uri mImageUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQ_CODE_ALBUM){
                mImageUri = data.getData();
                try {
                    DrawableSticker sticker = new DrawableSticker(Drawable.createFromStream(getContentResolver().openInputStream(mImageUri),mImageUri.toString()));
                    stickerView.addSticker(sticker);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getFolderName(String name) {
        File mediaStorageDir =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        name);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return "";
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }
    public static File getNewFile(Context context, String folderName) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);

        String timeStamp = simpleDateFormat.format(new Date());

        String path;
        if (isSDAvailable()) {
            path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
        } else {
            path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
        }

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        return new File(path);
    }

    private static boolean isSDAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
