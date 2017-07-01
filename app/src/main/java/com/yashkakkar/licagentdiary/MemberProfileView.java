package com.yashkakkar.licagentdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.joooonho.SelectableRoundedImageView;
import com.yashkakkar.licagentdiary.async.IsWhatsAppNumber;
import com.yashkakkar.licagentdiary.async.eventbus.GetMemberPoliciesListEvent;
import com.yashkakkar.licagentdiary.async.eventbus.IsWhatsAppNumberEvent;
import com.yashkakkar.licagentdiary.async.policy.GetMemberPoliciesListTask;
import com.yashkakkar.licagentdiary.database.DatabaseHelper;
import com.yashkakkar.licagentdiary.models.Adapters.PoliciesAdapter;
import com.yashkakkar.licagentdiary.models.Adapters.PolicyListAdapter;
import com.yashkakkar.licagentdiary.models.Member;
import com.yashkakkar.licagentdiary.models.Policy;
import com.yashkakkar.licagentdiary.utils.BitmapUtility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.yashkakkar.licagentdiary.utils.Constants.PATH;

/**
 * Created by Yash Kakkar on 26-05-2017.
 */

public class MemberProfileView extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.member_profile_call) LinearLayout memberCall;
    @BindView(R.id.member_profile_whatsapp) LinearLayout memberWhatsApp;
    @BindView(R.id.member_profile_sms) LinearLayout memberSms;
    @BindView(R.id.member_profile_fav_unselected) ToggleButton memberFav;

    @BindView(R.id.member_profile_phone_number) TextView memberPhoneNumber;
    @BindView(R.id.member_profile_email) TextView memberEmail;
    @BindView(R.id.member_proflie_gender) TextView memberGender;

    @BindView(R.id.member_profile_image_icon) SelectableRoundedImageView memberProfileImageIcon;
    @BindView(R.id.member_profile_image_bg) ImageView memberProfileImageBg;
    @BindView(R.id.verticalLayout) LinearLayout verticalLayout;

    // Whatsapp
    @BindView(R.id.member_profile_whatsapp_card_view) CardView whatsAppCard;
    @BindView(R.id.whatsapp_message) LinearLayout whatsAppMessage;
    @BindView(R.id.whatsapp_voice_call) LinearLayout whatsAppVoiceCall;
    @BindView(R.id.whatsapp_video_call) LinearLayout whatsAppVideoCall;
    // Whatsapp text
    @BindView(R.id.member_profile_whatsapp_message) TextView whatsAppMessageText;
    @BindView(R.id.member_profile_whatsapp_voice_call) TextView whatsAppVoiceText;
    @BindView(R.id.member_profile_whatsapp_video_call) TextView whatsAppVideoText;

    @BindView(R.id.member_policies_list_view) RecyclerView memberPolicyListView;
    private boolean isWhatsAppNumber = false;
    private String whatsAppId;
    private Unbinder unbinder;
    private Member member;
    private Policy policy;
    private static final int EDIT_MEMBER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile_view);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            member = savedInstanceState.getParcelable("selectedMember");
            policy = savedInstanceState.getParcelable("selectedPolicy");
            // Toast.makeText(this, "Selected policy holder " + member.getMemberName(), Toast.LENGTH_SHORT).show();
        }

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(member.getMemberName());
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        memberPhoneNumber.setText(member.getMemberPhoneNumber());
        memberEmail.setText(member.getMemberEmailId());
        memberGender.setText(member.getMemberGender());

        /*
        byte[] image =  member.getMemberImageFile();
        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
        Bitmap imageBitmap = dbBitmapUtility.getBitmap(image);
        memberProfileImageIcon.setImageBitmap(imageBitmap);
        */  //imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,image);
        // bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_member_profile_pic);
        //memberProfileImageIcon.setImageBitmap(bitmap);
        // memberProfileImageBg.setImageBitmap(bitmap);

        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + PATH + "/" + member.getMemberImageName() + ".jpg");
        if (bitmap != null) {
            BitmapUtility bitmapUtility = new BitmapUtility();
            bitmap = bitmapUtility.getScaledBitmap(bitmap, 600);
            //Log.v("Image name",member.getMemberImageName()+"kk");
            memberProfileImageIcon.setImageBitmap(bitmap);
            memberProfileImageBg.setImageBitmap(bitmap);
        }

        // remove card check
        if (!isWhatsAppNumber) whatsAppCard.setVisibility(View.INVISIBLE);

        // get member is fav or not
        final int fav = member.getMemberFav();
        if (fav == 0) {
            memberFav.setChecked(false);
            memberFav.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_outline));
        } else {
            memberFav.setChecked(true);
            memberFav.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star));
        }

        memberFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    memberFav.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star));
                    member.setMemberFav(1);
                    Toast.makeText(MemberProfileView.this, "Added to Favourate list!", Toast.LENGTH_SHORT).show();
                } else {
                    memberFav.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_outline));
                    member.setMemberFav(0);
                    Toast.makeText(MemberProfileView.this, "Remove from Favourate list!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // show View only if the number is on whatsapp otherwise hide
        // check if phone number have whatsapp contact
        IsWhatsAppNumber asyncCheckNumber = new IsWhatsAppNumber(this);
        asyncCheckNumber.execute(member.getMemberPhoneNumber());


        // sharing text message to a particular whatsapp number
        whatsAppMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Open the whats app chat box
                 */
                Uri uri = Uri.parse("smsto:" + member.getMemberPhoneNumber());
                Intent intentMsg = new Intent(Intent.ACTION_SENDTO, uri);
                intentMsg.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(intentMsg, ""));
            }
        });


        /* Not Working, do not found the activity to make a call
        whatsAppVoiceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVoiceCall = new Intent();
                intentVoiceCall.setAction(Intent.ACTION_VIEW);
                // the _ids you save goes here at the end of /data/12562
                intentVoiceCall.setDataAndType(Uri.parse("content://com.android.contacts/data/"+whatsAppId),
                        "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                intentVoiceCall.setPackage("com.whatsapp");
                startActivity(intentVoiceCall);
            }
        });

        whatsAppVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // video call
                Intent intentVideoCall = new Intent();
                intentVideoCall.setAction(Intent.ACTION_VIEW);
                intentVideoCall.setDataAndType(Uri.parse("content://com.android.contacts/data/"+whatsAppId),
                        "vnd.android.cursor.item/vnd.com.whatsapp.video.call");
                intentVideoCall.setPackage("com.whatsapp");
                startActivity(intentVideoCall);
            }
        });*/

        // show no. of policy belongs to a particular member with option of view,share, edit, delete

        GetMemberPoliciesListTask getMemberPoliciesListTask = new GetMemberPoliciesListTask(this);
        getMemberPoliciesListTask.execute(member.getMemberId());

    }

    @Subscribe
    public void onEvent(IsWhatsAppNumberEvent event){
        isWhatsAppNumber = event.iswhatsAppNumberValid();
       if (isWhatsAppNumber){
           whatsAppId = event.getWhatsAppId();
           whatsAppMessageText.setText("Message "+ member.getMemberPhoneNumber());
           whatsAppVoiceText.setText("Voice call "+ member.getMemberPhoneNumber());
           whatsAppVideoText.setText("Video Call "+ member.getMemberPhoneNumber());
           whatsAppCard.setVisibility(View.VISIBLE);
       }
    }

    @Subscribe
    public void onEvent(GetMemberPoliciesListEvent event){
        // get policies
        List<Policy> policyList = event.getPolicies();
        // set adapter
        PoliciesAdapter policyListAdapter = new PoliciesAdapter(this,policyList);
        memberPolicyListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        memberPolicyListView.setAdapter(policyListAdapter);
    }
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_member_profile_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){

            case R.id.member_profile_view_edit:
                // open new activity to edit existing user information
                Intent intent = new Intent(this,EditMember.class);
                intent.putExtra("memberEdit",(Parcelable) member);
                startActivityForResult(intent,EDIT_MEMBER);
                break;

            case R.id.member_profile_view_delete:
                // delete member form database
                // Ask to delete or not
                AlertDialog.Builder alert = new AlertDialog.Builder(MemberProfileView.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure you want to delete this Policy holder ?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean flag  = DatabaseHelper.getInstance().deleteMembers(member.getMemberId());
                        if(flag){
                            // go back to previous activity
                            finish();
                            Toast.makeText(MemberProfileView.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MemberProfileView.this,"Unable to delete",Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.dismiss();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
                break;

            case R.id.member_profile_view_delete_forever:
                // delete forever
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.member_profile_call:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + member.getMemberPhoneNumber()));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(this,"Calling, "+member.getMemberName()+"!",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    // ask user to give permissions
                   Toast.makeText(this,"Please give permissions to call!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.member_profile_whatsapp:
                // ask for whatsapp message or a voice call or video call
                Uri uri = Uri.parse("smsto:" + member.getMemberPhoneNumber());
                Intent intentMsg = new Intent(Intent.ACTION_SENDTO, uri);
                intentMsg.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(intentMsg, ""));
                break;
            case R.id.member_profile_sms:
                Intent intentSms = new Intent(Intent.ACTION_SENDTO);
                intentSms.addCategory(Intent.CATEGORY_DEFAULT);
                intentSms.setType("vnd.android-dir/mms-sms");
                intentSms.setData(Uri.parse("smsto:"+member.getMemberPhoneNumber())); // This ensures only SMS apps respond
                startActivity(intentSms);
                break;
            case R.id.member_profile_fav_unselected:

                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case EDIT_MEMBER:
                    // change data and refresh member details
                    // otherwise no nothing
                    break;

            }
        }
    }

}