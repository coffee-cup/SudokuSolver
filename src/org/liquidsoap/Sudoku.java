package org.liquidsoap;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class Sudoku extends JFrame {
	private static final long serialVersionUID = 1L;

	private Solver solver;

	public static final Font plain = new Font("Arial", Font.PLAIN, 14);
	public static final Font bold = new Font("Arial", Font.BOLD, 14);

	private JPanel contentPane;
	private final JMenuBar menuBar = new JMenuBar();
	private final JPanel sudoPanel = new JPanel();
	private final JPanel buttonPanel = new JPanel();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenuItem quitMenuItem = new JMenuItem("Quit");
	public final JButton btnSolve = new JButton("Solve Puzzle");
	private final JProgressBar progressBar = new JProgressBar();
	private final Component rigidArea = Box
			.createRigidArea(new Dimension(0, 10));
	private final Component rigidArea_1 = Box.createRigidArea(new Dimension(0,
			10));
	public final JButton btnClear = new JButton("Clear Grid");
	private final Component rigidArea_2 = Box.createRigidArea(new Dimension(0,
			100));
	public final JButton btnRevert = new JButton("Revert Grid");
	private final Component rigidArea_3 = Box.createRigidArea(new Dimension(0,
			10));

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sudoku frame = new Sudoku();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Sudoku() {
		super.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("icon.png")));
		initComponents();
	}

	private void initComponents() {
		setTitle("Sudoku Solver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 325);

		setJMenuBar(menuBar);

		menuBar.add(fileMenu);
		quitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		fileMenu.add(quitMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		sudoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		sudoPanel.setPreferredSize(new Dimension(280, 280));

		contentPane.add(sudoPanel, BorderLayout.WEST);
		sudoPanel.setLayout(new GridLayout(1, 1));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setPreferredSize(new Dimension(200, 280));
		contentPane.add(buttonPanel, BorderLayout.EAST);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		buttonPanel.add(rigidArea_1);
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSolve.setEnabled(false);
				solver.solve();
				btnSolve.setEnabled(true);
			}
		});
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solver.clearGrid();
			}
		});
		btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);

		buttonPanel.add(btnClear);

		buttonPanel.add(rigidArea_3);
		btnRevert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solver.revertGrid();
			}
		});
		btnRevert.setAlignmentX(Component.CENTER_ALIGNMENT);

		buttonPanel.add(btnRevert);

		buttonPanel.add(rigidArea_2);
		btnSolve.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(btnSolve);

		buttonPanel.add(rigidArea);
		buttonPanel.add(progressBar);

		solver = new Solver(this);
		solver.setProgressBar(progressBar);
		sudoPanel.add(solver.getPanel());
	}
}
