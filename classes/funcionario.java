package classes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class funcionario {
    private int id;
    private String nome;
    private String pw;
    private int IDconta;
    private String type = "funcionario";

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public funcionario(int id, String nome, String pw, int IDconta, int s) throws IOException{
        this.id = id;
        this.nome = nome;
        this.pw = pw;
        this.IDconta = IDconta;

        if (s == 1) handlers.log(id, nome, this.type, "logou no sistema");
    }

    public String[] criarCliente(String nome, int[] dataNascimento, int CNI) throws IOException, NoSuchAlgorithmException {
        int id = handlers.varsHandler(0);
        int NumConta = handlers.varsHandler(1);

        //gerar senha aleatorio:
        Random gerador = new Random();
        int aux = gerador.nextInt(10);
        String pw = "" + (gerador.nextInt(1000) + 1000 * aux);

        cliente cl = new cliente(id, nome, hashing.hash256(pw), NumConta, 0, CNI, dataNascimento, 0); 
        boolean check = cl.saveData();

        String ocorrencia = "Criou usuario " + cl.getNome() + " com id: " + cl.getId();
        handlers.log(getId(), getNome(), this.type, ocorrencia);

        if (check)
        {
            String[] out = {""+id, nome, pw, ""+NumConta,""+dataNascimento[0]+"/"+dataNascimento[1]+"/"+dataNascimento[2], ""+CNI};
            String[] aux0 = blockchain.transferencia(this.type, ""+this.id, 1000, id);

            if (aux0[0].equals("transacaoErro")) out[0] = "-1";
            return out;
        }else {
            String[] out = {"-1"};
            return out;
        }
    }

    //fazer deposito
    public  String[] deposito(double valor, int receptorID) throws NumberFormatException, NoSuchAlgorithmException, IOException {
        String out[] = blockchain.transferencia(this.type, ""+this.id, valor, receptorID);
        return out;
    }

    //mudar senha
    public boolean mudarSenha(String velhaSenha, String NovaSenha) throws IOException
    {
        if (this.pw.equals(velhaSenha)) {
            this.pw = NovaSenha;
            return saveData();
        }

        return false;
    }


    private String to_String()
    {
        String out = "" + this.id + "," + this.nome + "," + this.pw + "," + getType() + "," + this.IDconta;
        return out;
    }

    //guardar dados
    public boolean saveData() throws IOException{
        try {
 
            if (handlers.saveData(""+ this.id, to_String())) return true;
            
        } catch (Exception e) {
            handlers.log(0,"system", "root", "problema em saveData 'funcionario', mais detalhes: " + e);
            System.out.println("problema em saveData, mais detalhes: " + e);
        }
        return false;
    }

}
