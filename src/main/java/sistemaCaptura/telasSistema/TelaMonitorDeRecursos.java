package sistemaCaptura.telasSistema;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
    import org.springframework.jdbc.core.JdbcTemplate;
import sistemaCaptura.DadosCaptura;
import sistemaCaptura.conexao.Conexao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TelaMonitorDeRecursos {
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConexaoDoBanco();

    java.util.Timer timerTela = new Timer();
    JFrame frame;
    public void gerarTelaMonitor(Integer idMaquina){

        frame = new JFrame("Monitor de Recursos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        tableModel.addColumn("Consumo CPU (%)");
        tableModel.addColumn("Consumo Disco");
        tableModel.addColumn("Consumo RAM");
        tableModel.addColumn("Janelas Abertas");
        tableModel.addColumn("Data hora");
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel();

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        timerTela.schedule(new TimerTask() {
            @Override
            public void run() {

                List<DadosCaptura> dadosConsumoCpu = con.query("select dataHora,  consumo , qtdJanelasAbertas from historico where fkHardware =1 and fkMaquina = ? order by idHistorico desc LIMIT 0, 20",
                        new BeanPropertyRowMapper<>(DadosCaptura.class),idMaquina);
                List<DadosCaptura> dadosConsumoRam = con.query("select dataHora,  consumo , qtdJanelasAbertas from historico where fkHardware =2 and fkMaquina = ? order by idHistorico desc LIMIT 0, 20",
                        new BeanPropertyRowMapper<>(DadosCaptura.class),idMaquina);
                List<DadosCaptura> dadosConsumoDisco = con.query("select dataHora,  consumo , qtdJanelasAbertas from historico where fkHardware =3 and fkMaquina = ? order by idHistorico desc LIMIT 0, 20",
                        new BeanPropertyRowMapper<>(DadosCaptura.class),idMaquina);



                tableModel.setRowCount(0);
                for (int i =0 ; i < dadosConsumoCpu.size();i++) {
                    tableModel.addRow(new Object[]{dadosConsumoCpu.get(i).getConsumo(),dadosConsumoDisco.get(i).getConsumo(),dadosConsumoRam.get(i).getConsumo(),dadosConsumoCpu.get(i).getQtdJanelasAbertas(),dadosConsumoCpu.get(i).getDataHora()});

                }
            }
        },0,2000);

        frame.setVisible(true);

    }

    public void fecharTela(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



}

