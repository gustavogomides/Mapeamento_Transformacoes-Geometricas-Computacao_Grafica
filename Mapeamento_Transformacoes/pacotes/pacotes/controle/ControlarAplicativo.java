package pacotes.controle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import pacotes.modelo.*;
import pacotes.view.*;

public class ControlarAplicativo implements ActionListener {

	private MontarPainelInicial pnCenario;
	public ArrayList<Retangulo> retangulos = new ArrayList<>();

	private ControlarRetangulo controleRetangulo;
	private ControlarCirculo controleCirculo = new ControlarCirculo();

	// ****************************************************************************
	// CONSTRUTORES DA CLASSE ControlarAplicativo
	public ControlarAplicativo() {
		pnCenario = new MontarPainelInicial(this);
		pnCenario.showPanel();
		pnCenario.mensagemInicial();
		pnCenario.iniciarGraphics();
		controleRetangulo = new ControlarRetangulo();
	}

	public ControlarAplicativo(boolean opcao) {

	}

	// ****************************************************************************
	// ACTION PERFORMED - CAPTURAR E TRATAR CLIQUE DOS BOTÕES
	public void actionPerformed(ActionEvent e) {
		String comando;
		comando = e.getActionCommand();

		// VIEWPORT
		if (comando.equals("botaoViewPort")) {
			ControlarViewPort controlarViewPort = new ControlarViewPort(retangulos);
			controlarViewPort.setarBotoes(false);
		}

		// LIMPAR TELA
		if (comando.equals("botaoLimpar")) {
			if (retangulos.size() != 0) {
				int option;

				option = JOptionPane.showConfirmDialog(null, "Deseja limpar a tela (excluir o desenho)?", "Limpar",
						JOptionPane.YES_NO_OPTION);

				if (option == JOptionPane.YES_OPTION) {
					pnCenario.ocultarDesenho();
					retangulos.clear();
				}
			}
		}

		// FIM DO PROGRAMA
		if (comando.equals("botaoFim")) {
			int option;

			option = JOptionPane.showConfirmDialog(null, "Deseja sair da aplicação?", "Sair",
					JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}

	}

	// ****************************************************************************
	// DESENHAR RETANGULOS POR MEIO DE DOIS PONTOS
	public void desenharRetanguloPontos(Point pontoEsquerdo, Point pontoDireito, Color cor, Graphics g) {
		Retangulo r = new Retangulo(pontoEsquerdo, pontoDireito);
		retangulos.add(r);
		controleRetangulo.desenharRetangulo(r, cor, g);
	}

	// ****************************************************************************
	// DESENHAR CÍRCULO MARCAÇÃO
	public void desenharCirculoMarcacao(Color cor, Graphics g, double x, double y) {
		controleCirculo.desenharCirculo(g, cor, 3, new Circulo(x, y, 3));
	}

}
