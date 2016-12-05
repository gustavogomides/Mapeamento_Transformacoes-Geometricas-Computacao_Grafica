package pacotes.controle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import pacotes.modelo.*;
import pacotes.view.*;

public class ControlarViewPort implements ActionListener, ItemListener {

	public ArrayList<Retangulo> retangulos = new ArrayList<>();

	private ControlarRetangulo controleRetangulo = new ControlarRetangulo();

	private ViewPort viewPort;

	private Graphics vpg;

	private boolean mapear = false;
	public boolean habilitarClique = false;
	public boolean escala = false;
	public boolean rotacao = false;
	public boolean tTranslacao = false;
	public boolean tEscala = false;
	public boolean tRotacao = false;

	public Point pontoClique = null;

	// ****************************************************************************
	// CONSTRUTORES DA CLASSE ControlarViewPort
	public ControlarViewPort(ArrayList<Retangulo> retangulos) {
		viewPort = new ViewPort(this);
		viewPort.showPanel();
		vpg = viewPort.iniciarGraphics();
		this.retangulos = retangulos;
	}

	public ControlarViewPort() {

	}

	// ****************************************************************************
	// ACTION DOS BOT�ES DO VIEWPORT
	@Override
	public void actionPerformed(ActionEvent e) {

		// MAPEAR
		if (e.getSource() == viewPort.btMapear) {
			mapear = true;
			setarBotoes(false);
			mapeamento(Color.BLACK, vpg, viewPort.frame.getHeight() - 70, viewPort.frame.getWidth());
		}

		if (e.getSource() == viewPort.btTranslacao) {
			translacao(Color.RED, vpg, viewPort.frame.getHeight() - 70, viewPort.frame.getWidth());
			viewPort.setarBotoes(true);
		}

		if (e.getSource() == viewPort.btEscala) {
			escala = true;
			rotacao = false;
			int opcao = opcaoReferencia();
			if (opcao == 0) {
				escala(Color.GREEN, vpg, viewPort.frame.getHeight() - 70, viewPort.frame.getWidth(), opcao);
			} else {
				JOptionPane.showMessageDialog(null, "Clique em um ponto na �rea de trabalho para ser a refer�ncia!");
				habilitarClique = true;
			}
			viewPort.setarBotoes(true);
		}

		if (e.getSource() == viewPort.btRotacao) {
			escala = false;
			rotacao = true;
			int opcao = opcaoReferencia();
			if (opcao == 0) {
				rotacao(Color.BLUE, vpg, opcao);
			} else {
				JOptionPane.showMessageDialog(null, "Clique em um ponto na �rea de trabalho para ser a refer�ncia!");
				habilitarClique = true;
			}
			viewPort.setarBotoes(true);
		}

		// TRANSFORMA��ES SUCESSIVAS
		if (e.getSource() == viewPort.btTransformacoesSucessivas) {
			viewPort.checkBox();
		}

		// CHECK-BOX
		if (e.getSource() == viewPort.btCheckBox) {
			viewPort.frame1.dispose();
			JOptionPane.showMessageDialog(null,
					"Para comparar as transforma��es compostas com as transforma��es individuais execute as transforma��es "
							+ "\nindividuais em uma nova �rea de trabalho com os mesmos par�metros de transforma��es utilizados nas "
							+ "transforma��es compostas.",
					"Aviso!", JOptionPane.INFORMATION_MESSAGE);
			transformacoesSucessivas(vpg, viewPort.frame.getHeight() - 70, viewPort.frame.getWidth(), true);
		}

		// LIMPAR TELA
		if (e.getSource() == viewPort.btLimparTela) {
			viewPort.ocultarDesenho();
		}

	}

	// ****************************************************************************
	// DESENHAR C�RCULO MARCA��O
	public void desenharCirculoMarcacao(Color cor, Graphics g, double x, double y) {
		ControlarCirculo controleCirculo = new ControlarCirculo();
		controleCirculo.desenharCirculo(g, cor, 3, new Circulo(x, y, 3));
	}

	// ****************************************************************************
	// MAPEAMENTO
	private void mapeamento(Color cor, Graphics g, int altura, int largura) {
		controleRetangulo.mapeamento(cor, g, altura, largura, viewPort, mapear, retangulos);
	}

	// ****************************************************************************
	// TRANSLACAO
	private void translacao(Color cor, Graphics g, int altura, int largura) {
		// controleRetangulo.translacao(cor, g, altura, largura, mapear,
		// viewPort);
		controleRetangulo.translacao(cor, g, altura, largura, mapear, viewPort);
	}

	// ****************************************************************************
	// ESCALA
	public void escala(Color cor, Graphics g, int altura, int largura, int opcao) {
		controleRetangulo.escala(cor, g, altura, largura, mapear, viewPort, opcao, pontoClique);
	}

	// ****************************************************************************
	// ROTACAO
	public void rotacao(Color cor, Graphics g, int opcao) {
		controleRetangulo.rotacao(cor, g, opcao, pontoClique, mapear, viewPort);
	}

	// ****************************************************************************
	// TRANSFORMA��ES SUCESSIVAS
	private void transformacoesSucessivas(Graphics g, int altura, int largura, boolean tipo) {
		if (tTranslacao && tEscala && tRotacao) {
			int opcao = opcaoTransformacoes("ter");
			if (opcao == 0) { // ter
				controleRetangulo.transformacoesSucessivas("ter", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			} else if (opcao == 1) { // tre
				controleRetangulo.transformacoesSucessivas("tre", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			} else if (opcao == 2) { // etr
				controleRetangulo.transformacoesSucessivas("etr", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			} else if (opcao == 3) { // ert
				controleRetangulo.transformacoesSucessivas("ert", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			} else if (opcao == 4) { // rte
				controleRetangulo.transformacoesSucessivas("rte", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			} else if (opcao == 5) { // ret
				controleRetangulo.transformacoesSucessivas("ret", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			}
		} else if (tTranslacao && tRotacao) { // transla��o e rota��o
			int opcao = opcaoTransformacoes("tr");
			if (opcao == 0) { // t -> r
				controleRetangulo.transformacoesSucessivas("tr", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			} else { // r -> t
				controleRetangulo.transformacoesSucessivas("rt", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			}
		} else if (tTranslacao && tEscala) { // transla��o e escala
			int opcao = opcaoTransformacoes("te");
			if (opcao == 0) { // t -> e
				controleRetangulo.transformacoesSucessivas("te", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			} else { // e -> t
				controleRetangulo.transformacoesSucessivas("et", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			}
		} else if (tEscala && tRotacao) { // escala e rota��o
			int opcao = opcaoTransformacoes("er");
			if (opcao == 0) { // r -> e
				controleRetangulo.transformacoesSucessivas("re", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			} else { // e -> r
				controleRetangulo.transformacoesSucessivas("er", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
			}
		} else if (tTranslacao) { // transla��o
			controleRetangulo.transformacoesSucessivas("t", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
		} else if (tEscala) { // escala
			controleRetangulo.transformacoesSucessivas("e", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
		} else if (tRotacao) { // rota��o
			controleRetangulo.transformacoesSucessivas("r", Color.ORANGE, vpg, altura, largura, viewPort, mapear);
		}
	}

	// ****************************************************************************
	// OP��O DE REFER�NCIA
	public int opcaoReferencia() {
		Object[] options = { "Origem", "Ponto Qualquer" };
		int opcao = JOptionPane.showOptionDialog(null, "Qual ser� a refer�ncia?", "Refer�ncia",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		return opcao;
	}

	// ****************************************************************************
	// OP��O DE TRANSFORMACAO SUCESSIVAS
	public int opcaoTransformacoes(String transformacoes) {
		int opcao = 0;
		if (transformacoes.equals("ter")) {
			Object[] options = { "Transla��o, Escala, Rota��o", "Transla��o, Rota��o, Escala",
					"Escala, Transla��o, Rota��o", "Escala, Rota��o, Transla��o", "Rota��o, Transla��o, Escala",
					"Rota��o, Escala, Transla��o" };
			opcao = JOptionPane.showOptionDialog(null, "Qual ser� a ordem de transforma��o?", "Ordem de transforma��o",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[5]);
		} else if (transformacoes.equals("te")) { // t, e
			Object[] options = { "Transla��o -> Escala", "Escala -> Transla��o" };
			opcao = JOptionPane.showOptionDialog(null, "Qual ser� a ordem de transforma��o?", "Ordem de transforma��o",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		} else if (transformacoes.equals("tr")) { // t, r
			Object[] options = { "Transla��o -> Rota��o", "Rota��o -> Transla��o" };
			opcao = JOptionPane.showOptionDialog(null, "Qual ser� a ordem de transforma��o?", "Ordem de transforma��o",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		} else if (transformacoes.equals("er")) { // e, r
			Object[] options = { "Rota��o -> Escala", "Escala -> Rota��o" };
			opcao = JOptionPane.showOptionDialog(null, "Qual ser� a ordem de transforma��o?", "Ordem de transforma��o",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		}

		return opcao;
	}

	// ****************************************************************************
	// SETAR BOT�ES
	public void setarBotoes(boolean estado) {
		viewPort.setarBotoes(estado);
	}

	// ****************************************************************************
	// ITENS SELECIONADOS CHECK-BOX
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();

		if (src == viewPort.cTranslacao) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				tTranslacao = true;
			}
		} else if (src == viewPort.cEscala) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				tEscala = true;
			}
		} else if (src == viewPort.cRotacao) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				tRotacao = true;
			}
		}

	}

	// ****************************************************************************
	// SETAR PONTO CLICADO NO VIEWPORT
	public void setPointClique(Point pontoClique) {
		this.pontoClique = pontoClique;
	}
}
