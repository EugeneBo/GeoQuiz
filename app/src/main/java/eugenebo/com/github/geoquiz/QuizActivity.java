package eugenebo.com.github.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX1 = "index1";
    private static final String KEY_INDEX2 = "index2";
    private static final String KEY_INDEX3 = "index3";


    private Button trueButton;
    private Button falseButton;
    private Button cheatButton;
    private Button restartButton;
    private ImageButton nextButton;
    private ImageButton prevButton;

    private TextView questionTextView;
    private int currentQuestionIndex = 0;
    private int numberOfCorrectAnswers = 0;
    private int numberOfAnsweredQuestions = 0;
    int toastMessage;

    private Question[] questionBank = new Question[]{
            new Question(R.string.question_Australia, true),
            new Question(R.string.question_Russia, false),
            new Question(R.string.question_America, true),
            new Question(R.string.question_GreatBritain, true),
            new Question(R.string.question_Europe, false),
            new Question(R.string.question_Africa, true),
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate (Bundle) called");
        setContentView(R.layout.activity_quiz);


        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt(KEY_INDEX1, 0);
            numberOfAnsweredQuestions = savedInstanceState.getInt(KEY_INDEX2);
            numberOfCorrectAnswers = savedInstanceState.getInt(KEY_INDEX3);
            questionBank = (Question[]) getLastCustomNonConfigurationInstance();

        }
        questionTextView = findViewById(R.id.question_text_view);
        falseButton = findViewById(R.id.false_button);
        trueButton = findViewById(R.id.true_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        restartButton = findViewById(R.id.restart_button);
        cheatButton = findViewById(R.id.cheat_button);


        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);

            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevQuestion();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();

            }
        });
        questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestionIndex = 0;
                numberOfCorrectAnswers = 0;
                numberOfAnsweredQuestions = 0;
                questionTextView.setTextColor(getResources().getColor(R.color.answer_text_view));
                for (Question question : questionBank) {
                    question.setAnswered(false);
                    question.setCheated(false);
                }
                updateQuestion();


            }
        });
        updateQuestion();

        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = questionBank[currentQuestionIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntentWithExtra(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {

        if (isAllQuestionsAnswered()) {
            showScore();
        } else {
            int question = questionBank[currentQuestionIndex].getTextResId();
            questionTextView.setText(question);
        }

        if (questionBank[currentQuestionIndex].isAnswered()) setButtonsDisabled();
        else setButtonsEnabled();

        if (currentQuestionIndex == 0 || isAllQuestionsAnswered()) prevButton.setEnabled(false);
        else prevButton.setEnabled(true);

        if (currentQuestionIndex == (questionBank.length - 1) || isAllQuestionsAnswered())
            nextButton.setEnabled(false);
        else nextButton.setEnabled(true);

    }

    private void nextQuestion() {
        if (currentQuestionIndex != (questionBank.length - 1)) {
            currentQuestionIndex++;
            updateQuestion();
        }
    }

    private void prevQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            updateQuestion();
        }
    }


    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = questionBank[currentQuestionIndex].isAnswerTrue();
        boolean isCheated = questionBank[currentQuestionIndex].isCheated();
        numberOfAnsweredQuestions++;
        if (isCheated) {
            toastMessage = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                toastMessage = R.string.correct_toast;
                numberOfCorrectAnswers++;
            } else toastMessage = R.string.incorrect_toast;
        }

        Toast toast = Toast.makeText(QuizActivity.this, toastMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 200);
        toast.show();

        setButtonsDisabled();

        if (isAllQuestionsAnswered()) {
            showScore();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSavedInstanceState");
        outState.putInt(KEY_INDEX1, currentQuestionIndex);
        outState.putInt(KEY_INDEX2, numberOfAnsweredQuestions);
        outState.putInt(KEY_INDEX3, numberOfCorrectAnswers);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        Log.i(TAG, "onRetainCustomNonConfigurationInstance");
        return questionBank;
    }

    private void setButtonsDisabled() {
        falseButton.setEnabled(false);
        trueButton.setEnabled(false);
        cheatButton.setEnabled(false);
        questionBank[currentQuestionIndex].setAnswered(true);
    }

    private void setButtonsEnabled() {
        falseButton.setEnabled(true);
        trueButton.setEnabled(true);
        cheatButton.setEnabled(true);
    }

    private void showScore() {
        int percentOfCorrectAnswers = (int) (double) ((100 * numberOfCorrectAnswers) / (questionBank.length));
        String result = "You have " + percentOfCorrectAnswers + "% of correct answers!";
        questionTextView.setTextColor(getResources().getColor(R.color.score_green));
        questionTextView.setText(result);

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        cheatButton.setEnabled(false);
    }

    private boolean isAllQuestionsAnswered() {
        return numberOfAnsweredQuestions == questionBank.length;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {  // this method calls when user click "back" button
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (intent == null) {
                return;
            }
            questionBank[currentQuestionIndex].setCheated(CheatActivity.wasAnswerShown(intent));
        }
    }
}
