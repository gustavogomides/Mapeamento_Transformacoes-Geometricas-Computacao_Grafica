package pacotes.controle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import pacotes.modelo.*;
import pacotes.view.*;

public class ControlarRetangulo {

	private ControlarReta ctrReta = new ControlarReta();

	public ArrayList<Retangulo> retangulosViewPort = new ArrayList<>();
	public ArrayList<RetanguloRotacionado> retangulosRotacionados = new ArrayList<>();

	private Toolkit tk = Toolkit.getDefaultToolkit();
	private Dimension dim = tk.getScreenSize();

	// *************************************************************************************
	// DESENHAR RETANGULO
	public void desenharRetangulo(Retangulo r, Color c, Graphics g) {
		ctrReta = new ControlarReta();

		Point p1 = new Point(), p2 = new Point();
		p1 = r.getpontoInicial();
		p2 = r.getpontoFinal();

		// Reta de P1 a  P2 sendo o X fixada no P1.
		ctrReta.desenharReta(new Reta(p1, new Point(p2.x, p1.y)), c, g);

		// Reta de P1 a P2 sendo o Y fixada no P1
		ctrReta.desenharReta(new Reta(p1, new Point(p1.x, p2.y)), c, g);

		// Reta de P1 a  P2 sendo o X fixada no P2.
		ctrReta.desenharReta(new Reta(new Point(p1.x, p2.y), p2), c, g);

		// Reta de P1 a  P2 sendo o Y fixada no P2.
		ctrReta.desenharReta(new Reta(p2, new Point(p2.x, p1.y)), c, g);

	}

	// *************************************************************************************
	// MAPEAMENTO
	public void mapeamento(Color cor, Graphics g, int altura, int largura, ViewPort viewPort, boolean mapear,
			ArrayList<Retangulo> retangulos) {

		if (retangulos.size() != 0) {
			viewPort.ocultarDesenho();

			double wx = 0;
			double wy = 0;
			double wh = dim.height;
			double wl = dim.width;

			double vx = 0;
			double vy = 0;

			double vh = altura;
			double vl = largura;

			double constante = vl / wl;
			double constante1 = vh / wh;

			retangulosViewPort.clear();
			retangulosRotacionados.clear();

			ControlarRetangulo controlarRetangulo = new ControlarRetangulo();
			Point p1 = new Point(), p2 = new Point(), p11 = null, p22 = null;
			int ci, di, cf, df;

			for (Retangulo r : retangulos) {
				p1 = r.getpontoInicial();
				p2 = r.getpontoFinal();

				ci = (int) (constante * (p1.getX() - wx) + vx);
				di = (int) (constante1 * (p1.getY() - wy) + vy);
				cf = (int) (constante * (p2.getX() - wx) + vx);
				df = (int) (constante1 * (p2.getY() - wy) + vy);

				p11 = new Point(ci, di);
				p22 = new Point(cf, df);

				controlarRetangulo.desenharRetangulo(new Retangulo(p11, p22), cor, g);

				retangulosViewPort.add(new Retangulo(p11, p22));
			}
			viewPort.setarBotoes(true);
		} else {
			JOptionPane.showMessageDialog(null, "Não existem retângulos para o mapeamento!");
		}

	}

	// *************************************************************************************
	// H
	private int h(int largura) {
		int h = Integer.parseInt(JOptionPane.showInputDialog("Deslocamento Horizontal Translação"));

		for (RetanguloRotacionado r : retangulosRotacionados) {
			while (((r.getpontoInicial().getX() + h) > largura) || ((r.getpontoFinal().getX() + h) > largura)
					|| ((r.getPonto3().getX() + h) > largura) || ((r.getPonto4().getX() + h) > largura)) {
				JOptionPane.showMessageDialog(null, "Deslocamento Horizontal Translação excede o tamanho da janela!");
				h = Integer.parseInt(JOptionPane.showInputDialog("Deslocamento Horizontal Translação:"));
			}
		}

		for (Retangulo r : retangulosViewPort) {
			while (((r.getpontoInicial().getX() + h) > largura) || ((r.getpontoFinal().getX() + h) > largura)) {
				JOptionPane.showMessageDialog(null, "Deslocamentoa Horizontal Translação excede o tamanho da janela!");
				h = Integer.parseInt(JOptionPane.showInputDialog("Deslocamento Horizontal Translação:"));
			}
		}
		return h;
	}

	// *************************************************************************************
	// V
	private int v(int altura) {
		int v = Integer.parseInt(JOptionPane.showInputDialog("Deslocamento Vertical Translação"));
		for (RetanguloRotacionado r : retangulosRotacionados) {
			while (((r.getpontoInicial().getY() + v) > altura) || ((r.getpontoFinal().getY() + v) > altura)
					|| ((r.getPonto3().getY() + v) > altura) || ((r.getPonto4().getY() + v) > altura)) {
				JOptionPane.showMessageDialog(null, "Deslocamentoa Vertical Translação excede o tamanho da janela!");
				v = Integer.parseInt(JOptionPane.showInputDialog("Deslocamento Vertical Translação"));
			}
		}
		for (Retangulo r : retangulosViewPort) {
			while (((r.getpontoInicial().getY() + v) > altura) || ((r.getpontoFinal().getY() + v) > altura)) {
				JOptionPane.showMessageDialog(null, "Deslocamentoa Vertical Translação excede o tamanho da janela!");
				v = Integer.parseInt(JOptionPane.showInputDialog("Fator de Escala Vertical Translação"));
			}
		}
		return v;
	}

	// *************************************************************************************
	// TRANSLAÇÃO
	public void translacao(Color cor, Graphics g, int altura, int largura, boolean mapear, ViewPort viewPort) {
		if (mapear) {
			// H
			int h = h(largura);

			// V
			int v = v(altura);

			viewPort.ocultarDesenho();

			for (RetanguloRotacionado r : retangulosRotacionados) {
				r.getpontoInicial().setLocation(r.getpontoInicial().getX() + h, r.getpontoInicial().getY() + v);
				r.getpontoFinal().setLocation(r.getpontoFinal().getX() + h, r.getpontoFinal().getY() + v);
				r.getPonto3().setLocation(r.getPonto3().getX() + h, r.getPonto3().getY() + v);
				r.getPonto4().setLocation(r.getPonto4().getX() + h, r.getPonto4().getY() + v);
				rotacionaRetanguloRotacionado(r, cor, g, 0, new Point(0, 0));
			}

			// CALCULO
			double mt[][] = { { 1, 0, h }, { 0, 1, v }, { 0, 0, 1 } };

			for (Retangulo r : retangulosViewPort) {
				if (!r.isRotacionado()) {
					double mi[][] = multiplicarMatrizes(mt, r.getpontoInicial());
					double mf[][] = multiplicarMatrizes(mt, r.getpontoFinal());

					r.getpontoInicial().setLocation(mi[0][0], mi[1][0]);
					r.getpontoFinal().setLocation(mf[0][0], mf[1][0]);

					desenharRetangulo(r, cor, g);

				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Não existem retângulos mapeados!");
		}
	}

	// *************************************************************************************
	// SX
	private double sx(int largura) {
		double sx = Double.parseDouble(JOptionPane.showInputDialog("Fator de Escala Horizontal"));

		for (RetanguloRotacionado r : retangulosRotacionados) {
			while (((r.getpontoInicial().getX() * sx) > largura) || ((r.getpontoFinal().getX() * sx) > largura)
					|| ((r.getPonto3().getX() * sx) > largura) || ((r.getPonto4().getX() * sx) > largura)) {
				JOptionPane.showMessageDialog(null, "Fator de Escala Horizontal excede o tamanho da janela!");
				sx = Double.parseDouble(JOptionPane.showInputDialog("Fator de Escala Horizontal"));
			}
		}
		for (Retangulo r : retangulosViewPort) {
			while (((r.getpontoInicial().getX() * sx) > largura) || ((r.getpontoFinal().getX() * sx) > largura)) {
				JOptionPane.showMessageDialog(null, "Fator de Escala Horizontal excede o tamanho da janela!");
				sx = Double.parseDouble(JOptionPane.showInputDialog("Fator de Escala Horizontal"));
			}
		}
		return sx;
	}

	// *************************************************************************************
	// SY
	private double sy(int altura) {
		double sy = Double.parseDouble(JOptionPane.showInputDialog("Fator de Escala Vertical"));
		for (RetanguloRotacionado r : retangulosRotacionados) {
			while (((r.getpontoInicial().getY() * sy) > altura) || ((r.getpontoFinal().getY() * sy) > altura)
					|| ((r.getPonto3().getY() * sy) > altura) || ((r.getPonto4().getY() * sy) > altura)) {
				JOptionPane.showMessageDialog(null, "Fator de Escala Vertical excede o tamanho da janela!");
				sy = Double.parseDouble(JOptionPane.showInputDialog("Fator de Escala Vertical"));
			}
		}

		for (Retangulo r : retangulosViewPort) {
			while (((r.getpontoInicial().getY() * sy) > altura) || ((r.getpontoFinal().getY() * sy) > altura)) {
				JOptionPane.showMessageDialog(null, "Fator de Escala Vertical excede o tamanho da janela!");
				sy = Double.parseDouble(JOptionPane.showInputDialog("Fator de Escala Vertical"));
			}
		}
		return sy;
	}

	// *************************************************************************************
	// ESCALA
	public void escala(Color cor, Graphics g, int altura, int largura, boolean mapear, ViewPort viewPort, int opcao,
			Point pontoClique) {

		if (mapear) {
			// SX
			double sx = sx(largura);

			// SY
			double sy = sy(altura);

			viewPort.ocultarDesenho();

			if (opcao == 0) { // referência: origem
				escalaOrigem(sx, sy, cor, g);
			} else { // referência: ponto qualquer
				escalaPonto(sx, sy, cor, g, pontoClique);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Não existem retângulos mapeados!");
		}
	}

	// ****************************************************************************
	// ESCALA REFERÊNCIA ORIGEM
	public void escalaOrigem(double sx, double sy, Color cor, Graphics g) {
		for (RetanguloRotacionado r : retangulosRotacionados) {
			r.getpontoInicial().setLocation(r.getpontoInicial().getX() * sx, r.getpontoInicial().getY() * sy);
			r.getpontoFinal().setLocation(r.getpontoFinal().getX() * sx, r.getpontoFinal().getY() * sy);
			r.getPonto3().setLocation(r.getPonto3().getX() * sx, r.getPonto3().getY() * sy);
			r.getPonto4().setLocation(r.getPonto4().getX() * sx, r.getPonto4().getY() * sy);
			rotacionaRetanguloRotacionado(r, cor, g, 0, new Point(0, 0));
		}

		double me[][] = { { sx, 0, 0 }, { 0, sy, 0 }, { 0, 0, 1 } };

		for (Retangulo r : retangulosViewPort) {
			if (!r.isRotacionado()) {
				double mi[][] = multiplicarMatrizes(me, r.getpontoInicial());
				double mf[][] = multiplicarMatrizes(me, r.getpontoFinal());

				r.getpontoInicial().setLocation(mi[0][0], mi[1][0]);
				r.getpontoFinal().setLocation(mf[0][0], mf[1][0]);

				desenharRetangulo(r, cor, g);

			}
		}
	}

	// ****************************************************************************
	// ESCALA REFERÊNCIA PONTO QUALQUER
	public void escalaPonto(double sx, double sy, Color cor, Graphics vpg, Point pontoClique) {
		ArrayList<RetanguloRotacionado> tortos = new ArrayList<>();
		for (RetanguloRotacionado r : retangulosRotacionados) {
			RetanguloRotacionado rt = new RetanguloRotacionado(pontoClique, pontoClique, pontoClique, pontoClique);
			rt = rotacionaRetanguloRotacionado(r, cor, vpg, 0, pontoClique);
			tortos.add(rt);
		}

		retangulosRotacionados.clear();
		retangulosRotacionados = tortos;

		Point p1 = new Point(), p2 = new Point();
		double xi = 0, yi = 0, xf = 0, yf = 0;

		for (Retangulo r : retangulosViewPort) {
			if (!r.isRotacionado()) {
				p1 = r.getpontoInicial();
				p2 = r.getpontoFinal();

				xi = ((p1.getX() - pontoClique.getX()) * sx) + pontoClique.getX();
				yi = ((p1.getY() - pontoClique.getY()) * sy) + pontoClique.getY();
				xf = ((p2.getX() - pontoClique.getX()) * sx) + pontoClique.getX();
				yf = ((p2.getY() - pontoClique.getY()) * sy) + pontoClique.getY();

				r.getpontoInicial().setLocation(xi, yi);
				r.getpontoFinal().setLocation(xf, yf);
				desenharRetangulo(r, cor, vpg);

			}
		}

	}

	// *************************************************************************************
	// ROTACAO
	public void rotacao(Color cor, Graphics vpg, int opcao, Point pontoClique, boolean mapear, ViewPort viewPort) {
		if (mapear) {
			double ang = Double.parseDouble(JOptionPane.showInputDialog("Ângulo de Rotação em Graus:"));

			viewPort.ocultarDesenho();

			if (opcao == 0) { // referência: origem
				rotacionarOrigem(ang, cor, vpg, pontoClique, mapear, viewPort);

			} else { // referência: ponto qualquer
				rotacionarPonto(ang, cor, vpg, pontoClique, mapear, viewPort);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Não existem retângulos mapeados!");
		}
	}

	// ****************************************************************************
	// ROTAÇÃO REFERÊNCIA ORIGEM
	private void rotacionarOrigem(double ang, Color cor, Graphics vpg, Point pontoClique, boolean mapear,
			ViewPort viewPort) {
		if (mapear) {

			double teta = (ang * Math.PI) / 180;
			viewPort.ocultarDesenho();

			ArrayList<RetanguloRotacionado> tortos = new ArrayList<>();
			for (RetanguloRotacionado r : retangulosRotacionados) {
				RetanguloRotacionado rt = new RetanguloRotacionado(pontoClique, pontoClique, pontoClique, pontoClique);
				rt = rotacionaRetanguloRotacionado(r, cor, vpg, ang, new Point(0, 0));

				tortos.add(rt);
			}

			retangulosRotacionados.clear();
			retangulosRotacionados = tortos;

			double mr[][] = { { Math.cos(teta), Math.sin(-1 * teta), 0 }, { Math.sin(teta), Math.cos(teta), 0 },
					{ 0, 0, 1 } };

			for (Retangulo r : retangulosViewPort) {
				if (!r.isRotacionado()) {
					double x1, y1, x2, y2, x3, y3, x4, y4;
					Point p1, p2, p3, p4;

					// p1 inicio.x inicio.y
					double m1[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoInicial().getY());
					x1 = m1[0][0];
					y1 = m1[1][0];
					p1 = new Point((int) x1, (int) y1);

					// p2 fim.x e fim.y
					double m2[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoFinal().getY());
					x2 = m2[0][0];
					y2 = m2[1][0];
					p2 = new Point((int) x2, (int) y2);

					// p3 inicio.x e fim.y
					double m3[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoFinal().getY());
					x3 = m3[0][0];
					y3 = m3[1][0];
					p3 = new Point((int) x3, (int) y3);

					// p4 fim.x e inicio.y
					double m4[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoInicial().getY());
					x4 = m4[0][0];
					y4 = m4[1][0];
					p4 = new Point((int) x4, (int) y4);

					retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
					r.setRotacionado(true);
					r.setpontoInicial(p1);
					r.setpontoFinal(p2);

					// DESENHAR RETANGULO
					ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
					ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
					ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
					ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
				}
			}
		}
	}

	// ****************************************************************************
	// ROTAÇÃO REFERÊNCIA PONTO QUALQUER
	private void rotacionarPonto(double ang, Color cor, Graphics vpg, Point pontoClique, boolean mapear,
			ViewPort viewPort) {
		ArrayList<RetanguloRotacionado> tortos = new ArrayList<>();
		for (RetanguloRotacionado r : retangulosRotacionados) {
			RetanguloRotacionado rt = new RetanguloRotacionado(pontoClique, pontoClique, pontoClique, pontoClique);
			rt = rotacionaRetanguloRotacionado(r, cor, vpg, ang, pontoClique);
			tortos.add(rt);
		}

		retangulosRotacionados.clear();
		retangulosRotacionados = tortos;

		for (Retangulo r : retangulosViewPort) {
			if (!r.isRotacionado()) {
				retangulosRotacionados.add(rotacionarRetangulo(r, cor, vpg, ang, pontoClique));
				r.setRotacionado(true);
			}
		}
	}

	// ****************************************************************************
	// ROTAÇÃO MATRIZ
	private double[][] rotacionarMatriz(double origemX, double origemY, double x, double y, double teta) {
		double x_linha, y_linha, x_2linha, y_2linha;

		double t1[][] = { { 1, 0, -1 * origemX }, { 0, 1, -1 * origemY }, { 0, 0, 1 } };
		double mTranslacao1[][] = multiplicarMatrizPonto(t1, x, y);
		x_linha = mTranslacao1[0][0];
		y_linha = mTranslacao1[1][0];

		double r[][] = { { Math.cos(teta), Math.sin(-1 * teta), 0 }, { Math.sin(teta), Math.cos(teta), 0 },
				{ 0, 0, 1 } };
		double mRotacao[][] = multiplicarMatrizPonto(r, x_linha, y_linha);
		x_2linha = mRotacao[0][0];
		y_2linha = mRotacao[1][0];

		double t2[][] = { { 1, 0, origemX }, { 0, 1, origemY }, { 0, 0, 1 } };
		double mTranslacao2[][] = multiplicarMatrizPonto(t2, x_2linha, y_2linha);
		return mTranslacao2;
	}

	// ****************************************************************************
	// ROTACIONAR RETANGULO
	private RetanguloRotacionado rotacionarRetangulo(Retangulo r, Color c, Graphics g, double angulo, Point origem) {
		ctrReta = new ControlarReta();
		double teta = (angulo * Math.PI) / 180;

		Point p1 = new Point(), p2 = new Point(), p3 = new Point(), p4 = new Point();
		double x1, y1;

		// P1
		double mp1_1[][] = rotacionarMatriz(origem.getX(), origem.getY(), r.getpontoInicial().getX(),
				r.getpontoInicial().getY(), teta);
		double mp1_2[][] = rotacionarMatriz(origem.getY(), origem.getX(), r.getpontoInicial().getY(),
				r.getpontoInicial().getX(), teta);
		x1 = mp1_1[0][0];
		y1 = mp1_2[1][0];
		p1.setLocation(x1, y1);

		// P2
		double mp2_1[][] = rotacionarMatriz(origem.getX(), origem.getY(), r.getpontoFinal().getX(),
				r.getpontoFinal().getY(), teta);
		double mp2_2[][] = rotacionarMatriz(origem.getY(), origem.getX(), r.getpontoFinal().getY(),
				r.getpontoFinal().getX(), teta);
		x1 = mp2_1[0][0];
		y1 = mp2_2[1][0];
		p2.setLocation(x1, y1);

		// P3
		double mp3_1[][] = rotacionarMatriz(origem.getX(), origem.getY(), r.getpontoInicial().getX(),
				r.getpontoFinal().getY(), teta);
		double mp3_2[][] = rotacionarMatriz(origem.getY(), origem.getX(), r.getpontoFinal().getY(),
				r.getpontoInicial().getX(), teta);
		x1 = mp3_1[0][0];
		y1 = mp3_2[1][0];
		p3.setLocation(x1, y1);

		// P4
		double mp4_1[][] = rotacionarMatriz(origem.getX(), origem.getY(), r.getpontoFinal().getX(),
				r.getpontoInicial().getY(), teta);
		double mp4_2[][] = rotacionarMatriz(origem.getY(), origem.getX(), r.getpontoInicial().getY(),
				r.getpontoFinal().getX(), teta);
		x1 = mp4_1[0][0];
		y1 = mp4_2[1][0];
		p4.setLocation(x1, y1);

		// DESENHAR RETANGULO
		ctrReta.desenharReta(new Reta(p1, p4), c, g);
		ctrReta.desenharReta(new Reta(p1, p3), c, g);
		ctrReta.desenharReta(new Reta(p3, p2), c, g);
		ctrReta.desenharReta(new Reta(p2, p4), c, g);

		return new RetanguloRotacionado(p1, p2, p3, p4);
	}

	// ****************************************************************************
	// ROTACIONAR RETANGULO JA ROTACIONADO ANTERIORMENTE
	private RetanguloRotacionado rotacionaRetanguloRotacionado(RetanguloRotacionado r, Color c, Graphics g,
			double angulo, Point origem) {
		ctrReta = new ControlarReta();
		double teta = (angulo * Math.PI) / 180;

		Point p1 = new Point(), p2 = new Point(), p3 = new Point(), p4 = new Point();
		double x1, y1;

		x1 = origem.getX() + (r.getpontoInicial().getX() - origem.getX()) * Math.cos(teta)
				- (r.getpontoInicial().getY() - origem.getY()) * Math.sin(teta);
		y1 = origem.getY() + (r.getpontoInicial().getY() - origem.getY()) * Math.cos(teta)
				+ (r.getpontoInicial().getX() - origem.getX()) * Math.sin(teta);
		p1.setLocation(x1, y1);

		x1 = origem.getX() + (r.getpontoFinal().getX() - origem.getX()) * Math.cos(teta)
				- (r.getpontoFinal().getY() - origem.getY()) * Math.sin(teta);
		y1 = origem.getY() + (r.getpontoFinal().getY() - origem.getY()) * Math.cos(teta)
				+ (r.getpontoFinal().getX() - origem.getX()) * Math.sin(teta);
		p2.setLocation(x1, y1);

		x1 = origem.getX() + (r.getPonto3().getX() - origem.getX()) * Math.cos(teta)
				- (r.getPonto3().getY() - origem.getY()) * Math.sin(teta);
		y1 = origem.getY() + (r.getPonto3().getY() - origem.getY()) * Math.cos(teta)
				+ (r.getPonto3().getX() - origem.getX()) * Math.sin(teta);
		p3.setLocation(x1, y1);

		x1 = origem.getX() + (r.getPonto4().getX() - origem.getX()) * Math.cos(teta)
				- (r.getPonto4().getY() - origem.getY()) * Math.sin(teta);
		y1 = origem.getY() + (r.getPonto4().getY() - origem.getY()) * Math.cos(teta)
				+ (r.getPonto4().getX() - origem.getX()) * Math.sin(teta);
		p4.setLocation(x1, y1);

		ctrReta.desenharReta(new Reta(p1, p4), c, g);
		ctrReta.desenharReta(new Reta(p1, p3), c, g);
		ctrReta.desenharReta(new Reta(p3, p2), c, g);
		ctrReta.desenharReta(new Reta(p2, p4), c, g);

		return new RetanguloRotacionado(p1, p2, p3, p4);
	}

	// ****************************************************************************
	// TRANSFORMACOES SUCESSIVAS
	public void transformacoesSucessivas(String ordem, Color cor, Graphics vpg, int altura, int largura,
			ViewPort viewPort, boolean mapear) {

		ControlarViewPort controlarViewPort = new ControlarViewPort();

		if (ordem.equals("te")) { // translação -> escala
			te(altura, largura, cor, vpg, 1, controlarViewPort, viewPort);
		} else if (ordem.equals("et")) { // escala -> translação
			te(altura, largura, cor, vpg, 2, controlarViewPort, viewPort);
		} else if (ordem.equals("tr")) { // translação -> rotação
			tr(altura, largura, cor, vpg, 1, controlarViewPort, viewPort);
		} else if (ordem.equals("rt")) { // rotação -> translação
			tr(altura, largura, cor, vpg, 2, controlarViewPort, viewPort);
		} else if (ordem.equals("er")) { // escala -> rotação
			er(altura, largura, cor, vpg, 1, controlarViewPort, viewPort);
		} else if (ordem.equals("re")) { // rotação -> escala
			er(altura, largura, cor, vpg, 2, controlarViewPort, viewPort);
		} else if (ordem.equals("ter")) { // translação -> escala -> rotação
			t_e_r(altura, largura, cor, vpg, "ter", viewPort);
		} else if (ordem.equals("tre")) { // translação -> rotação -> escala
			t_e_r(altura, largura, cor, vpg, "tre", viewPort);
		} else if (ordem.equals("etr")) { // escala -> translação -> rotação
			t_e_r(altura, largura, cor, vpg, "etr", viewPort);
		} else if (ordem.equals("ert")) { // escala -> rotação -> translação
			t_e_r(altura, largura, cor, vpg, "ert", viewPort);
		} else if (ordem.equals("rte")) { // rotação -> translação -> escala
			t_e_r(altura, largura, cor, vpg, "rte", viewPort);
		} else if (ordem.equals("ret")) { // rotação -> escala -> translação
			t_e_r(altura, largura, cor, vpg, "ret", viewPort);
		} else if (ordem.equals("t")) { // translação
			translacao(cor, vpg, altura, largura, mapear, viewPort);
		} else if (ordem.equals("e")) { // escala
			controlarViewPort.escala = true;
			controlarViewPort.rotacao = false;
			int opcao = controlarViewPort.opcaoReferencia();
			if (opcao == 0) {
				escala(cor, vpg, altura, largura, mapear, viewPort, opcao, controlarViewPort.pontoClique);
			} else {
				JOptionPane.showMessageDialog(null, "Clique em um ponto na área de trabalho para ser a referência!");
				controlarViewPort.habilitarClique = true;
			}
		} else if (ordem.equals("r")) { // rotação
			controlarViewPort.escala = false;
			controlarViewPort.rotacao = true;
			int opcao = controlarViewPort.opcaoReferencia();
			if (opcao == 0) {
				rotacao(cor, vpg, opcao, controlarViewPort.pontoClique, mapear, viewPort);
			} else {
				JOptionPane.showMessageDialog(null, "Clique em um ponto na área de trabalho para ser a referência!");
				controlarViewPort.habilitarClique = true;
			}
		}
	}

	// ****************************************************************************
	// TRANSLACAO | ESCALA
	private void te(int altura, int largura, Color cor, Graphics vpg, int ordem, ControlarViewPort controlarViewPort,
			ViewPort viewPort) {
		int h = h(largura);
		int v = v(altura);
		double sx = sx(largura);
		double sy = sy(altura);
		viewPort.ocultarDesenho();
		double mt[][] = { { 1, 0, h }, { 0, 1, v }, { 0, 0, 1 } };
		double me[][] = { { sx, 0, 0 }, { 0, sy, 0 }, { 0, 0, 1 } };

		if (ordem == 1) { // translação -> escala
			for (Retangulo rec : retangulosViewPort) {
				double mresultantei[][] = multiplicar2MatrizesPonto(mt, me, rec.getpontoInicial());
				double mresultantef[][] = multiplicar2MatrizesPonto(mt, me, rec.getpontoFinal());

				rec.getpontoInicial().setLocation(mresultantei[0][0], mresultantei[1][0]);
				rec.getpontoFinal().setLocation(mresultantef[0][0], mresultantef[1][0]);

				desenharRetangulo(rec, cor, vpg);
			}
		} else { // escala -> translação
			for (Retangulo rec : retangulosViewPort) {
				double mresultantei[][] = multiplicar2MatrizesPonto(me, mt, rec.getpontoInicial());
				double mresultantef[][] = multiplicar2MatrizesPonto(me, mt, rec.getpontoFinal());

				rec.getpontoInicial().setLocation(mresultantei[0][0], mresultantei[1][0]);
				rec.getpontoFinal().setLocation(mresultantef[0][0], mresultantef[1][0]);

				desenharRetangulo(rec, cor, vpg);
			}
		}
	}

	// ****************************************************************************
	// TRANSLACAO | ROTAÇÃO
	private void tr(int altura, int largura, Color cor, Graphics vpg, int ordem, ControlarViewPort controlarViewPort,
			ViewPort viewPort) {
		int h = h(largura);
		int v = v(altura);
		double ang = Double.parseDouble(JOptionPane.showInputDialog("Ângulo de Rotação em Graus:"));
		viewPort.ocultarDesenho();
		double teta = (ang * Math.PI) / 180;

		double mt[][] = { { 1, 0, h }, { 0, 1, v }, { 0, 0, 1 } };
		double mr[][] = { { Math.cos(teta), Math.sin(-1 * teta), 0 }, { Math.sin(teta), Math.cos(teta), 0 },
				{ 0, 0, 1 } };

		if (ordem == 1) { // translação -> rotação
			for (Retangulo r : retangulosViewPort) {
				double x1, y1, x2, y2, x3, y3, x4, y4;
				Point p1, p2, p3, p4;

				// p1 inicio.x inicio.y
				double m1[][] = multiplicarMatrizPonto(mt, r.getpontoInicial().getX(), r.getpontoInicial().getY());
				double mr1[][] = multiplicar2Matrizes(mr, m1);

				x1 = mr1[0][0];
				y1 = mr1[1][0];
				p1 = new Point((int) x1, (int) y1);

				// p2 fim.x e fim.y
				double m2[][] = multiplicarMatrizPonto(mt, r.getpontoFinal().getX(), r.getpontoFinal().getY());
				double mr2[][] = multiplicar2Matrizes(mr, m2);
				x2 = mr2[0][0];
				y2 = mr2[1][0];
				p2 = new Point((int) x2, (int) y2);

				// p3 inicio.x e fim.y
				double m3[][] = multiplicarMatrizPonto(mt, r.getpontoInicial().getX(), r.getpontoFinal().getY());
				double mr3[][] = multiplicar2Matrizes(mr, m3);
				x3 = mr3[0][0];
				y3 = mr3[1][0];
				p3 = new Point((int) x3, (int) y3);

				// p4 fim.x e inicio.y
				double m4[][] = multiplicarMatrizPonto(mt, r.getpontoFinal().getX(), r.getpontoInicial().getY());
				double mr4[][] = multiplicar2Matrizes(mr, m4);
				x4 = mr4[0][0];
				y4 = mr4[1][0];
				p4 = new Point((int) x4, (int) y4);

				retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
				r.setRotacionado(true);
				r.setpontoInicial(p1);
				r.setpontoFinal(p2);

				// DESENHAR RETANGULO
				ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
				ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
				ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
				ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
			}
		} else { // rotação -> translação
			for (Retangulo r : retangulosViewPort) {
				double x1, y1, x2, y2, x3, y3, x4, y4;
				Point p1, p2, p3, p4;

				// p1 inicio.x inicio.y
				double m1[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoInicial().getY());
				double mr1[][] = multiplicar2Matrizes(mt, m1);

				x1 = mr1[0][0];
				y1 = mr1[1][0];
				p1 = new Point((int) x1, (int) y1);

				// p2 fim.x e fim.y
				double m2[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoFinal().getY());
				double mr2[][] = multiplicar2Matrizes(mt, m2);
				x2 = mr2[0][0];
				y2 = mr2[1][0];
				p2 = new Point((int) x2, (int) y2);

				// p3 inicio.x e fim.y
				double m3[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoFinal().getY());
				double mr3[][] = multiplicar2Matrizes(mt, m3);
				x3 = mr3[0][0];
				y3 = mr3[1][0];
				p3 = new Point((int) x3, (int) y3);

				// p4 fim.x e inicio.y
				double m4[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoInicial().getY());
				double mr4[][] = multiplicar2Matrizes(mt, m4);
				x4 = mr4[0][0];
				y4 = mr4[1][0];
				p4 = new Point((int) x4, (int) y4);

				retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
				r.setRotacionado(true);
				r.setpontoInicial(p1);
				r.setpontoFinal(p2);

				// DESENHAR RETANGULO
				ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
				ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
				ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
				ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
			}
		}
	}

	// ****************************************************************************
	// ESCALA | ROTAÇÃO
	// ESCALA | ROTAÇÃO
	private void er(int altura, int largura, Color cor, Graphics vpg, int ordem, ControlarViewPort controlarViewPort,
			ViewPort viewPort) {
		double sx = sx(largura);
		double sy = sy(altura);
		double ang = Double.parseDouble(JOptionPane.showInputDialog("Ângulo de Rotação em Graus:"));
		viewPort.ocultarDesenho();
		double teta = (ang * Math.PI) / 180;

		double me[][] = { { sx, 0, 0 }, { 0, sy, 0 }, { 0, 0, 1 } };
		double mr[][] = { { Math.cos(teta), Math.sin(-1 * teta), 0 }, { Math.sin(teta), Math.cos(teta), 0 },
				{ 0, 0, 1 } };

		if (ordem == 1) { // escala -> rotação
			for (Retangulo r : retangulosViewPort) {
				double x1, y1, x2, y2, x3, y3, x4, y4;
				Point p1, p2, p3, p4;

				// p1 inicio.x inicio.y
				double m1[][] = multiplicarMatrizPonto(me, r.getpontoInicial().getX(), r.getpontoInicial().getY());
				double mr1[][] = multiplicar2Matrizes(mr, m1);

				x1 = mr1[0][0];
				y1 = mr1[1][0];
				p1 = new Point((int) x1, (int) y1);

				// p2 fim.x e fim.y
				double m2[][] = multiplicarMatrizPonto(me, r.getpontoFinal().getX(), r.getpontoFinal().getY());
				double mr2[][] = multiplicar2Matrizes(mr, m2);
				x2 = mr2[0][0];
				y2 = mr2[1][0];
				p2 = new Point((int) x2, (int) y2);

				// p3 inicio.x e fim.y
				double m3[][] = multiplicarMatrizPonto(me, r.getpontoInicial().getX(), r.getpontoFinal().getY());
				double mr3[][] = multiplicar2Matrizes(mr, m3);
				x3 = mr3[0][0];
				y3 = mr3[1][0];
				p3 = new Point((int) x3, (int) y3);

				// p4 fim.x e inicio.y
				double m4[][] = multiplicarMatrizPonto(me, r.getpontoFinal().getX(), r.getpontoInicial().getY());
				double mr4[][] = multiplicar2Matrizes(mr, m4);
				x4 = mr4[0][0];
				y4 = mr4[1][0];
				p4 = new Point((int) x4, (int) y4);

				retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
				r.setRotacionado(true);
				r.setpontoInicial(p1);
				r.setpontoFinal(p2);

				// DESENHAR RETANGULO
				ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
				ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
				ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
				ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
			}
		} else { // rotação -> escala
			for (Retangulo r : retangulosViewPort) {
				double x1, y1, x2, y2, x3, y3, x4, y4;
				Point p1, p2, p3, p4;

				// p1 inicio.x inicio.y
				double m1[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoInicial().getY());
				double mr1[][] = multiplicar2Matrizes(me, m1);

				x1 = mr1[0][0];
				y1 = mr1[1][0];
				p1 = new Point((int) x1, (int) y1);

				// p2 fim.x e fim.y
				double m2[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoFinal().getY());
				double mr2[][] = multiplicar2Matrizes(me, m2);
				x2 = mr2[0][0];
				y2 = mr2[1][0];
				p2 = new Point((int) x2, (int) y2);

				// p3 inicio.x e fim.y
				double m3[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoFinal().getY());
				double mr3[][] = multiplicar2Matrizes(me, m3);
				x3 = mr3[0][0];
				y3 = mr3[1][0];
				p3 = new Point((int) x3, (int) y3);

				// p4 fim.x e inicio.y
				double m4[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoInicial().getY());
				double mr4[][] = multiplicar2Matrizes(me, m4);
				x4 = mr4[0][0];
				y4 = mr4[1][0];
				p4 = new Point((int) x4, (int) y4);

				retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
				r.setRotacionado(true);
				r.setpontoInicial(p1);
				r.setpontoFinal(p2);

				// DESENHAR RETANGULO
				ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
				ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
				ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
				ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
			}
		}
	}

	// ****************************************************************************
	// TRANSLAÇÃO | ESCALA | ROTAÇÃO
	// TRANSLACAO | ESCALA | ROTACAO
	private void t_e_r(int altura, int largura, Color cor, Graphics vpg, String opcao, ViewPort viewPort) {
		int h = h(largura);
		int v = v(altura);
		double sx = sx(largura);
		double sy = sy(altura);
		double ang = Double.parseDouble(JOptionPane.showInputDialog("Ângulo de Rotação em Graus:"));
		viewPort.ocultarDesenho();
		double teta = (ang * Math.PI) / 180;

		double mt[][] = { { 1, 0, h }, { 0, 1, v }, { 0, 0, 1 } };
		double me[][] = { { sx, 0, 0 }, { 0, sy, 0 }, { 0, 0, 1 } };
		double mr[][] = { { Math.cos(teta), Math.sin(-1 * teta), 0 }, { Math.sin(teta), Math.cos(teta), 0 },
				{ 0, 0, 1 } };

		if (opcao.equals("ter")) { // translação -> escala -> rotação
			for (Retangulo rec : retangulosViewPort) {
				ter(rec, mt, me, mr, cor, vpg);
			}
		} else if (opcao.equals("tre")) { // translação -> rotação -> escala
			for (Retangulo rec : retangulosViewPort) {
				tre(rec, mt, me, mr, cor, vpg);
			}
		} else if (opcao.equals("etr")) { // escala -> translação -> rotação
			for (Retangulo rec : retangulosViewPort) {
				etr(rec, mt, me, mr, cor, vpg);
			}
		} else if (opcao.equals("ert")) { // escala -> rotação -> translação
			for (Retangulo rec : retangulosViewPort) {
				ert(rec, mt, me, mr, cor, vpg);
			}
		} else if (opcao.equals("rte")) { // rotação -> translação -> escala
			for (Retangulo rec : retangulosViewPort) {
				rte(rec, mt, me, mr, cor, vpg);
			}
		} else if (opcao.equals("ret")) { // rotação -> escala -> translação
			for (Retangulo rec : retangulosViewPort) {
				ret(rec, mt, me, mr, cor, vpg);
			}
		}
	}

	// ****************************************************************************
	// TRANSLAÇÃO -> ESCALA -> ROTAÇÃO
	// TRANSLAÇÃO -> ESCALA -> ROTAÇÃO
	private void ter(Retangulo r, double mt[][], double me[][], double mr[][], Color cor, Graphics vpg) {
		double x1, y1, x2, y2, x3, y3, x4, y4;
		Point p1, p2, p3, p4;

		// p1 inicio.x inicio.y
		double mp1_1[][] = multiplicarMatrizPonto(mt, r.getpontoInicial().getX(), r.getpontoInicial().getY());
		double mp1_2[][] = multiplicar2Matrizes(me, mp1_1);
		double mp1_3[][] = multiplicar2Matrizes(mr, mp1_2);
		x1 = mp1_3[0][0];
		y1 = mp1_3[1][0];
		p1 = new Point((int) x1, (int) y1);

		// p2 fim.x e fim.y
		double mp2_1[][] = multiplicarMatrizPonto(mt, r.getpontoFinal().getX(), r.getpontoFinal().getY());
		double mp2_2[][] = multiplicar2Matrizes(me, mp2_1);
		double mp2_3[][] = multiplicar2Matrizes(mr, mp2_2);
		x2 = mp2_3[0][0];
		y2 = mp2_3[1][0];
		p2 = new Point((int) x2, (int) y2);

		// p3 inicio.x e fim.y
		double mp3_1[][] = multiplicarMatrizPonto(mt, r.getpontoInicial().getX(), r.getpontoFinal().getY());
		double mp3_2[][] = multiplicar2Matrizes(me, mp3_1);
		double mp3_3[][] = multiplicar2Matrizes(mr, mp3_2);
		x3 = mp3_3[0][0];
		y3 = mp3_3[1][0];
		p3 = new Point((int) x3, (int) y3);

		// p4 fim.x e inicio.y
		double mp4_1[][] = multiplicarMatrizPonto(mt, r.getpontoFinal().getX(), r.getpontoInicial().getY());
		double mp4_2[][] = multiplicar2Matrizes(me, mp4_1);
		double mp4_3[][] = multiplicar2Matrizes(mr, mp4_2);
		x4 = mp4_3[0][0];
		y4 = mp4_3[1][0];
		p4 = new Point((int) x4, (int) y4);

		retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
		r.setRotacionado(true);
		r.setpontoInicial(p1);
		r.setpontoFinal(p2);

		// DESENHAR RETANGULO
		ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
		ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
		ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
		ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
	}

	// ****************************************************************************
	// TRANSLAÇÃO -> ROTAÇÃO -> ESCALA
	// TRANSLAÇÃO -> ROTAÇÃO -> ESCALA
	private void tre(Retangulo r, double mt[][], double me[][], double mr[][], Color cor, Graphics vpg) {
		double x1, y1, x2, y2, x3, y3, x4, y4;
		Point p1, p2, p3, p4;

		// p1 inicio.x inicio.y
		double mp1_1[][] = multiplicarMatrizPonto(mt, r.getpontoInicial().getX(), r.getpontoInicial().getY());
		double mp1_2[][] = multiplicar2Matrizes(mr, mp1_1);
		double mp1_3[][] = multiplicar2Matrizes(me, mp1_2);
		x1 = mp1_3[0][0];
		y1 = mp1_3[1][0];
		p1 = new Point((int) x1, (int) y1);

		// p2 fim.x e fim.y
		double mp2_1[][] = multiplicarMatrizPonto(mt, r.getpontoFinal().getX(), r.getpontoFinal().getY());
		double mp2_2[][] = multiplicar2Matrizes(mr, mp2_1);
		double mp2_3[][] = multiplicar2Matrizes(me, mp2_2);
		x2 = mp2_3[0][0];
		y2 = mp2_3[1][0];
		p2 = new Point((int) x2, (int) y2);

		// p3 inicio.x e fim.y
		double mp3_1[][] = multiplicarMatrizPonto(mt, r.getpontoInicial().getX(), r.getpontoFinal().getY());
		double mp3_2[][] = multiplicar2Matrizes(mr, mp3_1);
		double mp3_3[][] = multiplicar2Matrizes(me, mp3_2);
		x3 = mp3_3[0][0];
		y3 = mp3_3[1][0];
		p3 = new Point((int) x3, (int) y3);

		// p4 fim.x e inicio.y
		double mp4_1[][] = multiplicarMatrizPonto(mt, r.getpontoFinal().getX(), r.getpontoInicial().getY());
		double mp4_2[][] = multiplicar2Matrizes(mr, mp4_1);
		double mp4_3[][] = multiplicar2Matrizes(me, mp4_2);
		x4 = mp4_3[0][0];
		y4 = mp4_3[1][0];
		p4 = new Point((int) x4, (int) y4);

		retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
		r.setRotacionado(true);
		r.setpontoInicial(p1);
		r.setpontoFinal(p2);

		// DESENHAR RETANGULO
		ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
		ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
		ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
		ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
	}

	// ***************************************************************************
	// ESCALA -> TRANSLAÇÃO -> ROTAÇÃO
	// ESCALA -> TRANSLAÇÃO -> ROTAÇÃO
	private void etr(Retangulo r, double mt[][], double me[][], double mr[][], Color cor, Graphics vpg) {
		double x1, y1, x2, y2, x3, y3, x4, y4;
		Point p1, p2, p3, p4;

		// p1 inicio.x inicio.y
		double mp1_1[][] = multiplicarMatrizPonto(me, r.getpontoInicial().getX(), r.getpontoInicial().getY());
		double mp1_2[][] = multiplicar2Matrizes(mt, mp1_1);
		double mp1_3[][] = multiplicar2Matrizes(mr, mp1_2);
		x1 = mp1_3[0][0];
		y1 = mp1_3[1][0];
		p1 = new Point((int) x1, (int) y1);

		// p2 fim.x e fim.y
		double mp2_1[][] = multiplicarMatrizPonto(me, r.getpontoFinal().getX(), r.getpontoFinal().getY());
		double mp2_2[][] = multiplicar2Matrizes(mt, mp2_1);
		double mp2_3[][] = multiplicar2Matrizes(mr, mp2_2);
		x2 = mp2_3[0][0];
		y2 = mp2_3[1][0];
		p2 = new Point((int) x2, (int) y2);

		// p3 inicio.x e fim.y
		double mp3_1[][] = multiplicarMatrizPonto(me, r.getpontoInicial().getX(), r.getpontoFinal().getY());
		double mp3_2[][] = multiplicar2Matrizes(mt, mp3_1);
		double mp3_3[][] = multiplicar2Matrizes(mr, mp3_2);
		x3 = mp3_3[0][0];
		y3 = mp3_3[1][0];
		p3 = new Point((int) x3, (int) y3);

		// p4 fim.x e inicio.y
		double mp4_1[][] = multiplicarMatrizPonto(me, r.getpontoFinal().getX(), r.getpontoInicial().getY());
		double mp4_2[][] = multiplicar2Matrizes(mt, mp4_1);
		double mp4_3[][] = multiplicar2Matrizes(mr, mp4_2);
		x4 = mp4_3[0][0];
		y4 = mp4_3[1][0];
		p4 = new Point((int) x4, (int) y4);

		retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
		r.setRotacionado(true);
		r.setpontoInicial(p1);
		r.setpontoFinal(p2);

		// DESENHAR RETANGULO
		ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
		ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
		ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
		ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
	}

	// ****************************************************************************
	// ESCALA -> ROTAÇÃO -> TRANSLAÇÃO
	// ESCALA -> ROTAÇÃO -> TRANSLAÇÃO
	private void ert(Retangulo r, double mt[][], double me[][], double mr[][], Color cor, Graphics vpg) {
		double x1, y1, x2, y2, x3, y3, x4, y4;
		Point p1, p2, p3, p4;

		// p1 inicio.x inicio.y
		double mp1_1[][] = multiplicarMatrizPonto(me, r.getpontoInicial().getX(), r.getpontoInicial().getY());
		double mp1_2[][] = multiplicar2Matrizes(mr, mp1_1);
		double mp1_3[][] = multiplicar2Matrizes(mt, mp1_2);
		x1 = mp1_3[0][0];
		y1 = mp1_3[1][0];
		p1 = new Point((int) x1, (int) y1);

		// p2 fim.x e fim.y
		double mp2_1[][] = multiplicarMatrizPonto(me, r.getpontoFinal().getX(), r.getpontoFinal().getY());
		double mp2_2[][] = multiplicar2Matrizes(mr, mp2_1);
		double mp2_3[][] = multiplicar2Matrizes(mt, mp2_2);
		x2 = mp2_3[0][0];
		y2 = mp2_3[1][0];
		p2 = new Point((int) x2, (int) y2);

		// p3 inicio.x e fim.y
		double mp3_1[][] = multiplicarMatrizPonto(me, r.getpontoInicial().getX(), r.getpontoFinal().getY());
		double mp3_2[][] = multiplicar2Matrizes(mr, mp3_1);
		double mp3_3[][] = multiplicar2Matrizes(mt, mp3_2);
		x3 = mp3_3[0][0];
		y3 = mp3_3[1][0];
		p3 = new Point((int) x3, (int) y3);

		// p4 fim.x e inicio.y
		double mp4_1[][] = multiplicarMatrizPonto(me, r.getpontoFinal().getX(), r.getpontoInicial().getY());
		double mp4_2[][] = multiplicar2Matrizes(mr, mp4_1);
		double mp4_3[][] = multiplicar2Matrizes(mt, mp4_2);
		x4 = mp4_3[0][0];
		y4 = mp4_3[1][0];
		p4 = new Point((int) x4, (int) y4);

		retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
		r.setRotacionado(true);
		r.setpontoInicial(p1);
		r.setpontoFinal(p2);

		// DESENHAR RETANGULO
		ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
		ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
		ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
		ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
	}

	// ****************************************************************************
	// ROTAÇÃO -> TRANSLAÇÃO -> ESCALA
	// ROTAÇÃO -> TRANSLAÇÃO -> ESCALA
	private void rte(Retangulo r, double mt[][], double me[][], double mr[][], Color cor, Graphics vpg) {
		double x1, y1, x2, y2, x3, y3, x4, y4;
		Point p1, p2, p3, p4;

		// p1 inicio.x inicio.y
		double mp1_1[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoInicial().getY());
		double mp1_2[][] = multiplicar2Matrizes(mt, mp1_1);
		double mp1_3[][] = multiplicar2Matrizes(me, mp1_2);
		x1 = mp1_3[0][0];
		y1 = mp1_3[1][0];
		p1 = new Point((int) x1, (int) y1);

		// p2 fim.x e fim.y
		double mp2_1[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoFinal().getY());
		double mp2_2[][] = multiplicar2Matrizes(mt, mp2_1);
		double mp2_3[][] = multiplicar2Matrizes(me, mp2_2);
		x2 = mp2_3[0][0];
		y2 = mp2_3[1][0];
		p2 = new Point((int) x2, (int) y2);

		// p3 inicio.x e fim.y
		double mp3_1[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoFinal().getY());
		double mp3_2[][] = multiplicar2Matrizes(mt, mp3_1);
		double mp3_3[][] = multiplicar2Matrizes(me, mp3_2);
		x3 = mp3_3[0][0];
		y3 = mp3_3[1][0];
		p3 = new Point((int) x3, (int) y3);

		// p4 fim.x e inicio.y
		double mp4_1[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoInicial().getY());
		double mp4_2[][] = multiplicar2Matrizes(mt, mp4_1);
		double mp4_3[][] = multiplicar2Matrizes(me, mp4_2);
		x4 = mp4_3[0][0];
		y4 = mp4_3[1][0];
		p4 = new Point((int) x4, (int) y4);

		retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
		r.setRotacionado(true);
		r.setpontoInicial(p1);
		r.setpontoFinal(p2);

		// DESENHAR RETANGULO
		ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
		ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
		ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
		ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
	}

	// ****************************************************************************
	// ROTAÇÃO -> ESCALA -> TRANSLAÇÃO
	// ROTAÇÃO -> ESCALA -> TRANSLAÇÃO
	private void ret(Retangulo r, double mt[][], double me[][], double mr[][], Color cor, Graphics vpg) {
		double x1, y1, x2, y2, x3, y3, x4, y4;
		Point p1, p2, p3, p4;

		// p1 inicio.x inicio.y
		double mp1_1[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoInicial().getY());
		double mp1_2[][] = multiplicar2Matrizes(me, mp1_1);
		double mp1_3[][] = multiplicar2Matrizes(mt, mp1_2);
		x1 = mp1_3[0][0];
		y1 = mp1_3[1][0];
		p1 = new Point((int) x1, (int) y1);

		// p2 fim.x e fim.y
		double mp2_1[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoFinal().getY());
		double mp2_2[][] = multiplicar2Matrizes(me, mp2_1);
		double mp2_3[][] = multiplicar2Matrizes(mt, mp2_2);
		x2 = mp2_3[0][0];
		y2 = mp2_3[1][0];
		p2 = new Point((int) x2, (int) y2);

		// p3 inicio.x e fim.y
		double mp3_1[][] = multiplicarMatrizPonto(mr, r.getpontoInicial().getX(), r.getpontoFinal().getY());
		double mp3_2[][] = multiplicar2Matrizes(me, mp3_1);
		double mp3_3[][] = multiplicar2Matrizes(mt, mp3_2);
		x3 = mp3_3[0][0];
		y3 = mp3_3[1][0];
		p3 = new Point((int) x3, (int) y3);

		// p4 fim.x e inicio.y
		double mp4_1[][] = multiplicarMatrizPonto(mr, r.getpontoFinal().getX(), r.getpontoInicial().getY());
		double mp4_2[][] = multiplicar2Matrizes(me, mp4_1);
		double mp4_3[][] = multiplicar2Matrizes(mt, mp4_2);
		x4 = mp4_3[0][0];
		y4 = mp4_3[1][0];
		p4 = new Point((int) x4, (int) y4);

		retangulosRotacionados.add(new RetanguloRotacionado(p1, p2, p3, p4));
		r.setRotacionado(true);
		r.setpontoInicial(p1);
		r.setpontoFinal(p2);

		// DESENHAR RETANGULO
		ctrReta.desenharReta(new Reta(p1, p4), cor, vpg);
		ctrReta.desenharReta(new Reta(p1, p3), cor, vpg);
		ctrReta.desenharReta(new Reta(p3, p2), cor, vpg);
		ctrReta.desenharReta(new Reta(p2, p4), cor, vpg);
	}

	// ****************************************************************************
	// MULTIPLICAR MATRIZES HOMOGÊNEAS PASSANDO O PONTO COMO PARÂMETRO
	// MULTIPLICAR AS MATRIZES HOMOGENEAS
	private double[][] multiplicarMatrizes(double m1[][], Point p) {

		double matrizResultante[][] = new double[3][1];

		double somai = 0.0;
		double m2[][] = { { p.getX() }, { p.getY() }, { 1 } };

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 1; j++) {
				somai = 0;
				for (int k = 0; k < 3; k++) {
					somai += m1[i][k] * m2[k][j];
				}
				matrizResultante[i][j] = somai;
			}
		}

		return matrizResultante;
	}

	// ****************************************************************************
	// MULTIPLICAR MATRIZES HOMOGÊNEAS PASSANDO O X,Y COMO PARÂMETRO
	// MULTIPLICAR AS MATRIZES HOMOGENEAS PASSANDO PONTO
	private double[][] multiplicarMatrizPonto(double m1[][], double x, double y) {

		double matrizResultante[][] = new double[3][1];
		double mc[][] = { { x }, { y }, { 1 } };
		double somai = 0.0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 1; j++) {
				somai = 0;
				for (int k = 0; k < 3; k++) {
					somai += m1[i][k] * mc[k][j];
				}
				matrizResultante[i][j] = somai;
			}
		}

		return matrizResultante;
	}

	// ****************************************************************************
	// MULTIPLICAR DUAS MATRIZES HOMOGÊNEAS
	// MULTIPLICAR DUAS MATRIZES HOMOGENEAS
	private double[][] multiplicar2Matrizes(double m1[][], double m2[][]) {
		double matrizResultante[][] = new double[3][1];
		double soma2 = 0.0;

		// m1[3][3] * m2[3][1] = matrizResultante[3][1]
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 1; j++) {
				soma2 = 0;
				for (int k = 0; k < 3; k++) {
					soma2 += m1[i][k] * m2[k][j];
				}
				matrizResultante[i][j] = soma2;
			}
		}

		return matrizResultante;

	}

	// ****************************************************************************
	// MULTIPLICAR DUAS MATRIZES HOMOGÊNEAS PASSANDO O PONTO COMO PARÂMETRO
	// MULTIPLICAR DUAS MATRIZES HOMOGENEAS PASSANDO PONTO
	private double[][] multiplicar2MatrizesPonto(double m1[][], double m2[][], Point p) {

		double matrizResultante[][] = new double[3][1];
		double matriz1[][] = new double[3][1];

		double soma1 = 0.0;
		double soma2 = 0.0;
		double mc[][] = { { p.getX() }, { p.getY() }, { 1 } }; // [3][1]

		// m1[3][3] * mc[3][1] = matriz1[3][1]
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 1; j++) {
				soma1 = 0;
				for (int k = 0; k < 3; k++) {
					soma1 += m1[i][k] * mc[k][j];
				}
				matriz1[i][j] = soma1;
			}
		}

		// m2[3][3] * matriz1[3][1] = matrizResultante[3][1]
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 1; j++) {
				soma2 = 0;
				for (int k = 0; k < 3; k++) {
					soma2 += m2[i][k] * matriz1[k][j];
				}
				matrizResultante[i][j] = soma2;
			}
		}

		return matrizResultante;
	}

	// ****************************************************************************
}