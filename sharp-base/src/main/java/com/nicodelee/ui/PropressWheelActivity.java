package com.nicodelee.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import com.nicodelee.common.view.progress.ProgressWheel;
import com.nicodelee.sharp.R;
import java.util.Random;

/**
 * Created by NocodeLee on 15/12/10.
 * Email：lirizhilirizhi@163.com
 */
public class PropressWheelActivity extends AppCompatActivity {
  private ProgressWheel pwOne;
  private boolean wasSpinning = false;
  private SeekBar seekBarProgress;
  private Button btnSpin, btnIncrement, btnRandom;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.progress_wheel_activity);
    pwOne = (ProgressWheel) findViewById(R.id.progressBarTwo);
    seekBarProgress = (SeekBar) findViewById(R.id.progressAmount);
    btnSpin = (Button) findViewById(R.id.btn_spin);
    btnIncrement = (Button) findViewById(R.id.btn_increment);
    btnRandom = (Button) findViewById(R.id.btn_random);

    seekBarProgress.setOnSeekBarChangeListener(new ProgressUpdater(pwOne));
    btnSpin.setOnClickListener(new SpinListener(pwOne, btnSpin, btnIncrement, seekBarProgress));
    btnIncrement.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        pwOne.incrementProgress(36);
      }
    });
    btnRandom.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        //randomBg(pwOne);
        styleRandom(pwOne, getBaseContext());
      }
    });
  }

  @Override public void onPause() {
    super.onPause();
    if (wasSpinning = pwOne.isSpinning()) pwOne.stopSpinning();
  }

  @Override public void onResume() {
    super.onResume();
    if (wasSpinning) {
      pwOne.startSpinning();
    }
    wasSpinning = false;
  }

  private static final class SpinListener implements View.OnClickListener {

    private boolean isRunning = false;
    private final ProgressWheel wheel;
    private final Button spinButton, incButton;
    private final SeekBar seekBar;
    private int cachedProgress = 0;

    SpinListener(ProgressWheel wheel, Button spinButton, Button incButton, SeekBar seekBar) {
      this.wheel = wheel;
      this.spinButton = spinButton;
      this.incButton = incButton;
      this.seekBar = seekBar;
    }

    @Override public void onClick(View button) {
      isRunning = !isRunning;
      if (isRunning) {
        cachedProgress = wheel.getProgress();
        wheel.resetCount();
        wheel.setText("Spinning...");
        wheel.startSpinning();
        spinButton.setText("Stop spinning");
      } else {
        spinButton.setText("Start spinning");
        //wheel.setText(cachedProgress+"");
        wheel.setText("");
        wheel.stopSpinning();
        wheel.setProgress(cachedProgress);
      }
      seekBar.setEnabled(!isRunning);
      incButton.setEnabled(!isRunning);
    }
  }

  private static final int[] barWidths = { 25, 2 };
  private static final int[] circleColors = { Integer.MAX_VALUE, 0xFF2C8437, };
  private static final int[] barColors = { 0xFF000000, 0xFF236167, };
  private static final int[] contourColors = { Integer.MAX_VALUE, 0xFF2F4172, };

  private static void styleRandom(ProgressWheel wheel, Context ctx) {
    wheel.setRimShader(null);
    wheel.setRimColor(0xFFFFFFFF);
    wheel.setCircleColor(0x00000000);
    wheel.setBarColor(0xFF000000);
    wheel.setContourColor(0xFFFFFFFF);
    wheel.setBarWidth(pxFromDp(ctx, 20));
    wheel.setBarLength(pxFromDp(ctx, 100));
    wheel.setSpinSpeed(4f);
    wheel.setDelayMillis(3);
  }

  private static void randomBg(ProgressWheel wheel) {
    Random random = new Random();
    int firstColour = random.nextInt();
    int secondColour = random.nextInt();
    int patternSize = (1 + random.nextInt(3)) * 8;
    int patternChange = (1 + random.nextInt(3)) * 8;
    int[] pixels = new int[patternSize];
    for (int i = 0; i < patternSize; i++) {
      pixels[i] = (i > patternChange) ? firstColour : secondColour;
    }
    wheel.setRimShader(
        new BitmapShader(Bitmap.createBitmap(pixels, patternSize, 1, Bitmap.Config.ARGB_8888),
            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
  }

  public static int pxFromDp(final Context context, final float dp) {
    return (int) (dp * context.getResources().getDisplayMetrics().density);
  }

  private static class ProgressUpdater implements SeekBar.OnSeekBarChangeListener {

    private final ProgressWheel wheel;

    ProgressUpdater(ProgressWheel wheel) {
      this.wheel = wheel;
    }

    @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
      double progress = 360.0 * (seekBar.getProgress() / 100.0);
      wheel.setProgress((int) progress);
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
    }
  }
}