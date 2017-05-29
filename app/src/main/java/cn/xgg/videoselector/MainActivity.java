package cn.xgg.videoselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initOperation();
    }

    private void initOperation() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content,
                        new VideoFragment())
                .commit();
    }


}
