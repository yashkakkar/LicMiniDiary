package com.yashkakkar.licagentdiary;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joooonho.SelectableRoundedImageView;
import com.yashkakkar.licagentdiary.async.member.SaveNewMemberTask;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.utils.GenerateUniqueId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateNewMember extends AppCompatActivity {

    @BindView(R.id.mName) EditText memberName;
    @BindView(R.id.mPhone) EditText memberPhone;
    @BindView(R.id.mEmailId) EditText memberEmail;
    @BindView(R.id.mProfileUpload) LinearLayout memberProfileUploadBtn;
    @BindView(R.id.mProfileIcon) SelectableRoundedImageView memberProfileIcon;

    private Unbinder unbinder;
    private Member member;

    ProgressDialog progressDialog;

    private final int REQUEST_CODE_ASK_PERMISSIONS_FOR_CAMERA = 111;
    private final int REQUEST_CODE_ASK_PERMISSIONS_FOR_READ_EXTERNAL_STORAGE = 222;
    private final int PIC_IMAGE_FROM_CAMERA = 0;
    private final int PIC_IMAGE_FROM_GALLERY = 1;
    private final static String PATH = "/LIC DIARY";
    private Bitmap bitMapImage;

    private boolean flag_image_selected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_member);
        unbinder = ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        member = new Member();
        // setting a default image to to user profile
        bitMapImage = BitmapFactory.decodeResource(getResources(),R.drawable.ic_member_profile_pic);

        progressDialog = new ProgressDialog(this);

        memberProfileUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  Material Dialog
                MaterialDialog materialDialog = new MaterialDialog.Builder(CreateNewMember.this)
                        .title("Add Photo")
                        .items(R.array.add_photo)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which){
                                    case 0: // Take a photo from a camera
                                        // grant permission for CAMERA
                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
                                            if(hasCameraPermission != PackageManager.PERMISSION_GRANTED){
                                                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                                                    showMessageOKCancel("You need to allow access to Camera",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                        requestPermissions(new String[] {android.Manifest.permission.CAMERA},
                                                                                REQUEST_CODE_ASK_PERMISSIONS_FOR_CAMERA);
                                                                    }
                                                                }
                                                            });
                                                    return;
                                                }
                                                requestPermissions(new String[]{android.Manifest.permission.CAMERA},REQUEST_CODE_ASK_PERMISSIONS_FOR_CAMERA);
                                                return;
                                            }
                                        }
                                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                       // i.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                            startActivityForResult(i, PIC_IMAGE_FROM_CAMERA);
                                            Toast.makeText(CreateNewMember.this, which + ": " + text + ", ID = " + view.getId(), Toast.LENGTH_LONG).show();
                                            break;
                                    case 1: // Take a photo from Gallery
                                        // grant permission for READ_EXTERNAL_STORAGE
                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                                            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                            if(hasReadPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED ){
                                                if (!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                    showMessageOKCancel("You need to allow access to Camera",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                        requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                                                                REQUEST_CODE_ASK_PERMISSIONS_FOR_READ_EXTERNAL_STORAGE);
                                                                    }
                                                                }
                                                            });
                                                    return;
                                                }
                                                requestPermissions(new String[]{android.Manifest.permission.CAMERA},REQUEST_CODE_ASK_PERMISSIONS_FOR_READ_EXTERNAL_STORAGE);
                                                return;
                                            }
                                        }
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        startActivityForResult(intent, PIC_IMAGE_FROM_GALLERY);
                                            Toast.makeText(CreateNewMember.this, which + ": " + text + ", ID = " + view.getId(),Toast.LENGTH_LONG).show();
                                            break;
                                    default:
                                            break;
                                }

                            }
                        })
                        .show();
            }
        });
        /*
        * This Activity has 2 fragments -->
        * 1. add new member fragment
        * 2. add new policy
        * After adding a new member --> Ask the user if they want to add new policy to the current member
        * --> if yes then replace the add new memberfragment to new policy layout then save the policy details to the member
        * and go to Member/policyholder profile activity
        * --> if not commit exit the activity
        * and go to Member/policyholder profile activity
        * -->
        * */


    }

    private void userPermissions() {


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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS_FOR_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else {Toast.makeText(this, "Capture photo from Camera Denied", Toast.LENGTH_SHORT).show();}
                break;
            case REQUEST_CODE_ASK_PERMISSIONS_FOR_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else {Toast.makeText(this, "Read External Storage Denied", Toast.LENGTH_SHORT).show();}
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

                                            /*if (path0 == ""){path0 = PATH;member.setMemberImagePath(path0);}*/
                                            memberProfileIcon.setImageBitmap(bitMapImage);
                                            flag_image_selected = true;
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

                                            memberProfileIcon.setImageBitmap(bitMapImage);
                                            flag_image_selected = true;
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

    private Member getMemberData(){
        String name = memberName.getText().toString();
        Log.v("Member Name ",name);
        Toast.makeText(CreateNewMember.this,name+"! Added as a Contact",Toast.LENGTH_SHORT).show();
        // check if the name is empty or not
        if (!TextUtils.isEmpty(name)){
            member.setMemberName(memberName.getText().toString());
            // generate a member id
            member.setMemberId(memberEmail.getText().toString());
        } else{
            // display a message name field is empty
            Toast.makeText(CreateNewMember.this,"Name Field is empty",Toast.LENGTH_SHORT).show();
        }
        member.setMemberPhoneNumber(memberPhone.getText().toString());
        member.setMemberEmailId(memberEmail.getText().toString());
        member.setMemberFav(0);
        if (flag_image_selected){
            saveImage(bitMapImage); // save image to the directory
        } // do not save any image to the directory
        member.setMemberGender("");
        return member;
    }

    private void createNewMemberEvent(Member member){
        // create member in background process using Async task
        SaveNewMemberTask saveNewMemberTask = new SaveNewMemberTask(this);
        saveNewMemberTask.execute(member);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_new_member_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_member){
            progressDialog.setMessage("Saving Contact...");
            progressDialog.show();
            createNewMemberEvent(getMemberData());
            progressDialog.dismiss();
            Intent i = new Intent(CreateNewMember.this, ActivityDashboard.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
