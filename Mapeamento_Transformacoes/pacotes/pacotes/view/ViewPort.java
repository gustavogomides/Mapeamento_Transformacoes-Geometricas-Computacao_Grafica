package pacotes.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import pacotes.controle.*;

import javax.swing.JCheckBox;

public class ViewPort implements MouseListener, MouseMotionListener {

	public JFrame frame;
	public JFrame frame1;
	public JLabel labelVisor;

	private JPanel buttonPanel;
	private JPanel outputPanel;
	private JPanel basePanel;

	public JButton btTranslacao;
	public JButton btEscala;
	public JButton btRotacao;
	public JButton btLimparTela;
	public JButton btMapear;
	public JButton btTransformacoesSucessivas;
	public JButton btCheckBox;

	public JCheckBox cTranslacao;
	public JCheckBox cEscala;
	public JCheckBox cRotacao;

	private Util u = new Util();

	private Toolkit tk = Toolkit.getDefaultToolkit();
	private Dimension dim = tk.getScreenSize();

	private Graphics viewport;

	private ControlarViewPort controleViewPort;

	private Point clique;

	// ******************************************************************************************************************
	// CONSTRUTOR DA CLASSE ControlarViewPort
	public ViewPort(ControlarViewPort controleViewPort) {
		this.controleViewPort = controleViewPort;
		// LAYOUT
		frame = new JFrame();
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setSize(new Dimension(800, 570));
		frame.setTitle("Área de Trabalho");
		frame.setLocation(new Point((int) ((dim.getWidth() / 2) - 150), 80));

		// OUTPUT PANEL
		outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());

		basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());

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
		// Mapear
		btMapear = u.addAButton("Mapear", "botaoMapearVP", buttonPanel);
		btMapear.addActionListener(controleViewPort);
		btMapear.setBackground(Color.CYAN);
		btMapear.setForeground(Color.BLACK);

		// Translação
		btTranslacao = u.addAButton("Translação", "botaoTranslacaoViewPort", buttonPanel);
		btTranslacao.addActionListener(controleViewPort);
		btTranslacao.setBackground(Color.CYAN);
		btTranslacao.setForeground(Color.BLACK);

		// Escala
		btEscala = u.addAButton("Escala", "botaoEscala", buttonPanel);
		btEscala.addActionListener(controleViewPort);
		btEscala.setBackground(Color.CYAN);
		btEscala.setForeground(Color.BLACK);

		// Rotação
		btRotacao = u.addAButton("Rotação", "botaoRotacao", buttonPanel);
		btRotacao.addActionListener(controleViewPort);
		btRotacao.setBackground(Color.CYAN);
		btRotacao.setForeground(Color.BLACK);

		// Transformações sucessivas
		btTransformacoesSucessivas = u.addAButton("Tranformações Sucessivas Compostas", "botaoTransformacao",
				buttonPanel);
		btTransformacoesSucessivas.addActionListener(controleViewPort);
		btTransformacoesSucessivas.setBackground(Color.CYAN);
		btTransformacoesSucessivas.setForeground(Color.BLACK);

		// Limpar Tela
		btLimparTela = u.addAButton("Limpar Tela", "botaoLimparViewPort", buttonPanel);
		btLimparTela.addActionListener(controleViewPort);
		btLimparTela.setBackground(new Color(219, 61, 61));
		btLimparTela.setForeground(Color.WHITE);

		// OUVINTES DO MOUSE
		outputPanel.addMouseListener(this);
		outputPanel.addMouseMotionListener(this);

		// VISIBLE PANELS
		frame.add(basePanel);
		basePanel.add(outputPanel, BorderLayout.CENTER);
		basePanel.add(buttonPanel, BorderLayout.PAGE_END);

		frame.setVisible(true);

	}

	// ******************************************************************************************************************
	// METODO PARA MOSTRAR O FRAME BASICO
	public void showPanel() {
		basePanel.setVisible(true);
	}

	// ******************************************************************************************************************
	// INICIAR GRÁFICOS
	public Graphics iniciarGraphics() {
		viewport = outputPanel.getGraphics();
		return (viewport);
	}

	// ******************************************************************************************************************
	// OCULTAR O DESENHO
	public void ocultarDesenho() {
		Color cinza = new Color(238, 238, 238);
		viewport.clearRect(0, 0, outputPanel.getWidth(), outputPanel.getHeight());
		viewport.setColor(cinza);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point P = new Point(e.getX(), e.getY());
		this.labelVisor.setText("Ponto: ( " + (int) P.getX() + " ; " + (int) P.getY() + " )");
	}

	@Override
	public void mouseClicked(MouseEvent evento) {

		if (controleViewPort.habilitarClique) {
			clique = new Point(evento.getX(), evento.getY());
			controleViewPort.desenharCirculoMarcacao(Color.RED, viewport, clique.getX(), clique.getY());
			controleViewPort.setPointClique(clique);

			if (controleViewPort.escala) {
				controleViewPort.escala(Color.RED, viewport, frame.getHeight() - 70, frame.getWidth(), 1);
			} else if (controleViewPort.rotacao) {
				controleViewPort.rotacao(Color.BLUE, viewport, 1);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	// ******************************************************************************************************************
	// CHECK-BOX TRANSFORMAÇÕES SUCESSIVAS
	public void checkBox() {
		JPanel panel = new JPanel(new GridLayout(2, 3, 5, 5));

		frame1 = new JFrame();
		frame1.setTitle("Transformações Sucessivas");
		JLabel b4, b6;

		cTranslacao = new JCheckBox("Translação");
		cEscala = new JCheckBox("Escalamento");
		cRotacao = new JCheckBox("Rotação");

		cTranslacao.addItemListener(controleViewPort);
		cEscala.addItemListener(controleViewPort);
		cRotacao.addItemListener(controleViewPort);

		b4 = new JLabel("");
		btCheckBox = u.addAButton("Transformar", "botaoCheckBox", panel);
		btCheckBox.addActionListener(controleViewPort);
		b6 = new JLabel("");

		panel.add(cTranslacao);
		panel.add(cEscala);
		panel.add(cRotacao);
		panel.add(b4);
		panel.add(btCheckBox);
		panel.add(b6);

		frame1.setLocationRelativeTo(frame);
		frame1.add(panel);
		frame1.pack();
		frame1.setSize(400, 120);
		frame1.setVisible(true);
	}

	// ******************************************************************************************************************
	// SETAR ESTADO BOTÕES
	public void setarBotoes(boolean estado) {
		btTranslacao.setEnabled(estado);
		btEscala.setEnabled(estado);
		btRotacao.setEnabled(estado);
		btLimparTela.setEnabled(estado);
		btTransformacoesSucessivas.setEnabled(estado);
	}
	// ******************************************************************************************************************
}
