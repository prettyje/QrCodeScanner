package hansungpass.qrreadertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

   /*     ConnectThread ct = new ConnectThread();
        ct.start();
*/
        textView = (TextView)findViewById(R.id.textview);
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "ALL");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 0) {

            if(resultCode == Activity.RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                //위의 contents 값에 scan result가 들어온다.
                //Toast.makeText(this,contents,Toast.LENGTH_LONG).show();
                //tv_unique_code.setText(contents+"&imei="+Constant.imei);
                textView.setText(contents);
                output = contents;
               ConnectThread ct = new ConnectThread();
                ct.start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 쓰레드 (서버연결 및 프로그래스바) - 현재는 임시로 한 쓰레드에 그냥 넣어둠
    class ConnectThread extends Thread{
        //ProgressBar progressBar = (ProgressBar)findViewById(R.id.qr_bar);
        public void run(){
            String host = "223.194.134.161";
            int port = 5001;


            try {
                Socket socket = new Socket(host, port);
                System.out.println("서버로 연결되었습니다. : " + host + ", " + port);

                //output = "send2";
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(output);
                outstream.flush();
                System.out.println("서버로 보낸 데이터 : " + output);
                //Toast.makeText(MainActivity.this, "서버로 보낸 데이터 : " + output , Toast.LENGTH_SHORT).show();

                String output2 = "send2";
                ObjectOutputStream outstream2 = new ObjectOutputStream(socket.getOutputStream());
                outstream2.writeObject(output);
                outstream2.flush();
                System.out.println("서버로 보낸 데이터 : " + output2);
                outstream.close();
                socket.close();

            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("접근실패");
            }
        }
    }
}
