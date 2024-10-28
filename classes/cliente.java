package classes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class cliente {
    private int id;
    private String nome;
    private String pw;
    private int NumConta;
    private double saldo;
    private int cni;
    private int[] dataNascimento;
    private String historico = null;
    private String type = "cliente";

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    
    public cliente(int id, String nome, String pw, int NumConta, double saldo, int cni, int[] dataNascimento, int s) throws IOException
    {
        this.id = id;
        this.nome = nome;
        this.pw = pw;
        this.NumConta = NumConta;
        this.saldo = saldo;
        this.cni = cni;
        this.dataNascimento = dataNascimento;

        if (s == 1) handlers.log(id, nome, this.type, "logou no sistema");
        
    }

    public boolean saveData() throws IOException{
        try {
 
            if (handlers.saveData(""+ this.id, to_String())) return true;
            
        } catch (Exception e) {
            handlers.log(0,"system", "root", "problema em saveData 'cliente', mais detalhes: " + e);
            System.out.println("problema em saveData, mais detalhes: " + e);
        }
        return false;
    }

    private String to_String()
    {
        String dataNascimento = ""+ this.dataNascimento[0] + "/" + this.dataNascimento[1] + "/" + this.dataNascimento[2]; 
        String out = "" + this.id + "," + this.nome + "," + this.pw + "," + this.type + "," + dataNascimento + "," + this.NumConta + "," + this.saldo + "," + this.cni + "," + this.historico;
        return out;
    }

    //calcular saldo
    public String[] calcSaldo() throws IOException
    {
        String[] out = {"status", "Falhou em Calcular o Saldo", "saldo", "-1"};

        try {
            double saldo = blockchain.calcularSaldo(""+this.id, this.type);
            out[1] = "Saldo Calculado com Sucesso";
            out[3] = "" + saldo;

        } catch (Exception e) {
            handlers.log(this.id, this.nome, this.type, "Falhou em Calcular Saldo, mais detalhes:" + e);
        }
        
        return out;
    }

    //mudar senha
    public boolean mudarSenha(String velhaSenha, String NovaSenha) throws IOException, NoSuchAlgorithmException
    {
        if (this.pw.equals(velhaSenha)) {
            this.pw = NovaSenha;
            return saveData();
        }

        return false;
    }

    //mudar Nome
    public boolean mudarNome(String nome) throws IOException{
        this.nome = nome;
        return saveData();
    }

    //consultar historico

    //transferencia
    public String[] transferencia(String valor, String receptorID) throws NumberFormatException, NoSuchAlgorithmException, IOException
    {
        String[] out = blockchain.transferencia(this.type, ""+ this.id, Double.parseDouble(valor), Integer.parseInt(receptorID));
        return out;
    }

    //fazer emprestimo

}
