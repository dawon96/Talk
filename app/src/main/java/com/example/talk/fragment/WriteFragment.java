package com.example.talk.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class WriteFragment extends Fragment {

    private FirebaseDatabase database;
    //private static final String TAG = "MainActivity";
    private ImageView ivPreview;
    private Uri filePath;

    public static final int GALLERY_CODE = 10;
    Button bt1;
    EditText et1;
    EditText et2;
    EditText et3;
    Button bt2;
    Button bt3;
    ImageView userimage;
    TextView userid;
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write,container,false);


        /*permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }*/

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.writefragment_relativelayout);

        userimage = (ImageView)view. findViewById(R.id.user_image);
        bt1 = (Button)view.findViewById(R.id.bt_upload);
        et1 = (EditText)view.findViewById(R.id.et_title);
        et2 = (EditText)view.findViewById(R.id.et_money);
        et3 = (EditText)view.findViewById(R.id.et_content);
        //bt3 = (Button)findViewById(R.id.image_upload);
        ivPreview = (ImageView)view.findViewById(R.id.img3);

        database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");


        spinner = (Spinner) view.findViewById(R.id.category);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                uploadFile();

            }
        });

        ivPreview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //choose image file
                /*
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,GALLERY_CODE);*/
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            //Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
                ivPreview.setImageBitmap(bitmap);
                ivPreview.setScaleType(ImageView.ScaleType.FIT_XY);
                ivPreview.setPadding(5,5,5,5);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null && spinner.getSelectedItemPosition()!=0) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();


            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";

            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://talk-4c5a4.appspot.com").child("writings/" + filename);
            Toast.makeText(getActivity(), "upload 진행중 ", Toast.LENGTH_SHORT).show();
            final adapter adapter  = new adapter();
            adapter.ad_title = et1.getText().toString();
            adapter.ad_money = et2.getText().toString();
            adapter.ad_content = et3.getText().toString();
            adapter.ad_category = spinner.getSelectedItem().toString();

            //upload
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getActivity(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            adapter.imageUrl = downloadUrl.toString();
                            adapter.ad_useruid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                            database.getReference().child("writings").push().setValue(adapter);

                            //글 비우는 코드
                            et1.setText("");
                            et2.setText("");
                            et3.setText("");
                            spinner.setSelection(0);
                            Bitmap bit  = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.image);
                            ivPreview.setImageBitmap(bit);


                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        }
        else if(filePath == null) {
            Toast.makeText(getActivity(), "사진 파일을 선택하세요.", Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getSelectedItemPosition()==0){
            Toast.makeText(getActivity(), "카테고리를 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

}