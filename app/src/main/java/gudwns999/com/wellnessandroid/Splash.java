package gudwns999.com.wellnessandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import gudwns999.com.wellnessandroid.Login.Login;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        //핸들러를 통해 Splash Activity에서 Login Activity로 넘어가고 Splash 화면은 finish()
        android.os.Handler handler = new android.os.Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                startActivity(new Intent(Splash.this, Login.class));
                finish();
            }
        };
        //delay는 약 1초간 준다.
        handler.sendEmptyMessageDelayed(0, 1000);
    }
}
