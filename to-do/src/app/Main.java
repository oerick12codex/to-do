package app;
import java.util.Scanner;
public class Main {
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        while(true) {
            System.out.println("\n === Menu ===");
            System.out.println("1. Adicionar tarefa");
            System.out.println("2. listar tarefas");
            System.out.println("3. Marcar tarefa como concluida");
            System.out.println("4. remover tarefa");
            System.out.println("5. sair");

            System.out.println("Escolha uma opcao:");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao) {
                case 1:
                    System.out.println("Digite o titulo da tarefa:");
                    String title = scanner.nextLine();
                    taskManager.addtarefa(title);
                    break;
                case 2:
                    taskManager.listarTarefas();
                    break;
                case 3:
                    System.out.println("indice da tarefa: ");
                    int completeIndex = scanner.nextInt();
                    taskManager.completTask(completeIndex);
                    break;
                case 4:
                    System.out.println("indice da tarefa: ");
                    int removeIndex = scanner.nextInt();
                    taskManager.removerTarefa(removeIndex);
                    break;
                case 5:
                    System.out.println("Saindo...");
                    scanner.close();
                default:
                    System.out.println("seja racional, seu jumento");
            }

        }
    }
}
