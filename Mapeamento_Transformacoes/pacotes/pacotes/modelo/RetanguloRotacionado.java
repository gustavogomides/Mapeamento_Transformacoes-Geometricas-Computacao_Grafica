package pacotes.modelo;

import java.awt.Point;

public class RetanguloRotacionado extends Retangulo {

	private Point ponto3, ponto4;

	public RetanguloRotacionado(Point pInicial, Point pFinal, Point ponto3, Point ponto4) {
		super(pInicial, pFinal);
		this.ponto3 = ponto3;
		this.ponto4 = ponto4;
	}

	public Point getPonto3() {
		return ponto3;
	}

	public void setPonto3(Point ponto3) {
		this.ponto3 = ponto3;
	}

	public Point getPonto4() {
		return ponto4;
	}

	public void setPonto4(Point ponto4) {
		this.ponto4 = ponto4;
	}

}
