import java.util.Scanner;

class No23 {
    int[] chaves = new int[2];   // máximo 2 chaves
    No23[] filhos = new No23[3]; // 2-3: máximo 3 filhos
    int numChaves = 0; //controle de chaves contidas

    boolean estaCheio() {
        return numChaves == 2; //max de chaves
    }

    boolean ehFolha() {
        return filhos[0] == null; //possui filhos?
    }

    boolean estaVazio() {
        return numChaves == 0;
    }
}

public class doistresarvore {
    private No23 raiz;

    public doistresarvore() {
        raiz = new No23(); //construtor inicializa raiz
    }

    //insere
    public void inserir(int valor) {
        if (raiz == null) {
            raiz = new No23(); //add nova raiz se raiz for nula
        }
        No23 novaRaiz = inserirRec(raiz, valor); //recursao
        if (novaRaiz != raiz) {
            raiz = novaRaiz; //se raiz alterar atualiza (divisão)
        }
    }

    private No23 inserirRec(No23 no, int valor) {
        if (no.ehFolha()) {
            return inserirEmFolha(no, valor); //insere em folha
        }

        int i = 0;
        while (i < no.numChaves && valor > no.chaves[i]) {
            i++; //encontra posição para inserir
        }

        No23 filhoAtualizado = inserirRec(no.filhos[i], valor);

        if (filhoAtualizado == no.filhos[i]) {
            return no;
        }

        return inserirChavePromovida(no, filhoAtualizado.chaves[0], //chave promovida altera filho
                filhoAtualizado.filhos[0], filhoAtualizado.filhos[1]);
    }

    private No23 inserirEmFolha(No23 folha, int valor) {
        if (!folha.estaCheio()) {
            inserirOrdenado(folha, valor);
            return folha;
        }
        return dividirFolhaSimples(folha, valor);
    }

    private No23 dividirFolhaSimples(No23 folha, int novaChave) {
        int[] todasChaves = {folha.chaves[0], folha.chaves[1], novaChave};
        ordenarTresChaves(todasChaves);

        //passos juntas tds as chaves
        // ordena
        // cria folha com menor chave e nova chave
        //chave pai(meio) promovida

        No23 novoPai = new No23();
        No23 novoIrmao = new No23();

        folha.chaves[0] = todasChaves[0];
        folha.numChaves = 1;

        novoIrmao.chaves[0] = todasChaves[2];
        novoIrmao.numChaves = 1;

        novoPai.chaves[0] = todasChaves[1];
        novoPai.numChaves = 1;
        novoPai.filhos[0] = folha;
        novoPai.filhos[1] = novoIrmao;

        return novoPai; //integra ou cria em raiz
    }

    private void inserirOrdenado(No23 no, int valor) {
        int i = no.numChaves - 1;
        while (i >= 0 && valor < no.chaves[i]) {
            no.chaves[i + 1] = no.chaves[i];
            i--;
        }
        no.chaves[i + 1] = valor;
        no.numChaves++;
    }

    private No23 inserirChavePromovida(No23 no, int chavePromovida, No23 filhoEsq, No23 filhoDir) {
        if (!no.estaCheio()) {
            inserirChaveComFilhos(no, chavePromovida, filhoEsq, filhoDir);
            return no;
        }
        return dividirNoInternoSimples(no, chavePromovida, filhoEsq, filhoDir); //no pai esta cheio
    }

    private void inserirChaveComFilhos(No23 no, int chave, No23 filhoEsq, No23 filhoDir) {
        int pos = no.numChaves - 1;
        int idxFilho = no.numChaves;
        //percorre as chaves de tras p frente deslocando os filhos

        while (pos >= 0 && chave < no.chaves[pos]) {
            no.chaves[pos + 1] = no.chaves[pos];
            no.filhos[idxFilho + 1] = no.filhos[idxFilho];
            pos--;
            idxFilho--;
        }

        no.chaves[pos + 1] = chave;
        no.filhos[idxFilho] = filhoEsq;
        no.filhos[idxFilho + 1] = filhoDir;
        no.numChaves++;
    }

    private No23 dividirNoInternoSimples(No23 no, int novaChave, No23 novoFilhoEsq, No23 novoFilhoDir) {
        int pos = 0;
        while (pos < no.numChaves && novaChave > no.chaves[pos]) {
            pos++;
        }

        // Cria arrays temporários maiores (3 chaves, 4 filhos)
        int[] chavesTemp = new int[3];
        No23[] filhosTemp = new No23[4];

        // Copia manualmente as chaves originais
        for (int i = 0; i < no.numChaves; i++) {
            chavesTemp[i] = no.chaves[i];
        }

        // Move as chaves para abrir espaço na posição de inserção
        for (int i = no.numChaves; i > pos; i--) {
            chavesTemp[i] = chavesTemp[i - 1];
        }
        chavesTemp[pos] = novaChave;

        // Copia manualmente os filhos originais
        for (int i = 0; i <= no.numChaves; i++) {
            filhosTemp[i] = no.filhos[i];
        }

        // Move filhos à direita para abrir espaço para os novos filhos
        for (int i = no.numChaves + 1; i > pos + 1; i--) {
            filhosTemp[i] = filhosTemp[i - 1];
        }

        filhosTemp[pos] = novoFilhoEsq;
        filhosTemp[pos + 1] = novoFilhoDir;

        // Cria os nós resultantes
        No23 novoPai = new No23();
        No23 novoIrmao = new No23();

        // Atualiza o nó original (lado esquerdo)
        no.chaves[0] = chavesTemp[0];
        no.numChaves = 1;
        no.filhos[0] = filhosTemp[0];
        no.filhos[1] = filhosTemp[1];
        no.filhos[2] = null;

        // Cria o novo irmão (lado direito)
        novoIrmao.chaves[0] = chavesTemp[2];
        novoIrmao.numChaves = 1;
        novoIrmao.filhos[0] = filhosTemp[2];
        novoIrmao.filhos[1] = filhosTemp[3];
        novoIrmao.filhos[2] = null;

        // Cria o novo pai com a chave promovida
        novoPai.chaves[0] = chavesTemp[1];
        novoPai.numChaves = 1;
        novoPai.filhos[0] = no;
        novoPai.filhos[1] = novoIrmao;

        return novoPai;
    }



    private void ordenarTresChaves(int[] chaves) {
        if (chaves[0] > chaves[1]) {
            int temp = chaves[0];
            chaves[0] = chaves[1];
            chaves[1] = temp;
        }
        if (chaves[1] > chaves[2]) {
            int temp = chaves[1];
            chaves[1] = chaves[2];
            chaves[2] = temp;
        }
        if (chaves[0] > chaves[1]) {
            int temp = chaves[0];
            chaves[0] = chaves[1];
            chaves[1] = temp;
        }//ordena local das chaves segundo ordem crescente
    }

    //remoção
    public void remover(int valor) {
        if (removerFolhaSimples(raiz, valor)) {
            System.out.println("-> Removido com sucesso.");

            // Ajusta a raiz se ficar vazia
            if (raiz.numChaves == 0 && !raiz.ehFolha()) {
                raiz = raiz.filhos[0];
            }
        } else {
            System.out.println("-> Valor não encontrado em folha!");
        }
    }

    private boolean removerFolhaSimples(No23 no, int valor) {
        //busca recursivamente até encontrar a chave em folha
        //se encontrou:
        // remove deslocando a chave para preencher as lacunas
        //nao faz merge nem borrow nó interno dificil de aplicar

        if (no == null) return false;

        // Busca pelo valor no nó atual
        int i = 0;
        while (i < no.numChaves && valor > no.chaves[i]) {
            i++;
        }

        // Se encontrou o valor em uma folha
        if (i < no.numChaves && no.chaves[i] == valor && no.ehFolha()) {
            // Remove a chave
            for (int j = i; j < no.numChaves - 1; j++) {
                no.chaves[j] = no.chaves[j + 1];
            }
            no.numChaves--;
            return true;
        }

        // Se não encontrou ou não é folha, continua buscando nos filhos
        if (!no.ehFolha()) {
            return removerFolhaSimples(no.filhos[i], valor);
        }

        return false;
    }

    // busca
    public boolean buscar(int valor) {
        return buscarRec(raiz, valor);
    }

    private boolean buscarRec(No23 no, int valor) {
        if (no == null) return false;

        //comparações e vai descendo pelo nó

        int i = 0;
        while (i < no.numChaves && valor > no.chaves[i]) {
            i++; //faz a procura
        }

        if (i < no.numChaves && valor == no.chaves[i]) {
            return true; // se chou
        }

        if (no.ehFolha()) {
            return false; //se nao achou
        }

        return buscarRec(no.filhos[i], valor);
    }

    // imppressão
    public void imprimir() {
        System.out.println("Árvore 2-3:");
        imprimirRec(raiz, "", true);

    }

    private void imprimirRec(No23 no, String prefixo, boolean ultimo) {
        if (no == null || no.estaVazio()) return;

        System.out.print(prefixo);
        System.out.print(ultimo ? "└─ " : "├─ ");
        System.out.print("[");
        for (int i = 0; i < no.numChaves; i++) {
            System.out.print(no.chaves[i]);
            if (i < no.numChaves - 1) System.out.print(" ");
        }
        System.out.println("]");

        if (!no.ehFolha()) {
            for (int i = 0; i <= no.numChaves; i++) {
                if (no.filhos[i] != null && !no.filhos[i].estaVazio()) {
                    imprimirRec(no.filhos[i], prefixo + (ultimo ? "   " : "│  "), i == no.numChaves);
                }
            }
        }
    }


    // main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        doistresarvore arvore = new doistresarvore();

        while (true) {
            System.out.println("Árvore 2-3");
            System.out.println("1 - Inserir");
            System.out.println("2 - Buscar");
            System.out.println("3 - Remover");
            System.out.println("4 - Mostrar");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            int op = sc.nextInt();
            if (op == 0) break;

            int valor;
            switch (op) {
                case 1:
                    System.out.print("Valor para inserir: ");
                    valor = sc.nextInt();
                    arvore.inserir(valor);
                    System.out.println(" Inserido.");
                    break;
                case 2:
                    System.out.print("Valor para buscar: ");
                    valor = sc.nextInt();
                    System.out.println(arvore.buscar(valor) ? " Encontrado " : " Não encontrado.");
                    break;
                case 3:
                    System.out.print("Valor para remover: ");
                    valor = sc.nextInt();
                    arvore.remover(valor);
                    break;
                case 4:
                    arvore.imprimir();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        sc.close();
    }
}