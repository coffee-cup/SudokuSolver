package org.liquidsoap;

import java.awt.Font;

import javax.swing.JFormattedTextField;

public class Node {
	private int num;
	private boolean solid = false;
	private int x, y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private JFormattedTextField field;

	public JFormattedTextField getField() {
		return field;
	}

	public void setField(JFormattedTextField field) {
		this.field = field;
	}

	public Node() {

	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;

		if (field.getValue() == null || field.getFont() != Sudoku.bold) {
			field.setFont(Sudoku.plain);
		}
		field.setValue(String.valueOf(this.num));
		field.repaint();
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}
}
