package app;

public class Tarefa {
    private String title;
    private boolean completed;

    public Tarefa(String title) {
        this.title = title;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        this.completed = true;
    }

    @Override
    public String toString() {
        return (completed ? "[X] " : "[ ] ") + title;
    }
}