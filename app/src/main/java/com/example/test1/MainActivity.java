package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动
    private static String url = "jdbc:mysql://10.230.56.52/retire_management?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
    private static String user = "root";// 数据库用户名
    private static String password = "mysqlmima";// 数据库密码

    private TextView textView;
    private TextView Contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }

    /**
     * 初始化组件
     */
    private void Init(){
        Contents=(TextView)findViewById(R.id.content);
        textView=(TextView)findViewById(R.id.TextView);

        ButtonClick buttonClick=new ButtonClick();
        textView.setOnClickListener(buttonClick);
    }

    /**
     * 向数据库追加数据
     * @param link
     * @throws SQLException
     */
    private  void setData(Connection link) throws SQLException {
        String sql = "insert into artical(id,articalName,artical) values(?,?,?)";
        PreparedStatement pst=link.prepareStatement(sql);
        pst.setObject(1,222);
        pst.setObject(2,"aaa");
        pst.setObject(3,"ffaaaf");
        pst.execute();
    }
    /**
     * 获取数据库中的数据
     * @param link
     * @return
     * @throws SQLException
     */
    private  String  getData(Connection link) throws SQLException{
        String Aritical = null;
        String commond = "select * from artical where id=2"; //找到id=2的行
        PreparedStatement pst = link.prepareStatement(commond);
        ResultSet rs = pst.executeQuery();

        if (rs != null){
            int column = rs.getMetaData().getColumnCount();
            while (rs.next()){
                String name = rs.getMetaData().getColumnName(3);
                Aritical = rs.getString(name);
            }
            pst.close();
            return  Aritical;

        }else {
            return null;
        }

    }

    private class ButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //   Android的数据库连接不能在主线程进行
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Class.forName(driver);// 动态加载类
                        Connection link = DriverManager.getConnection(url, user, password);
                        Log.i("","SUCCESS");

                        //TO DO
                        //.............操作数据库.............
                        String articals = getData(link);
                        Contents.setText(articals);
                        //The end

                        link.close();

                    } catch (Exception  e) {
                        Log.e("","ERROR");
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}


