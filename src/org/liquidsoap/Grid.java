package org.liquidsoap;

import java.awt.GridLayout;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

public class Grid {
	private JPanel content;
	private Box[][] boxes;

	public Grid() {
		initComponents();
	}

	private void initComponents() {
		content = new JPanel();
		content.setLayout(new GridLayout(3, 3, 3, 3));

		boxes = new Box[3][3];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Box b = new Box();
				boxes[i][j] = b;
				content.add(b.getPanel());
			}
		}
	}

	public Box getBox(int x, int y) {
		x /= 3;
		y /= 3;

		return (boxes[x][y]);
	}

	private JFormattedTextField getField(Box box, int x, int y) {
		while (x >= 3) {
			x -= 3;
		}
		while (y >= 3) {
			y -= 3;
		}

		return (box.getField(x, y));
	}

	public void setEnable(boolean e, int x, int y) {
		JFormattedTextField f = getField(getBox(x, y), x, y);
		f.setEditable(e);
	}

	public int getNumber(int x, int y) {
		int value = -1;

		JFormattedTextField f = getField(getBox(x, y), x, y);
		try {
			f.commitEdit();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (f.getValue() != null) {
			String s = (String) f.getValue();
			value = Integer.parseInt(s);
		}

		return (value);
	}

	public JFormattedTextField getField(int x, int y) {
		return (getField(getBox(x, y), x, y));
	}

	public JPanel getPanel() {
		return (content);
	}
}
