package grupo04;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Aplicacao {
    private static Object[][] produtos = new Object[2][9];
    private static int quantidadeLinhas = 0;

    public static void main(String[] args) {

        Scanner ler = new Scanner(System.in);
        String option;
        System.out.println("###### SUPERMERCADO ######");
        System.out.println("Bem-vindo!");


        do {
            System.out.println("O que deseja fazer?");
            System.out.println("1 - Cadastrar/Comprar produtos");
            System.out.println("2 - Imprimir estoque");
            System.out.println("3 - Listar os produto pelo Tipo");
//                System.out.println("4 - Pesquisar um produto pelo codigo");
//                System.out.println("5 - Pesquisar um produto pelo nome");
//                System.out.println("6 - Vendas");
//                System.out.println("7 - Relatório de vendas analítico (todas as vendas)");
//                System.out.println("8 - Relatório de vendas sintético (consolidado por CPF");
            System.out.println("0 - Sair");
            option = ler.nextLine();

            switch (option) {
                case "0":
                    System.out.println("Saindo...");
                    System.exit(1);
                case "1":
                    if (quantidadeLinhas == produtos.length) {
                        redimensionar();
                    }
                    programa(produtos, ler);
                    break;
                case "2":
                    imprimirEstoque(produtos);
                    break;
                case "3":
                    listarProdutosTipo(produtos, ler);
                    break;
//                    case "4":
//
//                        break;
//                    case "5":
//
//                        break;
//                    case "6":
//
//                        break;
//                    case "7":
//
//                        break;
//                    case "8":
//
//                        break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (!(option.equals("0")));
    }

    public static void programa(Object[][] produtos, Scanner ler) {
        //imprimirCadastro(produtos);
        System.out.print("Insira a marca do produto: ");
        String marca = ler.nextLine();
        System.out.print("Insira o identificador do produto: ");
        String identificador = ler.nextLine();

        int linha = estaCadastrado(produtos, marca, identificador);

        if (linha < 0) {
            int linhaLivre = encontrarPosicaoLivre(produtos);

            produtos[linhaLivre][1] = marca;
            produtos[linhaLivre][2] = identificador;

            cadastrarTipoProduto(produtos, ler, linhaLivre);
            cadastrarNomeProduto(produtos, ler, linhaLivre);
            cadastrarPrecoCusto(produtos, ler, linhaLivre);
            cadastrarQuantidadeProduto(produtos, ler, linhaLivre);
            cadastrarDataCompra(produtos, ler, linhaLivre);
            cadastrarPrecoVenda(produtos, linhaLivre);

            calcularEstoque(produtos, linhaLivre);
            quantidadeLinhas++;
        } else {
            System.out.println("ATUALIZANDO ESTOQUE DO PRODUTO");
            cadastrarPrecoCusto(produtos, ler, linha);
            cadastrarQuantidadeProduto(produtos, ler, linha);
            cadastrarDataCompra(produtos, ler, linha);
            cadastrarPrecoVenda(produtos, linha);
            calcularEstoque(produtos, linha);
        }

        System.out.println();
    }

    public static int estaCadastrado(Object[][] produtos, String marca, String identificador) {
        for (int i = 0; i < produtos.length; i++) {
            Object object = produtos[i][1];
            Object object2 = produtos[i][2];
            if (object instanceof String && object2 instanceof String) {
                String marcaRegistrada = (String) object;
                String identificadorRegistrado = (String) object2;
                if (marcaRegistrada != null && identificadorRegistrado != null) {
                    if (marcaRegistrada.equals(marca) && identificadorRegistrado.equals(identificador)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static int encontrarPosicaoLivre(Object[][] produtos) {
        for (int i = 0; i < produtos.length; i++) {
            String marca = (String) produtos[i][1];
            String identificador = (String) produtos[i][2];
            if (marca == null && identificador == null) {
                return i;
            }
        }
        return -1;
    }

    public static void cadastrarTipoProduto(Object[][] produtos, Scanner ler, int linha) {
        System.out.println("Insira o tipo do produto:");
        System.out.println("ALIMENTOS - BEBIDA - HIGIENE");
        String tipoProduto = ler.nextLine().toUpperCase();
        try {
            TipoProduto tipo = TipoProduto.valueOf(tipoProduto);
            produtos[linha][0] = tipo;
        } catch (Exception exception) {
            if (exception instanceof IllegalArgumentException) {
                System.out.println("Opção inválida");
                ler.nextLine();
                cadastrarTipoProduto(produtos, ler, linha);
            }
            if (exception instanceof NullPointerException) {
                System.out.println("Algo deu errado...");
                System.out.println("Insira o tipo do produto novamente:");
                ler.nextLine();
                cadastrarTipoProduto(produtos, ler, linha);
            }
        }
    }

    public static void cadastrarNomeProduto(Object[][] produtos, Scanner ler, int linha) {
        System.out.print("Insira o nome: ");
        String nome = ler.nextLine().toUpperCase();
        produtos[linha][3] = nome;
    }

    public static void cadastrarPrecoCusto(Object[][] produtos, Scanner ler, int linha) {
        System.out.print("Insira o preço de custo: ");
        try {
            String custo = ler.nextLine();
            double precoCusto = Double.parseDouble(custo.replace(',', '.'));
            produtos[linha][4] = precoCusto;
        } catch (Exception exception) {
            if (exception instanceof InputMismatchException) {
                System.out.println("ERRO DE PREÇO");
                ler.nextLine();
                cadastrarPrecoCusto(produtos, ler, linha);
            }
            if (exception instanceof NullPointerException) {
                System.out.println("Insira o preço novamente");
                ler.nextLine();
                cadastrarPrecoCusto(produtos, ler, linha);
            }
        }
    }

    public static void cadastrarQuantidadeProduto(Object[][] produtos, Scanner ler, int linha) {
        System.out.print("Insira a quantidade: ");
        try {
            int quantidade = ler.nextInt();
            produtos[linha][5] = quantidade;
            ler.nextLine();
        } catch (Exception exception) {
            if (exception instanceof InputMismatchException) {
                System.out.println("Erro de inserção! Tente novamente, utilize somente números");
                ler.nextLine();
                cadastrarQuantidadeProduto(produtos, ler, linha);
            }
            if (exception instanceof NullPointerException) {
                System.out.println("Algo deu errado!");
                System.out.println("Insira a quantidade novamente:");
                ler.nextLine();
                cadastrarQuantidadeProduto(produtos, ler, linha);
            }
        }
    }

    public static void cadastrarDataCompra(Object[][] produtos, Scanner ler, int linha) {

        //ADICIONAR VERIFICAÇÕES DE ERRO

        /*ler.nextLine();
        System.out.print("Insira a data da compra dd/MM/yyyy: ");
        String stringDataCompra = ler.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataCompra = LocalDate.parse(stringDataCompra, formatter);*/
        LocalDateTime dataCompra = LocalDateTime.now();
        produtos[linha][6] = dataCompra;
    }

    public static void cadastrarPrecoVenda(Object[][] produtos, int linha) {

        //ADICIONAR VERIFICAÇÕES DE ERRO

        TipoProduto tipo = (TipoProduto) produtos[linha][0];
        Double precoVenda = tipo.calcularPrecoVenda(tipo, (Double) produtos[linha][4]);
        produtos[linha][7] = precoVenda;
    }

    public static void calcularEstoque(Object[][] produtos, int linha) {

        //ADICIONAR VERIFICAÇÕES DE ERRO

        int quantidade = (Integer) produtos[linha][5];
        if (produtos[linha][8] == null) {
            produtos[linha][8] = quantidade;
        } else {
            int estoque = (Integer) produtos[linha][8];
            estoque += quantidade;
            produtos[linha][8] = estoque;
        }
    }

    /*public static void imprimirCadastro(Object[][]produtos){
        System.out.println();
        System.out.println("#################################################");
        System.out.println("------------ PRODUTOS CADASTRADOS ------------");
        System.out.println("#################################################");
        System.out.println();
        System.out.println("Tipo - Marca - Identificador - Nome - Preço de Custo" +
                " - Quantidade - Data Compra - Preço de Venda - Estoque");
        for (Object[]array : produtos) {
            for(Object elemento : array) {
                System.out.print(elemento + " - ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------");
    }*/

    public static void imprimirEstoque(Object[][] produtos) {
        System.out.println("---------------------------------------");
        System.out.println("               ESTOQUE                 ");
        System.out.println("---------------------------------------");
        System.out.println("Marca - Identificador - Nome - Estoque");
        for (int i = 0; i < produtos.length; i++) {
            String marca = (String) produtos[i][1];
            if (marca == null) {
                break;
            }
            String identificador = (String) produtos[i][2];
            String nome = (String) produtos[i][3];
            int estoque = (Integer) produtos[i][8];
            System.out.println(marca + " - " + identificador + " - " + nome + " - " + estoque);
        }
        System.out.println("---------------------------------------");
    }

    public static void listarProdutosTipo(Object[][] produtos, Scanner ler) {
        System.out.println("Insira a opção do tipo a ser listado:");
        for (TipoProduto value : TipoProduto.values()) {
            System.out.println(value.ordinal() + "-" + value.getTipo());
        }
        int option = ler.nextInt();
        ler.nextLine();
        TipoProduto tipoProduto;
        switch (option) {
            case 0:
                tipoProduto = TipoProduto.ALIMENTOS;
                imprimirProdutosTipo(produtos, tipoProduto);
                break;
            case 1:
                tipoProduto = TipoProduto.BEBIDA;
                imprimirProdutosTipo(produtos, tipoProduto);
                break;
            case 2:
                tipoProduto = TipoProduto.HIGIENE;
                imprimirProdutosTipo(produtos, tipoProduto);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    public static void imprimirProdutosTipo(Object[][] produtos, TipoProduto tipoProduto) {
        System.out.println("Tipo - Marca - Identificador - Nome - Preço de Custo" +
                " - Quantidade - Data Compra - Preço de Venda - Estoque");

        int contador = 0;

        for (int i = 0; i < produtos.length; i++) {
            TipoProduto tipoCadastrado = (TipoProduto) produtos[i][0];
            if (tipoCadastrado == tipoProduto) {
                String marca = (String) produtos[i][1];
                String identificador = (String) produtos[i][2];
                String nome = (String) produtos[i][3];
                double precoCusto = (Double) produtos[i][4];
                int quantidade = (Integer) produtos[i][5];
                LocalDateTime dataCompra = (LocalDateTime) produtos[i][6];
                double precoVenda = (Double) produtos[i][7];
                int estoque = (Integer) produtos[i][8];
                System.out.printf("%s - %s - %s - %s - %.2f - %d - %s - %.2f - %d %n", tipoCadastrado.getTipo(), marca,
                        identificador, nome, precoCusto, quantidade,
                        dataCompra.format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm")), precoVenda, estoque);
            } else {
                contador++;
            }
        }
        if (contador == 0) {
            System.out.println("Não há produtos cadastrados para esse tipo");
        }
    }

    public static void redimensionar() {
        Object[][] novaTabela = new Object[produtos.length * 2][9];
        for (int i = 0; i < produtos.length; i++) {
            for (int j = 0; j < produtos[i].length; j++) {
                novaTabela[i][j] = produtos[i][j];
            }
        }
        produtos = novaTabela;
    }
}


