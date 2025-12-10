[DOCUMENTATION.md](https://github.com/user-attachments/files/24089246/DOCUMENTATION.md)[Uploading D# Documentação do diretório src (package app)

Repositório: oerick12codex/to-do  
Arquivos analisados (package app): Main.java, Tarefa.java, TaskManager.java

A seguir documentei cada arquivo com foco na lógica: classes, métodos, estruturas de dados, complexidade, fluxos de uso, casos de borda e sugestões de melhoria.

---

## src/app/Main.java

### Objetivo geral
Classe contendo o ponto de entrada (main) da aplicação de linha de comando. Exibe um menu, lê entradas do usuário e delega operações ao TaskManager.

### Dependências
- java.util.Scanner
- app.TaskManager

### Estrutura / Classe
Classe: Main
- Método principal:
  - assinatura: public static void main(String[] args)
  - Descrição: loop interativo que mostra um menu, lê a opção do usuário e chama métodos de TaskManager conforme a escolha.
  - Parâmetros: args — argumentos de linha de comando (não utilizados).
  - Comportamento interno importante:
    - Cria um Scanner(System.in) para leitura de entradas.
    - Instancia TaskManager.
    - Entra em um loop while(true) que:
      1. Imprime o menu.
      2. Lê um inteiro com scanner.nextInt() para a opção.
      3. Usa switch(opcao) para tratar as cinco opções:
         - 1: pede título (scanner.nextLine()) e chama taskManager.addtarefa(title)
         - 2: chama taskManager.listarTarefas()
         - 3: pede índice (scanner.nextInt()) e chama taskManager.completTask(index)
         - 4: pede índice (scanner.nextInt()) e chama taskManager.removerTarefa(index)
         - 5: imprime "Saindo..." e faz scanner.close()
         - default: imprime mensagem "seja racional, seu jumento"
    - Observação: o programa não chama `break` nem `return` após o case 5, então após fechar o scanner, o loop continua — isso é um bug (ver abaixo).

### Comportamento / problemas observados
- Bug de saída: no case 5 (sair) o scanner é fechado, mas não há `break` ou `System.exit(0)` ou `return`, assim o fluxo continua (fall-through para default) e o loop permanece. Além disso, fechar o Scanner mas continuar a usar System.in causa problemas. Solução: após `scanner.close()` fazer `return;` ou `break` do loop ou chamar `System.exit(0)`.
- Leitura frágil: uso de `scanner.nextInt()` sem tratamento de InputMismatchException. Se o usuário digitar um texto não-numérico, a aplicação lança exceção e encerra.
- Mistura de responsabilidades: Main faz UI (I/O) e coordena diretamente a lógica; pode ser melhor separar UI (console) e lógica (TaskManager).
- Mensagem padrão ofensiva: `seja racional, seu jumento` — ajustar para mensagem informativa e amigável ao usuário.

### Sugestões de melhoria (Main)
- Tratar entrada com try/catch para InputMismatchException e consumir a linha inválida (scanner.nextLine()).
- Alterar o fluxo de saída para realmente terminar o programa (`break` no loop e `return` do main, ou `System.exit(0)`).
- Usar leituras por linha (`nextLine()`) e converter/validar para inteiro com Integer.parseInt para controlar erros.
- Implementar um método separado para exibir o menu e outro para processar a opção, melhorando testabilidade.
- Remover mensagens ofensivas e usar mensagens claras de erro/ajuda.
- Encapsular a UI em uma classe ConsoleUI para separar lógica de apresentação e facilitar testes.

---

## src/app/Tarefa.java

### Objetivo geral
Representa a entidade tarefa com título e status de conclusão. Classe de modelo/valor simples.

### Estrutura / Classe
Classe: Tarefa
- Pacote: app
- Campos:
  - private String title — título da tarefa; inicializado no construtor; não existe setter (imutabilidade parcial do título).
  - private boolean completed — indica se a tarefa está concluída; inicializado como false no construtor.
- Construtor:
  - public Tarefa(String title)
    - inicializa title e set completed = false.
- Métodos públicos:
  - public String getTitle()
    - Retorna o título da tarefa.
    - Complexidade: O(1).
  - public boolean isCompleted()
    - Retorna se a tarefa está concluída.
    - Complexidade: O(1).
  - public void complete()
    - Marca a tarefa como concluída (this.completed = true).
    - Efeito colateral: altera estado interno.
    - Complexidade: O(1).
  - @Override public String toString()
    - Formata a tarefa para exibição: retorna "[X] " + title se concluída, ou "[ ] " + title caso contrário.
    - Complexidade: O(1) (alocação de String).
- Métodos que faltam (sugestão):
  - equals() e hashCode() (útil para comparações e uso em coleções baseadas em hash).
  - Possibilidade de adicionar createdAt/updatedAt (Instant) se for necessário rastrear timestamps.
  - Validação do título (null/empty) no construtor.

### Observações de design
- Título não pode ser alterado após criação (não há setter), o que é uma decisão razoável.
- completed é mutável via método complete(); se for necessária reversão (marcar como não concluída), faltaria método undo/incomplete.
- Classe simples e adequada para uso em coleções.

---

## src/app/TaskManager.java

### Objetivo geral
Gerencia a coleção de tarefas em memória: adicionar, concluir, remover e listar. Atua como repositório/serviço simples em memória.

### Estrutura / Classe
Classe: TaskManager
- Pacote: app
- Campo principal:
  - private ArrayList<Tarefa> tarefas = new ArrayList<>();
    - Estrutura de dados: ArrayList fornece acesso por índice O(1), iteração rápida e append amortizado O(1). Remoção por índice causa deslocamento (O(n)).
    - Sugestão: declarar como List<Tarefa> tarefas = new ArrayList<>(); para programar contra a interface.
- Métodos públicos:
  - public void addtarefa(String title)
    - Assinatura: void addtarefa(String title)
    - Descrição: cria nova Tarefa com o título e adiciona ao final da lista.
    - Observações: não valida título (null ou vazio); o nome do método não segue convenção camelCase (`addTarefa`) e mistura português/inglês em outros métodos.
    - Complexidade: O(1) amortizado.
  - public void completTask(int index)
    - Assinatura: void completTask(int index)
    - Descrição: se index válido (0 <= index < size), obtém a tarefa e chama task.complete().
    - Observações: nome mistura português/inglês e contém typo (ideal: `completeTask` ou `completarTarefa`).
    - Complexidade: O(1) para get + O(1) para mudança boolean.
    - Comportamento em índice inválido: ignora a operação silenciosamente (nenhuma mensagem/erro).
  - public void removerTarefa(int index)
    - Assinatura: void removerTarefa(int index)
    - Descrição: remove a tarefa no índice se o índice for válido.
    - Complexidade: O(n) no pior caso (devido ao deslocamento dos elementos no ArrayList).
    - Comportamento em índice inválido: ignora silenciosamente.
  - public void listarTarefas()
    - Assinatura: void listarTarefas()
    - Descrição: imprime "nenhuma tarefa encontrada:" se a lista estiver vazia; caso contrário imprime cabeçalho "=== Tarefas ===" e, para cada tarefa, imprime "i - tarefa.toString()" onde i é índice (0-based).
    - Complexidade: O(n) para iteração.
    - Observações: apresenta índices 0-based para referência na CLI; pode confundir usuários (um sistema 1-based pode ser mais amigável).

### Casos de borda e comportamento atual
- Índices inválidos (negativos ou >= size) são simplesmente ignorados. Isso evita exceções, mas não informa o usuário quando a operação falha.
- Não há sincronização; se houver acessos concorrentes, ArrayList não é thread-safe.
- Sem persistência: dados são mantidos apenas em memória até o encerramento do processo.
- Nomes inconsistentes e convenções de estilo (camelCase, idioma) — dificulta manutenção.

### Sugestões de melhoria (TaskManager)
- Assinar o tipo do campo como List<Tarefa> tarefas = new ArrayList<>(); e importar java.util.List.
- Normalizar nomes de métodos para um idioma e convenção (ex.: addTarefa, completarTarefa, removerTarefa, listarTarefas).
- Retornar boolean em operações mutantes para indicar sucesso/falha:
  - public boolean completarTarefa(int index)
  - public boolean removerTarefa(int index)
  Isso facilita
OCUMENTATION.md…]()
