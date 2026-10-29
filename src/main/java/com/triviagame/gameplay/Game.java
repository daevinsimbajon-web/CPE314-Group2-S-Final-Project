package com.triviagame;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;

/* Features:
 * 1. Queue (FIFO): Serves questions in order via poll() / offer()
 * 2. Stack (LIFO): Tracks scored answers for instant undo rollback via push() / pop()
 * 
 * Generic <TQuestion> allows your group members to use String or any custom Question class.
 */

public class GameEngine<TQuestion> {

    // Snapshot record of a scored turn stored in the Undo Stack.
  
    public static class ScoredTurn<TQuestion> {
        private final TQuestion question;
        private final String answer;
        private final int points;
        private final int previousScore;

        public ScoredTurn(TQuestion question, String answer, int points, int previousScore) {
            this.question = question;
            this.answer = answer;
            this.points = points;
            this.previousScore = previousScore;
        }

        public TQuestion getQuestion() { return question; }
        public String getAnswer() { return answer; }
        public int getPoints() { return points; }
        public int getPreviousScore() { return previousScore; }
    }

    // Queue to serve questions in FIFO order
    private final Deque<TQuestion> questionQueue = new ArrayDeque<>();

    // Stack for 'undo last scored answer' in LIFO order
    private final Stack<ScoredTurn<TQuestion>> undoStack = new Stack<>();

    private int currentScore = 0;
    private TQuestion activeQuestion = null;

    public GameEngine() {}

    //Enqueue a question to the rear of the Queue (FIFO).
    public void enqueueQuestion(TQuestion question) {
        Objects.requireNonNull(question, "Question cannot be null");
        questionQueue.offer(question);
    }

    /*Dequeue and serve the next question in FIFO order.
     * @return The next question, or null if the queue is empty.
     */
    
    public TQuestion serveNextQuestion() {
        this.activeQuestion = questionQueue.poll();
        return this.activeQuestion;
    }

    /*
     * Record an answer and update score.
     * Pushes the turn onto the Undo Stack (LIFO).
     */
    
    public void recordAnswer(String answer, int pointsAwarded) {
        if (activeQuestion == null) {
            throw new IllegalStateException("No active question to answer. Call serveNextQuestion() first.");
        }

        int previousScore = this.currentScore;
        this.currentScore += pointsAwarded;

        // Push snapshot onto the Undo Stack (LIFO)
        undoStack.push(new ScoredTurn<>(activeQuestion, answer, pointsAwarded, previousScore));
        this.activeQuestion = null;
    }

    /**
     * UNDO LAST SCORED ANSWER (STACK LIFO):
     * 1. Pops the last action from the Undo Stack
     * 2. Reverts the score to its previous value
     * 3. Requeues the question at the FRONT of the Question Queue
     * 4. Restores the active question
     */
    
    public ScoredTurn<TQuestion> undoLastAnswer() {
        if (undoStack.isEmpty()) {
            return null;
        }

        // 1. Pop from LIFO stack
        ScoredTurn<TQuestion> lastTurn = undoStack.pop();

        // 2. Revert score
        this.currentScore = lastTurn.getPreviousScore();

        // 3. Put question back at the FRONT of the queue
        questionQueue.addFirst(lastTurn.getQuestion());
        this.activeQuestion = lastTurn.getQuestion();

        return lastTurn;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean hasMoreQuestions() {
        return !questionQueue.isEmpty();
    }

    public int getRemainingQuestionsCount() {
        return questionQueue.size();
    }

    public int getUndoCount() {
        return undoStack.size();
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public TQuestion getActiveQuestion() {
        return activeQuestion;
    }

    public Queue<TQuestion> getQuestionQueue() {
        return questionQueue;
    }

    public Stack<ScoredTurn<TQuestion>> getUndoStack() {
        return undoStack;
    }

    public void reset() {
        questionQueue.clear();
        undoStack.clear();
        currentScore = 0;
        activeQuestion = null;
    }
}
