package eugenebo.com.github.geoquiz;

public class Question {
    private int textResId;
    private boolean answerTrue;
    private boolean isAnswered;
    private boolean isCheated;

    public Question(int textResId, boolean answerTrue) {
        this.textResId = textResId;
        this.answerTrue = answerTrue;
        this.isAnswered = false;
        this.isCheated = false;
    }

    public int getTextResId() {
        return textResId;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public boolean isCheated() {
        return isCheated;
    }

    public void setCheated(boolean cheated) {
        isCheated = cheated;
    }
}
