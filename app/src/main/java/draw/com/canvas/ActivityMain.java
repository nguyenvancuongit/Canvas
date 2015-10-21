package draw.com.canvas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import draw.com.canvas.view.BouncingBallView;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View bouncingBallView = new BouncingBallView(this);
        setContentView(bouncingBallView);
        bouncingBallView.setBackgroundColor(Color.BLACK);
    }
}
