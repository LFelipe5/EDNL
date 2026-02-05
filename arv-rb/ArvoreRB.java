public class ArvoreRB <T extends Comparable<T>> implements ArvoreRubroNegraInterface<T> {
    private NoRubroNegro<T> raiz;

    public ArvoreRB() {
        this.raiz = null;
    }

    // Método para obter a raiz da árvore e inserir um nó na árvore Rubro-Negra
    @Override
    public void inserirNo(T dado) {
        if (getRaiz() == null) {
            NoRubroNegro<T> novo = new NoRubroNegro<>(dado);
            novo.setCor(Cor.PRETO);
            setRaiz(novo);
            System.out.println("Nó " + dado + " inserido como raiz (preto).");
        } else {
            NoRubroNegro<T> inserido = inserirRecursivo(getRaiz(), dado);
            if (inserido == null) return; // duplicado, nada a fazer
            inserido.setCor(Cor.VERMELHO);
            verificaRegras(inserido);
        }
    }

    // Método auxiliar para inserir um nó na árvore Rubro-Negra de forma recursiva
    private NoRubroNegro<T> inserirRecursivo(NoRubroNegro<T> no, T dado) {
        if (no == null) {
            // não deveria chegar aqui a partir da raiz; tratamento feito no chamador
            return new NoRubroNegro<>(dado);
        }

        int cmp = dado.compareTo(no.getDado());
        if (cmp < 0) {
            if (no.getEsquerda() == null) {
                NoRubroNegro<T> novo = new NoRubroNegro<>(dado);
                no.setEsquerda(novo);
                return novo;
            } else {
                return inserirRecursivo(no.getEsquerda(), dado);
            }
        } else if (cmp > 0) {
            if (no.getDireita() == null) {
                NoRubroNegro<T> novo = new NoRubroNegro<>(dado);
                no.setDireita(novo);
                return novo;
            } else {
                return inserirRecursivo(no.getDireita(), dado);
            }
        } else {
            // duplicado: não insere
            System.out.println("Nó " + dado + " já existe (duplicado). Inserção cancelada.");
            return null;
        }
    }

    // Método para verificar as regras da árvore Rubro-Negra após a inserção de um nó
    private void verificaRegras(NoRubroNegro<T> no) {
        NoRubroNegro<T> pai = no.getPai();
        NoRubroNegro<T> avo, tio;

        if (pai == null || pai.getCor() == Cor.PRETO) {
            return;
        } else { // pai rubro
            if (pai.getPai() != null) {
                avo = pai.getPai();
                if (avo.getEsquerda() == pai) {
                    tio = avo.getDireita();
                } else {
                    tio = avo.getEsquerda();
                }
                if (tio != null && tio.getCor() == Cor.VERMELHO) {
                    recoloracao(no, pai, tio, avo);
                } else {
                    rotacoes(no, pai, tio, avo);
                }
            }
        }
    }

    // Método para realizar a recoloração dos nós em caso de violação das regras da árvore Rubro-Negra
    private void recoloracao(NoRubroNegro<T> no, NoRubroNegro<T> pai, NoRubroNegro<T> tio, NoRubroNegro<T> avo) {
        // Aqui o pai e o tio do no serão pintados de negros e o avo de rubro
        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;
        pai.setCor(preto);
        tio.setCor(preto);

        if (avo != getRaiz()) {
            avo.setCor(vermelho);
        }

        verificaRegras(avo);
    }

    // Método para realizar as rotações necessárias em caso de violação das regras da árvore Rubro-Negra
    private void rotacoes(NoRubroNegro<T> no, NoRubroNegro<T> pai, NoRubroNegro<T> tio, NoRubroNegro<T> avo) {
        if (pai.getEsquerda() == no && avo.getEsquerda() == pai) {
            rotacaoDireita(avo);
        } else if (pai.getDireita() == no && avo.getDireita() == pai) {
            rotacaoEsquerda(avo);
        } else if (pai.getDireita() == no && avo.getEsquerda() == pai) {
            rotacaoDireitaDupla(avo);
        } else if (pai.getEsquerda() == no && avo.getDireita() == pai) {
            rotacaoEsquerdaDupla(avo);
        }
    }

    // Método de rotação simples à direita
    private NoRubroNegro<T> rotacaoDireita(NoRubroNegro<T> no) {
        NoRubroNegro<T> sucessorNovoNegro = no.getEsquerda(); // filho que será o negro no topo
        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;

        if (no.getPai() == null) { // se o no era raiz, o que ocupará seu lugar será a raiz
            sucessorNovoNegro.setPai(null);
            setRaiz(sucessorNovoNegro);
        } else {
            sucessorNovoNegro.setPai(no.getPai()); // pai do nó alterado agr será o pai do filho direito
            if (no == no.getPai().getEsquerda()) { // se o no desb. for filho esquerdo do seu pai
                no.getPai().setEsquerda(sucessorNovoNegro); // o filho esquerdo do pai do no desb. será o filho
                // direito do no desb.
            } else {
                no.getPai().setDireita(sucessorNovoNegro); // o filho direito do pai do no desb. será o filho
                // direito do no desb.
            }
        }
        no.setPai(sucessorNovoNegro);
        no.setEsquerda(sucessorNovoNegro.getDireita());

        if (sucessorNovoNegro.getDireita() != null) {
            sucessorNovoNegro.getDireita().setPai(no); //
        }

        sucessorNovoNegro.setDireita(no);
        sucessorNovoNegro.setCor(preto);
        
        no.setCor(vermelho);
        return sucessorNovoNegro;
    }

    // Método de rotação simples à esquerda
    private NoRubroNegro<T> rotacaoEsquerda(NoRubroNegro<T> no) {
        NoRubroNegro<T> sucessorNovoNegro = no.getDireita();
        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;

        if (no.getPai() == null) { // se o no era raiz, o que ocupará seu lugar será a raiz
            sucessorNovoNegro.setPai(null);
            setRaiz(sucessorNovoNegro);
        } else {
            sucessorNovoNegro.setPai(no.getPai()); // pai do nó alterado agr será o pai do filho esquerdo
            if (no == no.getPai().getEsquerda()) { // se o no desb. for filho esquerdo do seu pai
                no.getPai().setEsquerda(sucessorNovoNegro); // o filho esquerdo do pai do no desb. será o filho
                // direito do no desb.
            } else {
                no.getPai().setDireita(sucessorNovoNegro); // o filho direito do pai do no desb. será o filho
                // direito do no desb.
            }
        }
        no.setPai(sucessorNovoNegro);
        no.setDireita(sucessorNovoNegro.getEsquerda());

        if (sucessorNovoNegro.getEsquerda() != null) {
            sucessorNovoNegro.getEsquerda().setPai(no); //
        }

        sucessorNovoNegro.setEsquerda(no);
        sucessorNovoNegro.setCor(preto);
        no.setCor(vermelho);
        return sucessorNovoNegro;
    }
    
    // Métodos de rotação dupla direita
    private void rotacaoDireitaDupla(NoRubroNegro<T> no) {
        rotacaoEsquerda(no.getEsquerda());
        rotacaoDireita(no);
    }

    // Método de rotação dupla esquerda
    private void rotacaoEsquerdaDupla(NoRubroNegro<T> no) {
        rotacaoDireita(no.getDireita());
        rotacaoEsquerda(no);
    }

    // Método para contar a quantidade de filhos de um nó
    private int quantidadeFilhos(NoRubroNegro<T> no) {
        if (no == null) return 0;
        int count = 0;
        if (no.getEsquerda() != null) {
            count++;
        }
        if (no.getDireita() != null) {
            count++;
        }
        return count;
    }

    // Método para excluir um nó da árvore Rubro-Negra, considerando as regras de balanceamento
    @Override
    public void excluirNo(T dado) {
        NoRubroNegro<T> no = buscarNo(dado);
        if (no == null) return;

        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;
        int quantFilhos = quantidadeFilhos(no);

        if (quantFilhos == 0) {
            if (no == getRaiz()) {
                setRaiz(null);
                remover(no);
            }
            if (no.getCor() == vermelho) { // se é rubro, só remove
                remover(no);
            } else if (no.getCor() == preto) {
                remover(no);
                boolean isLeft = no == no.getPai().getEsquerda();
                checagemDuploNegro(no.getPai(), isLeft); // vai direto para a situação 3 (casos recursivos), is left aqui mostrará se n é filho esquerdo
            }

        } else if (quantFilhos == 1) {
            if (no == getRaiz()) { // no só tem um filho e é a raiz. Neste caso ele só pode ter filho rubro pelas
                // regras, e este será pintado de preto
                setRaiz(no.getEsquerda() != null ? no.getEsquerda() : no.getDireita()); // se no é a raiz, seu filho
                // será a raiz
                if (no.getDireita() != null && no.getDireita().getCor() == vermelho) {
                    no.getDireita().setCor(preto);
                }
                remover(no);
            }
            if (no.getCor() == vermelho) { // se tem 1 filho e é rubro o filho é negro pelas regras só substitui
                remover(no);
            } else if (no.getCor() == preto) { // no é negro e seu filho pode ser rubro ou negro
                // Achar posição do seu filho
                NoRubroNegro<T> filho;
                if(no.getEsquerda() != null){
                    filho = no.getEsquerda();
                } else {
                    filho = no.getDireita();
                }

                if(filho.getCor() == vermelho){ // Prévia da situação 2, pinta filho de negro e acabou
                    filho.setCor(preto);
                    remover(no);
                }else{ // Se filho tbm é negro, segue direto para situação 3
                    remover(no);
                    boolean isLeft = filho == no.getEsquerda();
                    checagemDuploNegro(no, isLeft); // is left aqui mostrará se seu unico filho é esquerdo
                }
            }

        } else { // casos que tem 2 filhos seguem para as 4 possíveis situações
            removerComRegras(no);
        }
    }

    // Método para buscar um nó desejado presente na árvore
    @Override
    public NoRubroNegro<T> buscarNo(T dado) {
        return buscarNoRecursivo(getRaiz(), dado);
    }

    // Método auxiliar para buscar recursivamente um nó desejado presente na árvore
    private NoRubroNegro<T> buscarNoRecursivo(NoRubroNegro<T> noAtual, T dado) {
      // Caso 1: Nó não encontrado (chegou ao fim de um galho)
      if (noAtual == null) {
         return null;
      }
      // Caso 2: Encontrado
      if (noAtual.getDado().equals(dado)) {
         return noAtual;
      }
        
      // Caso 3: (Passo Recursivo) Procura na sub-árvore esquerda
      NoRubroNegro<T> noEncontrado = buscarNoRecursivo(noAtual.getEsquerda(), dado);
        
      // Se não achou na esquerda, procura na direita
      if (noEncontrado == null) {
         noEncontrado = buscarNoRecursivo(noAtual.getDireita(), dado);
      }

      return noEncontrado;
   }
    
   // Método auxiliar para remover um nó com 2 filhos, considerando as regras de balanceamento da árvore Rubro-Negra
    private void removerComRegras(NoRubroNegro<T> no) {
        NoRubroNegro<T> sucessor = encontrarMenorNoRB(no.getDireita());
        NoRubroNegro<T> paiSucessor = sucessor.getPai();
        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;

        boolean isLeft = false;

        // Verifico se é filho esquerdo
        if (sucessor == sucessor.getPai().getEsquerda()) {
            isLeft = true;
        }

        // SITUAÇÃO 1: NO RUBRO E SUCESSOR RUBRO
        if (no.getCor() == vermelho && sucessor.getCor() == vermelho && sucessor != null) {
            remover(no);
            // SITUAÇÃO 2: NO NEGRO E SUCESSOR RUBRO
        } else if (no.getCor() == preto && sucessor.getCor() == vermelho && sucessor != null) {
            sucessor.setCor(preto);
            remover(no);
            // SITUAÇÃO 3: NO NEGRO E SUCESSOR NEGRO
        } else if (no.getCor() == preto && sucessor.getCor() == preto && sucessor != null) {
            remover(no);
            checagemDuploNegro(paiSucessor, isLeft);

            // SITUAÇÃO 4: NO RUBRO E SUCESSOR NEGRO
        } else if (no.getCor() == vermelho && sucessor.getCor() == preto && sucessor != null) {
            sucessor.setCor(vermelho);
            remover(no);
            checagemDuploNegro(paiSucessor, isLeft);

        }
    }

    // Método que remove fisicamente o nó com dado igual a `dado`.
    // Se o nó tiver dois filhos, substitui o dado pelo sucessor (menor da subárvore direita)
    // e remove o sucessor (que terá no máximo um filho).
    private NoRubroNegro remover(NoRubroNegro<T> no) {
        // Se tem 2 filhos, troca o dado com o sucessor e passa a remover o sucessor
        if (no.getEsquerda() != null && no.getDireita() != null) {
            NoRubroNegro<T> sucessor = encontrarMenorNoRB(no.getDireita());
            no.setDado(sucessor.getDado());
            no = sucessor; // iremos remover o sucessor abaixo
        }

        // Agora `no` tem no máximo um filho
        NoRubroNegro<T> filho = (no.getEsquerda() != null) ? no.getEsquerda() : no.getDireita();

        if (filho != null) { // caso com um filho
            if (no.getPai() == null) { // removendo raiz
                setRaiz(filho);
                filho.setPai(null);
            } else {
                if (no == no.getPai().getEsquerda()) {
                    no.getPai().setEsquerda(filho);
                } else {
                    no.getPai().setDireita(filho);
                }
            }
        } else { // caso folha
            if (no.getPai() == null) { // é a raiz
                setRaiz(null);
            } else {
                if (no == no.getPai().getEsquerda()) {
                    no.getPai().setEsquerda(null);
                } else {
                    no.getPai().setDireita(null);
                }
            }
        }

        return no;
    }

    // Método auxiliar para encontrar o menor nó da subárvore direita
    // Usado para encontrar o sucessor em casos de remoção de nós com 2 filhos
    private NoRubroNegro<T> encontrarMenorNoRB(NoRubroNegro<T> NoRB) {
        while (NoRB.getEsquerda() != null) {
            NoRB = NoRB.getEsquerda();
        }
        return NoRB;
    }

    // Método auxiliar para realizar a checagem de nós duplo-negro
    private void checagemDuploNegro(NoRubroNegro<T> current, boolean isLeft) { // na 1° vez recebo o pai do sucessor
        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;
        // System.out.println(current);

        // CASO 3: No a ser removido é negro e seu sucessor é negro
        // CASO 3.1: se sucessor é filho esquerdo e o filho direito do seu pai é rubro
        if (isLeft == true
                && current.getDireita() != null
                && current.getDireita().getCor() == vermelho) {
            current.getDireita().setCor(preto); // irmão pintado de negro
            current.setCor(vermelho); // pai pintado de rubro
            rotacaoEsquerda(current);
            // continua para o proximo caso
            checagemDuploNegro(current, isLeft);
            // Caso espelhado
        } else if (isLeft == false
                && current.getEsquerda() != null
                && current.getEsquerda().getCor() == vermelho) {
            current.getEsquerda().setCor(preto); // irmão pintado de negro
            current.setCor(vermelho); // pai pintado de rubro
            rotacaoDireita(current);
            checagemDuploNegro(current, isLeft);
        }
        // CASO 3.2A: irmão negro e filhos do irmão negros; e pai negro: pintar irmão de
        // rubroe continuar
        if (isLeft == true
                && current.getDireita() != null
                && current.getDireita().getCor() == preto
                && current.getCor() == preto
                && ((current.getDireita().getDireita() != null && current.getDireita().getDireita().getCor() == preto) || current.getDireita().getDireita() == null)
                && ((current.getDireita().getEsquerda() != null && current.getDireita().getEsquerda().getCor() == preto) || current.getDireita().getEsquerda() == null)
        ) {
            current.getDireita().setCor(vermelho);
            if(current.getPai() != null) {
                checagemDuploNegro(current.getPai(), isLeft); // Aqui vai subindo o duplo negro
            } else{
                return;
            }
            // Caso espelhado
        } else if (isLeft == false
                && current.getEsquerda() != null
                && current.getEsquerda().getCor() == preto
                && current.getCor() == preto
                && ((current.getEsquerda().getDireita() != null && current.getEsquerda().getDireita().getCor() == preto) || current.getEsquerda().getDireita() == null)
                && ((current.getEsquerda().getEsquerda() != null && current.getEsquerda().getEsquerda().getCor() == preto) || current.getEsquerda().getEsquerda() == null)
        ) {
            current.getEsquerda().setCor(vermelho);
            if(current.getPai() != null){
                checagemDuploNegro(current.getPai(), isLeft); // Aqui vai subindo o duplo negro
            } else {
                return;
            }
        }
        // CASO 3.2B: Irmão é negro com filhos negros e pai rubro: pintar irmão de rubro
        // e pai de negro
        if (isLeft == true
                && current.getDireita() != null
                && current.getDireita().getCor() == preto
                && current.getCor() == vermelho
                && ((current.getDireita().getDireita() != null && current.getDireita().getDireita().getCor() == preto) || current.getDireita().getDireita() == null)
                && ((current.getDireita().getEsquerda() != null && current.getDireita().getEsquerda().getCor() == preto) || current.getDireita().getEsquerda() == null)
        ) {
            current.getDireita().setCor(vermelho);
            current.setCor(preto);
            return; // Caso terminal!
            // Caso espelhado
        } else if (isLeft == false
                && current.getEsquerda() != null
                && current.getEsquerda().getCor() == preto
                && current.getCor() == vermelho
                && ((current.getEsquerda().getDireita() != null && current.getEsquerda().getDireita().getCor() == preto) || current.getEsquerda().getDireita() == null)
                && ((current.getEsquerda().getEsquerda() != null && current.getEsquerda().getEsquerda().getCor() == preto) || current.getEsquerda().getEsquerda() == null)
        ) {
            current.getEsquerda().setCor(vermelho);
            current.setCor(preto);
            return; // Caso terminal!
        }
        // CASO 3.3: irmão negro, irmão com filho esquerdo rubro e irmão com filho
        // direito negro
        // Rotação direita simples em irmão
        // irmão fica com cor r e filhho esquerdo com cor n
        if (isLeft == true
                && current.getDireita() != null
                && current.getDireita().getCor() == preto
                && ((current.getDireita().getDireita() != null && current.getDireita().getDireita().getCor() == preto) || current.getDireita().getDireita() == null)
                && current.getDireita().getEsquerda() != null
                && current.getDireita().getEsquerda().getCor() == vermelho) {
            current.getDireita().setCor(vermelho);
            current.getDireita().getEsquerda().setCor(preto);
            rotacaoDireita(current.getDireita());
            checagemDuploNegro(current, isLeft);
            // Caso espelhado
        } else if (isLeft == false
                && current.getEsquerda() != null
                && current.getEsquerda().getCor() == preto
                && current.getEsquerda().getDireita() != null
                && current.getEsquerda().getDireita().getCor() == vermelho
                && ((current.getEsquerda().getEsquerda() != null && current.getEsquerda().getEsquerda().getCor() == preto) || current.getEsquerda().getEsquerda() == null)) {
            current.getEsquerda().setCor(vermelho);
            current.getEsquerda().getDireita().setCor(preto);
            rotacaoEsquerda(current.getEsquerda());
            checagemDuploNegro(current, isLeft);
        }
        // CASO 3.4: Irmão negro e irmão com filho direito rubro
        if (isLeft == true
                && current.getDireita() != null
                && current.getDireita().getCor() == preto
                && current.getDireita().getDireita() != null
                && current.getDireita().getDireita().getCor() == vermelho) {
            Cor corPai = current.getCor();
            current.getDireita().setCor(corPai);
            current.setCor(preto);
            current.getDireita().getDireita().setCor(preto);
            rotacaoEsquerda(current);
            return; // Caso terminal!
            // Caso espelhado
        } else if (isLeft == false
                && current.getEsquerda() != null
                && current.getEsquerda().getCor() == preto
                && current.getEsquerda().getEsquerda() != null
                && current.getEsquerda().getEsquerda().getCor() == vermelho) {
            Cor corPai = current.getCor();
            current.getEsquerda().setCor(corPai);
            current.setCor(preto);
            current.getEsquerda().getEsquerda().setCor(preto);
            rotacaoDireita(current);
            return; // Caso terminal!
        }

        if (current == getRaiz()) {
            return;
        }
    }

    // Métodos para imprimir a árvore em pré-ordem, pós-ordem e na ordem, mostrando a cor de cada nó
    @Override
    public void imprimirPreOrdem() {
        imprimirPreOrdemRecursivo(getRaiz(), 0);
    }

    private void imprimirPreOrdemRecursivo(NoRubroNegro<T> noAtual, int nivel) {
        if (noAtual == null) return;
        // 1. Raiz
        char cor = (noAtual.getCor() == Cor.VERMELHO) ? 'R' : 'B';
        System.out.println(indentacao(nivel) + " " + noAtual.getDado() + " (" + cor + ")"); 
        // 2. Esquerda
        imprimirPreOrdemRecursivo(noAtual.getEsquerda(), nivel + 1); 
        // 3. Direita
        imprimirPreOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
    }

    @Override
    public void imprimirPosOrdem() {
        imprimirPosOrdemRecursivo(getRaiz(), 0);
    }

    private void imprimirPosOrdemRecursivo(NoRubroNegro<T> noAtual, int nivel) {
        if (noAtual == null) return;
        // 1. Esquerda
        imprimirPosOrdemRecursivo(noAtual.getEsquerda(), nivel + 1);
        // 2. Direita 
        imprimirPosOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
        // 3. Raiz
        char cor = (noAtual.getCor() == Cor.VERMELHO) ? 'R' : 'B';
        System.out.println(indentacao(nivel) + " " + noAtual.getDado() + " (" + cor + ")"); 
    }

    @Override
    public void imprimirNaOrdem() {
        imprimirNaOrdemRecursivo(getRaiz(), 0);
    }

    private void imprimirNaOrdemRecursivo(NoRubroNegro<T> noAtual, int nivel) {
        if (noAtual == null) return;
        // 1. Esquerda
        imprimirNaOrdemRecursivo(noAtual.getEsquerda(), nivel + 1); 
        // 2. Raiz
        char cor = (noAtual.getCor() == Cor.VERMELHO) ? 'R' : 'B';
        System.out.println(indentacao(nivel) + " " + noAtual.getDado() + " (" + cor + ")"); 
        // 3. Direita
        imprimirNaOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
    }

    private String indentacao(int nivel) {
        if (nivel == 0) return "";
        return " ".repeat(nivel * 3) + "|- ";
    }

    // Método para obter a raiz da árvore
    @Override
    public NoRubroNegro<T> getRaiz() { return this.raiz; }

    // Método para verificar se a árvore está vazia
    @Override
    public boolean isVazia() { return this.raiz == null; }

    // Método para definir a raiz da árvore
    private void setRaiz(NoRubroNegro<T> raiz) { this.raiz = raiz; }

}