package ru.job4j.rxjava;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private Disposable sbr;
    private ProgressBar bar;
    private TextView info;
    private int startAt;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        bar = findViewById(R.id.load);
        info = findViewById(R.id.info);
        boolean recreated = bundle != null;
        startAt = recreated ? bundle.getInt("progress", 0) : 0;
        info.setText(startAt + "%");
        Button start = findViewById(R.id.start);
        start.setOnClickListener(v -> start(startAt));
        Button stop = findViewById(R.id.stop);
        stop.setOnClickListener(v -> stop());
        if (recreated) {
            start(startAt);
        }
    }

    public void start(int startAt) {
        if(sbr == null || sbr.isDisposed()) {
            this.sbr = Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(v -> {
                        info.setText(startAt + v.intValue() + "%");
                        bar.setProgress(startAt + v.intValue());
                    });
        }
    }

    public void stop() {
        this.sbr.dispose();
        startAt = 0;
        bar.setProgress(0);
        info.setText("0%");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("progress", bar.getProgress());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.sbr != null) {
            this.sbr.dispose();
        }
    }
}