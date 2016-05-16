package gudwns999.com.wellnessandroid.Login;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import gudwns999.com.wellnessandroid.R;

/**
 * Created by user on 2016-05-14.
 */
public class Regist extends Activity {
    //등록 php
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    //regist의 ID,PW,PWConfirm, registBtn, 서버응답 부분
    EditText IDRegist_Edit, PWRegist_Edit, PWRegistConfirm_Edit;
    Button registRegist_Btn;
    TextView responseRegist_Txt;

    @Override
    protected void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.regist_layout);
        //객체 참조
        IDRegist_Edit = (EditText)findViewById(R.id.IDRegist_Edit);
        PWRegist_Edit = (EditText)findViewById(R.id.PWRegist_Edit);
        PWRegistConfirm_Edit = (EditText)findViewById(R.id.PWRegistConfirm_Edit);
        registRegist_Btn = (Button)findViewById(R.id.registRegist_Btn);
        responseRegist_Txt = (TextView)findViewById(R.id.responseRegist_Txt);
        //ID와 PW/PWConfirm이 모두 입력되었을때만 버튼활성화.
        registRegist_Btn.setEnabled(false);
        //비밀번호 확인까지 입력해야 버튼 활성화 할꺼임.
        PWRegistConfirm_Edit.addTextChangedListener(pwdWatcher);
        //등록버튼 눌렀을 시 이벤트 발생
        registRegist_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Regist.this, "버튼활성화", Toast.LENGTH_SHORT).show();

                dialog = ProgressDialog.show(Regist.this, "",
                        "기다려주세요...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkDuplication();
                    }
                }).start();
            }

        });
    }
    //아이디와 거울 동시 중복값 찾아내기 위한 기능.
    void checkDuplication(){
        try{
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://52.9.33.19/PHP/duplicationCheck.php"); // make sure the url is correct. Test
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user_id",IDRegist_Edit.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("user_pw",PWRegist_Edit.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response=httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    responseRegist_Txt.setText("서버응답 : " + response);
                    dialog.dismiss();
                }
            });

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    //이미 등록된 유저 발견되면 경고창
    public void showAlert(){
        Regist.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Regist.this);
                builder.setTitle("가입 에러");
                builder.setMessage("이미 등록된 아이디")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    //입력 조건이 맞아야만 버튼 활성화
    private TextWatcher pwdWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        //입력 끝나고 난 뒤 실행되는 이벤트
        @Override
        public void afterTextChanged(Editable s) {
            registRegist_Btn.setEnabled(true);
            registRegist_Btn.setBackgroundColor(Color.rgb(244, 67, 54));
        }
    };
}
