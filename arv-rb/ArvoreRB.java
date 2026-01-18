public class ArvoreRB {
    private NoRubroNegro<T> raiz;

    public ArvoreRB() {
        this.raiz = null;
    }

    public void inserirRB(T dado) {
        NoRB novo = new NoRB(dado);

        if (this.getRaiz() == null) {
            Cor novaCor = Cor.PRETO;
            novo.setCor(novaCor);
            this.setRaiz(novo);
        } else {
            Cor novaCor = Cor.VERMELHO;
            novo = incluir(dado);
            novo.setCor(novaCor);
            verificaRegras(novo);
        }
    }

    public void verificaRegras(NoRubroNegro<T> no) {
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

    private void recoloracao(NoRubroNegro<T> no, NoRubroNegro<T> pai, NoRubroNegro<T> tio, NoRubroNegro<T> avo) {
        // Aqui o pai e o tio do no serão pintados de negros e o avo de rubro
        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;
        pai.setCor(preto);
        tio.setCor(preto);

        if (avo != raiz) {
            avo.setCor(vermelho);
        }

        verificaRegras(avo);
    }

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

    private void rotacaoDireitaDupla(NoRubroNegro<T> no) {
        rotacaoEsquerda(no.getEsquerda());
        rotacaoDireita(no);
    }

    private void rotacaoEsquerdaDupla(NoRubroNegro<T> no) {
        rotacaoDireita(no.getDireita());
        rotacaoEsquerda(no);
    }

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

    @Override
    public void excluirNo(T dado) {
        NoRubroNegro<T> n = buscarNo(dado);
        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;
        int quantFilhos = quantidadeFilhos(n);

        if (quantFilhos == 0) {
            if (n == raiz) {
                setRaiz(null);
                remover(dado);
            }
            if (n.getCor() == vermelho) { // se é rubro, só remove
                remover(dado);
            } else if (n.getCor() == preto) {
                remover(dado);
                boolean isLeft = n == n.getPai().getEsquerda();
                checagemDuploNegro(n.getPai(), isLeft); // vai direto para a situação 3 (casos recursivos), is left aqui mostrará se n é filho esquerdo
            }

        } else if (quantFilhos == 1) {
            if (n == raiz) { // n só tem um filho e é a raiz. Neste caso ele só pode ter filho rubro pelas
                // regras, e este será pintado de preto
                setRaiz(n.getEsquerda() != null ? n.getEsquerda() : n.getDireita()); // se n é a raiz, seu filho
                // será a raiz
                if (n.getDireita() != null && n.getDireita().getCor() == vermelho) {
                    n.getDireita().setCor(preto);
                }
                remover(dado);
            }
            if (n.getCor() == vermelho) { // se tem 1 filho e é rubro o filho é negro pelas regras só substitui
                remover(dado);
            } else if (n.getCor() == preto) { // n é negro e seu filho pode ser rubro ou negro
                // Achar posição do seu filho
                NoRubroNegro<T> filho;
                if(n.getEsquerda() != null){
                    filho = n.getEsquerda();
                } else {
                    filho = n.getDireita();
                }

                if(filho.getCor() == vermelho){ // Prévia da situação 2, pinta filho de negro e acabou
                    filho.setCor(preto);
                    remover(n.getDado());
                }else{ // Se filho tbm é negro, segue direto para situação 3
                    remover(dado);
                    boolean isLeft = filho == n.getEsquerda();
                    checagemDuploNegro(n, isLeft); // is left aqui mostrará se seu unico filho é esquerdo
                }
            }

        } else { // casos que tem 2 filhos seguem para as 4 possíveis situações
            removerComRegras(n);
        }
    }

    @Override
    public NoRubroNegro<T> buscarNo(T dado) {
        return buscarNoRecursivo(this.raiz, dado);
    }

    private NoBinario<T> buscarNoRecursivo(NoBinario<T> noAtual, T dado) {
      // Caso 1: Nó não encontrado (chegou ao fim de um galho)
      if (noAtual == null) {
         return null;
      }
      // Caso 2: Encontrado!
      if (noAtual.getDado().equals(dado)) {
         return noAtual;
      }
        
      // Caso 3: (Passo Recursivo) Procura na sub-árvore esquerda
      NoBinario<T> noEncontrado = buscarNoRecursivo(noAtual.getEsquerda(), dado);
        
      // Se não achou na esquerda, procura na direita
      if (noEncontrado == null) {
         noEncontrado = buscarNoRecursivo(noAtual.getDireita(), dado);
      }

      return noEncontrado;
   }
    
    private void removerComRegras(NoRubroNegro<T> n) {
        NoRubroNegro<T> sucessor = encontrarMenorNoRB(n.getDireita());
        NoRubroNegro<T> paiSucessor = sucessor.getPai();
        Cor vermelho = Cor.VERMELHO;
        Cor preto = Cor.PRETO;

        boolean isLeft = false;
        // Verifico se é filho esquerdo
        if (sucessor == sucessor.getPai().getEsquerda()) {
            isLeft = true;
        }

        // SITUAÇÃO 1: NO RUBRO E SUCESSOR RUBRO
        if (n.getCor() == vermelho && sucessor.getCor() == vermelho && sucessor != null) {
            remover(n.getKey());
            // SITUAÇÃO 2: NO NEGRO E SUCESSOR RUBRO
        } else if (n.getCor() == preto && sucessor.getCor() == vermelho && sucessor != null) {
            sucessor.setCor(preto);
            remover(n.getDado());
            // SITUAÇÃO 3: NO NEGRO E SUCESSOR NEGRO
        } else if (n.getCor() == preto && sucessor.getCor() == preto && sucessor != null) {
            remover(n.getDado());
            checagemDuploNegro(paiSucessor, isLeft);

            // SITUAÇÃO 4: NO RUBRO E SUCESSOR NEGRO
        } else if (n.getCor() == vermelho && sucessor.getCor() == preto && sucessor != null) {
            sucessor.setCor(vermelho);
            remover(n.getDado());
            checagemDuploNegro(paiSucessor, isLeft);

        }
    }

    // Método que remove fisicamente o nó com dado igual a `dado`.
    // Se o nó tiver dois filhos, substitui o dado pelo sucessor (menor da subárvore direita)
    // e remove o sucessor (que terá no máximo um filho).
    private void remover(T dado) {
        NoRubroNegro<T> no = buscarNo(dado);
        if (no == null) return;

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
                this.raiz = filho;
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
                this.raiz = null;
            } else {
                if (no == no.getPai().getEsquerda()) {
                    no.getPai().setEsquerda(null);
                } else {
                    no.getPai().setDireita(null);
                }
            }
        }
    }

    private NoRubroNegro<T> encontrarMenorNoRB(NoRubroNegro<T> NoRB) {
        while (NoRB.getEsquerda() != null) {
            NoRB = NoRB.getEsquerda();
        }
        return NoRB;
    }

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
            char corPai = current.getCor();
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
            char corPai = current.getCor();
            current.getEsquerda().setCor(corPai);
            current.setCor(preto);
            current.getEsquerda().getEsquerda().setCor(preto);
            rotacaoDireita(current);
            return; // Caso terminal!
        }

        if (current == raiz) {
            return;
        }
    }

    @Override
    public void imprimirPreOrdem() {
        imprimirPreOrdemRecursivo(this.raiz, 0);
    }

    private void imprimirPreOrdemRecursivo(NoBinario<T> noAtual, int nivel) {
        if (noAtual == null) return;
        // 1. Raiz
        System.out.println(indentacao(nivel) + " " + noAtual.getDado()); 
        // 2. Esquerda
        imprimirPreOrdemRecursivo(noAtual.getEsquerda(), nivel + 1); 
        // 3. Direita
        imprimirPreOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
    }

    @Override
    public void imprimirPosOrdem() {
        imprimirPosOrdemRecursivo(this.raiz, 0);
    }

    private void imprimirPosOrdemRecursivo(NoBinario<T> noAtual, int nivel) {
        if (noAtual == null) return;
        // 1. Esquerda
        imprimirPosOrdemRecursivo(noAtual.getEsquerda(), nivel + 1);
        // 2. Direita 
        imprimirPosOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
        // 3. Raiz
        System.out.println(indentacao(nivel) + " " + noAtual.getDado()); 
    }

    @Override
    public void imprimirNaOrdem() {
        imprimirNaOrdemRecursivo(this.raiz, 0);
    }

    private void imprimirNaOrdemRecursivo(NoBinario<T> noAtual, int nivel) {
        if (noAtual == null) return;
        // 1. Esquerda
        imprimirNaOrdemRecursivo(noAtual.getEsquerda(), nivel + 1); 
        // 2. Raiz
        System.out.println(indentacao(nivel) + " " + noAtual.getDado()); 
        // 3. Direita
        imprimirNaOrdemRecursivo(noAtual.getDireita(), nivel + 1); 
    }

    private String indentacao(int nivel) {
        if (nivel == 0) return "";
        return " ".repeat(nivel * 3) + "|- ";
    }

    @Override
    public NoRubroNegro<T> getRaiz() { return this.raiz; }

    @Override
    public boolean isVazia() { return this.raiz == null; }
}