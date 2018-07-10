package eugenebo.com.github.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "eugenebo.com.github.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "eugenebo.com.github.geoquiz.answer_shown";
    private static final String EXTRA_BUTTON_CLICKED = "eugenebo.com.github.geoquiz.button_clicked";

    private boolean answerIsTrue;

    private Button showAnswerButton;
    private TextView answerTextView;

    private boolean buttonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CheatActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);


        if (savedInstanceState != null) {
            answerIsTrue = savedInstanceState.getBoolean(EXTRA_ANSWER_IS_TRUE, false);
            buttonClicked = savedInstanceState.getBoolean(EXTRA_BUTTON_CLICKED, false);

        } else answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);


        showAnswerButton = findViewById(R.id.show_answer_button);
        answerTextView = findViewById(R.id.answer_text_view);

        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = true;
                showAnswer();



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    int cx = showAnswerButton.getWidth() / 2;
                    int cy = showAnswerButton.getHeight() / 2;
                    float radius = showAnswerButton.getWidth();

                    Animator animator = ViewAnimationUtils
                            .createCircularReveal(showAnswerButton, cx, cy, radius, 0);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            showAnswerButton.setVisibility(answerTextView.INVISIBLE);
                        }
                    });

                    animator.start();
                } else showAnswerButton.setVisibility(answerTextView.VISIBLE);



            }
        });
        showAnswer();



    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }


    public static Intent newIntentWithExtra(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;

    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, intent);
    }

    private void showAnswer() {
        if (buttonClicked) {
            if (answerIsTrue) answerTextView.setText(R.string.true_button);
            else answerTextView.setText(R.string.false_button);
            setAnswerShownResult(true);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        outState.putBoolean(EXTRA_BUTTON_CLICKED, buttonClicked);


    }
}
