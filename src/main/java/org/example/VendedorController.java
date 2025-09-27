package org.example;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VendedorController {
    private static final String ARQUIVO = "C:/arquivos/vendedores.csv";

    public static void config(Javalin app) {
        app.get("/vendedores", VendedorController::listarVendedores);
        app.get("/vendedores/{id}", VendedorController::buscarVendedor);
        app.post("/vendedores", VendedorController::adicionarVendedor);
        app.put("/vendedores/{id}", VendedorController::atualizarVendedor);
        app.delete("/vendedores/{id}", VendedorController::removerVendedor);
    }

    public static void listarVendedores(Context ctx) {
        List<Vendedor> vendedores = carregarTodos();
        ctx.json(vendedores);
    }
    public static void buscarVendedor(Context contexto) {
        int id = Integer.parseInt(contexto.pathParam("id"));
        Vendedor v = carregarTodos().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if (v != null) {
            contexto.json(v);
        }
        else contexto.status(404);
    }

    public static void adicionarVendedor(Context contexto) {
        List<Vendedor> vendedores = carregarTodos();
        int id = idAtual(vendedores);
        Vendedor novo = contexto.bodyAsClass(Vendedor.class);
        novo.setId(id);
        vendedores.add(novo);
        salvarTodos(vendedores);
        contexto.status(201).json(novo);
    }

    public static void atualizarVendedor(Context contexto) {
        int id = Integer.parseInt(contexto.pathParam("id"));
        Vendedor atualizado = contexto.bodyAsClass(Vendedor.class);
        atualizado.setId(id);
        List<Vendedor> vendedores = carregarTodos();
        boolean encontrado = false;
        for (int i = 0; i < vendedores.size(); i++) {
            if (vendedores.get(i).getId() == id) {
                vendedores.set(i, atualizado);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            salvarTodos(vendedores);
            contexto.status(200).json(atualizado);
        } else{
            contexto.status(404);
        }
    }

    public static void removerVendedor(Context contexto) {
        int id = Integer.parseInt(contexto.pathParam("id"));
        List<Vendedor> vendedores = carregarTodos();
        boolean removed = vendedores.removeIf(vendedor -> vendedor.getId() == id);
        if (removed) {
            salvarTodos(vendedores);
            contexto.status(204);
        } else contexto.status(404);
    }

    private static List<Vendedor> carregarTodos() {
        List<Vendedor> vendedores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length < 5) continue;
                int id = Integer.parseInt(partes[0]);
                vendedores.add(new Vendedor(id, partes[1], partes[2], partes[3], partes[4]));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo");
        }
        return vendedores;
    }

    private static void salvarTodos(List<Vendedor> vendedores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            bw.write("id,nome,cpf_cnpj,email,telefone");
            bw.newLine();
            for (Vendedor vendedor : vendedores) {
                bw.write(vendedor.dadosParaArquivo());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar arquivo");
        }
    }

    public static int idAtual(List<Vendedor> vendedores){
        return vendedores.size() + 1;
    }
}

