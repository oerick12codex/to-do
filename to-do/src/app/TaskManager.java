package app;


import java.util.ArrayList;

public class TaskManager {

    private ArrayList<Tarefa> tarefas = new ArrayList<>();

    public void addtarefa(String title) {
        tarefas.add(new Tarefa(title));
    }

    public void completTask(int index) {
        if (index >= 0 && index < tarefas.size()) {
            tarefas.get(index).complete();
        }
    }

    public void removerTarefa(int index) {
        if (index >= 0 && index < tarefas.size()) {
            tarefas.remove(index);
        }
    }
    public void listarTarefas() {
        if (tarefas.isEmpty()) {
            System.out.println("nenhuma tarefa encontrada:");
            return;
        }
        System.out.println("=== Tarefas ===");
        for (int i = 0; i < tarefas.size(); i++) {
            System.out.println(i + " - " + tarefas.get(i));
        }
    }

}
