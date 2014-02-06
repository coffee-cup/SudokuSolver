package org.liquidsoap;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Solver {
	private JPanel content = new JPanel();
	private JProgressBar progressBar;
	private Grid grid;
	private Sudoku parent;

	private ArrayList<Node> nodes;

	public static final int SIZE = 3;

	public Solver(Sudoku parent) {
		this.parent = parent;
		initComponets();
	}

	public void clearGrid() {
		nodes = null;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				JFormattedTextField f = grid.getField(i, j);
				f.setValue(null);
			}
		}
	}

	public void revertGrid() {
		loadNodes();
		if (nodes == null) {
			return;
		}
		for (Node n : nodes) {
			if (!n.isSolid()) {
				n.getField().setValue(null);
			}
		}
	}

	public void solve() {
		Thread solveThread = new SolveThread();
		solveThread.start();
	}

	class SolveThread extends Thread {
		public void run() {
			parent.btnSolve.setEnabled(false);
			parent.btnClear.setEnabled(false);
			parent.btnRevert.setEnabled(false);
			setTextEdit(false);
			progressBar.setIndeterminate(true);
			loadNodes();

			if (!startSolve()) {
				JOptionPane.showMessageDialog(null,
						"Grid could not be Solved!", "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}

			setTextEdit(true);
			progressBar.setValue(0);
			parent.btnSolve.setEnabled(true);
			parent.btnClear.setEnabled(true);
			parent.btnRevert.setEnabled(true);
		}
	}

	private void setTextEdit(boolean e) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				JFormattedTextField f = grid.getField(i, j);
				f.setEditable(e);
			}
		}
	}

	// Solve the puzzle
	private boolean startSolve() {
		boolean solved = false;
		int index = 0;
		int stuck = 0;
		int high = index;
		Node current = nodes.get(index);

		// check to see if valid placements of numbers at start
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!checkGrid(i, j)) {
					progressBar.setIndeterminate(false);
					solved = false;
					return (solved);
				}
			}
		}
		progressBar.setIndeterminate(false);
		progressBar.setMaximum(81);
		progressBar.setMinimum(0);

		/*
		 * Algorithm to solve the puzzle
		 */
		while (!solved && index >= 0) {
			if (!current.isSolid()) {
				if (current.getNum() <= 0) {
					current.setNum(1);
				}
				int num = current.getNum();
				if (num <= 9 && checkGrid(current.getX(), current.getY())) {
					index++;
				} else {
					num++;
					current.setNum(num);
					if (num > 9) {
						current.setNum(0);
						index--;
						if (index < 0) {
							solved = false;
							index = -1;
							continue;
						}
						while (nodes.get(index).isSolid()) {
							index--;
							if (index < 0) {
								solved = false;
								index = -1;
								continue;
							}
						}
						nodes.get(index).setNum(nodes.get(index).getNum() + 1);
					}
				}
			} else {
				if (!checkGrid(current.getX(), current.getY())) {
					index--;
				} else {
					index++;
				}
			}

			if (index > high) {
				stuck = 0;
				high = index;
				progressBar.setIndeterminate(false);
				progressBar.setValue(index);
			} else {
				stuck++;
			}

			if (stuck > 1000) {
				progressBar.setIndeterminate(true);
			}

			if (index < 0) {
				solved = false;
				index = -1;
				continue;
			} else if (index >= nodes.size()) {
				solved = true;
				continue;
			}
			current = nodes.get(index);
		}
		progressBar.setIndeterminate(false);
		progressBar.setValue(0);
		return (solved);
	}

	// Check if Grid is OK
	private boolean checkGrid(int x, int y) {
		boolean safe = false;
		if (checkBox(x, y) && checkHorLine(y) && checkVerLine(x)) {
			safe = true;
		}
		return (safe);
	}

	// Check Horizontal Lines of Grid
	private boolean checkHorLine(int y) {
		boolean safe = true;
		boolean[] row = new boolean[9];

		for (int i = 0; i < 9; i++) {
			int value = grid.getNumber(i, y) - 1;
			if (value >= 0) {
				if (row[value]) {
					safe = false;
					break;
				} else {
					row[value] = true;
				}
			}
		}
		return (safe);
	}

	// Check Vertical Lines of Grid
	private boolean checkVerLine(int x) {
		boolean safe = true;
		boolean[] col = new boolean[9];

		for (int i = 0; i < 9; i++) {
			int value = grid.getNumber(x, i) - 1;
			if (value >= 0) {
				if (col[value]) {
					safe = false;
					break;
				} else {
					col[value] = true;
				}
			}
		}
		return (safe);
	}

	// Check boxes of grid
	private boolean checkBox(int x, int y) {
		boolean safe = true;
		boolean[] row_col = new boolean[9];
		Box box = grid.getBox(x, y);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int value = box.getNum(i, j) - 1;
				if (value >= 0) {
					if (row_col[value]) {
						safe = false;
						break;
					} else {
						row_col[value] = true;
					}
				}
			}
		}
		return (safe);
	}

	// load up all of the text fields and numbers into the arraylist
	private void loadNodes() {
		nodes = new ArrayList<Node>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Node n = new Node();
				nodes.add(n);
				n.setX(i);
				n.setY(j);
				n.setField(grid.getField(i, j));
				int value = grid.getNumber(i, j);
				if (value > 0 && n.getField().getFont() == Sudoku.bold) {
					n.setNum(value);
					n.setSolid(true);
				}
			}
		}
	}

	private void initComponets() {
		content = new JPanel();
		content.setLayout(new GridLayout(1, 1));

		grid = new Grid();
		content.add(grid.getPanel());
	}

	public int getNumber(int x, int y) {
		return (grid.getNumber(x, y));
	}

	public void setProgressBar(JProgressBar progress) {
		this.progressBar = progress;
	}

	public JPanel getPanel() {
		return (content);
	}
}
