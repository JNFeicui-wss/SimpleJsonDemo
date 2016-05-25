package fuicui.edu.simplejsondemo;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    public static final int MESSAGE = 1;
    private TextView mName1;
    private TextView mName2;
    private TextView mAge1;
    private TextView mAge2;
    private TextView mClassName;
    private String mClassName1;
    private String             url      = "http://192.168.1.147:8080/index2.jsp";
    private List<StudentsBean> listData =null;
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE:
                    try {
                        mName1.setText(listData.get(0).name);
                        mAge1.setText(String.valueOf(listData.get(0).age));
                        mName2.setText(listData.get(1).name);
                        mAge2.setText(String.valueOf(listData.get(1).age));
                        mClassName.setText(mClassName1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName1 = (TextView) findViewById(R.id.tv_name01);
        mName2 = (TextView) findViewById(R.id.tv_name02);
        mAge1 = (TextView) findViewById(R.id.tv_age01);
        mAge2 = (TextView) findViewById(R.id.tv_age02);
        mClassName = (TextView) findViewById(R.id.tv_class);
        new Thread(){
            @Override
            public void run() {
                String json  = getJsonData(url);
                listData = parseJson(json);
                handler.sendEmptyMessage(MESSAGE);

            }
        }.start();
    }
    private List<StudentsBean> parseJson(String json) {
        List<StudentsBean> list = new ArrayList<>();
        try {
            JSONObject students = new JSONObject(json);
            mClassName1= (String) students.get("classname");

            JSONArray studentsArray = (JSONArray) students.get("students");
            for (int i = 0; i<studentsArray.length(); i++){
                JSONObject jsonObject = (JSONObject) studentsArray.get(i);
                StudentsBean bean  = new StudentsBean();
                bean.name = (String) jsonObject.get("name");
                Log.d("name",bean.name);

                bean.age = jsonObject.getInt("age");
                Log.d("age", String.valueOf(bean.age));
                list.add(bean);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  list;
    }
    private String getJsonData(String url){
        String jsonData = null;
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            InputStream inputStream = connection.getInputStream();

            String jsonDatas= null;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int leng = 0;
            while((leng = inputStream.read(bytes)) != -1){
                out.write(bytes,0,leng);
            }
            out.close();
            inputStream.close();
           jsonData = out.toString();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("********", "2b2b2b2b2b2b2b2b");
        }
        return   jsonData;
    }

}


