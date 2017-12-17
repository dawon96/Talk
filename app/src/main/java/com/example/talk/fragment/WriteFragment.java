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


        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.writefragment_relativelayout);

        userimage = (ImageView)view. findViewById(R.id.user_image);
        bt1 = (Button)view.findViewById(R.id.bt_upload);
        et1 = (EditText)view.findViewById(R.id.et_title);
        et2 = (EditText)view.findViewById(R.id.et_money);
        et3 = (EditText)view.findViewById(R.id.et_content);
        ivPreview = (ImageView)view.findViewById(R.id.img3);

        database = FirebaseDatabase.getInstance();

        spinner = (Spinner) view.findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            try {
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
       if (filePath != null && spinner.getSelectedItemPosition()!=0) {
           final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("업로드중...");
            progressDialog.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";

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
                            Bitmap bit  = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_image);
                            ivPreview.setImageBitmap(bit);
                            ivPreview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ivPreview.setPadding(60,60,60,60);

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