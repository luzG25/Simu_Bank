package classes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class handlers {
    private static String pathLog = "DATA/log.dat";
    private static String pathVars = "DATA/vars.dat";
    public static String pathData = "DATA/mainData.dat";
    private static String pathTokens = "DATA/tokens.dat";
    private static String pathTemp = "DATA/temp.dat";

    public static String getData() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    public static int varsHandler(int pos) throws IOException {
        try {
            BufferedReader buffRead = new BufferedReader(new FileReader(pathVars));
            int num;
            
            String linha = buffRead.readLine();
            String[] data = linha.split(",");
            num = Integer.parseInt(data[pos]);
            buffRead.close();

            BufferedWriter buffWrite = new BufferedWriter(new FileWriter(pathVars));
            data[pos] = "" + (num + 1);
            linha = "" + data[0] + "," + data[1] + "," + data[2];
            buffWrite.append(linha + '\n');
            buffWrite.close();

            return num;
        
            
        } catch (Exception e) {
            System.out.println("ERRO em getID()... Mais detalhes: " + e);
            return -1;
        }

    }

    public static boolean saveData(String id, String dataIN) throws IOException{
        BufferedReader file = new BufferedReader(new FileReader(pathData));
        String data = "";
        boolean on = true;
        int c = 0;

        while (true) 
        {
            String linha = file.readLine();
            if (linha == null) break;
            if (c > 0) data += "\n";
            c++;

            if (id != null) {
                String[] dat = linha.split(",");
                if (dat[0].equals(id)) {
                    linha = dataIN;
                    on = false;
                }
            }
            data += linha;
        }

        if (on) data += ("\n" + dataIN);

        file.close();

        BufferedWriter file0 = new BufferedWriter(new FileWriter(pathData));
        file0.write(data);
        file0.close();

        return true;

    }

    //log
    public static void log(int id, String username, String type, String ocorrencia) throws IOException
    {
        BufferedReader file = new BufferedReader(new FileReader(pathLog));
        String dados = "";
        String linha;
        
        while (true)
        {
            linha = file.readLine();
            if (linha == null) break;
            dados += linha + "\n";
        }
        file.close();

        linha = "" + varsHandler(2) + "," + id + "," + username + "," + type + "," + getData() + "," + ocorrencia;
        dados += linha;

        BufferedWriter file0 = new BufferedWriter(new FileWriter(pathLog));
        file0.write(dados);
        file0.close();
    }

    //token
    public static boolean confirmarToken(String id, String token) throws IOException
    {
        BufferedReader file = new BufferedReader(new FileReader(pathTokens));
        boolean check = false;

        while (true)
        {
            String linha = file.readLine();
            if (linha == null) break;
            String[] data = linha.split(",");
            if (data[0].equals(id) && data[1].equals(token)) {check = true; break;}

        }
        file.close();
        return check;
    }

    public static boolean adicionarToken(String id, String token) throws IOException
    {
        BufferedReader file = new BufferedReader(new FileReader(pathTokens));
        String data = readData(id, file,0);
        file.close();

        data += "\n" + id + "," + token;

        BufferedWriter file0 = new BufferedWriter(new FileWriter(pathTokens));
        file0.write(data);
        file0.close();

        return true;
    }

    public static void inicializarToken() throws IOException
    {
        BufferedWriter file = new BufferedWriter(new FileWriter(pathTokens));
        file.write("");
        file.close();
    }

    //recarregar informação de mainData
    public static String[] loadInfo(String id) throws IOException
    {
        BufferedReader file = new BufferedReader(new FileReader(pathData));
        String linha;

        while (true)
        {
            linha = file.readLine();
            if (linha == null) break;
            if (linha.split(",")[0].equals(id)) {file.close(); return linha.split(",");}
        }

        file.close();
        String[] obj = {"-1"};
        return obj;
    }


    //temp
    public static void inicializarTemp() throws IOException{
        BufferedWriter file = new BufferedWriter(new FileWriter(pathTemp));
        file.write("");
        file.close();
    }

    public static void adicionarTemp(String id) throws IOException
    {
        String[] dados = loadInfo(id);
        String dat = "";
        for (int i=0; i < dados.length; i++)
        {
            if (i > 0) dat += ",";
            dat += dados[i];
        }

        BufferedReader file = new BufferedReader(new FileReader(pathTemp));
        String data = readData(id, file,0);
        file.close();

        data += "\n" + dat;

        BufferedWriter file0 = new BufferedWriter(new FileWriter(pathTemp));
        file0.write(data);
        file0.close();
    }

    public static String[] loadInfoTemp(String id) throws IOException
    {
        BufferedReader file = new BufferedReader(new FileReader(pathData));
        String linha;

        while (true)
        {
            linha = file.readLine();
            if (linha == null) break;
            if (linha.split(",")[0].equals(id)) {file.close(); return linha.split(",");}
        }

        file.close();
        String[] obj = {"-1"};
        return obj;
    }

    public static String readData(String arg, BufferedReader file, int pos) throws IOException
    {
        //arg="-2":nao verificar arg
        int c = 0;
        String data = "";
        String linha;

        while (true)
        {
            linha = file.readLine();
            if (linha == null) break;
            
            if (!(arg.equals("-2")))
            {
               if (!(linha.split(",")[pos].equals(arg)) && !(linha.strip().equals(""))) 
                {
                    if (c > 0) data += "\n";
                    c++;
                    data += linha;
                }
                // critico: se algum outra pessoa não autorizada entrar na conta, o usuario perde a sessão 
            }

            else {
                if (c > 0) data += "\n";
                    c++;
                    data += linha;
            }
            
        }

        return data;
    }

    public static String to_cvsString(String[] a)
    {
        String out = "";

        for (int i=0; i < a.length; i++)
        {
            if (i > 0) out += ",";
            out += a[i];
        }

        return out;
    }
}
