package com.yashkakkar.licagentdiary;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joooonho.SelectableRoundedImageView;
import com.yashkakkar.licagentdiary.async.eventbus.UpdateMemberEvent;
import com.yashkakkar.licagentdiary.async.member.UpdateMemberTask;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.models.Policy;
import com.yashkakkar.licagentdiary.utils.BitmapUtility;
import com.yashkakkar.licagentdiary.utils.GenerateUniqueId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.yashkakkar.licagentdiary.utils.Constants.PATH;

public class EditMember extends AppCompatActivity {

    // member
    @BindView(R.id.mProfileIconChange) SelectableRoundedImageView memberProfileImg;
    @BindView(R.id.mNameEdit) EditText memberName;
    @BindView(R.id.mEmailIdEdit) EditText memberEmailId;
    @BindView(R.id.mPhoneEdit) EditText memberPhoneNumer;
    @BindView(R.id.mGenderEdit) Spinner memberGender;
    @BindView(R.id.mProfileChange) LinearLayout memberProfileChange;
    // policy


    private final int REQUEST_CODE_ASK_PERMISSIONS_FOR_CAMERA = 111;
    private final int REQUEST_CODE_ASK_PERMISSIONS_FOR_READ_EXTERNAL_STORAGE = 222;
    private final int PIC_IMAGE_FROM_CAMERA = 0;
    private final int PIC_IMAGE_FROM_GALLERY = 1;

    private Unbinder unbinder;
    private Member member;
    private Policy policy;
    private Bitmap bitMapImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (savedInstanceState != null) {
            member = savedInstanceState.getParcelable("selectedMember");
            policy = savedInstanceState.getParcelable("selectedPolicy");
        }
        setContentView(R.layout.activity_edit_member);

        // set photo
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + PATH + "/" + member.getMemberImageName() + ".jpg");
        if (bitmap != null) {
            BitmapUtility bitmapUtility = new BitmapUtility();
            bitmap = bitmapUtility.getScaledBitmap(bitmap, 600);
            memberProfileImg.setImageBitmap(bitmap);
        }else{
            // if image not set set a default image
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_member_profile_pic);
            memberProfileImg.setImageBitmap(bitmap);
        }
        // Profile change
        memberProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog materialDialog = new MaterialDialog.Builder(EditMember.this)
                        .title("Update Photo")
                        .items(R.array.add_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:// Take a photo from a camera
                                        // grant permission for CAMERA
                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
                                            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                                                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                                                    showMessageOKCancel("You need to allow access to Camera",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                                                                REQUEST_CODE_ASK_PERMISSIONS_FOR_CAMERA);
                                                                    }
                                                                }
                                                            });
                                                    return;
                                                }
                                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_FOR_CAMERA);
                                                return;
                                            }
                                        }
                                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        // i.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                        startActivityForResult(i, PIC_IMAGE_FROM_CAMERA);
                                        Toast.makeText(EditMember.this, which + ": " + text + ", ID = " + view.getId(), Toast.LENGTH_LONG).show();

                                        break;
                                    case 1:// Take a photo from Gallery
                                        // grant permission for READ_EXTERNAL_STORAGE
                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                                            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                            if (hasReadPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                                                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                    showMessageOKCancel("You need to allow access to Camera",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                                                                REQUEST_CODE_ASK_PERMISSIONS_FOR_READ_EXTERNAL_STORAGE);
                                                                    }
                                                                }
                                                            });
                                                    return;
                                                }
                                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_FOR_READ_EXTERNAL_STORAGE);
                                                return;
                                            }
                                        }
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        startActivityForResult(intent, PIC_IMAGE_FROM_GALLERY);
                                        Toast.makeText(EditMember.this, which + ": " + text + ", ID = " + view.getId(), Toast.LENGTH_LONG).show();

                                        break;
                                }
                            }
                        }).show();
            }
        });

        // set member data
        memberName.setText(member.getMemberName());
        memberEmailId.setText(member.getMemberEmailId());
        memberPhoneNumer.setText(member.getMemberPhoneNumber());
        // set spinner
       // memberGender.setSelection(((ArrayAdapter)memberGender.getAdapter().getPosition(""));

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_member_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.update_member:
                // get all data and fire update using async task
                setMember();
                UpdateMemberTask updateMemberTask = new UpdateMemberTask(this,member);
                updateMemberTask.execute();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMember() {
        member.setMemberName(memberName.getText().toString().trim());
        member.setMemberEmailId(memberEmailId.getText().toString().trim());
        member.setMemberPhoneNumber(memberPhoneNumer.getText().toString().trim());
        // set spinner

        // set image path and name

    }

    @Subscribe
    public void onEvent(UpdateMemberEvent event){
        if (event.isMemberUpdated()){
            // member updated
            Toast.makeText(EditMember.this,"Member Updated Successfully!",Toast.LENGTH_SHORT).show();
            finish();
        }else {
            // something went wrong
            Toast.makeText(EditMember.this,"Something went wrong!!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PIC_IMAGE_FROM_CAMERA:
                    Bundle extras = data.getExtras();
                    bitMapImage = (Bitmap) extras.get("data");

                    // DbBitmapUtility db = new DbBitmapUtility();
                    // byte[] image = db.getByteArray(bitMapImage);
                    saveImage(bitMapImage);
                                            /*if (path0 == ""){path0 = PATH;member.setMemberImagePath(path0);}*/
                    memberProfileImg.setImageBitmap(bitMapImage);

                    // Log.v("Image saved at: ",path0);
                    Toast.makeText(this,"Image Loaded from camera!", Toast.LENGTH_SHORT).show();
                    break;
                case PIC_IMAGE_FROM_GALLERY:
                    Uri contentURI = data.getData();// content://media/external/images/media/180263

                    Log.v("Getting Image URI:", contentURI.toString());
                    try {
                        bitMapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),contentURI);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    memberProfileImg.setImageBitmap(bitMapImage);
                    saveImage(bitMapImage);
                    //Log.v("Image saved at: ",path1);
                    Toast.makeText(this,"Image Loaded from gallery", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    private String saveImage(Bitmap bitMapImage) {

        // saving image to internal directory /Lic Diary/

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitMapImage.compress(Bitmap.CompressFormat.JPEG, 100 , bytes);


        File imageFile = new File(Environment.getExternalStorageDirectory() + PATH);
        // check for the directory exist or not
        if (!imageFile.exists()){
            imageFile.mkdir();
        }
        try {
            // Create a new file
            // Give a unique name to photo
            long time_stamp = Calendar.getInstance().getTimeInMillis();
            String photo_name = GenerateUniqueId.newInstance().generateUniqueKeyUsingUUID();
            member.setMemberImageName(photo_name);
            member.setMemberImageFile(bytes.toByteArray());

            File f = new File(imageFile, photo_name+".jpg");
            f.createNewFile();
            // Write file
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved:" + f.getAbsolutePath());
            member.setMemberImagePath(f.getAbsolutePath());
            return f.getAbsolutePath();
        }catch (IOException e){
            e.getStackTrace();
        }
        return "";
    }

}
