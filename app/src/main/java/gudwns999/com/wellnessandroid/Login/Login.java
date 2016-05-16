package gudwns999.com.wellnessandroid.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import gudwns999.com.wellnessandroid.Main;
import gudwns999.com.wellnessandroid.R;

/**
 * Created by user on 2016-05-14.
 */
public class Login extends Activity {
    //로그인 php
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    //ID,PW, loginBtn, registBtn, 서버응답
    EditText ID_Edit,PW_Edit;
    Button login_Btn, regist_Btn;
    TextView response_Txt;
    @Override
    protected void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.login_layout);
        //객체참조 및 초기화
        ID_Edit = (EditText)findViewById(R.id.LID_Edit); ID_Edit.setText(null);
        PW_Edit = (EditText)findViewById(R.id.LPW_Edit); PW_Edit.setText(null);
        login_Btn = (Button)findViewById(R.id.login_Btn);
        regist_Btn = (Button)findViewById(R.id.regist_Btn);
        response_Txt = (TextView)findViewById(R.id.response_Txt);

        //로그인 버튼 눌렀을 시 이벤트 발생.
        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Login.this, "","확인중입니다. 기다려주세요.", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }).start();
            }
        });
        //회원 가입 버튼을 눌렀을 시.
        regist_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원 가입 화면으로 넘어감.
                Intent intent = new Intent(Login.this, Regist.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //로그인 처리 메소드
    void login(){
        try{
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://gudwns999.com/PHP/AndroidCheckID.php"); // make sure the url is correct. Test
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("user_id",ID_Edit.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("user_pw",PW_Edit.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            String[]res = response.split(";");

            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    response_Txt.setText("서버응답 : " + response);
                    dialog.dismiss();
                }
            });
            if(res[0].equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Login.this,"로그인 성공", Toast.LENGTH_SHORT).show();
                    }
                });
                //다음 Activity에게 값 넘겨주기
                Intent intent = new Intent(Login.this, Main.class);
                intent.putExtra("USERID",res[1]);
                intent.putExtra("LOCALNUM",res[2]);
                startActivity(intent);
                finish();
            }else{
                showAlert();
            }
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    //경고창 처리 메소드
    public void showAlert(){
        Login.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("로그인 에러");
                builder.setMessage("사용자가 없습니다.")
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
}
