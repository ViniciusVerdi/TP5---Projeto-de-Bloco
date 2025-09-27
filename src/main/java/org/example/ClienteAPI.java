package org.example;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class ClienteAPI {
    private static final String BASE_URL = "http://localhost:7000/vendedores";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("---------Menu---------");
            System.out.println("1 - Listar vendedores");
            System.out.println("2 - Adicionar vendedor");
            System.out.println("3 - Atualizar vendedor");
            System.out.println("4 - Remover vendedor");
            System.out.println("5 - Buscar vendedor");
            System.out.println("0 - Sair");
            System.out.print("Digite a opção desejada: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 0:
                    return;
                case 1:
                    listar();
                    break;
                case 2:
                    adicionar();
                    break;
                case 3:
                    atualizar();
                    break;
                case 4:
                    remover();
                    break;
                case 5:
                    buscarVendedor();
                    break;
                default:
                    System.out.println("A opção " + opcao + " não é válida! Tente novamente.");
            }

        }
    }
    private static void listar() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String linha;
            while ((linha = br.readLine()) != null) System.out.println(linha);
        }
    }
    private static void buscarVendedor() throws Exception {
        StringBuilder resposta = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do vendedor: ");
        String id = scanner.nextLine();
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL+"/"+id).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                resposta.append(linha);
            }
            System.out.println(resposta);
        }
        catch (Exception e) {
            System.out.println("O vendedor não existe!");
        }
    }

    public static String entrarDadosVendedor(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF/CNPJ: ");
        String cpf = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Telefone: ");
        String tel = scanner.nextLine();
        String vendedorJSON = String.format("{\"nome\":\"%s\",\"cpf_cnpj\":\"%s\",\"email\":\"%s\",\"telefone\":\"%s\"}",
                 nome, cpf, email, tel);
        return vendedorJSON;
    }

    private static void adicionar() throws Exception {
        String vendedorJSON = entrarDadosVendedor();
        sendRequest("POST", BASE_URL, vendedorJSON);
    }

    private static void atualizar() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ID do vendedor a ser atualizado: ");
        int id = Integer.parseInt(scanner.nextLine());
        String vendedorJSON = entrarDadosVendedor();
        sendRequest("PUT", BASE_URL + "/" + id, vendedorJSON);
    }

    private static void remover() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("ID do vendedor a remover: ");
        int id = Integer.parseInt(scanner.nextLine());
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        conn.setRequestMethod("DELETE");
        int status = conn.getResponseCode();
        if (status == HttpURLConnection.HTTP_NO_CONTENT) {
            System.out.println("Vendedor removido com sucesso!");
        }
        else {
            System.out.println("Erro: Vendedor não encontrado!");
        }
    }

    private static void sendRequest(String method, String urlStr, String json) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        }
    }
}