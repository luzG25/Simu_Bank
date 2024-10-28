package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class root extends funcionario{

    public root(int id, String nome, String pw, int IDconta, int s) throws IOException {
        super(id, nome, pw, IDconta, 0);
        setType("root");
        if (s == 1) handlers.log(id, nome, getType(), "logou no sistema");
    }

    //remover cliente ou funcionario
    public boolean remover(int id) throws IOException{
        if (id == 0) return false;

        //abrir dados
        BufferedReader file = new BufferedReader(new FileReader(handlers.pathData));
        String dados = "";
        String[] rem = {"", ""};
        int c = 0;
        boolean check = false;

        while (true)
        {
            String linha = file.readLine();
            if (linha == null) break;
            if (linha.split(",")[0].equals(""+id)) {check = true; rem = linha.split(",");}
            else {
                if (c > 0) dados += "\n";
                c++;
                dados += linha;
            }
            
        }
        file.close();

            
        if (check)
        {
            String ocorrencia = "removeu usuario " + rem[1] + ",id:" + rem[0];
            handlers.log(getId(), getNome(), getType(), ocorrencia);
        }

        //gravar dados
        BufferedWriter file0 = new BufferedWriter(new FileWriter(handlers.pathData));
        file0.write(dados);
        file0.close();
        
        return check;
    }

    //criar funcionario
    public String[] criarFuncionario(String nome, int idConta)throws IOException{
        //gerar senha aleatorio:
        Random gerador = new Random();
        int aux = gerador.nextInt(10);
        String pw = "" + (gerador.nextInt(1000) + 1000 * aux);
        int id = handlers.varsHandler(0);
        funcionario func = new funcionario(id, nome, pw, idConta, 0);
        boolean check = func.saveData();

        String ocorrencia = "Criou usuario " + func.getNome() + " com id: " + func.getId();
        handlers.log(getId(), getNome(), "root", ocorrencia);
        
        if (check) {
            String[] out = {""+id, nome, pw, ""+idConta};
            return out;
        }
        else
        {
            String[] out = {"-1"};
            return out;
        }  

        
    }


    //ver logs

    //aprovar emprestimos

    //calcular saldo do banco

}
