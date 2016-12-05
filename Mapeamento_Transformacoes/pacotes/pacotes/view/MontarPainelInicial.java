package pacotes.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import pacotes.controle.*;

public class MontarPainelInicial implements MouseListener, MouseMotionListener {

	public JFrame baseFrame;
	private JPanel basePanel;
	private JPanel outputPanel;
	private JPanel buttonPanel;

	private JButton btEnd;
	private JButton btLimparTela;
	private JButton btViewPort;

	private JLabel labelVisor;

	private Graphics desenho;

	private ControlarAplicativo controlePrograma;

	private int cliques;

	private Point pontoPrimeiroClique;

	// ******************************************************************************************************************
	// MONTAR PAINEL INICIAL
	public MontarPainelInicial(ControlarAplicativo controlePrograma) {
		this.controlePrograma = controlePrograma;
		Util u = new Util();

		// LAYOUT
		baseFrame = new JFrame();
		baseFrame.setLayout(new BoxLayout(baseFrame.getContentPane(), BoxLayout.Y_AXIS));

		baseFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // FITS PANEL TO THE
															// ACTUAL MONITOR
		baseFrame.setUndecorated(true); // TURN OFF ALL THE PANEL BORDERS

		basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());

		// OUTPUT PANEL
		outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());

		// BUTTON PANEL
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(0, 70));
		buttonPanel.setBackground(Color.BLUE);

		JPanel panel = new JPanel(new BorderLayout()); // LABEL VISOR
		labelVisor = new JLabel("");
		labelVisor.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(labelVisor, BorderLayout.NORTH);
		buttonPanel.add(panel);
		labelVisor.setBackground(Color.WHITE);
		labelVisor.setForeground(Color.BLACK);
		labelVisor.setBorder(new EmptyBorder(5, 5, 5, 5));

		// ADICIONAR BOTÕES
		// Viewport
		btViewPort = u.addAButton("Área de Trabalho", "botaoViewPort", buttonPanel);
		btViewPort.addActionListener(controlePrograma);
		btViewPort.setBackground(Color.CYAN);
		btViewPort.setForeground(Color.BLACK);

		// Limpar Tela
		btLimparTela = u.addAButton("Limpar Tela", "botaoLimpar", buttonPanel);
		btLimparTela.addActionListener(controlePrograma);
		btLimparTela.setBackground(new Color(219, 61, 61));
		btLimparTela.setForeground(Color.WHITE);

		// Sair do Programa
		btEnd = u.addAButton("Sair", "botaoFim", buttonPanel);
		btEnd.addActionListener(controlePrograma);
		btEnd.setBackground(new Color(219, 61, 61));
		btEnd.setForeground(Color.WHITE);

		// OUVINTES DO MOUSE
		outputPanel.addMouseListener(this);
		outputPanel.addMouseMotionListener(this);

		// VISIBLE PANELS
		baseFrame.add(basePanel);
		basePanel.add(outputPanel, BorderLayout.CENTER);
		basePanel.add(buttonPanel, BorderLayout.PAGE_END);

		baseFrame.setVisible(true);
	}

	// ******************************************************************************************************************
	// METODO UTILIZADO PARA ADICIONAR UM BOTAO A UM CONTAINER DO PROGRAMA
	// return JButton -> retorna o botão

	// ******************************************************************************************************************
	// METODO PARA MOSTRAR O FRAME BASICO
	public void showPanel() {
		basePanel.setVisible(true);
	}

	// ******************************************************************************************************************
	public void mouseClicked(MouseEvent evento) {
		Point P = new Point(evento.getX(), evento.getY());

		// CONTROLE DE CLIQUE
		if (this.cliques == 0) { // Primeiro clique
			this.cliques = 1;
			this.pontoPrimeiroClique = P;
			controlePrograma.desenharCirculoMarcacao(Color.RED, desenho, P.getX(), P.getY());

		} else { // Segundo clique
			if ((this.pontoPrimeiroClique.getY() != P.getY()) && (this.pontoPrimeiroClique.getX() != P.getX())) {
				this.cliques = 0;
				controlePrograma.desenharCirculoMarcacao(new Color(238, 238, 238), desenho,
						this.pontoPrimeiroClique.getX(), this.pontoPrimeiroClique.getY());
				controlePrograma.desenharRetanguloPontos(this.pontoPrimeiroClique, P, Color.BLACK, desenho);
			}
		}
	}

	// ******************************************************************************************************************
	public void mouseEntered(MouseEvent evento) {
	}

	// ******************************************************************************************************************
	public void mouseMoved(MouseEvent e) {
		Point P = new Point(e.getX(), e.getY());

		this.labelVisor.setText("Ponto: ( " + (int) P.getX() + " ; " + (int) P.getY() + " )");
	}

	// ******************************************************************************************************************
	public void mouseExited(MouseEvent evento) {
	}

	// ******************************************************************************************************************
	public void mousePressed(MouseEvent evento) {
	}

	// ******************************************************************************************************************
	public void mouseReleased(MouseEvent evento) {
	}

	// ******************************************************************************************************************
	public void mouseDragged(MouseEvent e) {
	}

	// ******************************************************************************************************************
	public Graphics iniciarGraphics() {
		desenho = outputPanel.getGraphics();
		return (desenho);
	}

	// ******************************************************************************************************************
	// OCULTAR O DESENHO
	public void ocultarDesenho() {
		Color cinza = new Color(238, 238, 238);
		desenho.clearRect(0, 0, outputPanel.getWidth(), outputPanel.getHeight());
		desenho.setColor(cinza);
	}

	// ******************************************************************************************************************
	// MENSAGEM INICIAL
	public void mensagemInicial() {
		JOptionPane.showMessageDialog(null, "Aulas 07 e 08: viewPort e transformações geométricas.", "Bem-vindo!",
				JOptionPane.INFORMATION_MESSAGE);
	}

	// ******************************************************************************************************************
}
